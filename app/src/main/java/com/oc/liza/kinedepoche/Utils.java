package com.oc.liza.kinedepoche;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {

    public static String getTodayDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    public static String getSimpleDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM");
        return dateFormat.format(date);
    }

    public static List<Integer> convertToListOfInt(String string) {
        List<Integer> listInt;

        Gson gson = new Gson();
        Type type = new TypeToken<Integer>() {
        }.getType();

        listInt = gson.fromJson(string, type);
        return listInt;
    }

    public static String convertToString(List<Integer> listOfInteger) {
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
            String minutes = String.valueOf(value / 60);
            String seconds = String.valueOf(value - 60 * (value / 60));
            StringBuilder str = new StringBuilder();
            str.append(minutes);
            str.append(":");
            str.append(seconds);
            result = str.toString();
        } else {
            result = String.valueOf(value);
        }
        return result;
    }
}
