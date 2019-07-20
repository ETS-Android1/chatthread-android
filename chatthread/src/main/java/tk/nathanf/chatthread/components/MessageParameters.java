package tk.nathanf.chatthread.components;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Px;
import androidx.annotation.RequiresApi;

import tk.nathanf.chatthread.components.dates.MessageDateFormatter;
import tk.nathanf.chatthread.util.Measure;

/**
 * Representation of Message Parameters.
 */
@SuppressWarnings("WeakerAccess")
public final class MessageParameters {

    /**
     * Representation of Avatar Scale.
     */
    @SuppressWarnings("unused")
    public enum AvatarScale {
        Small(40),
        Medium(50),
        Large(60);

        private int value;
        AvatarScale(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Representation of AvatarShape.
     */
    public enum AvatarShape {
        Circle(0),
        Square(1),
        RoundSquare(2);

        private int shape;
        AvatarShape(int shape) {
            this.shape = shape;
        }

        int getValue() {
            return this.shape;
        }

        public static AvatarShape valueOf(int value) {
            if (value == 0) {
                return AvatarShape.Circle;
            } else if (value == 1) {
                return AvatarShape.Square;
            } else if (value == 2) {
                return AvatarShape.RoundSquare;
            }

            return null;
        }
    }

    private Context context;
    int sentColor;
    int sentMessageTextColor;
    int receivedColor;
    int receivedMessageTextColor;
    int dateColor;
    float messageRadiusTopFrom;
    float messageRadiusTopTo;
    float messageRadiusBottomFrom;
    float messageRadiusBottomTo;
    int textMessagePadding;
    int imageMessagePadding;
    int previewMessagePadding;
    int progressBarColor;
    float elevation;
    int avatarScale;
    int avatarShape;
    boolean displayOutgoingAvatars;
    boolean displayIncomingAvatars;
    MessageDateFormatter dateFormatter;
    boolean dateHeaderEnabled;
    int dateHeaderColor;
    int dateHeaderSeparationMinutes;
    Typeface messageFont;
    Typeface dateFont;
    Typeface dateHeaderFont;
    float messageFontSize;
    float dateFontSize;
    float dateHeaderFontSize;

    /**
     * Create the new Message Parameters.
     */
    public MessageParameters(
            Context context,
            int sentColor,
            int sentMessageTextColor,
            int receivedColor,
            int receivedMessageTextColor,
            int dateColor,
            float messageRadiusTopFrom,
            float messageRadiusBottomFrom,
            float messageRadiusTopTo,
            float messageRadiusBottomTo,
            int textMessagePadding,
            int imageMessagePadding,
            int previewMessagePadding,
            int progressBarColor,
            float elevation,
            int avatarScale,
            int avatarShape,
            boolean displayIncomingAvatars,
            boolean displayOutgoingAvatars,
            boolean dateHeaderEnabled,
            int dateHeaderColor,
            int dateHeaderSeparationMinutes,
            Typeface messageFont,
            Typeface dateFont,
            Typeface dateHeaderFont,
            float messageFontSize,
            float dateFontSize,
            float dateHeaderFontSize,
            MessageDateFormatter dateFormatter
    ) {
        this.context = context;
        this.sentColor = sentColor;
        this.sentMessageTextColor = sentMessageTextColor;
        this.receivedColor = receivedColor;
        this.receivedMessageTextColor = receivedMessageTextColor;
        this.dateColor = dateColor;
        this.messageRadiusTopFrom = messageRadiusTopFrom;
        this.messageRadiusTopTo = messageRadiusTopTo;
        this.messageRadiusBottomFrom = messageRadiusBottomFrom;
        this.messageRadiusBottomTo = messageRadiusBottomTo;
        this.textMessagePadding = textMessagePadding;
        this.imageMessagePadding = imageMessagePadding;
        this.previewMessagePadding = previewMessagePadding;
        this.progressBarColor = progressBarColor;
        this.dateFormatter = dateFormatter;
        this.elevation = elevation;
        this.avatarScale = avatarScale;
        this.avatarShape = avatarShape;
        this.displayIncomingAvatars = displayIncomingAvatars;
        this.displayOutgoingAvatars = displayOutgoingAvatars;
        this.dateHeaderEnabled = dateHeaderEnabled;
        this.dateHeaderColor = dateHeaderColor;
        this.dateHeaderSeparationMinutes = dateHeaderSeparationMinutes;
        this.messageFont = messageFont;
        this.dateFont = dateFont;
        this.dateHeaderFont = dateHeaderFont;
        this.messageFontSize = messageFontSize;
        this.dateFontSize = dateFontSize;
        this.dateHeaderFontSize = dateHeaderFontSize;
    }

    /**
     * Set the Radius for messages.
     *
     * @param topFrom    The TOP FROM corner.
     * @param topTo      The TOP TO corner.
     * @param bottomTo   The BOTTOM TO corner.
     * @param bottomFrom The BOTTOM FROM corner.
     */
    void setMessageRadiusPx(float topFrom, float topTo, float bottomTo, float bottomFrom) {
        this.messageRadiusTopFrom = topFrom;
        this.messageRadiusTopTo = topTo;
        this.messageRadiusBottomTo = bottomTo;
        this.messageRadiusBottomFrom = bottomFrom;
    }

    /**
     * Retrieve the Text color for OUTGOING messages.
     *
     * @return The background color.
     */
    public @ColorInt int getSentMessageTextColor() {
        return sentMessageTextColor;
    }

    /**
     * Retrieve the Text color for INCOMING messages.
     *
     * @return The background color.
     */
    public @ColorInt int getReceivedMessageTextColor() {
        return receivedMessageTextColor;
    }

    /**
     * Retrieve the default Radius for messages in px.
     *
     * @return The default corner radius in the order of
     *         top-from, top-to, bottom-from, bottom-to.
     */
    public float[] getMessageRadiusPx() {
        return new float[] {
            messageRadiusTopFrom, messageRadiusTopTo,
            messageRadiusBottomTo, messageRadiusBottomFrom
        };
    }

    /**
     * Retrieve the default Radius for messages in px, where each element is repeated once.
     *
     * @return The default corner radius in the order of
     *         top-from, top-to, bottom-from, bottom-to.
     */
    public float[] getMessageRadiusPxDoubled() {
        return new float[] {
            messageRadiusTopFrom, messageRadiusTopFrom,
            messageRadiusTopTo, messageRadiusTopTo,
            messageRadiusBottomTo, messageRadiusBottomTo,
            messageRadiusBottomFrom, messageRadiusBottomFrom
        };
    }

    /**
     * Retrieve the default Radius for messages in dp.
     *
     * @return The default corner radius in the order of
     *         top-from, top-to, bottom-from, bottom-to.
     */
    public float[] getMessageRadiusDp() {
        return new float[] {
            Measure.pxToDp(messageRadiusTopFrom, this.context),
            Measure.pxToDp(messageRadiusTopTo, this.context),
            Measure.pxToDp(messageRadiusBottomTo, this.context),
            Measure.pxToDp(messageRadiusBottomFrom, this.context),
        };
    }

    /**
     * Retrieve the color for dates.
     *
     * @return The color for dates.
     */
    public @ColorInt int getDateColor() {
        return dateColor;
    }

    /**
     * Retrieve the padding for Text Messages.
     *
     * @return The padding in the order of Left, Top, Right Bottom.
     */
    public @Px int getTextMessagePadding() {
        return textMessagePadding;
    }

    /**
     * Retrieve the padding for Image Messages.
     *
     * @return The padding in the order of Left, Top, Right Bottom.
     */
    public @Px int getImageMessagePadding() {
        return imageMessagePadding;
    }

    /**
     * Retrieve the padding for Preview Messages.
     *
     * @return The padding in the order of Left, Top, Right Bottom.
     */
    public @Px int getPreviewMessagePadding() {
        return previewMessagePadding;
    }

    /**
     * Retrieve the color for Progress Bars.
     *
     * @return The color.
     */
    public @Px int getProgressBarColor() {
        return progressBarColor;
    }

    /**
     * Retrieve the Date Formatter.
     *
     * @return The date formatter.
     */
    public MessageDateFormatter getDateFormatter() {
        return dateFormatter;
    }

    /**
     * Retrieve the background color for OUTGOING messages.
     *
     * @return The color.
     */
    public @ColorInt int getSentColor() {
        return sentColor;
    }

    /**
     * Retrieve the background color for INCOMING messages.
     *
     * @return The color.
     */
    public @ColorInt int getReceivedColor() {
        return receivedColor;
    }

    /**
     * Retrieve the Elevation for messages.
     *
     * @return The elevation.
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public float getElevation() {
        return elevation;
    }

    /**
     * Retrieve the Scale for Avatars in Pixels.
     *
     * @return The scale.
     */
    public @Px int getAvatarScale() {
        return avatarScale;
    }

    /**
     * Retrieve the Shape of avatars.
     *
     * @return The shape.
     */
    public AvatarShape getAvatarShape() {
        return AvatarShape.valueOf(this.avatarShape);
    }

    /**
     * @return true if outgoing avatars should be displayed, false otherwise.
     */
    public boolean shouldDisplayOutgoingAvatars() {
        return displayOutgoingAvatars;
    }

    /**
     * @return true if incoming avatars should be displayed, false otherwise.
     */
    public boolean shouldDisplayIncomingAvatars() {
        return displayIncomingAvatars;
    }

    /**
     * @return true if the date header is enabled, false otherwise.
     */
    public boolean isDateHeaderEnabled() {
        return dateHeaderEnabled;
    }

    /**
     * @return The color for date headers.
     */
    public @ColorInt int getDateHeaderColor() {
        return dateHeaderColor;
    }

    /**
     * @return the number of minutes that can elapse before a new date header will be displayed.
     */
    public int getDateHeaderSeparationMinutes() {
        return dateHeaderSeparationMinutes;
    }

    /**
     * @return the default Typeface for messages.
     */
    public @NonNull Typeface getMessageFont() {
        return messageFont == null ? Typeface.DEFAULT : messageFont;
    }

    /**
     * @return the default Typeface for dates.
     */
    public @NonNull Typeface getDateFont() {
        return dateFont == null ? getMessageFont() : dateFont;
    }

    /**
     * @return the default Typeface for date headers.
     */
    public @NonNull Typeface getDateHeaderFont() {
        return dateHeaderFont == null ? getDateFont() : dateHeaderFont;
    }

    /**
     * @return The font size for message text.
     */
    public float getMessageFontSizeSp() {
        return messageFontSize;
    }

    /**
     * @return The font size for dates.
     */
    public float getDateFontSizeSp() {
        return dateFontSize;
    }

    /**
     * @return the font size for date headers.
     */
    public float getDateHeaderFontSizeSp() {
        return dateHeaderFontSize;
    }
}
