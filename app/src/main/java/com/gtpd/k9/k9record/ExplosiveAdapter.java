package com.gtpd.k9.k9record;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;


public class ExplosiveAdapter extends RecyclerView.Adapter<ExplosiveAdapter.ExplosiveHolder> {

    private List<Explosive> explosives;
    private Context context;
    private ExplosiveSelectionFragment fragment;

    public ExplosiveAdapter(List<Explosive> explosives, Context context, ExplosiveSelectionFragment fragment) {
        this.explosives = explosives;
        this.context = context;
        this.fragment = fragment;
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
        fragment.animateStartButton(true);
    }

    public void updateExplosive(Explosive explosive, int explosivePosition) {
        explosives.set(explosivePosition, explosive);
        notifyItemChanged(explosivePosition);
    }

    public void removeExplosive(int removePosition) {
        explosives.remove(removePosition);
        notifyItemRemoved(removePosition);

        if (explosives.isEmpty()) {
            fragment.animateStartButton(false);
        }
    }

    public List<Explosive> getExplosives() {
        return explosives;
    }

    public class ExplosiveHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView explosiveNameTextView;
        private final TextView explosiveQuantityTextView;
        private final TextView explosiveLocationTextView;
        private Explosive explosive;

        public ExplosiveHolder(View itemView) {
            super(itemView);

            explosiveNameTextView = (TextView) itemView.findViewById(R.id.explosiveName);
            explosiveQuantityTextView = (TextView) itemView.findViewById(R.id.explosiveQuantity);
            explosiveLocationTextView = (TextView) itemView.findViewById(R.id.explosiveLocation);
            itemView.setOnClickListener(this);
        }

        public void bindExplosive(Explosive explosive) {
            this.explosive = explosive;
            explosiveNameTextView.setText(explosive.name);
            explosiveQuantityTextView.setText(explosive.getQuantityAsString());

            if (explosive.location.isEmpty()) {
                explosiveLocationTextView.setText("Location Unknown");
            } else {
                explosiveLocationTextView.setText(explosive.location);
            }
        }

        @Override
        public void onClick(View v) {
            if (v != null) {
                FragmentManager fragmentManager = ((FragmentActivity) context).getFragmentManager();
                ((ExplosiveSelectionFragment) fragmentManager.findFragmentByTag("explosive")).closeExplosiveDialog();

                Intent intent = new Intent(context, ExplosiveActivity.class);
                intent.putExtra("explosive", new Gson().toJson(explosive, Explosive.class));
                intent.putExtra("explosivePosition", getLayoutPosition());
                intent.putExtra("requestCode", ExplosiveSelectionFragment.UPDATE);
                fragment.startActivityForResult(intent, ExplosiveSelectionFragment.UPDATE);
            }
        }
    }
}
