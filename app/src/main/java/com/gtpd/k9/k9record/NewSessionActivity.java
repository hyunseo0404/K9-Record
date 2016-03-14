package com.gtpd.k9.k9record;


import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;

import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public class NewSessionActivity extends AppCompatActivity implements NewTrainingFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("New Session");
        setContentView(R.layout.activity_new_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.next_page_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.new_session_fragment, new DogSelectionFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        Log.d("NewSessionActivity", "count: " + fm.getBackStackEntryCount());
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            return true;
        } else {
            return super.onSupportNavigateUp();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // TODO
    }

    public void showNotesDialog(String content, String explosiveName){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("notesDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment addNotesFragment = NotesDialogFragment.newInstance(content, explosiveName);
        addNotesFragment.show(ft, "notesDialog");

    }
}
