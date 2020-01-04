package com.oc.liza.kinedepoche.controllers;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.oc.liza.kinedepoche.R;
import com.oc.liza.kinedepoche.models.User;
import com.oc.liza.kinedepoche.viewmodel.UserViewModel;

import java.util.List;
import java.util.Objects;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import butterknife.BindView;


public class HomeFragment extends BaseFragment {

    @BindView(R.id.welcome_message)
    TextView welcomeTextView;

    @BindView(R.id.home_fragment_user)
    FrameLayout userFrameLayout;

    @BindView(R.id.home_fragment_first_time_user)
    LinearLayout firstTimeUserLayout;

    @BindView(R.id.user_name_text_input)
    TextInputEditText userNameInput;

    @BindView(R.id.btn_save)
    MaterialButton buttonSave;

    private Boolean loggedIn = false;
    private String message = "";
    private String userName;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {

    }


    @Override
    public void onResume() {
        super.onResume();
        loggedIn = sharedPref.getBoolean("LoggedIn", false);
        if (!loggedIn) {
            userFrameLayout.setVisibility(View.GONE);
            firstTimeUserLayout.setVisibility(View.VISIBLE);

            buttonSave.setOnClickListener(v -> logInWithUserName());
        } else {
            updateWelcomeMessage();
        }
    }

    private void logInWithUserName() {
        if (!Objects.requireNonNull(userNameInput.getText()).toString().isEmpty()) {
            sharedViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
            sharedViewModel.getAllUsers().observe(this, this::checkIfUserExists);
        } else {
            Toast.makeText(getActivity(), "Please choose a user name", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkIfUserExists(List<User> list) {
        //CHECK IF FIRST TIME USER
        userName = Objects.requireNonNull(userNameInput.getText()).toString();

        for (User user : list) {
            if (user.getName().equals(userName)) {
                message = "Welcome " + user.getName() + "!";
                loggedIn = true;
                sharedPref.edit().putLong("CurrentUser", user.getId()).apply();
                sharedPref.edit().putString("CurrentUserName", user.getName()).apply();
                sharedPref.edit().putBoolean("LoggedIn", true).apply();

                Log.e("home", "existing user " + user.getName() + user.getId());
            }
        }
        updateWelcomeMessage();
    }


    private void updateWelcomeMessage() {
        if (!loggedIn) {
            //CREATE USER
            User newUser = new User();
            newUser.setName(userName);
            sharedViewModel.createUser(newUser);
            Log.e("home", "create user " + userName);
        }

        userName = sharedPref.getString("CurrentUserName", null);
        message = "Welcome " + userName + "!";
        welcomeTextView.setText(message);

        userFrameLayout.setVisibility(View.VISIBLE);
        firstTimeUserLayout.setVisibility(View.GONE);

        //SET LISTENER TO IMAGE VIEW
        initClickableImage();
    }

    private void initClickableImage() {
        ImageView exercise = Objects.requireNonNull(getView()).findViewById(R.id.program);
        exercise.setClickable(true);
        exercise.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.next_action, null));
    }
}