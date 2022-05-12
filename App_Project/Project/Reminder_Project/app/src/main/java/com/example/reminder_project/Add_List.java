package com.example.reminder_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Add_List extends AppCompatActivity implements IconAdapter.ItemClickListener {

    Button submit, cancel;
//    ImageButton btn1,btn2,btn3,btn4,btn5,btn6;
    ImageView imagePrev;
    EditText title;
    Integer icon;

    RecyclerView rv;
    IconAdapter adapter;

    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseUser currentUser;
    ArrayList<List> listList = new ArrayList<>();
    ArrayList<Integer> iconsList = new ArrayList<>();
    List list;

    int maxid = 0;
    ArrayList<String> maxidl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        submit = findViewById(R.id.button10);
        cancel = findViewById(R.id.button9);
        title = findViewById(R.id.editTextTextPersonName3);
        imagePrev = findViewById(R.id.imageView);
        imagePrev.setImageResource(R.drawable.ic_align_right_solid);
        icon = R.drawable.ic_align_right_solid;

        rv = findViewById(R.id.recyclerView2);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(Add_List.this, 4);
        rv.setLayoutManager(layoutManager);
        iconsList.add(R.drawable.ic_align_right_solid);
        iconsList.add(R.drawable.ic_angle_right_solid);
        iconsList.add(R.drawable.ic_arrow_pointer_solid);
        iconsList.add(R.drawable.ic_asterisk_solid);
        iconsList.add(R.drawable.ic_business_time_solid);
        iconsList.add(R.drawable.ic_code_solid);

        adapter = new IconAdapter(iconsList);
        adapter.setClickListener(Add_List.this);
        rv.setAdapter(adapter);

        db = FirebaseDatabase.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = db.getReference().child(currentUser.getUid()).child("lists");

        list = new List();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
//                    maxid = (int) snapshot.getChildrenCount();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        maxid = Integer.parseInt(dataSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Add_List.this, "Please enter a Title", Toast.LENGTH_SHORT).show();
                }else{
                    list.setTitle(title.getText().toString());
                    list.setIcon(icon.toString());

                    ref.child(String.valueOf(maxid+1)).setValue(list);
                    Toast.makeText(getApplicationContext(), "Your list was created", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Add_List.this, Lists_Page.class);
                    i.putExtra("listList", listList);
                    startActivity(i);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Add_List.this, Lists_Page.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        switch(position) {
            case 0:
                icon = R.drawable.ic_align_right_solid;
                break;
            case 1:
                icon = R.drawable.ic_angle_right_solid;
                break;
            case 2:
                icon = R.drawable.ic_arrow_pointer_solid;
                break;
            case 3:
                icon = R.drawable.ic_asterisk_solid;
                break;
            case 4:
                icon = R.drawable.ic_business_time_solid;
                break;
            case 5:
                icon = R.drawable.ic_code_solid;
                break;
        }
        imagePrev.setImageResource(icon);
    }
}