package com.oc.liza.kinedepoche.database.dao;

import android.database.Cursor;

import com.oc.liza.kinedepoche.models.ExerciseDate;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DateDao {
    @Query("SELECT * FROM ExerciseDate WHERE userId = :userId")
    LiveData<List<ExerciseDate>> getDates(long userId);

    @Query("SELECT * FROM ExerciseDate WHERE date = :date")
    LiveData<ExerciseDate> getDate(String date);

    @Query("SELECT * FROM ExerciseDate WHERE userId = :userId")
    Cursor getDateWithCursor(long userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertDate(ExerciseDate date);

    @Update
    int updateDate(ExerciseDate date);

    @Query("DELETE FROM ExerciseDate WHERE id = :dateId")
    int deleteDate(long dateId);
}
