package com.oc.liza.kinedepoche.database;

import android.content.Context;

import com.oc.liza.kinedepoche.database.dao.DateDao;
import com.oc.liza.kinedepoche.database.dao.ExerciseDao;
import com.oc.liza.kinedepoche.database.dao.UserDao;
import com.oc.liza.kinedepoche.models.Exercise;
import com.oc.liza.kinedepoche.models.ExerciseDate;
import com.oc.liza.kinedepoche.models.User;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, ExerciseDate.class, Exercise.class}, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile UserDatabase INSTANCE;

    // --- DAO ---
    public abstract UserDao userDao();

    public abstract DateDao dateDao();

    public abstract ExerciseDao exerciseDao();

    // --- INSTANCE ---
    public static UserDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (UserDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UserDatabase.class, "UserDatabase.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
