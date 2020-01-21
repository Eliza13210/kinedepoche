package com.oc.liza.kinedepoche;

import com.jjoe64.graphview.GraphView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class StatisticsManagerTest {

    private GraphView graph;

    @Before
    public void setUp() {
        graph = Mockito.mock(GraphView.class);
    }

    @Test
    public void addDatesToWeek_whenNumberIsMinusFour_thenReturnListOfFiveDates() {
        StatisticsManager manager = new StatisticsManager(graph);
        manager.getWeekProgress();

        int listLengthExpected = 5;

        assertFalse(manager.getListOfDates().isEmpty());
        assertEquals(listLengthExpected, manager.getListOfDates().size());
    }


    @Test
    public void initDataPoints() {
        StatisticsManager manager = new StatisticsManager(graph);
        manager.getWeekProgress();
        manager.initDataPoints();

        int listOfDataPointsLength = 5;
        assertEquals(listOfDataPointsLength, manager.getListOfDataPoints().size());

    }

    @Test
    public void updateDataPoints() {
    }
}