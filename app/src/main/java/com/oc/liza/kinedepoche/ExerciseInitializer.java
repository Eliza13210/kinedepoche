package com.oc.liza.kinedepoche;

import android.app.Activity;

import com.oc.liza.kinedepoche.models.Exercise;
import com.oc.liza.kinedepoche.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class ExerciseInitializer {

    private Activity activity;
    private String[] listOfVideos;
    private String[] listOfDescription;
    private String[] listOfNavText;
    private int[] listOfTime;

    private List<Exercise> listOfExercises;
    private int numberOfExercises = 5;

    public ExerciseInitializer(Activity callingActivity) {
        this.activity = callingActivity;
        initArrays();
    }

    private void initArrays() {
        listOfVideos = activity.getResources().getStringArray(R.array.video_url_array);
        listOfDescription = activity.getResources().getStringArray(R.array.description_array);
        listOfTime = activity.getResources().getIntArray(R.array.time_array);
        listOfNavText = activity.getResources().getStringArray(R.array.exercise_number_array);
    }

    public void initExerciseProgram() {
        listOfExercises = new ArrayList<>();
        for (int i = 0; i < numberOfExercises; i++) {
            Exercise exercise = new Exercise((long) i, null, listOfVideos[i], listOfDescription[i], listOfNavText[i],
                    listOfTime[i], false);
            listOfExercises.add(exercise);
        }
    }

    public List<Exercise> getListOfExercises() {
        return this.listOfExercises;
    }

    public Exercise getExercise(int id) {
        return listOfExercises.get(id);
    }

    public void addExercisesToDatabase(UserViewModel viewModel, List<Exercise> list, long dateId) {
        for (Exercise exercise : list) {
            exercise.setDateId(dateId);
        }
        viewModel.insertListOfExercises(list);
    }

    public int getCount() {
        return listOfExercises.size();
    }
}
