package com.github.manoduck.economy.utils;

public class Helper {

    public static Double tryParseDouble(String string) {

        try {

            return Double.parseDouble(string);

        }

        catch (NumberFormatException exception) { return null; }

    }

    public static Integer tryParseInt(String string) {

        try {

            return Integer.parseInt(string);

        }

        catch (NumberFormatException exception) { return null; }

    }
}