package com.oc.liza.kinedepoche.controllers;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.oc.liza.kinedepoche.R;
import com.oc.liza.kinedepoche.Utils;
import com.oc.liza.kinedepoche.models.ExerciseDate;

import java.util.Calendar;
import java.util.List;
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

    @Override
    public int getLayoutView() {
        return R.layout.fragment_exercise;
    }

    @Override
    public void onResume() {
        super.onResume();

        //FOR TESTING ONLY
        todayDate = Calendar.getInstance().getTime().toString();
        ExerciseDate createDate = new ExerciseDate(null, 1, todayDate, 20, "1");
        sharedViewModel.createDate(createDate);

        initClickableImageView();
        initDate();
    }

    private void initClickableImageView() {
        ImageView exercise = Objects.requireNonNull(getView()).findViewById(R.id.desk_exercises_image);
        exercise.setClickable(true);
        exercise.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.next_action, null));
    }

    private void initDate() {

        //CHECK IF TODAY EXISTS IN DATABASE
        todayDate = Calendar.getInstance().getTime().toString();
        sharedViewModel.getDate(todayDate).observe(this, this::initProgress);

        todayDate = Utils.getTodayDate(Calendar.getInstance().getTime());
        date.setText(todayDate);
    }

    private void initProgress(ExerciseDate exerciseDate){
        if (exerciseDate != null) {
            circularProgress.setCurrentProgress(exerciseDate.getProgress());
        } else {
            circularProgress.setCurrentProgress(0);
        }
    }

    /**
    private void initUser() {
        userId = sharedPref.getLong("CurrentUser", 100);
        Log.e("exercise", "userId " + userId);
        sharedViewModel.getDates(userId).observe(this, this::showProgress);
    }

    private void showProgress(List<ExerciseDate> listDates) {
        Log.e("exercise", "list dates " + listDates.size());
        //SHOW TODAY PROGRESS IN EXERCISE PROGRAM
        for (int i = 0; i < listDates.size(); i++) {
            if (todayDate.equals(listDates.get(i).getDate())) {
                circularProgress.setCurrentProgress(listDates.get(i).getProgress());
                sharedPref.edit().putInt("TodayProgress", 0).apply();
            } else {
                circularProgress.setProgress(0, 100);
            }
        }
    }**/
}
