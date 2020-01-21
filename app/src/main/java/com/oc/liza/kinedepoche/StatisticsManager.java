package com.oc.liza.kinedepoche;

import android.graphics.Color;
import android.util.Log;

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
    private List<DataPoint> listDataPoints = new ArrayList<>();
    private int daysToShow = -4;
    private int x = 1;
    private int y;

    public StatisticsManager(GraphView graph) {
        this.graphView = graph;
    }

    public void getWeekProgress() {
        listOfDates = new ArrayList<>();
        for (int i = daysToShow; i <= 0; i++) {
            getDate(i);
        }
    }


    private void getDate(int daysAgo) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, daysAgo);
        Date date = c.getTime();
        listOfDates.add(date);
    }

    public List<Date> getListOfDates() {
        return this.listOfDates;
    }

    public void initDataPoints() {
        x = 1;
        y = 0;
        for (Date d : listOfDates) {
            DataPoint point = new DataPoint(x, y);
            listDataPoints.add(point);
            x++;
        }
    }

    List<DataPoint> getListOfDataPoints() {
        return this.listDataPoints;
    }

    public void updateDataPoints(ExerciseDate result) {
        if (result != null) {
            y = result.getProgress();
            for (int i = 0; i < listOfDates.size(); i++) {
                if (result.getDate().equals((Utils.getTodayDate(listOfDates.get(i))))) {
                    x = i + 1;
                    DataPoint point = new DataPoint(x, y);
                    listDataPoints.set(i, point);
                }
            }
        }
        initGraph();
    }

    private void initGraph() {
        graphView.removeAllSeries();

        DataPoint[] pointArr = new DataPoint[listDataPoints.size()];
        pointArr = listDataPoints.toArray(pointArr);

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(pointArr);

        series.setAnimated(true);
        graphView.addSeries(series);

        // styling
        series.setValueDependentColor(data -> Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100));
        series.setSpacing(50);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(listDataPoints.size() + 1);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(100);

        graphView.getViewport().setYAxisBoundsManual(true);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);

        List<String> stockList = new ArrayList<>();
        stockList.add(" ");
        for (Date d : listOfDates) {
            stockList.add(Utils.getSimpleDate(d));
        }
        stockList.add(" ");

        String[] stockArr = new String[stockList.size()];
        stockArr = stockList.toArray(stockArr);
        Log.e("stock", "size" + stockArr.length);

        staticLabelsFormatter.setHorizontalLabels(stockArr);
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }
}
