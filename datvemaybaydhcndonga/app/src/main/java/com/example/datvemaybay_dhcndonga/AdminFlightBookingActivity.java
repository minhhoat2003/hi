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
import com.example.datvemaybay_dhcndonga.Booking.Booking;
import com.example.datvemaybay_dhcndonga.Booking.BookingAdapter;
import com.example.datvemaybay_dhcndonga.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AdminFlightBookingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_booking);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(bookingList);
        recyclerView.setAdapter(bookingAdapter);

        requestQueue = Volley.newRequestQueue(this);

        fetchBookings();
    }

    private void fetchBookings() {
        String url = "http://10.0.2.2:3000/bookings";  // Replace with your server URL

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray bookingsArray = response.getJSONArray("bookings");
                                for (int i = 0; i < bookingsArray.length(); i++) {
                                    JSONObject bookingObject = bookingsArray.getJSONObject(i);
                                    String bookedBy = bookingObject.getString("booked_by");
                                    String bookingDate = bookingObject.getString("booking_date");
                                    int totalAmount = bookingObject.getInt("total_amount");

                                    bookingList.add(new Booking(bookedBy, bookingDate, totalAmount));
                                }
                                bookingAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(AdminFlightBookingActivity.this, "Failed to fetch bookings", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminFlightBookingActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}
