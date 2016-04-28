package com.gtpd.k9.k9record;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.sql.Timestamp;
import java.util.Date;

public class ExplosiveActivity extends AppCompatActivity {

    private Explosive explosive;
    private int explosivePosition;

    private int requestCode;

    private EditText quantityEditText;
    private Spinner unitSpinner;
    private EditText locationEditText;
    private EditText heightEditText;
    private EditText depthEditText;
    private EditText containerEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explosive);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        explosive = new Gson().fromJson(getIntent().getStringExtra("explosive"), Explosive.class);
        explosivePosition = getIntent().getIntExtra("explosivePosition", -1);

        TextView selectedExplosiveName = (TextView) findViewById(R.id.selectedExplosiveName);
        quantityEditText = (EditText) findViewById(R.id.quantityEditText);
        unitSpinner = (Spinner) findViewById(R.id.unitSpinner);
        locationEditText = (EditText) findViewById(R.id.locationEditText);
        heightEditText = (EditText) findViewById(R.id.heightEditText);
        depthEditText = (EditText) findViewById(R.id.depthEditText);
        containerEditText = (EditText) findViewById(R.id.containerEditText);

        selectedExplosiveName.setText(explosive.name);

        String[] unitArray = getResources().getStringArray(explosive.unitResource);
        ArrayAdapter<String> unitArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitArray);
        unitArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        unitSpinner.setAdapter(unitArrayAdapter);

        Button addButton = (Button) findViewById(R.id.addButton);
        Button updateButton = (Button) findViewById(R.id.updateButton);
        Button removeButton = (Button) findViewById(R.id.removeButton);

        requestCode = getIntent().getIntExtra("requestCode", -1);

        if (requestCode == ExplosiveSelectionFragment.UPDATE) {
            addButton.setVisibility(View.GONE);
            updateButton.setVisibility(View.VISIBLE);
            removeButton.setVisibility(View.VISIBLE);

            quantityEditText.setText(Double.toString(explosive.quantity));
            unitSpinner.setSelection(unitArrayAdapter.getPosition(explosive.unit.toString().toUpperCase()));
            locationEditText.setText(explosive.location);
            heightEditText.setText(Double.toString(explosive.height));
            depthEditText.setText(Double.toString(explosive.depth));
            containerEditText.setText(explosive.container);
        }

        quantityEditText.setFocusableInTouchMode(true);
        quantityEditText.requestFocus();
    }

    public void saveExplosive(View v) {
        String quantityString = quantityEditText.getText().toString();

        EditText errorEditText = null;
        String errorText = "";

        if (isEmpty(quantityEditText)) {
            errorEditText = quantityEditText;
            errorText = "Quantity value is required!";
        } else if (isEmpty(locationEditText)) {
            errorEditText = locationEditText;
            errorText = "Location description is required!";
        } else if (isEmpty(heightEditText)) {
            errorEditText = heightEditText;
            errorText = "Height value is required!";
        } else if (isEmpty(depthEditText)) {
            errorEditText = depthEditText;
            errorText = "Depth value is required!";
        } else if (isEmpty(containerEditText)) {
            errorEditText = containerEditText;
            errorText = "Container description is required!";
        }

        if (errorEditText != null) {
            Animation shakeAnimation = AnimationUtils.loadAnimation(ExplosiveActivity.this, R.anim.dialog_shake);
            shakeAnimation.setRepeatCount(2);
            shakeAnimation.setDuration(100);
            errorEditText.startAnimation(shakeAnimation);
            errorEditText.requestFocus();
            errorEditText.setError(errorText);
            return;
        }

        explosive.quantity = Double.parseDouble(quantityString);
        explosive.unit = Explosive.Unit.valueOf(unitSpinner.getSelectedItem().toString());
        explosive.location = locationEditText.getText().toString();

        if (!heightEditText.getText().toString().isEmpty()) {
            explosive.height = Double.parseDouble(heightEditText.getText().toString());
        }

        if (!depthEditText.getText().toString().isEmpty()) {
            explosive.depth = Double.parseDouble(depthEditText.getText().toString());
        }

        explosive.container = containerEditText.getText().toString();

        if (requestCode == ExplosiveSelectionFragment.NEW) {
            java.util.Date date = new java.util.Date();
            explosive.placementTime = new Timestamp(date.getTime());
        }

        Intent intent = new Intent();
        intent.putExtra("explosive", new Gson().toJson(explosive, Explosive.class));
        intent.putExtra("explosivePosition", explosivePosition);
        setResult(requestCode, intent);
        finish();
    }

    private boolean isEmpty(EditText et) {
        return et.getText().toString().trim().isEmpty() || et.getText().toString().trim().equals(".");
    }

    public void removeExplosive(View v) {
        Intent intent = new Intent();
        intent.putExtra("explosive", new Gson().toJson(explosive, Explosive.class));
        intent.putExtra("explosivePosition", explosivePosition);
        setResult(ExplosiveSelectionFragment.REMOVE, intent);
        finish();
    }

    public void cancel(View v) {
        finish();
    }
}
