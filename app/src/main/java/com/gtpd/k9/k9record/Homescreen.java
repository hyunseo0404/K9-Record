package com.gtpd.k9.k9record;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Clayton on 3/10/2016.
 */
public class Homescreen extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homescreen, container, false);

        //Set the button Listeners
        CardView newTrainingSession = (CardView) view.findViewById(R.id.start_session_button);

        newTrainingSession.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewSessionActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
