package com.example.datvemaybay_dhcndonga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UserMainActivity extends AppCompatActivity {

    private TextView customerIdTextView;
    private TextView customerFullName;
    private Button viewPersonalInfoButton, searchFlightsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        viewPersonalInfoButton = findViewById(R.id.viewPersonalInfoButton);
        searchFlightsButton = findViewById(R.id.searchFlightsButton);

        // Find TextView where you'll display the iduser (if needed)
        customerIdTextView = findViewById(R.id.customerIdValue);  // Make sure this ID matches your XML layout
        customerFullName = findViewById(R.id.customerIdLabel);
        // Retrieve the iduser passed from LoginActivity
        int iduser = getIntent().getIntExtra("iduser", -1);  // Default value is -1 in case iduser is not passed
        String fullname = getIntent().getStringExtra("fullname");
        // Display or use the iduser
        if (iduser != -1) {
            customerIdTextView.setText("Mã Khách Hàng: " + iduser);  // Display iduser in a TextView
        }
        customerFullName.setText("Xin chào " + fullname);
        viewPersonalInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainActivity.this, UserInfoActivity.class);
                // Truyền iduser và fullname qua Intent
                intent.putExtra("iduser", iduser);  // Truyền iduser

                // Khởi chạy SearchFlightActivity
                startActivity(intent);
            }
        });
        searchFlightsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainActivity.this, SearchFlightActivity.class);
                // Truyền iduser và fullname qua Intent
                intent.putExtra("iduser", iduser);  // Truyền iduser
                intent.putExtra("fullname", fullname);  // Truyền fullname

                // Khởi chạy SearchFlightActivity
                startActivity(intent);
            }
        });




    }
}
