package com.oc.liza.kinedepoche.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oc.liza.kinedepoche.injections.Injection;
import com.oc.liza.kinedepoche.injections.ViewModelFactory;
import com.oc.liza.kinedepoche.viewmodel.UserViewModel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    protected UserViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutView(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        initViewModel(getFragment());
    }

    public abstract int getLayoutView();

    public abstract Fragment getFragment();


    protected void initViewModel(Fragment fragment) {

        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(getActivity());
        this.viewModel = ViewModelProviders.of(fragment, mViewModelFactory).get(UserViewModel.class);
    }
}
