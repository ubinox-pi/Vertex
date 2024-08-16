package com.vertex.io;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class actionBar extends com.google.android.material.appbar.AppBarLayout {

    private TextView Points;
    private View navigationIcon;
    private FirebaseAuth mAuth;

    public actionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public actionBar(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.action_app_bar, this, true);
        Points = findViewById(R.id.point);
        navigationIcon = findViewById(R.id.toolbar);
//        navigationIcon.setOnClickListener(v ->  {
//            Toast.makeText(context, "Navigation Icon Clicked", Toast.LENGTH_SHORT).show();
//        });
        

    }
    public void setTitleText(String title) {
        Points = findViewById(R.id.point);
        Points.setText(title);
    }

}