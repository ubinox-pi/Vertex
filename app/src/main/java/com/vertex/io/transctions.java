package com.vertex.io;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class transctions {
    private String date;
    private String amount;
    private String type;
    private String id,ID;
    private String time;
    private String Uid;
    private Context context;

    private  pop_dialog popDialog;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    public transctions(Context contex,char sign,String amount,String type,String message) {
//        popDialog = new pop_dialog(contex);
//        loading();
        final Date today = new Date();
        final String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(today);

        mAuth = FirebaseAuth.getInstance();
        this.Uid = mAuth.getCurrentUser().getUid().toString();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(Uid).child("Transactions");

        final Time todayTime = new Time(today.getTime());
        this.context = contex;

        this.amount = sign+amount;
        this.type = type;
        this.date = todayStr;
        this.time = todayTime.toString();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    transctions.this.id = String.valueOf(snapshot.getChildrenCount() + 1);
                }
                else {
                    transctions.this.id = "1";
                }
                transctions.this.ID = mDatabase.push().getKey();
                HashMap<String, String> userMap = new HashMap<>();
                userMap.put("id", transctions.this.ID);
                userMap.put("amount", transctions.this.amount);
                userMap.put("type", transctions.this.type);
                userMap.put("date", transctions.this.date);
                userMap.put("time", transctions.this.time);
                mDatabase.child(id).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(transctions.this.context, message , Toast.LENGTH_SHORT).show();
                            //loading_cancel();
                        }
                        else {
                            Toast.makeText(transctions.this.context, "Failed", Toast.LENGTH_SHORT).show();
                            //loading_cancel();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //loading_cancel();
            }
        });
    }

    public void loading(){
        popDialog.setCancelable(false);
        popDialog.getWindow().setBackgroundDrawable(transctions.this.context.getDrawable(R.drawable.loading_popup));
        popDialog.getWindow().setWindowAnimations(R.style.popupAnimation);
        popDialog.getWindow().setGravity(Gravity.CENTER);
        popDialog.create();
        popDialog.show();
    }

    public void loading_cancel(){
        popDialog.cancel();
    }


}
