package com.example.datvemaybay_dhcndonga;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.datvemaybay_dhcndonga.User.User;
import com.example.datvemaybay_dhcndonga.User.UserAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminViewUserActivity extends AppCompatActivity {

    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_user);

        userRecyclerView = findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        fetchUsers(); // Gọi API để lấy danh sách người dùng
    }

    private void fetchUsers() {
        String url = "http://10.0.2.2:3000/customers"; // Đối với Emulator, dùng 10.0.2.2

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray customers = response.getJSONArray("customers");
                                for (int i = 0; i < customers.length(); i++) {
                                    JSONObject userJson = customers.getJSONObject(i);
                                    int iduser = userJson.getInt("iduser");
                                    String username = userJson.getString("username");
                                    String fullname = userJson.getString("fullname");
                                    String birthdate = userJson.getString("birthdate");

                                    // Thêm người dùng vào danh sách
                                    userList.add(new User(iduser, username, fullname, convertDateTimeFormat(birthdate)));
                                }
                                userAdapter = new UserAdapter(userList);
                                userRecyclerView.setAdapter(userAdapter);
                            } else {
                                Toast.makeText(AdminViewUserActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminViewUserActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
    // Phương thức chuyển đổi định dạng ngày giờ
    private String convertDateTimeFormat(String dateTime) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = inputFormat.parse(dateTime);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateTime; // Trả về chuỗi ban đầu nếu xảy ra lỗi
        }
    }
}
