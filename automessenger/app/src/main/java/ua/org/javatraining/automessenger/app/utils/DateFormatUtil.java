package ua.org.javatraining.automessenger.app.utils;

import android.content.Context;
import android.util.Log;
import ua.org.javatraining.automessenger.app.R;

public class DateFormatUtil {

    public static String toReadable(Context context, long stamp) {

        if (context != null) {

            long cut = (System.currentTimeMillis() - stamp) / 1000; //cut off milliseconds

            if (cut < 60) return Long.toString(cut) + context.getString(R.string.short_seconds);

            cut /= 60; //cut off seconds

            if (cut < 60) return Long.toString(cut) + context.getString(R.string.short_minutes);

            cut /= 60; //cut off minutes

            if (cut < 24) return Long.toString(cut) + context.getString(R.string.short_hours);

            cut /= 24; //cut off hours

            if (cut < 30) return Long.toString(cut) + context.getString(R.string.short_days);

            cut /= 30; //cut off days

            if (cut < 12) return Long.toString(cut) + context.getString(R.string.short_months);

            cut /= 12; //cut off months

            return Long.toString(cut) + context.getString(R.string.short_years);
        }
        return "-";
    }

}
