// app/src/main/java/com/vertex/io/ContactUs.java
package com.vertex.io;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactUs extends AppCompatActivity {

    private EditText nameEditText, emailEditText, messageEditText;
    String UID;
    private Button submitButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        UID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        nameEditText = findViewById(R.id.contact_us_name);
        emailEditText = findViewById(R.id.contact_us_email);
        messageEditText = findViewById(R.id.contact_us_message);
        submitButton = findViewById(R.id.contact_us_submit);

        databaseReference = FirebaseDatabase.getInstance().getReference("ContactUs");

        submitButton.setOnClickListener(v -> submitContactForm());
    }

    private void submitContactForm() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String message = messageEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(message)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = databaseReference.push().getKey();
        ContactForm contactForm = new ContactForm(name, email, message,UID);

        if (id != null) {
            databaseReference.child(id).setValue(contactForm).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ContactUs.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                    nameEditText.setText("");
                    emailEditText.setText("");
                    messageEditText.setText("");
                } else {
                    Toast.makeText(ContactUs.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static class ContactForm {
        public String name;
        public String email;
        public String message;
        public String UID;

        public ContactForm() {
            // Default constructor required for calls to DataSnapshot.getValue(ContactForm.class)
        }

        public ContactForm(String name, String email, String message,String UID) {
            this.name = name;
            this.email = email;
            this.message = message;
            this.UID = UID;
        }
    }
}