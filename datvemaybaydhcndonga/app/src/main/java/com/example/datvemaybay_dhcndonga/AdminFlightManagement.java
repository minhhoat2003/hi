package com.example.datvemaybay_dhcndonga;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.datvemaybay_dhcndonga.Flight.Flight;
import com.example.datvemaybay_dhcndonga.Flight.AdminFlightAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminFlightManagement extends AppCompatActivity {

    private RecyclerView flightRecyclerView;
    private AdminFlightAdapter flightAdapter;
    private List<Flight> flightList;
    private Button addFlightButton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_flight_management);

        flightRecyclerView = findViewById(R.id.flightRecyclerView);
        addFlightButton = findViewById(R.id.addFlightButton);
        requestQueue = Volley.newRequestQueue(this);

        flightRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        flightList = new ArrayList<>();
        flightAdapter = new AdminFlightAdapter(flightList, this);
        flightRecyclerView.setAdapter(flightAdapter);

        // Fetch flight data from server
        fetchFlightsFromServer();

        addFlightButton.setOnClickListener(v -> showAddFlightDialog());
    }

    private void fetchFlightsFromServer() {
        String url = "http://10.0.2.2:3000/flight"; // Update the URL accordingly

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            JSONArray flightsArray = response.getJSONArray("flights");
                            for (int i = 0; i < flightsArray.length(); i++) {
                                JSONObject flightObject = flightsArray.getJSONObject(i);
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
                                flightList.add(flight);
                            }
                            flightAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(AdminFlightManagement.this, "Không có chuyến bay nào.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(AdminFlightManagement.this, "Lỗi phân tích dữ liệu JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("Volley", "Error: " + error.getMessage());
                    Toast.makeText(AdminFlightManagement.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void showAddFlightDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm Chuyến Bay");

        // Inflate custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_flight, null);
        builder.setView(dialogView);

        // EditText fields for flight details
        EditText flightCodeInput = dialogView.findViewById(R.id.input_flight_code);
        TextView departureTimeInput = dialogView.findViewById(R.id.input_departure_time);
        TextView arrivalTimeInput = dialogView.findViewById(R.id.input_arrival_time);
        EditText departureLocationInput = dialogView.findViewById(R.id.input_departure_location);
        EditText arrivalLocationInput = dialogView.findViewById(R.id.input_arrival_location);
        EditText totalFirstClassSeatsInput = dialogView.findViewById(R.id.input_total_first_class_seats);
        EditText availableFirstClassSeatsInput = dialogView.findViewById(R.id.input_available_first_class_seats);
        EditText firstClassSeatPriceInput = dialogView.findViewById(R.id.input_first_class_seat_price);
        EditText totalEconomySeatsInput = dialogView.findViewById(R.id.input_total_economy_seats);
        EditText availableEconomySeatsInput = dialogView.findViewById(R.id.input_available_economy_seats);
        EditText economySeatPriceInput = dialogView.findViewById(R.id.input_economy_seat_price);

        departureTimeInput.setOnClickListener(v -> showDateTimePicker(departureTimeInput));
        arrivalTimeInput.setOnClickListener(v -> showDateTimePicker(arrivalTimeInput));

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            // Validate inputs before adding
            if (validateInputs(flightCodeInput, departureTimeInput, arrivalTimeInput,
                    departureLocationInput, arrivalLocationInput, totalFirstClassSeatsInput,
                    availableFirstClassSeatsInput, firstClassSeatPriceInput,
                    totalEconomySeatsInput, availableEconomySeatsInput, economySeatPriceInput)) {

                Flight newFlight = new Flight(
                        flightCodeInput.getText().toString(),
                        departureTimeInput.getText().toString(),
                        arrivalTimeInput.getText().toString(),
                        departureLocationInput.getText().toString(),
                        arrivalLocationInput.getText().toString(),
                        Integer.parseInt(totalFirstClassSeatsInput.getText().toString()),
                        Integer.parseInt(availableFirstClassSeatsInput.getText().toString()),
                        Integer.parseInt(firstClassSeatPriceInput.getText().toString()),
                        Integer.parseInt(totalEconomySeatsInput.getText().toString()),
                        Integer.parseInt(availableEconomySeatsInput.getText().toString()),
                        Integer.parseInt(economySeatPriceInput.getText().toString())
                );

                addFlightToServer(newFlight);
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private boolean validateInputs(EditText flightCode, TextView departureTime, TextView arrivalTime,
                                   EditText departureLocation, EditText arrivalLocation,
                                   EditText totalFirstClassSeats, EditText availableFirstClassSeats,
                                   EditText firstClassSeatPrice, EditText totalEconomySeats,
                                   EditText availableEconomySeats, EditText economySeatPrice) {
        if (flightCode.getText().toString().trim().isEmpty()) {
            flightCode.setError("Mã chuyến bay không được để trống");
            return false;
        }
        if (departureTime.getText().toString().trim().isEmpty()) {
            departureTime.setError("Thời gian khởi hành không được để trống");
            return false;
        }
        if (arrivalTime.getText().toString().trim().isEmpty()) {
            arrivalTime.setError("Thời gian đến không được để trống");
            return false;
        }
        if (departureLocation.getText().toString().trim().isEmpty()) {
            departureLocation.setError("Địa điểm khởi hành không được để trống");
            return false;
        }
        if (arrivalLocation.getText().toString().trim().isEmpty()) {
            arrivalLocation.setError("Địa điểm đến không được để trống");
            return false;
        }
        if (totalFirstClassSeats.getText().toString().trim().isEmpty()) {
            totalFirstClassSeats.setError("Tổng số ghế hạng nhất không được để trống");
            return false;
        }
        if (availableFirstClassSeats.getText().toString().trim().isEmpty()) {
            availableFirstClassSeats.setError("Số ghế hạng nhất còn trống không được để trống");
            return false;
        }
        if (firstClassSeatPrice.getText().toString().trim().isEmpty()) {
            firstClassSeatPrice.setError("Giá ghế hạng nhất không được để trống");
            return false;
        }
        if (totalEconomySeats.getText().toString().trim().isEmpty()) {
            totalEconomySeats.setError("Tổng số ghế hạng phổ thông không được để trống");
            return false;
        }
        if (availableEconomySeats.getText().toString().trim().isEmpty()) {
            availableEconomySeats.setError("Số ghế hạng phổ thông còn trống không được để trống");
            return false;
        }
        if (economySeatPrice.getText().toString().trim().isEmpty()) {
            economySeatPrice.setError("Giá ghế hạng phổ thông không được để trống");
            return false;
        }

        return true; // Tất cả các trường hợp đã hợp lệ
    }


    private void showDateTimePicker(TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                            (view1, selectedHour, selectedMinute) -> {
                                // Định dạng chuỗi thời gian theo định dạng mong muốn
                                String dateTime = String.format("%04d-%02d-%02d %02d:%02d:00",
                                        selectedYear, selectedMonth + 1, selectedDay, selectedHour, selectedMinute);
                                textView.setText(dateTime);
                            }, hour, minute, true);
                    timePickerDialog.show();
                }, year, month, day);
        datePickerDialog.show();
    }



    private void addFlightToServer(Flight flight) {
        String url = "http://10.0.2.2:3000/flights"; // Cập nhật URL nếu cần

        // Tạo đối tượng JSON chứa các thông tin chuyến bay
        JSONObject params = new JSONObject();
        try {
            params.put("flight_code", flight.getFlightCode());
            params.put("departure_time", flight.getDepartureTime());
            params.put("arrival_time", flight.getArrivalTime());
            params.put("departure_location", flight.getDepartureLocation());
            params.put("arrival_location", flight.getArrivalLocation());
            params.put("total_first_class_seats", flight.getTotalFirstClassSeats());
            params.put("available_first_class_seats", flight.getAvailableFirstClassSeats());
            params.put("first_class_seat_price", flight.getFirstClassSeatPrice());
            params.put("total_economy_seats", flight.getTotalEconomySeats());
            params.put("available_economy_seats", flight.getAvailableEconomySeats());
            params.put("economy_seat_price", flight.getEconomySeatPrice());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Tạo yêu cầu JSON
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                params,
                response -> {
                    try {
                        // Xử lý phản hồi từ server
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(AdminFlightManagement.this, "Thêm chuyến bay thành công!", Toast.LENGTH_SHORT).show();
                            flightList.add(flight); // Thêm chuyến bay vào danh sách
                            flightAdapter.notifyItemInserted(flightList.size() - 1); // Cập nhật adapter
                        } else {
                            Toast.makeText(AdminFlightManagement.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(AdminFlightManagement.this, "Lỗi phân tích dữ liệu JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Xử lý lỗi khi gửi yêu cầu
                    Log.e("Volley", "Error: " + error.getMessage());
                    Toast.makeText(AdminFlightManagement.this, "Lỗi thêm chuyến bay: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json"); // Đặt header Content-Type
                return headers;
            }
        };

        // Thêm yêu cầu vào hàng đợi
        requestQueue.add(jsonObjectRequest);
    }


}
