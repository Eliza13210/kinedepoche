package com.oc.liza.kinedepoche.viewmodel;

import com.oc.liza.kinedepoche.models.Exercise;
import com.oc.liza.kinedepoche.models.ExerciseDate;
import com.oc.liza.kinedepoche.models.User;
import com.oc.liza.kinedepoche.viewmodel.repositories.ExerciseDateRepository;
import com.oc.liza.kinedepoche.viewmodel.repositories.ExerciseRepository;
import com.oc.liza.kinedepoche.viewmodel.repositories.UserRepository;

import java.util.List;
import java.util.concurrent.Executor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {

    // REPOSITORIES
    private final UserRepository mUserRepository;
    private final ExerciseDateRepository mExerciseDateRepository;
    private final ExerciseRepository mExerciseRepository;
    private final Executor executor;

    public UserViewModel(UserRepository userRepository, ExerciseDateRepository exerciseDateRepository, ExerciseRepository exerciseRepository,
                         Executor executor) {
        this.mUserRepository = userRepository;
        this.mExerciseDateRepository = exerciseDateRepository;
        this.mExerciseRepository = exerciseRepository;
        this.executor = executor;
    }

    // -------------
    // FOR USER
    // -------------

    public LiveData<User> getUser(long userId) {
        return mUserRepository.getUser(userId);
    }

    public LiveData<User> getUserByName(String userName) {
        return mUserRepository.getUserByName(userName);
    }

    public LiveData<List<User>> getAllUsers() {
        return mUserRepository.getAllUsers();
    }

    public void createUser(User user) {
        executor.execute(() -> mUserRepository.createUser(user));
    }

    public void updateUser(User user) {
        executor.execute(() -> mUserRepository.updateUser(user));
    }

    // -------------
    // FOR EXERCISE DATE
    // -------------

    public LiveData<List<ExerciseDate>> getDates(long userId) {
        return mExerciseDateRepository.getDates(userId);
    }

    public LiveData<ExerciseDate> getDate(String date, long userId) {
        return mExerciseDateRepository.getDate(date, userId);
    }

    public void createDate(ExerciseDate date) {
        executor.execute(() -> mExerciseDateRepository.createDate(date));
    }

    public void updateDate(ExerciseDate date) {
        executor.execute(() -> mExerciseDateRepository.updateDate(date));
    }

    public void deleteDate(long dateId) {
        executor.execute(() -> mExerciseDateRepository.deleteDate(dateId));
    }

    // -------------
    // FOR EXERCISE
    // -------------

    public LiveData<List<Exercise>> getAllExercises() {
        return mExerciseRepository.getAllExercises();
    }

    public LiveData<List<Exercise>> getExercisesByDate(long dateId){
        return mExerciseRepository.getExercisesByDate(dateId);
    }

    public void insertExercise(Exercise exercise) {
        executor.execute(() -> mExerciseRepository.insertExercise(exercise));
    }

    public void insertListOfExercises(List<Exercise> list) {
        executor.execute(() -> mExerciseRepository.insertListOfExercises(list));
    }

    public void updateExercise(Exercise exercise) {
        executor.execute(() -> mExerciseRepository.updateExercise(exercise));
    }

    public void deleteExercise(long exerciseId) {
        executor.execute(() -> mExerciseRepository.deleteExercise(exerciseId));
    }
}
