package com.vertex.io;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Home extends AppCompatActivity {


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private AdView mAdView;
    private RewardedAd rewardedAd;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;
    pop_dialog popDialog;

    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("This permission is necessary to receive notifications. Please grant the permission to continue.")
                .setPositiveButton("Request Permission", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                // Your code for handling notifications goes here
            } else {
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TextView em = findViewById(R.id.Gmail_textview);
        ScrollView scrollView = findViewById(R.id.scroll);
        popDialog = new pop_dialog(this);
        show_loading();


        // Register a NetworkCallback to listen for changes in connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // For Android Nougat (API 24) and above, register the network callback
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    // Network is available, check for internet connectivity
                    runOnUiThread(() -> checkInternetConnectivity(connectivityManager));
                }

                @Override
                public void onLost(Network network) {
                    // Network is lost
                    Intent intent = new Intent(Home.this, No_Internet.class);
                    startActivity(intent);
                }
            });
        } else {
            // For devices below Android N, use BroadcastReceiver to monitor network changes
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    checkInternetConnectivity(connectivityManager);
                }
            }, filter);
        }
        ImageView refer = findViewById(R.id.refer);
        refer.setOnClickListener(v->{
            startActivity(new Intent(this,com.vertex.io.refer.class));
        });



        ImageView math = findViewById(R.id.Math);
        math.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Math.class);
            startActivity(intent);
        });

        em.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not required for this purpose
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().trim();
                if (!text.isEmpty()) {
                    scrollView.setVisibility(View.VISIBLE);
                    hide_loading();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


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


        TextView Points = findViewById(R.id.point);
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

        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        androidx.appcompat.widget.Toolbar actionBar = findViewById(R.id.home_action);
        actionBar.setNavigationOnClickListener(v -> {
            drawerLayout.openDrawer(navigationView);
        });

        ImageView profile = findViewById(R.id.Profile_view);
        profile.setOnClickListener(v -> {
            Intent intent =  new Intent(context, Profile.class);
            startActivity(intent);
        });

        com.google.android.material.navigation.NavigationView navigationView2 = findViewById(R.id.navigation_view);
        LinearLayout Profile = findViewById(R.id.nav_profile);
        LinearLayout wallet = findViewById(R.id.nav_wallet);
        LinearLayout privacy = findViewById(R.id.nav_privacy_policy);
        LinearLayout terms_and_condition = findViewById(R.id.nav_terms_and_condition);
        LinearLayout about_us = findViewById(R.id.nav_about_us);
        LinearLayout contact_us = findViewById(R.id.nav_contact_us);
        LinearLayout faqs = findViewById(R.id.nav_faq);

        Profile.setOnClickListener(a ->{
            Intent intent = new Intent(Home.this, Profile.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
        });
        wallet.setOnClickListener(a ->{
            Toast.makeText(context, "Wallet", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Home.this, Withdraw.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
        });
        privacy.setOnClickListener(a ->{
            Intent intent = new Intent(Home.this, privacy_policy.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
        });
        terms_and_condition.setOnClickListener(v->{
            Intent intent = new Intent(Home.this, terms_and_conditions.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
        });
        about_us.setOnClickListener(v->{
            Intent intent = new Intent(Home.this, about_us.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
        });
        contact_us.setOnClickListener(v->{
            Intent intent = new Intent(Home.this, ContactUs.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
        });
        faqs.setOnClickListener(v->{
            Intent intent = new Intent(Home.this, faq.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
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

        ImageView profile_pic = findViewById(R.id.Profile_pics);
        User.child("Url").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    File directory = Home.this.getFilesDir();
                    File local = new File(directory,"imageFile");
                    String imageFileName = "image_"+UID+".jpg";
                    try {
                        if (!local.exists())
                        {
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference().child(imageFileName);
                            storageRef.getFile(local).addOnSuccessListener(v ->{
                                File imageFile = local;
                                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                                profile_pic.setImageBitmap(bitmap);
                            }).addOnFailureListener( v -> {
                                Toast.makeText(Home.this,v.getMessage(),Toast.LENGTH_SHORT).show();
                            });
                        } else if (local.exists()) {
                            File imageFile = local;
                            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                            profile_pic.setImageBitmap(bitmap);
                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(Home.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ImageView task = findViewById(R.id.Spacial_task);
        task.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Task.class);
            startActivity(intent);
        });

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
        ImageView scratch = findViewById(R.id.Scratch);
        scratch.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Scratch.class);
            startActivity(intent);
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


        ImageView withdraw = findViewById(R.id.Withdraw);
        withdraw.setOnClickListener(v -> {
            Intent intent = new Intent(this, com.vertex.io.Withdraw.class);
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
                    if (!snapshot.child("date").exists()) {
                        User.child("date").setValue(todayStr);
                    } else {
                        String date = snapshot.child("date").getValue(String.class);
                        if (date != null && date.equals(todayStr)) {
                            Toast.makeText(context, "You Already Checked In Today", Toast.LENGTH_LONG).show();
                        } else {
                            if (rewardedAd != null) {
                                Toast.makeText(context, "Watch Full Ad TO Earn Reward", Toast.LENGTH_LONG).show();
                                rewardedAd.show((Activity) context, rewardItem -> {
                                    if (rewardItem != null) {
                                        double c = snapshot.child("coin").getValue(double.class);
                                        User.child("coin").setValue(c + 1).addOnSuccessListener(aVoid -> {
                                            User.child("date").setValue(todayStr).addOnSuccessListener(aVoid2 -> {
                                                Toast.makeText(context, "Daily Check In Successful", Toast.LENGTH_LONG).show();
                                                Log.d(TAG, "You earned the reward.");
                                            }).addOnFailureListener(e -> {
                                                Toast.makeText(context, "Failed to update check-in date.", Toast.LENGTH_SHORT).show();
                                                Log.e(TAG, "Failed to update check-in date.", e);
                                            });
                                        }).addOnFailureListener(e -> {
                                            Toast.makeText(context, "Failed to update coins.", Toast.LENGTH_SHORT).show();
                                            Log.e(TAG, "Failed to update coins.", e);
                                        });
                                    } else {
                                        Toast.makeText(context, "Reward not Available.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Log.d(TAG, "The rewarded ad wasn't ready yet.");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Database error: " + error.getMessage());
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
            Intent intent = new  Intent(Home.this, No_Internet.class);
        }
    }

    private final Context context = this;

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

        File directory = Home.this.getFilesDir();
        File local = new File(directory,"imageFile");
        ImageView profile_pic = findViewById(R.id.Profile_pics);
        if (local.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(local.getAbsolutePath());
            profile_pic.setImageBitmap(bitmap);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
                }
            else if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
                showPermissionDeniedDialog();
            }

        }

        Dns_check();
        Vpn_check();
    }

    private void showDnsStatus(String message) {
        // Update UI to display DNS status (e.g., Toast message, TextView)
        Toast.makeText(this, "DNS: " + message, Toast.LENGTH_SHORT).show();
    }

    private void showVpnStatus(String message) {
        // Update UI to display VPN status (e.g., Toast message, TextView)
        Toast.makeText(this, "VPN: " + message, Toast.LENGTH_SHORT).show();
    }

    private void checkInternetConnectivity(ConnectivityManager connectivityManager) {
        Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);

            if (networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                // Run internet reachability check in background
                new Thread(() -> {
                    boolean isInternetReachable = isInternetReachable();

                    // UI update should be done on the main thread
                    runOnUiThread(() -> {
                        if (isInternetReachable) {
                            //showDnsStatus("Connected to the internet");
                        } else {
                            //showDnsStatus("Connected but no internet access");
                        }
                    });
                }).start();
            } else {
                runOnUiThread(() -> showDnsStatus("No internet capability on active network"));
            }
        } else {
            runOnUiThread(() -> showDnsStatus("No active network connection"));
        }
    }
    // Simulating a real check for internet access (e.g., by pinging a server)
    private boolean isInternetReachable() {
        try {
            // Ping Google DNS to check for internet connectivity
            Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8");
            int returnVal = process.waitFor();
            return (returnVal == 0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    void show_loading(){
        popDialog.setCancelable(false);
        popDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.loading_popup));
        popDialog.getWindow().setWindowAnimations(R.style.popupAnimation);
        popDialog.getWindow().setGravity(Gravity.CENTER);
        popDialog.create();
        popDialog.show();
    }
    void hide_loading(){
        popDialog.dismiss();
    }

    private void Dns(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("DNS Connected");
        builder.setMessage("In order to use this app you must use default DNS. Please disconnect the private DNS come again.");
        builder.setPositiveButton("Ok", (dialog, which) -> finishAffinity());
        builder.setCancelable(false);
        builder.create().show();
    }

    void Dns_check(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager != null) {
                    Network activeNetwork = connectivityManager.getActiveNetwork();
                    NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);

                    if (networkCapabilities != null) {
                        // Check if the device is connected to the internet
                        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                            // Retrieve Private DNS status
                            String privateDnsSetting = Settings.Global.getString(getContentResolver(), "private_dns_mode");
                            String privateDnsSpecifier = Settings.Global.getString(getContentResolver(), "private_dns_specifier");

                            if ("hostname".equals(privateDnsSetting) && privateDnsSpecifier != null) {
                                Dns(Home.this);
                                //showDnsStatus("Connected, using Private DNS: " + privateDnsSpecifier);
                            } else if ("opportunistic".equals(privateDnsSetting)) {
                                //showDnsStatus("Connected, using Opportunistic DNS (default DNS)");
                            } else {
                                // Wi-Fi and Cellular-specific checks
                                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                                    //showDnsStatus("Connected over Wi-Fi, using default DNS");
                                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                                    //showDnsStatus("Connected over mobile network, using default DNS");
                                } else {
                                    //showDnsStatus("Connected, but transport type unknown");
                                }
                            }
                        } else {
//                            Intent intent = new Intent(this, No_Internet.class);
//                            startActivity(intent);
//                            showDnsStatus("No internet capability on active network");
                        }
                    } else {
                        //showDnsStatus("No active network connection");
                    }
                } else {
                    //showDnsStatus("ConnectivityManager not available");
                }
            } catch (Exception e) {
                e.printStackTrace();
                //showDnsStatus("Error determining DNS status");
            }
        } else {
            // For devices below Android 9, DNS check is not available using NetworkCapabilities
            //showDnsStatus("DNS check not available on this device version");
        }

    }

    void Vpn_check(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
                if (networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {

                    Intent intent = new Intent(Home.this, vpn.class);
                    startActivity(intent);
                } else {
                    //showVpnStatus("VPN is not connected");
                }
            } else {
                //showVpnStatus("No active network connection");
            }
        } else {
            //showVpnStatus("ConnectivityManager is not available");
        }
    }
}