package tk.nathanf.chatthread.components.messages.types;

import android.content.Context;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import tk.nathanf.chatthread.components.Author;
import tk.nathanf.chatthread.components.Message;
import tk.nathanf.chatthread.components.MessageParameters;

/**
 * Representation of a Text Message.
 */
@SuppressWarnings("unused")
public final class TextMessage extends Message {
    private String message;

    /**
     * Empty Constructor required for Parsing messages.
     * DO NOT USE THIS TO INSTANTIATE THIS CLASS.
     */
    public TextMessage() {}

    /**
     * Create the Text Message.
     *
     * @param context The Context.
     * @param author  The Author.
     */
    public TextMessage(Context context, Author author) {
        super(context, author);
    }

    /**
     * Set the Message text.
     *
     * @param message The message text.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Retrieve the Text from the Message.
     *
     * @return The message text.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Create the View.
     *
     * @param parameters The Parameters.
     * @param parent     The Parent ViewGroup.
     *
     * @return The View.
     * @see Message#createView(MessageParameters, ViewGroup)
     */
    @Override
    public View createView(MessageParameters parameters, ViewGroup parent) {
        TextView textView = new TextView(this.getContext());
        textView.setLayoutParams(new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));

        if (this.getAuthor().getSource() == Source.Self) {
            textView.setTextColor(parameters.getSentMessageTextColor());
        } else {
            textView.setTextColor(parameters.getReceivedMessageTextColor());
        }

        return textView;
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
        TextView textView = (TextView)view;
        textView.setText(this.message);
        Linkify.addLinks(textView, Linkify.ALL);
    }
}
