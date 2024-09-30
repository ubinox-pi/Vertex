package com.vertex.io;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class information extends AppCompatActivity {

    TextView name, description,whatToDo;
    Button click;
    String id,Name,Description,WhatToDo,Link,LongDescription,Imagelink;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("spacialTask");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        name = findViewById(R.id.App_info);
        description = findViewById(R.id.description);
        whatToDo = findViewById(R.id.whatToDo);
        click = findViewById(R.id.submit);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Name = intent.getStringExtra("name");
        Description = intent.getStringExtra("description");
        WhatToDo = intent.getStringExtra("whatToDo");
        Link = intent.getStringExtra("link");
        LongDescription = intent.getStringExtra("longDescription");
        Imagelink = intent.getStringExtra("imagelink");

        name.setText(Name);
        description.setText(Description);
        whatToDo.setText(WhatToDo);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        click.setOnClickListener(v->{
            reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Toast.makeText(information.this, "You have already enrolled", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        reference.child(id).child("id").setValue(id);
                        reference.child(id).child("name").setValue(Name);
                        reference.child(id).child("description").setValue(Description);
                        reference.child(id).child("whatToDo").setValue(WhatToDo);
                        reference.child(id).child("link").setValue(Link);
                        reference.child(id).child("longDescription").setValue(LongDescription);
                        reference.child(id).child("imagelink").setValue(Imagelink);
                        reference.child(id).child("status").setValue("pending");
                        Toast.makeText(information.this, "Enrolled", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(information.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}