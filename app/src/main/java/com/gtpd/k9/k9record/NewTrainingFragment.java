package com.gtpd.k9.k9record;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewTrainingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewTrainingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewTrainingFragment extends Fragment {

    private Menu mMenu;

    // Timer related variables
    Button butnstart, butnreset;
    static TextView time;
    long starttime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedtime = 0L;
    int t = 1;
    int secs = 0;
    int mins = 0;
    int milliseconds = 0;
    Handler handler;

    private RecyclerView mRecView;
    private OnFragmentInteractionListener mListener;

    public NewTrainingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewTrainingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewTrainingFragment newInstance() {
        return new NewTrainingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        handler = new Handler();
    }

    public Runnable updateTimer = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - starttime;
            updatedtime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updatedtime / 1000);
            mins = secs / 60;
            secs = secs % 60;
            milliseconds = (int) (updatedtime % 1000);
            time.setText("" + mins + ":" + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            time.setTextColor(Color.RED);
            handler.postDelayed(this, 0);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_training, container, false);

        getActivity().setTitle("New Session");

        butnstart = (Button) view.findViewById(R.id.startTraining);
        butnreset = (Button) view.findViewById(R.id.reset);
        time = (TextView) view.findViewById(R.id.timerText);

        butnstart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (t == 1) {
                    butnstart.setText("Pause");
                    starttime = SystemClock.uptimeMillis();
                    handler.postDelayed(updateTimer, 0);
                    t = 0;
                    MenuItem finishSession = mMenu.findItem(R.id.action_finish_session);
                    finishSession.setEnabled(true);
                } else {
                    butnstart.setText("Start");
                    time.setTextColor(Color.BLUE);
                    timeSwapBuff += timeInMilliseconds;
                    handler.removeCallbacks(updateTimer);
                    t = 1;
                }
            }
        });

        butnreset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                starttime = 0L;
                timeInMilliseconds = 0L;
                timeSwapBuff = 0L;
                updatedtime = 0L;
                t = 1;
                secs = 0;
                mins = 0;
                milliseconds = 0;
                butnstart.setText("Start");
                handler.removeCallbacks(updateTimer);
                time.setText("00:00:00");
            }
        });

        mRecView = (RecyclerView) view.findViewById(R.id.training_recycler_view);
        mRecView.setHasFixedSize(true);

        // Set the layout manager
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mRecView.setLayoutManager(llm);

        Explosive [] expArr = new Explosive[NewSessionActivity.session.explosives.size()];
        NewSessionActivity.session.explosives.toArray(expArr);
        TrainingCardAdapter mAdapter = new TrainingCardAdapter(expArr, getActivity());
        mRecView.setAdapter(mAdapter);

        // Reset the time
        time.setText("00:00:00");
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_training_actions, menu);
        mMenu = menu;
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}


class TrainingCardAdapter extends RecyclerView.Adapter<TrainingCardAdapter.ViewHolder> {
    private Explosive[] mDataset;
    private Activity mParent;
    private int mExplosivesLeftToFind;
    private String clockedTime;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView mCardView;
        public TextView mExplosiveName;
        public TextView mExplosiveAmount;
        public TextView mHidingPlace;
        public TextView mDuration;
//        public String mNotesContent;
        public ViewFlipper mViewFlipper;

        // For other card layout
        public ImageButton mFlipButton;
        public ImageButton mFoundButton;
        public ImageButton mAddNotesButton;

        public boolean mCardBackShowing;
        public ViewHolder(View v) {
            super(v);
            mCardView = (CardView)v.findViewById(R.id.cv);

            mExplosiveName = (TextView)v.findViewById(R.id.explosiveName);
            mExplosiveAmount = (TextView)v.findViewById(R.id.explosiveAmount);
            mHidingPlace = (TextView)v.findViewById(R.id.hidingPlace);
            mDuration = (TextView)v.findViewById(R.id.duration);
//            mNotesContent = "";

            mFlipButton = (ImageButton) v.findViewById(R.id.flipBackButton);
            mFoundButton = (ImageButton) v.findViewById(R.id.confirmFoundButton);
            mAddNotesButton = (ImageButton) v.findViewById(R.id.addNotesButton);
            mViewFlipper = (ViewFlipper) v.findViewById(R.id.cardFlipper);

            mCardBackShowing = false;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TrainingCardAdapter(Explosive[] myDataset, Activity parent) {
        mDataset = myDataset;
        mParent = parent;
        mExplosivesLeftToFind = mDataset.length;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TrainingCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_training, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mExplosiveName.setText(mDataset[position].name);
        holder.mDuration.setVisibility(View.GONE);
        holder.mExplosiveAmount.setText("" + mDataset[position].quantity + " " + (mDataset[position].unit).toString().toLowerCase());
        holder.mHidingPlace.setText("" + mDataset[position].location);

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: Need to disable this if we're in the other side....could set a member var
                if (!holder.mCardBackShowing) {
                    clockedTime = NewTrainingFragment.time.getText().toString();
                    holder.mViewFlipper.showNext();
                    holder.mCardBackShowing = true;
                }
            }
        });

        holder.mFoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // flip back the card
                holder.mViewFlipper.showNext();
                holder.mCardBackShowing = false;

                // Log to the session
                Tuple<Explosive, String> loggedTime = new Tuple<>(mDataset[position], clockedTime);
                ((NewSessionActivity) mParent).session.logTime(loggedTime);

                // Update the textview with the logged time
                holder.mDuration.setVisibility(View.VISIBLE);
                holder.mDuration.setText(" found in " + clockedTime);

                // make the add notes button enabled
                holder.mAddNotesButton.setVisibility(View.VISIBLE);
                holder.mAddNotesButton.setClickable(true);

                // Make the found button go away but still occupy space
                holder.mFoundButton.setVisibility(View.INVISIBLE);
                holder.mFoundButton.setClickable(false);

                mExplosivesLeftToFind--;
                if(mExplosivesLeftToFind == 0){

                    switchToReviewSessionScreen();
                }
            }
        });

        holder.mAddNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewSessionActivity)mParent).showNotesDialog(position, holder.mExplosiveName.getText().toString());

                // Flip the card when you make a note
                holder.mViewFlipper.showNext();
                holder.mCardBackShowing = false;
            }
        });

        holder.mFlipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mViewFlipper.showNext();
                holder.mCardBackShowing = false;

                // TODO: INSERT COOL ANIMATION back to front
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    /**
     *
     * @param clocked_time
     * @param durationView: TextView to modify if they OK it
     * @return
     */
    public AlertDialog.Builder setupDialog(final String clocked_time, final TextView durationView,
                                           final TextView explosiveName, final CardView cv){
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(mParent);

        // 2. Chain together various setter methods to set the dialog characteristics
        String message = mParent.getText(R.string.dialog_message).toString();
        message += "\nClocked time: "+ clocked_time;
        builder.setMessage(message)
                .setTitle(R.string.dialog_title);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                String message = explosiveName.getText() + " found in: " + clocked_time;
                durationView.setVisibility(View.VISIBLE);
                durationView.setText(message);

                // Disable the cardview from being clicked again after being found
                cv.setEnabled(false);
                mExplosivesLeftToFind--;
                if(mExplosivesLeftToFind == 0){
//                    launchCompleteSessionDialog();
                }
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        return builder;
    }


    public void switchToReviewSessionScreen(){

        mParent.getFragmentManager().beginTransaction()
                .replace(R.id.new_session_fragment, new FinishedSessionFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
}