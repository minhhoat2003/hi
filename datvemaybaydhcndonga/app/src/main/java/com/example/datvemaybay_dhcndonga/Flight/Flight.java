package com.example.datvemaybay_dhcndonga.Flight;

public class Flight {
    private String flightCode;
    private String departureTime;
    private String arrivalTime;
    private String departureLocation;
    private String arrivalLocation;
    private int totalFirstClassSeats;
    private int availableFirstClassSeats;
    private int firstClassSeatPrice;
    private int totalEconomySeats;
    private int availableEconomySeats;
    private int economySeatPrice;

    public Flight(String flightCode, String departureTime, String arrivalTime,
                  String departureLocation, String arrivalLocation,
                  int totalFirstClassSeats, int availableFirstClassSeats,
                  int firstClassSeatPrice, int totalEconomySeats,
                  int availableEconomySeats, int economySeatPrice) {
        this.flightCode = flightCode;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.totalFirstClassSeats = totalFirstClassSeats;
        this.availableFirstClassSeats = availableFirstClassSeats;
        this.firstClassSeatPrice = firstClassSeatPrice;
        this.totalEconomySeats = totalEconomySeats;
        this.availableEconomySeats = availableEconomySeats;
        this.economySeatPrice = economySeatPrice;
    }

    // Các phương thức getter
    public String getFlightCode() { return flightCode; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public String getDepartureLocation() { return departureLocation; }
    public String getArrivalLocation() { return arrivalLocation; }
    public int getTotalFirstClassSeats() { return totalFirstClassSeats; }
    public int getAvailableFirstClassSeats() { return availableFirstClassSeats; }
    public int getFirstClassSeatPrice() { return firstClassSeatPrice; }
    public int getTotalEconomySeats() { return totalEconomySeats; }
    public int getAvailableEconomySeats() { return availableEconomySeats; }
    public int getEconomySeatPrice() { return economySeatPrice; }

    public void setFlightCode(String flightCode) {
        this.flightCode = flightCode;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public void setArrivalLocation(String arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
    }

    public void setTotalFirstClassSeats(int totalFirstClassSeats) {
        this.totalFirstClassSeats = totalFirstClassSeats;
    }

    public void setAvailableFirstClassSeats(int availableFirstClassSeats) {
        this.availableFirstClassSeats = availableFirstClassSeats;
    }

    public void setFirstClassSeatPrice(int firstClassSeatPrice) {
        this.firstClassSeatPrice = firstClassSeatPrice;
    }

    public void setTotalEconomySeats(int totalEconomySeats) {
        this.totalEconomySeats = totalEconomySeats;
    }

    public void setAvailableEconomySeats(int availableEconomySeats) {
        this.availableEconomySeats = availableEconomySeats;
    }

    public void setEconomySeatPrice(int economySeatPrice) {
        this.economySeatPrice = economySeatPrice;
    }
}
