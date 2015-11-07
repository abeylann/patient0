package com.dise.emmanuelabiola.navigationdrawer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    protected Button mLoginButton;
    

    //mLoginButton = (Button)findViewById(R.id.loginButton);
    //mLoginButton.setOnClickListener(new View.OnClickListener() {

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    // Login
    public void sendLogin() {
        String url = "http://178.62.120.115/api/v1/login";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    jsonResponse.get("email");
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
                //Set parameters as email, password and role
                Map<String, String> params = new HashMap<String, String>();
                params.put("email","test@test.com");


                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

    }

}

