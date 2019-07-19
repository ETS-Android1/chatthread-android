package tk.nathanf.chatthread.components.messages.types;

import java.io.IOException;
import java.io.InputStream;
import java.lang.String;
import java.lang.RuntimeException;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.makeramen.roundedimageview.RoundedImageView;

import tk.nathanf.chatthread.activities.PreviewImage;
import tk.nathanf.chatthread.R;
import tk.nathanf.chatthread.components.Author;
import tk.nathanf.chatthread.components.Message;
import tk.nathanf.chatthread.components.MessageParameters;
import tk.nathanf.chatthread.components.MessageThreadListAdapter;

/**
 * Representation of an Image message.
 */
@SuppressWarnings("unused")
public final class ImageMessage extends Message {
    /**
     * The image to display.
     */
    private Bitmap image;

    /**
     * The name to use for the image when it is downloaded.
     */
    private String name;

    /**
     * Whether or not this Image has been loaded yet.
     */
    private boolean loaded = false;

    /**
     * Empty Constructor required for Parsing messages.
     * DO NOT USE THIS TO INSTANTIATE THIS CLASS.
     */
    public ImageMessage() {}

    /**
     * Create a ImageMessage.
     *
     * @param context The Context.
     * @param author  The Author.
     */
    public ImageMessage(Context context, Author author) {
        super(context, author);
    }

    /**
     * Set the Name of the Image for when it is downloaded.
     *
     * @param name The name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the Image.
     *
     * @param image The Image.
     */
    public void setImage(Bitmap image) {
        this.image = image;
        this.loaded = true;
    }

    /**
     * Set the Image based on a URL. This will load the image in. Once the image is loaded in,
     * if this ImageMessage is attached to an Adapter,
     * {@link MessageThreadListAdapter#notifyDataSetChanged()} will be called.
     *
     * @param url The URL.
     */
    public void setImage(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream input = new URL(url).openStream();
                    image = BitmapFactory.decodeStream(input);
                    Log.d("PPD", "Finished load of avatar");
                } catch (IOException e) {
                    image = null;
                    e.printStackTrace();
                } finally {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (getAdapter() != null) {
                                loaded = true;
                                getAdapter().notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * Create the View.
     *
     * @param parameters The Parameters.
     * @param parent     The Parent ViewGroup.
     *
     * @return The View.
     * @see Message#createView(MessageParameters, ViewGroup)
     * @throws RuntimeException if the Layout Inflater service cannot be reached.
     */
    @Override
    public View createView(final MessageParameters parameters, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)this.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) {
            throw new RuntimeException("Unable to bind to the Layout Inflater service.");
        }
        View view = inflater.inflate(R.layout.message_image, parent, false);
        RoundedImageView imageView = view.findViewById(R.id.previewImage);
        imageView.setImageBitmap(this.image);
        float[] rads = parameters.getMessageRadiusPx();
        imageView.setCornerRadius(rads[0]);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreviewImage.currentlyDisplayedImage = ImageMessage.this.image;
                PreviewImage.currentlyDisplayedImageName = ImageMessage.this.name;
                PreviewImage.currentlyDisplayedImageAuthor = ImageMessage.this.getAuthor();
                PreviewImage.currentlyDisplayedImageDate = parameters.getDateFormatter().format(
                    ImageMessage.this.getSentOn()
                );
                Intent intent = new Intent(getContext(), PreviewImage.previewActivity);
                getContext().startActivity(intent);
            }
        });

        return view;
    }

    /**
     * Bind this Message to a View.
     *
     * @param parameters The Parameters.
     * @param view       The View.
     * @see Message#bindView(MessageParameters, View)
     */
    @Override
    public void bindView(MessageParameters parameters, View view) {
        ConstraintLayout loadingLayout = view.findViewById(R.id.loadingLayout);
        RoundedImageView imageView = view.findViewById(R.id.previewImage);

        if (this.loaded) {
            loadingLayout.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(this.image);
        } else {
            loadingLayout.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);
        }

        imageView.requestLayout();
    }

    /**
     * Retrieve the Padding for this Message.
     *
     * @param parameters The Parameters.
     *
     * @return The float array of length four representing the padding
     *         in the order of Left, Top, Right, Bottom.
     * @see Message#getPadding(MessageParameters)
     */
    @Override
    public int[] getPadding(MessageParameters parameters) {
        int padding = parameters.getImageMessagePadding();
        return new int[] {padding, padding, padding, padding};
    }

    /**
     * Retrieve the minimum width for this Message Type.
     *
     * @param parameters The Parameters.
     *
     * @return The minimum width.
     * @see Message#getMinWidth(MessageParameters)
     */
    @Override
    public int getMinWidth(MessageParameters parameters) {
        return 0;
    }

    /**
     * Retrieve the minimum height for this Message Type.
     *
     * @param parameters The Parameters.
     *
     * @return The minimum height.
     * @see Message#getMinHeight(MessageParameters)
     */
    @Override
    public int getMinHeight(MessageParameters parameters) {
        return 0;
    }

    /**
     * Retrieve the corner radius for this Message Type.
     *
     * @param parameters The parameters.
     *
     * @return The corner radius in the order of top-from, top-to, bottom-from, bottom-to.
     * @see Message#getRadius(MessageParameters)
     */
    @Override
    public float[] getRadius(MessageParameters parameters) {
        float[] rads = parameters.getMessageRadiusPx();
        return new float[] {rads[0], rads[0], rads[0], rads[0]};
    }

    /**
     * Retrieve the Parse Priority for this Message Type.
     *
     * @return The parse priority.
     * @see Message#getParsePriority()
     */
    @Override
    public int getParsePriority() {
        return 0;
    }

    /**
     * Check if the value passed is parsable as this Message type.
     *
     * @param value The value.
     *
     * @return True if the value is parsable.
     * @see Message#isParsable(String)
     */
    @Override
    public boolean isParsable(String value) {
        String[] words = value.split(" ");
        for (String word : words) {
            if (Patterns.WEB_URL.matcher(word).matches()) {
                return (
                    word.endsWith("png") ||
                    word.endsWith("jpg") ||
                    word.endsWith("bmp") ||
                    word.endsWith("jpeg")
                );
            }
        }

        return false;
    }

    /**
     * Parse a value that has be validated with {@link Message#isParsable(String)} and return
     * the resulting Messages.
     *
     * @param context The context.
     * @param author  The Author.
     * @param value   The value.
     *
     * @return The Messages.
     * @see Message#parseMessage(Context, Author, String)
     */
    @Override
    public Message[] parseMessage(Context context, Author author, String value) {
        String[] words = value.split(" ");
        for (String word : words) {
            int end = value.indexOf(word) + word.length();
            if (Patterns.WEB_URL.matcher(word).matches()) {
                if (
                    word.endsWith("png") ||
                    word.endsWith("jpg") ||
                    word.endsWith("bmp") ||
                    word.endsWith("jpeg")
                ) {
                    //SpannableString sString = new SpannableString(value);
                    //sString.setSpan(new UnderlineSpan(), value.indexOf(word), end, 0);
                    if (! value.equals(word)) {
                        TextMessage textMessage = new TextMessage(context, author);
                        textMessage.setMessage(value);
                        ImageMessage imageMessage = new ImageMessage(context, author);
                        imageMessage.setName(Uri.parse(word).getLastPathSegment());
                        imageMessage.setImage(word);
                        return new Message[] {
                                textMessage, imageMessage
                        };
                    } else {
                        ImageMessage imageMessage = new ImageMessage(context, author);
                        imageMessage.setName(Uri.parse(word).getLastPathSegment());
                        imageMessage.setImage(word);
                        return new Message[] {
                            imageMessage
                        };
                    }
                }
            }
        }

        return new Message[] {};
    }
}
