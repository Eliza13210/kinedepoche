package com.oc.liza.kinedepoche.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oc.liza.kinedepoche.viewmodel.UserViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    protected long userId;
    SharedPreferences sharedPref;
    UserViewModel sharedViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutView(), container, false);
        ButterKnife.bind(this, view);
        initViewModel();
        initUser();
        initView();
        return view;
    }

    private void initUser() {
        sharedPref = Objects.requireNonNull(getActivity()).getSharedPreferences("KineDePoche", Context.MODE_PRIVATE);
        boolean loggedIn = sharedPref.getBoolean("LoggedIn", false);
        if (!loggedIn) {
            startActivity(new Intent(getActivity(), MainActivity.class));
        } else {
            userId = sharedPref.getLong("CurrentUser", 0);
        }
    }

    public abstract int getLayoutView();

    public abstract void initView();

    private void initViewModel() {
        sharedViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(UserViewModel.class);
    }
}
