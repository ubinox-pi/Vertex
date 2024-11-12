package com.vertex.io;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class NewTicket extends AppCompatActivity {

    EditText subject, description;
    Button submit;
    DatabaseReference ticketsRef, userTicketRef;
    String UID, ticketID, date, status;
    Date dates = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ticket);
        subject = findViewById(R.id.subjectInput);
        description = findViewById(R.id.descriptionInput);
        submit = findViewById(R.id.submitButton);

        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ticketsRef = FirebaseDatabase.getInstance().getReference().child("Tickets");
        userTicketRef = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("MyTickets");
        submit.setOnClickListener(v -> {
            if (subject.getText().toString().isEmpty() || description.getText().toString().isEmpty()) {
                subject.setError("Subject is required");
                description.setError("Description is required");
            } else if(subject.getText().toString().length() < 5){
                subject.setError("Subject must be at least 5 characters");
            } else if(description.getText().toString().length() < 10){
                description.setError("Description must be at least 10 characters");
            } else {
                ticketID = ticketsRef.push().getKey();
                date = dates.toString();
                status = "Open";
                Tickets tickets = new Tickets(ticketID, date, status, subject.getText().toString(), description.getText().toString());
                ticketsRef.child(ticketID).setValue(tickets);
                ticketsRef.child(ticketID).child("UID").setValue(UID);
                userTicketRef.child(ticketID).setValue(tickets);
                finish();
            }
        });

    }
}