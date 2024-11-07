package com.vertex.io;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class pendingTask extends AppCompatActivity {

    String Id;

    pop_dialog popDialog;
    ImageView taskImage;
    Button task;
    Boolean isImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_task);
        Id = getIntent().getStringExtra("Id");
        popDialog = new pop_dialog(this);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference Users = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("spacialTask").child(Id);
        Users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Url").exists()){
                   isImage = true;
                }
                else {
                    isImage = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        taskImage = findViewById(R.id.upload_image);
        task = findViewById(R.id.upload);
        taskImage.setOnClickListener(v -> {
            if(isImage){
                Toast.makeText(this, "You have already uploaded image. We are reviewing please wait.", Toast.LENGTH_SHORT).show();
            }else{
                loading();
                launcher.launch(
                        ImagePicker.with(this)
                                //...
                                .setMultipleAllowed(false)
                                .galleryOnly()
                                .createIntent()
                );
            }
        });
        ImagePicker.with(this)
                .setDismissListener(this::dismiss);
    }



    ActivityResultLauncher<Intent> launcher=
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                if(result.getResultCode()==RESULT_OK){
                    Uri uri=result.getData().getData();
                    Toast.makeText(this, "Please Wait", Toast.LENGTH_LONG).show();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    String UID = mAuth.getCurrentUser().getUid();
                    String imageFileName = "spacialTask"+UID+Id+".jpg";
                    DatabaseReference Users = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("spacialTask").child(Id);
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference imageRef = storageRef.child(imageFileName);
                    imageRef.putFile(Objects.requireNonNull(result.getData().getData())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            Users.child("Url").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                    dismiss();
                                    Toast.makeText(pendingTask.this, "Upload success. Once we will review you will rewarded", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dismiss();
                                    Toast.makeText(pendingTask.this, "Upload failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dismiss();
                            Toast.makeText(pendingTask.this, "Upload failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                }else if(result.getResultCode()==ImagePicker.RESULT_ERROR){
                    dismiss();
                    Toast.makeText(this, ImagePicker.Companion.getError(result.getData()), Toast.LENGTH_SHORT).show();
                }else{
                    dismiss();
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
                }
            });



    private void loading(){
        popDialog.setCancelable(false);
        popDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.loading_popup));
        popDialog.getWindow().setWindowAnimations(R.style.popupAnimation);
        popDialog.getWindow().setGravity(Gravity.CENTER);
        popDialog.create();
        popDialog.show();
    }
    private void dismiss(){
        popDialog.dismiss();
    }
}