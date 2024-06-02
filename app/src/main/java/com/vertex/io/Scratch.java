package com.vertex.io;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class Scratch extends AppCompatActivity {

    private ScratchCardView scratchCardView;
    private TextView resultTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);

        scratchCardView = findViewById(R.id.scratchCardView);
        resultTextView = findViewById(R.id.resultTextView);

        scratchCardView.setOnTouchListener((v, event) -> {
            if (scratchCardView.isScratched()) {
                Random random = new Random();
                int randomNumber = random.nextInt(10);
                resultTextView.setText("You won Rs"+randomNumber);
            }
            return false;
        });
    }
}