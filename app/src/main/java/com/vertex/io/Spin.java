package com.vertex.io;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Spin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_spin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
        ImageView image = findViewById(R.id.Image_spin);
        TextView spin_left = findViewById(R.id.Spin_Left);

        spin.setOnClickListener(v -> {
            if (!spin_left.getText().toString().equals("0")) 
            {
                spinImage();
            }
            else
            {
                Toast.makeText(this, "No Spins Teft", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private ImageView image;
    private Button spinButton;
    private TextView resultText;

    private RotateAnimation getSpinAnimation() {
        float pivotX = 270 / 2f;
        float pivotY = 270 / 2f;
        RotateAnimation animation = new RotateAnimation(0f, 3600f, pivotX, pivotY);
        animation.setDuration(3000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setFillAfter(true);
        return animation;
    }

    public void spinImage() {
        // Set duration and rotation angles
        float duration = 3000f; // milliseconds (3 seconds)
        float fromAngle = 0f;
        float toAngle = 180f;

        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(image, "rotation", fromAngle, toAngle);
        rotateAnim.setDuration((long) duration);
        rotateAnim.setInterpolator(null); // Use linear interpolation for constant speed

        // Stop the animation after the specified duration
        rotateAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                image.setRotation(toAngle); // Ensure final position is 180 degrees
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        rotateAnim.start();
    }
}