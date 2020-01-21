package com.oc.liza.kinedepoche.controllers;

import android.widget.TextView;

import com.oc.liza.kinedepoche.R;

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
        why_text_view.setText("Prolonged use of computer or activities with similar position such as driving often give rise to\n" +
                "      musculoskeletal symptoms. Headache and back pain are the most common symptoms. The back can easily support the weight of your head and neck\n" +
                "      when your posture is tall and erect, but if your head leans forward in a 45 degree angle, your neck has to work like a lever lifting a heavy object.\n" +
                "Muscles need movement, contraction and relaxation in order to be free from tension and well vascularized.");

        how_text_view.setText("Take breaks every hour and do the exercises to release tensions and give oxygen to the muscles");
    }


}
