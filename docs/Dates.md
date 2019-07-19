# Implementing a custom Date Formatter

If you don't wish to use the built in Date Formatter, you can opt to create your own.

```java
class MyDateFormatter extends MessageDateFormatter {
    @Override
    public String format(Date date) {
        return "";
    }
}
```

Within the `format(Date date)` method, you can use the following methods:

* `int getFlags()` -- Retrieve the flags for this Date Formatter. This are bit flags containing these three values: `FLAG_MINUTES` and/or `FLAG_DAYS`, `FLAG_ALL`, or `0`.
* `boolean isToday(Date)` -- Check if the given date was Today.
* `long getMinutesAgo(Date)` -- Check how many minutes ago the given Date was.
* `long getDaysAgo(Date)` -- Check how many days ago the given Date was.
* `String getDefaultFormat()` -- Get the default format for dates.

See: [Message Thread Attributes](./MessageThreads.md)

## Format

The format for the Date Formatter can either be set with an attribute or in code.

1. With Attribute
    ```
    app:mt_date_format="MMM d, yyyy 'at' h:mm aa"
    ```
2. In Code
    ```java
    dateFormatter.setDefaultFormat("MMM d, yyyy 'at' h:mm aa");
    ```

## Flags

The flags for a formatter can be set either using the attributes or in code.

1. With attributes
    ```
    app:mt_date_format_days="true"
    app:mt_date_format_minutes="true"
    ```
2. In Code
    ```java
    dateFormatter.setFlags(MessageDateFormatter.FLAG_MINUTES | MessageDateFormatter.FLAG_DAYS);
    ```
    
You can parse these flags in your custom implementation like so:
```java
boolean shouldUseMinutes = (getFlags() & MessageDateFormatter.FLAG_MINUTES) == MessageDateFormatter.FLAG_MINUTES;
```