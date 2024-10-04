package com.vertex.io;

import android.os.Bundle;
import android.text.Spanned;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class privacy_policy extends AppCompatActivity {

    pop_dialog popDialog;
    TextView privacy;
    String htmlText;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        reference = FirebaseDatabase.getInstance().getReference("policies").child("privacy_policy");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    htmlText = Objects.requireNonNull(snapshot.getValue()).toString();
                    Spanned spanned = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY);  // Adjust mode as needed
                    privacy.setText(spanned);
                    dismiss();
                }
                else {
                    dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(privacy_policy.this, "An error occurred while fetching", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        popDialog = new pop_dialog(this);
        privacy = findViewById(R.id.privacy_policy);
        loading();

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    public void loading(){
        popDialog.setCancelable(false);
        Objects.requireNonNull(popDialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.loading_popup));
        popDialog.getWindow().setWindowAnimations(R.style.popupAnimation);
        popDialog.getWindow().setGravity(Gravity.CENTER);
        popDialog.create();
        popDialog.show();
    }
    public void dismiss(){
        popDialog.dismiss();
    }
}