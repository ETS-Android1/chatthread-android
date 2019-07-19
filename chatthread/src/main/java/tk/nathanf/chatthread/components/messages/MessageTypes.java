package tk.nathanf.chatthread.components.messages;

import androidx.annotation.Nullable;

import java.util.HashMap;

import tk.nathanf.chatthread.components.Message;
import tk.nathanf.chatthread.components.messages.types.ImageMessage;
import tk.nathanf.chatthread.components.messages.types.PreviewMessage;
import tk.nathanf.chatthread.components.messages.types.TextMessage;

/**
 * Utility class used for storing Message Type classes.
 */
public final class MessageTypes {
    /**
     * The next view type ID to assign to a MessageType.
     */
    private static int nextViewType = 0;

    /**
     * The List of Message Types.
     */
    private static HashMap<Class<? extends Message>, Integer> messageTypes
            = new HashMap<Class<? extends Message>, Integer>() {{
                put(TextMessage.class, getNextViewType());
                put(ImageMessage.class, getNextViewType());
                put(PreviewMessage.class, getNextViewType());
            }};

    /**
     * Retrieve the Message Types and their View Type IDs.
     *
     * @return The Message Types.
     */
    public static HashMap<Class<? extends Message>, Integer> getMessageTypes() {
        return messageTypes;
    }

    /**
     * Register a new Message Type.
     *
     * @param type The Message Type.
     */
    public static void register(Class<? extends Message> type) {
        messageTypes.put(type, getNextViewType());
    }

    /**
     * Retrieve the View Type for a Message Type.
     *
     * @param type The Message Type.
     *
     * @return The View Type ID.
     */
    public static @Nullable Integer getViewType(Class<? extends Message> type) {
        return messageTypes.get(type);
    }

    /**
     * Get the count of View Types.
     *
     * @return The count.
     */
    public static int getTypeCount() {
        return messageTypes.size();
    }

    /**
     * Retrieve the next View Type ID.
     *
     * @return The next View Type ID.
     */
    private static int getNextViewType() {
        int ret = nextViewType;
        nextViewType += 1;
        return ret;
    }
}
