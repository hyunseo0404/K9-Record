package com.gtpd.k9.k9record;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;


public class ExplosiveSelectionFragment extends Fragment {

    private ExplosiveAdapter explosiveAdapter;
    private FloatingActionButton fab;
    private LinearLayout emptyListLayout;
    private Button startButton;
    private View startView;
    private Dialog explosiveDialog;
    private boolean startViewShown = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explosive_selection, container, false);

        getActivity().setTitle("Select Explosives");

        explosiveAdapter = new ExplosiveAdapter(new ArrayList<Explosive>(), getActivity(), this);
        RecyclerView explosiveList = (RecyclerView) view.findViewById(R.id.explosiveList);
        explosiveList.setAdapter(explosiveAdapter);
        explosiveList.setLayoutManager(new LinearLayoutManager(getActivity()));

        fab = (FloatingActionButton) view.findViewById(R.id.addFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().findFragmentByTag("newExplosive") == null) {
                    explosiveDialog = new Dialog(getActivity());
                    explosiveDialog.setContentView(R.layout.explosive_selection_dialog);
                    RecyclerView recyclerView = (RecyclerView) explosiveDialog.findViewById(R.id.newNewExplosiveList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

                    ArrayList<Explosive> explosives = new ArrayList<>(Arrays.asList(
                            new Explosive("C4 Military", R.mipmap.ic_launcher, R.array.unit_array_weight),
                            new Explosive("C4 Civilian", R.mipmap.ic_launcher, R.array.unit_array_weight),
                            new Explosive("TNT", R.mipmap.ic_launcher, R.array.unit_array_weight),
                            new Explosive("Semtex", R.mipmap.ic_launcher, R.array.unit_array_weight),
                            new Explosive("Black Powder", R.mipmap.ic_launcher, R.array.unit_array_weight),
                            new Explosive("Single Base Smokeless Powder", R.mipmap.ic_launcher, R.array.unit_array_weight),
                            new Explosive("Double Base Smokeless Powder", R.mipmap.ic_launcher, R.array.unit_array_weight),
                            new Explosive("Deta Sheet", R.mipmap.ic_launcher, R.array.unit_array_weight),
                            new Explosive("Cast Booster", R.mipmap.ic_launcher, R.array.unit_array_stick),
                            new Explosive("Safety Fuse", R.mipmap.ic_launcher, R.array.unit_array_length),
                            new Explosive("Det Cord", R.mipmap.ic_launcher, R.array.unit_array_length),
                            new Explosive("Water Gel", R.mipmap.ic_launcher, R.array.unit_array_stick),
                            new Explosive("Dynamite", R.mipmap.ic_launcher, R.array.unit_array_stick),
                            new Explosive("Dyno AP", R.mipmap.ic_launcher, R.array.unit_array_stick),
                            new Explosive("Ammonium Nitrate", R.mipmap.ic_launcher, R.array.unit_array_weight),
                            new Explosive("Potassium Perchlorate", R.mipmap.ic_launcher, R.array.unit_array_weight),
                            new Explosive("Ammonium Perchlorate", R.mipmap.ic_launcher, R.array.unit_array_weight),
                            new Explosive("Comp B", R.mipmap.ic_launcher, R.array.unit_array_weight),
                            new Explosive("HMX", R.mipmap.ic_launcher, R.array.unit_array_weight),
                            new Explosive("TATP", R.mipmap.ic_launcher, R.array.unit_array_weight),
                            new Explosive("HMTD", R.mipmap.ic_launcher, R.array.unit_array_weight),
                            new Explosive("Urea Nitrate", R.mipmap.ic_launcher, R.array.unit_array_weight)
                    ));

                    recyclerView.setAdapter(new NewExplosiveAdapter(explosives, getActivity()));

                    explosiveDialog.show();
                }
            }
        });

        emptyListLayout = (LinearLayout) view.findViewById(R.id.emptyListLayout);

        startButton = (Button) view.findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewSessionActivity.session.explosives = explosiveAdapter.explosives;

                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                NewTrainingFragment trainingFragment = NewTrainingFragment.newInstance();
                getFragmentManager().beginTransaction()
                        .replace(R.id.newSessionContent, trainingFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        startView = view.findViewById(R.id.startView);

        return view;
    }

    public void addExplosive(Explosive explosive) {
        explosiveAdapter.addExplosive(explosive);
        emptyListLayout.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);
    }

    public void updateExplosive(int explosivePosition) {
        explosiveAdapter.updateExplosive(explosivePosition);
    }

    public void removeExplosive(int explosivePosition) {
        explosiveAdapter.removeExplosive(explosivePosition);

        if (explosiveAdapter.getItemCount() == 0) {
            emptyListLayout.setVisibility(View.VISIBLE);
            startButton.setVisibility(View.GONE);
        }
    }

    public void closeExplosiveDialog() {
        if (explosiveDialog != null) {
            explosiveDialog.dismiss();
        }
    }

    public void animateContinueButton(final boolean show) {
        if (show && !startViewShown) {
            startView.animate().translationYBy(-startView.getHeight());
            fab.animate().translationYBy(-startView.getHeight());
            startViewShown = true;
        } else if (!show && startViewShown) {
            startView.animate().translationYBy(startView.getHeight());
            fab.animate().translationYBy(startView.getHeight());
            startViewShown = false;
        }
    }
}
