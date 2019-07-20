package tk.nathanf.chatthread.components;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import tk.nathanf.chatthread.components.messages.MessageTypes;
import tk.nathanf.chatthread.components.messages.types.ImageMessage;
import tk.nathanf.chatthread.components.messages.types.TextMessage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.lang.String;
import java.lang.Class;

/**
 * A representation of a Message to be displayed within a {@link MessageThread}.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class Message {
    /**
     * The source of a Message.
     */
    public enum Source {
        Self,
        Other
    }

    /**
     * The Context.
     */
    private Context context;

    /**
     * The Adapter that this message belongs to, if any.
     */
    private MessageThreadListAdapter adapter;

    /**
     * The position of this Message in it's adapter, if any.
     */
    private int position;

    /**
     * The Date on which this Message was sent.
     *
     * @see Message#getSentOn()
     * @see Message#setSentOn(Date)
     */
    private Date sentOn;

    /**
     * The Author of this Message.
     *
     * @see Author
     */
    private Author author;

    /**
     * Empty Constructor required for Parsing messages.
     * DO NOT USE THIS TO INSTANTIATE THIS CLASS.
     *
     * @see Message#parse(Context, Author, String)
     * @see Message#parseMessage(Context, Author, Date, String)
     * @see Message#getParsePriority()
     * @see Message#isParsable(String)
     */
    public Message() {}

    /**
     * Create a new Message.
     *
     * @param context The Context.
     * @param author  The Author.
     */
    public Message(Context context, @NonNull Author author) {
        this.context = context;
        this.author = author;
    }

    /**
     * Retrieve the Author.
     *
     * @return The Author.
     * @see Author
     */
    public final @NonNull Author getAuthor() {
        return this.author;
    }

    /**
     * Set the Date on which this message was sent.
     *
     * @param date The date.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public final void setSentOn(Date date) {
        this.sentOn = date;
    }

    /**
     * Retrieve the Date on which this Message was sent. If non is set,
     * the current date will be returned instead.
     *
     * @return The date.
     */
    public final @NonNull Date getSentOn() {
        return this.sentOn == null ? new Date() : this.sentOn;
    }

    /**
     * Set the Position for this Message within it's Adapter.
     *
     * @param position The Position.
     */
    void setPosition(int position) {
        this.position = position;
    }

    /**
     * Retrieve the position within this Messages Adapter.
     *
     * @return The position.
     */
    @SuppressWarnings("unused")
    public final int getPosition() {
        return this.position;
    }

    /**
     * Set the Adapter that this Message belongs to, if Any.
     *
     * @param adapter The adapter.
     */
    void setAdapter(MessageThreadListAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * Retrieve the Adapter that this Message belongs to, if any.
     *
     * @return The adapter.
     */
    public final @Nullable MessageThreadListAdapter getAdapter() {
        return adapter;
    }

    /**
     * Retrieve the Context for the Message.
     *
     * @return The Context.
     */
    public final Context getContext() {
        return this.context;
    }

    /**
     * <p>
     * Parse a group of Messages from a String.
     * </p><br>
     *
     * <p>
     * This will get a list of all Messages registered using {@link MessageTypes#register(Class)}
     * and use their {@link Message#getParsePriority()} and {@link Message#isParsable(String)}
     * methods to determine which MessageType should be used for the input data.
     * </p><br>
     *
     * <p>
     * If no suitable message type can be determined, the function will
     * result in a {@link TextMessage} by default.
     * </p><br>
     *
     * <p>
     * Once the message type has been determined, we use the
     * {@link Message#parseMessage(Context, Author, Date, String)}
     * method to parse the Message out to an array of Messages.
     * </p><br>
     *
     * <p>
     * You can make your custom {@link Message} implementations work with this method by
     * implementing the three main parse methods.
     * </p><br>
     *
     *
     * <p>
     * This method returns multiple messages as some Message types can result in multiple Messages
     * being returned, for an example see the {@link ImageMessage} implementation.
     * </p>
     *
     * @param context The Context.
     * @param author  The Author.
     * @param data    The Data.
     *
     * @return The messages
     *
     * @see Message#isParsable(String)
     * @see Message#parseMessage(Context, Author, Date, String)
     * @see Message#getParsePriority()
     * @see Message#parse(Context, Author, Date, String)
     */
    public static Message[] parse(Context context, Author author, String data) {
        return Message.parse(context, author, new Date(), data);
    }

    /**
     * <p>
     * Parse a group of Messages from a String.
     * </p><br>
     *
     * <p>
     * This will get a list of all Messages registered using {@link MessageTypes#register(Class)}
     * and use their {@link Message#getParsePriority()} and {@link Message#isParsable(String)}
     * methods to determine which MessageType should be used for the input data.
     * </p><br>
     *
     * <p>
     * If no suitable message type can be determined, the function will
     * result in a {@link TextMessage} by default.
     * </p><br>
     *
     * <p>
     * Once the message type has been determined, we use the
     * {@link Message#parseMessage(Context, Author, Date, String)}
     * method to parse the Message out to an array of Messages.
     * </p><br>
     *
     * <p>
     * You can make your custom {@link Message} implementations work with this method by
     * implementing the three main parse methods.
     * </p><br>
     *
     *
     * <p>
     * This method returns multiple messages as some Message types can result in multiple Messages
     * being returned, for an example see the {@link ImageMessage} implementation.
     * </p>
     *
     * @param context The Context.
     * @param author  The Author.
     * @param sentOn  The Date on which this Message was sent.
     * @param data    The Date.
     *
     * @return        The Messages.
     *
     * @see Message#isParsable(String)
     * @see Message#parseMessage(Context, Author, Date, String)
     * @see Message#getParsePriority()
     */
    public static Message[] parse(Context context, Author author, Date sentOn, String data) {
        Set<Class<? extends Message>> messageTypesSet = MessageTypes.getMessageTypes().keySet();
        List<Class<? extends Message>> messageTypesList = new LinkedList<>(messageTypesSet);
        Collections.sort(messageTypesList, new Comparator<Class<? extends Message>>() {
            @SuppressWarnings("UseCompareMethod")
            @Override
            public int compare(
                Class<? extends Message> theClass, Class<? extends Message> otherClass
            ) {
                try {
                    Message m1 = theClass.newInstance();
                    Message m2 = otherClass.newInstance();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        return Integer.compare(m1.getParsePriority(), m2.getParsePriority());
                    } else {
                        return Integer.valueOf(m1.getParsePriority())
                                .compareTo(m2.getParsePriority());
                    }
                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            }
        });

        for(Class<? extends Message> messageTypeClass : messageTypesList) {
            try {
                Message m = messageTypeClass.newInstance();
                if (m.isParsable(data)) {
                    return m.parseMessage(context, author, sentOn, data);
                }
            } catch (Exception ignored) {}
        }

        TextMessage textMessage = new TextMessage(context, author);
        textMessage.setMessage(data);
        textMessage.setSentOn(sentOn);
        return new Message[] { textMessage };
    }

    /**
     * Retrieve the Padding for this Message Type.
     * Defaults to {@link MessageParameters#getTextMessagePadding()}
     *
     * @param parameters The Parameters.
     *
     * @return The float array of length four representing the padding
     *         in the order of Left, Top, Right, Bottom.
     */
    public int[] getPadding(MessageParameters parameters) {
        return new int[] {
            parameters.getTextMessagePadding(),
            parameters.getTextMessagePadding(),
            parameters.getTextMessagePadding(),
            parameters.getTextMessagePadding()
        };
    }

    /**
     * Retrieve the corner radius for this Message Type.
     * Defaults to {@link MessageParameters#getMessageRadiusPx()}
     *
     * @param parameters The parameters.
     *
     * @return The corner radius in the order of top-from, top-to, bottom-from, bottom-to.
     */
    public float[] getRadius(MessageParameters parameters) {
        return parameters.getMessageRadiusPx();
    }

    /**
     * Retrieve the minimum width for this Message Type. Defaults to 50.
     *
     * @param parameters The Parameters.
     *
     * @return The minimum width.
     */
    public int getMinWidth(MessageParameters parameters) {
        return 50;
    }

    /**
     * Retrieve the minimum height for this Message Type. Defaults to 50.
     *
     * @param parameters The Parameters.
     *
     * @return The minimum height.
     */
    public int getMinHeight(MessageParameters parameters) {
        return 50;
    }

    /**
     * Retrieve the Parse Priority for this Message Type.
     *
     * A lower values means it will have a higher parse priority.
     *
     * @return The parse priority.
     */
    public int getParsePriority() {
        return Integer.MAX_VALUE;
    }

    /**
     * Check if the value passed is parsable as this Message type.
     *
     * @param value The value.
     *
     * @return True if the value is parsable.
     */
    public boolean isParsable(String value) {
        return false;
    }

    /**
     * Parse a value that has be validated with {@link Message#isParsable(String)} and return
     * the resulting Messages.
     *
     * This method returns multiple messages as some Message types can result in multiple Messages
     * being returned, for an example see the {@link ImageMessage} implementation.
     *
     * @param context The context.
     * @param author  The Author.
     * @param sentOn  The date on which the Message was sent.
     * @param value   The value.
     *
     * @return The Messages.
     */
    public Message[] parseMessage(Context context, Author author, Date sentOn, String value) {
        return new Message[] {};
    }

    /**
     * Create the View for this Message Type.
     *
     * This will be called once for each value in {@link Message.Source} depending on if there
     * are any Messages of this type within it's parent Adapter
     * corresponding to that Source value.
     *
     * @param parameters The Parameters.
     * @param parent     The Parent ViewGroup.
     *
     * @return           The View.
     */
    public abstract View createView(MessageParameters parameters, ViewGroup parent);

    /**
     * Bind this Message to a View.
     *
     * This View received here is the View that was created earlier by the adapter using
     * {@link Message#createView(MessageParameters, ViewGroup)}. You should use this function
     * to modify the View with the new content from this Message.
     *
     * @param parameters The Parameters.
     * @param view       The View.
     *
     * @see Message#createView(MessageParameters, ViewGroup)
     */
    public abstract void bindView(MessageParameters parameters, View view);
}
