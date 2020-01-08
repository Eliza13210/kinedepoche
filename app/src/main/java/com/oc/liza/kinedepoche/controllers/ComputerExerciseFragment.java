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
import com.oc.liza.kinedepoche.Utils;
import com.oc.liza.kinedepoche.models.Exercise;
import com.oc.liza.kinedepoche.models.ExerciseDate;
import com.oc.liza.kinedepoche.models.User;

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

    private ExerciseInitializer initializer;

    private User user;
    private int lastExercise = 0;
    private boolean navigationPossible = true;

    private ExerciseDate exerciseDate;
    private String todayDate;

    private Exercise exercise;
    private List<Exercise> listOfExercise;

    private String uri;
    private Uri videoUri;

    private int progressBarValue = 0;
    private int timerCount;

    private ExerciseAsyncTask startExercise;

    private boolean firstClick;

    public ComputerExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_computer_exercise;
    }

    @Override
    public void initView() {
        initExercisesAndUser();
        initNavigation();
        initButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Hide toolbar
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
    }

    private void initExercisesAndUser() {
        initializer = new ExerciseInitializer(getActivity());
        initializer.initExerciseProgram();
        sharedViewModel.getUser(userId).observe(this, this::initUser);
    }

    private void initUser(User result) {
        user = result;
        initExerciseDate();
    }

    private void initExerciseDate() {
        todayDate = Utils.getTodayDate(Calendar.getInstance().getTime());
        //CHECK IF USER HAS STARTED EXERCISES TODAY
        sharedViewModel.getDate(todayDate, userId).observe(this, this::createDate);
    }

    private void createDate(ExerciseDate date) {
        if (date == null) {
            //IF EXERCISE DATE DOESN'T EXIST IN DATABASE, CREATE ONE FOR TODAY
            user.setLast_exercise(0);
            sharedViewModel.updateUser(user);
            Log.e("ex", "date is null and last ex=" + lastExercise);

            exerciseDate = new ExerciseDate(null, userId, todayDate, 0);
            sharedViewModel.createDate(exerciseDate);
        } else {
            exerciseDate = date;
            Log.e("ex", "date is NOT null and progress=" + date.getProgress() + "userId=" + userId + "date=" + todayDate);
            lastExercise = user.getLast_exercise();
            Log.e("last ex", "is" + lastExercise);
            sharedViewModel.getExercisesByDate(exerciseDate.getId()).observe(this, this::addToList);
        }
    }

    private void addToList(List<Exercise> list) {
        if (list.isEmpty()) {
            //CREATE EXERCISES FOR TODAY IN DATABASE
            listOfExercise = initializer.getListOfExercises();
            initializer.addExercisesToDatabase(sharedViewModel, listOfExercise, exerciseDate.getId());
            Log.e("create", "id=" + exerciseDate.getId());
        } else {
            listOfExercise = list;
        }
        Log.e("ex", "list of  ex " + list.size());

        updateExercise();
    }

    private void initNavigation() {
        iconLeft.setOnClickListener(v -> clickLeft());
        iconRight.setOnClickListener(v -> clickRight());
    }

    private void clickLeft() {
        if (navigationPossible) {
            if (lastExercise > 0) {
                lastExercise--;
            } else {
                lastExercise = listOfExercise.size() - 1;
            }
            updateExercise();
        }
    }

    private void clickRight() {
        if (navigationPossible) {
            if (lastExercise < listOfExercise.size() - 1) {
                lastExercise++;
            } else {
                lastExercise = 0;
            }
            updateExercise();
        }
    }

    private void updateExercise() {
        exercise = listOfExercise.get(lastExercise);

        //SET EXERCISE NUMBER IN NAVIGATION
        exerciseNumber.setText(exercise.getNav_text());

        //DESCRIPTION
        description.setText(exercise.getDescription());
        if (exercise.getCompleted()) {
            description.setText("DONE \n" + exercise.getDescription());
        }

        //TIMER
        timerCount = exercise.getTime();
        timerText.setText(String.valueOf(timerCount));

        //REPEAT
        repeatText.setText(String.valueOf(exercise.getRepeat()));

        //SET VIDEO PREVIEW
        uri = "android.resource://" + getActivity().getPackageName() + "/";

        int raw = getResources().getIdentifier(exercise.getUrl(), "raw", getActivity().getPackageName());

        videoUri = Uri.parse(uri + raw);
        videoView.setVideoURI(videoUri);
        videoView.seekTo(1);
        Log.e("exercise ", videoUri.toString());
    }


    private void initButton() {
        pauseIcon.setOnClickListener(v -> pauseVideo());
    }

    private void pauseVideo() {
        if (firstClick) {
            videoView.start();
            pauseIcon.setImageResource(R.drawable.outline_pause_circle_outline_black_24);
            firstClick = false;
        } else {
            if (videoView.isPlaying()) {
                videoView.pause();
                pauseIcon.setImageResource(R.drawable.outline_pause_circle_outline_black_24);
                //PAUSE PROGRESS AND TIMER COUNT!
            } else {
                if (startExercise == null) {
                    startTimer();
                }
                videoView.start();
                pauseIcon.setImageResource(R.drawable.outline_play_circle_outline_black_24);
            }
        }
    }

    private void startTimer() {
        playVideo();
        startExercise = new ExerciseAsyncTask();
        startExercise.execute();
    }

    private class ExerciseAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            navigationPossible = false;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
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
            navigationPossible = true;
            progressBarValue = 0;
            timerCount = exercise.getTime();
            timerText.setText(String.valueOf(timerCount));
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
        videoView.setOnCompletionListener(mp -> {
            // Video Playing is completed
            firstClick=true;
            pauseIcon.setImageResource(R.drawable.outline_play_circle_outline_black_24);
            updateExerciseDate();
        });
    }

    private void updateExerciseDate() {

        Log.e("update", "updated");
        //UPDATE LAST EXERCISE USER
        user.setLast_exercise(lastExercise);
        sharedViewModel.updateUser(user);

        //UPDATE EXERCISE AND PROGRESS IF NOT ALREADY COMPLETED
        if (!exercise.getCompleted()) {
            exercise.setCompleted(true);
            sharedViewModel.updateExercise(exercise);

            //UPDATE TODAY DATE PROGRESS
            int progress = exerciseDate.getProgress() + 100 / listOfExercise.size();
            exerciseDate.setProgress(progress);
            sharedViewModel.updateDate(exerciseDate);

            Log.e("update", "last ex=" + lastExercise + "progress=" + exerciseDate.getProgress());
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (videoView.isPlaying()) videoView.pause();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}