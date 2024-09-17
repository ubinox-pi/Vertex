package com.vertex.io;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class refer extends AppCompatActivity {
    private TextView refer;
    private Button generate;
    private TextView points;
    private Toolbar appBar;
    private String UID;
    public pop_dialog popDialog;
    FirebaseAuth firebaseAuth;
    DatabaseReference pointReference;
    DatabaseReference reference;
    DatabaseReference refers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_refer);
        loading();

        refer = findViewById(R.id.refer_id);
        generate = findViewById(R.id.refer_generate);
        points = findViewById(R.id.point);

        firebaseAuth = FirebaseAuth.getInstance();
        UID = firebaseAuth.getCurrentUser().getUid().toString();
        pointReference = FirebaseDatabase.getInstance().getReference("Users").child(UID).child("coin");
        reference = FirebaseDatabase.getInstance().getReference("Users").child(UID).child("ReferId");
        refers = FirebaseDatabase.getInstance().getReference("refers");

        appBar = findViewById(R.id.app_bar);
        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        appBar.setNavigationOnClickListener(v->{
            finish();
        });

        refer.setOnClickListener(v->{
            ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", refer.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(refer.this,"Copied to clipboard",Toast.LENGTH_SHORT).show();
        });

        pointReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double coin = snapshot.getValue(Double.class);
                points.setText(String.valueOf(coin));
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            refer.setText(snapshot.getValue(String.class));
                            loading_cancel();
                        }
                        else {
                            loading_cancel();
                            generate.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(refer.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(refer.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        generate.setOnClickListener(v->{
            loading();
            Toast.makeText(refer.this,"Generating refer ID",Toast.LENGTH_SHORT).show();
            String ref_id = generate_ref_id();
            reference.setValue(ref_id).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                {
                    refers.child(ref_id).child("UID").setValue(UID).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()){
                            Toast.makeText(refer.this,"Refer ID Generated",Toast.LENGTH_SHORT).show();
                            loading_cancel();
                            refer.setText(ref_id);
                            generate.setVisibility(View.GONE);
                        }
                    });
                }
            });
        });


    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void loading(){
        popDialog.setCancelable(false);
        Objects.requireNonNull(popDialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.loading_popup));
        popDialog.getWindow().setWindowAnimations(R.style.popupAnimation);
        popDialog.getWindow().setGravity(Gravity.CENTER);
        popDialog.create();
        popDialog.show();
    }
    public void loading_cancel(){
        popDialog.cancel();
    }
    String generate_ref_id()
    {
        Random random = new Random();
        String uid = UID.substring(0,3);
        String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(3);
        for (int i = 0; i < 3; i++) {
            sb.append(alphabets.charAt(random.nextInt(alphabets.length())));
        }
        Date today = new Date();
        String date = String.valueOf(today).substring(0,6);
        return uid+date+sb.toString().toUpperCase();
    }
}