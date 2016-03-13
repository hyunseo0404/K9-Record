package com.gtpd.k9.k9record;

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

    private String mNotesContent;
    private String mExplosiveName;
    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static NotesDialogFragment newInstance(String content, String expName) {
        NotesDialogFragment f = new NotesDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("content", content);
        args.putString("explosiveName", expName);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNotesContent = getArguments().getString("content");
        mExplosiveName = getArguments().getString("explosiveName");

//        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_notes, container, false);
        View et = v.findViewById(R.id.notesEditText);
        if(!mNotesContent.equals("")){
            ((EditText)et).setText(mNotesContent);
        } else {
            ((EditText)et).setHint(((EditText) et).getHint() + mExplosiveName);
        }

        return v;
    }
}
