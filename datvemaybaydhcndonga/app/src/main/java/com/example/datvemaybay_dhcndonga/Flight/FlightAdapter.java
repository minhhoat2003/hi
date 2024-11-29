package com.example.datvemaybay_dhcndonga.Flight;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
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
import com.android.volley.toolbox.Volley;
import com.example.datvemaybay_dhcndonga.DisplayFlightActivity;
import com.example.datvemaybay_dhcndonga.QRCodeActivity;
import com.example.datvemaybay_dhcndonga.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {

    private List<Flight> flightList;
    private Context context; // Thêm biến context để hiển thị Toast

    public FlightAdapter(List<Flight> flightList, Context context) {
        this.flightList = flightList;
        this.context = context; // Lưu context
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flight_item, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);

        // Set dữ liệu cho các TextView
        holder.flightCodeTextView.setText(flight.getFlightCode());
        holder.departureTimeTextView.setText(flight.getDepartureTime());
        holder.arrivalTimeTextView.setText(flight.getArrivalTime());
        holder.departureLocationTextView.setText(flight.getDepartureLocation());
        holder.arrivalLocationTextView.setText(flight.getArrivalLocation());
        holder.totalFirstClassSeatsTextView.setText(String.valueOf(flight.getTotalFirstClassSeats()));
        holder.availableFirstClassSeatsTextView.setText(String.valueOf(flight.getAvailableFirstClassSeats()));
        holder.firstClassSeatPriceTextView.setText(String.valueOf(flight.getFirstClassSeatPrice()) + " VND");
        holder.totalEconomySeatsTextView.setText(String.valueOf(flight.getTotalEconomySeats()));
        holder.availableEconomySeatsTextView.setText(String.valueOf(flight.getAvailableEconomySeats()));
        holder.economySeatPriceTextView.setText(String.valueOf(flight.getEconomySeatPrice()) + " VND");

        // Sự kiện bấm nút Đặt vé hạng nhất
        holder.bookFirstClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookingDialog(flight.getFlightCode(), flight.getAvailableFirstClassSeats(), flight.getFirstClassSeatPrice(), "first_class");
            }
        });

        // Sự kiện bấm nút Đặt vé thường
        holder.bookEconomyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookingDialog(flight.getFlightCode(), flight.getAvailableEconomySeats(), flight.getEconomySeatPrice(), "economy");
            }
        });
    }

    private void showBookingDialog(String flightCode, int availableSeats, int seatPrice, String classType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Nhập số lượng vé");

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Đặt vé", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enteredText = input.getText().toString();
                if (!enteredText.isEmpty()) {
                    int ticketQuantity = Integer.parseInt(enteredText);

                    if (ticketQuantity > availableSeats) {
                        Toast.makeText(context, "Số lượng vé không được vượt quá " + availableSeats, Toast.LENGTH_SHORT).show();
                    } else {
                        // Tính tổng số tiền
                        int totalAmount = ticketQuantity * seatPrice;

                        // Tạo URL cho mã QR (chỉ là ví dụ)
                        String qrUrl = "https://img.vietqr.io/image/vpbank-0338014464-compact2.jpg?amount=" + totalAmount + "&addInfo=chuyen%20tien%20ve%20may%20bay&accountName=PHAM%20MINH%20HOAT";

                        // Lấy iduser từ Intent hoặc context (cập nhật theo app của bạn)
                        int iduser = ((DisplayFlightActivity) context).getIntent().getIntExtra("iduser", -1);

                        // Gọi hàm gửi yêu cầu đặt vé đến server
                        bookFlight(iduser, flightCode, classType, ticketQuantity);

                        // Tạo Intent để chuyển sang QRCodeActivity để hiển thị QR code
                        Intent intent = new Intent(context, QRCodeActivity.class);
                        intent.putExtra("qrUrl", qrUrl);
                        intent.putExtra("flightInfo", flightCode + " (" + ticketQuantity + " vé, tổng: " + totalAmount + " VND)");

                        // Bắt đầu Activity hiển thị QR code
                        context.startActivity(intent);
                    }
                } else {
                    Toast.makeText(context, "Vui lòng nhập số lượng vé", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // Hàm gửi yêu cầu đặt vé đến server
    private void bookFlight(int iduser, String flightCode, String classType, int ticketQuantity) {
        String url = "http://192.168.108.100:3000/bookings"; // Địa chỉ server của bạn

        // Xác định seat_type dựa trên classType
        int seatType = classType.equals("first_class") ? 0 : 1; // 0 cho hạng nhất, 1 cho hạng thường

        // Tạo JSON object chứa dữ liệu đặt vé
        JSONObject bookingData = new JSONObject();
        try {
            bookingData.put("iduser", iduser);
            bookingData.put("flight_code", flightCode);
            bookingData.put("seat_type", seatType);
            bookingData.put("number_of_ticket", ticketQuantity);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Gửi yêu cầu POST lên server
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, bookingData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Thêm yêu cầu vào hàng đợi
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }






    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView flightCodeTextView;
        TextView departureTimeTextView;
        TextView arrivalTimeTextView;
        TextView departureLocationTextView;
        TextView arrivalLocationTextView;
        TextView totalFirstClassSeatsTextView;
        TextView availableFirstClassSeatsTextView;
        TextView firstClassSeatPriceTextView;
        TextView totalEconomySeatsTextView;
        TextView availableEconomySeatsTextView;
        TextView economySeatPriceTextView;
        Button bookFirstClassButton; // Nút Đặt vé hạng nhất
        Button bookEconomyButton; // Nút Đặt vé thường

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            flightCodeTextView = itemView.findViewById(R.id.tv_flight_code);
            departureTimeTextView = itemView.findViewById(R.id.tv_departure_time);
            arrivalTimeTextView = itemView.findViewById(R.id.tv_arrival_time);
            departureLocationTextView = itemView.findViewById(R.id.tv_departure_location);
            arrivalLocationTextView = itemView.findViewById(R.id.tv_arrival_location);
            totalFirstClassSeatsTextView = itemView.findViewById(R.id.tv_total_first_class_seats);
            availableFirstClassSeatsTextView = itemView.findViewById(R.id.tv_available_first_class_seats);
            firstClassSeatPriceTextView = itemView.findViewById(R.id.tv_first_class_seat_price);
            totalEconomySeatsTextView = itemView.findViewById(R.id.tv_total_economy_seats);
            availableEconomySeatsTextView = itemView.findViewById(R.id.tv_available_economy_seats);
            economySeatPriceTextView = itemView.findViewById(R.id.tv_economy_seat_price);
            bookFirstClassButton = itemView.findViewById(R.id.btn_book_first_class);
            bookEconomyButton = itemView.findViewById(R.id.btn_book_economy);
        }
    }

}
