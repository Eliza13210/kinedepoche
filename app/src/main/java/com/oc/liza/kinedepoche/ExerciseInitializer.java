package com.oc.liza.kinedepoche;

import android.app.Activity;

import com.oc.liza.kinedepoche.models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseInitializer {

    private Activity activity;
    private String[] listOfVideos;
    private String[] listOfDescription;
    private String[] listOfNavText;
    private int[] listOfTime;
    private int[] listOfRepeat;

    private List<Exercise> listOfExercises;

    public ExerciseInitializer(Activity callingActivity) {
        this.activity = callingActivity;
        initArrays();
    }

    private void initArrays() {
        listOfVideos = activity.getResources().getStringArray(R.array.video_url_array);
        listOfDescription = activity.getResources().getStringArray(R.array.description_array);
        listOfTime = activity.getResources().getIntArray(R.array.time_array);
        listOfRepeat = activity.getResources().getIntArray(R.array.repeat_array);
        listOfNavText = activity.getResources().getStringArray(R.array.exercise_number_array);
    }

    public void initExerciseProgram() {
        listOfExercises = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Exercise exercise = new Exercise(i, listOfVideos[i], listOfDescription[i], listOfNavText[i], listOfTime[i], listOfRepeat[i]);
            listOfExercises.add(exercise);
        }
    }

    public Exercise getExercise(int number) {
        return listOfExercises.get(number);
    }

    public int getCount() {
        return listOfExercises.size();
    }
}
