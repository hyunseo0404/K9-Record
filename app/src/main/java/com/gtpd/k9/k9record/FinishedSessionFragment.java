package com.gtpd.k9.k9record;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Clayton on 3/13/2016.
 */
public class FinishedSessionFragment extends Fragment {

    private Menu actionBarMenu;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_finished_session, container, false);
        setHasOptionsMenu(true);

        ((NewSessionActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);

        this.getActivity().setTitle("Review");

        //Fill all the text views
        populateItems(inflater);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_finished_session_actions, menu);
        actionBarMenu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save_session) {
            ((NewSessionActivity)this.getActivity()).uploadSession();
            Toast.makeText(this.getActivity(), "Session Saved", Toast.LENGTH_SHORT).show();

//            Snackbar.make(view, "Session saved", Snackbar.LENGTH_LONG).show();
            Intent intent = new Intent(this.getActivity(), MainActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateItems(LayoutInflater inflater){
        final TrainingSession session = NewSessionActivity.session;
        Dog dog = session.getDog();

        //Grab all the views that need to be set
        TextView dogNameView = (TextView) view.findViewById(R.id.dog_name_value);

        //Set all the views from session variables
        dogNameView.setText(dog.getName());

        // Temperature
        TextView temperatureView = (TextView) view.findViewById(R.id.temperatureContent);
        temperatureView.setText(session.getTemperatureStr());

        // Wind
        TextView windView = (TextView) view.findViewById(R.id.windContent);
        windView.setText(session.getWindStr());

        // Location
        TextView locationView = (TextView) view.findViewById(R.id.locationContent);
        locationView.setText(session.getGPS());

        // Elapsed time
        TextView timeElapsedView = (TextView) view.findViewById(R.id.timeElapsedContent);
        timeElapsedView.setText(session.getElapsedTime());

        // Weather desc
        TextView weatherDescView = (TextView) view.findViewById(R.id.weatherDescContent);
        weatherDescView.setText(session.getWeatherDesc());

        //add all of the explosive cards
        //Add the individual explosives to the view
        LinearLayout parent = (LinearLayout)view.findViewById(R.id.finished_session_root);
        View template;
        RelativeLayout templateRoot;
        int exp_itr = 0;
        for(final Explosive explosive : session.explosives){
            template = inflater.inflate(R.layout.single_explosive_card_template, null);
            templateRoot = (RelativeLayout) template.getRootView();

            //Set all of the textviews
            TextView explosiveTitle = (TextView) templateRoot.findViewById(R.id.explosive_card_title);
            TextView explosiveQuantity = (TextView) templateRoot.findViewById(R.id.explosive_quantity_value);
            TextView explosiveLocation = (TextView) templateRoot.findViewById(R.id.explosive_location_value);

            TextView explosiveStartTime = (TextView) templateRoot.findViewById(R.id.startTimeContent);
            TextView explosiveEndTime = (TextView) templateRoot.findViewById(R.id.endTimeContent);
            TextView explosiveTimeToFind = (TextView) templateRoot.findViewById(R.id.timeToFindContent);

            TextView explosivesResults = (TextView) templateRoot.findViewById(R.id.resultsContent);
            TextView heightView = (TextView) templateRoot.findViewById(R.id.heightContent);
            TextView depthView = (TextView) templateRoot.findViewById(R.id.depthContent);

            TextView noteCountLabel = (TextView) templateRoot.findViewById(R.id.noteCountLabel);
            final ListView notesView = (ListView) templateRoot.findViewById(R.id.notesListView);

            ImageButton addNoteButton = (ImageButton) templateRoot.findViewById(R.id.addExplosiveNoteButton);

            explosiveTitle.setText(explosive.name);
            explosiveQuantity.setText(explosive.getQuantityAsString());
            explosiveLocation.setText(explosive.location);
            explosiveStartTime.setText(explosive.getStartTime().toString());
            explosiveEndTime.setText(explosive.getEndTime().toString());
            explosiveTimeToFind.setText(explosive.getElapsedTime());
            depthView.setText("" + explosive.depth);
            heightView.setText("" + explosive.height);

            explosivesResults.setText(Arrays.toString(explosive.getResultsStr()));

            // TODO: Add in the subsection for notes using simple arrayAdapter
            List<String> noteContent = session.getNotes(explosive);
            if(noteContent == null) {
                noteContent = new ArrayList<String>();
            }
            noteCountLabel.setText(" (" + noteContent.size() + "):");

            final ArrayAdapter adapter = new ArrayAdapter<String>(this.getActivity(),
                    android.R.layout.simple_list_item_1,
                    noteContent);
            notesView.setAdapter(adapter);
            setListViewHeightBasedOnItems(notesView);

            final int finalExp_itr = exp_itr;
            addNoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((NewSessionActivity) getActivity()).showNotesDialog(finalExp_itr, explosive.name);
                    ((NewSessionActivity) getActivity()).updateAdapter(adapter, notesView);
                }
            });
            parent.addView(templateRoot);
            exp_itr++;
        }

        // TODO: Generate the notes

    }


    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }
}
