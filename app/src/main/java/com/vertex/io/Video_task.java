package com.vertex.io;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Video_task extends AppCompatActivity {

    private RewardedAd rewardedAd;
    private Context context = this;

    public void adRewd(){
        Toast.makeText(context,"Ad Is Loading Please Wait",Toast.LENGTH_SHORT).show();
        AdRequest req = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                req, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String Uid = mAuth.getCurrentUser().getUid().toString();
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DatabaseReference Data = FirebaseDatabase.getInstance().getReference("Users").child(Uid).child("videoTask");

        Date today = new Date();
        String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(today);
        Data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Date = snapshot.child("Videodate").getValue(String.class);
                if (!todayStr.equals(Date))
                {
                    Data.child("v1").setValue("not");
                    Data.child("v2").setValue("not");
                    Data.child("v3").setValue("not");
                    Data.child("v4").setValue("not");
                    Data.child("v5").setValue("not");
                    Data.child("v6").setValue("not");
                    Data.child("v7").setValue("not");
                    Data.child("v8").setValue("not");
                    Data.child("v9").setValue("not");
                    Data.child("v10").setValue("not");
                    Data.child("vf").setValue("not");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_task);

        displayPoints();


        androidx.appcompat.widget.Toolbar actionBar = findViewById(R.id.action);
        actionBar.setNavigationIcon(ContextCompat.getDrawable(Video_task.this,R.drawable.arrow_back));
        actionBar.setNavigationOnClickListener(v -> finish());


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String Uid = mAuth.getCurrentUser().getUid().toString();
        DatabaseReference Data = FirebaseDatabase.getInstance().getReference("Users").child(Uid).child("videoTask");


        Button Task1 = findViewById(R.id.Task1);
        Button Task2 = findViewById(R.id.task2);
        Button Task3 = findViewById(R.id.Task3);
        Button Task4 = findViewById(R.id.task4);
        Button Task5 = findViewById(R.id.Task5);
        Button Task6 = findViewById(R.id.task6);
        Button Task7 = findViewById(R.id.Task7);
        Button Task8 = findViewById(R.id.task8);
        Button Task9 = findViewById(R.id.Task9);
        Button Task10 = findViewById(R.id.task10);
        Button TaskF = findViewById(R.id.Final);
        Activity context = this;
        Activity activity = this;

        Task1.setOnClickListener(v -> {
            Toast.makeText(context,"Please Wait",Toast.LENGTH_SHORT).show();
            Data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String a = snapshot.child("v1").getValue(String.class);
                    if (Objects.equals(a, "not"))
                    {
                        adRewd();
                        if (rewardedAd != null) {
                            rewardedAd.show(context, rewardItem -> {
                                Data.child("v1").setValue("1");
                                Toast.makeText(context, "Task Completed", Toast.LENGTH_LONG).show();
                            });
                        }
                        else {
                            Toast.makeText(context, "Ad not available yet. Please try again later.", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {

                        Toast.makeText(context,"Task 1 Is Already Completed",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });


        Task2.setOnClickListener(v -> {
            Toast.makeText(context,"Please Wait",Toast.LENGTH_SHORT).show();
            Data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String a = snapshot.child("v1").getValue(String.class);
                    String b = snapshot.child("v2").getValue(String.class);
                    if (Objects.equals(a, "1"))
                    {
                        if (Objects.equals(b, "not"))
                        {
                            adRewd();
                            if (rewardedAd != null) {
                                rewardedAd.show(context, rewardItem -> {
                                    Data.child("v2").setValue("1");
                                    Toast.makeText(context, "Task Completed", Toast.LENGTH_LONG).show();
                                });
                            } else {
                                Toast.makeText(context, "Ad not available yet. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(context,"Task 2 Is Already Completed",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {

                        Toast.makeText(context,"Complete Task 1 First",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });


        Task3.setOnClickListener(v -> {
            Toast.makeText(context,"Please Wait",Toast.LENGTH_SHORT).show();
            Data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String a = snapshot.child("v1").getValue(String.class);
                    String b = snapshot.child("v2").getValue(String.class);
                    String c = snapshot.child("v3").getValue(String.class);
                    if (Objects.equals(a, "1"))
                    {
                        if (Objects.equals(b, "1"))
                        {
                            if (Objects.equals(c, "not"))
                            {
                                adRewd();
                                if (rewardedAd != null) {
                                    rewardedAd.show(context, rewardItem -> {
                                        Data.child("v3").setValue("1");
                                        Toast.makeText(context, "Task Completed", Toast.LENGTH_LONG).show();
                                    });
                            }
                                else
                                {
                                    Toast.makeText(context, "Ad not available yet. Please try again later.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(context,"Task 3 Is Completed",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(context,"Complete Task 2 First",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {

                        Toast.makeText(context,"Complete Task 1 First",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        Task4.setOnClickListener(v -> {
            Toast.makeText(context,"Please Wait",Toast.LENGTH_SHORT).show();
            Data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String a = snapshot.child("v1").getValue(String.class);
                    String b = snapshot.child("v2").getValue(String.class);
                    String c = snapshot.child("v3").getValue(String.class);
                    String d = snapshot.child("v4").getValue(String.class);
                    if (Objects.equals(a, "1"))
                    {
                        if (Objects.equals(b, "1"))
                        {
                            if (Objects.equals(c, "1"))
                            {
                                if (Objects.equals(d, "not"))
                                {
                                    adRewd();
                                    if (rewardedAd != null) {
                                        rewardedAd.show(context, rewardItem -> {
                                            Data.child("v4").setValue("1");
                                            Toast.makeText(context, "Task Completed", Toast.LENGTH_LONG).show();
                                        });
                                    }
                                    else
                                    {
                                        Toast.makeText(context, "Ad not available yet. Please try again later.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(context,"Task 4 Is Completed",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(context,"Complete Task 3 First",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(context,"Complete Task 2 First",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {

                        Toast.makeText(context,"Complete Task 1 First",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });



        Task5.setOnClickListener(v -> {
            Toast.makeText(context,"Please Wait",Toast.LENGTH_SHORT).show();
            Data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String a = snapshot.child("v1").getValue(String.class);
                    String b = snapshot.child("v2").getValue(String.class);
                    String c = snapshot.child("v3").getValue(String.class);
                    String d = snapshot.child("v4").getValue(String.class);
                    String e = snapshot.child("v5").getValue(String.class);
                    if (Objects.equals(a, "1"))
                    {
                        if (Objects.equals(b, "1"))
                        {
                            if (Objects.equals(c, "1"))
                            {
                                if (Objects.equals(d, "1"))
                                {
                                    if (Objects.equals(e, "not"))
                                    {
                                        adRewd();
                                        if (rewardedAd != null) {
                                            rewardedAd.show(context, rewardItem -> {
                                                Data.child("v5").setValue("1");
                                                Toast.makeText(context, "Task Completed", Toast.LENGTH_LONG).show();
                                            });
                                        }
                                        else
                                        {
                                            Toast.makeText(context, "Ad not available yet. Please try again later.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(context,"Task 5 Is Completed",Toast.LENGTH_SHORT).show();

                                    }
                                }
                                else {
                                    Toast.makeText(activity, "Complete task 4 First", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(context,"Complete Task 3 First",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(context,"Complete Task 2 First",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {

                        Toast.makeText(context,"Complete Task 1 First",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });


        Task6.setOnClickListener(v -> {
            Toast.makeText(context,"Please Wait",Toast.LENGTH_SHORT).show();
            Data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String a = snapshot.child("v1").getValue(String.class);
                    String b = snapshot.child("v2").getValue(String.class);
                    String c = snapshot.child("v3").getValue(String.class);
                    String d = snapshot.child("v4").getValue(String.class);
                    String e = snapshot.child("v5").getValue(String.class);
                    String f = snapshot.child("v6").getValue(String.class);
                    if (Objects.equals(a, "1"))
                    {
                        if (Objects.equals(b, "1"))
                        {
                            if (Objects.equals(c, "1"))
                            {
                                if (Objects.equals(d, "1"))
                                {
                                    if (Objects.equals(e, "1"))
                                    {
                                        if (Objects.equals(f, "not"))
                                        {
                                            adRewd();
                                            if (rewardedAd != null) {
                                                rewardedAd.show(context, rewardItem -> {
                                                    Data.child("v6").setValue("1");
                                                    Toast.makeText(context, "Task Completed", Toast.LENGTH_LONG).show();
                                                });
                                            }
                                            else
                                            {
                                                Toast.makeText(context, "Ad not available yet. Please try again later.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(context, "Task 6 Is Completed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(context,"Complete Task 5 First",Toast.LENGTH_SHORT).show();

                                    }
                                }
                                else {
                                    Toast.makeText(activity, "Complete task 4 First", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(context,"Complete Task 3 First",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(context,"Complete Task 2 First",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {

                        Toast.makeText(context,"Complete Task 1 First",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });


        Task7.setOnClickListener(v -> {
            Toast.makeText(context,"Please Wait",Toast.LENGTH_SHORT).show();
            Data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String a = snapshot.child("v1").getValue(String.class);
                    String b = snapshot.child("v2").getValue(String.class);
                    String c = snapshot.child("v3").getValue(String.class);
                    String d = snapshot.child("v4").getValue(String.class);
                    String e = snapshot.child("v5").getValue(String.class);
                    String f = snapshot.child("v6").getValue(String.class);
                    String g = snapshot.child("v7").getValue(String.class);
                    if (Objects.equals(a, "1"))
                    {
                        if (Objects.equals(b, "1"))
                        {
                            if (Objects.equals(c, "1"))
                            {
                                if (Objects.equals(d, "1"))
                                {
                                    if (Objects.equals(e, "1"))
                                    {
                                        if (Objects.equals(f, "1"))
                                        {
                                            if (Objects.equals(g, "not"))
                                            {
                                                adRewd();
                                                if (rewardedAd != null) {
                                                    rewardedAd.show(context, rewardItem -> {
                                                        Data.child("v7").setValue("1");
                                                        Toast.makeText(context, "Task Completed", Toast.LENGTH_LONG).show();
                                                    });
                                                }
                                                else
                                                {
                                                    Toast.makeText(context, "Ad not available yet. Please try again later.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else
                                            {
                                                Toast.makeText(context, "Task 7 Is Completed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(context, "Complete Task 6 First", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(context,"Complete Task 5 First",Toast.LENGTH_SHORT).show();

                                    }
                                }
                                else {
                                    Toast.makeText(activity, "Complete task 4 First", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(context,"Complete Task 3 First",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(context,"Complete Task 2 First",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {

                        Toast.makeText(context,"Complete Task 1 First",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });



        Task8.setOnClickListener(v -> {
            Toast.makeText(context,"Please Wait",Toast.LENGTH_SHORT).show();
            Data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String a = snapshot.child("v1").getValue(String.class);
                    String b = snapshot.child("v2").getValue(String.class);
                    String c = snapshot.child("v3").getValue(String.class);
                    String d = snapshot.child("v4").getValue(String.class);
                    String e = snapshot.child("v5").getValue(String.class);
                    String f = snapshot.child("v6").getValue(String.class);
                    String g = snapshot.child("v7").getValue(String.class);
                    String h = snapshot.child("v8").getValue(String.class);
                    if (Objects.equals(a, "1"))
                    {
                        if (Objects.equals(b, "1"))
                        {
                            if (Objects.equals(c, "1"))
                            {
                                if (Objects.equals(d, "1"))
                                {
                                    if (Objects.equals(e, "1"))
                                    {
                                        if (Objects.equals(f, "1"))
                                        {
                                            if (Objects.equals(g, "1"))
                                            {
                                                if (Objects.equals(h, "not"))
                                                {
                                                    adRewd();
                                                    if (rewardedAd != null) {
                                                        rewardedAd.show(context, rewardItem -> {
                                                            Data.child("v8").setValue("1");
                                                            Toast.makeText(context, "Task Completed", Toast.LENGTH_LONG).show();
                                                        });
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(context, "Ad not available yet. Please try again later.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                else
                                                {
                                                    Toast.makeText(activity, "Task 8 Is Completed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else
                                            {
                                                Toast.makeText(context, "Complete Task 7 First", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(context, "Complete Task 6 First", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(context,"Complete Task 5 First",Toast.LENGTH_SHORT).show();

                                    }
                                }
                                else {
                                    Toast.makeText(activity, "Complete task 4 First", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(context,"Complete Task 3 First",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(context,"Complete Task 2 First",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {

                        Toast.makeText(context,"Complete Task 1 First",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });



        Task9.setOnClickListener(v -> {
            Toast.makeText(context,"Please Wait",Toast.LENGTH_SHORT).show();
            Data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String a = snapshot.child("v1").getValue(String.class);
                    String b = snapshot.child("v2").getValue(String.class);
                    String c = snapshot.child("v3").getValue(String.class);
                    String d = snapshot.child("v4").getValue(String.class);
                    String e = snapshot.child("v5").getValue(String.class);
                    String f = snapshot.child("v6").getValue(String.class);
                    String g = snapshot.child("v7").getValue(String.class);
                    String h = snapshot.child("v8").getValue(String.class);
                    String i = snapshot.child("v9").getValue(String.class);
                    if (Objects.equals(a, "1"))
                    {
                        if (Objects.equals(b, "1"))
                        {
                            if (Objects.equals(c, "1"))
                            {
                                if (Objects.equals(d, "1"))
                                {
                                    if (Objects.equals(e, "1"))
                                    {
                                        if (Objects.equals(f, "1"))
                                        {
                                            if (Objects.equals(g, "1"))
                                            {
                                                if (Objects.equals(h, "1"))
                                                {
                                                    if (Objects.equals(i, "not"))
                                                    {
                                                        adRewd();
                                                        if (rewardedAd != null) {
                                                            rewardedAd.show(context, rewardItem -> {
                                                                Data.child("v9").setValue("1");
                                                                Toast.makeText(context, "Task Completed", Toast.LENGTH_LONG).show();
                                                            });
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(context, "Ad not available yet. Please try again later.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(activity, "Task 9 Is Completed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                else
                                                {
                                                    Toast.makeText(activity, "Complete Task 8 First", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else
                                            {
                                                Toast.makeText(context, "Complete Task 7 First", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(context, "Complete Task 6 First", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(context,"Complete Task 5 First",Toast.LENGTH_SHORT).show();

                                    }
                                }
                                else {
                                    Toast.makeText(activity, "Complete task 4 First", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(context,"Complete Task 3 First",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(context,"Complete Task 2 First",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {

                        Toast.makeText(context,"Complete Task 1 First",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });



        Task10.setOnClickListener(v -> {
            Toast.makeText(context,"Please Wait",Toast.LENGTH_SHORT).show();
            Data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String a = snapshot.child("v1").getValue(String.class);
                    String b = snapshot.child("v2").getValue(String.class);
                    String c = snapshot.child("v3").getValue(String.class);
                    String d = snapshot.child("v4").getValue(String.class);
                    String e = snapshot.child("v5").getValue(String.class);
                    String f = snapshot.child("v6").getValue(String.class);
                    String g = snapshot.child("v7").getValue(String.class);
                    String h = snapshot.child("v8").getValue(String.class);
                    String i = snapshot.child("v9").getValue(String.class);
                    String j = snapshot.child("v10").getValue(String.class);
                    if (Objects.equals(a, "1"))
                    {
                        if (Objects.equals(b, "1"))
                        {
                            if (Objects.equals(c, "1"))
                            {
                                if (Objects.equals(d, "1"))
                                {
                                    if (Objects.equals(e, "1"))
                                    {
                                        if (Objects.equals(f, "1"))
                                        {
                                            if (Objects.equals(g, "1"))
                                            {
                                                if (Objects.equals(h, "1"))
                                                {
                                                    if (Objects.equals(i, "1"))
                                                    {
                                                        if (Objects.equals(j, "not"))
                                                        {
                                                            adRewd();
                                                            if (rewardedAd != null) {
                                                                rewardedAd.show(context, rewardItem -> {
                                                                    Data.child("v10").setValue("1");
                                                                    Toast.makeText(context, "Task Completed", Toast.LENGTH_LONG).show();
                                                                });
                                                            }
                                                            else
                                                            {
                                                                Toast.makeText(context, "Ad not available yet. Please try again later.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }else
                                                        {
                                                            Toast.makeText(activity, "Task 10 Is Completed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(activity, "Complete Task 9 First", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                else
                                                {
                                                    Toast.makeText(activity, "Complete Task 8 First", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else
                                            {
                                                Toast.makeText(context, "Complete Task 7 First", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(context, "Complete Task 6 First", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(context,"Complete Task 5 First",Toast.LENGTH_SHORT).show();

                                    }
                                }
                                else {
                                    Toast.makeText(activity, "Complete task 4 First", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(context,"Complete Task 3 First",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(context,"Complete Task 2 First",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {

                        Toast.makeText(context,"Complete Task 1 First",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });



        TaskF.setOnClickListener(v -> {
            Toast.makeText(context,"Please Wait",Toast.LENGTH_SHORT).show();
            Data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String a = snapshot.child("v1").getValue(String.class);
                    String b = snapshot.child("v2").getValue(String.class);
                    String c = snapshot.child("v3").getValue(String.class);
                    String d = snapshot.child("v4").getValue(String.class);
                    String e = snapshot.child("v5").getValue(String.class);
                    String f = snapshot.child("v6").getValue(String.class);
                    String g = snapshot.child("v7").getValue(String.class);
                    String h = snapshot.child("v8").getValue(String.class);
                    String i = snapshot.child("v9").getValue(String.class);
                    String j = snapshot.child("v10").getValue(String.class);
                    String k = snapshot.child("vf").getValue(String.class);
                    if (Objects.equals(a, "1"))
                    {
                        if (Objects.equals(b, "1"))
                        {
                            if (Objects.equals(c, "1"))
                            {
                                if (Objects.equals(d, "1"))
                                {
                                    if (Objects.equals(e, "1"))
                                    {
                                        if (Objects.equals(f, "1"))
                                        {
                                            if (Objects.equals(g, "1"))
                                            {
                                                if (Objects.equals(h, "1"))
                                                {
                                                    if (Objects.equals(i, "1"))
                                                    {
                                                        if (Objects.equals(j, "1"))
                                                        {
                                                            if (Objects.equals(k, "not"))
                                                            {
                                                                adRewd();
                                                                Toast.makeText(context, "Click On This Video To Complete Task", Toast.LENGTH_SHORT).show();
                                                                if (rewardedAd != null) {
                                                                    rewardedAd.show(context, rewardItem -> {
                                                                        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                                                            @Override
                                                                            public void onAdClicked() {
                                                                                Data.child("vf").setValue("1").addOnCompleteListener(task -> {
                                                                                    Date today = new Date();
                                                                                    String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(today);
                                                                                    Data.child("Videodate").setValue(todayStr);
                                                                                    DatabaseReference Users = FirebaseDatabase.getInstance().getReference("Users").child(Uid);
                                                                                    Users.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                            long Coin = snapshot.child("coin").getValue(Long.class);
                                                                                            Users.child("coin").setValue(Coin+4.5).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    Toast.makeText(context, "Task Completed", Toast.LENGTH_LONG).show();
                                                                                                }
                                                                                            });
                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                        }
                                                                                    });
                                                                                });
                                                                                Log.d(TAG, "Ad was clicked.");
                                                                            }

                                                                            @Override
                                                                            public void onAdDismissedFullScreenContent() {
                                                                                // Called when ad is dismissed.
                                                                                // Set the ad reference to null so you don't show the ad a second time.
                                                                                Log.d(TAG, "Ad dismissed fullscreen content.");
                                                                                rewardedAd = null;
                                                                            }

                                                                            @Override
                                                                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                                                                // Called when ad fails to show.
                                                                                Log.e(TAG, "Ad failed to show fullscreen content.");
                                                                                rewardedAd = null;
                                                                            }

                                                                            @Override
                                                                            public void onAdImpression() {
                                                                                // Called when an impression is recorded for an ad.
                                                                                Log.d(TAG, "Ad recorded an impression.");
                                                                            }

                                                                            @Override
                                                                            public void onAdShowedFullScreenContent() {
                                                                                // Called when ad is shown.
                                                                                Log.d(TAG, "Ad showed fullscreen content.");
                                                                            }
                                                                        });
                                                                    });
                                                                }
                                                                else
                                                                {
                                                                    Toast.makeText(context, "Ad not available yet. Please try again later.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                            else
                                                            {
                                                                Toast.makeText(activity, "All Tasks Are Completed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(activity, "Complete Task 10 First", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(activity, "Complete Task 9 First", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                else
                                                {
                                                    Toast.makeText(activity, "Complete Task 8 First", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else
                                            {
                                                Toast.makeText(context, "Complete Task 7 First", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(context, "Complete Task 6 First", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(context,"Complete Task 5 First",Toast.LENGTH_SHORT).show();

                                    }
                                }
                                else {
                                    Toast.makeText(activity, "Complete task 4 First", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(context,"Complete Task 3 First",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(context,"Complete Task 2 First",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {

                        Toast.makeText(context,"Complete Task 1 First",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }

    void displayPoints()
    {
        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid().toString();
        DatabaseReference Point = DB.getReference("Users").child(UID).child("coin");
        TextView point = findViewById(R.id.point);
        Point.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int coin = snapshot.getValue(Integer.class);
                String string = String.valueOf(coin);
                point.setText(String.valueOf(coin));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}