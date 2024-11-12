package com.vertex.io;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewTicket extends AppCompatActivity {

    pop_dialog popDialog;
    TextView ticketId;
    EditText message;
    Button send;
    ListView messageList;
    String UID;
    String ticketID;
    DatabaseReference reference;
    String count;
    ChatAdapter chatAdapter;
    List<chat> chatList = new ArrayList<>();
    boolean canMessage = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticket);
        popDialog = new pop_dialog(this);
        loading();
        chatAdapter = new ChatAdapter(this, R.layout.tickets_chat, chatList);
        try {
            Intent intent = getIntent();
            ticketId = findViewById(R.id.ticket_id);
            UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            ticketId.setText(intent.getStringExtra("ticketID"));
            ticketID = intent.getStringExtra("ticketID");
            message = findViewById(R.id.messageInput);
            send = findViewById(R.id.btn_send_message);
            messageList = findViewById(R.id.listView);
            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("MyTickets").child(ticketID).child(ticketID).child("Messages");


            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String you = dataSnapshot.child("you").getValue(String.class);
                        String admin = dataSnapshot.child("admin").getValue(String.class);
                        chat chat = new chat(you, admin);
                        chatList.add(chat);
                        if (chat.getYourMessage() != null){
                            chatAdapter = new ChatAdapter(ViewTicket.this, R.layout.tickets_chat, chatList);
                            messageList.setAdapter(chatAdapter);
                        }
                        if(snapshot.exists()){
                            if (chat.getAdminMessage() != null){
                                canMessage = true;
                            }
                            else {
                                canMessage = false;
                            }
                        }

                    }
                    loading_cancel();
                    chatAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            send.setOnClickListener(v -> {
                if (canMessage) {
                        if (message.getText().toString().isEmpty() || message.getText().toString().length() < 10) {
                            message.setError("Message is required");
                        } else {
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        long i = 1 + snapshot.getChildrenCount();
                                        count = String.valueOf(i);
                                    } else {
                                        count = "0";
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            reference.child(count).child("you").setValue(message.getText().toString());
                            message.setText("");
                        }
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Error");
                    builder.setMessage("You can't send a message until an admin replies to your ticket.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", (dialog, which) -> dialog.cancel());
                    builder.create().show();
                }
            });
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage(e.getMessage());
            builder.setCancelable(false);
            builder.setPositiveButton("OK", (dialog, which) -> finish());
            builder.create().show();
        }
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