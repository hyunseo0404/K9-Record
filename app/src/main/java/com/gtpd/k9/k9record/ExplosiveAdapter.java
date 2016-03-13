package com.gtpd.k9.k9record;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ExplosiveAdapter extends RecyclerView.Adapter<ExplosiveAdapter.ExplosiveHolder> {

    public ArrayList<Explosive> selected;

    public List<Explosive> explosives;

    public ExplosiveAdapter(List<Explosive> explosives) {
        this.explosives = explosives;
        this.selected = new ArrayList<>();
    }

    @Override
    public ExplosiveHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.explosive_list_item, parent, false);
        return new ExplosiveHolder(view);
    }

    @Override
    public void onBindViewHolder(ExplosiveHolder holder, int position) {
        holder.bindExplosive(explosives.get(position));
    }

    @Override
    public int getItemCount() {
        return explosives.size();
    }

    public void addExplosive(Explosive explosive) {
        explosives.add(explosive);
        notifyItemInserted(explosives.size() - 1);
    }

    public class ExplosiveHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView explosiveImageView;
        private final TextView explosiveNameTextView;
        private final ImageView explosiveSelectedImageView;
        private boolean selected = false;
        private Explosive explosive;

        public ExplosiveHolder(View itemView) {
            super(itemView);

            explosiveImageView = (ImageView) itemView.findViewById(R.id.explosiveImage);
            explosiveNameTextView = (TextView) itemView.findViewById(R.id.explosiveName);
            explosiveSelectedImageView = (ImageView) itemView.findViewById(R.id.explosiveSelectedImage);
            itemView.setOnClickListener(this);
        }

        public void bindExplosive(Explosive explosive) {
            this.explosive = explosive;
            explosiveImageView.setImageResource(explosive.imageResource);
            explosiveNameTextView.setText(explosive.name);
        }

        @Override
        public void onClick(View v) {
//            if (explosive != null) {
//                if (selected) {
//                    explosiveSelectedImageView.setVisibility(View.INVISIBLE);
//                    adapter.selected.remove(explosive);
//                } else {
//                    explosiveSelectedImageView.setVisibility(View.VISIBLE);
//                    adapter.selected.add(explosive);
//                }
//                selected = !selected;
//            }
        }
    }
}
