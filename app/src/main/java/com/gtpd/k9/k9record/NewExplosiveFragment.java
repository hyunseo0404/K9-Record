package com.gtpd.k9.k9record;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;


public class NewExplosiveFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_explosive, container, false);

        ArrayList<Explosive> explosives = new ArrayList<>(Arrays.asList(
                new Explosive("C4", R.mipmap.ic_launcher),
                new Explosive("Nitro", R.mipmap.ic_launcher),
                new Explosive("Gunpowder", R.mipmap.ic_launcher),
                new Explosive("C4", R.mipmap.ic_launcher),
                new Explosive("Nitro", R.mipmap.ic_launcher),
                new Explosive("Gunpowder", R.mipmap.ic_launcher)  // FIXME: test values
        ));

        RecyclerView newExplosiveList = (RecyclerView) view.findViewById(R.id.newExplosiveList);
        newExplosiveList.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false));
        newExplosiveList.setAdapter(new NewExplosiveAdapter(explosives, getActivity()));
        newExplosiveList.setHasFixedSize(true);
        newExplosiveList.addItemDecoration(new GridItemDecoration(2, 120, true));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class GridItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);

            if (includeEdge) {
                if (position < spanCount) {
                    outRect.left = spacing;
                }
                outRect.right = spacing;
            } else {
                if (position >= spanCount) {
                    outRect.left = spacing;
                }
            }
        }
    }
}
