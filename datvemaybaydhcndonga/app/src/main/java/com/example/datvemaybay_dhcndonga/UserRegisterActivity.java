package com.example.datvemaybay_dhcndonga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRegisterActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput, fullnameInput, birthdateInput;
    private Button registerButton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        fullnameInput = findViewById(R.id.fullname);
        birthdateInput = findViewById(R.id.birthdate);
        registerButton = findViewById(R.id.register_button);
        requestQueue = Volley.newRequestQueue(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        TextView switchToUserLogin = findViewById(R.id.login_link);
        switchToUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserRegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser() {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        String fullname = fullnameInput.getText().toString();
        String birthdate = birthdateInput.getText().toString();

        // Create a JSON object to hold the registration details
        JSONObject registerDetails = new JSONObject();
        try {
            registerDetails.put("username", username);
            registerDetails.put("password", password);
            registerDetails.put("fullname", fullname);
            registerDetails.put("birthdate", birthdate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Define the URL for registration
        String url = "http://10.0.2.2:3000/register";

        // Create a POST request using Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                registerDetails,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");
                            Toast.makeText(UserRegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                            if (success) {
                                // Redirect to LoginActivity
                                Intent intent = new Intent(UserRegisterActivity.this, LoginActivity.class);
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
                        Toast.makeText(UserRegisterActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the Volley request queue
        requestQueue.add(jsonObjectRequest);
    }
}
