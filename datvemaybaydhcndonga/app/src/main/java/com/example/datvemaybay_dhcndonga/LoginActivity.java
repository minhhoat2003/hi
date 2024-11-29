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

public class LoginActivity extends AppCompatActivity {

    private EditText usernameText;
    private EditText passwordText;
    private Button loginButton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = findViewById(R.id.usernameInput);
        passwordText = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        requestQueue = Volley.newRequestQueue(this);  // Initialize Volley Request Queue

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        // Tìm nút chuyển sang AdminLoginActivity
        View switchToAdminButton = findViewById(R.id.switchToAdmin);

        // Thiết lập sự kiện khi nút được nhấn
        switchToAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển sang AdminLoginActivity
                Intent intent = new Intent(LoginActivity.this, AdminLoginActivity.class);
                startActivity(intent);
            }
        });

        View switchToRegisterButton = findViewById(R.id.switchToRegister);

        // Thiết lập sự kiện khi nút được nhấn
        switchToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển sang AdminLoginActivity
                Intent intent = new Intent(LoginActivity.this, UserRegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void attemptLogin() {
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        // Create a JSON object to hold the login details
        JSONObject loginDetails = new JSONObject();
        try {
            loginDetails.put("username", username);
            loginDetails.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Define the URL of the Node.js server (update with your server's IP or domain)
        String url = "http://10.0.2.2:3000/login";

        // Create a POST request using Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                loginDetails,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                // Login successful
                                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                // Get the iduser from the response
                                int iduser = response.getInt("iduser");
                                String fullname = response.getString("fullname");
                                // Login successful, pass iduser to UserMainActivity
                                Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                                intent.putExtra("iduser", iduser);  // Pass iduser to UserMainActivity
                                intent.putExtra("fullname", fullname);
                                startActivity(intent);
                            } else {
                                // Login failed
                                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the Volley request queue
        requestQueue.add(jsonObjectRequest);
    }
}
