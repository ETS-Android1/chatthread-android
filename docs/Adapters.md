# Managing Authors

There are two main types of Adapters that can be used with a Message Thread.

* List Adapters -- Has a pre-defined list and can make use of methods such as `addToBottom()` and `addToTop()`.
* Normal Adapters -- Implement an interface and cannot use the `addToBottom()` or `addToTop()` methods.

## List Adapters

To create a List Adapter simple instantiate it wrapping your list.

```java
MessageThreadListAdapter adapter = new MessageThreadListAdapter(new ArrayList<Message>() {{
    add(Message.parse(ThisActivity.this, author, "Hey, what's up!"));
}});

messageThread.setAdapter(adapter);
```

Alternately, you can initialize it with no arguments and use the `addToBottom()` and `addToTop()` methods.

### Adding Messages to List Adapters

To add a message to your List Adapter, use one of the following.

> Note: These can only be used with regular List Adapters.

1. `addToBottom(message(s), scroll)`
    ```java
    // If you set this to true, the list will automatically scroll to the bottom.
    boolean shouldScroll = true;
    
    messageThread.getAdapter().addToBottom(
        Message.parse(ThisActivity.this, author, "Hey, what's up!"),
        shouldScroll
    );
    ```

2. `addToTop(messages, reverse)`
    ```java
    // Set this to true if you want the messages added in reverse order.
    boolean reverse = false;
    messageThread.getAdapter().addToBottom(
        new Message[] {
                Message.parse(ThisActivity.this, author, "Hey, what's up!")[0],
                Message.parse(ThisActivity.this, author, "Not much!")[0],
        },
        reverse
    );
    ```

## Normal Adapters

You can alternately use a normal style Adapter with your MessageThread.

```java
messageThread.setAdapter(new MessageThreadAdapter() {
    private ArrayList<Message> messages = new ArrayList<Message>();
    
    public Message getMessage(int position) {
        return messages.get(position);    
    }

    public int getCount() {
        return messages.size();
    }
})
```


