package com.oc.liza.kinedepoche.viewmodel.repositories

import androidx.lifecycle.LiveData
import com.oc.liza.kinedepoche.database.dao.ExerciseDao
import com.oc.liza.kinedepoche.models.Exercise

class ExerciseRepository(val exerciseDao: ExerciseDao) {

    // --- GET ---
    fun getExercisesByDate(dateId: Long): LiveData<List<Exercise>> {
        return this.exerciseDao.getExercisesByDate(dateId)
    }

    fun getAllExercises(): LiveData<List<Exercise>> {
        return this.exerciseDao.getAllExercises()
    }

    // --- CREATE ---
    fun insertExercise(exercise: Exercise) {
        exerciseDao.insertExercise(exercise)
    }

    fun insertListOfExercises(list: List<Exercise>) {
        exerciseDao.insertListOfExercise(list)
    }

    // --- DELETE ---
    fun deleteExercise(id: Long) {
        exerciseDao.deleteExercise(id)
    }

    // --- UPDATE ---
    fun updateExercise(exercise: Exercise) {
        exerciseDao.updateExercise(exercise)
    }
}