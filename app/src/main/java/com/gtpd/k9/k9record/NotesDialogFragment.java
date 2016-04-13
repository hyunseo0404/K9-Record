package com.gtpd.k9.k9record;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by andrasta on 3/13/16.
 */
public class NotesDialogFragment extends DialogFragment {

    private int mSelectedPos;
    private String mExplosiveName;
    private OnCompleteListener mListener;
    private EditText mNotesContentET;

    public static interface OnCompleteListener {
        public abstract void onComplete(int selectedPos, String noteContent);
        public abstract void dismiss();
    }

    // make sure the Activity implemented it
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static NotesDialogFragment newInstance(int selectedPos, String expName) {
        NotesDialogFragment f = new NotesDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("selectedPos", selectedPos);
        args.putString("explosiveName", expName);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExplosiveName = getArguments().getString("explosiveName");
        mSelectedPos = getArguments().getInt("selectedPos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        getDialog().setTitle("Add Note");
        View v = inflater.inflate(R.layout.fragment_add_notes, container, false);
        mNotesContentET = (EditText) v.findViewById(R.id.notesEditText);
        mNotesContentET.setHint(mNotesContentET.getHint() + mExplosiveName + "\nLong press to use speech to text");


        mNotesContentET.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((NewSessionActivity)getActivity()).promptSpeechInput();
                return true;
            }
        });


        Button save = (Button) v.findViewById(R.id.saveNotesButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onComplete(mSelectedPos, mNotesContentET.getText().toString());
            }
        });

        Button cancel = (Button) v.findViewById(R.id.cancelAddNotesButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.dismiss();
            }
        });

        return v;
    }

    public void setNotesContent(String content) {
        mNotesContentET.setText(mNotesContentET.getText() + " " + content);
    }
}
