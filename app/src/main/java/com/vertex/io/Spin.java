package com.vertex.io;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import java.util.Date;
import java.util.Objects;
import java.util.Random;


public class Spin extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin);

        displayPoints();
        androidx.appcompat.widget.Toolbar actionBar = findViewById(R.id.action);
        actionBar.setNavigationIcon(ContextCompat.getDrawable(Spin.this,R.drawable.arrow_back));
        actionBar.setNavigationOnClickListener(v -> finish());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid().toString();
        DatabaseReference User = FirebaseDatabase.getInstance().getReference("Users").child(UID);

        TextView Point = findViewById(R.id.Point_textview);

        User.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String coin = snapshot.child("coin").getValue().toString();
                Point.setText(coin);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        User.child("spin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Date today = new Date();
                String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(today);
                TextView spin_left = findViewById(R.id.Spin_Left);
                String spin;
                String date;
                spin = String.valueOf(snapshot.child("spinTask").getValue(long.class));
                date = snapshot.child("date").getValue(String.class);
                spin_left.setText(spin);
                if (!snapshot.exists())
                {
                    User.child("spin").child("spinTask").setValue(10);
                    User.child("spin").child("date").setValue(todayStr);
                }
                 if (!Objects.equals(date, todayStr))
                 {
                     User.child("spin").child("spinTask").setValue(10);
                     User.child("spin").child("date").setValue(todayStr);
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Button spin = findViewById(R.id.Spin);
        ImageView imageView = findViewById(R.id.Image_spin);
        TextView spin_left = findViewById(R.id.Spin_Left);
        Random random = new Random();

        spin.setOnClickListener(v -> {
            if (!spin_left.getText().toString().equals("0")) 
            {
                spin.setEnabled(false);
                spin.setBackgroundResource(R.drawable.round_btn);
                RotateAnimation animation = new RotateAnimation(
                        0f, 1080f, // Starting and ending angles (0 to 360 for full spin)
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(5000); // 5 seconds (in milliseconds)
                animation.setInterpolator(new LinearInterpolator()); // Adjust interpolation (optional)
                animation.setFillAfter(true);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // Optional: Perform actions when animation starts
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ImageView imageView = findViewById(R.id.Image_spin);
                        int[] stopDegrees = {0,45, 90, 135, 180, 225, 270, 315};  // Example degrees (8 sections)
                        float fromDegrees = 0f;
                        float toDegrees = 0f;

                        int randomIndex = (int) (Math.random() * stopDegrees.length);
                        toDegrees = stopDegrees[randomIndex];
                        startSpinAnimation(fromDegrees, toDegrees);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // Not applicable here
                    }
                });
                imageView.startAnimation(animation);

            }
            else
            {
                Toast.makeText(this, "No Spins Left", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void startSpinAnimation(float fromDegrees, float toDegrees) {
        RotateAnimation animation = new RotateAnimation(
                fromDegrees, toDegrees,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        ImageView imageView = findViewById(R.id.Image_spin);
        animation.setDuration(5000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setFillAfter(false);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int finalDegrees = Math.round(imageView.getRotation());
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String UID = mAuth.getCurrentUser().getUid().toString();
                DatabaseReference User = FirebaseDatabase.getInstance().getReference("Users").child(UID);
                if(finalDegrees == 0)
                {
                    User.child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            double amount = snapshot.getValue(double.class);
                            amount = amount+1;
                            User.child("coin").setValue(amount).addOnCompleteListener(task -> {
                                if (task.isSuccessful())
                                {
                                    User.child("spin").child("spinTask").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int spin = snapshot.getValue(int.class);
                                            spin = spin-1;
                                            User.child("spin").child("spinTask").setValue(spin).addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful())
                                                {
                                                    Toast.makeText(Spin.this, "You Won 1 Coin", Toast.LENGTH_SHORT).show();
                                                    Button spin1 = findViewById(R.id.Spin);
                                                    spin1.setEnabled(true);
                                                    spin1.setBackgroundResource(R.drawable.round_btn);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if (finalDegrees == 45)
                {
                    User.child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            double amount = snapshot.getValue(double.class);
                            amount = amount+0;
                            User.child("coin").setValue(amount).addOnCompleteListener(task -> {
                                if (task.isSuccessful())
                                {
                                    User.child("spin").child("spinTask").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int spin = snapshot.getValue(int.class);
                                            spin = spin-1;
                                            User.child("spin").child("spinTask").setValue(spin).addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful())
                                                {
                                                    Toast.makeText(Spin.this, "You Won 0 Coin", Toast.LENGTH_SHORT).show();
                                                    Button spin1 = findViewById(R.id.Spin);
                                                    spin1.setEnabled(true);
                                                    spin1.setBackgroundResource(R.drawable.round_btn);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if (finalDegrees ==90)
                {
                    User.child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            double amount = snapshot.getValue(double.class);
                            amount = amount+2;
                            User.child("coin").setValue(amount).addOnCompleteListener(task -> {
                                if (task.isSuccessful())
                                {
                                    User.child("spin").child("spinTask").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int spin = snapshot.getValue(int.class);
                                            spin = spin-1;
                                            User.child("spin").child("spinTask").setValue(spin).addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful())
                                                {
                                                    Toast.makeText(Spin.this, "You Won 2 Coin", Toast.LENGTH_SHORT).show();
                                                    Button spin1 = findViewById(R.id.Spin);
                                                    spin1.setEnabled(true);
                                                    spin1.setBackgroundResource(R.drawable.round_btn);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if (finalDegrees == 135)
                {
                    User.child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            double amount = snapshot.getValue(double.class);
                            amount = amount+3;
                            User.child("coin").setValue(amount).addOnCompleteListener(task -> {
                                if (task.isSuccessful())
                                {
                                    User.child("spin").child("spinTask").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int spin = snapshot.getValue(int.class);
                                            spin = spin-1;
                                            User.child("spin").child("spinTask").setValue(spin).addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful())
                                                {
                                                    Toast.makeText(Spin.this, "You Won 3 Coin", Toast.LENGTH_SHORT).show();
                                                    Button spin1 = findViewById(R.id.Spin);
                                                    spin1.setEnabled(true);
                                                    spin1.setBackgroundResource(R.drawable.round_btn);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if (finalDegrees == 180)
                {
                    User.child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            double amount = snapshot.getValue(double.class);
                            amount = amount+0;
                            User.child("coin").setValue(amount).addOnCompleteListener(task -> {
                                if (task.isSuccessful())
                                {
                                    User.child("spin").child("spinTask").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int spin = snapshot.getValue(int.class);
                                            spin = spin-1;
                                            User.child("spin").child("spinTask").setValue(spin).addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful())
                                                {
                                                    Toast.makeText(Spin.this, "You Won 0 Coin", Toast.LENGTH_SHORT).show();
                                                    Button spin1 = findViewById(R.id.Spin);
                                                    spin1.setEnabled(true);
                                                    spin1.setBackgroundResource(R.drawable.round_btn);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if (finalDegrees == 225)
                {
                    User.child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            double amount = snapshot.getValue(double.class);
                            amount = amount+2;
                            User.child("coin").setValue(amount).addOnCompleteListener(task -> {
                                if (task.isSuccessful())
                                {
                                    User.child("spin").child("spinTask").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int spin = snapshot.getValue(int.class);
                                            spin = spin-1;
                                            User.child("spin").child("spinTask").setValue(spin).addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful())
                                                {
                                                    Toast.makeText(Spin.this, "You Won 2 Coin", Toast.LENGTH_SHORT).show();
                                                    Button spin1 = findViewById(R.id.Spin);
                                                    spin1.setEnabled(true);
                                                    spin1.setBackgroundResource(R.drawable.round_btn);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if (finalDegrees == 270 )
                {
                    User.child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            double amount = snapshot.getValue(double.class);
                            amount = amount+0;
                            User.child("coin").setValue(amount).addOnCompleteListener(task -> {
                                if (task.isSuccessful())
                                {
                                    User.child("spin").child("spinTask").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int spin = snapshot.getValue(int.class);
                                            spin = spin-1;
                                            User.child("spin").child("spinTask").setValue(spin).addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful())
                                                {
                                                    Toast.makeText(Spin.this, "You Won 0 Coin", Toast.LENGTH_SHORT).show();
                                                    Button spin1 = findViewById(R.id.Spin);
                                                    spin1.setEnabled(true);
                                                    spin1.setBackgroundResource(R.drawable.round_btn);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if (finalDegrees == 315)
                {
                    User.child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            double amount = snapshot.getValue(double.class);
                            amount = amount+1;
                            User.child("coin").setValue(amount).addOnCompleteListener(task -> {
                                if (task.isSuccessful())
                                {
                                    User.child("spin").child("spinTask").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int spin = snapshot.getValue(int.class);
                                            spin = spin-1;
                                            User.child("spin").child("spinTask").setValue(spin).addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful())
                                                {
                                                    Toast.makeText(Spin.this, "You Won 1 Coin", Toast.LENGTH_SHORT).show();
                                                    Button spin1 = findViewById(R.id.Spin);
                                                    spin1.setEnabled(true);
                                                    spin1.setBackgroundResource(R.drawable.round_btn);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else
                {
                    User.child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            double amount = snapshot.getValue(double.class);
                            amount = amount+0;
                            User.child("coin").setValue(amount).addOnCompleteListener(task -> {
                                if (task.isSuccessful())
                                {
                                    User.child("spin").child("spinTask").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int spin = snapshot.getValue(int.class);
                                            spin = spin-1;
                                            User.child("spin").child("spinTask").setValue(spin).addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful())
                                                {
                                                    Toast.makeText(Spin.this, "You Won 0 Coin", Toast.LENGTH_SHORT).show();
                                                    Button spin1 = findViewById(R.id.Spin);
                                                    spin1.setEnabled(true);
                                                    spin1.setBackgroundResource(R.drawable.round_btn);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Not applicable here
            }
        });
        imageView.startAnimation(animation);
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
                Log.e("", "Failed to read value.", error.toException());
            }
        });
    }

}