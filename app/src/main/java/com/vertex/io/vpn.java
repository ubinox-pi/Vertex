package com.vertex.io;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class vpn extends AppCompatActivity {

    Button vpn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpn);

        vpn = findViewById(R.id.Vpn);
        vpn.setOnClickListener(v->{
            if(check_vpn()){
                Toast.makeText(this, "VPN is connected", Toast.LENGTH_SHORT).show();
            }else{
                finish();
            }
        });
    }

    boolean check_vpn(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
                if (networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {

                    return true;
                } else {
                    return false;
                }
            } else {
                Intent intent = new Intent(this,No_Internet.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(this, "Connectivity Manager is null", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}