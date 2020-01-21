package com.oc.liza.kinedepoche.controllers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.oc.liza.kinedepoche.R;
import com.oc.liza.kinedepoche.injections.Injection;
import com.oc.liza.kinedepoche.injections.ViewModelFactory;
import com.oc.liza.kinedepoche.models.User;
import com.oc.liza.kinedepoche.viewmodel.UserViewModel;

import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.user_name_text_input)
    TextInputEditText userNameInput;

    @BindView(R.id.btn_save)
    MaterialButton buttonSave;


    public static final String CHANNEL_ID = "NOTIFICATION CHANNEL";
    public UserViewModel viewModel;
    private Boolean loggedIn = false;
    protected SharedPreferences sharedPref;

    @Override
    protected void onResume() {
        super.onResume();
        loggedIn = sharedPref.getBoolean("LoggedIn", false);
        if (loggedIn) {
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sharedPref = this.getSharedPreferences("KineDePoche", Context.MODE_PRIVATE);
        initViewModel();
        initButton();
        createNotificationChannel();
    }

    private void initViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.viewModel = ViewModelProviders.of(this, mViewModelFactory).get(UserViewModel.class);
    }

    private void initButton() {
        buttonSave.setOnClickListener(v -> logInWithUserName());
    }

    private void logInWithUserName() {
        if (!Objects.requireNonNull(userNameInput.getText()).toString().isEmpty()) {
            viewModel.getAllUsers().observe(this, this::logInUser);
        } else {
            Toast.makeText(this, "Please choose a user name", Toast.LENGTH_SHORT).show();
        }
    }


    private void logInUser(List<User> list) {
        //CHECK IF FIRST TIME USER
        String userName = Objects.requireNonNull(userNameInput.getText()).toString();
        for (User user : list) {
            if (user.getName().equals(userName)) {
                loggedIn = true;
                sharedPref.edit().putLong("CurrentUser", user.getId()).apply();
                sharedPref.edit().putString("CurrentUserName", user.getName()).apply();
                sharedPref.edit().putBoolean("LoggedIn", true).apply();

                startActivity(new Intent(this, ProfileActivity.class));
            }
        }
        if (!loggedIn) {
            //CREATE USER
            User newUser = new User();
            newUser.setName(userName);
            viewModel.createUser(newUser);
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
        //Save the Channel Id in shared preferences
        SharedPreferences preferences = getSharedPreferences("KineDePoche", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("CHANNEL_KEY", CHANNEL_ID).apply();

    }

}
