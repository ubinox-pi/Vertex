package com.vertex.io;


import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public abstract class ads {

    DatabaseReference mDatabase;
    private static RewardedAd rewardedAds;

    private static Context context;
    private static Activity activity;

    private static String interstitial;
    private static String banner;
    private static String nativeAd;
    private static String rewardedAd;
    private static String appOpenAd;
    private static String rewardedInterstitialAd;

    public ads() {
        mDatabase = FirebaseDatabase.getInstance().getReference("ads");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ads.this.interstitial = snapshot.child("interstitial").getValue().toString();
                    ads.this.banner = snapshot.child("banner").getValue().toString();
                    ads.this.nativeAd = snapshot.child("nativeAd").getValue().toString();
                    ads.this.rewardedAd = snapshot.child("rewardedAd").getValue().toString();
                    ads.this.appOpenAd = snapshot.child("appOpenAd").getValue().toString();
                    ads.this.rewardedInterstitialAd = snapshot.child("rewardedInterstitialAd").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public ads(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public String getInterstitial() {
        return interstitial;
    }

    public String getBanner() {
        return banner;
    }

    public String getNativeAd() {
        return nativeAd;
    }

    public String getRewardedAd() {
        return rewardedAd;
    }

    public String getAppOpenAd() {
        return appOpenAd;
    }

    public String getRewardedInterstitialAd() {
        return rewardedInterstitialAd;
    }

    public void adRewd(){
        Toast.makeText(context,"Ad Is Loading Please Wait",Toast.LENGTH_SHORT).show();
        AdRequest req = new AdRequest.Builder().build();
        RewardedAd.load(context, rewardedAd,
                req, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        rewardedAds = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAds = ad;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });
    }

    public void show(){
        if (rewardedAds != null) {
            rewardedAds.show(activity, rewardItem -> {
                UserRewarded();
            });
        } else {
            adRewd();
            Toast.makeText(context, "Ad not available yet. Please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    public abstract void UserRewarded();
}
