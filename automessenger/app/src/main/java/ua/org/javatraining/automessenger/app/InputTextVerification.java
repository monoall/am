package ua.org.javatraining.automessenger.app;

/**
 * Created by easy on 29.05.15.
 */
public class InputTextVerification {

    //Проверка на валидность номера авто
    public boolean checkTag(String tag) {

        int length = tag.length();

        //проверка на количество знаков.
        if (length < 3 | length > 10) return false;

        int letters = 0;
        int digits = 0;
        int otherChars = 0;

        //Считаем количество цифр и букв отдельно
        for (int i = 0; i < length; i++) {
            int chr = (int) tag.charAt(i);
            if (chr > 47 && chr < 58) digits++;
            else if ((chr > 64 && chr < 91) | (chr > 96 && chr < 123) |
                    (chr > 190 && chr <= 255)) letters++;
            else otherChars++;
        }

        //В должны быть только цифры или буквы
        if (otherChars > 0) return false;
        //В номере должно быть минимум две цифры (включительно с кодом региона)
        if (digits < 2) return false;
        //В номере должно быть от 0...8 букв
        if (letters > 8) return false;

        //Если все ок, то ...
        return true;
    }

}
