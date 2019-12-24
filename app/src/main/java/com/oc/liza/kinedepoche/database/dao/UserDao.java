package com.oc.liza.kinedepoche.database.dao;

import android.database.Cursor;

import com.oc.liza.kinedepoche.models.User;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM User WHERE id = :id")
    LiveData<User> getUser(long id);

    @Query("SELECT * FROM User WHERE name = :name")
    LiveData<User> getUserByName(String name);

    @Query("SELECT * FROM User WHERE id = :id")
    Cursor getUserWithCursor(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createUser(User user);

    @Update
    int updateUser(User user);

    @Query("DELETE FROM User WHERE id = :userId")
    int deleteUser(long userId);
}
