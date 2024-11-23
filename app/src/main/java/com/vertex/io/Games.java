package com.vertex.io;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Games extends AppCompatActivity {

    ImageView FloppyBird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        FloppyBird = findViewById(R.id.floppy_bird);
        FloppyBird.setOnClickListener(v -> {
            startActivity(new Intent(Games.this, FloppyBirdGame.class));
        });

    }
}