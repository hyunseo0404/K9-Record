package com.gtpd.k9.k9record;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


public class DogSelectionFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dog_selection, container, false);

        getActivity().setTitle("Select Dog");

        ArrayList<Dog> myDogs = new ArrayList<>(Arrays.asList(
                new Dog("Koda", "Golden Retriever", R.mipmap.ic_launcher),
                new Dog("Doggy", "Golden Retriever", R.mipmap.ic_launcher)   // FIXME: test values
        ));

        ArrayList<Dog> dogs = new ArrayList<>(Arrays.asList(
                new Dog("Cory", "Black Labrador", R.drawable.black_lab), new Dog("Max", "German Shepherd", R.mipmap.ic_launcher), new Dog("Molly", "Golden Retriever", R.mipmap.ic_launcher)  // FIXME: test values
        ));

        Collections.sort(dogs, new Comparator<Dog>() {
            @Override
            public int compare(Dog lhs, Dog rhs) {
                return lhs.name.compareToIgnoreCase(rhs.name);
            }
        });

        final DogAdapter dogAdapter = new DogAdapter(myDogs, dogs);
        RecyclerView dogList = (RecyclerView) view.findViewById(R.id.dogList);
        dogList.setAdapter(dogAdapter);
        dogList.setLayoutManager(new LinearLayoutManager(getActivity()));

        final Button continueButton = (Button) view.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewSessionActivity.session = new TrainingSession(dogAdapter.selectedDogHolder.getDog());

                getFragmentManager().beginTransaction()
                        .replace(R.id.newSessionContent, new ExplosiveSelectionFragment(), "explosive")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Listen for any touch in the dog selection
        dogList.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                continueButton.setVisibility(View.VISIBLE);
                return false;
            }
        });

        return view;
    }
}
