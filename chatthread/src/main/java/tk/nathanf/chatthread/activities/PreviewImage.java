package tk.nathanf.chatthread.activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.chrisbanes.photoview.PhotoView;

import tk.nathanf.chatthread.R;
import tk.nathanf.chatthread.components.Author;

/**
 * Default Preview Image activity.
 */
@SuppressWarnings("unused")
public final class PreviewImage extends AppCompatActivity {
    /**
     * The currently displayed Bitmap.
     */
    public static Bitmap currentlyDisplayedImage;

    /**
     * The currently displayed image name.
     */
    public static String currentlyDisplayedImageName;

    /**
     * The currently displayed image Author.
     */
    public static Author currentlyDisplayedImageAuthor;

    /**
     * The currently displayed image date.
     */
    public static String currentlyDisplayedImageDate;

    /**
     * The default Activity class to use for Previewing Images.
     */
    public static Class<? extends Activity> previewActivity = PreviewImage.class;

    /**
     * Permission Request for Downloading Files.
     */
    private static final int DOWNLOAD_PERMISSION_REQUEST = 0b0101110110101111;

    /**
     * Create the Preview Image activity.
     *
     * @param savedInstanceState The saved Instance State.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_preview);

        PhotoView photoView = findViewById(R.id.previewImage);
        photoView.setImageBitmap(currentlyDisplayedImage);

        Button downloadButton = findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        TextView dateLabel = findViewById(R.id.dateLabel);
        dateLabel.setText(currentlyDisplayedImageDate);
    }

    /**
     * Check for the permission to download files to the device.
     */
    private void checkPermission() {
        int permissionCheck = ContextCompat
                .checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                DOWNLOAD_PERMISSION_REQUEST
            );
        } else {
            doDownload();
        }
    }

    /**
     * Handle the Permission Result.
     *
     * @param requestCode  The Result Code.
     * @param permissions  The Permissions.
     * @param grantResults The Grant Results.
     */
    @Override
    public void onRequestPermissionsResult(
        int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults
    ) {
        if (requestCode == DOWNLOAD_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doDownload();
            }
        }
    }

    /**
     * Download the File.
     */
    private void doDownload() {
        MediaStore.Images.Media.insertImage(
            getContentResolver(), currentlyDisplayedImage, currentlyDisplayedImageName, null
        );
        Toast.makeText(PreviewImage.this, "Image saved to device", Toast.LENGTH_SHORT).show();
    }
}
