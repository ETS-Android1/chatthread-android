package tk.nathanf.chatthread.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Utility class for measurements.
 */
@SuppressWarnings("unused")
public class Measure {
    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit,
     *                which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     *
     * @return        A float value to represent px equivalent to dp depending on device density.
     */
    public static float dpToPx(float dp, Context context){
        return dp * (
            (float) context.getResources().getDisplayMetrics().densityDpi /
                    DisplayMetrics.DENSITY_DEFAULT
        );
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into dp
     * @param context Context to get resources and device specific display metrics
     *
     * @return A float value to represent dp equivalent to px value
     */
    public static float pxToDp(float px, Context context){
        return px / (
            (float) context.getResources().getDisplayMetrics().densityDpi /
                    DisplayMetrics.DENSITY_DEFAULT
        );
    }

    /**
     * This method converts device specific pixels to scaled independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into dp
     * @param context Context to get resources and device specific display metrics
     *
     * @return A float value to represent sp equivalent to px value
     */
    public static float pxToSp(float px, Context context) {
        return px / context.getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * This method converts sp unit to equivalent pixels, depending on device density.
     *
     * @param sp      A value in sp (scaled independent pixels) unit,
     *                which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     *
     * @return        A float value to represent px equivalent to sp depending on device density.
     */
    public static float spToPx(float sp, Context context) {
        return sp * context.getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * Retrieve 64dp in Pixels.
     *
     * @param context The Context.
     * @return 64dp in Pixels.
     */
    public static float dp64(Context context) {
        return dpToPx(64, context);
    }

    /**
     * Retrieve 32dp in Pixels.
     *
     * @param context The Context.
     * @return 32dp in Pixels.
     */
    public static float dp32(Context context) {
        return dpToPx(32, context);
    }

    /**
     * Retrieve 24dp in Pixels.
     *
     * @param context The Context.
     * @return 24dp in Pixels.
     */
    public static float dp24(Context context) {
        return dpToPx(24, context);
    }

    /**
     * Retrieve 16dp in Pixels.
     *
     * @param context The Context.
     * @return 16dp in Pixels.
     */
    public static float dp16(Context context) {
        return dpToPx(16, context);
    }

    /**
     * Retrieve 8dp in Pixels.
     *
     * @param context The Context.
     * @return 8dp in Pixels.
     */
    public static float dp8(Context context) {
        return dpToPx(8, context);
    }

    /**
     * Retrieve 4dp in Pixels.
     *
     * @param context The Context.
     * @return 4dp in Pixels.
     */
    public static float dp4(Context context) {
        return dpToPx(4, context);
    }
}
