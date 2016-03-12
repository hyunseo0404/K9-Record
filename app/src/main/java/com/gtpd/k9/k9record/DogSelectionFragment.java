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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


public class DogSelectionFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dog_selection, container, false);
        //this.getActivity().setTitle("Select Dog");

        ArrayList<Dog> dogs = new ArrayList<>(Arrays.asList(
                new Dog("Cory", "Black Labrador", R.drawable.black_lab), new Dog("Max", "German Shepherd", R.mipmap.ic_launcher), new Dog("Molly", "Golden Retriever", R.mipmap.ic_launcher)  // FIXME: test values
        ));

        DogAdapter dogAdapter = new DogAdapter(dogs);
        RecyclerView dogList = (RecyclerView) view.findViewById(R.id.dogList);
        dogList.setAdapter(dogAdapter);
        dogList.setLayoutManager(new LinearLayoutManager(getActivity()));
        //Grab the action button for the item touch listener
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.next_page_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.new_session_fragment, new ExplosiveSelectionFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });
        //Listen for any touch in the dog selection
        dogList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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

        /*Button continueButton = (Button) view.findViewById(R.id.dogContinueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction()
                        .replace(R.id.new_session_fragment, new ExplosiveSelectionFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });*/




        return view;
    }
}
