package com.gtpd.k9.k9record;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        this.getActivity().setTitle("Review");

        //Add the individual explosives to the view
        LinearLayout parent = (LinearLayout)view.findViewById(R.id.finished_session_root);
        View template = inflater.inflate(R.layout.single_explosive_card_template, null);
        RelativeLayout templateRoot = (RelativeLayout) template.getRootView();
        parent.addView(templateRoot);

        template = inflater.inflate(R.layout.single_explosive_card_template, null);
        templateRoot = (RelativeLayout) template.getRootView();
        parent.addView(templateRoot);

        //Fill all the text views
        populateItems();

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
            Toast.makeText(this.getActivity(), "Session Saved", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this.getActivity(), MainActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateItems(){
        TrainingSession session = NewSessionActivity.session;
        Dog dog = session.getDog();

        //Grab all the views that need to be set
        TextView dogNameView = (TextView) view.findViewById(R.id.dog_name_value);

        //Set all the views from session variables
        dogNameView.setText(dog.name);
    }
}
