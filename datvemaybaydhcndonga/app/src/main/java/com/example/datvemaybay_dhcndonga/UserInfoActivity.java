package com.example.datvemaybay_dhcndonga;

import android.os.Bundle;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserInfoActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private TextView usernameTextView;
    private TextView fullnameTextView;
    private TextView birthdateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // Khởi tạo các TextView
        usernameTextView = findViewById(R.id.usernameTextView);
        fullnameTextView = findViewById(R.id.fullnameTextView);
        birthdateTextView = findViewById(R.id.birthdateTextView);

        requestQueue = Volley.newRequestQueue(this);

        int iduser = getIntent().getIntExtra("iduser", -1);
        if (iduser != -1) {
            fetchUserInfo(iduser);
        } else {
            Toast.makeText(this, "User ID is invalid", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchUserInfo(int iduser) {
        String url = "http://10.0.2.2:3000/api/user/" + iduser; // Đối với Emulator, dùng 10.0.2.2

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
                                JSONObject user = response.getJSONObject("user");
                                String username = user.getString("username");
                                String fullname = user.getString("fullname");
                                String birthdate = user.getString("birthdate");

                                // Cập nhật UI với thông tin người dùng
                                usernameTextView.setText("Tên Đăng Nhập: " + username);
                                fullnameTextView.setText("Họ và Tên: " + fullname);
                                birthdateTextView.setText("Ngày Sinh: " + convertDateTimeFormat(birthdate));
                            } else {
                                Toast.makeText(UserInfoActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserInfoActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
