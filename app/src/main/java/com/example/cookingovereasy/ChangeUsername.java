package com.example.cookingovereasy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChangeUsername extends AppCompatActivity {

    EditText editUsername;
    Button update;
    Button back;
    FirebaseAuth mAuth;
    TextView textView;
    FirebaseFirestore db;
    FirebaseUser user;

    DocumentReference docref;
    FirebaseFirestore ff = FirebaseFirestore.getInstance();

    /* onCreate: sets up change username activity and creates on click listeners for the update
    and back button */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //user = mAuth.getInstance();
        setContentView(R.layout.activity_change_username);

        editUsername = findViewById(R.id.newUsername);
        update = findViewById(R.id.updateBtn);
        back = findViewById(R.id.backBtn);
        //getSupportActionBar().setTitle("hello test");

        update.setOnClickListener(new View.OnClickListener() { //button listener for update btn
            @Override
            public void onClick(View view) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                db = FirebaseFirestore.getInstance();
                Toast.makeText(getApplicationContext(), "Successfully changed username.",
                        Toast.LENGTH_SHORT).show();

                DocumentReference df = db.collection("Users").document(user.getUid());
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("Username", editUsername.getText().toString());
                df.set(userInfo);

                finish();

            }
        });

        back.setOnClickListener((new View.OnClickListener() { //button listener for back btn
            @Override
            public void onClick(View view) {
                finish();
            }
        }));
    }
}