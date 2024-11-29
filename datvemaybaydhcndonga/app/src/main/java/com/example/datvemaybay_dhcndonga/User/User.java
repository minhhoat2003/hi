package com.example.datvemaybay_dhcndonga.User;

public class User {
    private int iduser;
    private String username;
    private String fullname;
    private String birthdate;

    public User(int iduser, String username, String fullname, String birthdate) {
        this.iduser = iduser;
        this.username = username;
        this.fullname = fullname;
        this.birthdate = birthdate;
    }

    public int getIduser() {
        return iduser;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getBirthdate() {
        return birthdate;
    }
}
