package com.example.datvemaybay_dhcndonga;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class QRCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        // Ánh xạ các view
        TextView qrInfoTextView = findViewById(R.id.tv_qr_info);
        ImageView qrImageView = findViewById(R.id.qr_image_view);

        // Nhận URL từ Intent
        String imageUrl = getIntent().getStringExtra("qrUrl");
        String flightInfo = getIntent().getStringExtra("flightInfo");
        Log.d("QRCodeActivity", "Image URL: " + imageUrl);

        // Cập nhật thông tin
        qrInfoTextView.setText("Thanh toán cho chuyến bay: " + flightInfo);
        Picasso.get().load(imageUrl)
                .placeholder(R.drawable.loadinggif)
                .into(qrImageView);

    }
}
