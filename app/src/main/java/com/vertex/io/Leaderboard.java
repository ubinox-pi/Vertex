package com.vertex.io;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import coil.ImageLoader;
import coil.request.ImageRequest;

public class Leaderboard extends AppCompatActivity {

    List<leaderBoardData> leader;
    DatabaseReference databaseReference;
    ListView listView;
    leaderboardAdapter adapter;
    int count, mainCount;
    ImageView profile1st, profile2nd, profile3rd;

    TextView name1, name2, name3, point1, point2, point3;


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
                        setProfileData(leader.get(0), name1, point1, profile1st);
                    }
                    if (leader.size() > 1) {
                        setProfileData(leader.get(1), name2, point2, profile2nd);
                    }
                    if (leader.size() > 2) {
                        setProfileData(leader.get(2), name3, point3, profile3rd);
                        leader.remove(2);
                        leader.remove(1);
                        leader.remove(0);
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Leaderboard.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "An error occurred. Please try again later.", Toast.LENGTH_LONG).show();
        }
    }

    private void setProfileData(leaderBoardData data, TextView nameText, TextView pointsText, ImageView profileImage) {
        String name = data.getName();
        Double coinValue = data.getCoin();
        String url = data.getUrl();

        nameText.setText(name);
        pointsText.setText(String.valueOf(coinValue));

        if (url == null || url.isEmpty()) {
            url = "https://firebasestorage.googleapis.com/v0/b/doge-69ff5.appspot.com/o/images%2Fimage_APNJZmB0fSbM9uVh88qnC6b10XS2.jpg?alt=media&token=bf04bc7f-39b9-4391-b2d5-9ddd0a9f36f1";
        }

        Uri imageUri = Uri.parse(url);

        ImageLoader imageLoader = new ImageLoader.Builder(this)
                .crossfade(true)
                .build();

        ImageRequest request = new ImageRequest.Builder(this)
                .data(imageUri)
                .target(profileImage)
                .placeholder(R.drawable.profile)
                .error(R.drawable.error)
                .build();

        imageLoader.enqueue(request);
    }

}
