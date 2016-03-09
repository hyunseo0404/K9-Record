package com.gtpd.k9.k9record;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;


public class DogSelectionFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // TODO
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dog_selection, container, false);

        ArrayList<Dog> dogs = new ArrayList<>(Arrays.asList(
                new Dog("Cory", "Black Labrador"), new Dog("Max", "German Shepherd"), new Dog("Molly", "Golden Retriever"), new Dog("Cory", "Black Labrador"), new Dog("Max", "German Shepherd"), new Dog("Molly", "Golden Retriever"), new Dog("Cory", "Black Labrador"), new Dog("Max", "German Shepherd"), new Dog("Molly", "Golden Retriever")  // FIXME: test values
        ));

        DogAdapter dogAdapter = new DogAdapter(dogs);
        RecyclerView dogList = (RecyclerView) view.findViewById(R.id.dogList);
        dogList.setAdapter(dogAdapter);
        dogList.setLayoutManager(new LinearLayoutManager(getActivity()));

        Button continueButton = (Button) view.findViewById(R.id.dogContinueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.content_main, new ExplosiveSelectionFragment())
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
            }
        });

        return view;
    }
}
