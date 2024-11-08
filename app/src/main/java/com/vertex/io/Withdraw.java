package com.vertex.io;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.Math;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Withdraw extends AppCompatActivity {

    pop_dialog popDialog;
    long count;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        popDialog = new pop_dialog(Withdraw.this);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.appbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(Withdraw.this,R.drawable.arrow_back));
        toolbar.setNavigationOnClickListener(v->{
            finish();
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        String UID = mAuth.getCurrentUser().getUid().toString();
        DatabaseReference Point = DB.getReference("Users").child(UID).child("coin");
        TextView coin_label = findViewById(R.id.point);


        Point.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double coin = snapshot.getValue(Double.class);
                coin_label.setText(String.valueOf(coin));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        List<withdraw_class> withdrawals;
        withdrawal_list withdrawalListAdapter;

        ListView withdrawallist = findViewById(R.id.pendingWithdrawalsListView);

        withdrawals = new ArrayList<>();
        withdrawalListAdapter = new withdrawal_list(this, withdrawals);

        ListAdapter withdraw_class = new withdrawal_list(this, withdrawals);
        withdrawallist.setAdapter(withdraw_class);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(UID).child("withdrawals");


        DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference(UID).child("withdrwals");
        databaseReference.addValueEventListener(new ValueEventListener() {

            TextView his = findViewById(R.id.NoHistory);
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    withdrawals.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        withdraw_class withdrawal = snapshot.getValue(withdraw_class.class);
                        withdrawals.add(withdrawal);
                    }
//                    withdrawal_list.notifyDataSetChanged();
                    his.setVisibility(View.GONE);
                    withdrawallist.setVisibility(View.VISIBLE);
                }
                else
                {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Withdraw.this, "Failed to load withdrawals.", Toast.LENGTH_SHORT).show();
            }
        });



        Button with = findViewById(R.id.withdrawButton);
        EditText upi = findViewById(R.id.withdrawId);
        EditText amount = findViewById(R.id.withdrawAmount);
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users").child(UID).child("coin");
        DatabaseReference User_withdrawal = FirebaseDatabase.getInstance().getReference("Users").child(UID).child("withdrawals");
        DatabaseReference withdrawal_all = FirebaseDatabase.getInstance().getReference("All_withdrawals");

        with.setOnClickListener(v ->{
            if (!upi.getText().toString().isEmpty() && upi.getText().toString().length() <= 20 && upi.getText().toString().contains("@"))
            {
                if (!amount.getText().toString().isEmpty())
                {
                    loading();
                    double i = Double.parseDouble(amount.getText().toString());
                    if (i >= 100)
                    {
                        user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                double coin = snapshot.getValue(Double.class);
                                Date today = new Date();
                                String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(today);
                                String Txn_id = generateRandomId();
                                if (coin >= Double.parseDouble(amount.getText().toString()))
                                {
                                    with.setEnabled(false);
                                    with.setBackgroundResource(R.drawable.round_btn);
                                    String d = amount.getText().toString();
                                    double c =  Double.parseDouble(d);
                                    double b = coin - c;
                                    user.setValue(b).addOnCompleteListener(task -> {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(Withdraw.this, "Wait Withdrawal is in progress", Toast.LENGTH_LONG).show();
                                            HashMap<String, Object> transactionDetails = new HashMap<>();
                                            transactionDetails.put("id", UID);
                                            transactionDetails.put("upiId", upi.getText().toString().trim());
                                            transactionDetails.put("amount", amount.getText().toString().toString());
                                            transactionDetails.put("txnId", Txn_id);
                                            transactionDetails.put("date", todayStr);
                                            transactionDetails.put("status", "pending");
                                            User_withdrawal.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists())
                                                    {
                                                        count = snapshot.getChildrenCount();
                                                    }
                                                    else {
                                                        count = 1;
                                                    }
                                                    User_withdrawal.child(String.valueOf(count)).setValue(transactionDetails).addOnCompleteListener(task2 ->{
                                                        if (task2.isSuccessful())
                                                        {
                                                            withdrawal_all.child(Txn_id).setValue(transactionDetails).addOnCompleteListener(task1 -> {
                                                                if (task1.isSuccessful())
                                                                {
                                                                    transctions transctions = new transctions(Withdraw.this,'-', amount.getText().toString(), "Withdrawal", "Withdrawal Success");
                                                                    loading_cancel();
                                                                    upi.setText("");
                                                                    amount.setText("");
                                                                }
                                                                else
                                                                {
                                                                    loading_cancel();
                                                                    Toast.makeText(Withdraw.this, "Withdrawal Failed", Toast.LENGTH_SHORT).show();
                                                                    with.setEnabled(true);
                                                                    with.setBackgroundResource(R.drawable.round_btn);
                                                                }
                                                            });
                                                        }
                                                        else
                                                        {
                                                            loading_cancel();
                                                            Toast.makeText(Withdraw.this, "Withdrawal Failed", Toast.LENGTH_SHORT).show();
                                                            with.setEnabled(true);
                                                            with.setBackgroundResource(R.drawable.round_btn);
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    loading_cancel();
                                                }
                                            });
                                        }
                                        else
                                        {
                                            loading_cancel();
                                            Toast.makeText(Withdraw.this, "Withdrawal Failed", Toast.LENGTH_SHORT).show();
                                            with.setEnabled(true);
                                            with.setBackgroundResource(R.drawable.round_btn);
                                        }
                                    });
                                }
                                else
                                {
                                    loading_cancel();
                                    Toast.makeText(Withdraw.this, "Insufficient balance", Toast.LENGTH_SHORT).show();
                                    with.setEnabled(true);
                                    with.setBackgroundResource(R.drawable.round_btn);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                loading_cancel();
                            }
                        });
                    }
                    else
                    {
                        loading_cancel();
                        Toast.makeText(Withdraw.this, "Minimum withdrawal amount is 100", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(Withdraw.this, "Enter an amount", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(Withdraw.this, "Invalid UPI ID", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static String generateRandomId() {
        StringBuilder sb = new StringBuilder();

        // Generate random alphabets for the first 5 digits
        for (int i = 0; i < 5; i++) {
            int randomInt = (int) (Math.random() * 26); // 0-25 for letters A-Z
            char randomChar = (char) ('A' + randomInt);
            sb.append(randomChar);
        }

        // Generate random numbers for the next 8 digits
        for (int i = 0; i < 8; i++) {
            int randomInt = (int) (Math.random() * 10); // 0-9 for digits
            sb.append(randomInt);
        }

        // Generate random alphabets for the last 2 digits
        for (int i = 0; i < 2; i++) {
            int randomInt = (int) (Math.random() * 26);
            char randomChar = (char) ('A' + randomInt);
            sb.append(randomChar);
        }

        return sb.toString();
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



