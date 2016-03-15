package com.gtpd.k9.k9record;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;


public class NewExplosiveAdapter extends RecyclerView.Adapter<NewExplosiveAdapter.ExplosiveHolder> {

    public List<Explosive> explosives;

    private Context context;

    public NewExplosiveAdapter(List<Explosive> explosives, Context context) {
        this.explosives = explosives;
        this.context = context;
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

                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.explosive_dialog);

                TextView selectedExplosiveName = (TextView) dialog.findViewById(R.id.selectedExplosiveName);
                selectedExplosiveName.setText(explosive.name);

                final EditText quantityEditText = (EditText) dialog.findViewById(R.id.quantityEditText);
                final Spinner unitSpinner = (Spinner) dialog.findViewById(R.id.unitSpinner);
                final EditText locationEditText = (EditText) dialog.findViewById(R.id.locationEditText);

                Button addButton = (Button) dialog.findViewById(R.id.addButton);
                Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String quantityString = quantityEditText.getText().toString();

                        if (quantityString.isEmpty() || quantityString.equals(".")) {
                            dialog.findViewById(R.id.errorTextView).setVisibility(View.VISIBLE);
                            Animation shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.dialog_shake);
                            shakeAnimation.setRepeatCount(2);
                            shakeAnimation.setDuration(100);
                            quantityEditText.startAnimation(shakeAnimation);
                            quantityEditText.requestFocus();
                            return;
                        }

                        double quantity = Double.parseDouble(quantityString);
                        Explosive.Unit unit = Explosive.Unit.valueOf(unitSpinner.getSelectedItem().toString());
                        String location = locationEditText.getText().toString();

                        Explosive newExplosive = new Explosive(explosive.name, quantity, unit, location, explosive.imageResource);
                        FragmentManager fragmentManager = ((FragmentActivity) context).getFragmentManager();
                        ((ExplosiveSelectionFragment) fragmentManager.findFragmentByTag("explosive")).addExplosive(newExplosive);

                        dialog.dismiss();
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                quantityEditText.setFocusableInTouchMode(true);
                quantityEditText.requestFocus();
            }
        }
    }
}
