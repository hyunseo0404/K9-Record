package com.gtpd.k9.k9record;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;


public class ExplosiveSelectionFragment extends Fragment {

    private ExplosiveAdapter explosiveAdapter;
    private TextView emptyTextView;
    private Button startButton;
    private Dialog explosiveDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explosive_selection, container, false);

        getActivity().setTitle("Select Explosives");

        explosiveAdapter = new ExplosiveAdapter(new ArrayList<Explosive>());
        RecyclerView explosiveList = (RecyclerView) view.findViewById(R.id.explosiveList);
        explosiveList.setAdapter(explosiveAdapter);
        explosiveList.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Grab the action button for the item touch listener
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager().findFragmentByTag("newExplosive") == null) {
                    explosiveDialog = new Dialog(getActivity());
                    explosiveDialog.setContentView(R.layout.explosive_selection_dialog);
                    RecyclerView recyclerView = (RecyclerView) explosiveDialog.findViewById(R.id.newNewExplosiveList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

                    ArrayList<Explosive> explosives = new ArrayList<>(Arrays.asList(
                            new Explosive("C4", 1.5, Explosive.Unit.KG, "Somewhere", R.mipmap.ic_launcher),
                            new Explosive("Nitro", 2.0, Explosive.Unit.LB, "Somewhere", R.mipmap.ic_launcher),
                            new Explosive("Gunpowder", 300, Explosive.Unit.G, "Somewhere", R.mipmap.ic_launcher),
                            new Explosive("C4", 1.5, Explosive.Unit.KG, "Somewhere", R.mipmap.ic_launcher),
                            new Explosive("Nitro", 2.0, Explosive.Unit.LB, "Somewhere", R.mipmap.ic_launcher),
                            new Explosive("Gunpowder", 300, Explosive.Unit.G, "Somewhere", R.mipmap.ic_launcher),
                            new Explosive("C4", 1.5, Explosive.Unit.KG, "Somewhere", R.mipmap.ic_launcher),
                            new Explosive("Nitro", 2.0, Explosive.Unit.LB, "Somewhere", R.mipmap.ic_launcher),
                            new Explosive("Gunpowder", 300, Explosive.Unit.G, "Somewhere", R.mipmap.ic_launcher)  // FIXME: test values
                    ));

                    recyclerView.setAdapter(new NewExplosiveAdapter(explosives, getActivity()));
                    recyclerView.addItemDecoration(new GridItemDecoration());

                    explosiveDialog.show();
                }
            }
        });

        emptyTextView = (TextView) view.findViewById(R.id.emptyTextView);

        startButton = (Button) view.findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                NewTrainingFragment trainingFragment = NewTrainingFragment.newInstance(explosiveAdapter.explosives);
                getFragmentManager().beginTransaction()
                        .replace(R.id.new_session_fragment, trainingFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        //Listen for any touch in the dog selection
//        explosiveList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                fab.setVisibility(View.VISIBLE);
//
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });

        return view;
    }

    public void addExplosive(Explosive explosive) {
        explosiveAdapter.addExplosive(explosive);
//        getFragmentManager().popBackStackImmediate();
        emptyTextView.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);
    }

    public void closeExplosiveDialog() {
        if (explosiveDialog != null) {
            explosiveDialog.dismiss();
        }
    }

    public class GridItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);

            if (position != 0 || position != parent.getAdapter().getItemCount() - 1) {
                outRect.top = 15;
                outRect.bottom = 15;
                outRect.left = 20;
                outRect.right = 20;
            }
        }
    }
}
