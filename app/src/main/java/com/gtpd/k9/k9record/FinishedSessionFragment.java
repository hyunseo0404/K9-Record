package com.gtpd.k9.k9record;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Clayton on 3/13/2016.
 */
public class FinishedSessionFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finished_session, container, false);

        //Add the individual explosives to the view
        LinearLayout parent = (LinearLayout)view.findViewById(R.id.finished_session_root);
        View template = inflater.inflate(R.layout.single_explosive_card_template, null);
        RelativeLayout templateRoot = (RelativeLayout) template.getRootView();
        parent.addView(templateRoot);

        template = inflater.inflate(R.layout.single_explosive_card_template, null);
        templateRoot = (RelativeLayout) template.getRootView();
        parent.addView(templateRoot);

        return view;
    }
}
