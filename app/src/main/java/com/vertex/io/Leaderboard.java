package com.vertex.io;

import static android.content.ContentValues.TAG;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard extends AppCompatActivity {

    List<leaderBoardData> leader;
    DatabaseReference databaseReference;
    ListView listView;
    leaderboardAdapter adapter;
    int count,mainCount;
    ImageView profile1st,profile2nd,profile3rd;

    TextView name1,name2,name3,point1,point2,point3;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_leaderboard);
        listView = findViewById(R.id.leaderboardList);

        leader = new ArrayList<>();
        adapter = new leaderboardAdapter(this, leader);
        listView.setAdapter(adapter);

        name1 = findViewById(R.id.profileName1st);
        name2 = findViewById(R.id.profileName2nd);
        name3 = findViewById(R.id.profileName3rd);
        point1 = findViewById(R.id.profilePoints1st);
        point2 = findViewById(R.id.profilePoints2nd);
        point3 = findViewById(R.id.profilePoints3rd);
        profile1st = findViewById(R.id.profile1st);
        profile2nd = findViewById(R.id.profile2nd);
        profile3rd = findViewById(R.id.profile3rd);


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        try {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    count = 0;
                    mainCount = 0;
                    leader.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        Double coinValue = dataSnapshot.child("coin").getValue(Double.class);
                        String Url = dataSnapshot.child("Url").getValue(String.class);

                        if (name != null && coinValue != null) {
                            leaderBoardData leaderBoardData = new leaderBoardData(name, coinValue, Url);
                            leader.add(leaderBoardData);
                        }
                    }

                    leader.sort((leader1, leader2) -> {
                        Double coin1 = leader1.getCoin();
                        Double coin2 = leader2.getCoin();
                        return coin2.compareTo(coin1);
                    });

                    if (leader.size() > 10) {
                        leader = leader.subList(0, 10);
                    }

                    if (leader.size() > 0) {
                        name1.setText(leader.get(0).getName());
                        point1.setText(String.valueOf(leader.get(0).getCoin()));
                        String link = leader.get(0).getUrl();
                        Glide.with(Leaderboard.this)
                                .load(link)
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.image) // Add a placeholder image
                                        .error(R.drawable.error) // Add an error image
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .override(Target.SIZE_ORIGINAL))
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        // Log the error or handle it
                                        Log.e(TAG,"An error occurred: ", e);
                                        e.printStackTrace();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        return false;
                                    }
                                }).into(profile1st);
                    }
                    if (leader.size() > 1) {
                        name2.setText(leader.get(1).getName());
                        point2.setText(String.valueOf(leader.get(1).getCoin()));
                        String link = leader.get(1).getUrl();
                        Toast.makeText(Leaderboard.this, link, Toast.LENGTH_LONG).show();
                        Glide.get(Leaderboard.this).clearMemory();
                        Glide.with(Leaderboard.this)
                                .load(link)
                                .apply(new RequestOptions()
                                        .placeholder(com.google.firebase.inappmessaging.display.R.drawable.image_placeholder) // Add a placeholder image
                                        .error(R.drawable.error) // Add an error image
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .override(Target.SIZE_ORIGINAL))
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        // Log the error or handle it
                                        Log.e(TAG,"An error occurred: ", e);
                                        e.printStackTrace();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        return false;
                                    }
                                }).into(profile2nd);
                    }
                    if (leader.size() > 2) {
                        name3.setText(leader.get(2).getName());
                        point3.setText(String.valueOf(leader.get(2).getCoin()));
                        String link = leader.get(2).getUrl();
                        Glide.with(Leaderboard.this)
                                .load(link)
                                .apply(new RequestOptions()
                                        .placeholder(com.google.firebase.inappmessaging.display.R.drawable.image_placeholder) // Add a placeholder image
                                        .error(R.drawable.error) // Add an error image
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .override(Target.SIZE_ORIGINAL))
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        // Log the error or handle it
                                        Log.e(TAG,"An error occurred: ", e);
                                        e.printStackTrace();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        return false;
                                    }
                                }).into(profile3rd);
                        leader.remove(1);
                        leader.remove(0);
                        leader.remove(2);
                    }
                    adapter.notifyDataSetChanged();
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "An error occurred. Please try after some time", Toast.LENGTH_LONG).show();
        }
    }

}
