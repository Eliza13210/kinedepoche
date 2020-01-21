package com.oc.liza.kinedepoche.controllers;

import android.content.Intent;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.oc.liza.kinedepoche.NotifyWorker;
import com.oc.liza.kinedepoche.R;
import com.oc.liza.kinedepoche.models.User;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import butterknife.BindView;

public class SettingsFragment extends BaseFragment {

    @BindView(R.id.user_name_text_input)
    TextInputEditText inputEditText;
    @BindView(R.id.btn_update)
    Button btnUpdate;
    @BindView(R.id.btn_logout)
    MaterialButton btnLogOut;
    @BindView(R.id.notification_switch)
    Switch switchNotify;

    private User user;
    private String newUserName;

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

        sharedViewModel.getUser(userId).observe(this, this::initUser);

        btnUpdate.setOnClickListener(v -> updateUserName());
        btnLogOut.setOnClickListener(v -> logOutUser());
        switchNotify.setOnClickListener(v -> activateNotification());
    }

    private void initUser(User result) {
        user = result;
    }

    private void updateUserName() {
        if (!Objects.requireNonNull(inputEditText.getText()).toString().isEmpty()) {
            newUserName = inputEditText.getText().toString();
            sharedViewModel.getUserByName(newUserName).observe(Objects.requireNonNull(getActivity()), this::checkIfAlreadyExists);
        } else {
            Toast.makeText(getActivity(), "Please enter your new username", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkIfAlreadyExists(User result) {
        if (result == null) {
            user.setName(newUserName);
            sharedViewModel.updateUser(user);
            sharedPref.edit().putString("CurrentUserName", user.getName()).apply();
        }
    }

    private void logOutUser() {
        sharedPref.edit().putBoolean("LoggedIn", false).apply();
        sharedPref.edit().putLong("CurrentUser", 100).apply();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    private void activateNotification() {
        if (switchNotify.isChecked()) {
            sharedPref.edit().putBoolean("SwitchIsChecked", true).apply();

            PeriodicWorkRequest.Builder notificationBuilder =
                    new PeriodicWorkRequest.Builder(NotifyWorker.class, 1, TimeUnit.DAYS)
                            .addTag(workTag);
            PeriodicWorkRequest request = notificationBuilder.build();
            WorkManager.getInstance(Objects.requireNonNull(getActivity())).enqueueUniquePeriodicWork("reminder",
                    ExistingPeriodicWorkPolicy.REPLACE, request);
        } else {
            sharedPref.edit().putBoolean("SwitchIsChecked", false).apply();
            cancelNotification();
        }
    }

    private void cancelNotification() {
        if (getActivity() != null)
            WorkManager.getInstance(getActivity()).cancelAllWorkByTag(workTag);
    }
}
