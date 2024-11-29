package com.example.datvemaybay_dhcndonga.Booking;

public class Booking {
    private String bookedBy;
    private String bookingDate;
    private int totalAmount;

    public Booking(String bookedBy, String bookingDate, int totalAmount) {
        this.bookedBy = bookedBy;
        this.bookingDate = bookingDate;
        this.totalAmount = totalAmount;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public int getTotalAmount() {
        return totalAmount;
    }
}
