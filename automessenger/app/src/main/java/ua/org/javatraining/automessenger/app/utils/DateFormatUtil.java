package ua.org.javatraining.automessenger.app.utils;

import android.content.Context;
import ua.org.javatraining.automessenger.app.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {

    public static String toReadable(Context context, long stamp) {

        if (context != null) {
            Date date = new Date(stamp);

            long cut = (System.currentTimeMillis() - stamp);

            if (cut < 86400000) {
                DateFormat df = new SimpleDateFormat("hh:mm");
                return context.getString(R.string.today) + " " + df.format(date);
            }

            DateFormat df = new SimpleDateFormat("dd.MM.yy");
            return df.format(date);
        }
        return "-";
    }

}
