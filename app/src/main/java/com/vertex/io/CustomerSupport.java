package com.vertex.io;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerSupport extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar;
    DatabaseReference reference,pointsReference;
    String UID;
    List<Tickets> ticketList = new ArrayList<>();
    ListView ticketListView;
    TicketsAdapter ticketsAdapter;
    pop_dialog popDialog;
    Button newTicket;
    TextView points;

    // TODO: 12-11-2024 will be add on click new ticket button to check if the user has already created a ticket or not

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_support);
        newTicket = findViewById(R.id.btn_new_ticket);
        popDialog = new pop_dialog(this);
        loading();
        points = findViewById(R.id.point);
        ticketListView = findViewById(R.id.listView);
        ticketsAdapter = new TicketsAdapter(this, R.layout.customer_support_item, ticketList);
        toolbar = findViewById(R.id.action_app_bar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        UID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("MyTickets");



        ticketListView.setAdapter(ticketsAdapter);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ticketList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Tickets tickets = dataSnapshot.getValue(Tickets.class);
                        ticketList.add(tickets);
                    }
                    List<Tickets> reversedList = new ArrayList<>();
                    for (int i = ticketList.size() - 1; i >= 0; i--){
                        reversedList.add(ticketList.get(i));
                    }
                    ticketList = reversedList;
                    ticketsAdapter.notifyDataSetChanged();
                }
                loading_cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerSupport.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                loading_cancel();
            }
        });

        newTicket.setOnClickListener(v->{
            startActivity(new Intent(CustomerSupport.this, NewTicket.class));
            finish();
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        pointsReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("coin");
        pointsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    double point = snapshot.getValue(Double.class);
                    points.setText(String.valueOf(point));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void loading(){
        popDialog.setCancelable(false);
        popDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.loading_popup));
        popDialog.getWindow().setWindowAnimations(R.style.popupAnimation);
        popDialog.getWindow().setGravity(Gravity.CENTER);
        popDialog.create();
        popDialog.show();
    }

    public void loading_cancel(){
        popDialog.cancel();
    }


}