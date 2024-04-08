package com.vertex.io;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.edit), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid().toString();
        FirebaseDatabase mDB = FirebaseDatabase.getInstance();
        DatabaseReference Users = FirebaseDatabase.getInstance().getReference("Users").child(UID);

        Context context = this;

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }

        });


        ImageView edit = findViewById(R.id.Edit);
        ImageView edit2 = findViewById(R.id.Edit2);
        ImageView edit3 = findViewById(R.id.Edit3);
        edit.setOnClickListener(v -> {
            Toast.makeText(context, "Contact Customer Care To Edit These", Toast.LENGTH_SHORT).show();
        });

        edit2.setOnClickListener(v -> {
            Toast.makeText(context, "Contact Customer Care To Edit These", Toast.LENGTH_SHORT).show();
        });

        edit3.setOnClickListener(v -> {
            Toast.makeText(context, "Contact Customer Care To Edit These", Toast.LENGTH_SHORT).show();
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid().toString();
        FirebaseDatabase mDB = FirebaseDatabase.getInstance();
        DatabaseReference Users = FirebaseDatabase.getInstance().getReference("Users").child(UID);
        EditText uid = findViewById(R.id.UID);
        EditText name = findViewById(R.id.Name);
        EditText gmail = findViewById(R.id.Gmail);
        EditText phone = findViewById(R.id.Phone);
        uid.setText(mAuth.getCurrentUser().getUid().toString());

        Users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Name,Gmail,Phone;
                Name = snapshot.child("name").getValue(String.class);
                Gmail = snapshot.child("email").getValue(String.class);
                Phone = snapshot.child("phone").getValue(String.class);
                name.setText(Name);
                gmail.setText(Gmail);
                phone.setText("+91"+Phone);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}