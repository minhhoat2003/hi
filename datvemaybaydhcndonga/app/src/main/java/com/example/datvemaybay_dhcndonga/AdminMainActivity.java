package com.example.datvemaybay_dhcndonga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminMainActivity extends AppCompatActivity {
    private Button viewCustomerInfoButton, manageFlightsButton, viewStatisticsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        viewCustomerInfoButton = findViewById(R.id.viewCustomerInfoButton);
        manageFlightsButton = findViewById(R.id.manageFlightsButton);
        viewStatisticsButton = findViewById(R.id.viewStatisticsButton);

        viewCustomerInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, AdminViewUserActivity.class);
                startActivity(intent);
            }
        });

        manageFlightsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, AdminFlightManagement.class);
                startActivity(intent);
            }
        });

        viewStatisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, AdminFlightBookingActivity.class);
                // Lấy iduser từ Intent đã nhận trong UserMainActivity
                int iduser = getIntent().getIntExtra("iduser", -1);

                // Truyền iduser sang SearchFlightActivity
                intent.putExtra("iduser", iduser);
                startActivity(intent);
            }
        });

        Button logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}