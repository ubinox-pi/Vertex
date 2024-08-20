package com.vertex.io;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
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
import java.util.Random;

public class Scratch extends AppCompatActivity {

    private RewardedAd rewardedAd;

    private pop_dialog popDialog;
    private TextView rewardTextView, leftTextView;
    private ImageView scratchImageView;

    private float floatx, floaty;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint = new Paint();

    private int intCount = 0;
    private static final int MAX_SCRATCH_COUNT = 150;
    private static final int CIRCLE_RADIUS = 60;
    private static final int OFFSET_Y = 0;
    private boolean hasRewarded = false;
    Context context = this;
    Activity activity = this;

    private void resetScratchImage() {
        intCount = 0;
        bitmap = Bitmap.createBitmap(scratchImageView.getWidth(), scratchImageView.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(0xFFAAAAAA);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        scratchImageView.setImageBitmap(bitmap);
    }

    public void adRewd(){
        AdRequest req = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                req, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        Toast.makeText(context, "Ad not available yet. Please try again later.", Toast.LENGTH_SHORT).show();
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d(TAG, "Ad was loaded.");
                        resetScratchImage();
                        hasRewarded = false;
                        rewardTextView = findViewById(R.id.show);
                        rewardTextView.setVisibility(View.VISIBLE);
                        loading_cancel();
                    }
                });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);

        androidx.appcompat.widget.Toolbar actionBar = findViewById(R.id.action);
        actionBar.setNavigationIcon(ContextCompat.getDrawable(Scratch.this,R.drawable.arrow_back));
        actionBar.setNavigationOnClickListener(v -> finish());

        popDialog = new pop_dialog(context);
        loading();


        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid().toString();
        DatabaseReference scratch = DB.getReference("Users").child(UID).child("scratches");

        rewardTextView = findViewById(R.id.show);
        scratchImageView = findViewById(R.id.scratch_image);
        leftTextView = findViewById(R.id.left);

        scratchImageView.post(() -> {
            bitmap = Bitmap.createBitmap(scratchImageView.getWidth(), scratchImageView.getHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            canvas.drawColor(0xFFAAAAAA);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        });

        scratchImageView.setOnTouchListener((v, event) -> {
            floatx = event.getX();
            floaty = event.getY();

            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                clearScratchCard();
            }
            return true;
        });

        generateRandomNumber();
        Thread thread = new Thread(runnable);
        thread.start();

        Button generate = findViewById(R.id.button1);
        generate.setOnClickListener(v ->{
            loading();
            generateRandomNumber();
        });



        scratch.child("date").addListenerForSingleValueEvent(new ValueEventListener() {
            final Date today = new Date();
            final String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(today);
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists())
                {
                    scratch.child("date").setValue(todayStr);
                    scratch.child("scratch").setValue(10);
                } else if (!snapshot.getValue(String.class).equals(todayStr)) {
                    scratch.child("date").setValue(todayStr);
                    scratch.child("scratch").setValue(10);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG,error.getMessage());
            }
        });
        Context context = this;
        scratch.child("scratch").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                leftTextView.setText(String.valueOf(snapshot.getValue(Integer.class)));
                if (Integer.parseInt(leftTextView.getText().toString())==0)
                {
                    new AlertDialog.Builder(context)
                            .setTitle("Sorry!")
                            .setMessage("You have used all your scratches.")
                            .setPositiveButton("OK", (dialog, which) -> {
                                finish();
                            })
                            .setCancelable(false)
                            .create()
                            .show();;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG,error.getMessage());
            }
        });

        displayPoints();
    }

    private void clearScratchCard() {
        canvas.drawCircle(floatx, floaty - OFFSET_Y, CIRCLE_RADIUS, paint);
        intCount++;

        if (intCount > MAX_SCRATCH_COUNT && !hasRewarded) {
            Button generate = findViewById(R.id.button1);
            generate.setVisibility(View.VISIBLE);
            hasRewarded = true;
            scratchImageView.setImageBitmap(null);
            TextView textView = findViewById(R.id.show);
            String log = textView.getText().toString();
            FirebaseDatabase DB = FirebaseDatabase.getInstance();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String UID = mAuth.getCurrentUser().getUid().toString();
            DatabaseReference Point = DB.getReference("Users").child(UID).child("coin");
            DatabaseReference scratch = DB.getReference("Users").child(UID).child("scratches");
            scratch.child("scratch").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    scratch.child("scratch").setValue(snapshot.getValue(Integer.class)-1).addOnCompleteListener(task ->
                            new AlertDialog.Builder(context)
                            .setTitle("Congratulations!")
                            .setMessage("You have earned " + log + " points.")
                            .setPositiveButton("OK", (dialog, which) -> {
                                if (rewardedAd != null) {
                                    rewardedAd.show(activity, rewardItem -> {
                                        Point.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                                TextView textView1 = findViewById(R.id.show);
                                                double coin= snapshot1.getValue(double.class);
                                                double c1 = Double.parseDouble(textView1.getText().toString());
                                                c1 +=coin;
                                                Point.setValue(c1);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    });
                                }
                                else {
                                    Toast.makeText(context, "Ad not available yet. Please try again later.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setCancelable(false)
                            .create()
                            .show());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            scratchImageView.setImageBitmap(bitmap);
        }
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


    public boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            checkInternetConnection();
            handler.postDelayed(this, 5000);
        }
    };

    private void checkInternetConnection() {
        if (!isConnectedToInternet()) {
            Intent intent = new  Intent(Scratch.this, No_Internet.class);
            finish();
            startActivity(intent);
        }
    }

    public void generateRandomNumber()
    {
        adRewd();
        Random random;
        int[] numbers = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 5, 6, 7, 8, 9, 10};
        int randomIndex = new Random().nextInt(numbers.length);
        int randomNumber = numbers[randomIndex];
        Button generate = findViewById(R.id.button1);
        generate.setVisibility(View.GONE);
        rewardTextView = findViewById(R.id.show);
        rewardTextView.setVisibility(View.GONE);
        rewardTextView.setText(String.valueOf(randomNumber));
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
