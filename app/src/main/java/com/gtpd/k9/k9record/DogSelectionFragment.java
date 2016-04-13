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

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


public class DogSelectionFragment extends Fragment {

    private DogAdapter dogAdapter;

    private RecyclerView dogList;
    private View continueView;
    private boolean continueViewShown = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            Dog selectedDog = new Gson().fromJson(savedInstanceState.getString("selected"), Dog.class);

            if (selectedDog != null) {
                continueView.setTranslationY(-savedInstanceState.getInt("height"));
                continueViewShown = true;
                dogList.scrollToPosition(dogAdapter.getSelectedPosition());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dog_selection, container, false);

        getActivity().setTitle("Select Dog");

        ArrayList<Dog> myDogs = new ArrayList<>(Arrays.asList(
                new Dog(1, "Koda", "Golden Retriever", R.mipmap.ic_launcher),
                new Dog(2, "Doggy", "Golden Retriever", R.mipmap.ic_launcher)   // FIXME: test values
        ));

        ArrayList<Dog> dogs = new ArrayList<>(Arrays.asList(
                new Dog(3, "Cory", "Black Labrador", R.drawable.black_lab),
                new Dog(4, "Max", "German Shepherd", R.mipmap.ic_launcher),
                new Dog(5, "Molly", "Golden Retriever", R.mipmap.ic_launcher)  // FIXME: test values
        ));

        Collections.sort(dogs, new Comparator<Dog>() {
            @Override
            public int compare(Dog lhs, Dog rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });

        Dog selectedDog = null;
        int selectedPosition = -1;

        if (savedInstanceState != null) {
            selectedDog = new Gson().fromJson(savedInstanceState.getString("selected"), Dog.class);
            selectedPosition = savedInstanceState.getInt("selectedPosition");
        }

        dogAdapter = new DogAdapter(myDogs, dogs, this, selectedDog, selectedPosition);
        dogList = (RecyclerView) view.findViewById(R.id.dogList);
        dogList.setAdapter(dogAdapter);
        dogList.setLayoutManager(new LinearLayoutManager(getActivity()));

        Button continueButton = (Button) view.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewSessionActivity.session = new TrainingSession(dogAdapter.getSelectedDog());

                getFragmentManager().beginTransaction()
                        .add(R.id.newSessionContent, new ExplosiveSelectionFragment(), "explosive")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
            }
        });

        continueView = view.findViewById(R.id.continueView);

        return view;
    }

    public void animateContinueButton(final boolean show) {
        if (show && !continueViewShown) {
            continueView.animate().translationYBy(-continueView.getHeight());
            continueViewShown = true;
        } else if (!show && continueViewShown) {
            continueView.animate().translationYBy(continueView.getHeight());
            continueViewShown = false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (dogAdapter != null) {
            outState.putString("selected", new Gson().toJson(dogAdapter.getSelectedDog()));
            outState.putInt("selectedPosition", dogAdapter.getSelectedPosition());
            outState.putInt("height", continueView.getHeight());
        }
    }
}
