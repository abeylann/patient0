package com.dise.emmanuelabiola.navigationdrawer;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    protected EditText mUsername;
    protected EditText mPassword;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = (EditText) findViewById(R.id.usernameField);
        mPassword = (EditText) findViewById(R.id.passwordField);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                Log.d("stuff","thing");
                username = username.trim();
                password = password.trim();

                if (username.isEmpty() || password.isEmpty()) {
                    /*AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.login_error_message)
                            .setTitle(R.string.login_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();*/
                    sendLogin(username,password);
                } else {
                    // Login
                    setProgressBarIndeterminateVisibility(true);

                    // Login using volley
                    sendLogin(username, password);

                }
            }
        });

    }

    public void sendLogin(final String username, final String password) {
        String url = "http://178.62.120.115/api/v1/login";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.d("Response:", response);
                    String email = jsonResponse.getString("email");

                    prefs = getSharedPreferences("UserDetails", MODE_PRIVATE);

                    if (email != ""){
                        SharedPreferences.Editor prefsEditor = prefs.edit();
                        prefsEditor.putString("email", email);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
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
                        Toast.makeText(getApplicationContext(), "Incorrect username / password", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //Set parameters as email, password and role
                Map<String, String> params = new HashMap<String, String>();
                //params.put("email", username);
                //params.put("password", password);
                params.put("email", "test@test.com");
                params.put("password", "testtest");

                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

    }
}

