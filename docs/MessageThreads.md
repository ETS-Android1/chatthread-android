# Implementing a Message Thread

## Creating a Message Thread

To add a Message Thread to your View, you have a few options.

1. Using XML

   ```xml
   <tk.nathanf.chatthread.components.MessageThread
        android:id="@+id/message_thread"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:background="#18181B" />
   ```
   ```java
       MessageThread messageThread = findViewById(R.id.message_thread);
   ``` 
2. In Java
   ```java
   MessageThread messageThread = new MessageThread(context);
    ```
    
## Scrolling the Message Thread

You can very easily scroll the Message Thread to the bottom at any time using
```java
messageThread.scrollToBottom();
```
    
## Configuring a Message Thread.

You can configure a message thread with the following attributes (There are matching setters/getters in Java for all attributes)
   
|Attribute|Default|Description|
|---|---|---|
|`app:mt_elevation`|`3dp`|The elevation of a Message element. (Only available on API 21+)|
|`app:mt_received_color`|`#6b6b6b`|The background color for an INCOMING message.|
|`app:mt_received_text_color`|`#FFFFFF`|The text color for an INCOMING message.|
|`app:mt_sent_color`|`#a6a6a6`|The background color for an OUTGOING message.|
|`app:mt_sent_text_color`|`#000000`|The text color for an OUTGOING message.|
|`app:mt_date_color`|`#FFFFFF`|The text color for a date displayed below a message.|
|`app:mt_date_format`|`MMM d, yyyy 'at' h:mm aa`|The default format for a date.|
|`app:mt_date_format_minutes`|`true`|Whether or not to display minute values for a date.|
|`app:mt_date_format_days`|`true`|Whether or not to display day values for a date.|
|`app:mt_date_header_enabled`|`true`|Whether or not to show date headers.|
|`app:mt_date_header_color`|`#FFFFFF`|The color for date header text.|
|`app:mt_date_header_separation_minutes`|`10`|The number of minutes that need to elapse before a new date header is displayed.|
|`app:mt_display_outgoing_avatars`|`false`|Whether or not to display OUTGOING avatars.|
|`app:mt_display_incoming_avatars`|`false`|Whether or not to display INCOMING avatars.|
|`app:mt_avatar_scale`|`medium`|The scale for avatars.|
|`app:mt_avatar_shape`|`circle`|The shape for avatars.|
|`app:mt_message_radius_top_from`|`16dp`|The top FROM corner radius for messages.|
|`app:mt_message_radius_top_to`|`16dp`|The top TO corner radius for messages.|
|`app:mt_message_radius_bottom_from`|`16dp`|The bottom FROM corner radius for messages.|
|`app:mt_message_radius_bottom_to`|`16dp`|The bottom TO corner radius for messages.|
|`app:mt_text_message_padding`|`16dp`|The default padding for Text messages and all Message types that do not override `getPadding()`|
|`app:mt_image_message_padding`|`0dp`|The default padding for Image messages.|
|`app:mt_preview_message_padding`|`0dp`|The default padding for Preview messages.|
|`app:mt_progress_bar_color`|`#ffffff`|The default color for preview message progress bars.|