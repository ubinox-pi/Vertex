package com.vertex.io;

import android.os.Bundle;
import android.os.Handler;
import android.text.Spanned;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.viewbinding.BuildConfig;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Math extends AppCompatActivity {

    private TextView number1TextView;
    private TextView number2TextView;
    private TextView operatorTextView;
    private EditText answerEditText;
    private Button submitButton;
    private AdView adView;
    private InterstitialAd interstitialAd;

    private int number1;
    private int number2;
    private char operator;
    private int correctAnswer;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);
        EditText editText = findViewById(R.id.answerEditText);
        editText.requestFocus();


//        startFunction();
        sec();

        AdView adViewm = findViewById(R.id.adView);
        if (BuildConfig.DEBUG) {
            AdRequest.Builder requestBuilder = new AdRequest.Builder();
            AdRequest request = requestBuilder.build();
            adViewm.loadAd(request);
        } else {
            adViewm.loadAd(new AdRequest.Builder().build());
        }


        TextView textView = findViewById(R.id.Reward_rule);
        String htmlText = "1.Qusetion time will be 10 sec. <br> 2.Reward System for these queestion will be like this mentioned below. <br>3. If you give answer corrctly under limited time you will get <br>1. Under 10 sec -> 25 <br>2. Under 9 sec -> 20 <br>3. Under 8 sec -> 15 <br>4. Under 7 sec -> 10 <br>5. Under 6 sec -> 8 <br>6. Under 5 sec -> 6 <br>7. Under 4 sec -> 4 <br>8. Under 3 sec -> 2 <br>9. Under 2 sec -> 2 <br>10. Under 1 sec -> 2";
        Spanned spanned = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY);  // Adjust mode as needed
        textView.setText(spanned);



        number1TextView = findViewById(R.id.number1TextView);
        number2TextView = findViewById(R.id.number2TextView);
        operatorTextView = findViewById(R.id.operatorTextView);
        answerEditText = findViewById(R.id.answerEditText);
        submitButton = findViewById(R.id.submitButton);
        adView = findViewById(R.id.adView);

        // Initialize the Mobile Ads SDK
        MobileAds.initialize(this, initializationStatus -> {});

        // Load a banner ad
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Load an interstitial ad
        loadInterstitialAd();

        // Generate a new question
        generateNewQuestion();

        submitButton.setOnClickListener(v -> {
            checkAnswer();
        });
    }

    private void generateNewQuestion() {
        Random random = new Random();
        number1 = random.nextInt(10000) + 1;
        number2 = random.nextInt(10000) + 1;
        operator = generateRandomOperator();

        number1TextView.setText(String.valueOf(number1));
        number2TextView.setText(String.valueOf(number2));
        operatorTextView.setText(String.valueOf(operator));

        switch (operator) {
            case '+':
                correctAnswer = number1 + number2;
                break;
            case '-':
                correctAnswer = number1 - number2;
                break;
            case '*':
                correctAnswer = number1 * number2;
                break;
            case '/':
                correctAnswer = number1 / number2;
                break;
        }
    }

    private char generateRandomOperator() {
        char[] operators = {'+', '-', '*', '/'};
        Random random = new Random();
        return operators[random.nextInt(operators.length)];
    }

    private void checkAnswer() {
        String answerStr = answerEditText.getText().toString();
        try {
            int answer = Integer.parseInt(answerStr);
            if (answer == correctAnswer) {
                showInterstitialAd();
                TextView textView = findViewById(R.id.Timer);
                int seconds = Integer.parseInt(textView.getText().toString());
                getReward(seconds);
                EditText editText = findViewById(R.id.answerEditText);
                editText.setText("");
                editText.requestFocus();
                textView.setText("11");
                generateNewQuestion();
            } else {
                EditText editText = findViewById(R.id.answerEditText);
                editText.setText("");
                editText.requestFocus();
                TextView textView = findViewById(R.id.Timer);
                textView.setText("11");
                generateNewQuestion();
                Toast.makeText(this, "Incorrect! Try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            EditText editText = findViewById(R.id.answerEditText);
            editText.setText("");
            editText.requestFocus();
            TextView textView = findViewById(R.id.Timer);
            textView.setText("11");
            generateNewQuestion();
            Toast.makeText(this, "Incorrect! Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd ad) {
                        interstitialAd = ad;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        interstitialAd = null;
                    }
                });
    }

    public void sec() {
        final Handler handler = new Handler(getApplicationContext().getMainLooper());

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    TextView textView = findViewById(R.id.Timer);
                    int seconds = Integer.parseInt(textView.getText().toString());
                    if (seconds > 0) {
                        textView.setText(String.valueOf(seconds - 1));
                    } else {
                        textView.setText("11");
                        generateNewQuestion();
                    }
                });
            }
        }, 1000, 1000);
    }

    private void getReward(int sec)
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase Db = FirebaseDatabase.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        DatabaseReference User = Db.getInstance().getReference("Users").child(uid);
        if (sec <= 3)
        {
            User.child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    double coin = snapshot.getValue(double.class);
                    User.child("coin").setValue(coin + 2).addOnCompleteListener(task -> {
                        transctions transctions = new transctions(Math.this,'+', "2", "Math Task", "You Won 2 Coin");
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(sec == 4)
        {
            User.child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    double coin = snapshot.getValue(double.class);
                    User.child("coin").setValue(coin + 4).addOnCompleteListener(task -> {
                        transctions transctions = new transctions(Math.this,'+', "4", "Math Task", "You Won 4 Coin");
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(sec == 5)
        {
            User.child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    double coin = snapshot.getValue(double.class);
                    User.child("coin").setValue(coin + 6).addOnCompleteListener(task -> {
                        transctions transctions = new transctions(Math.this,'+', "6", "Math Task", "You Won 6 Coin");
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(sec == 6)
        {
            User.child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    double coin = snapshot.getValue(double.class);
                    User.child("coin").setValue(coin + 8).addOnCompleteListener(task -> {
                        transctions transctions = new transctions(Math.this,'+', "8", "Math Task", "You Won 8 Coin");
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(sec == 7)
        {
            User.child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    double coin = snapshot.getValue(double.class);
                    User.child("coin").setValue(coin + 10).addOnCompleteListener(task -> {
                        transctions transctions = new transctions(Math.this,'+', "10", "Math Task", "You Won 10 Coin");
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(sec == 8)
        {
            User.child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    double coin = snapshot.getValue(double.class);
                    User.child("coin").setValue(coin + 15).addOnCompleteListener(task -> {
                        transctions transctions = new transctions(Math.this,'+', "15", "Math Task", "You Won 15 Coin");
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(sec == 9)
        {
            User.child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    double coin = snapshot.getValue(double.class);
                    User.child("coin").setValue(coin + 20).addOnCompleteListener(task -> {
                        transctions transctions = new transctions(Math.this,'+', "20", "Math Task", "You Won 20 Coin");
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(sec == 10)
        {
            User.child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    double coin = snapshot.getValue(double.class);
                    User.child("coin").setValue(coin + 25).addOnCompleteListener(task -> {
                        transctions transctions = new transctions(Math.this,'+', "25", "Math Task", "You Won 25 Coin");
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    private void showInterstitialAd() {
        if (interstitialAd != null) {
            interstitialAd.show(this);
            loadInterstitialAd();
        }
    }
}