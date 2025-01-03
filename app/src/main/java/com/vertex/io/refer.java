package com.vertex.io;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class refer extends AppCompatActivity {
    private TextView refer;
    private Button generate;
    private TextView points;
    private androidx.appcompat.widget.Toolbar appBar;
    private String UID;
    public pop_dialog popDialog;
    FirebaseAuth firebaseAuth;
    DatabaseReference pointReference;
    DatabaseReference reference;
    DatabaseReference refers;
    Button share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
        popDialog = new pop_dialog(refer.this);
        loading();
        share = findViewById(R.id.share);

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
                            share.setVisibility(View.VISIBLE);
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

        share.setOnClickListener(v->{
            String shareBody = "Hey, I am using this app and I am earning money. You can also earn money by using this app. Use my refer code "+refer.getText().toString()+" to get Rs5 real cash. Download the app from the link below\n\nhttps://play.google.com/store/apps/details?id="+getPackageName();
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Earn Money");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
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
        String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(3);
        for (int i = 0; i < 3; i++) {
            sb.append(alphabets.charAt(random.nextInt(alphabets.length())));
        }
        Date today = new Date();
        String date = new SimpleDateFormat("ddMMyy").format(today);
        return uid+date+sb.toString().toUpperCase().trim();
    }
}