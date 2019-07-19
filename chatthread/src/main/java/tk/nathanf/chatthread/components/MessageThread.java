package tk.nathanf.chatthread.components;

import android.content.Context;
import android.content.res.TypedArray;

import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ListView;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import tk.nathanf.chatthread.R;
import tk.nathanf.chatthread.components.dates.DefaultMessageDateFormatter;
import tk.nathanf.chatthread.components.dates.MessageDateFormatter;
import tk.nathanf.chatthread.util.Measure;
import tk.nathanf.chatthread.components.messages.types.TextMessage;
import tk.nathanf.chatthread.components.messages.types.ImageMessage;
import tk.nathanf.chatthread.components.messages.types.PreviewMessage;

/**
 * Used to represent a Message Thread.
 */
@SuppressWarnings("unused")
public final class MessageThread extends ConstraintLayout {
    /**
     * The List View for the Message Thread.
     */
    ListView messageThreadView;

    /**
     * The loaded Parameters.
     */
    MessageParameters parameters;

    /**
     * Create a new Message Thread.
     *
     * @param context The Context.
     */
    public MessageThread(Context context) {
        super(context);
        this.init(context, null, 0);
    }

    /**
     * Create a new Message Thread.
     *
     * @param context The Context.
     * @param attrs   The attributes
     */
    public MessageThread(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs, 0);
    }

    /**
     * Create a new Message Thread.
     *
     * @param context      The Context.
     * @param attrs        The attributes.
     * @param defStyleAttr The default style attribute.
     */
    public MessageThread(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs, defStyleAttr);
    }

    /**
     * Initialize the Message Thread.
     *
     * @param context      The Context.
     * @param attrs        The attributes.
     * @param defStyleAttr The default style attribute.
     */
    private void init(
        @NonNull Context context,
        @Nullable AttributeSet attrs,
        @SuppressWarnings("unused") int defStyleAttr
    ) {
        TypedArray typedArray;
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.MessageThread);
        this.initializeParameters(typedArray);
        typedArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert(inflater != null);
        inflater.inflate(R.layout.message_thread, this, true);
        messageThreadView = findViewById(R.id.message_thread);
    }

    /**
     * Set the Adapter for this Message Thread.
     *
     * @param adapter The Adapter.
     */
    public void setAdapter(MessageThreadListAdapter adapter) {
        adapter.setOwner(this);
        this.messageThreadView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        this.scrollToBottom();
    }

    /**
     * Scroll the Thread to the Bottom.
     */
    public void scrollToBottom() {
        messageThreadView.post(new Runnable() {
            @Override
            public void run() {
                messageThreadView.setSelection(messageThreadView.getAdapter().getCount() - 1);
            }
        });
    }

    /**
     * Set the Adapter for this Message Thread.
     *
     * @param adapter The adapter.
     */
    public void setAdapter(MessageThreadAdapter adapter) {
        this.setAdapter(new MessageThreadListAdapter(adapter));
    }

    /**
     * Retrieve the Adapter.
     *
     * @return The Adapter.
     */
    public MessageThreadListAdapter getAdapter() {
        return (MessageThreadListAdapter)this.messageThreadView.getAdapter();
    }

    /**
     * Initialize the Parameters.
     *
     * @param typedArray The TypedArray storing the Parameters.
     */
    private void initializeParameters(TypedArray typedArray) {
        float dp3 = Measure.dpToPx(16, getContext());
        float elevation = typedArray.getDimension(
            R.styleable.MessageThread_mt_elevation,
            dp3
        );

        float dp16 = Measure.dpToPx(16, getContext());
        float messageRadiusTopFrom = typedArray.getDimension(
            R.styleable.MessageThread_mt_message_radius_top_from,
            dp16
        );
        float messageRadiusTopTo = typedArray.getDimension(
            R.styleable.MessageThread_mt_message_radius_top_to,
            dp16
        );
        float messageRadiusBottomFrom = typedArray.getDimension(
            R.styleable.MessageThread_mt_message_radius_bottom_from,
            dp16
        );
        float messageRadiusBottomTo = typedArray.getDimension(
            R.styleable.MessageThread_mt_message_radius_bottom_to,
            dp16
        );

        int textMessagePadding = (int)typedArray.getDimension(
            R.styleable.MessageThread_mt_text_message_padding,
            dp16
        );

        int imageMessagePadding = (int)typedArray.getDimension(
            R.styleable.MessageThread_mt_image_message_padding,
            0
        );

        int previewMessagePadding = (int)typedArray.getDimension(
            R.styleable.MessageThread_mt_preview_message_padding,
            0
        );

        int receivedColor = typedArray.getColor(
            R.styleable.MessageThread_mt_received_color,
            getContext().getResources().getColor(R.color.default_received)
        );

        int sentColor = typedArray.getColor(
            R.styleable.MessageThread_mt_sent_color,
            getContext().getResources().getColor(R.color.default_sent)
        );

        int receivedTextColor = typedArray.getColor(
            R.styleable.MessageThread_mt_received_text_color,
            getContext().getResources().getColor(R.color.default_received_text)
        );

        int sentTextColor = typedArray.getColor(
            R.styleable.MessageThread_mt_sent_text_color,
            getContext().getResources().getColor(R.color.default_sent_text)
        );

        int dateColor = typedArray.getColor(
            R.styleable.MessageThread_mt_date_color,
            getContext().getResources().getColor(R.color.default_date_color)
        );

        boolean displayOutgoingAvatars = typedArray.getBoolean(
            R.styleable.MessageThread_mt_display_outgoing_avatars,
            false
        );

        boolean displayIncomingAvatars = typedArray.getBoolean(
            R.styleable.MessageThread_mt_display_incoming_avatars,
            true
        );

        int avatarScale = typedArray.getInt(R.styleable.MessageThread_mt_avatar_scale, 40);
        int avatarShape = typedArray.getInt(R.styleable.MessageThread_mt_avatar_shape, 0);

        int progressBarColor = typedArray.getColor(
            R.styleable.MessageThread_mt_progress_bar_color,
            getContext().getResources().getColor(R.color.default_progress_bar_color)
        );

        boolean allow_minutes = typedArray.getBoolean(
            R.styleable.MessageThread_mt_date_format_minutes, true
        );

        boolean allow_days = typedArray.getBoolean(
            R.styleable.MessageThread_mt_date_format_days, true
        );

        String dateFormat = typedArray.getString(
            R.styleable.MessageThread_mt_date_format
        );
        if (dateFormat == null) {
            dateFormat = "MMM d, yyyy 'at' h:mm aa";
        }

        int dateFlags = MessageDateFormatter.FLAG_ALL;

        if (!allow_minutes) {
            dateFlags -= MessageDateFormatter.FLAG_MINUTES;
        }

        if (!allow_days) {
            dateFlags -= MessageDateFormatter.FLAG_DAYS;
        }

        MessageDateFormatter formatter = new DefaultMessageDateFormatter(dateFormat, dateFlags);

        parameters = new MessageParameters(
            getContext(), sentColor, sentTextColor, receivedColor, receivedTextColor, dateColor,
            messageRadiusTopFrom, messageRadiusBottomFrom, messageRadiusTopTo,
            messageRadiusBottomTo, textMessagePadding, imageMessagePadding,
            previewMessagePadding, progressBarColor, elevation, avatarScale, avatarShape,
            displayIncomingAvatars, displayOutgoingAvatars, formatter
        );
    }

    /**
     * Set the Date Formatter for this Message Thread.
     *
     * @param formatter The Date Formatter.
     */
    public void setDateFormatter(MessageDateFormatter formatter) {
        this.parameters.dateFormatter = formatter;
    }

    /**
     * Set the background color for OUTGOING messages.
     *
     * @param sentColor The Color.
     */
    public void setSentColor(@ColorInt int sentColor) {
        this.parameters.sentColor = sentColor;
    }

    /**
     * Set the Text color for OUTGOING messages.
     *
     * @param sentMessageTextColor The Text Color.
     */
    public void setSentMessageTextColor(@ColorInt int sentMessageTextColor) {
        this.parameters.sentMessageTextColor = sentMessageTextColor;
    }

    /**
     * Set the background color for INCOMING messages.
     *
     * @param receivedColor The background color.
     */
    public void setReceivedColor(@ColorInt int receivedColor) {
        this.parameters.receivedColor = receivedColor;
    }

    /**
     * Set the Text color for INCOMING messages.
     *
     * @param receivedMessageTextColor The Text color.
     */
    public void setReceivedMessageTextColor(@ColorInt int receivedMessageTextColor) {
        this.parameters.receivedMessageTextColor = receivedMessageTextColor;
    }

    /**
     * Set the Text color for Dates.
     *
     * @param dateColor The text color.
     */
    public void setDateColor(@ColorInt int dateColor) {
        this.parameters.dateColor = dateColor;
    }

    /**
     * Set the Padding for Text Messages.
     *
     * @param textMessagePadding The padding.
     * @see TextMessage
     */
    public void setTextMessagePadding(@Px int textMessagePadding) {
        this.parameters.textMessagePadding = textMessagePadding;
    }

    /**
     * Set the padding for Image messages.
     *
     * @param imageMessagePadding The padding.
     * @see ImageMessage
     */
    public void setImageMessagePadding(@Px int imageMessagePadding) {
        this.parameters.imageMessagePadding = imageMessagePadding;
    }

    /**
     * Set the padding for Preview messages.
     *
     * @param previewMessagePadding The padding.
     * @see PreviewMessage
     */
    public void setPreviewMessagePadding(@Px int previewMessagePadding) {
        this.parameters.previewMessagePadding = previewMessagePadding;
    }

    /**
     * Set the color for Progress bars when certain message types are in the
     * Loading state before the content has been loaded.
     *
     * @param progressBarColor The color.
     */
    public void setProgressBarColor(@ColorInt int progressBarColor) {
        this.parameters.progressBarColor = progressBarColor;
    }

    /**
     * Set the Elevation for Messages.
     *
     * @param elevation The elevation.
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public void setElevation(float elevation) {
        this.parameters.elevation = elevation;
    }

    /**
     * Set the Scale for the Avatars.
     *
     * @param scale The scale.
     */
    public void setAvatarScale(MessageParameters.AvatarScale scale) {
        this.parameters.avatarScale = scale.getValue();
    }

    /**
     * Set the Scale for the Avatars.
     *
     * @param scale The scale.
     */
    public void setAvatarScale(@Dimension int scale) {
        this.parameters.avatarScale = scale;
    }

    /**
     * Set the shape for the Avatars.
     *
     * @param shape The shape.
     */
    public void setAvatarShape(MessageParameters.AvatarShape shape) {
        this.parameters.avatarShape = shape.getValue();
    }

    /**
     * Set whether or not to display outgoing avatars.
     *
     * @param value True to display outgoing avatars.
     */
    public void setDisplayOutgoingAvatars(boolean value) {
        this.parameters.displayOutgoingAvatars = value;
    }

    /**
     * Set whether or not to display incoming avatars.
     *
     * @param value True to display incoming avatars.
     */
    public void setDisplayIncomingAvatars(boolean value) {
        this.parameters.displayIncomingAvatars = value;
    }

    /**
     * Set the Radius for messages.
     *
     * @param topFrom    The TOP FROM corner.
     * @param topTo      The TOP TO corner.
     * @param bottomTo   The BOTTOM TO corner.
     * @param bottomFrom The BOTTOM FROM corner.
     */
    public void setMessageRadiusPx(float topFrom, float topTo, float bottomTo, float bottomFrom) {
        this.parameters.setMessageRadiusPx(topFrom, topTo, bottomTo, bottomFrom);
    }

    /**
     * Retrieve the Text color for OUTGOING messages.
     *
     * @return The background color.
     */
    public @ColorInt int getSentMessageTextColor() {
        return this.parameters.sentMessageTextColor;
    }

    /**
     * Retrieve the Text color for INCOMING messages.
     *
     * @return The background color.
     */
    public @ColorInt int getReceivedMessageTextColor() {
        return this.parameters.receivedMessageTextColor;
    }

    /**
     * Retrieve the default Radius for messages in px.
     *
     * @return The default corner radius in the order of
     *         top-from, top-to, bottom-from, bottom-to.
     */
    public float[] getMessageRadiusPx() {
        return this.parameters.getMessageRadiusPx();
    }

    /**
     * Retrieve the default Radius for messages in dp.
     *
     * @return The default corner radius in the order of
     *         top-from, top-to, bottom-from, bottom-to.
     */
    public float[] getMessageRadiusDp() {
        return this.parameters.getMessageRadiusDp();
    }

    /**
     * Retrieve the color for dates.
     *
     * @return The color for dates.
     */
    public @ColorInt int getDateColor() {
        return this.parameters.dateColor;
    }

    /**
     * Retrieve the padding for Text Messages.
     *
     * @return The padding.
     */
    public @Px int getTextMessagePadding() {
        return this.parameters.textMessagePadding;
    }

    /**
     * Retrieve the padding for Image Messages.
     *
     * @return The padding.
     */
    public @Px int getImageMessagePadding() {
        return this.parameters.imageMessagePadding;
    }

    /**
     * Retrieve the padding for Preview Messages.
     *
     * @return The padding.
     */
    public @Px int getPreviewMessagePadding() {
        return this.parameters.previewMessagePadding;
    }

    /**
     * Retrieve the color for Progress Bars.
     *
     * @return The color.
     */
    public @Px int getProgressBarColor() {
        return this.parameters.progressBarColor;
    }

    /**
     * Retrieve the Date Formatter.
     *
     * @return The date formatter.
     */
    public MessageDateFormatter getDateFormatter() {
        return this.parameters.dateFormatter;
    }

    /**
     * Retrieve the background color for OUTGOING messages.
     *
     * @return The color.
     */
    public @ColorInt int getSentColor() {
        return this.parameters.sentColor;
    }

    /**
     * Retrieve the background color for INCOMING messages.
     *
     * @return The color.
     */
    public @ColorInt int getReceivedColor() {
        return this.parameters.receivedColor;
    }

    /**
     * Retrieve the Elevation for messages.
     *
     * @return The elevation.
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public float getElevation() {
        return this.parameters.elevation;
    }

    /**
     * Retrieve the Scale for Avatars in Pixels.
     *
     * @return The scale.
     */
    public @Px int getAvatarScale() {
        return this.parameters.avatarScale;
    }

    /**
     * Retrieve the Shape of avatars.
     *
     * @return The shape.
     */
    public MessageParameters.AvatarShape getAvatarShape() {
        return parameters.getAvatarShape();
    }

    /**
     * @return true if outgoing avatars should be displayed, false otherwise.
     */
    public boolean shouldDisplayOutgoingAvatars() {
        return this.parameters.displayOutgoingAvatars;
    }

    /**
     * @return true if incoming avatars should be displayed, false otherwise.
     */
    public boolean shouldDisplayIncomingAvatars() {
        return this.parameters.displayIncomingAvatars;
    }
}
