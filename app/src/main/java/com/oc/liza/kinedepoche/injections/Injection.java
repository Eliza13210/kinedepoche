package com.oc.liza.kinedepoche.injections;

import android.content.Context;

import com.oc.liza.kinedepoche.database.UserDatabase;
import com.oc.liza.kinedepoche.viewmodel.repositories.ExerciseDateRepository;
import com.oc.liza.kinedepoche.viewmodel.repositories.ExerciseRepository;
import com.oc.liza.kinedepoche.viewmodel.repositories.UserRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {
    public static UserRepository provideUserDataSource(Context context) {
        UserDatabase database = UserDatabase.getInstance(context);
        return new UserRepository(database.userDao());
    }

    public static ExerciseDateRepository provideExerciseDateDataSource(Context context) {
        UserDatabase database = UserDatabase.getInstance(context);
        return new ExerciseDateRepository(database.dateDao());
    }

    public static ExerciseRepository provideExerciseDataSource(Context context) {
        UserDatabase database = UserDatabase.getInstance(context);
        return new ExerciseRepository(database.exerciseDao());
    }

    public static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        UserRepository dataSourceUser = provideUserDataSource(context);
        ExerciseDateRepository dataSourceDate = provideExerciseDateDataSource(context);
        ExerciseRepository dataSourceExercise = provideExerciseDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceUser, dataSourceDate, dataSourceExercise, executor);
    }
}
