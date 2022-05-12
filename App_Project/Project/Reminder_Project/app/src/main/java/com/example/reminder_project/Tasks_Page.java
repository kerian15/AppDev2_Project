package com.example.reminder_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tasks_Page extends AppCompatActivity {

    TextView user, listTitle;
    Button back, addTask;
    RecyclerView rv;
    String id;

    TaskAdapter adapter;
    ArrayList<Task> task = new ArrayList<>();

    FirebaseDatabase db;
    static DatabaseReference ref;
    FirebaseAuth auth;
    FirebaseUser currentUser;

    static ArrayList<String> maxid = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_page);

        back = findViewById(R.id.button5);
        listTitle = findViewById(R.id.textView3);
        user = findViewById(R.id.textView4);
        rv = findViewById(R.id.recyclerView3);
        addTask = findViewById(R.id.button6);

//        adapter = new TaskAdapter(task);
//        adapter.setClickListener(Tasks_Page.this);
//        rv.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                Tasks_Page.this, LinearLayoutManager.VERTICAL,false);
        rv.addItemDecoration(new DividerItemDecoration(rv.getContext(),LinearLayoutManager.VERTICAL));
        rv.setLayoutManager(layoutManager);

        Intent i = getIntent();
        listTitle.setText(i.getStringExtra("title"));

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        user.setText(currentUser.getEmail());
//        if(id == null){
            id = i.getStringExtra("ID");
//        }
        //i.getStringExtra("ID")

        db = FirebaseDatabase.getInstance();
        ref = db.getReference().child(currentUser.getUid()).child("lists").child(id).child("tasks");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                task.clear();
                maxid.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String name= dataSnapshot.getValue(Task.class).getName();
                    String note= dataSnapshot.getValue(Task.class).getNote();
                    String date= dataSnapshot.getValue(Task.class).getDate();
                    String time= dataSnapshot.getValue(Task.class).getTime();
                    String repeat= dataSnapshot.getValue(Task.class).getRepeat();
                    String priority= dataSnapshot.getValue(Task.class).getPriority();
                    task.add(new Task(name, note, date, time, repeat, priority));
                    maxid.add(dataSnapshot.getKey());
                }
                adapter = new TaskAdapter((ArrayList<Task>) task);
                rv.setAdapter(adapter);
//                adapter.setClickListener(Tasks_Page.this);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Tasks_Page.this,"Error:" + error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Tasks_Page.this, Add_Task.class);
                i.putExtra("ID", id);
                startActivity(i);
//                ref.child(String.valueOf(maxid+1)).setValue(list);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Tasks_Page.this, Lists_Page.class);
                startActivity(i);
            }
        });
    }
}