package ua.org.javatraining.automessenger.app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputTextVerification {

    //Проверка на валидность номера авто
    public static boolean checkTag(String tag) {
        Pattern p;
        Matcher m;

        String[] patterns = {"(^[a-zA-Z0-9а-яА-Я]{3,12}$)","(^(\\D*\\d){2,6}$)"};

        for(String str: patterns) {
            p = Pattern.compile(str);
            m = p.matcher(tag);
            if (!m.matches()) return false;
        }
        return true;
    }

}
