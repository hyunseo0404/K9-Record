package com.gtpd.k9.k9record;


import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;

import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class NewSessionActivity extends AppCompatActivity implements NewTrainingFragment.OnFragmentInteractionListener,
        NotesDialogFragment.OnCompleteListener {

    public static TrainingSession session;

    private DialogFragment addNotesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("New Session");
        setContentView(R.layout.activity_new_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.new_session_fragment, new DogSelectionFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
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

    public void showNotesDialog(int selectedPos, String explosiveName){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("notesDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        addNotesFragment = NotesDialogFragment.newInstance(selectedPos, explosiveName);
        addNotesFragment.show(ft, "notesDialog");
    }

    @Override
    public void onComplete(int selectedPos, String noteContent) {
        Tuple<Explosive, String> note = new Tuple<>(session.explosives.get(selectedPos), noteContent);
        session.addNotes(note);
        addNotesFragment.dismiss();
    }

    @Override
    public void dismiss(){
        addNotesFragment.dismiss();
    }
}
