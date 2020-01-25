package com.oc.liza.kinedepoche.controllers;

import android.widget.TextView;

import com.oc.liza.kinedepoche.R;

import java.util.Objects;

import butterknife.BindView;


public class TheoryFragment extends BaseFragment {

    @BindView(R.id.why_text)
    TextView why_text_view;
    @BindView(R.id.how_text)
    TextView how_text_view;

    public TheoryFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutView() {
        return R.layout.fragment_theory;
    }

    @Override
    public void initView() {
        why_text_view.setText(Objects.requireNonNull(getActivity()).getResources().getString(R.string.why_text));

        how_text_view.setText(getActivity().getResources().getString(R.string.how_text));
    }


}
