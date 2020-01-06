package com.oc.liza.kinedepoche.controllers;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.oc.liza.kinedepoche.NotifyWorker;
import com.oc.liza.kinedepoche.R;
import com.oc.liza.kinedepoche.models.User;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import butterknife.BindView;

public class SettingsFragment extends BaseFragment {

    @BindView(R.id.user_name_text_input)
    TextInputEditText inputEditText;
    @BindView(R.id.btn_update)
    Button btnUpdate;
    @BindView(R.id.notification_switch)
    Switch switchNotify;

    private User user;

    public static final String workTag = "notificationWork";

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

        btnUpdate.setOnClickListener(v -> updateUserName());
        switchNotify.setOnClickListener(v -> activateNotification());
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

    private void activateNotification() {
        if (switchNotify.isChecked()) {
            Log.e("settingfr", "activate notification");
            sharedPref.edit().putBoolean("SwitchIsChecked", true).apply();

            PeriodicWorkRequest.Builder notificationBuilder =
                    new PeriodicWorkRequest.Builder(NotifyWorker.class, 1, TimeUnit.DAYS);
            PeriodicWorkRequest request = notificationBuilder.build();
            WorkManager.getInstance(getActivity()).enqueueUniquePeriodicWork(workTag, ExistingPeriodicWorkPolicy.REPLACE, request);
        } else {

            Log.e("settings", "cancel notification");
            sharedPref.edit().putBoolean("SwitchIsChecked", false).apply();
            cancelNotification();
        }
    }

    private void cancelNotification() {
        WorkManager.getInstance(getActivity()).cancelAllWorkByTag(workTag);
    }
}
