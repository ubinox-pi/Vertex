package com.vertex.io;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class information extends AppCompatActivity {

    TextView name, description,whatToDo;
    Button click;
    String id,Name,Description,WhatToDo,Link,LongDescription,Imagelink;

    ImageView taskImage;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("spacialTask");


    pop_dialog popDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        popDialog = new pop_dialog(information.this);

        taskImage = findViewById(R.id.images);
        name = findViewById(R.id.App_info);
        description = findViewById(R.id.description);
        whatToDo = findViewById(R.id.whatToDo);
        click = findViewById(R.id.submit);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Name = intent.getStringExtra("name");
        Description = intent.getStringExtra("description");
        WhatToDo = intent.getStringExtra("whatToDo");
        Link = intent.getStringExtra("link");
        LongDescription = intent.getStringExtra("longDescription");
        Imagelink = intent.getStringExtra("imagelink");

        Glide.with(information.this)
                .load(Imagelink)
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
                        dismiss();
                        Log.e(TAG,"An error occurred: ", e);
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(taskImage);

        name.setText(Name);
        description.setText(Description);
        whatToDo.setText(WhatToDo);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        click.setOnClickListener(v->{
            loading();
            reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        dismiss();
                        Toast.makeText(information.this, "You have already enrolled", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        reference.child(id).child("id").setValue(id);
                        reference.child(id).child("name").setValue(Name);
                        reference.child(id).child("description").setValue(Description);
                        reference.child(id).child("whatToDo").setValue(WhatToDo);
                        reference.child(id).child("link").setValue(Link);
                        reference.child(id).child("longDescription").setValue(LongDescription);
                        reference.child(id).child("imagelink").setValue(Imagelink);
                        reference.child(id).child("status").setValue("pending").addOnCompleteListener(task -> {
                            dismiss();
                        });
                        Toast.makeText(information.this, "Enrolled", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    dismiss();
                    Toast.makeText(information.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    void loading()
    {
        popDialog.setCancelable(false);
        popDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.loading_popup));
        popDialog.getWindow().setWindowAnimations(R.style.popupAnimation);
        popDialog.getWindow().setGravity(Gravity.CENTER);
        popDialog.create();
        popDialog.show();
    }
    void dismiss()
    {
        popDialog.dismiss();
    }
}