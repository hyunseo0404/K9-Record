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
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.sql.Timestamp;
import java.util.Date;
import android.support.design.widget.Snackbar;


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

    private RecyclerView mRecView;
    private OnFragmentInteractionListener mListener;
    private TrainingCardAdapter mAdapter;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_training, container, false);

        ((NewSessionActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
        getActivity().setTitle("New Session");

        mRecView = (RecyclerView) view.findViewById(R.id.training_recycler_view);
        mRecView.setHasFixedSize(true);

        // Set the layout manager
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mRecView.setLayoutManager(llm);


        Explosive [] expArr = new Explosive[NewSessionActivity.session.explosives.size()];
        NewSessionActivity.session.explosives.toArray(expArr);
        mAdapter = new TrainingCardAdapter(expArr, getActivity(), mRecView);
        mRecView.setAdapter(mAdapter);

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

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        if (mAdapter != null) {
//            outState.putString("explosives", new Gson().toJson(mAdapter.getExplosives()));
//        }
//    }
}


class TrainingCardAdapter extends RecyclerView.Adapter<TrainingCardAdapter.ViewHolder> {
    private Explosive[] mDataset;
    private Activity mParent;
    private int mExplosivesLeftToFind;
    private RecyclerView mContainer;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView mCardView;
        public TextView mExplosiveName;
        public TextView mExplosiveAmount;
        public TextView mHidingPlace;
        public TextView mDuration;
        public ViewFlipper mViewFlipper;
        public Button mStartButton;
        public Button mResetButton;
        public TextView mAidStatus;

        // For other card layout
        public ImageButton mFlipButton;
        public ImageButton mFindButton;
        public ImageButton mFalsePosButton;
        public ImageButton mHandlerErrorButton;
        public ImageButton mMissButton;
        public ImageButton mAddNotesButton;

        // Frame layouts for buttons that can be hidden
        public FrameLayout mFoundButtonFrame;
        public FrameLayout mMissButtonFrame;
        public FrameLayout mFalsePosBtnFrame;
        public FrameLayout mHandlerErrorBtnFrame;
        public FrameLayout mAddNotesBtnFrame;

        public boolean mCardBackShowing;
        public ViewHolder(View v) {
            super(v);
            mCardView = (CardView)v.findViewById(R.id.cv);

            mStartButton = (Button) v.findViewById(R.id.startIndividualButton);
            mResetButton = (Button) v.findViewById(R.id.resetIndividualButton);
            mAidStatus = (TextView) v.findViewById(R.id.aidStatusLabel);

            mExplosiveName = (TextView)v.findViewById(R.id.explosiveName);
            mExplosiveAmount = (TextView)v.findViewById(R.id.explosiveAmount);
            mHidingPlace = (TextView)v.findViewById(R.id.hidingPlace);
            mDuration = (TextView)v.findViewById(R.id.duration);

            // Buttons
            mFlipButton = (ImageButton) v.findViewById(R.id.flipBackButton);
            mFindButton = (ImageButton) v.findViewById(R.id.confirmFoundButton);
            mMissButton = (ImageButton) v.findViewById(R.id.missButton);
            mFalsePosButton = (ImageButton) v.findViewById(R.id.falsePositiveButton);
            mHandlerErrorButton = (ImageButton) v.findViewById(R.id.handlerErrorButton);

            mAddNotesButton = (ImageButton) v.findViewById(R.id.addNotesButton);

            // Frames
            mFoundButtonFrame = (FrameLayout) v.findViewById(R.id.confirmFoundButtonFrame);
            mMissButtonFrame = (FrameLayout) v.findViewById(R.id.missButtonFrame);
            mFalsePosBtnFrame = (FrameLayout) v.findViewById(R.id.falsePositiveButtonFrame);
            mHandlerErrorBtnFrame = (FrameLayout) v.findViewById(R.id.handlerErrorButtonFrame);
            mAddNotesBtnFrame = (FrameLayout) v.findViewById(R.id.addNotesButtonFrame);

            mViewFlipper = (ViewFlipper) v.findViewById(R.id.cardFlipper);

            mCardBackShowing = false;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TrainingCardAdapter(Explosive[] myDataset, Activity parent, RecyclerView container) {
        mDataset = myDataset;
        mParent = parent;
        mExplosivesLeftToFind = mDataset.length;
        mContainer = container;
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
        holder.mExplosiveAmount.setText("" + mDataset[position].quantity +
                " " + (mDataset[position].unit).toString().toLowerCase());
        holder.mHidingPlace.setText("" + mDataset[position].location);

        holder.mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((NewSessionActivity) mParent).session.activeExplosiveIndex == -1) {
                    holder.mStartButton.setVisibility(View.GONE);
                    holder.mAidStatus.setText("ACTIVE");
                    ((NewSessionActivity) mParent).session.activeExplosiveIndex = position;
                    Timestamp time = new Timestamp((new Date()).getTime());
                    mDataset[position].setStartTime(time);
                    if(((NewSessionActivity) mParent).session.startTime == null) {
                        ((NewSessionActivity) mParent).session.startTime = time;
                    }

//                holder.mCardView.setCardBackgroundColor(R.color.colorAccent);
                    holder.mViewFlipper.setBackgroundColor(ContextCompat.getColor(mParent, R.color.colorAccent));

                } else {
                    Snackbar.make(mContainer, "ANOTHER AID IS CURRENTLY ACTIVE", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (!holder.mCardBackShowing) {
                holder.mViewFlipper.showNext();
                holder.mCardBackShowing = true;
            }
            }
        });

        holder.mFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // flip back the card
                holder.mViewFlipper.showNext();
                holder.mCardBackShowing = false;
                holder.mAidStatus.setText("COMPLETED");
                holder.mViewFlipper.setBackgroundColor(Color.WHITE);
//                holder.mCardView.setCardBackgroundColor(Color.WHITE);


                java.util.Date date = new java.util.Date();
                Timestamp time = new Timestamp(date.getTime());
                mDataset[position].setEndTime(time);
                String clockedTime = mDataset[position].getElapsedTime();

                // Log to the session
                Tuple<Explosive, String> loggedTime = new Tuple<>(mDataset[position], clockedTime);
                ((NewSessionActivity) mParent).session.logTime(loggedTime);
                ((NewSessionActivity) mParent).session.activeExplosiveIndex = -1;

                // Update the textview with the logged time
                holder.mDuration.setVisibility(View.VISIBLE);
                holder.mDuration.setText(" found in " + clockedTime);

                // Make the found button go away but still occupy space
                holder.mFoundButtonFrame.setVisibility(View.INVISIBLE);
                holder.mFindButton.setClickable(false);

                holder.mFalsePosBtnFrame.setVisibility(View.INVISIBLE);
                holder.mMissButtonFrame.setVisibility(View.INVISIBLE);
                holder.mHandlerErrorBtnFrame.setVisibility(View.GONE);

                // make the add notes button enabled
                holder.mAddNotesBtnFrame.setVisibility(View.VISIBLE);
                holder.mAddNotesButton.setClickable(true);

                // If the explosive was found decrement the count
                mExplosivesLeftToFind--;

                if (mExplosivesLeftToFind == 0) {
                    ((NewSessionActivity) mParent).session.endTime = time;
                    switchToReviewSessionScreen();
                }
            }
        });

        holder.mMissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // flip back the card
//                holder.mViewFlipper.showNext();
//                holder.mCardBackShowing = false;
//                holder.mAidStatus.setText("COMPLETED");
//                holder.mViewFlipper.setBackgroundColor(Color.WHITE);

                java.util.Date date = new java.util.Date();
                Timestamp time = new Timestamp(date.getTime());
                mDataset[position].addMiss(time);

                Snackbar.make(mContainer, "Miss recorded", Snackbar.LENGTH_LONG).show();

                //setEndTime(time);
//                String clockedTime = mDataset[position].getElapsedTime();

                // Log to the session
//                Tuple<Explosive, String> loggedTime = new Tuple<>(mDataset[position], clockedTime);
//                ((NewSessionActivity) mParent).session.logTime(loggedTime);
//                ((NewSessionActivity) mParent).session.activeExplosiveIndex = -1;

                // Update the textview with the logged time
//                holder.mDuration.setVisibility(View.VISIBLE);
//                holder.mDuration.setText(" timer stopped at " + clockedTime);

                // Make the found button go away but still occupy space
//                holder.mFoundButtonFrame.setVisibility(View.INVISIBLE);
//                holder.mFindButton.setClickable(false);
//
//                holder.mFalsePosBtnFrame.setVisibility(View.INVISIBLE);
//                holder.mMissButtonFrame.setVisibility(View.INVISIBLE);
//                holder.mHandlerErrorBtnFrame.setVisibility(View.GONE);
//
//                // make the add notes button enabled
//                holder.mAddNotesBtnFrame.setVisibility(View.VISIBLE);
//                holder.mAddNotesButton.setClickable(true);
//
//                // If the explosive was found decrement the count
//                mExplosivesLeftToFind--;
//
//                if (mExplosivesLeftToFind == 0) {
//                    switchToReviewSessionScreen();
//                }
            }
        });

        holder.mFalsePosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Date date = new java.util.Date();
                Timestamp time = new Timestamp(date.getTime());
                mDataset[position].addFalsePositive(time);

                Snackbar.make(mContainer, "False positive recorded", Snackbar.LENGTH_LONG).show();
            }
        });

        holder.mHandlerErrorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Date date = new java.util.Date();
                Timestamp time = new Timestamp(date.getTime());
                mDataset[position].addHandlerError(time);

                Snackbar.make(mContainer, "Handler error recorded", Snackbar.LENGTH_LONG).show();
            }
        });

        holder.mAddNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewSessionActivity) mParent).showNotesDialog(position, holder.mExplosiveName.getText().toString());

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
                .replace(R.id.newSessionContent, new FinishedSessionFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
}