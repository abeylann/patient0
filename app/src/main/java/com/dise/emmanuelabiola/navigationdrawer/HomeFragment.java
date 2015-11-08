package com.dise.emmanuelabiola.navigationdrawer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    TextView title;
    TextView description;
    ListView listView;
    SharedPreferences prefs;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    PatientDataSource patientDataSource;
    TextView bloodPressureButton;
    TextView temperatureButton;
    TextView diagnose;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        bloodPressureButton = (TextView) rootView.findViewById(R.id.takebloodpressure);
        bloodPressureButton.setOnClickListener(this);

        temperatureButton = (TextView) rootView.findViewById(R.id.checkTemperature);
        temperatureButton.setOnClickListener(this);

        diagnose = (TextView) rootView.findViewById(R.id.diagnose);
        diagnose.setOnClickListener(this);

        title = (TextView) rootView.findViewById(R.id.titleLabel);
        description = (TextView) rootView.findViewById(R.id.description);

        listView = (ListView) rootView.findViewById(R.id.listView);

        list = new ArrayList<>();

        adapter = new ArrayAdapter<String>(MainActivity.c, R.layout.list_item, list);

        listView.setAdapter(adapter);

        prefs = MainActivity.c.getSharedPreferences("Patient", Context.MODE_PRIVATE);
        patientDataSource = new PatientDataSource(MainActivity.c);

        Log.d("Test", prefs.toString());

        getData();

        return rootView;
    }

    public void getData() {
        title.setText("Patient doesn't exist yet");
        description.setText("Doing stuff here");

        String url = "http://178.62.120.115/api/v1/new_patient";

        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject patient = jsonResponse.getJSONObject("patient");

                    title.setText(patient.getString("name"));
                    description.setText(jsonResponse.getString("description"));

                    String test = "";
                    try {
                        patientDataSource.open();
                        test = patientDataSource.newPatient(patient.getString("name"), patient.getInt("blood_pressure"), patient.getInt("temperature"), patient.getInt("condition_id"), patient.getInt("id"));
                        patientDataSource.close();
                    } catch (SQLException s) {
                        s.printStackTrace();
                    }
                    Log.d("Testing", test);

                    adapter.notifyDataSetChanged();

                    Log.d("Response:", response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        Volley.newRequestQueue(MainActivity.c).add(postRequest);

        String secondUrl = "http://178.62.120.115//api/v1/get_conditions";

        StringRequest postRequestTwo = new StringRequest(Request.Method.GET, secondUrl, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    try {
                    for(int i = 0 ; i < jsonResponse.length() ; i++)
                    {
                        JSONObject condition = jsonResponse.getJSONObject(i);

                            patientDataSource.open();
                            patientDataSource.addConditionToDatabase(condition.getInt("id"), condition.getString("name"));


                    }
                        Log.d("Condition:", "" + patientDataSource.getConditionFromName("Stoned"));
                        patientDataSource.close();
                    }catch(SQLException s)
                    {
                        s.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        Volley.newRequestQueue(MainActivity.c).add(postRequestTwo);

    }

    public void onClick(View v) {
        //do what you want to do when bloodPressureButton is clicked
        /*switch (v.getId()) {
            case R.id.textView_help:
                switchFragment(HelpFragment.TAG);
                break;
            case R.id.textView_settings:
                switchFragment(SettingsFragment.TAG);
                break;
        }*/
        TextView b = (TextView) v;
        b.getText();
        PatientDataSource pds = new PatientDataSource(MainActivity.c);

        switch (v.getId()) {
            case R.id.takebloodpressure:
                Log.d("Got to the switch", "sdfsd");
                try {

                    pds.open();
                    if(list.contains("Blood Pressure: " + pds.getBloodPressure()))
                    {
                        break;
                    }
                    Log.d("Shouldn't", "Get here");
                    adapter.add("Blood Pressure: " + pds.getBloodPressure());
                    pds.close();
                } catch (SQLException s) {
                    s.printStackTrace();
                }

                break;

            case R.id.checkTemperature:
                Log.d("Got", "temp bloodPressureButton");
                try {
                    pds.open();
                    if(list.contains("Temperature is: " + pds.getTemperature()))
                    {
                        break;
                    }
                    adapter.add("Temperature is: " + pds.getTemperature());
                    pds.close();
                }catch(SQLException s)
                {
                    s.printStackTrace();
                }

                break;

            case R.id.diagnose:
                Log.d("Got", "Stuff");

        }
    }


}