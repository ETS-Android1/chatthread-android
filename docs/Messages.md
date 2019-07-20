# Managing Messages

## Parsing Messages

The easiest way to parse a Message is to use the `Message.parse()` method. This will take into account all Message types that have been registered and implement the parsable methods to determine what Message type to return.

> Note: Keep in mind that `Message.parse()` returns an Array of messages as some Message types, when parsed, can return multiple Messages of multiple types.

### Text Messages

The Text Message is the most basic type of Message. It simply stores a String of Text to be displayed. Any phone numbers or URLs will be clickable.

There are two options available for creating a new TextMessage.

1. Parse a String with the `Message.parse()` method.
    ```java
    Message[] messages = Message.parse(context, author, "Hey, what's up!");
    ```
    > Note: When using this method, if the string contains a URL the PreviewMessage type will be returned instead.
2. Instantiate the Message and set it's Text.
    ```java
    TextMessage message = new TextMessage(context, author);
    message.setSentOn(new Date());
    message.setMessage("Hey, what's up?")
    ```

#### Text Message Padding
    
You can configure the padding for Text Messages using either attributes or the setter method on the `MessageThread` object.

```
app:mt_text_message_padding="16dp"
```

```java
messageThread.setTextMessagePadding(Measure.dpToPx(16, context));
```

> Note: Text Message padding is used by default by any Message Type that does not override the `getPadding()` method of the Message class.


### Image Messages

Image Messages speak for themselves. They give you a very easy to use interface for sending Images, both from the web and from regular Bitmap files.

There are two main ways in which to create a new Image Message.

1. Parse a String with the `Message.parse()` method.
    ```java
    Message[] messages = Message.parse(context, author, "Check out this picture: https://website.com/image.png");
    ```
    When using this method, if the text sent to `Message.parse()` contains a URL that IS an image, the URl for the image will be extracted and an Image message will be returned for that. If the message also contains text along side the URl for the image, all of the text will be returned as a TextMessage and the URL will be parsed into an Image Message.
2. Instantiate the Message and set it's Image.
    ```java
    ImageMessage message = new ImageMessage(context, author);
    message.setImage(someBitmap);
    message.setName("the-image-name-for-downloads");
    ``` 
    When using this method, if you provide a Bitmap to the `setImage` method it will be loaded instantly. If you provide a URL to the `setImage` method, it will be loaded asynchronously and a progress bar will be displayed until it is loaded.

#### Image Message Padding

You can configure the padding for Image Messages using either attributes or the setter method on the `MessageThread` object.

```
app:mt_image_message_padding="0dp"
```

```java
messageThread.setImageMessagePadding(Measure.dpToPx(16, context));
```

#### Opening Images

Image Message support a special Activity when clicked. This is the Activity used for viewing the image, potentially downloading it, etc. By default there is a built in Activity that will allow the user to download the image and see when it was sent. This activity also supports zooming in on the photo.

See [PreviewImage.java](../chatthread/src/main/java/tk/nathanf/chatthread/activities/PreviewImage.java)

You can supply your own Activity for this by setting `PreviewImage.previewActivity` to the Activity class you would like to use.

```java
PreviewImage.previewActivity = MyActivity.class;
```  

Once the Image is tapped, the following four will be populated and your Activity will be started.

|Item|Type|Description|
|---|---|---|
|`PreviewImage.currentlyDisplayedImage`|`Bitmap`|The image to be displayed.|
|`PreviewImage.currentlyDisplayedImageName`|`String`|The name of the image.|
|`PreviewImage.currentlyDisplayedImageAuthor`|`Author`|The author of the message.|
|`PreviewImage.currentlyDisplayedImageAuthor`|`Author`|The author of the message.|
|`PreviewImage.currentlyDisplayedImageDate`|`Date`|The date at which the message was sent.|


### Preview Messages

Preview Messages supply an easy to use system for generating Previews of URLs, or custom content if you so wish.

There are three main ways you can create a Preview Message.

1. Parse a String with the `Message.parse()` method.
    ```java
    Message[] messages = Message.parse(context, author, "Hey, check out my site. https://nathanf.tk/");
    ```
    Any message that is sent to the `Message.parse()` method that contains a URL that is not an image will be parsed as a Preview Message by default. When the message is parsed, the largest image found on the page will be used for the preview.

2. Instantiate the Message and set it's content.
    ```java
    PreviewMessage message = new PreviewMessage(context, author);
    message.setTitle("My Preview");
    message.setContent("This goes underneath the title.");
    message.setUrl("https://google.com/");
    message.setText("Hey, check out this site https://google.com");
    message.setImage(someBitmap);
    ```  
    
#### Preview Message Padding

You can configure the padding for Preview Messages using either attributes or the setter method on the `MessageThread` object.

```
app:mt_preview_message_padding="0dp"
```

```java
messageThread.setPreviewMessagePadding(Measure.dpToPx(16, context));
```
    
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

### Including your own Message Type in Parsing

To include your own custom Message Type in the `parse()` method, you need to do a few things.

1. Make **sure** your message type has an **empty constructor.**
2. Override the following methods.
    * `int getParsePriority()` -- Retrieves the parse priority for this Message. The lower the number, the higher the priority.
    * `boolean isParsable(value)` -- Check if a string value CAN be parsed into this Message Type.
    * `Message[] parseMessage(value)` -- Parse the value that has been validated using `isParsable` into this Message type.
