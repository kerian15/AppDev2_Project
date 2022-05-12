package com.example.reminder_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText editLoginID, editPassword;
    Button login,signup;
    String username;
    String password;
    TextView quotet, author;

    FirebaseAuth auth;

    FirebaseDatabase db;
    DatabaseReference ref;
    String key;
    Random random = new Random();

    private List<Quote> quoteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editLoginID = findViewById(R.id.editTextTextPersonName);
        editPassword = findViewById(R.id.editTextTextPassword);
        quotet = findViewById(R.id.textView17);
        author = findViewById(R.id.textView18);
        login = findViewById(R.id.button);
        signup = findViewById(R.id.button2);
        auth = FirebaseAuth.getInstance();

        db = FirebaseDatabase.getInstance();
        ref = db.getReference().child("user");
        key = ref.push().getKey();

        getQuotes();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Sign_Up_Page.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = editLoginID.getText().toString().trim();
                password = editPassword.getText().toString().trim();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), "Invalid Fields", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MainActivity.this, Lists_Page.class);
                                i.putExtra("ID", username);
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), "Error, please review your credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user!=null){
            Intent i = new Intent(MainActivity.this, Lists_Page.class);
            FirebaseUser cuser = FirebaseAuth.getInstance().getCurrentUser();
            i.putExtra("ID", cuser.getEmail());
            startActivity(i);
        }
    }

    private void getQuotes(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);
        Call<List<Quote>> call = api.getQuotes();
        call.enqueue(new Callback<List<Quote>>() {
            @Override
            public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {
                List<Quote> quotesList = response.body();
                Quote quote;
                for (int i = 0; i < quotesList.size(); i++) {
                    quote = new Quote(quotesList.get(i).getText(),
                            quotesList.get(i).getAuthor());
                    quoteList.add(quote);
                }
                int i = random.nextInt(quoteList.size());
                quotet.setText(quoteList.get(i).getText());
                author.setText(quoteList.get(i).getAuthor());
            }

            @Override
            public void onFailure(Call<List<Quote>> call, Throwable t) {

            }
        });
    }
}