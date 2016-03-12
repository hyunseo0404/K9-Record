package com.gtpd.k9.k9record;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;


public class ExplosiveSelectionFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explosive_selection, container, false);

        ArrayList<String> explosives = new ArrayList<>(Arrays.asList(
                "C4", "Nitro", "Gunpowder"  // FIXME: test values
        ));

        final ExplosiveAdapter explosiveAdapter = new ExplosiveAdapter(explosives);
        RecyclerView explosiveList = (RecyclerView) view.findViewById(R.id.explosiveList);
        explosiveList.setAdapter(explosiveAdapter);
        explosiveList.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Grab the action button for the item touch listener
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.next_page_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewTrainingFragment trainingFragment = NewTrainingFragment.newInstance(explosiveAdapter.selected);

                getFragmentManager().beginTransaction()
                        .replace(R.id.new_session_fragment, trainingFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });
        //Listen for any touch in the dog selection
        explosiveList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                fab.setVisibility(View.VISIBLE);

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        /*Button continueButton = (Button) view.findViewById(R.id.explosiveContinueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewTrainingFragment trainingFragment = NewTrainingFragment.newInstance(explosiveAdapter.selected);
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_main, trainingFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });*/

        return view;
    }
}