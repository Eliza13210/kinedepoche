package com.oc.liza.kinedepoche;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Objects;

import androidx.fragment.app.Fragment;


public class StatisticsFragment extends Fragment {

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initGraph();
    }

    private void initGraph() {
        GraphView graph = Objects.requireNonNull(getActivity()).findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, 100),

        });
        series.setAnimated(true);
        graph.addSeries(series);

        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(2,33)
        });
        series2.setColor(Color.YELLOW);
        series2.setAnimated(true);
        graph.addSeries(series2);

        BarGraphSeries<DataPoint> series3 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(3,33)
        });
        series3.setColor(Color.GREEN);
        series3.setAnimated(true);
        graph.addSeries(series3);

        BarGraphSeries<DataPoint> series4 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(4,33)
        });
        series4.setColor(Color.RED);
        series4.setAnimated(true);
        graph.addSeries(series4);

        BarGraphSeries<DataPoint> series5 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(5,33)
        });
        series2.setColor(Color.YELLOW);
        series2.setAnimated(true);
        graph.addSeries(series5);

        BarGraphSeries<DataPoint> series6 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(6,33)
        });
        series2.setColor(Color.BLUE);
        series2.setAnimated(true);
        graph.addSeries(series6);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(8);
        graph.getViewport().setMaxY(100);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);

        staticLabelsFormatter.setHorizontalLabels(new String[] { "01/12", "02/12", "03/12", "04/12", "05/12", "06/12", "07/12"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);}
}
