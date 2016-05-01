package com.gtpd.k9.k9record;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class ExplosiveSelectionFragment extends Fragment {

    public static final int NEW = 1;
    public static final int UPDATE = 2;
    public static final int REMOVE = 3;
    public static final int CANCEL = 0;

    private ExplosiveAdapter explosiveAdapter;
    private FloatingActionButton fab;
    private LinearLayout emptyListLayout;
    private View startView;
    private Dialog explosiveDialog;
    private boolean startViewShown = false;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (savedInstanceState != null && explosiveAdapter.getItemCount() > 0) {
            int height = savedInstanceState.getInt("height");
            startView.setTranslationY(-height);
            fab.setTranslationY(-height);
            startViewShown = true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem searchView = menu.findItem(R.id.search_item);
        searchView.setVisible(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_explosive, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_test_explosive) {
            // Add 3 sample explosives to the list
            Explosive explosive_0 = new Explosive(
                    "C4 Military",
                    350, Explosive.Unit.G,
                    "Under the bench on the 3rd floor",
                    50, Explosive.Unit.IN, 2.4, Explosive.Unit.FT,
                    new Timestamp(new Date().getTime()), "Black Box",
                    R.array.unit_array_weight);
            Explosive explosive_1 = new Explosive(
                    "TNT",
                    12.8, Explosive.Unit.LB,
                    "Inside the 2nd floor bathroom",
                    3, Explosive.Unit.FT, 12, Explosive.Unit.IN,
                    new Timestamp(new Date().getTime()), "White cylinder",
                    R.array.unit_array_weight);
            Explosive explosive_2 = new Explosive("Dynamite",
                    2, Explosive.Unit.STICK,
                    "Next to the main gate",
                    2.8, Explosive.Unit.IN, 5, Explosive.Unit.IN,
                    new Timestamp(new Date().getTime()), "No container",
                    R.array.unit_array_stick);
            explosiveAdapter.addExplosive(explosive_0);
            explosiveAdapter.addExplosive(explosive_1);
            explosiveAdapter.addExplosive(explosive_2);
            emptyListLayout.setVisibility(View.GONE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explosive_selection, container, false);

        getActivity().setTitle("Select Explosives");

        emptyListLayout = (LinearLayout) view.findViewById(R.id.emptyListLayout);

        List<Explosive> explosives;

        if (savedInstanceState != null) {
            explosives = new Gson().fromJson(savedInstanceState.getString("explosives"), new TypeToken<List<Explosive>>(){}.getType());
            if (!explosives.isEmpty()) {
                emptyListLayout.setVisibility(View.GONE);
            }
        } else {
            explosives = new ArrayList<>();
        }

        explosiveAdapter = new ExplosiveAdapter(explosives, getActivity(), this);
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
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    final ArrayList<Explosive> explosives = new ArrayList<>(Arrays.asList(
                            new Explosive("C4 Military", R.array.unit_array_weight),
                            new Explosive("C4 Civilian", R.array.unit_array_weight),
                            new Explosive("TNT", R.array.unit_array_weight),
                            new Explosive("Semtex", R.array.unit_array_weight),
                            new Explosive("Black Powder", R.array.unit_array_weight),
                            new Explosive("Single Base Smokeless Powder", R.array.unit_array_weight),
                            new Explosive("Double Base Smokeless Powder", R.array.unit_array_weight),
                            new Explosive("Deta Sheet", R.array.unit_array_weight),
                            new Explosive("Cast Booster", R.array.unit_array_stick),
                            new Explosive("Safety Fuse", R.array.unit_array_length),
                            new Explosive("Det Cord", R.array.unit_array_length),
                            new Explosive("Water Gel", R.array.unit_array_stick),
                            new Explosive("Dynamite", R.array.unit_array_stick),
                            new Explosive("Dyno AP", R.array.unit_array_stick),
                            new Explosive("Ammonium Nitrate", R.array.unit_array_weight),
                            new Explosive("Potassium Perchlorate", R.array.unit_array_weight),
                            new Explosive("Ammonium Perchlorate", R.array.unit_array_weight),
                            new Explosive("Comp B", R.array.unit_array_weight),
                            new Explosive("HMX", R.array.unit_array_weight),
                            new Explosive("TATP", R.array.unit_array_weight),
                            new Explosive("HMTD", R.array.unit_array_weight),
                            new Explosive("Urea Nitrate", R.array.unit_array_weight)
                    ));

                    recyclerView.setAdapter(new NewExplosiveAdapter(explosives, getActivity(), ExplosiveSelectionFragment.this));

                    explosiveDialog.show();
                }
            }
        });

        Button startButton = (Button) view.findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewSessionActivity.session.explosives = explosiveAdapter.getExplosives();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == CANCEL) return;

        Explosive explosive = new Gson().fromJson(data.getStringExtra("explosive"), Explosive.class);
        int explosivePosition = data.getIntExtra("explosivePosition", -1);

        if (resultCode == NEW) {
            explosiveAdapter.addExplosive(explosive);
            emptyListLayout.setVisibility(View.GONE);
        } else if (resultCode == UPDATE) {
            explosiveAdapter.updateExplosive(explosive, explosivePosition);
        } else if (resultCode == REMOVE) {
            explosiveAdapter.removeExplosive(explosivePosition);

            if (explosiveAdapter.getItemCount() == 0) {
                emptyListLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    public void closeExplosiveDialog() {
        if (explosiveDialog != null) {
            explosiveDialog.dismiss();
        }
    }

    public void animateStartButton(final boolean show) {
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (explosiveAdapter != null) {
            outState.putString("explosives", new Gson().toJson(explosiveAdapter.getExplosives()));
            outState.putInt("height", startView.getHeight());
        }
    }
}
