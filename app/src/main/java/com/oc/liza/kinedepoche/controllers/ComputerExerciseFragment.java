package com.oc.liza.kinedepoche.controllers;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.material.button.MaterialButton;
import com.oc.liza.kinedepoche.ExerciseInitializer;
import com.oc.liza.kinedepoche.R;
import com.oc.liza.kinedepoche.models.Exercise;
import com.oc.liza.kinedepoche.models.ExerciseDate;
import com.oc.liza.kinedepoche.models.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
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
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
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

    private User user;
    private int lastExercise = 0;
    private boolean isDone = false;

    private ExerciseDate exerciseDate;
    private String todayDate;
    private List<Integer> listInt = new ArrayList<>();
    private Exercise exercise;

    private String uri;
    private Uri videoUri;

    private int progressBarValue = 0;
    private int timerCount;

    public ComputerExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        //Hide toolbar
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();

        initExercisesAndUser();
        initExerciseDate();
        initNavigation();
        initButton();
    }

    private void initExercisesAndUser() {
        initializer = new ExerciseInitializer(getActivity());
        initializer.initExerciseProgram();

        userId = sharedPref.getLong("CurrentUser", 1);
        sharedViewModel.getUser(userId).observe(this, this::initUser);

        Log.e("exercise", "number of exercises" + initializer.getCount() + " userid " + userId);
    }

    private void initUser(User result) {
        user = result;
        Log.e("exercise", "user=" + user.toString());

    }

    private void initExerciseDate() {
        todayDate = Calendar.getInstance().getTime().toString();
        //CHECK IF USER HAS STARTED EXERCISES TODAY
        sharedViewModel.getDate(todayDate, userId).observe(this, this::createDate);
    }

    private void createDate(ExerciseDate date) {
        if (date == null) {
            exerciseDate = new ExerciseDate(null, userId, todayDate, 0, null);
            updateExercise();
        } else {
            exerciseDate = date;
            //GET COMPLETED EXERCISES
            if (exerciseDate.getCompletedExercises() != null) {
                String[] array = exerciseDate.getCompletedExercises().split("");
                Log.e("exercise", "array lenght= " + array.length + array);
                //CONVERT TO INT ARRAY
                for (String s : array) {
                    listInt.add(Integer.parseInt(s));
                }
            }
            Log.e("exercise", "listint= " + listInt);
            lastExercise = user.getLast_exercise();

            Log.e("ex", "ex no " + lastExercise);
            updateExercise();
        }
    }

    private void initNavigation() {
        iconLeft.setOnClickListener(v -> clickLeft());
        iconRight.setOnClickListener(v -> clickRight());
    }

    private void clickLeft() {
        if (lastExercise > 0) {
            lastExercise--;
        } else {
            lastExercise = initializer.getCount() - 1;
        }
        updateExercise();
    }

    private void clickRight() {
        if (lastExercise < initializer.getCount() - 1) {
            lastExercise++;
        } else {
            lastExercise = 0;
        }
        updateExercise();
    }

    private void updateExercise() {
        exercise = initializer.getExercise(lastExercise);

        //SET EXERCISE NUMBER IN NAVIGATION
        exerciseNumber.setText(exercise.getNav_text());

        //DESCRIPTION
        description.setText(exercise.getDescription());
        for (int i = 0; i < listInt.size(); i++) {
            if (listInt.get(i) == lastExercise) {
                description.setText("DONE \n" + exercise.getDescription());
                isDone = true;
            } else {
                isDone = false;
            }
        }

        //TIMER
        timerCount = exercise.getTime();
        timerText.setText(String.valueOf(timerCount));

        //REPEAT
        repeatText.setText(String.valueOf(exercise.getRepeat()));

        //SET VIDEO PREVIEW
        uri = "android.resource://" + getActivity().getPackageName() + "/";
        videoUri = Uri.parse(uri + R.raw.test);
        videoView.setVideoURI(videoUri);
        videoView.seekTo(1);
        Log.e("exercise ", videoUri.toString());
    }


    private void initButton() {
        btn_start.setOnClickListener(v -> startTimer());
    }

    private void startTimer() {
        btn_start.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        playVideo();
        ExerciseAsyncTask startExercise = new ExerciseAsyncTask();
        startExercise.execute();

    }

    private class ExerciseAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            /*
             *    do things before doInBackground() code runs
             *    such as preparing and showing a Dialog or ProgressBar
             */
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            /*
             *    updating data
             *    such a Dialog or ProgressBar
             */
            progressBar.setProgress(progressBarValue);
            timerText.setText(String.valueOf(timerCount));
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (progressBarValue < 100) {
                progressBarValue++;
                if (timerCount > 0 && progressBarValue % 10 == 0) timerCount--;
                publishProgress();
                try {
                    Thread.sleep(exercise.getTime() * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            btn_start.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            progressBarValue = 0;
            timerCount = exercise.getTime();
            updateExerciseDate();
        }
    }

    private void playVideo() {

        Uri videoUri = Uri.parse(uri + R.raw.test);

        MediaController mc = new MediaController(getActivity());
        mc.setVisibility(View.GONE);
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);

        videoView.setMediaController(mc);
        videoView.setVideoURI(videoUri);

        videoView.start();
    }

    private void updateExerciseDate() {
        //UPDATE LAST EXERCISE USER
        if (lastExercise < initializer.getCount()) user.setLast_exercise(lastExercise);
        sharedViewModel.updateUser(user);

        //CHECK IF EXERCISE IS ALREADY DONE TODAY
        if (!isDone) {
            listInt.add(lastExercise);
            exerciseDate.setCompletedExercises(String.valueOf(listInt));
            int progress = exerciseDate.getProgress() + 100 / initializer.getCount();
            exerciseDate.setProgress(progress);
            sharedViewModel.updateDate(exerciseDate);
        }
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