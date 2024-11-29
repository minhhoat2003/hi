package com.example.datvemaybay_dhcndonga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AdminLoginActivity extends AppCompatActivity {
    private EditText usernameText;
    private EditText passwordText;
    private Button loginButton;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        usernameText = findViewById(R.id.adminUsernameInput);
        passwordText = findViewById(R.id.adminPasswordInput);
        loginButton = findViewById(R.id.adminLoginButton);
        requestQueue = Volley.newRequestQueue(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        // Tìm nút chuyển về LoginActivity
        View switchToUserButton = findViewById(R.id.switchToUserLogin);

        // Thiết lập sự kiện khi nút được nhấn
        switchToUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển về LoginActivity
                Intent intent = new Intent(AdminLoginActivity.this, LoginActivity.class);
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
        String url = "http://10.0.2.2:3000/loginadmin";  // Use 10.0.2.2 for localhost in Android emulator

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
                                Toast.makeText(AdminLoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AdminLoginActivity.this, AdminMainActivity.class);
                                startActivity(intent);
                            } else {
                                // Login failed
                                Toast.makeText(AdminLoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AdminLoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        // Add the request to the Volley request queue
        requestQueue.add(jsonObjectRequest);
    }
}
