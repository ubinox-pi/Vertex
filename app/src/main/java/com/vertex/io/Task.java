package com.vertex.io;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Task extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    private DatabaseReference databaseReference;
    private ListView taskListView;
    private AllTaskAdapter taskAdapter;
    private ArrayList<AllTaskData> taskList = new ArrayList<>();


    String UID;

    TextView Point,pending,complete,taskAll;

    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        mAuth = FirebaseAuth.getInstance();
        UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Users").child(UID);
        Point = findViewById(R.id.point);
        pending = findViewById(R.id.pending);
        complete = findViewById(R.id.completed);
        taskAll = findViewById(R.id.allTask);
        taskListView = findViewById(R.id.lists_allTask);
        //listView = findViewById(R.id.lists_allTask);


        taskListView.setOnItemClickListener((parent, view, position, id) ->{
            AllTaskData clickedTask = taskList.get(position);
            Intent intent = new Intent(this, information.class);
            intent.putExtra("id",clickedTask.Id);
            intent.putExtra("name",clickedTask.name);
            intent.putExtra("description",clickedTask.description);
            intent.putExtra("longDescription",clickedTask.LongDescription);
            intent.putExtra("link",clickedTask.Link);
            intent.putExtra("imagelink",clickedTask.Imagelink);
            intent.putExtra("whatToDo",clickedTask.whatToDo);
            startActivity(intent);
            Toast.makeText(this, "Clicked: " + clickedTask, Toast.LENGTH_SHORT).show();
        });


        toolbar = findViewById(R.id.appBar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(v-> finish());

        mReference.child("coin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Double coin = snapshot.getValue(Double.class);
                    Point.setText(String.valueOf(coin));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        taskAll.setOnClickListener(v->{
            taskAll.setBackground(AppCompatResources.getDrawable(this,R.color.secondry_bg));
            pending.setBackground(AppCompatResources.getDrawable(this,R.drawable.bg_app));
            complete.setBackground(AppCompatResources.getDrawable(this,R.drawable.bg_app));
            taskAll.setTextColor(getResources().getColor(R.color.secondary_text));
            pending.setTextColor(getResources().getColor(R.color.primary_text));
            complete.setTextColor(getResources().getColor(R.color.primary_text));
        });
        pending.setOnClickListener(v->{
            pending.setBackground(AppCompatResources.getDrawable(this,R.color.secondry_bg));
            taskAll.setBackground(AppCompatResources.getDrawable(this,R.drawable.bg_app));
            complete.setBackground(AppCompatResources.getDrawable(this,R.drawable.bg_app));
            pending.setTextColor(getResources().getColor(R.color.secondary_text));
            taskAll.setTextColor(getResources().getColor(R.color.primary_text));
            complete.setTextColor(getResources().getColor(R.color.primary_text));
        });
        complete.setOnClickListener(v->{
            complete.setBackground(AppCompatResources.getDrawable(this,R.color.secondry_bg));
            pending.setBackground(AppCompatResources.getDrawable(this,R.drawable.bg_app));
            taskAll.setBackground(AppCompatResources.getDrawable(this,R.drawable.bg_app));
            complete.setTextColor(getResources().getColor(R.color.secondary_text));
            pending.setTextColor(getResources().getColor(R.color.primary_text));
            taskAll.setTextColor(getResources().getColor(R.color.primary_text));
        });



        databaseReference = FirebaseDatabase.getInstance().getReference("AllTask");

        // Initialize ListView and set up the custom TaskAdapter
        taskAdapter = new AllTaskAdapter(this, taskList);
        taskListView.setAdapter(taskAdapter);

        // Fetch Data from Firebase
        fetchTaskData();


    }

    private void fetchTaskData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                taskList.clear();  // Clear the list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AllTaskData task = snapshot.getValue(AllTaskData.class);
                    task.setVisible(snapshot.child("isVisible").getValue(Boolean.class));
//                    taskList.add(task);
//                    Toast.makeText(Task.this,String.valueOf(task.isVisible()),Toast.LENGTH_LONG).show();
                    if(task != null)
                    {
                        if (task.isVisible) {  // Check if task is visible
                            taskList.add(task);  // Add only visible tasks  to the list
                        }
                    }
                }
                taskAdapter.notifyDataSetChanged();  // Notify adapter to update the UI
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error (if any)
                Toast.makeText(Task.this, "Failed to load tasks", Toast.LENGTH_SHORT).show();
            }
        });
    }
}