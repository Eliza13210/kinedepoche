package com.oc.liza.kinedepoche.controllers;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oc.liza.kinedepoche.R;

import androidx.navigation.Navigation;
import butterknife.BindView;


public class HomeFragment extends BaseFragment {

    @BindView(R.id.welcome_message)
    TextView welcomeTextView;
    @BindView(R.id.program)
    ImageView exercise;
    @BindView(R.id.fragment_home)
    LinearLayout fragmentLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutView() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {
        updateWelcomeMessage();
        initClickableImage();
    }

    private void updateWelcomeMessage() {
        String userName = sharedPref.getString("CurrentUserName", null);
        String message = getActivity().getResources().getString(R.string.welcome) + " "+ userName + "!";
        welcomeTextView.setText(message);
    }

    private void initClickableImage() {
        exercise.setClickable(true);
        exercise.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.next_action, null));
    }
}