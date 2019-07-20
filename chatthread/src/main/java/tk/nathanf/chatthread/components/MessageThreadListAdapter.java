package tk.nathanf.chatthread.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.lang.RuntimeException;

import tk.nathanf.chatthread.R;
import tk.nathanf.chatthread.components.messages.MessageTypes;
import tk.nathanf.chatthread.util.Measure;

/**
 * The adapter used for all MessageThreads.
 *
 * This can either wrap a List of type {@link List<Message>} or
 * an adapter of type {@link MessageThreadAdapter}.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class MessageThreadListAdapter extends BaseAdapter {
    /**
     * The messages, if wrapping a List.
     */
    private List<Message> messages = null;

    /**
     * The Adapter, if wrapping an Adapter.
     */
    private MessageThreadAdapter adapter = null;

    /**
     * The dates that should remain visible when
     * {@link MessageThreadListAdapter#notifyDataSetChanged()} is invoked.
     */
    private ArrayList<Integer> visibleDates = new ArrayList<>();

    /**
     * The MessageThread to which this Adapter belongs.
     */
    private MessageThread owner;

    /**
     * Create an empty Adapter wrapping a {@link List<Message>}.
     */
    public MessageThreadListAdapter() {
        this.messages = new ArrayList<>();
    }

    /**
     * Create an Adapter wrapping a {@link List<Message>}.
     *
     * @param messages The Messages.
     */
    public MessageThreadListAdapter(List<Message> messages) {
        this.messages = messages;
    }

    /**
     * Create an Adapter wrapping a {@link MessageThreadAdapter}
     *
     * @param adapter The adapter.
     */
    public MessageThreadListAdapter(MessageThreadAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * Add a message to the bottom of the Thread and scroll the Thread to the bottom.
     *
     * @param message The Message.
     * @throws RuntimeException If this Adapter is wrapping a {@link MessageThreadAdapter}.
     */
    public void addToBottom(Message message) {
        this.addToBottom(message, true);
    }

    /**
     * Add a message to the bottom of the Thread. If scroll is set to true, the Thread will be
     * scrolled to the bottom.
     *
     * @param message The Message.
     * @param scroll Whether or not to scroll to the bottom.
     * @throws RuntimeException If this Adapter is wrapping a {@link MessageThreadAdapter}.
     */
    public void addToBottom(Message message, boolean scroll) {
        if (this.messages == null) {
            throw new RuntimeException(
                "Cannot add messages to a MessageThreadListAdapter " +
                "that wraps a MessageThreadAdapter."
            );
        }
        this.messages.add(message);
        this.notifyDataSetChanged();
        if (scroll) {
            owner.scrollToBottom();
        }
    }

    /**
     * Add a group of Messages to the bottom of the Thread and scroll the Thread to the bottom.
     *
     * @param messages The Message.
     * @throws RuntimeException If this Adapter is wrapping a {@link MessageThreadAdapter}.
     */
    public void addToBottom(Message[] messages) {
        this.addToBottom(messages, true);
    }

    /**
     * Add a group of Messages to the bottom of the Thread. If scroll is set to true,
     * the Thread will be scrolled to the bottom.
     *
     * @param messages The Message.
     * @param scroll Whether or not to scroll to the bottom.
     * @throws RuntimeException If this Adapter is wrapping a {@link MessageThreadAdapter}.
     */
    public void addToBottom(Message[] messages, boolean scroll) {
        for (Message message : messages) {
            this.addToBottom(message, scroll);
        }
    }

    /**
     * Add a group of messages to the Top of the Thread. If reverse is set to true, they will be
     * added in reverse order.
     *
     * @param messages The Messages.
     * @param reverse  Whether or not to reverse their order.
     * @throws RuntimeException If this Adapter is wrapping a {@link MessageThreadAdapter}.
     */
    public void addToTop(Message[] messages, boolean reverse) {
        if (this.messages == null) {
            throw new RuntimeException(
                "Cannot add messages to a MessageThreadListAdapter " +
                "that wraps a MessageThreadAdapter."
            );
        }

        if (messages.length < 1) return;

        List<Message> list = Arrays.asList(messages);

        // When we add the messages later, we do it in reversed
        // order, so if they want it reversed we don't reverse
        // the collection here.
        if (!reverse) Collections.reverse(list);

        if (this.messages.isEmpty()) {
            this.messages = list;
            return;
        }

        for(Message message : list) {
            this.messages.add(0, message);
        }
        this.notifyDataSetChanged();
    }

    /**
     * Remove a Message at the specified Position.
     *
     * @param position The Position.
     * @throws RuntimeException If this Adapter is wrapping a {@link MessageThreadAdapter}.
     */
    public void remove(int position) {
        if (this.messages == null) {
            throw new RuntimeException(
                "Cannot remove messages from a MessageThreadListAdapter " +
                "that wraps a MessageThreadAdapter."
            );
        }
        this.messages.remove(position);
        this.notifyDataSetChanged();
    }

    /**
     * Set the Owner MessageThread for this Adapter.
     *
     * @param owner The Owner.
     */
    void setOwner(MessageThread owner) {
        this.owner = owner;
    }

    /**
     * Retrieve the View type for the View at the specified Position.
     *
     * @param position The position.
     * @return The View type.
     * @throws RuntimeException when the Message class has not been registered.
     */
    @Override
    public int getItemViewType(int position) {
        Message message = getItem(position);
        assert(message != null);
        Class<? extends Message> mClass = message.getClass();
        Integer viewType = MessageTypes.getViewType(mClass);
        if (viewType == null) {
            throw new RuntimeException(
                "View type for " + mClass.getName() + " is null. Did you forget to " +
                "register it with MessageTypes.register(Class<? extends Message>)?"
            );
        }

        return (message.getAuthor().getSource() == Message.Source.Other)
                ? viewType
                : (getViewTypeCount() / 2) + viewType;
    }

    /**
     * Retrieve the View Type Count.
     *
     * @return The View Type Count.
     */
    @Override
    public int getViewTypeCount() {
        return MessageTypes.getTypeCount() * 2;
    }

    /**
     * Retrieve the number of Messages managed by this Adapter.
     *
     * @return The number of Messages.
     */
    @Override
    public int getCount() {
        if (this.adapter == null && this.messages != null) {
            return this.messages.size();
        } else if (this.adapter != null && this.messages == null) {
            return this.adapter.getCount();
        }

        return 0;
    }

    /**
     * Retrieve a Message from this Adapter.
     *
     * @param position The position.
     * @return The Message.
     */
    @Override
    public Message getItem(int position) {
        Message message = null;

        if (this.adapter == null && this.messages != null) {
            try {
                message = this.messages.get(position);
            } catch (ArrayIndexOutOfBoundsException ignored) {}
        } else if (this.adapter != null && this.messages == null) {
            message = this.adapter.getMessage(position);
        }

        if(message != null) {
            message.setAdapter(this);
            message.setPosition(position);
        }

        return message;
    }

    /**
     * Retrieve the Item ID for the specified Position.
     *
     * @param position The Position.
     * @return The Item ID.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Retrieve a View at the specified Position.
     *
     * @param position The Position.
     * @param recycled The Recycled view for this View Type.
     * @param parent   The parent ViewGroup.
     *
     * @return The View.
     */
    @Override
    public View getView(final int position, View recycled, ViewGroup parent) {
        final Message message = getItem(position);
        assert(message != null);

        Context context = this.owner.getContext();
        MessageParameters params = owner.parameters;

        // Define our main views.
        View view = recycled;
        ConstraintLayout messageContainer = null;

        // Check if we are creating a new view or re-populating an existing view.
        if (view == null) {
            // Create the inflater we will use to generate our view.
            LayoutInflater inflater = (LayoutInflater)context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (inflater == null) {
                throw new RuntimeException("Unable to bind to the Layout Inflater service.");
            }

            float[] containerRadius;
            int containerColor;
            @LayoutRes int layoutResource;

            // Determine if this is a SELF message or an OTHER message
            // and based on the result initialize the containerRadius,
            // containerColor and layoutResource variables.
            if (message.getAuthor().getSource() == Message.Source.Self) {
                float[] rads = message.getRadius(params);
                layoutResource = R.layout.message_thread_outgoing_element;
                containerRadius = new float[] {
                    rads[1], rads[1], rads[0], rads[0],
                    rads[3], rads[3], rads[2], rads[2]
                };
                containerColor = params.getSentColor();
            } else {
                float[] rads = message.getRadius(params);
                layoutResource = R.layout.message_thread_incoming_element;
                containerRadius = new float[] {
                    rads[0], rads[0], rads[1], rads[1],
                    rads[2], rads[2], rads[3], rads[3]
                };
                containerColor = params.getReceivedColor();
            }

            // Inflate the outgoing message element.
            view = inflater.inflate(
                layoutResource, parent, false
            );

            // Generate the drawable for the background of the element
            // using the radius and color defined in the parameters.
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadii(containerRadius);
            drawable.setColor(containerColor);

            // Retrieve the Message Container.
            messageContainer = view.findViewById(R.id.message_container);

            // Set the background of the view to our generated drawable.
            messageContainer.setBackground(drawable);

            // Generate the View for the message based on the Message Type.
            messageContainer.addView(message.createView(params, messageContainer));

            // Set the padding for the Message Container.
            int[] padding = message.getPadding(params);
            messageContainer.setPadding(padding[0], padding[1], padding[2], padding[3]);

            // Set the minimum height and width for the container.
            messageContainer.setMinHeight(message.getMinHeight(params));
            messageContainer.setMinWidth(message.getMinWidth(params));
        }

        // Populate View.

        // Load the necessary views to be populated.
        RoundedImageView circleImageView = view.findViewById(R.id.ownerImage);
        messageContainer = (messageContainer != null)
                ? messageContainer : (ConstraintLayout)view.findViewById(R.id.message_container);

        // Generate placeholder values for DP measurements.
        int dp16 = (int)Measure.dp16(context);
        int dp8 = (int)Measure.dp8(context);
        int dpPp = (int)Measure.dpToPx(params.getAvatarScale(), context);

        // Determine if this is a SELF or OTHER message.
        if (message.getAuthor().getSource() == Message.Source.Self) {
            // Determine if we should display OUTGOING avatars.
            if (params.shouldDisplayOutgoingAvatars()) {
                // Configure the Avatar.
                circleImageView.setVisibility(View.VISIBLE);
                if (params.avatarShape == 2) {
                    circleImageView.setCornerRadius(10);
                } else if (params.avatarShape == 1) {
                    circleImageView.setCornerRadius(0);
                } else {
                    circleImageView.setCornerRadius((float)(dpPp / 2));
                }
                ((ConstraintLayout.LayoutParams)messageContainer.getLayoutParams()).setMarginEnd(
                        dp8
                );
                circleImageView.setImageBitmap(message.getAuthor().getAvatar(this));
            } else {
                circleImageView.setVisibility(View.GONE);
                ((ConstraintLayout.LayoutParams)messageContainer.getLayoutParams()).setMarginEnd(
                    dp16
                );
            }
        } else {
            // Determine if we should display INCOMING avatars.
            if (params.shouldDisplayIncomingAvatars()) {
                // Configure the Avatar
                circleImageView.setVisibility(View.VISIBLE);
                if (params.avatarShape == 2) {
                    circleImageView.setCornerRadius(dp8);
                } else if (params.avatarShape == 1) {
                    circleImageView.setCornerRadius(0);
                } else {
                    circleImageView.setCornerRadius((float)(dpPp / 2));
                }
                ((ConstraintLayout.LayoutParams)messageContainer.getLayoutParams()).setMarginStart(
                        dp8
                );
                circleImageView.setImageBitmap(message.getAuthor().getAvatar(this));
                Bitmap authorMb = message.getAuthor().getAvatar(this);
                if (authorMb != null) {
                    Log.d("PPD", "Found author BM");
                }
            } else {
                // Configure the Avatar
                circleImageView.setVisibility(View.GONE);
                ((ConstraintLayout.LayoutParams)messageContainer.getLayoutParams()).setMarginStart(
                        dp16
                );
            }
        }

        // Configure the Date Message.
        final TextView dateContainer = view.findViewById(R.id.date);
        dateContainer.setTextColor(params.getDateColor());
        dateContainer.setText(params.getDateFormatter().format(message.getSentOn()));
        dateContainer.setTypeface(params.getDateFont());
        dateContainer.setTextSize(params.getDateFontSizePx());
        if (visibleDates.contains(position) || (
            position == this.getCount() - 1 &&
            params.getDateFormatter().getMinutesAgo(message.getSentOn()) > 1
        )) {
            dateContainer.setVisibility(View.VISIBLE);
        } else {
            dateContainer.setVisibility(View.GONE);
        }

        // Configure the Date Header
        TextView dateHeader = view.findViewById(R.id.dateHeader);
        if (params.isDateHeaderEnabled()) {
            dateHeader.setTypeface(params.getDateHeaderFont());
            dateHeader.setTextSize(params.getDateHeaderFontSizePx());
            dateHeader.setTextColor(params.getDateHeaderColor());
            Message lastMessage = getItem(position - 1);
            if (lastMessage == null) {
                dateHeader.setVisibility(View.VISIBLE);
                dateHeader.setText(params.getDateFormatter().format(message.getSentOn()));
            } else {
                long minutesBetween = params.getDateFormatter().getMinutesBetween(
                        lastMessage.getSentOn(), message.getSentOn()
                );
                if (minutesBetween >= params.getDateHeaderSeparationMinutes()) {
                    dateHeader.setVisibility(View.VISIBLE);
                    dateHeader.setText(params.getDateFormatter().format(message.getSentOn()));
                } else {
                    dateHeader.setVisibility(View.GONE);
                }
            }
        } else {
            dateHeader.setVisibility(View.GONE);
        }

        // Set the OnClick Listener.
        messageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateContainer.getVisibility() == View.VISIBLE) {
                    visibleDates.remove(Integer.valueOf(position));
                    dateContainer.setVisibility(View.GONE);
                } else {
                    visibleDates.add(position);
                    dateContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        // Configure the Elevation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            messageContainer.setElevation(params.getElevation());
        }

        // Populate the View based on the Message Type.
        message.bindView(params, messageContainer.getChildAt(0));

        // Request Layouts
        circleImageView.requestLayout();
        messageContainer.requestLayout();
        view.requestLayout();

        // Return the View.
        return view;
    }
}
