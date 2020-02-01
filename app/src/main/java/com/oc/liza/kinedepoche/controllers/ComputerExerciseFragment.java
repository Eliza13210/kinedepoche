package com.oc.liza.kinedepoche.controllers;

import android.net.Uri;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.oc.liza.kinedepoche.ExerciseInitializer;
import com.oc.liza.kinedepoche.R;
import com.oc.liza.kinedepoche.Utils;
import com.oc.liza.kinedepoche.models.Exercise;
import com.oc.liza.kinedepoche.models.ExerciseDate;
import com.oc.liza.kinedepoche.models.User;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;

public class ComputerExerciseFragment extends BaseFragment {

    @BindView(R.id.ic_left)
    ImageView iconLeft;
    @BindView(R.id.ic_right)
    ImageView iconRight;
    @BindView(R.id.done_text_view)
    TextView doneTextView;
    @BindView(R.id.exercise_number)
    TextView exerciseNumber;
    @BindView(R.id.exercise_video_view)
    VideoView videoView;
    @BindView(R.id.description_exercise)
    TextView description;
    @Nullable
    @BindView(R.id.description_frame_layout)
    FrameLayout description_frame_layout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ic_timer)
    ImageView timerIcon;
    @BindView(R.id.timer_text)
    TextView timerText;
    @BindView(R.id.ic_pause)
    ImageView pauseIcon;
    @BindView(R.id.progressbar_time)
    TextView progressbar_time_text_view;
    private ExerciseInitializer initializer;

    private User user;
    private int lastExercise = 0;
    private boolean navigationPossible = true;

    private ExerciseDate exerciseDate;
    private String todayDate;

    private Exercise exercise;
    private List<Exercise> listOfExercise;

    private Uri videoUri;

    private int timerCount;
    private Timer timer;
    private boolean firstClick = true;
    private boolean isTablet = false;

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
        isTablet = Objects.requireNonNull(getActivity()).getResources().getBoolean(R.bool.isTablet);
        //Hide toolbar if phone
        if (!isTablet)
            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
    }

    //ADD EXERCISES
    private void initExercisesAndUser() {
        initializer = new ExerciseInitializer(getActivity());
        initializer.initExerciseProgram();
        sharedViewModel.getUser(userId).observe(this, this::initUser);
    }

    private void initUser(User result) {
        user = result;
        initExerciseDate();
    }

    //CHECK IF USER HAS STARTED EXERCISES TODAY
    private void initExerciseDate() {
        todayDate = Utils.getTodayDate(Calendar.getInstance().getTime());
        sharedViewModel.getDate(todayDate, userId).observe(this, this::createDate);
    }

    //IF EXERCISE DATE DOESN'T EXIST IN DATABASE, CREATE ONE FOR TODAY
    private void createDate(ExerciseDate date) {
        if (date == null) {
            user.setLast_exercise(0);
            sharedViewModel.updateUser(user);
            exerciseDate = new ExerciseDate(null, userId, todayDate, 0);
            sharedViewModel.createDate(exerciseDate);
        } else {
            exerciseDate = date;
            lastExercise = user.getLast_exercise();
            sharedViewModel.getExercisesByDate(exerciseDate.getId()).observe(this, this::addToList);
        }
    }

    //CREATE EXERCISES FOR TODAY IN DATABASE IF THEY HAVEN'T YET BEEN ADDED TODAY
    private void addToList(List<Exercise> list) {
        if (list.isEmpty()) {
            listOfExercise = initializer.getListOfExercises();
            initializer.addExercisesToDatabase(sharedViewModel, listOfExercise, exerciseDate.getId());
        } else {
            listOfExercise = list;
        }
        updateExercise();
    }

    //NAVIGATION ON TOP
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

    //SHOW CURRENT EXERCISE
    private void updateExercise() {
        exercise = listOfExercise.get(lastExercise);

        //SET EXERCISE NUMBER IN NAVIGATION
        exerciseNumber.setText(exercise.getNav_text());

        //DESCRIPTION
        description.setText(exercise.getDescription());
        if (exercise.getCompleted()) {
            doneTextView.setVisibility(View.VISIBLE);
        } else {
            doneTextView.setVisibility(View.GONE);
        }
        //SET VIDEO PREVIEW
        String uri = "android.resource://" + Objects.requireNonNull(getActivity()).getPackageName() + "/";
        int raw = getResources().getIdentifier(exercise.getUrl(), "raw", getActivity().getPackageName());

        videoUri = Uri.parse(uri + raw);
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.setOnPreparedListener(mediaPlayer -> {
            videoView.seekTo(1);
        });
        //TIMER
        timerCount = exercise.getTime();
        timerText.setText(Utils.returnInMinutes(timerCount));

        //INIT PROGRESSBAR
        progressBar.setProgress(0);
        progressbar_time_text_view.setText(Utils.returnInMinutes(timerCount));
    }

    //Start or pause video
    private void initButton() {
        pauseIcon.setOnClickListener(v -> pauseOrPlayVideo());
    }

    private void pauseOrPlayVideo() {
        if (firstClick) {
            startTimer();
            pauseIcon.setImageResource(R.drawable.outline_pause_circle_outline_black_24);
            firstClick = false;
        } else {
            if (videoView.isPlaying()) {
                videoView.pause();
                pauseIcon.setImageResource(R.drawable.outline_play_circle_outline_black_24);
            } else {
                videoView.start();
                pauseIcon.setImageResource(R.drawable.outline_pause_circle_outline_black_24);
            }
        }
    }

    private void startTimer() {
        playVideo();
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    navigationPossible = false;

                    final int position = videoView.getCurrentPosition();
                    final int percentage = Math.round((position * 100 / (videoView.getDuration())));

                    progressBar.post(() -> progressBar.setProgress(percentage));
                    progressbar_time_text_view.post(() -> progressbar_time_text_view.setText(
                            Utils.returnInMinutes((videoView.getDuration() - position) / 1000)));
                }
            }, 0, 1000);
        }
    }

    private void playVideo() {
        if (description_frame_layout != null)
            description_frame_layout.setVisibility(View.INVISIBLE);

        MediaController mc = new MediaController(getActivity());
        mc.setVisibility(View.GONE);
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        videoView.setMediaController(mc);
        videoView.setVideoURI(videoUri);

        videoView.setOnCompletionListener(mp -> {
            // Video Playing is completed
            firstClick = true;
            pauseIcon.setImageResource(R.drawable.outline_play_circle_outline_black_24);

            if (timer != null) {
                timer.cancel();
                timer.purge();
                timer = null;
            }
            navigationPossible = true;
            timerCount = videoView.getDuration();

            if (description_frame_layout != null)
                description_frame_layout.setVisibility(View.VISIBLE);

            updateExerciseDate();
        });

        videoView.setOnPreparedListener(mediaPlayer -> videoView.start());
    }

    private void updateExerciseDate() {
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
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (videoView.isPlaying()) videoView.pause();
        if (!isTablet)
            ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar().show();
    }
}