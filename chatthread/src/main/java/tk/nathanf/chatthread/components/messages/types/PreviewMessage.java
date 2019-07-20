package tk.nathanf.chatthread.components.messages.types;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Looper;
import android.os.Handler;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.lang.RuntimeException;
import java.lang.String;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.joooonho.SelectableRoundedImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import tk.nathanf.chatthread.components.Author;
import tk.nathanf.chatthread.components.MessageThreadListAdapter;
import tk.nathanf.chatthread.R;
import tk.nathanf.chatthread.components.Message;
import tk.nathanf.chatthread.components.MessageParameters;
import tk.nathanf.chatthread.util.Measure;

/**
 * Representation of a PreviewMessage.
 */
@SuppressWarnings("unused")
public final class PreviewMessage extends Message implements View.OnClickListener {
    /**
     * The Image.
     */
    private Bitmap image;

    /**
     * The Title.
     */
    private String title;

    /**
     * The Content.
     */
    private String content;

    /**
     * The URL.
     */
    private String url;

    /**
     * The Text.
     */
    private String text;

    /**
     * Whether or not this PreviewMessage is loaded yet.
     * @see PreviewMessage#loadAsync()
     */
    private boolean loaded = false;

    /**
     * Empty Constructor required for Parsing messages.
     * DO NOT USE THIS TO INSTANTIATE THIS CLASS.
     */
    public PreviewMessage() {}

    /**
     * Create a new PreviewMessage.
     *
     * @param context The Context.
     * @param author  The Author.
     */
    @SuppressWarnings("WeakerAccess")
    public PreviewMessage(Context context, Author author) {
        super(context, author);
    }

    /**
     * Set the Image.
     *
     * @param image The Image.
     */
    @SuppressWarnings("WeakerAccess")
    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     * Set the Title.
     *
     * @param title The title.
     */
    @SuppressWarnings("WeakerAccess")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set the Content.
     *
     * @param content The content.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Set the URL.
     *
     * @param url The URL.
     */
    @SuppressWarnings("WeakerAccess")
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Set the Text.
     *
     * @param text The text.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Retrieve the Image.
     *
     * @return The Image.
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * Retrieve the Title.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Retrieve the Content.
     *
     * @return The content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Retrieve the URL.
     *
     * @return The URL.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Retrieve the Text.
     *
     * @return The Text.
     */
    public String getText() {
        return text;
    }

    /**
     * Load the Message asynchronously based on the currently set URL.
     *
     * Once this is completed, if the Message has an Adapter set it will call
     * {@link MessageThreadListAdapter#notifyDataSetChanged()}.
     */
    private void loadAsync() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String path = new URI(PreviewMessage.this.url).getPath();
                    String title = path.substring(path.lastIndexOf("/") + 1);
                    String description = "Tap to open in browser";
                    Bitmap image = null;

                    // Try to load a frame from a video, if one exists.
                    MediaMetadataRetriever mediaMetadataRetriever = null;
                    try {
                        mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(url, new HashMap<String, String>());
                        image = mediaMetadataRetriever.getFrameAtTime();
                    } catch (Exception ignored) { } finally {
                        if (mediaMetadataRetriever != null) {
                            mediaMetadataRetriever.release();
                        }
                    }

                    // If no frame could be loaded since this was not a video,
                    // attempt to load the largest image from the URL.
                    if (image == null) {
                        Document document = Jsoup.connect(url).get();
                        if (url.contains("youtube.com/watch?v=")) {
                            String imageUrl = document
                                    .select("link[itemprop=thumbnailUrl]")
                                    .first()
                                    .absUrl("href");
                            InputStream input = new URL(imageUrl).openStream();
                            image = BitmapFactory.decodeStream(input);
                        } else {
                            Elements imgs = document.getElementsByTag("img");
                            Bitmap largestImage = null;
                            for (Element element : imgs) {
                                if (largestImage == null) {
                                    if (element.hasAttr("src")) {
                                        String absUrl = element.absUrl("src");
                                        InputStream input = new URL(absUrl).openStream();
                                        largestImage = BitmapFactory.decodeStream(input);
                                    }

                                    continue;
                                }

                                if (element.hasAttr("src")) {
                                    String absUrl = element.absUrl("src");
                                    InputStream input = new URL(absUrl).openStream();
                                    Bitmap newImage = BitmapFactory.decodeStream(input);
                                    if (
                                        newImage.getWidth() * newImage.getHeight() >
                                        largestImage.getWidth() * largestImage.getHeight()
                                    ) {
                                        largestImage = newImage;
                                    }
                                }
                            }

                            if (largestImage != null) {
                                image = largestImage;
                            }
                        }

                        title = document.title();
                        if (title == null || title.trim().length() < 1) {
                            title = url;
                        }

                        if (title.equals(url)) {
                            description = url;
                        } else {
                            description = document.body().text();
                            Elements metaElements = document.getElementsByTag("meta");
                            for (Element element : metaElements) {
                                if (element.hasAttr("name")) {
                                    if (element.attr("name").equalsIgnoreCase("description")) {
                                        description = element.attr("content");
                                    }
                                }
                            }
                        }
                    }

                    PreviewMessage.this.setImage(image);
                    PreviewMessage.this.setTitle(title);
                    PreviewMessage.this.setContent(description);
                } catch (Exception ignored) {}
                PreviewMessage.this.loaded = true;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        MessageThreadListAdapter adapter = PreviewMessage.this.getAdapter();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
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
    public View createView(MessageParameters parameters, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) {
            throw new RuntimeException("Unable to bind to the Layout Inflater service.");
        }
        View view = inflater.inflate(R.layout.messsage_preview, parent, false);
        TextView titleText = view.findViewById(R.id.titleText);
        TextView descriptionText = view.findViewById(R.id.descriptionText);
        TextView urlText = view.findViewById(R.id.urlText);
        SelectableRoundedImageView previewImage = view.findViewById(R.id.previewImage);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable()
            .setColorFilter(
                new PorterDuffColorFilter(
                    parameters.getProgressBarColor(), PorterDuff.Mode.SRC_IN
                )
            );
        previewImage.setCornerRadiiDP(0,0,0,0);

        if (this.getAuthor().getSource() == Source.Self) {
            titleText.setTextColor(parameters.getSentMessageTextColor());
            descriptionText.setTextColor(parameters.getSentMessageTextColor());
            urlText.setTextColor(parameters.getSentMessageTextColor());
        } else {
            titleText.setTextColor(parameters.getReceivedMessageTextColor());
            descriptionText.setTextColor(parameters.getReceivedMessageTextColor());
            urlText.setTextColor(parameters.getReceivedMessageTextColor());
        }

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
        SelectableRoundedImageView previewImage = view.findViewById(R.id.previewImage);
        TextView titleText = view.findViewById(R.id.titleText);
        TextView descriptionText = view.findViewById(R.id.descriptionText);
        TextView urlText = view.findViewById(R.id.urlText);
        ConstraintLayout loadingLayout = view.findViewById(R.id.loadingLayout);

        if (this.loaded) {
            previewImage.setVisibility(View.VISIBLE);
            titleText.setVisibility(View.VISIBLE);
            descriptionText.setVisibility(View.VISIBLE);
            urlText.setVisibility(View.VISIBLE);
            loadingLayout.setVisibility(View.INVISIBLE);
            if (this.image != null) {
                previewImage.getLayoutParams().width = 0;
                previewImage.getLayoutParams().height = (int)Measure.dpToPx(120, getContext());
                previewImage.setImageBitmap(this.image);
                float dp16 = Measure.dpToPx(16, getContext());
                ((ConstraintLayout.LayoutParams)titleText.getLayoutParams()).setMargins(
                    (int)dp16, (int)dp16, 0, 0
                );
                titleText.requestLayout();
            } else {
                previewImage.getLayoutParams().width = 0;
                previewImage.getLayoutParams().height = 0;
                float dp16 = Measure.dpToPx(16, getContext());
                ((ConstraintLayout.LayoutParams)titleText.getLayoutParams()).setMargins(
                    (int)dp16, 0, 0, 0
                );
                titleText.requestLayout();
            }
            titleText.setText(this.title);
            descriptionText.setText(this.content);
            urlText.setText(this.text);
            Linkify.addLinks(urlText, Linkify.ALL);
            urlText.setLinksClickable(true);
            if (this.content.equals(this.url) && this.title.equals(this.url)) {
                titleText.setVisibility(View.GONE);
                descriptionText.setVisibility(View.GONE);
            } else {
                titleText.setVisibility(View.VISIBLE);
                descriptionText.setVisibility(View.VISIBLE);
                if (this.title.equals(this.url)) {
                    titleText.setEllipsize(TextUtils.TruncateAt.END);
                    titleText.setMaxLines(2);
                } else {
                    titleText.setEllipsize(null);
                    titleText.setMaxLines(Integer.MAX_VALUE);
                }
            }
        } else {
            previewImage.setVisibility(View.INVISIBLE);
            titleText.setVisibility(View.INVISIBLE);
            descriptionText.setVisibility(View.INVISIBLE);
            urlText.setVisibility(View.INVISIBLE);
            loadingLayout.setVisibility(View.VISIBLE);
        }

        titleText.setOnClickListener(this);
        urlText.setOnClickListener(this);
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
        int padding = parameters.getPreviewMessagePadding();
        return new int[] {padding, padding, padding, padding};
    }

    /**
     * Handle a Click event for the Message.
     *
     * @param view The view that was Clicked.
     */
    @Override
    public void onClick(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.url));
        getContext().startActivity(browserIntent);
    }

    /**
     * Retrieve the Parse Priority for this Message Type.
     *
     * @return The parse priority.
     * @see Message#getParsePriority()
     */
    @Override
    public int getParsePriority() {
        return 1;
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
                return true;
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
     * @see Message#parseMessage(Context, Author, Date, String)
     */
    @Override
    public Message[] parseMessage(Context context, Author author, Date sentOn, String value) {
        String[] words = value.split(" ");
        for (String word : words) {
            if (Patterns.WEB_URL.matcher(word).matches()) {
                PreviewMessage message = new PreviewMessage(context, author);
                message.setTitle(word);
                message.setContent("Tap to open in browser");
                message.setUrl(word);
                message.setText(value);
                message.setSentOn(sentOn);
                message.loadAsync();
                return new Message[]{message};
            }
        }

        return new Message[] {};
    }
}
