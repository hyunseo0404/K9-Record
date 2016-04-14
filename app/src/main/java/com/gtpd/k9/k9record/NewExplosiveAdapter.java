package com.gtpd.k9.k9record;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;


public class NewExplosiveAdapter extends RecyclerView.Adapter<NewExplosiveAdapter.ExplosiveHolder> {

    public List<Explosive> explosives;

    private Context context;
    private ExplosiveSelectionFragment fragment;

    public NewExplosiveAdapter(List<Explosive> explosives, Context context, ExplosiveSelectionFragment fragment) {
        this.explosives = explosives;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public ExplosiveHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_explosive_item, parent, false);
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

    public class ExplosiveHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView newExplosiveImageView;
        private final TextView newExplosiveTextView;
        private Explosive explosive;

        public ExplosiveHolder(View itemView) {
            super(itemView);

            newExplosiveImageView = (ImageView) itemView.findViewById(R.id.newExplosiveImage);
            newExplosiveTextView = (TextView) itemView.findViewById(R.id.newExplosiveName);
            itemView.setOnClickListener(this);
        }

        public void bindExplosive(Explosive explosive) {
            this.explosive = explosive;
            newExplosiveImageView.setImageResource(explosive.imageResource);
            newExplosiveTextView.setText(explosive.name);
        }

        @Override
        public void onClick(final View v) {
            if (explosive != null) {
                FragmentManager fragmentManager = ((FragmentActivity) context).getFragmentManager();
                ((ExplosiveSelectionFragment) fragmentManager.findFragmentByTag("explosive")).closeExplosiveDialog();

                Intent intent = new Intent(context, ExplosiveActivity.class);
                intent.putExtra("explosive", new Gson().toJson(explosive, Explosive.class));
                intent.putExtra("requestCode", ExplosiveSelectionFragment.NEW);
                fragment.startActivityForResult(intent, ExplosiveSelectionFragment.NEW);
            }
        }
    }
}
