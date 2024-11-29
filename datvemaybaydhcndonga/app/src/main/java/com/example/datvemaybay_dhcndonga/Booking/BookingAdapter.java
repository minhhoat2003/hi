package com.example.datvemaybay_dhcndonga.Booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datvemaybay_dhcndonga.R;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookingList;

    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.tvBookedBy.setText(booking.getBookedBy());
        holder.tvBookingDate.setText(booking.getBookingDate());
        holder.tvTotalAmount.setText("Tổng tiền: " + booking.getTotalAmount() + " VND");
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookedBy, tvBookingDate, tvTotalAmount;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookedBy = itemView.findViewById(R.id.tvBookedBy);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
        }
    }
}
