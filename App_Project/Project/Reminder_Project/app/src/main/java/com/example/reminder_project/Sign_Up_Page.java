package com.example.reminder_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_Up_Page extends AppCompatActivity {

    EditText editLoginID, editPassword, editPasswordCon;
    Button login,signup;
    String key;
    String username;
    String password;
    String passwordCon;

    FirebaseAuth auth;

//    FirebaseDatabase db;
//    DatabaseReference ref;
//    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        editLoginID = findViewById(R.id.editTextTextPersonName2);
        editPassword = findViewById(R.id.editTextTextPassword2);
        editPasswordCon = findViewById(R.id.editTextTextPassword3);

        signup = findViewById(R.id.button3);
        login = findViewById(R.id.button4);

        auth = FirebaseAuth.getInstance();

//        db = FirebaseDatabase.getInstance();
//        ref = db.getReference().child("users");
//        key = ref.push().getKey();

        //clicking the submit button, push the data to firebase
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
                    username = editLoginID.getText().toString().trim();
                    password = editPassword.getText().toString().trim();
                    passwordCon = editPasswordCon.getText().toString().trim();
                    if(!passwordCon.equals(password)){
                        Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
                    } else if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordCon)){
                        Toast.makeText(getApplicationContext(), "Invalid Fields", Toast.LENGTH_SHORT).show();
                    } else {
                        auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Your account has been created", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Sign_Up_Page.this, MainActivity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getApplicationContext(), "There was an error while creating your account, please review your credentials", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
//                    user = new User(username, password, ref.push().getKey());
//                    ref.child(user.getKey()).setValue(user);
                    editLoginID.setText("");
                    editPassword.setText("");
                    editPasswordCon.setText("");
//                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(), " " + e, Toast.LENGTH_SHORT).show();
//                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Sign_Up_Page.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
}