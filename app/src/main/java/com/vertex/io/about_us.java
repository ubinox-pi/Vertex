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

public class about_us extends AppCompatActivity {

    pop_dialog popDialog;
    TextView about;
    String htmlText;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        reference = FirebaseDatabase.getInstance().getReference("policies").child("about_us");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    htmlText = Objects.requireNonNull(snapshot.getValue()).toString();
                    Spanned spanned = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY);  // Adjust mode as needed
                    about.setText(spanned);
                    dismiss();
                }
                else {
                    dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(about_us.this, "An error occurred while fetching", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });


        popDialog = new pop_dialog(this);
        about = findViewById(R.id.about_us);
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