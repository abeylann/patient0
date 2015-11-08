package com.dise.emmanuelabiola.navigationdrawer;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiagnosisFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiagnosisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiagnosisFragment extends Fragment {


    EditText searchText;
    ListView searchList;
    ArrayList<String> searchArrayList;
    ArrayAdapter<String> searchListAdapter;
    PatientDataSource pds;
    GifImageView gif;

    public DiagnosisFragment()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_diagnosis, container, false);
        gif = (GifImageView) rootView.findViewById(R.id.gif);
        gif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new TestFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame, fragment).commit();
                } else {
                    // error in creating fragment
                    Log.e("MainActivity", "Error in creating fragment");
                }
            }
        });
        searchArrayList = new ArrayList<>();
        searchText = (EditText) rootView.findViewById(R.id.searchEditText);
        searchList = (ListView) rootView.findViewById(R.id.searchList);
        searchListAdapter = new ArrayAdapter<String>(MainActivity.c, R.layout.diagnosis_item, searchArrayList);
        searchList.setAdapter(searchListAdapter);
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                Log.d("Checking textview", tv.getText().toString());
                try{
                    pds.open();
                    int conditionId = pds.getConditionFromName(tv.getText().toString());
                    int patientId = pds.getId();
                    Log.d("Condition:", "" + conditionId + ":" + patientId);
                    pds.close();
                    sendDiagnosis(patientId, conditionId);
                }catch(SQLException s)
                {
                    s.printStackTrace();
                }
            }
        });


        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                searchListAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        pds = new PatientDataSource(MainActivity.c);
        try{
            pds.open();
            searchArrayList.addAll(pds.getAllConditions());
            pds.close();
        }
        catch(SQLException s)
        {
            s.printStackTrace();
        }
        return rootView;
    }

    private void sendDiagnosis(int patientId, int conditionId)
    {
        String secondUrl = "http://178.62.120.115/api/v1/diagnose_patient?patient_id=" + patientId + "&condition_id=" + conditionId ;

        StringRequest getRequest = new StringRequest(Request.Method.GET, secondUrl, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.d("Checking:", response);
                    if(jsonResponse.get("result") == true)
                    {
                        gif.setVisibility(View.VISIBLE);

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
        Volley.newRequestQueue(MainActivity.c).add(getRequest);
    }

}
