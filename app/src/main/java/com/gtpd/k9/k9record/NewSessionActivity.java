package com.gtpd.k9.k9record;


import android.Manifest;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;

import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NewSessionActivity extends AppCompatActivity implements NewTrainingFragment.OnFragmentInteractionListener,
        NotesDialogFragment.OnCompleteListener {

    public static TrainingSession session = new TrainingSession();

    private DialogFragment addNotesFragment;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private final String TAG = "NEW_SESSION";
    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("New Session");
        setContentView(R.layout.activity_new_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.newSessionContent, new DogSelectionFragment()).commit();
        }

        String [] locArr = fetchLocation();
        fetchWeather(locArr);
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
            setTitle("Select Dog");
            return true;
        } else {
            return super.onSupportNavigateUp();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // TODO
    }

    public void showNotesDialog(int selectedPos, String explosiveName) {
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
    public void dismiss() {
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
                    ((NotesDialogFragment) addNotesFragment).setNotesContent(result.get(0));
                }
                break;
            }
        }
    }


    public String[] fetchLocation() {

        String [] arr = new String[2];
        double[] gpsLocs = getGPS();
        if(session == null){
            Log.d(TAG, "SESSION IS NULL");//Toast.makeText(getApplicationContext(), "GPS locs: " + gpsLocs[0] + ", "+ gpsLocs[1] , Toast.LENGTH_SHORT).show();
        }
        session.setGPS(gpsLocs[0], gpsLocs[1]);
        Log.i(TAG, "GPS locs: " + gpsLocs[0] + ", "+ gpsLocs[1]);
//        Toast.makeText(getApplicationContext(), "GPS locs: " + gpsLocs[0] + ", "+ gpsLocs[1] , Toast.LENGTH_SHORT).show();

        Geocoder gc = new Geocoder(NewSessionActivity.this);
        List<Address> addresses;
        try {
            addresses = gc.getFromLocation(gpsLocs[0], gpsLocs[1], 1);
            //getFromLocationName(address, 1);
            if (addresses != null && addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                String city  = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                arr[0] = city;
                arr[1] = state;
                Log.i(TAG, city + ", " + state);
//                Toast.makeText(getApplicationContext(), city + ", " + state, Toast.LENGTH_SHORT).show();
                return arr;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error parsing the address", e);
            Toast.makeText(getApplicationContext(), "Error obtaining location", Toast.LENGTH_SHORT).show();
        }
        return arr;
    }

    public double[] getGPS() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

        /* Loop over the array backwards, and if you get an accurate location, then break                 out the loop*/
        Location l = null;

        for (int i = providers.size() - 1; i >= 0; i--) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
//                return TODO;
            }
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
        }

        double[] gps = new double[2];
        if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
        } else {
            // Default to culc for demonstration
            gps[0] = 33.774249;
            gps[1] = -84.396445;
        }
        return gps;
    }

    public void fetchWeather(String [] locArr) {
        if((locArr[0] == null || locArr[0].equals("") || locArr[1] == null || locArr[1].equals(""))) {
            Log.e(TAG, "Weather cannot be obtained atm");
//            Toast.makeText(getApplicationContext(), "Weather cannot be obtained at the moment", Toast.LENGTH_SHORT).show();
        } else {
            String URL = "http://api.wunderground.com/api/411cf1f3f83da7eb/conditions/q/"+ locArr[1];
            URL += "/" + locArr[0] + ".json";

            JsonObjectRequest req = new JsonObjectRequest(URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));
                                Log.i(TAG, response.toString());
                                // Update the sessions weather
                                session.setWeather((JSONObject) response.get("current_observation"));
//                                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });

            // add the request object to the queue to be executed
            addToRequestQueue(req);
        }
    }

    public void uploadSession() {

        final String URL = "http://ec2-52-207-245-173.compute-1.amazonaws.com/api/addSession";
        // Post params to be sent to the server
        JSONObject payload;
        try {
            payload = session.generateSessionPayload();
            Log.i(TAG, payload.toString());

            JsonObjectRequest req = new JsonObjectRequest(URL, payload,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Log.e(TAG, "ERROR POSTING SESSION", error);
            }
        });

        // add the request object to the queue to be executed
        addToRequestQueue(req);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
