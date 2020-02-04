package com.oc.liza.kinedepoche.database.dao;

import com.oc.liza.kinedepoche.models.Exercise;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ExerciseDao {

    @Query("SELECT * FROM Exercise WHERE dateId = :id")
    LiveData<List<Exercise>> getExercisesByDate(long id);

    @Query("SELECT * FROM Exercise ")
    LiveData<List<Exercise>> getAllExercises();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertExercise(Exercise exercise);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertListOfExercise(List<Exercise> listOfExercises);

    @Update
    int updateExercise(Exercise exercise);

    @Query("DELETE FROM Exercise WHERE id = :exerciseId")
    int deleteExercise(long exerciseId);
}
