package com.oc.liza.kinedepoche.controllers;

import android.os.AsyncTask;
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

import java.util.List;
import java.util.Objects;

import androidx.fragment.app.Fragment;
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

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();

        //CHECK IF FIRST TIME USER
        viewModel.getAllUsers().observe(this, this::updateWelcomeMessage);
    }

    private void updateWelcomeMessage(List<User> list) {
        if (list.size() > 0) {
            String message = "Welcome back " + list.get(0).getName() + "!";
            welcomeTextView.setText(message);

            userFrameLayout.setVisibility(View.VISIBLE);

            //SET LISTENER TO IMAGE VIEW
            initClickableImage();
        } else {
            //SET LISTENER TO SAVE BUTTON
            firstTimeUserLayout.setVisibility(View.VISIBLE);
            buttonSave.setOnClickListener(v -> saveUserName());
        }
    }

    private void initClickableImage() {
        ImageView exercise = Objects.requireNonNull(getView()).findViewById(R.id.program);
        exercise.setClickable(true);
        exercise.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.next_action, null));
    }

    private void saveUserName() {
        if (!userNameInput.getText().toString().isEmpty()) {
            String userName = Objects.requireNonNull(userNameInput.getText()).toString();
            User user = new User();
            user.setName(userName);

            viewModel.createUser(user);

        } else {
            Toast.makeText(getActivity(), "Please choose a user name", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_home;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

}