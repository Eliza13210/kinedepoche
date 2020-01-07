package com.oc.liza.kinedepoche.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oc.liza.kinedepoche.viewmodel.UserViewModel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    protected long userId;
    protected SharedPreferences sharedPref;
    protected UserViewModel sharedViewModel;

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
        sharedPref = getActivity().getSharedPreferences("KineDePoche", Context.MODE_PRIVATE);
        userId = sharedPref.getLong("CurrentUser", 0);
    }

    public abstract int getLayoutView();

    public abstract void initView();

    private void initViewModel() {
        sharedViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
    }
}
