package com.oc.liza.kinedepoche.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.oc.liza.kinedepoche.R;

import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class ExerciseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        //SET LISTENER TO IMAGE VIEW
        Bundle args=new Bundle();

        ImageView exercise= Objects.requireNonNull(getView()).findViewById(R.id.desk_exercises_image);
        exercise.setClickable(true);
        exercise.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.next_action, args));
        //SHOX PROGRESS IN EXERCISE PROGRAM
        CircularProgressIndicator circularProgress = Objects.requireNonNull(getView()).findViewById(R.id.circular_progress);
        circularProgress.setCurrentProgress(100);
        circularProgress.setProgress(20, 100);
    }
}
