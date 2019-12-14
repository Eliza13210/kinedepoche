package com.oc.liza.kinedepoche;

import com.oc.liza.kinedepoche.models.ExerciseDate;
import com.oc.liza.kinedepoche.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)

public class UserDaoTest {

        // DATA SET FOR TEST
        private static long USER_ID = 1;
        private static User USER_DEMO = new User(USER_ID, true, 2);


        // FOR DATA
        private UserDatabase database;

        private static ExerciseDate NEW_DATE = new ExerciseDate(USER_ID, "2019-01-01", false, true, false, false, false);

        @Rule
        public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

        @Before
        public void initDb() throws Exception {
            this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                    UserDatabase.class)
                    .allowMainThreadQueries()
                    .build();
        }

        @After
        public void closeDb() throws Exception {
            database.close();
        }

        @Test
        public void insertAndGetUser() throws InterruptedException {
            // BEFORE : Adding a new user
            this.database.userDao().createUser(USER_DEMO);
            // TEST
            User user = LiveDataTestUtil.getValue(this.database.userDao().getRealEstate(USER_ID));
            assertTrue(user.getType().equals(USER_DEMO.getType()) && user.getId() == USER_ID);
        }


        @Test
        public void getPhotosWhenNoPhotosInserted() throws InterruptedException {
            // TEST
            List<ExerciseDate> dates = LiveDataTestUtil.getValue(this.database.dateDao().getPhotos(USER_ID));
            assertTrue(dates.isEmpty());
        }

        @Test
        public void insertAndGetItems() throws InterruptedException {
            // BEFORE : Adding demo real estate & demo photo

            this.database.userDao().createRealEstate(USER_DEMO);
            this.database.dateDao().insertPhoto(NEW_DATE);

            // TEST
            List<ExerciseDate> dates = LiveDataTestUtil.getValue(this.database.dateDao().getPhotos(USER_ID));
            assertTrue(dates.size() == 1);
        }

        @Test
        public void insertAndUpdateItem() throws InterruptedException {
            // BEFORE : Adding demo real estate & demo photo. Next, update photo added & re-save it
            this.database.userDao().createRealEstate(USER_DEMO);
            this.database.userDao().insertPhoto(NEW_DATE);
            ExerciseDate dateAdded = LiveDataTestUtil.getValue(this.database.dateDao().getPhotos(USER_ID)).get(0);

            this.database.dateDao().updatePhoto(dateAdded);

            //TEST
            List<ExerciseDate> dates = LiveDataTestUtil.getValue(this.database.dateDao().getPhotos(USER_ID));
            assertTrue(dates.size() == 1 );

        }

        @Test
        public void insertAndDeleteItem() throws InterruptedException {
            // BEFORE : Adding demo user & demo item. Next, get the item added & delete it.
            this.database.userDao().createRealEstate(USER_DEMO);
            this.database.dateDao().insertPhoto(NEW_DATE);
            ExerciseDate itemAdded = LiveDataTestUtil.getValue(this.database.dateDao().getPhotos(USER_ID)).get(0);
            this.database.dateDao().deletePhoto(itemAdded.getId());

            //TEST
            List<ExerciseDate> items = LiveDataTestUtil.getValue(this.database.dateDao().getPhotos(USER_ID));
            assertTrue(items.isEmpty());
        }
    }
}
