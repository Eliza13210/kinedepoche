package com.oc.liza.kinedepoche.controllers;

import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.oc.liza.kinedepoche.R;
import com.oc.liza.kinedepoche.StatisticsManager;
import com.oc.liza.kinedepoche.Utils;
import com.oc.liza.kinedepoche.models.ExerciseDate;

import java.util.Date;
import java.util.List;

import butterknife.BindView;


public class StatisticsFragment extends BaseFragment {

    @BindView(R.id.graph)
    GraphView graphView;

    private StatisticsManager manager;

    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutView() {
        return R.layout.fragment_statistics;
    }

    @Override
    public void initView() {
        initGraph();
    }

    private void addToWeekList(ExerciseDate result) {
        manager.updateDataPoints(result);
    }

    private void initGraph() {
        manager = new StatisticsManager(graphView);
        manager.getWeekProgress();
        manager.initDataPoints();
        List<Date> listOfDates=manager.getListOfDates();


        Log.e("initgraph", "date list="+ listOfDates.size());
        for (Date d : listOfDates) {

            sharedViewModel.getDate(Utils.getTodayDate(d), userId).observe(this, this::addToWeekList);
            Log.e("initGraph", "date=" + Utils.getTodayDate(d));
        }
    }
}
