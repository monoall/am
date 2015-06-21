package ua.org.javatraining.automessenger.app.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

    //Проверка на валидность номера авто
    public static boolean checkTag(String tag) {
        if ( tag.length() < 3 || tag.length() > 12 ) return false;

        String pattern = "^(\\D*\\d\\D*){2,6}$";

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(tag);

        if (!m.matches()) return false;

        return true;
    }

}
