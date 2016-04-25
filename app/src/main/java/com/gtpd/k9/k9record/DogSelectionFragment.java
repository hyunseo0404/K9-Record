package com.gtpd.k9.k9record;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class DogSelectionFragment extends Fragment {

    private static final String DOG_API = "http://ec2-52-207-245-173.compute-1.amazonaws.com/api/getDogs";

    private DogAdapter dogAdapter;
    private RecyclerView dogList;
    private View continueView;
    private boolean continueViewShown = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            Dog selectedDog = new Gson().fromJson(savedInstanceState.getString("selected"), Dog.class);

            if (selectedDog != null) {
                continueView.setTranslationY(-savedInstanceState.getInt("height"));
                continueViewShown = true;
                dogList.scrollToPosition(dogAdapter.getSelectedPosition());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dog_selection, container, false);

        getActivity().setTitle("Select Dog");

        Dog selectedDog = null;
        int selectedPosition = -1;

        if (savedInstanceState != null) {
            selectedDog = new Gson().fromJson(savedInstanceState.getString("selected"), Dog.class);
            selectedPosition = savedInstanceState.getInt("selectedPosition");
        }

        dogAdapter = new DogAdapter(new ArrayList<Dog>(), new ArrayList<Dog>(), this, selectedDog, selectedPosition);
        dogList = (RecyclerView) view.findViewById(R.id.dogList);
        dogList.setAdapter(dogAdapter);
        dogList.setLayoutManager(new LinearLayoutManager(getActivity()));

        new DogTask(getActivity(), dogAdapter, RequestType.ASSIGNED).execute(MainActivity.account.getId());
        new DogTask(getActivity(), dogAdapter, RequestType.ALL).execute();

        Button continueButton = (Button) view.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewSessionActivity.session = new TrainingSession(dogAdapter.getSelectedDog());

                getFragmentManager().beginTransaction()
                        .add(R.id.newSessionContent, new ExplosiveSelectionFragment(), "explosive")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
            }
        });

        continueView = view.findViewById(R.id.continueView);

        return view;
    }

    public void animateContinueButton(final boolean show) {
        if (show && !continueViewShown) {
            continueView.animate().translationYBy(-continueView.getHeight());
            continueViewShown = true;
        } else if (!show && continueViewShown) {
            continueView.animate().translationYBy(continueView.getHeight());
            continueViewShown = false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (dogAdapter != null) {
            outState.putString("selected", new Gson().toJson(dogAdapter.getSelectedDog()));
            outState.putInt("selectedPosition", dogAdapter.getSelectedPosition());
            outState.putInt("height", continueView.getHeight());
        }
    }

    private static class DogTask extends AsyncTask<String, Void, JSONArray> {
        private Context context;
        private DogAdapter dogAdapter;
        private RequestType requestType;

        public DogTask(Context context, DogAdapter dogAdapter, RequestType requestType) {
            this.context = context;
            this.dogAdapter = dogAdapter;
            this.requestType = requestType;
        }

        protected JSONArray doInBackground(String... queries) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(DOG_API).openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(10000);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                switch (requestType) {
                    case ALL:
                        writer.write("{}");
                        break;
                    case ASSIGNED:
                        writer.write("{\"trainerGoogle_ID\":\"" + queries[0] + "\"}");
                        break;
                    case MATCHING:
                        writer.write("{\"names\":\"" + URLEncoder.encode(queries[0], "UTF-8") + "\"}");
                        break;
                }

                writer.flush();
                writer.close();
                os.close();

                connection.connect();

                if (connection.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    JSONObject result = new JSONObject(sb.toString());
                    return result.getJSONArray("return");
                } else {
                    return null;
                }
            } catch (IOException | JSONException e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        protected void onPostExecute(JSONArray result) {
            if (result == null) {
                Toast.makeText(context, "Error while fetching list of dogs", Toast.LENGTH_LONG).show();
            } else {
                ArrayList<Dog> dogs = new ArrayList<>(result.length());
                for (int i = 0; i < result.length(); i++) {
                    try {
                        JSONObject jsonDog = result.getJSONObject(i);
                        Dog dog = new Dog(jsonDog.getInt("P_ID"), jsonDog.getString("Name"), jsonDog.getString("Breed"), R.drawable.black_lab);
                        dogs.add(dog);
                    } catch (JSONException e) {
                        Toast.makeText(context, "Error while adding dogs", Toast.LENGTH_LONG).show();
                    }
                }

                switch (requestType) {
                    case ALL:
                    case MATCHING:
                        Collections.sort(dogs, new Comparator<Dog>() {
                            @Override
                            public int compare(Dog lhs, Dog rhs) {
                                return lhs.getName().compareToIgnoreCase(rhs.getName());
                            }
                        });
                        dogAdapter.setDogs(dogs);
                        break;
                    case ASSIGNED:
                        dogAdapter.setMyDogs(dogs);
                        break;
                }
            }
        }
    }

    private enum RequestType {
        ALL, ASSIGNED, MATCHING
    }
}
