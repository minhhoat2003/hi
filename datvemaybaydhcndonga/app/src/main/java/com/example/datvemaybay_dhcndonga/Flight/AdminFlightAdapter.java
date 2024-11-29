package com.example.datvemaybay_dhcndonga.Flight;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.datvemaybay_dhcndonga.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdminFlightAdapter extends RecyclerView.Adapter<AdminFlightAdapter.FlightViewHolder> {

    private List<Flight> flightList;
    private Context context;
    private RequestQueue requestQueue;

    public AdminFlightAdapter(List<Flight> flightList, Context context) {
        this.flightList = flightList;
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);

        // Chuyển đổi định dạng ngày giờ
        holder.flightCodeTextView.setText(flight.getFlightCode());
        holder.departureTimeTextView.setText(convertDateTimeFormat(flight.getDepartureTime()));
        holder.arrivalTimeTextView.setText(convertDateTimeFormat(flight.getArrivalTime()));
        holder.departureLocationTextView.setText(flight.getDepartureLocation());
        holder.arrivalLocationTextView.setText(flight.getArrivalLocation());
        holder.totalFirstClassSeatsTextView.setText(String.format("Hạng nhất: %d/%d", flight.getAvailableFirstClassSeats(), flight.getTotalFirstClassSeats()));
        holder.firstClassSeatPriceTextView.setText(String.valueOf(flight.getFirstClassSeatPrice()) + " VND");
        holder.totalEconomySeatsTextView.setText(String.format("Phổ thông: %d/%d", flight.getAvailableEconomySeats(), flight.getTotalEconomySeats()));
        holder.economySeatPriceTextView.setText(String.valueOf(flight.getEconomySeatPrice()) + " VND");

        holder.editFlightButton.setOnClickListener(v -> showEditDialog(flight, position));
        holder.deleteFlightButton.setOnClickListener(v -> deleteFlightFromServer(flight.getFlightCode(), position));
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

    private void showEditDialog(Flight flight, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_flight, null);
        builder.setView(dialogView);

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

        // Lưu lại flightCode ban đầu
        String originalFlightCode = flight.getFlightCode();

        // Set dữ liệu hiện tại vào các trường
        flightCodeInput.setText(flight.getFlightCode());
        departureTimeInput.setText(convertDateTimeFormat(flight.getDepartureTime()));
        arrivalTimeInput.setText(convertDateTimeFormat(flight.getArrivalTime()));
        departureLocationInput.setText(flight.getDepartureLocation());
        arrivalLocationInput.setText(flight.getArrivalLocation());
        totalFirstClassSeatsInput.setText(String.valueOf(flight.getTotalFirstClassSeats()));
        availableFirstClassSeatsInput.setText(String.valueOf(flight.getAvailableFirstClassSeats()));
        firstClassSeatPriceInput.setText(String.valueOf(flight.getFirstClassSeatPrice()));
        totalEconomySeatsInput.setText(String.valueOf(flight.getTotalEconomySeats()));
        availableEconomySeatsInput.setText(String.valueOf(flight.getAvailableEconomySeats()));
        economySeatPriceInput.setText(String.valueOf(flight.getEconomySeatPrice()));

        // Thêm OnClickListener cho TextView thời gian khởi hành
        departureTimeInput.setOnClickListener(v -> showDateTimePicker(departureTimeInput));

        // Thêm OnClickListener cho TextView thời gian đến
        arrivalTimeInput.setOnClickListener(v -> showDateTimePicker(arrivalTimeInput));

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            // Cập nhật các thông tin chuyến bay
            flight.setFlightCode(flightCodeInput.getText().toString());
            flight.setDepartureTime(departureTimeInput.getText().toString());
            flight.setArrivalTime(arrivalTimeInput.getText().toString());
            flight.setDepartureLocation(departureLocationInput.getText().toString());
            flight.setArrivalLocation(arrivalLocationInput.getText().toString());
            flight.setTotalFirstClassSeats(Integer.parseInt(totalFirstClassSeatsInput.getText().toString()));
            flight.setAvailableFirstClassSeats(Integer.parseInt(availableFirstClassSeatsInput.getText().toString()));
            flight.setFirstClassSeatPrice(Integer.parseInt(firstClassSeatPriceInput.getText().toString()));
            flight.setTotalEconomySeats(Integer.parseInt(totalEconomySeatsInput.getText().toString()));
            flight.setAvailableEconomySeats(Integer.parseInt(availableEconomySeatsInput.getText().toString()));
            flight.setEconomySeatPrice(Integer.parseInt(economySeatPriceInput.getText().toString()));

            // Cập nhật RecyclerView
            notifyItemChanged(position);

            // Gửi request lên server, sử dụng originalFlightCode thay vì flightCode mới
            updateFlightOnServer(flight, originalFlightCode);

            Toast.makeText(context, "Chuyến bay đã được cập nhật.", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showDateTimePicker(TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, selectedYear, selectedMonth, selectedDay) -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, (timeView, selectedHour, selectedMinute) -> {
                // Định dạng thời gian
                String dateTime = String.format("%04d-%02d-%02d %02d:%02d:%02d", selectedYear, selectedMonth + 1, selectedDay, selectedHour, selectedMinute, 0);
                textView.setText(dateTime);
            }, hour, minute, true);
            timePickerDialog.show();
        }, year, month, day);

        datePickerDialog.show();
    }

    private void updateFlightOnServer(Flight flight, String originalFlightCode) {
        String url = "http://10.0.2.2:3000/flights/" + originalFlightCode;

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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, params,
                response -> Toast.makeText(context, "Cập nhật chuyến bay thành công", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(context, "Lỗi khi cập nhật chuyến bay", Toast.LENGTH_SHORT).show());

        requestQueue.add(request);
    }


    private void deleteFlightFromServer(String flightCode, int position) {
        String url = "http://10.0.2.2:3000/flights/" + flightCode;

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    flightList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Xóa chuyến bay thành công", Toast.LENGTH_SHORT).show();
                },
                error -> Toast.makeText(context, "Lỗi khi xóa chuyến bay", Toast.LENGTH_SHORT).show());

        requestQueue.add(request);
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView flightCodeTextView, departureTimeTextView, arrivalTimeTextView, departureLocationTextView, arrivalLocationTextView, totalFirstClassSeatsTextView, firstClassSeatPriceTextView, totalEconomySeatsTextView, economySeatPriceTextView;
        Button editFlightButton, deleteFlightButton;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            flightCodeTextView = itemView.findViewById(R.id.flightCodeTextView);
            departureTimeTextView = itemView.findViewById(R.id.departureTimeTextView);
            arrivalTimeTextView = itemView.findViewById(R.id.arrivalTimeTextView);
            departureLocationTextView = itemView.findViewById(R.id.departureLocationTextView);
            arrivalLocationTextView = itemView.findViewById(R.id.arrivalLocationTextView);
            totalFirstClassSeatsTextView = itemView.findViewById(R.id.firstClassSeatsTextView);
            firstClassSeatPriceTextView = itemView.findViewById(R.id.firstClassPriceTextView);
            totalEconomySeatsTextView = itemView.findViewById(R.id.economySeatsTextView);
            economySeatPriceTextView = itemView.findViewById(R.id.economyPriceTextView);
            editFlightButton = itemView.findViewById(R.id.editFlightButton);
            deleteFlightButton = itemView.findViewById(R.id.deleteFlightButton);
        }
    }
}
