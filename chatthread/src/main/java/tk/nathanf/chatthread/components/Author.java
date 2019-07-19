package tk.nathanf.chatthread.components;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.os.Handler;
import android.util.Log;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Author {
    /**
     * The currently loaded Avatar for the Author.
     */
    private @Nullable Bitmap avatar;

    /**
     * The source of this Message.
     */
    private @NonNull Message.Source source;

    /**
     * The name.
     */
    private @NonNull String name;

    /**
     * The Avatar URL.
     */
    private @Nullable String avatarUrl;

    /**
     * This flag will be set to true when the avatar is in the loading process.
     */
    private boolean avatarLoading = false;

    /**
     * Create the Author.
     *
     * @param source The Source.
     * @param name   The name.
     */
    @SuppressWarnings("WeakerAccess")
    public Author(@NonNull Message.Source source, @NonNull String name) {
        this.name = name;
        this.source = source;
    }

    /**
     * Create the Author.
     *
     * @param source The Source.
     * @param name   The name.
     * @param avatar The avatar.
     */
    @SuppressWarnings("unused")
    public Author(@NonNull Message.Source source, @NonNull String name, @NonNull Bitmap avatar) {
        this(source, name);
        this.avatar = avatar;
    }

    /**
     * Create the Author
     *
     * @param source    The Source.
     * @param name      The name.
     * @param avatarUrl The avatar URL.
     */
    public Author(@NonNull Message.Source source, @NonNull String name, @NonNull String avatarUrl) {
        this(source, name);
        this.avatarUrl = avatarUrl;
    }

    /**
     * Retrieve the source for the Author.
     *
     * @return The Source.
     */
    public @NonNull Message.Source getSource() {
        return this.source;
    }

    /**
     * Retrieve the name of the Author.
     *
     * @return The name.
     */
    public @NonNull String getName() {
        return this.name;
    }

    /**
     * Retrieve the URL to the Avatar of the Author. This will only be called
     * if, {@link Author#getAvatar(BaseAdapter)} returns null.
     * By default, this is called once to initially retrieve the Bitmap for
     * the Author. This Bitmap is then stored and subsequent calls to the
     * Author for their Avatar will use the value stored in
     * {@link Author#avatar}.
     *
     * @return The URL to the Authors Avatar.
     * @see Author#getAvatar(BaseAdapter)
     */
    @SuppressWarnings("WeakerAccess")
    public @Nullable String getAvatarUrl() {
        return this.avatarUrl;
    }

    /**
     * Retrieve the Avatar for the Author. By default, this returns null until
     * {@link Author#getAvatarUrl()} has been called and returned a non-null
     * value that was properly formatted into a Bitmap. If the avatar ends up
     * being loaded from a URL, once it is completed the adapter will have
     * {@link BaseAdapter#notifyDataSetChanged()} called to update the
     * relevant ImageViews.
     *
     * If you override this function, {@link Author#getAvatarUrl()} will never
     * be called.
     *
     * @param adapter The Adapter.
     *
     * @return The Avatar for the Member.
     * @see Author#getAvatarUrl()
     */
    @SuppressWarnings("WeakerAccess")
    public @Nullable Bitmap getAvatar(final BaseAdapter adapter) {
        if (this.avatar != null) {
            if (this.source == Message.Source.Other)
                Log.d("PPD", "Avatar already set");
            return this.avatar;
        }

        final String url = this.getAvatarUrl();
        if (url == null) {
            if (this.source == Message.Source.Other)
                Log.d("PPD", "URL Is null");
            return null;
        }

        if (this.avatarLoading) {
            if (this.source == Message.Source.Other)
                Log.d("PPD", "Avatar still loading");
            return this.avatar;
        }
        if (this.source == Message.Source.Other)
            Log.d("PPD", "Starting load of avatar");
        avatarLoading = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream input = new URL(url).openStream();
                    avatar = BitmapFactory.decodeStream(input);
                    Log.d("PPD", "Finished load of avatar");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (adapter != null) {
                                avatarLoading = false;
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        }).start();

        return this.avatar;
    }
}
