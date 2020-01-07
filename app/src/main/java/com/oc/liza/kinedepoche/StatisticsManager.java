package com.oc.liza.kinedepoche;

import android.graphics.Color;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.oc.liza.kinedepoche.models.ExerciseDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticsManager {


    private GraphView graphView;

    private List<Date> listOfDates;
    private int daysToShow = -7;
    private int x = 1;
    private int y;

    public StatisticsManager(GraphView graph) {
        this.graphView = graph;
    }

    public void getWeekProgress() {
        listOfDates = new ArrayList<>();
        for (int i = daysToShow; i < 0; i++) {
            getDate(i);
        }
    }


    private void getDate(int daysAgo) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, daysAgo);
        Date date = c.getTime();
        if (date != null) listOfDates.add(date);
    }

    public List<Date> getListOfDates() {
        return this.listOfDates;
    }

    public void addToWeekList(ExerciseDate result) {
        if (result != null) {
            y = result.getProgress();
        } else {
            y = 0;
        }
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(x, y),
        });
        series.setColor(Color.YELLOW);
        series.setAnimated(true);
        graphView.addSeries(series);
        x++;
        if (x == 7) {
            initGraph();
        }

    }

    private void initGraph() {
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(8);
        graphView.getViewport().setMaxY(100);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);

        List<String> stockList = new ArrayList<>();
        for (Date d : listOfDates) {
            stockList.add(Utils.getTodayDate(d));
        }

        String[] stockArr = new String[stockList.size()];
        stockArr = stockList.toArray(stockArr);

        staticLabelsFormatter.setHorizontalLabels(stockArr);
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }
}
