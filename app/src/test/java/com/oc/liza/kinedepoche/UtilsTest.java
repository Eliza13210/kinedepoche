package com.oc.liza.kinedepoche;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;

import static junit.framework.Assert.assertEquals;

public class UtilsTest {

    private Date date;

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

}