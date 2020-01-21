package com.oc.liza.kinedepoche.controllers;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oc.liza.kinedepoche.R;
import com.oc.liza.kinedepoche.Utils;
import com.oc.liza.kinedepoche.models.ExerciseDate;

import java.util.Calendar;

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
    @BindView(R.id.trophy)
    LinearLayout trophy;


    @Override
    public int getLayoutView() {
        return R.layout.fragment_exercise;
    }

    @Override
    public void initView() {
        initClickableImageView();
        initDate();
    }

    private void initClickableImageView() {
        exerciseImageView.setClickable(true);
        exerciseImageView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.next_action, null));
    }

    private void initDate() {
        String todayDate = Utils.getTodayDate(Calendar.getInstance().getTime());
        date.setText(todayDate);

        //CHECK IF TODAY EXISTS IN DATABASE
        sharedViewModel.getDate(todayDate, userId).observe(this, this::initProgress);
    }

    private void initProgress(ExerciseDate exerciseDate) {
        if (exerciseDate != null) {
            circularProgress.setCurrentProgress(exerciseDate.getProgress());
            if (exerciseDate.getProgress() == 100) {
                trophy.setVisibility(View.VISIBLE);
            } else {
                trophy.setVisibility(View.GONE);
            }
        } else {
            circularProgress.setCurrentProgress(0);
        }
    }
}
