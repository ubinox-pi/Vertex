package com.vertex.io;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Home extends AppCompatActivity {


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private AdView mAdView;
    private RewardedAd rewardedAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Context context2 = this;
        Activity activity = this;


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        TextView UIDi = findViewById(R.id.Uid_textview);
        UIDi.setText(mAuth.getCurrentUser().getUid().toString());

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        TextView Points = findViewById(R.id.Point_textview);
        String UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        DatabaseReference User = FirebaseDatabase.getInstance().getReference("Users").child(UID);
        User.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String points;
                points = String.valueOf(snapshot.child("coin").getValue());
                Points.setText(points);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ImageView profile = findViewById(R.id.Profile_view);
        profile.setOnClickListener(v -> {
            Intent intent =  new Intent(context, Profile.class);
            startActivity(intent);
        });

        DatabaseReference msg = FirebaseDatabase.getInstance().getReference("Message");
        TextView messa = findViewById(R.id.Message_text);
        msg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String m = snapshot.child("msg").getValue(String.class);
                messa.setText(m);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Context context = this;


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog(context);
            }

            private void showExitConfirmationDialog(Context context) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm Exit");
                builder.setMessage("Are you sure you want to exit the app?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();
            }
        });

        ImageView Task = findViewById(R.id.Video_task);
        Task.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this,Video_task.class);
            startActivity(intent);
        });

        TextView Name = findViewById(R.id.Name_textview);
        TextView Gmail = findViewById(R.id.Gmail_textview);
        TextView Phone = findViewById(R.id.phone_text);
        TextView Point = findViewById(R.id.Point_textview);
        TextView Refer = findViewById(R.id.Refer_text);
        User.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name,gmail,phone,points,refer;

                name = String.valueOf(snapshot.child("name").getValue(String.class));
                gmail = String.valueOf(snapshot.child("email").getValue(String.class));
                phone = String.valueOf(snapshot.child("phone").getValue(String.class));
                points = String.valueOf(snapshot.child("coin").getValue());
                refer = String.valueOf(snapshot.child("referBy").getValue());

                Name.setText(name);
                Gmail.setText(gmail);
                Phone.setText(phone);
                Point.setText(points);
                Refer.setText(refer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ImageView Spin = findViewById(R.id.Spin_Image);
        Spin.setOnClickListener(v -> {
            Intent intent = new Intent(this, com.vertex.io.Spin.class);
            startActivity(intent);
        });


        ImageView Daily = findViewById(R.id.Daily_img);
        Date today = new Date();
        String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(today);
        Daily.setOnClickListener(v -> {
            adRewd();
            User.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String date = snapshot.child("date").getValue(String.class);
                    if (date.equals(todayStr)) {
                        Toast.makeText(context, "You Already Checked In Today", Toast.LENGTH_LONG).show();
                    }
                    else {

                        if (rewardedAd != null) {
                            Toast.makeText(context,"Watch Full Ad TO Earn Reward",Toast.LENGTH_LONG).show();
                            rewardedAd.show((Activity) context, new OnUserEarnedRewardListener() {
                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                    long c = (long) snapshot.child("coin").getValue();
                                    User.child("coin").setValue((c + 1));
                                    User.child("date").setValue(todayStr);
                                    Toast.makeText(context, "Daily Check In Successful", Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "You earned the reward.");
                                }
                            });
                        }
                        else
                        {
                            Log.d(TAG, "The rewarded ad wasn't ready yet.");
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        ImageView dev = findViewById(R.id.Contact_dev);
        dev.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/__raj.only__"));
            intent.setPackage("com.instagram.android");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/" + "__raj.only__"));
                startActivity(intent);
            }
        });

    }

    private Context context = this;

    public void adRewd(){
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
    public void onResume() {
        super.onResume();
        mAdView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdView.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdView.destroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}