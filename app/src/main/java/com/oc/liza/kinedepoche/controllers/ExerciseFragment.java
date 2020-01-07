package com.oc.liza.kinedepoche.controllers;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.oc.liza.kinedepoche.R;
import com.oc.liza.kinedepoche.Utils;
import com.oc.liza.kinedepoche.models.ExerciseDate;

import java.util.Calendar;
import java.util.Objects;

import androidx.navigation.Navigation;
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import butterknife.BindView;

public class ExerciseFragment extends BaseFragment {

    @BindView(R.id.desk_exercises_image)
    ImageView exerciseImageView;
    @BindView(R.id.circular_progress)
    CircularProgressIndicator circularProgress;
    @BindView(R.id.date)
    TextView date;

    private String todayDate;

    private Boolean loggedIn = false;

    @Override
    public int getLayoutView() {
        return R.layout.fragment_exercise;
    }

    @Override
    public void initView() {
        loggedIn = sharedPref.getBoolean("LoggedIn", false);
        if (loggedIn) {
            initClickableImageView();
        }
        initDate();
    }

    private void initClickableImageView() {
        exerciseImageView.setClickable(true);
        exerciseImageView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.next_action, null));
    }

    private void initDate() {

        //CHECK IF TODAY EXISTS IN DATABASE
        todayDate = Utils.getTodayDate(Calendar.getInstance().getTime());
        sharedViewModel.getDate(todayDate, userId).observe(this, this::initProgress);

        Log.e("initDte", "date " +"userId="+userId+"date="+todayDate);
         date.setText(todayDate);
    }

    private void initProgress(ExerciseDate exerciseDate) {
        if (exerciseDate != null) {
            circularProgress.setCurrentProgress(exerciseDate.getProgress());
        } else {
            circularProgress.setCurrentProgress(0);
        }
    }
}
