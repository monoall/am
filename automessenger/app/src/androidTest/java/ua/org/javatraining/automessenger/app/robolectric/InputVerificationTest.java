package ua.org.javatraining.automessenger.app.robolectric;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ua.org.javatraining.automessenger.app.InputTextVerification.checkTag;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class InputVerificationTest {

    @Test //Тест на длину строки. Строка слишком короткая
    public void testLengthCheckForShort() throws Exception {
        String shortString = "11";

        assertFalse(checkTag(shortString));
    }

    @Test //Тест на длину строки. Строка правильная
    public void testLengthCheckForCorrect() throws Exception {
        String correctString1 ="474";
        String correctString2 ="a1a1";
        String correctString3 ="asdasd7as7";
        String correctString4 ="a1a1aa1a000";

        assertTrue(checkTag(correctString1));
        assertTrue(checkTag(correctString2));
        assertTrue(checkTag(correctString3));
        assertTrue(checkTag(correctString4));
    }

    @Test //Тест на длину строки. Строка слишком длинная
    public void testLengthCheckForLong() throws Exception {
        String longString = "aa1a233aa444a1";

        assertFalse(checkTag(longString));
    }

    @Test //Тест на соостветствие допустимому количеству цыфровых знаков. Мало знаков
    public void testCountNumbersCheckForLess() throws Exception {
        String str0 = "1hhd";

        assertFalse(checkTag(str0));
    }

    @Test //Тест на соостветствие допустимому количеству цыфровых знаков. Коректное количество
    public void testCountNumbersCheckForCorrect() throws Exception {
        String str1 = "233";
        String str2 = "фф2ыы33";

        assertTrue(checkTag(str1));
        assertTrue(checkTag(str2));
    }

    @Test //Тест на соостветствие допустимому количеству цыфровых знаков. Слишком много
    public void testCountNumbersCheckForToMany() throws Exception {
        String str3 = "244433444";

        assertFalse(checkTag(str3));
    }

    @Test //Тест на соостветствие допустимому количеству букв. Коректное количество
    public void testCountLettersCheckForCorrect() throws Exception {
        String str1 = "213";
        String str2 = "g2gU33";

        assertTrue(checkTag(str1));
        assertTrue(checkTag(str2));
    }

}
