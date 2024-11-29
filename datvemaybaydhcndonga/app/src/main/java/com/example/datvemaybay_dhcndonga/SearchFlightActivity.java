package com.example.datvemaybay_dhcndonga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SearchFlightActivity extends AppCompatActivity {
    private Button searchButton;
    private DatePicker datePicker;
    private RadioGroup rgClassType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flight);

        searchButton = findViewById(R.id.btn_search);
        datePicker = findViewById(R.id.date_picker);
        rgClassType = findViewById(R.id.rg_class_type);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy ngày tháng từ DatePicker
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                String selectedDate = year + "-" + (month + 1) + "-" + day;

                // Lấy iduser đã được truyền từ UserMainActivity
                int iduser = getIntent().getIntExtra("iduser", -1);

                // Kiểm tra xem có radio button nào được chọn không
                int selectedClassId = rgClassType.getCheckedRadioButtonId();
                if (selectedClassId == -1) {
                    Toast.makeText(SearchFlightActivity.this, "Chọn hạng ghế trước khi tìm kiếm", Toast.LENGTH_SHORT).show();
                } else {
                    String classType = "economy"; // Mặc định là economy class
                    if (selectedClassId == R.id.rb_first_class) {
                        classType = "first_class";
                    }

                    // Chuyển đến DisplayFlightActivity với thông tin tìm kiếm và iduser
                    Intent intent = new Intent(SearchFlightActivity.this, DisplayFlightActivity.class);
                    intent.putExtra("selectedDate", selectedDate);
                    intent.putExtra("classType", classType);
                    intent.putExtra("iduser", iduser);  // Truyền iduser sang DisplayFlightActivity
                    startActivity(intent);
                }
            }
        });
    }
}
