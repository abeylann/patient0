package com.dise.emmanuelabiola.navigationdrawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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
public class HomeFragment extends Fragment {

    TextView title;
    TextView description;
    ListView listView;
    //ListAdapter listViewAdapter;
    SharedPreferences prefs;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        title = (TextView) rootView.findViewById(R.id.titleLabel);
        description = (TextView) rootView.findViewById(R.id.description);

        listView = (ListView) rootView.findViewById(R.id.listView);

        list = new ArrayList<>();

        adapter = new ArrayAdapter<String>(MainActivity.c, R.layout.list_item, list);

        listView.setAdapter(adapter);

        prefs = MainActivity.c.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        Log.d("Test", prefs.toString());

        getData();

        return rootView;
    }

    public void getData()
    {
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

                    adapter.add("Blood pressure: " + Integer.toString(patient.getInt("blood_pressure")));
                    adapter.add("Temperature: " + Integer.toString(patient.getInt("temperature")));

                    Log.d("Testing", listView.getItemAtPosition(0).toString());

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
    }
}