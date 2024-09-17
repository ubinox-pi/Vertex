package com.vertex.io;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.otpless.dto.HeadlessRequest;
import com.otpless.dto.HeadlessResponse;
import com.otpless.main.OtplessManager;
import com.otpless.main.OtplessView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    DatabaseReference refer;
    DatabaseReference phoneReference;

    User_update users;

    MainActivity main = this;

    String OTP;

    String Pnumber;
    OtplessView otplessView;

    private Otp otp;
    private pop_dialog popDialog;

    AirtableRecord record;
    AirtableAPI apiService;

    String uids;

    String Email;
    String Password;
    String Phone;
    String ReferBy;
    String Name;
    String Uid;
    String Date;
    Double Coin;
    String Url;
    String Address;

    JSONObject successResponse1,responseWithToken1;

    HeadlessRequest request;

    boolean numberCheck = false;

    Context context;
    String BASE_URL = "https://api.airtable.com/v0/appOOtw5HApriW4K8/";
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Users");
        refer = mFirebaseDatabase.getReference("refers");
        phoneReference = mFirebaseDatabase.getReference("Phones");


        ReferBy = "Admin";
        context = MainActivity.this;
        popDialog = new pop_dialog(context);

        // Initialize Otpless
        otplessView = OtplessManager.getInstance().getOtplessView(this);
        otplessView.initHeadless("9N0B8BKCTZNMZMK8LX5X");
        otplessView.setHeadlessCallback(this::onHeadlessCallback);

        //hideStatusBar();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);

        //these are style for custom progress
//        @style/SpinKitView
//        @style/SpinKitView.Circle
//        @style/SpinKitView.Large
//        @style/SpinKitView.Small
//        @style/SpinKitView.Small.DoubleBounce


        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    LinearLayout layout_r = findViewById(R.id.Register_layout);
                    LinearLayout layout_main = findViewById(R.id.main_layout1);
                    LinearLayout layout_v = findViewById(R.id.verify_layout);
                    FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                    if(mFirebaseUser == null)
                    {
                        layout_main.setVisibility(View.GONE);
                        layout_r.setVisibility(View.VISIBLE);
                    }
                    else if (mFirebaseAuth.getCurrentUser().isEmailVerified()) {
                        Intent intent =  new Intent(MainActivity.this, Home.class);
                        startActivity(intent);
                    }
                    else
                    {
                        layout_main.setVisibility(View.GONE);
                        layout_v.setVisibility(View.VISIBLE);
                    }


                });
            }

        }, 5000);

//        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.app_theme)));
        TextView lo = findViewById(R.id.Lo);
        LinearLayout layout_v = findViewById(R.id.verify_layout);
        LinearLayout layout_l0 = findViewById(R.id.login_layout);
        lo.setOnClickListener(v -> {
            layout_v.setVisibility(View.GONE);
            layout_l0.setVisibility(View.VISIBLE);
        });



        TextView e = findViewById(R.id.email_text_box);
        TextView forget = findViewById(R.id.forgot);
        forget.setOnClickListener(v -> {
            if (e.getText().toString().isEmpty())
            {
                Toast.makeText(MainActivity.this,"Enter Gmail To Forget Password",Toast.LENGTH_SHORT).show();
            }
            else if (!e.getText().toString().contains("@gmail.com"))
            {
                Toast.makeText(MainActivity.this,"Enter Valid Gmail Id",Toast.LENGTH_SHORT).show();
            }
            else
            {
                mFirebaseAuth.sendPasswordResetEmail(e.getText().toString().trim().toLowerCase());
                Toast.makeText(MainActivity.this,"Password Reset Link send To Your Gmail",Toast.LENGTH_LONG).show();
            }
        });


        Activity context = this;


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog(context);
            }

            private void showExitConfirmationDialog(Context context) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("Confirm Exit");
                builder.setMessage("Are you sure you want to exit the app?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity(); // Close all Activities in the task stack
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

        /** REGISTRATION **/

        Button registration = findViewById(R.id.Button_r2);
        registration.setOnClickListener(v -> {
            EditText text_e = findViewById(R.id.email_text_box2);
            EditText text_p = findViewById(R.id.phone_text_box);
            EditText text_ps = findViewById(R.id.password_text_box3);
            EditText text_cps = findViewById(R.id.check_password_text_box);
            EditText name = findViewById(R.id.Full_Name);
            EditText refer = findViewById(R.id.Refer);
            LinearLayout layout_reg = findViewById(R.id.Register_layout);

            if (!name.getText().toString().isEmpty() && name.getText().toString().length()>2 && name.getText().toString().length()<30 && name.getText().toString().contains(" "))
            {
                if (!text_e.getText().toString().isEmpty() && text_e.getText().toString().contains("@gmail.com") && text_e.getText().length()>15)
                {
                    if (!text_p.getText().toString().isEmpty() && text_p.getText().toString().length()==10)
                    {
                        Pnumber = text_p.getText().toString();
                        if (!text_ps.getText().toString().isEmpty() && text_ps.getText().toString().contains("@") && text_ps.getText().length()<20)
                        {
                            if (!text_cps.getText().toString().isEmpty())
                            {
                                if (text_ps.getText().toString().equals(text_cps.getText().toString()))
                                {
                                    DatabaseReference phone = FirebaseDatabase.getInstance().getReference("Phones").child(Pnumber);
                                    phone.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists())
                                            {
                                                Toast.makeText(MainActivity.this,"Phone Number Already Registered",Toast.LENGTH_SHORT).show();
                                                numberCheck = true;
                                            }
                                            else
                                            {
                                                Name = name.getText().toString();
                                                Email = text_e.getText().toString();
                                                Password = text_cps.getText().toString();
                                                Phone = text_p.getText().toString();
                                                java.util.Date today = new Date();
                                                Date = new SimpleDateFormat("yyyy-MM-dd").format(today);
                                                Address = "";
                                                Coin = 1.0;
                                                Url = "";
                                                ReferBy = refer.getText().toString();
                                                //otp calling
                                                loading();
                                                Otp_dialog(Pnumber);
                                                sendOtp();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this,"Password Does Not matched",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"Enter Password Again",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Password is not strong",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"Enter Your Phone Number",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Enter Correct Gmail Id",Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(MainActivity.this,"Enter Your Full Name",Toast.LENGTH_SHORT).show();
            }


        });

        TextView r_l = findViewById(R.id.R_L);
        TextView l_r = findViewById(R.id.L_R);
        LinearLayout layout_r = findViewById(R.id.Register_layout);
        LinearLayout layout_l = findViewById(R.id.login_layout);

        r_l.setOnClickListener(v -> {
            layout_r.setVisibility(View.GONE);
            layout_l.setVisibility(View.VISIBLE);
        });

        l_r.setOnClickListener(v -> {
            layout_l.setVisibility(View.GONE);
            layout_r.setVisibility(View.VISIBLE);
        });

        TextView text_email = findViewById(R.id.email_text_box);
        TextView text_pass =  findViewById(R.id.Password_text_box);
        Button button_login = findViewById(R.id.button_login);
        button_login.setOnClickListener(v -> {
            if (text_email.getText().toString().contains("@gmail.com")  && text_email.getText() != null)
            {
                if (text_pass.getText() != null)
                {
                    mFirebaseAuth.signInWithEmailAndPassword(text_email.getText().toString().toLowerCase(),text_pass.getText().toString().trim())
                            .addOnCompleteListener(MainActivity.this,task -> {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(MainActivity.this,"Sign In Success" +task.getException() , Toast.LENGTH_SHORT).show();
                                    Intent intent =  new Intent(MainActivity.this, Home.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                    builder.setMessage(""+task.getException());
                                    builder.setNegativeButton("Ok", (dialog, which) -> dialog.dismiss());
                                    builder.create().show();
                                    Toast.makeText(MainActivity.this,""+task.getException(),Toast.LENGTH_LONG).show();
                                }
                            });
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Enter Correct Password",Toast.LENGTH_SHORT).show();
                }
            }
            else {
            Toast.makeText(MainActivity.this,"Enter Correct Gmail Id",Toast.LENGTH_SHORT).show();

            }
        });


        Button button_ver = findViewById(R.id.Button_send_verification);

        button_ver.setOnClickListener(v -> {
            Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).sendEmailVerification();
            Toast.makeText(MainActivity.this,"Verification Mail Send To Your Gmail Please Verify And Restart App",Toast.LENGTH_LONG).show();
            button_ver.setVisibility(View.GONE);
        });



    }
    private void onHeadlessCallback(@NonNull final HeadlessResponse response) {
        if (response.getStatusCode() == 200) {
            switch (response.getResponseType()) {
                case "INITIATE":
                    // notify that headless authentication has been initiated
                    Toast.makeText(getApplicationContext(),"Otp sent",Toast.LENGTH_SHORT).show();
                    break;
                case "VERIFY":
                    Otp_dialog_cancel();
                    registeration();
                    // notify that verification is completed
                    // and this is notified just before "ONETAP" final response
                    break;
                case "OTP_AUTO_READ":
                    final String otp = response.getResponse().optString("otp");
                    Context context = getApplicationContext();
                    Otp otp1 = new Otp(context,otp);
                    break;
                case "ONETAP":
                    // final response with token
                    final JSONObject responseWithToken = response.getResponse();
                    this.responseWithToken1 = responseWithToken;
                    //mDatabaseReference.child(Uid).child("Registration_response").setValue(responseWithToken);
                    break;
            }
            JSONObject successResponse = response.getResponse();
            this.successResponse1 = successResponse;
        } else {
            // handle error
            String error = response.getResponse().optString("errorMessage");
            Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
        }
    }

    public void Otp_dialog(String number)
    {
        Context context = MainActivity.this;
        otp = new Otp(context,number);
        otp.setOtplessView(otplessView);
        otp.setOtp_ref(otp);
        otp.setMain(main);
        otp.setCancelable(false);
        otp.getWindow().setBackgroundDrawable(getDrawable(R.drawable.otp));
        otp.getWindow().setWindowAnimations(R.style.DialogAnimation);
        otp.getWindow().setGravity(Gravity.BOTTOM);
        otp.create();
        otp.show();
    }

    void Otp_dialog_cancel()
    {
        otp.dismiss();
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


    void SetOtp(String otp){
        this.OTP = otp;
        final HeadlessRequest request = new HeadlessRequest();
        request.setPhoneNumber("91", this.Phone);
        request.setOtp(String.valueOf(this.OTP));
        otplessView.startHeadless(request, this::onHeadlessCallback);
    }

    void sendOtp(){
        request = new HeadlessRequest();
        request.setPhoneNumber("91",Pnumber);
        otplessView.startHeadless(request, MainActivity.this::onHeadlessCallback);
    }

    void registeration(){
        Toast.makeText(MainActivity.this,"Registration started",Toast.LENGTH_SHORT).show();
        mFirebaseAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Uid = mFirebaseAuth.getCurrentUser().getUid();
                users = new User_update(Name,Email,Phone,Password,ReferBy,Date,Coin,Url,Address);
                if (!ReferBy.equals("Admin"))
                {
                    users.setCoin(5);
                }
                refer.child(ReferBy).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            refer.child(ReferBy).child("UID").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    /*******************************************************/
                                    uids = snapshot.getValue(String.class);
                                    mDatabaseReference.child(uids).child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            double coin = snapshot.getValue(double.class);
                                            coin += 10;
                                            mDatabaseReference.child(uids).child("coin").setValue(coin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    refer.child(ReferBy).child("Referred").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                String Ids = snapshot.getValue(String.class);
                                                                Ids += Uid + ",";
                                                                refer.child(ReferBy).child("Referred").setValue(Ids).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                    }
                                                                });
                                                            } else {
                                                                refer.child(ReferBy).child("Referred").setValue(Uid);
                                                            }
                                                            mDatabaseReference.setValue(Uid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    mDatabaseReference.child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            if (snapshot.exists())
                                                                            {
                                                                                mDatabaseReference.child(Uid).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        record = new AirtableRecord(Uid,Name,Email,Phone,Password,ReferBy,Date,Coin,Url,Address);
                                                                                        apiService = AirtableClient.getClient().create(AirtableAPI.class);
                                                                                        Call<AirtableResponse> call = apiService.createRecord(record);
                                                                                        call.enqueue(new Callback<AirtableResponse>() {
                                                                                            @Override
                                                                                            public void onResponse(Call<AirtableResponse> call, Response<AirtableResponse> response) {
                                                                                                if (response.isSuccessful()) {
                                                                                                    phoneReference.child(Phone).child("phones").setValue(Phone).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                                            if (MainActivity.this.responseWithToken1 != null) {
                                                                                                                mDatabaseReference.child(Uid).child("Registration_response").setValue(MainActivity.this.responseWithToken1);
                                                                                                            }
                                                                                                            if (MainActivity.this.successResponse1 != null){
                                                                                                                mDatabaseReference.child(Uid).child("Success_response").setValue(MainActivity.this.successResponse1);
                                                                                                            }

                                                                                                            Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                                                                                                            mFirebaseAuth.getCurrentUser().sendEmailVerification();
                                                                                                            loading_cancel();
                                                                                                            Intent intent =  new Intent(MainActivity.this, Home.class);
                                                                                                            startActivity(intent);
                                                                                                            Log.d("Airtable", "Record added: " + response.body().getId());
                                                                                                        }
                                                                                                    });
                                                                                                } else {
                                                                                                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                                                                                                    Log.d("Airtable", "Failed to add record: " + response.message());
                                                                                                }
                                                                                            }

                                                                                            @Override
                                                                                            public void onFailure(Call<AirtableResponse> call, Throwable t) {
                                                                                                Log.e("Airtable", "Error: " + t.getMessage());
                                                                                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                });
                                                                            }
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
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

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
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else{
                Toast.makeText(getApplicationContext(), (CharSequence) task.getException(), Toast.LENGTH_SHORT).show();
            }

        });
    }
}
