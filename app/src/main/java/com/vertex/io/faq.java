package com.vertex.io;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class faq extends AppCompatActivity {

    List<FAQItem> faqList;
    DatabaseReference databaseReference;
    pop_dialog popDialog;

    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        popDialog = new pop_dialog(this);
        loading();
        databaseReference = FirebaseDatabase.getInstance().getReference("FAQ");
        layout = findViewById(R.id.points_layout);
        layout.setVisibility(View.INVISIBLE);

        RecyclerView faqRecyclerView = findViewById(R.id.faqRecyclerView);

        // Sample data
        faqList = new ArrayList<>();
        // Add more questions as per your need
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                faqList.clear();
                for (DataSnapshot x:snapshot.getChildren()){
                    String question = x.child("Q").getValue().toString();
                    String answer = x.child("S").getValue().toString();
                    faqList.add(new FAQItem(question, answer));
                }
                faqRecyclerView.getAdapter().notifyDataSetChanged();
                dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        FAQAdapter faqAdapter = new FAQAdapter(faqList);
        faqRecyclerView.setAdapter(faqAdapter);
        faqRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    void loading(){
        popDialog.setCancelable(false);
        popDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.loading_popup));
        popDialog.getWindow().setWindowAnimations(R.style.popupAnimation);
        popDialog.getWindow().setGravity(Gravity.CENTER);
        popDialog.create();
        popDialog.show();
    }
    void dismiss(){
        popDialog.dismiss();
    }
}