package com.oc.liza.kinedepoche.injections;

import com.oc.liza.kinedepoche.viewmodel.UserViewModel;
import com.oc.liza.kinedepoche.viewmodel.repositories.ExerciseDateRepository;
import com.oc.liza.kinedepoche.viewmodel.repositories.UserRepository;

import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final UserRepository mUserDataSource;
    private final ExerciseDateRepository mdateDataSource;
    private final Executor executor;

    ViewModelFactory(UserRepository userDataSource, ExerciseDateRepository dateDataSource, Executor executor) {
        this.mdateDataSource = dateDataSource;
        this.mUserDataSource = userDataSource;
        this.executor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(mUserDataSource, mdateDataSource, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
