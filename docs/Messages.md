# Managing Messages

There are three types of Messages built into ChatThread.

* Text Messages
* Image Messages
* Preview Messages (for parsing URLs)

## Creating your own Message Type

You can create your own Message type by extending the Message class.

By default this will override two methods.

* `createView` -- Use this method to create a View for this Message type. Do not populate the View here, as this View will be re-used for all Messages of the same Message Type.
* `bindView` -- This method will be used to populate a View created using createView. 

```java
public class CustomMessage extends Message {
    @Override
    public View createView(MessageParameters parameters, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(MessageParameters parameters, View view) {

    }
}
```

After you have created your custom Message type, you need to make sure you register it at run time using
```java
MessageTypes.register(CustomMessage.class);
```
> Note: If you choose to add a custom constructor to your Message Type, make sure you also add an empty constructor. This is important for internal parsing.

**Other Methods you can override:**

See: [Message Thread Attributes](./MessageThreads.md)

* `int[] getPadding()` -- Retrieve the padding for this Message Type.
* `float[] getRadius()` -- Retrieve the corner radius for this Message Type in the order of top-from, top-to, bottom-from, bottom-to. 
* `int getMinWidth()` -- Retrieve the minimum width for this Message Type.
* `int getMinHeight()` -- Retrieve the minimum height for this Message Type.

## Parsing Messages

The easiest way to parse a Message is to use the `Message.parse()` method. This will take into account all Message types that have been registered and implement the parsable methods to determine what Message type to return.

```java
Message[] messages = Message.parse(context, author, "Hey, what's up!");
Message[] messages = Message.parse(context, author, "https://github.com/");
```

> Note: Keep in mind that `Message.parse()` returns an Array of messages as some Message types, when parsed, can return multiple Messages of multiple types.

### Including your own Message Type in Parsing

To include your own custom Message Type in the `parse()` method, you need to do a few things.

1. Make **sure** your message type has an **empty constructor.**
2. Override the following methods.
    * `int getParsePriority()` -- Retrieves the parse priority for this Message. The lower the number, the higher the priority.
    * `boolean isParsable(value)` -- Check if a string value CAN be parsed into this Message Type.
    * `Message[] parseMessage(value)` -- Parse the value that has been validated using `isParsable` into this Message type.
