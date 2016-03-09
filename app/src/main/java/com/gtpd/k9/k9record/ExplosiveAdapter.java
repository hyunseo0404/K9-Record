package com.gtpd.k9.k9record;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class ExplosiveAdapter extends RecyclerView.Adapter<ExplosiveAdapter.ExplosiveHolder> {

    private List<String> explosives;

    public ExplosiveAdapter(List<String> explosives) {
        this.explosives = explosives;
    }

    @Override
    public ExplosiveHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_list_item, parent, false);
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

    public static class ExplosiveHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView ExplosiveImageView;
        private final TextView ExplosiveNameTextView;
        private String explosive;

        public ExplosiveHolder(View itemView) {
            super(itemView);

            ExplosiveImageView = (ImageView) itemView.findViewById(R.id.dogImage);
            ExplosiveNameTextView = (TextView) itemView.findViewById(R.id.dogName);
            itemView.setOnClickListener(this);
        }

        public void bindExplosive(String explosive) {
            this.explosive = explosive;
            ExplosiveImageView.setImageResource(R.mipmap.ic_launcher);  // FIXME: test image
            ExplosiveNameTextView.setText(explosive);
        }

        @Override
        public void onClick(View v) {
            if (explosive != null) {
                // TODO
            }
        }
    }
}
