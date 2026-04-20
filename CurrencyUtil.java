package utils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtil {

    public static double toJPY(double value) {
        return value;
    }

    public static double toJPY(int value) {
        return value;
    }

    public static String formatJPY(double value) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.JAPAN);
        nf.setMaximumFractionDigits(0);
        nf.setMinimumFractionDigits(0);
        return "¥" + nf.format(value);
    }

    public static String formatJPY(int value) {
        return formatJPY((double) value);
    }

    public static String yen(double value) {
        return formatJPY(value);
    }

    public static String yen(int value) {
        return formatJPY(value);
    }
}