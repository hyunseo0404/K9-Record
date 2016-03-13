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
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewTrainingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewTrainingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewTrainingFragment extends Fragment {
    private static final String ARG_EXPL_LIST = "explosives list";

    private List<Explosive> mExplosivesList;
    private Menu mMenu;

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
    Handler handler = new Handler();

    private GridView mGridView;
    private RecyclerView mRecView;
    private OnFragmentInteractionListener mListener;

    public NewTrainingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param expList Parameter 1.
     * @return A new instance of fragment NewTrainingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewTrainingFragment newInstance(List<Explosive> expList) {
        NewTrainingFragment fragment = new NewTrainingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EXPL_LIST, new Gson().toJson(expList));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Type listType = new TypeToken<List<Explosive>>(){}.getType();
            mExplosivesList = new Gson().fromJson(getArguments().getString(ARG_EXPL_LIST), listType);
        }
        setHasOptionsMenu(true);
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

//        gridView = (GridView) view.findViewById(R.id.gridView);

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

//        ArrayAdapter<String> itemsAdapter =
//                new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, this.mExplosivesList);
//        mGridView.setAdapter(itemsAdapter);
//        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
////                Toast.makeText(NewTrainingFragment.this.getActivity(), "" + position,
////                        Toast.LENGTH_SHORT).show();
//
//                String clocked_time = time.getText().toString();
//
//                AlertDialog.Builder builder = setupDialog(clocked_time, ((TextView)v));
//
//                // Get the AlertDialog from create()
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        });


        mRecView = (RecyclerView) view.findViewById(R.id.training_recycler_view);
        mRecView.setHasFixedSize(true);

        // Set the layout manager
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mRecView.setLayoutManager(llm);

        Explosive [] expArr = new Explosive[mExplosivesList.size()];
        mExplosivesList.toArray(expArr);
        TrainingCardAdapter mAdapter = new TrainingCardAdapter(expArr, getActivity());
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
}


class TrainingCardAdapter extends RecyclerView.Adapter<TrainingCardAdapter.ViewHolder> {
    private Explosive[] mDataset;
    private Activity mParent;
    private int mExplosiveFoundCount;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mExplosiveName;
        CardView mCardView;
        public TextView mDuration;

        public ViewHolder(View v) {
            super(v);
            mCardView = (CardView)v.findViewById(R.id.cv);
            mExplosiveName = (TextView)v.findViewById(R.id.explosiveName);
            mDuration = (TextView)v.findViewById(R.id.duration);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TrainingCardAdapter(Explosive[] myDataset, Activity parent) {
        mDataset = myDataset;
        mParent = parent;
        mExplosiveFoundCount = mDataset.length;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TrainingCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_training, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mExplosiveName.setText(mDataset[position].name);
        holder.mDuration.setVisibility(View.GONE);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // item clicked
                String clocked_time = NewTrainingFragment.time.getText().toString();

                AlertDialog.Builder builder = setupDialog(clocked_time,
                                                          holder.mDuration,
                                                          holder.mExplosiveName,
                                                          holder.mCardView);
                builder.show();
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
                mExplosiveFoundCount--;
                if(mExplosiveFoundCount == 0){
                    launchCompleteSessionDialog();
                }
            }
        });

        builder.setNeutralButton(R.string.add_notes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        return builder;
    }


    public void launchCompleteSessionDialog(){
        /*// 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(mParent);

        // 2. Chain together various setter methods to set the dialog characteristics
        String title = "Complete training session";
        String message = "All explosives found, session complete?";
        builder.setMessage(message)
                .setTitle(title)
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mParent, "TODO: Session completed", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();*/

        mParent.getFragmentManager().beginTransaction()
                .replace(R.id.new_session_fragment, new FinishedSessionFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
}