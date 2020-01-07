package com.oc.liza.kinedepoche.controllers;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.oc.liza.kinedepoche.NotifyWorker;
import com.oc.liza.kinedepoche.R;
import com.oc.liza.kinedepoche.models.User;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.navigation.Navigation;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import butterknife.BindView;

import static androidx.navigation.Navigation.createNavigateOnClickListener;

public class SettingsFragment extends BaseFragment {

    @BindView(R.id.user_name_text_input)
    TextInputEditText inputEditText;
    @BindView(R.id.btn_update)
    Button btnUpdate;
    @BindView(R.id.btn_logout)
    MaterialButton btnLogOut;
    @BindView(R.id.notification_switch)
    Switch switchNotify;
    @BindView(R.id.not_logged_in)
    TextView userNotLoggedIn;

    private User user;

    private static final String workTag = "notificationWork";

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutView() {
        return R.layout.fragment_settings;
    }

    @Override
    public void initView() {
        switchNotify.setChecked(sharedPref.getBoolean("SwitchIsChecked", false));
        userId = sharedPref.getLong("CurrentUser", 1);
        sharedViewModel.getUser(userId).observe(this, this::initUser);

        if (sharedPref.getBoolean("LoggedIn", false)) {
            btnUpdate.setOnClickListener(v -> updateUserName());
            btnLogOut.setOnClickListener(v -> logOutUser());
            switchNotify.setOnClickListener(v -> activateNotification());
        } else {
            userNotLoggedIn.setVisibility(View.VISIBLE);
        }
    }

    private void initUser(User result) {
        user = result;
    }

    private void updateUserName() {
        if (inputEditText.getText() != null) {
            String newUserName = inputEditText.getText().toString();
            user.setName(newUserName);
            sharedViewModel.updateUser(user);
            sharedPref.edit().putString("CurrentUserName", user.getName()).apply();
        } else {
            Toast.makeText(getActivity(), "Please enter your new username", Toast.LENGTH_SHORT).show();
        }
    }

    private void logOutUser() {
        sharedPref.edit().putBoolean("LoggedIn", false).apply();
        sharedPref.edit().putLong("CurrentUser", 100).apply();
        Toast.makeText(getActivity(), "You're logged out", Toast.LENGTH_SHORT).show();
    }

    private void activateNotification() {
        if (switchNotify.isChecked()) {
            Log.e("settingfr", "activate notification");
            sharedPref.edit().putBoolean("SwitchIsChecked", true).apply();

            PeriodicWorkRequest.Builder notificationBuilder =
                    new PeriodicWorkRequest.Builder(NotifyWorker.class, 15, TimeUnit.MINUTES)
                            .addTag(workTag);
            PeriodicWorkRequest request = notificationBuilder.build();
            WorkManager.getInstance(Objects.requireNonNull(getActivity())).enqueueUniquePeriodicWork("reminder",
                    ExistingPeriodicWorkPolicy.REPLACE, request);
        } else {
            Log.e("settings", "cancel notification");
            sharedPref.edit().putBoolean("SwitchIsChecked", false).apply();
            cancelNotification();
        }
    }

    private void cancelNotification() {
        if (getActivity() != null)
            WorkManager.getInstance(getActivity()).cancelAllWorkByTag(workTag);
    }
}
