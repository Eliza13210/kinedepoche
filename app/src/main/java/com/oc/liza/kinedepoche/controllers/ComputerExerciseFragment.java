package com.oc.liza.kinedepoche.controllers;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.material.button.MaterialButton;
import com.oc.liza.kinedepoche.ExerciseInitializer;
import com.oc.liza.kinedepoche.R;
import com.oc.liza.kinedepoche.models.Exercise;
import com.oc.liza.kinedepoche.models.ExerciseDate;
import com.oc.liza.kinedepoche.models.User;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import butterknife.BindView;

public class ComputerExerciseFragment extends BaseFragment {

    @BindView(R.id.ic_left)
    ImageView iconLeft;
    @BindView(R.id.ic_right)
    ImageView iconRight;
    @BindView(R.id.exercise_number)
    TextView exerciseNumber;
    @BindView(R.id.exercise_video_view)
    VideoView videoView;
    @BindView(R.id.description_exercise)
    TextView description;
    @BindView(R.id.circular_progress)
    CircularProgressIndicator circularProgress;
    @BindView(R.id.ic_timer)
    ImageView timerIcon;
    @BindView(R.id.timer_text)
    TextView timerText;
    @BindView(R.id.ic_repeat)
    ImageView repeatIcon;
    @BindView(R.id.repeat_text)
    TextView repeatText;
    @BindView(R.id.ic_pause)
    ImageView pauseIcon;
    @BindView(R.id.btn_start)
    MaterialButton btn_start;

    private ExerciseInitializer initializer;
    private int lastExercise = 0;
    private int lastVideo = 1;
    private ExerciseDate exerciseDate;
    private int count;
    private int repeat;
    private CountDownTimer timer;

    public ComputerExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        //Hide toolbar
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();

        initExercises();
        initUser();
        initNavigation();
    }


    private void initExercises() {
        initializer = new ExerciseInitializer(getActivity());
        initializer.initExerciseProgram();
    }

    private void initNavigation() {
        iconLeft.setOnClickListener(v -> clickLeft());
        iconRight.setOnClickListener(v -> clickRight());
    }

    private void clickLeft() {
        if (lastExercise > 0) {
            lastExercise--;
        } else {
            lastExercise = initializer.getCount();
        }
        updateExercise();
    }

    private void clickRight() {
        if (lastExercise < initializer.getCount()) {
            lastExercise++;
        } else {
            lastExercise = 0;
        }
        updateExercise();
    }

    private void initUser() {
        userId = sharedPref.getLong("CurrentUser", 100);
        sharedViewModel.getUser(userId).observe(this, this::showExercise);
    }

    private void showExercise(User user) {
        lastExercise = user.getLast_exercise();
        Log.e("ex", "ex no " + lastExercise);
        updateExercise();
    }

    private void updateExercise() {
        Exercise exercise = initializer.getExercise(lastExercise);

        //SET EXERCISE NUMBER IN NAVIGATION
        exerciseNumber.setText(exercise.getNav_text());

        //DESCRIPTION
        description.setText(exercise.getDescription());

        //TIMER
        timerText.setText(exercise.getTime() + "s");

        //REPEAT
        repeatText.setText(String.valueOf(exercise.getRepeat()));

        //VIDEO
        // Uri videoUri = Uri.parse(exercise.getUrl());
        //MediaController mc = new MediaController(getActivity());
        //mc.setAnchorView(videoView);
        //mc.setMediaPlayer(videoView);
        //videoView.setMediaController(mc);
        //videoView.setVideoURI(videoUri);


    }


    private void initButton() {
        btn_start.setOnClickListener(v -> startTimer());
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(50000, 1000) {
            public void onTick(long millisUntilFinished) {
                //SHOW PAUSE BUTTON BIG AND CLICKABLE HANDLE CLICK
                //GREY START REPEAT
                //COUNT DOWN TIMER TEXT

            }

            public void onFinish() {
                //DONT CLICK PAUSE
                //COUNT++ CHECK IF COUNT=REPEAT THEN DO THE FOLLOWING;
                //IF NOT? PAUS 2 S AND REPEAT TIMER
                //UPDATE LAST EXERCISE USER
                //UPDATE PROGRESS
                //UPDATE EXERCISE DATE BOOLEAN CORRESPONDING EXERCISE
                //CHANGE EXERCISE FROM DATABASE

            }
        }.start();
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_computer_exercise;
    }

    @Override
    public void onPause() {
        super.onPause();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}