package com.gtpd.k9.k9record;


import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;

import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class NewSessionActivity extends AppCompatActivity implements NewTrainingFragment.OnFragmentInteractionListener,
        NotesDialogFragment.OnCompleteListener {

    public static TrainingSession session;

    private DialogFragment addNotesFragment;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("New Session");
        setContentView(R.layout.activity_new_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.newSessionContent, new DogSelectionFragment()).commit();
        }
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


    /**
     * Showing google speech input dialog
     * */
    public void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("NEWSESSION", result.get(0));
                    ((NotesDialogFragment)addNotesFragment).setNotesContent(result.get(0));
                }
                break;
            }
        }
    }
}
