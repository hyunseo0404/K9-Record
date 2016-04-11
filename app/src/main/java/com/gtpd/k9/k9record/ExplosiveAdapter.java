package com.gtpd.k9.k9record;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ExplosiveAdapter extends RecyclerView.Adapter<ExplosiveAdapter.ExplosiveHolder> {

    public ArrayList<Explosive> selected;

    public List<Explosive> explosives;

    private Context context;
    private ExplosiveSelectionFragment fragment;

    public ExplosiveAdapter(List<Explosive> explosives, Context context, ExplosiveSelectionFragment fragment) {
        this.explosives = explosives;
        this.selected = new ArrayList<>();
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
        fragment.animateContinueButton(true);
    }

    public void updateExplosive(int explosivePosition) {
        notifyItemChanged(explosivePosition);
    }

    public void removeExplosive(int removePosition) {
        explosives.remove(removePosition);
        notifyItemRemoved(removePosition);

        if (explosives.isEmpty()) {
            fragment.animateContinueButton(false);
        }
    }

    public class ExplosiveHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView explosiveImageView;
        private final TextView explosiveNameTextView;
        private final TextView explosiveQuantityTextView;
        private final TextView explosiveLocationTextView;
        private Explosive explosive;

        public ExplosiveHolder(View itemView) {
            super(itemView);

            explosiveImageView = (ImageView) itemView.findViewById(R.id.explosiveImage);
            explosiveNameTextView = (TextView) itemView.findViewById(R.id.explosiveName);
            explosiveQuantityTextView = (TextView) itemView.findViewById(R.id.explosiveQuantity);
            explosiveLocationTextView = (TextView) itemView.findViewById(R.id.explosiveLocation);
            itemView.setOnClickListener(this);
        }

        public void bindExplosive(Explosive explosive) {
            this.explosive = explosive;
            explosiveImageView.setImageResource(explosive.imageResource);
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

                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.explosive_dialog);

                TextView selectedExplosiveName = (TextView) dialog.findViewById(R.id.selectedExplosiveName);
                selectedExplosiveName.setText(explosive.name);

                final EditText quantityEditText = (EditText) dialog.findViewById(R.id.quantityEditText);
                final Spinner unitSpinner = (Spinner) dialog.findViewById(R.id.unitSpinner);
                final EditText locationEditText = (EditText) dialog.findViewById(R.id.locationEditText);

                quantityEditText.setText(Double.toString(explosive.quantity));
                unitSpinner.setSelection(explosive.unit.ordinal());
                locationEditText.setText(explosive.location);

                Button addButton = (Button) dialog.findViewById(R.id.addButton);
                Button updateButton = (Button) dialog.findViewById(R.id.updateButton);
                Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
                Button removeButton = (Button) dialog.findViewById(R.id.removeButton);

                String[] unitArray = context.getResources().getStringArray(explosive.unitResource);
                ArrayAdapter<String> unitArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, unitArray);
                unitArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                unitSpinner.setAdapter(unitArrayAdapter);

                addButton.setVisibility(View.GONE);
                updateButton.setVisibility(View.VISIBLE);
                removeButton.setVisibility(View.VISIBLE);

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String quantityString = quantityEditText.getText().toString();

                        if (quantityString.isEmpty() || quantityString.equals(".")) {
                            Animation shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.dialog_shake);
                            shakeAnimation.setRepeatCount(2);
                            shakeAnimation.setDuration(100);
                            quantityEditText.startAnimation(shakeAnimation);
                            quantityEditText.requestFocus();
                            quantityEditText.setError("Quantity value is required!");
                            return;
                        }

                        explosive.quantity = Double.parseDouble(quantityString);
                        explosive.unit = Explosive.Unit.valueOf(unitSpinner.getSelectedItem().toString());
                        explosive.location = locationEditText.getText().toString();

                        FragmentManager fragmentManager = ((FragmentActivity) context).getFragmentManager();
                        ((ExplosiveSelectionFragment) fragmentManager.findFragmentByTag("explosive")).updateExplosive(getLayoutPosition());

                        dialog.dismiss();
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fragmentManager = ((FragmentActivity) context).getFragmentManager();
                        ((ExplosiveSelectionFragment) fragmentManager.findFragmentByTag("explosive")).removeExplosive(getLayoutPosition());

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
