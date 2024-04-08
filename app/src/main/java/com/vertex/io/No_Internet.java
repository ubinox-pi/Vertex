package com.vertex.io;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class No_Internet extends AppCompatActivity {

    private ImageView noInternetImage;
    private TextView noInternetTitle;
    private TextView noInternetMessage;
    private Button noInternetRetryButton;

//    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_no_internet);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });



        noInternetImage = findViewById(R.id.no_internet_image);
        noInternetTitle = findViewById(R.id.no_internet_title);
        noInternetMessage = findViewById(R.id.no_internet_message);
        noInternetRetryButton = findViewById(R.id.no_internet_retry_button);
        
        
        noInternetRetryButton.setOnClickListener(v -> {
            if (isNetworkAvailable())
            {
                finish();
            }
            else {
                Toast.makeText(this, "No Internet Check Again", Toast.LENGTH_SHORT).show();
            }
        });



    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}