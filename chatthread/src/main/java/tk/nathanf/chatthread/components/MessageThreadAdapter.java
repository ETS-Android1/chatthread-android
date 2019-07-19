package tk.nathanf.chatthread.components;

import android.content.Context;

import java.lang.String;

/**
 * For implementing a custom Adapter for MessageThreads.
 */
public interface MessageThreadAdapter {
    /**
     * Retrieve the Message at the specified position.
     *
     * @param position The position.
     * @return The Message.
     * @see Message
     * @see Message#parse(Context, Author, String)
     */
    Message getMessage(int position);

    /**
     * Retrieve the number of Messages in this Adapter.
     *
     * @return The number of Messages.
     */
    int getCount();
}
