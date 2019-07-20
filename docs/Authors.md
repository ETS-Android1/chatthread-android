# Managing Authors

Creating a new Author is very simple and straight forward.

```java
Author author = new Author(Message.Source.Self, "John Doe", "https://my.site/image.jpg");
```

In this example, the Author has the following attributes:

|Attribute|Value|Description|
|---|---|---|
|Source|`Source.Self`|This means that the message originated from this Device. The options here are `Source.Self` and `Source.Other`.|
|Name|"John Doe"|This is the name of the Author.|
|Avatar|`https://my.site/image.jpg`|The last parameter of the Author constructor is their Avatar, here it can take either a Bitmap or a URL to an Image. If you have your ChatThread configured to not display Avatars, this will be ignored and you can use `null` instead. If you use a URL for the Avatar, it will be loaded asynchronously once and then stored within the Author object for subsequent calls. See [Author.java](../chatthread/src/main/java/tk/nathanf/chatthread/components/Author.java)|