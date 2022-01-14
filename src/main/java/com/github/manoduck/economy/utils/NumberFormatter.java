package com.github.manoduck.economy.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatter {

    public static String format(double value) {

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag("pt-BR"));

        numberFormat.setMaximumFractionDigits(0);

        return numberFormat.format(value);

    }

    public static boolean isInvalid(double value) {
        return value < 0 || Double.isNaN(value) || Double.isInfinite(value);
    }

}