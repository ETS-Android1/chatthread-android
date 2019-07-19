package tk.nathanf.chatthread.components.dates;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The Default MessageDateFormatter.
 */
public final class DefaultMessageDateFormatter extends MessageDateFormatter {
    /**
     * Create the Date Formatter.
     *
     * @param defaultFormat The Default Format.
     * @param flags         The Flags.
     */
    public DefaultMessageDateFormatter(String defaultFormat, int flags) {
        super(defaultFormat, flags);
    }

    /**
     * Format the Date.
     *
     * @param date  The Date.
     *
     * @return The formatted Date.
     */
    @Override
    public String format(Date date) {
        long minutesAgo = this.getMinutesAgo(date);
        long daysAgo = this.getDaysAgo(date);
        int flags = this.getFlags();

        SimpleDateFormat todayFormat = new SimpleDateFormat("h:mm aa", Locale.US);
        SimpleDateFormat weekFormat = new SimpleDateFormat("EEEE 'at' h:mm aa", Locale.US);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                this.getDefaultFormat(), Locale.US
        );

        if ((flags & FLAG_MINUTES) == FLAG_MINUTES && this.isToday(date)) {
            if (minutesAgo < 60) {
                if (minutesAgo < 1) {
                    return "Just now";
                }

                if (minutesAgo < 2) {
                    return "A minute ago";
                }

                return minutesAgo + " minutes ago";
            } else if ((flags & FLAG_DAYS) == FLAG_DAYS) {
                return "Today at " + todayFormat.format(date);
            } else {
                return dateFormat.format(date);
            }
        } else {
            if ((flags & FLAG_DAYS) == FLAG_DAYS && daysAgo < 2) {
                return ("Yesterday at " + todayFormat.format(date));
            } else if ((flags & FLAG_DAYS) == FLAG_DAYS && daysAgo < 8) {
                return weekFormat.format(date);
            } else {
                return dateFormat.format(date);
            }
        }
    }
}
