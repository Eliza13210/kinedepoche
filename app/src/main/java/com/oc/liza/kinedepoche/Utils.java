package com.oc.liza.kinedepoche;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static String getTodayDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        return dateFormat.format(date);
    }

    static String getSimpleDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.FRANCE);
        return dateFormat.format(date);
    }

    static String convertToString(List<Integer> listOfInteger) {
        String result;

        StringBuilder builder = new StringBuilder();
        for (int value : listOfInteger) {
            builder.append(value);
        }
        result = builder.toString();
        return result;
    }

    public static String returnInMinutes(int value) {
        String result;
        if (value > 60) {
            int minutesInt = value / 60;
            String minutes = String.valueOf(minutesInt);
            int secondsInt = value - (minutesInt * 60) * (value / 60);
            String seconds;
            if (secondsInt < 10) {
                seconds = "0" + secondsInt;
            } else {
                seconds = String.valueOf(secondsInt);
            }
            result = minutes +
                    ":" +
                    seconds;
        } else {
            result = String.valueOf(value);
        }
        return result;
    }
}
