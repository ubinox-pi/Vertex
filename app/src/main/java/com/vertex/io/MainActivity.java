package com.vertex.io;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference dbR,dbR2;
    String vCode;
    PhoneAuthProvider.ForceResendingToken FRT = null;

    long timeoutSecond = 300L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase DB = FirebaseDatabase.getInstance();

        DatabaseReference DBF = FirebaseDatabase.getInstance().getReference("refers");


        TextView lo = findViewById(R.id.Lo);
        LinearLayout layout_v = findViewById(R.id.verify_layout);
        LinearLayout layout_l0 = findViewById(R.id.main_layout);
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

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        Button r = findViewById(R.id.Button_r2);
        r.setOnClickListener(v -> {
            EditText text_e = findViewById(R.id.email_text_box2);
            EditText text_p = findViewById(R.id.phone_text_box);
            EditText text_ps = findViewById(R.id.password_text_box3);
            EditText text_cps = findViewById(R.id.check_password_text_box);
            EditText name = findViewById(R.id.Full_Name);
            EditText refer = findViewById(R.id.Refer);
            LinearLayout layout_reg = findViewById(R.id.Register_layout);

            if (!name.getText().toString().isEmpty())
            {
                if (!text_e.getText().toString().isEmpty() && text_e.getText().toString().contains("@gmail.com"))
                {
                    if (!text_p.getText().toString().isEmpty() && text_p.getText().toString().length()==10)
                    {
                        if (!text_ps.getText().toString().isEmpty() && text_ps.getText().toString().contains("@"))
                        {
                            if (!text_cps.getText().toString().isEmpty())
                            {
                                if (!refer.getText().toString().isEmpty())
                                {
                                    if (text_ps.getText().toString().equals(text_cps.getText().toString()))
                                    {
                                        String ref = refer.getText().toString();
                                        DBF.child(ref).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists())
                                                {
                                                    String Ud = snapshot.getValue(String.class);
                                                    DatabaseReference sk = FirebaseDatabase.getInstance().getReference("Users").child(Ud);
                                                    sk.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            long p;
                                                            p = snapshot.child("coin").getValue(long.class);
                                                            sk.child("coin").setValue(p+2);
                                                            text_e.setEnabled(false);
                                                            text_p.setEnabled(false);
                                                            text_ps.setEnabled(false);
                                                            text_cps.setEnabled(false);
                                                            name.setEnabled(false);
                                                            refer.setEnabled(false);
                                                            r.setEnabled(false);
                                                            Toast.makeText(MainActivity.this, "Registration Started", Toast.LENGTH_SHORT).show();

                                                            mFirebaseAuth.createUserWithEmailAndPassword(text_e.getText().toString().toLowerCase().trim(),text_p.getText().toString().trim())

                                                                    .addOnCompleteListener(MainActivity.this, task1 -> {
                                                                        if (task1.isSuccessful())
                                                                        {
                                                                            FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                                                                            assert u != null;
                                                                            String Uid = u.getUid();
                                                                            String Email = text_e.getText().toString();
                                                                            String Password = text_cps.getText().toString();
                                                                            String Phone = text_p.getText().toString();
                                                                            String ReferBy = refer.getText().toString();
                                                                            String Name = name.getText().toString();
                                                                            Phones phone = new Phones(Phone);
                                                                            dbR = DB.getReference("Users");

                                                                            Map<String, Object> Users = new HashMap<>();
                                                                            Users.put("name",Name);
                                                                            Users.put("email",Email);
                                                                            Users.put("password",Password);
                                                                            Users.put("phone",Phone);
                                                                            Users.put("referBy",ReferBy);
                                                                            Users.put("coin",0);
                                                                            Users.put("link","");
                                                                            Users.put("date","");


                                                                            dbR.child(Uid).setValue(Users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                    Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).sendEmailVerification();
                                                                                    mFirebaseAuth.getCurrentUser().sendEmailVerification();
                                                                                    Toast.makeText(MainActivity.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                                                                                    Toast.makeText(MainActivity.this, "Verification Mail Has Been Send Your Gmail Id", Toast.LENGTH_SHORT).show();
                                                                                    text_e.setText("");
                                                                                    text_p.setText("");
                                                                                    text_ps.setText("");
                                                                                    text_cps.setText("");
                                                                                    layout_reg.setVisibility(View.GONE);
                                                                                    Intent intent =  new Intent(MainActivity.this, Home.class);
                                                                                    startActivity(intent);

                                                                                }
                                                                            });
                                                                            dbR2 = DB.getReference("Phones");
                                                                            dbR2.child(Phone).setValue(phone);
                                                                        }
                                                                        else
                                                                        {
                                                                            Toast.makeText(MainActivity.this, "Registration Failed: " + task1.getException(), Toast.LENGTH_SHORT).show();
                                                                            Log.e("RegistrationError", "Registration failed: " + task1.getException());
                                                                            text_e.setEnabled(true);
                                                                            text_p.setEnabled(true);
                                                                            text_ps.setEnabled(true);
                                                                            text_cps.setEnabled(true);
                                                                            name.setEnabled(false);
                                                                            refer.setEnabled(false);
                                                                            r.setEnabled(true);
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
                                                    Toast.makeText(MainActivity.this,"Invalid Refer Id",Toast.LENGTH_SHORT).show();
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
                                    if (text_ps.getText().toString().equals(text_cps.getText().toString()))
                                    {
                                        text_e.setEnabled(false);
                                        text_p.setEnabled(false);
                                        text_ps.setEnabled(false);
                                        text_cps.setEnabled(false);
                                        name.setEnabled(false);
                                        refer.setEnabled(false);
                                        r.setEnabled(false);
                                        Toast.makeText(MainActivity.this, "Registration Started", Toast.LENGTH_SHORT).show();

                                        mFirebaseAuth.createUserWithEmailAndPassword(text_e.getText().toString().toLowerCase().trim(),text_p.getText().toString().trim())

                                                .addOnCompleteListener(MainActivity.this, task1 -> {
                                                    if (task1.isSuccessful())
                                                    {
                                                        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                                                        assert u != null;
                                                        String Uid = u.getUid();
                                                        String Email = text_e.getText().toString();
                                                        String Password = text_cps.getText().toString();
                                                        String Phone = text_p.getText().toString();
                                                        String ReferBy = "Admin(Dev)";
                                                        String Name = name.getText().toString();
                                                        Users users = new Users(Email,Phone,Password,ReferBy,Name);
                                                        Phones phone = new Phones(Phone);

                                                        Map<String, Object> Users = new HashMap<>();
                                                        Users.put("name",Name);
                                                        Users.put("email",Email);
                                                        Users.put("password",Password);
                                                        Users.put("phone",Phone);
                                                        Users.put("referBy",ReferBy);
                                                        Users.put("coin",0);
                                                        Users.put("link","");
                                                        Users.put("date","");

                                                        dbR.child(Uid).setValue(Users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).sendEmailVerification();
                                                                mFirebaseAuth.getCurrentUser().sendEmailVerification();
                                                                Toast.makeText(MainActivity.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                                                                Toast.makeText(MainActivity.this, "Verification Mail Has Been Send Your Gmail Id", Toast.LENGTH_SHORT).show();
                                                                text_e.setText("");
                                                                text_p.setText("");
                                                                text_ps.setText("");
                                                                text_cps.setText("");
                                                                layout_reg.setVisibility(View.GONE);
                                                                Intent intent =  new Intent(MainActivity.this, Home.class);
                                                                startActivity(intent);

                                                            }
                                                        });
                                                        dbR2 = DB.getReference("Phones");
                                                        dbR2.child(Phone).setValue(phone);
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(MainActivity.this, "Registration Failed: " + task1.getException(), Toast.LENGTH_SHORT).show();
                                                        Log.e("RegistrationError", "Registration failed: " + task1.getException());
                                                        text_e.setEnabled(true);
                                                        text_p.setEnabled(true);
                                                        text_ps.setEnabled(true);
                                                        text_cps.setEnabled(true);
                                                        name.setEnabled(false);
                                                        refer.setEnabled(false);
                                                        r.setEnabled(true);
                                                    }
                                                });
                                    }
                                    else
                                    {
                                        Toast.makeText(MainActivity.this,"Password Does Not Matched",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"Enter Password Again",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Enter Your Password",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"Enter Your Phone Number",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Enter Your Gmail Id",Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(MainActivity.this,"Enter Your Full Name",Toast.LENGTH_SHORT).show();
            }


        });


        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        EditText text_e = findViewById(R.id.email_text_box2);
        EditText text_p = findViewById(R.id.phone_text_box);
        EditText text_ps = findViewById(R.id.password_text_box3);
        EditText text_cps = findViewById(R.id.check_password_text_box);
        EditText text_otp = findViewById(R.id.otp_text_box);
        Button  register = findViewById(R.id.button_register);
        Button  otp = findViewById(R.id.Button_otp);
        TextView Resend = findViewById(R.id.Resend);
        otp.setOnClickListener(v -> {
            if (text_e.getText() != null && text_e.getText().toString().contains("@gmail.com"))
            {
                if (text_p.getText() != null && text_p.getText().toString().length()==10 )
                {
                    if (text_ps.getText() != null && text_ps.getText().toString().contains("@") && text_e.getText().toString().length()>6 && text_ps.getText().toString().length()<16)
                    {
                        if (text_cps.getText().toString().equals(text_ps.getText().toString()))
                        {
                            text_e.setEnabled(false);
                            text_p.setEnabled(false);
                            text_ps.setEnabled(false);
                            text_cps.setEnabled(false);
                            String Ph = "+91"+text_p.getText().toString().trim();
                            sendOtp(Ph , false);


                        }
                        else {
                            Toast.makeText(MainActivity.this, "Password Does Not Matched", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Enter Hard Password", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Enter Correct Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(MainActivity.this, "Enter Correct Gmail Id", Toast.LENGTH_SHORT).show();
            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        TextView r_l = findViewById(R.id.R_L);
        TextView l_r = findViewById(R.id.L_R);
        LinearLayout layout_r = findViewById(R.id.Register_layout);
        LinearLayout layout_l = findViewById(R.id.main_layout);

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

        button_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).sendEmailVerification();
                Toast.makeText(MainActivity.this,"Verification Mail Send To Your Gmail Please Verify And Restart App",Toast.LENGTH_LONG).show();
                button_ver.setVisibility(View.GONE);
            }
        });



    }


    void sendOtp (String PhoneNumber, @NonNull Boolean isResend)
    {
        PhoneAuthOptions.Builder builder =
        PhoneAuthOptions.newBuilder(mFirebaseAuth)
                .setPhoneNumber(String.valueOf(PhoneNumber))
                .setTimeout(120L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Button button_ot = findViewById(R.id.Button_otp);
                        Button button_reg = findViewById(R.id.button_register);
                        TextView resend = findViewById(R.id.Resend);
                        EditText text_otp = findViewById(R.id.otp_text_box);

                        text_otp.setVisibility(View.VISIBLE);
                        button_ot.setVisibility(View.GONE);
                        button_reg.setVisibility(View.VISIBLE);
                        resend.setVisibility(View.VISIBLE);


                        Toast.makeText(MainActivity.this,"Otp Send Successfully1",Toast.LENGTH_SHORT).show();

                        button_ot.setVisibility(View.GONE);
                        button_reg.setVisibility(View.VISIBLE);
                        resend.setVisibility(View.VISIBLE);


                        LinearLayout layout_l = findViewById(R.id.main_layout);
                        LinearLayout layout_reg = findViewById(R.id.Register_layout);

                        EditText text_e = findViewById(R.id.email_text_box2);
                        EditText text_p = findViewById(R.id.phone_text_box);
                        EditText text_ps = findViewById(R.id.password_text_box3);
                        EditText text_cps = findViewById(R.id.check_password_text_box);
                        //EditText text_otp = findViewById(R.id.otp_text_box);

                        FirebaseDatabase DB = FirebaseDatabase.getInstance();

                        button_reg.setOnClickListener(v -> {

                            String Otp = text_otp.getText().toString();
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(vCode,Otp);


                            mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(MainActivity.this,"Otp Is Verified",Toast.LENGTH_SHORT).show();
                                    Toast.makeText(MainActivity.this, "Registration Started", Toast.LENGTH_SHORT).show();

                                    mFirebaseAuth.createUserWithEmailAndPassword(text_e.getText().toString().toLowerCase(),text_p.getText().toString().trim())
                                            .addOnCompleteListener(MainActivity.this, task1 -> {
                                                if (task1.isSuccessful())
                                                {
                                                    FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                                                    assert u != null;
                                                    String Uid = u.getUid();
                                                    String Email = text_e.getText().toString();
                                                    String Password = text_cps.getText().toString();
                                                    String Phone = text_p.getText().toString();
                                                    String ReferBy = null;
                                                    String Name = null;
                                                    Users users = new Users(Email,Phone,Password,ReferBy,Name);
                                                    Phones phone = new Phones(Phone);
                                                    dbR = DB.getReference("Users");
                                                    dbR.child(Uid).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {


                                                            Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).sendEmailVerification();
                                                            mFirebaseAuth.getCurrentUser().sendEmailVerification();
                                                            Toast.makeText(MainActivity.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                                                            Toast.makeText(MainActivity.this, "Verification Mail Has Been Send Your Gmail Id", Toast.LENGTH_SHORT).show();
                                                            text_e.setText("");
                                                            text_p.setText("");
                                                            text_ps.setText("");
                                                            text_cps.setText("");
                                                            text_otp.setVisibility(View.GONE);
                                                            button_ot.setVisibility(View.VISIBLE);
                                                            button_reg.setVisibility(View.GONE);
                                                            resend.setVisibility(View.GONE);
                                                            layout_l.setVisibility(View.GONE);
                                                            layout_reg.setVisibility(View.VISIBLE);
                                                            mFirebaseAuth.signOut();
                                                            Toast.makeText(MainActivity.this,"Login To Go Ahead",Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    dbR2 = DB.getReference("Phones");
                                                    dbR2.child(Phone).setValue(phone);
                                                }
                                                else
                                                {
                                                    Toast.makeText(MainActivity.this, "Registration Failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                                                    Log.e("RegistrationError", "Registration failed: " + task.getException());
                                                    button_reg.setEnabled(true);
                                                }
                                            });
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this,""+task.getException(),Toast.LENGTH_LONG).show();
                                    button_reg.setEnabled(true);
                                }
                            });


                        });


                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(MainActivity.this,"Otp Send Failed2"+e.getMessage(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        vCode = s;
                        FRT = forceResendingToken;
                        Toast.makeText(MainActivity.this,"Otp send Successfully3",Toast.LENGTH_SHORT).show();

                        EditText text_e = findViewById(R.id.email_text_box2);
                        EditText text_p = findViewById(R.id.phone_text_box);
                        EditText text_ps = findViewById(R.id.password_text_box3);
                        EditText text_cps = findViewById(R.id.check_password_text_box);
                        EditText text_otp = findViewById(R.id.otp_text_box);

                        Button button_ot = findViewById(R.id.Button_otp);
                        Button button_reg = findViewById(R.id.button_register);
                        TextView resend = findViewById(R.id.Resend);

//                        Button button_ot = findViewById(R.id.Button_otp);
//                        Button button_reg = findViewById(R.id.button_register);
//                        TextView resend = findViewById(R.id.Resend);
//                        EditText text_otp = findViewById(R.id.otp_text_box);

                        text_otp.setVisibility(View.VISIBLE);
                        button_ot.setVisibility(View.GONE);
                        button_reg.setVisibility(View.VISIBLE);
                        resend.setVisibility(View.VISIBLE);
                        LinearLayout layout_l = findViewById(R.id.main_layout);
                        LinearLayout layout_reg = findViewById(R.id.Register_layout);

                        FirebaseDatabase DB = FirebaseDatabase.getInstance();


                        button_reg.setOnClickListener(v -> {

                            String Otp = text_otp.getText().toString();
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(vCode,Otp);


                            mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(MainActivity.this,"Otp Is Verified",Toast.LENGTH_SHORT).show();
                                    Toast.makeText(MainActivity.this, "Registration Started", Toast.LENGTH_SHORT).show();

                                    mFirebaseAuth.createUserWithEmailAndPassword(text_e.getText().toString().toLowerCase(),text_p.getText().toString().trim())
                                            .addOnCompleteListener(MainActivity.this, task1 -> {
                                                if (task1.isSuccessful())
                                                {
                                                    FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                                                    assert u != null;
                                                    String Uid = u.getUid();
                                                    String Email = text_e.getText().toString();
                                                    String Password = text_cps.getText().toString();
                                                    String Phone = text_p.getText().toString();
                                                    String ReferBy = null;
                                                    String Name = null;
                                                    Users users = new Users(Email,Phone,Password,ReferBy,Name);
                                                    Phones phone = new Phones(Phone);
                                                    dbR = DB.getReference("Users");
                                                    dbR.child(Uid).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).sendEmailVerification();
                                                            mFirebaseAuth.getCurrentUser().sendEmailVerification();
                                                            Toast.makeText(MainActivity.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                                                            Toast.makeText(MainActivity.this, "Verification Mail Has Been Send Your Gmail Id", Toast.LENGTH_SHORT).show();
                                                            text_e.setText("");
                                                            text_p.setText("");
                                                            text_ps.setText("");
                                                            text_cps.setText("");
                                                            text_otp.setVisibility(View.GONE);
                                                            button_ot.setVisibility(View.VISIBLE);
                                                            button_reg.setVisibility(View.GONE);
                                                            resend.setVisibility(View.GONE);
                                                            layout_reg.setVisibility(View.GONE);
                                                            layout_l.setVisibility(View.VISIBLE);
                                                            mFirebaseAuth.signOut();
                                                            Toast.makeText(MainActivity.this,"Login To Go Ahead",Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                                    dbR2 = DB.getReference("Phones");
                                                    dbR2.child(Phone).setValue(phone);
                                                }
                                                else
                                                {
                                                    Toast.makeText(MainActivity.this, "Registration Failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                                                    Log.e("RegistrationError", "Registration failed: " + task.getException());
                                                    button_reg.setEnabled(true);
                                                }
                                            });
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this,""+task.getException(),Toast.LENGTH_LONG).show();
                                    button_reg.setEnabled(true);
                                }
                            });


                        });


                    }
                });

        if (isResend)
        {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(FRT).build());
        }
        else
        {
            FRT = null;

            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }


        TextView Ctext = findViewById(R.id.Email_change_text_b0x);
        Button Cbutton = findViewById(R.id.Gmail_change);
        Cbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Ctext.getText() == null && Ctext.getText().toString().contains("@gmail.com"))
                {
                    Toast.makeText(MainActivity.this,"Enter Correct Gmail Id",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Enter Correct Gmail Id",Toast.LENGTH_SHORT).show();
                }
            }
        });


        TextView Resend_otp  = findViewById(R.id.Resend);
        TextView Otp = findViewById(R.id.otp_text_box);
        String Phone = "+91"+Otp.getText().toString();
        Resend_otp.setOnClickListener(v -> {
            sendOtp(Phone,true);
            Toast.makeText(MainActivity.this,"Otp Resend",Toast.LENGTH_SHORT).show();
        });
    }
}