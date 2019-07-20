package tk.nathanf.chatthread.components.dates;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * A representation of a Date Formatter.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class MessageDateFormatter {
    /**
     * Flag for displaying Minutes ago.
     * <br>
     * <p>
     *     i.e. "10 minutes ago", "50 minutes ago", "a minute ago"
     * </p>
     */
    public static final int FLAG_MINUTES = 0b01;

    /**
     * Flag for displaying Days ago.
     * <br>
     * <p>
     *     i.e. "Today at 5:30pm", "Yesterday at 5:30pm", "Monday at 5:30pm"
     * </p>
     */
    public static final int FLAG_DAYS = 0b10;

    /**
     * Support both FLAG_MINUTES and FLAG_DAYS.
     *
     * @see MessageDateFormatter#FLAG_MINUTES
     * @see MessageDateFormatter#FLAG_DAYS
     */
    public static final int FLAG_ALL = 0b11;

    /**
     * The default format to use for Dates.
     */
    private String defaultFormat;

    /**
     * The Flags.
     */
    private int flags;

    /**
     * Create the Date Formatter.
     *
     * @param defaultFormat The Default Format.
     * @param flags         The Flags.
     */
    public MessageDateFormatter(String defaultFormat, int flags) {
        this.defaultFormat = defaultFormat;
        this.flags = flags;
    }

    /**
     * Check if a Date is today.
     *
     * @param date The Date.
     * @return True if it was today, otherwise false.
     */
    public boolean isToday(Date date) {
        return this.getDaysAgo(date) < 1;
    }

    /**
     * Retrieve the number of minutes ago a specific date was.
     *
     * @param date The Date.
     * @return The number of minutes ago it was.
     */
    public long getMinutesAgo(Date date) {
        return TimeUnit.MILLISECONDS.toMinutes(new Date().getTime() - date.getTime());
    }

    /**
     * Retrieve the number of minutes between two dates.
     *
     * @param first  The first Date.
     * @param second The second Date.
     * @return       The number of minutes between the two Dates.
     */
    public long getMinutesBetween(Date first, Date second) {
        return Math.abs(TimeUnit.MILLISECONDS.toMinutes(first.getTime() - second.getTime()));
    }

    /**
     * The number of Days ago a specific Date was.
     *
     * @param date The Date.
     * @return     The number of days ago it was.
     */
    public long getDaysAgo(Date date) {
        return TimeUnit.MILLISECONDS.toDays(new Date().getTime() - date.getTime());
    }

    /**
     * Retrieve the Default Format for this Formatter.
     *
     * @return The default format.
     */
    public String getDefaultFormat() {
        return defaultFormat;
    }

    /**
     * Retrieve the Flags for this Formatter.
     *
     * @return The flags.
     */
    public int getFlags() {
        return flags;
    }

    /**
     * Set the flags for the Formatter.
     *
     * @param flags The flags.
     *
     * @see MessageDateFormatter#FLAG_DAYS
     * @see MessageDateFormatter#FLAG_MINUTES
     * @see MessageDateFormatter#FLAG_ALL
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * Set the default format.
     *
     * @param format The new format.
     */
    public void setDefaultFormat(String format) {
        this.defaultFormat = format;
    }

    /**
     * Format a Date.
     *
     * @param date  The Date.
     * @return      The formatted Date.
     */
    public abstract String format(Date date);
}
