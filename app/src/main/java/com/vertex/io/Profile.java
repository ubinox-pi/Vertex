package com.vertex.io;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class Profile extends AppCompatActivity {

    pop_dialog popDialog ;

    ActivityResultLauncher<Intent> launcher=
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                if(result.getResultCode()==RESULT_OK){
                    Uri uri=result.getData().getData();
                    Toast.makeText(this, "Please Wait", Toast.LENGTH_LONG).show();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    String UID = mAuth.getCurrentUser().getUid().toString();
                    String imageFileName = "image_"+UID+".jpg";
                    DatabaseReference Users = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid().toString());
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference imageRef = storageRef.child(imageFileName);
                    imageRef.putFile(Objects.requireNonNull(result.getData().getData())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            Users.child("Url").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    ImageView profile_img = findViewById(R.id.Profile);
                                    File directory = Profile.this.getFilesDir();
                                    File local = new File(directory,"imageFile");
                                    String imageFileName = "image_"+UID+".jpg";
                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference storageRef = storage.getReference().child(imageFileName);
                                    storageRef.getFile(local).addOnSuccessListener(v ->{
                                        File imageFile = local;
                                        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                                        profile_img.setImageBitmap(bitmap);
                                        loading_cancel();
                                    }).addOnFailureListener( v -> {
                                        Toast.makeText(Profile.this,v.getMessage(),Toast.LENGTH_SHORT).show();
                                    });
                                    Toast.makeText(Profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loading_cancel();
                                    Toast.makeText(Profile.this, "Profile Not Updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loading_cancel();
                            Toast.makeText(Profile.this, "Profile Not Updated", Toast.LENGTH_SHORT).show();
                        }
                    });


                }else if(result.getResultCode()==ImagePicker.RESULT_ERROR){
                    loading_cancel();
                    Toast.makeText(this, ImagePicker.Companion.getError(result.getData()), Toast.LENGTH_SHORT).show();
                }else{
                    loading_cancel();
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
                }
            });


    private final int GALLERY_REQ_CODE = 1000;
    File localFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.edit), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        popDialog = new pop_dialog(this);

        ImageView profile = findViewById(R.id.Profile);
        profile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            ImagePicker.Companion.with(this)
                    .setMultipleAllowed(false)
                    .provider(ImageProvider.BOTH)
                    .createIntentFromDialog((Function1)(new Function1(){
                        public Object invoke(Object var1){
                            this.invoke((Intent)var1);
                            return Unit.INSTANCE;
                        }
                        public final void invoke(@NotNull Intent it){
                            Intrinsics.checkNotNullParameter(it,"it");
                            launcher.launch(it);
                            loading();
                        }
                    }));
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid().toString();
        FirebaseDatabase mDB = FirebaseDatabase.getInstance();
        DatabaseReference Users = FirebaseDatabase.getInstance().getReference("Users").child(UID);



        @SuppressLint("CutPasteId") ImageView profile_img = findViewById(R.id.Profile);
        Users.child("Url").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    File directory = Profile.this.getFilesDir();
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
                                profile_img.setImageBitmap(bitmap);
                            }).addOnFailureListener( v -> {
                                Toast.makeText(Profile.this,v.getMessage(),Toast.LENGTH_SHORT).show();
                            });
                        } else if (local.exists()) {
                            File imageFile = local;
                            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                            profile_img.setImageBitmap(bitmap);
                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(Profile.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Context context = this;

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });


        ImageView edit = findViewById(R.id.Edit);
        ImageView edit2 = findViewById(R.id.Edit2);
        ImageView edit3 = findViewById(R.id.Edit3);
        edit.setOnClickListener(v -> {
            Toast.makeText(context, "Contact Customer Care To Edit These", Toast.LENGTH_SHORT).show();
        });

        edit2.setOnClickListener(v -> {
            Toast.makeText(context, "Contact Customer Care To Edit These", Toast.LENGTH_SHORT).show();
        });

        edit3.setOnClickListener(v -> {
            Toast.makeText(context, "Contact Customer Care To Edit These", Toast.LENGTH_SHORT).show();
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid().toString();
        FirebaseDatabase mDB = FirebaseDatabase.getInstance();
        DatabaseReference Users = FirebaseDatabase.getInstance().getReference("Users").child(UID);
        EditText uid = findViewById(R.id.UID);
        EditText name = findViewById(R.id.Name);
        EditText gmail = findViewById(R.id.Gmail);
        EditText phone = findViewById(R.id.Phone);
        uid.setText(mAuth.getCurrentUser().getUid().toString());

        Users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Name,Gmail,Phone;
                Name = snapshot.child("name").getValue(String.class);
                Gmail = snapshot.child("email").getValue(String.class);
                Phone = snapshot.child("phone").getValue(String.class);
                name.setText(Name);
                gmail.setText(Gmail);
                phone.setText("+91"+Phone);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void loading(){
        popDialog.setCancelable(false);
        popDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.loading_popup));
        popDialog.getWindow().setWindowAnimations(R.style.popupAnimation);
        popDialog.getWindow().setGravity(Gravity.CENTER);
        popDialog.create();
        popDialog.show();
    }

    public void loading_cancel(){
        popDialog.cancel();
    }
}