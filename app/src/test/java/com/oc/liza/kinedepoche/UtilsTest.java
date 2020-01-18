package com.oc.liza.kinedepoche;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class UtilsTest {

    private Date date;
    private List<Integer> listInt;

    @Before
    public void setUp() {
        date = Mockito.mock(Date.class);
    }

    @Test
    public void getTodayDate() {
        String value = Utils.getTodayDate(date);
        String expectedValue = "01/01/1970";

        assertEquals(expectedValue, value);
    }

    @Test
    public void convertToListOfInt() {
        String toBeConverted = "123";
        int expectedValue = 1;
        int value;

        listInt = Utils.convertToListOfInt(toBeConverted);
        value = listInt.get(0);
        assertEquals(expectedValue, value);

    }

    @Test
    public void convertListOfIntToString() {

        String expectedValue = "123";
        listInt = new ArrayList<>();
        listInt.add(1);
        listInt.add(2);
        listInt.add(3);
        String value = Utils.convertToString(listInt);


        assertEquals(expectedValue, value);
    }

    @Test
    public void givenTime_whenMoreThanSixty_thenReturnInMinutes() {
        int intValue = 97;
        String value;
        String expectedValue = "1:37";

        value = Utils.returnInMinutes(intValue);

        assertEquals(expectedValue, value);

    }

    @Test
    public void givenTime_whenLessThanSixty_thenReturnInSeconds() {
        int intValue = 55;
        String value;
        String expectedValue = "55";

        value = Utils.returnInMinutes(intValue);

        assertEquals(expectedValue, value);

    }
}