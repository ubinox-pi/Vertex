package com.vertex.io;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    pop_dialog popDialog;

    private DatabaseReference databaseReference,databaseReference2;
    private ListView taskListView,list_pending,list_completed;
    private AllTaskAdapter taskAdapter;
    private pending_list_adapter pendingAdapter,completedAdapter;
    private ArrayList<AllTaskData> taskList = new ArrayList<>();
    private ArrayList<pending_list_data> pendingList = new ArrayList<>();
    private ArrayList<pending_list_data> completedList = new ArrayList<>();

    Animation slide_left_in, slide_right_in,slide_right_out,slide_left_out;

    String UID;

    TextView Point,pending,complete,taskAll;

    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        popDialog = new pop_dialog(this);

        showDialog();

        mAuth = FirebaseAuth.getInstance();
        UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Users").child(UID);
        Point = findViewById(R.id.point);
        pending = findViewById(R.id.pending);
        complete = findViewById(R.id.completed);
        taskAll = findViewById(R.id.allTask);
        taskListView = findViewById(R.id.lists_allTask);
        list_pending = findViewById(R.id.lists_pending);
        list_completed = findViewById(R.id.lists_completed);

        slide_left_in = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left);
        slide_right_in = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right);
        slide_right_out = AnimationUtils.loadAnimation(this, R.anim.slide_out_to_right);
        slide_left_out = AnimationUtils.loadAnimation(this, R.anim.slide_out_to_left);


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
        });

        list_pending.setOnItemClickListener((parent, view, position, id) ->{
            pending_list_data clickedTask = pendingList.get(position);
            Intent intent = new Intent(this, pendingTask.class);
            intent.putExtra("Id",clickedTask.Id);
            startActivity(intent);
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
            if (taskListView.getVisibility() != View.VISIBLE) {
                taskListView.setVisibility(View.VISIBLE);
                taskListView.startAnimation(slide_left_in);
                list_pending.setVisibility(View.GONE);
                list_completed.setVisibility(View.GONE);
            }
            taskAll.setBackground(AppCompatResources.getDrawable(this,R.color.secondry_bg));
            pending.setBackground(AppCompatResources.getDrawable(this,R.drawable.bg_app));
            complete.setBackground(AppCompatResources.getDrawable(this,R.drawable.bg_app));
            taskAll.setTextColor(getResources().getColor(R.color.secondary_text));
            pending.setTextColor(getResources().getColor(R.color.primary_text));
            complete.setTextColor(getResources().getColor(R.color.primary_text));
        });
        pending.setOnClickListener(v->{
            if (list_pending.getVisibility() != View.VISIBLE){
                if (taskListView.getVisibility() == View.VISIBLE)
                {
                    taskListView.setVisibility(View.GONE);
                    list_pending.setVisibility(View.VISIBLE);
                    list_pending.startAnimation(slide_right_in);
                } else if (list_completed.getVisibility() == View.VISIBLE)
                {
                    list_completed.setVisibility(View.GONE);
                    list_pending.setVisibility(View.VISIBLE);
                    list_pending.startAnimation(slide_left_in);
                }
            }
            pending.setBackground(AppCompatResources.getDrawable(this,R.color.secondry_bg));
            taskAll.setBackground(AppCompatResources.getDrawable(this,R.drawable.bg_app));
            complete.setBackground(AppCompatResources.getDrawable(this,R.drawable.bg_app));
            pending.setTextColor(getResources().getColor(R.color.secondary_text));
            taskAll.setTextColor(getResources().getColor(R.color.primary_text));
            complete.setTextColor(getResources().getColor(R.color.primary_text));
        });
        complete.setOnClickListener(v->{
            if (list_completed.getVisibility() != View.VISIBLE){
                list_completed.setVisibility(View.VISIBLE);
                list_completed.startAnimation(slide_right_in);
                list_pending.setVisibility(View.GONE);
                taskListView.setVisibility(View.GONE);
            }
            complete.setBackground(AppCompatResources.getDrawable(this,R.color.secondry_bg));
            pending.setBackground(AppCompatResources.getDrawable(this,R.drawable.bg_app));
            taskAll.setBackground(AppCompatResources.getDrawable(this,R.drawable.bg_app));
            complete.setTextColor(getResources().getColor(R.color.secondary_text));
            pending.setTextColor(getResources().getColor(R.color.primary_text));
            taskAll.setTextColor(getResources().getColor(R.color.primary_text));
        });



        databaseReference = FirebaseDatabase.getInstance().getReference("AllTask");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(UID).child("spacialTask");

        // Initialize ListView and set up the custom TaskAdapter
        taskAdapter = new AllTaskAdapter(this, taskList);
        taskListView.setAdapter(taskAdapter);
        pendingAdapter = new pending_list_adapter(this,pendingList);
        completedAdapter = new pending_list_adapter(this,completedList);
        list_completed.setAdapter(completedAdapter);
        list_pending.setAdapter(pendingAdapter);

        // Fetch Data from Firebase
        fetchTaskData();
        fetchTaskData2();
        fetchTaskData3();


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
                taskAdapter.notifyDataSetChanged();// Notify adapter to update the UI
                hideDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error (if any)
                Toast.makeText(Task.this, "Failed to load tasks", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchTaskData2() {
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pendingList.clear();  // Clear the list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    pending_list_data task = snapshot.getValue(pending_list_data.class);
                    task.setIsPending(snapshot.child("status").getValue(Boolean.class));
//                    taskList.add(task);
//                    Toast.makeText(Task.this,String.valueOf(task.isVisible()),Toast.LENGTH_LONG).show();
                    if(task != null)
                    {
                        if (task.isPending) {  // Check if task is visible
                            pendingList.add(task);  // Add only visible tasks  to the list
                        }
                    }
                }
                taskAdapter.notifyDataSetChanged();// Notify adapter to update the UI
                hideDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error (if any)
                Toast.makeText(Task.this, "Failed to load tasks", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchTaskData3() {
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                completedList.clear();  // Clear the list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    pending_list_data task = snapshot.getValue(pending_list_data.class);
                    task.setIsPending(snapshot.child("status").getValue(Boolean.class));
//                    taskList.add(task);
//                    Toast.makeText(Task.this,String.valueOf(task.isVisible()),Toast.LENGTH_LONG).show();
                    if(task != null)
                    {
                        if (!task.isPending) {  // Check if task is visible
                            completedList.add(task);  // Add only visible tasks  to the list
                        }
                    }
                }
                taskAdapter.notifyDataSetChanged();// Notify adapter to update the UI
                hideDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error (if any)
                Toast.makeText(Task.this, "Failed to load tasks", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void showDialog(){
        popDialog.setCancelable(false);
        popDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.loading_popup));
        popDialog.getWindow().setWindowAnimations(R.style.popupAnimation);
        popDialog.getWindow().setGravity(Gravity.CENTER);
        popDialog.create();
        popDialog.show();
    }
    void hideDialog(){
        popDialog.dismiss();
    }
}