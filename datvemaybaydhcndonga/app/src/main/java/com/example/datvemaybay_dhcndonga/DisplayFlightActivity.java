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
import com.example.datvemaybay_dhcndonga.Flight.Flight;
import com.example.datvemaybay_dhcndonga.Flight.FlightAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplayFlightActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFlights;
    private FlightAdapter flightAdapter;
    private List<Flight> flights;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_list);

        // Khởi tạo RecyclerView
        recyclerViewFlights = findViewById(R.id.recycler_view_flights);
        recyclerViewFlights.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách chuyến bay và adapter
        flights = new ArrayList<>();
        flightAdapter = new FlightAdapter(flights, this);
        recyclerViewFlights.setAdapter(flightAdapter);

        // Khởi tạo RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Lấy thông tin tìm kiếm từ Intent
        String selectedDate = getIntent().getStringExtra("selectedDate");
        String classType = getIntent().getStringExtra("classType");
        int iduser = getIntent().getIntExtra("iduser", -1);  // Nhận iduser

        // Sử dụng iduser theo nhu cầu của bạn, ví dụ như gửi lên server hoặc hiển thị
        if (iduser != -1) {
            Toast.makeText(this, "User ID: " + iduser, Toast.LENGTH_SHORT).show();
        }

        // Lấy dữ liệu chuyến bay từ server
        fetchFlights(selectedDate, classType);
    }

    private void fetchFlights(String selectedDate, String classType) {
        String url = "http://10.0.2.2:3000/flights?date=" + selectedDate + "&classType=" + classType;

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
                                 JSONArray flightArray = response.getJSONArray("flights");
                                flights.clear(); // Xóa dữ liệu cũ
                                for (int i = 0; i < flightArray.length(); i++) {
                                    JSONObject flightObject = flightArray.getJSONObject(i);
                                    // Khởi tạo đối tượng Flight và thêm vào danh sách
                                    Flight flight = new Flight(
                                            flightObject.getString("flight_code"),
                                            flightObject.getString("departure_time"),
                                            flightObject.getString("arrival_time"),
                                            flightObject.getString("departure_location"),
                                            flightObject.getString("arrival_location"),
                                            flightObject.getInt("total_first_class_seats"),
                                            flightObject.getInt("available_first_class_seats"),
                                            flightObject.getInt("first_class_seat_price"),
                                            flightObject.getInt("total_economy_seats"),
                                            flightObject.getInt("available_economy_seats"),
                                            flightObject.getInt("economy_seat_price")
                                    );
                                    flights.add(flight);
                                }
                                // Thông báo adapter cập nhật dữ liệu
                                flightAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(DisplayFlightActivity.this, "No flights found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DisplayFlightActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Thêm yêu cầu vào hàng đợi
        requestQueue.add(jsonObjectRequest);
    }
}
