package com.vertex.io;

import android.os.Bundle;
import android.text.Spanned;
import android.view.Gravity;
import android.webkit.WebView;
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

public class terms_and_conditions extends AppCompatActivity {

    pop_dialog popDialog;
    TextView terms;
    String htmlText;
    DatabaseReference reference;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        webView = findViewById(R.id.webView);

        reference = FirebaseDatabase.getInstance().getReference("policies").child("terms_and_conditions");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    htmlText = Objects.requireNonNull(snapshot.getValue()).toString();
                    Spanned spanned = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY);  // Adjust mode as needed
                    terms.setText(spanned);
                    dismiss();
                }
                else {
                    dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(terms_and_conditions.this, "An error occurred while fetching", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });


        popDialog = new pop_dialog(this);
        terms = findViewById(R.id.terms_and_conditions);
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