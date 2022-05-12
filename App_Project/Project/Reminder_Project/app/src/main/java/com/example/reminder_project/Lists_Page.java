package com.example.reminder_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Lists_Page extends AppCompatActivity implements ItemClickListener{

    TextView user;
    Button logout, addList;

    RecyclerView rv;
    ListAdapter adapter;
    ArrayList<com.example.reminder_project.List> list = new ArrayList<>();

    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseAuth auth;
    FirebaseUser currentUser;

    ArrayList<String> maxid = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists_page);
        user = findViewById(R.id.textView6);
        logout = findViewById(R.id.button7);
        addList = findViewById(R.id.button8);

        db = FirebaseDatabase.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = db.getReference().child(currentUser.getUid()).child("lists");
        user.setText(currentUser.getEmail());

        auth = FirebaseAuth.getInstance();

        rv = findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                Lists_Page.this, LinearLayoutManager.VERTICAL,false);
        rv.addItemDecoration(new DividerItemDecoration(rv.getContext(),LinearLayoutManager.VERTICAL));
        rv.setLayoutManager(layoutManager);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                maxid.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String title = dataSnapshot.getValue(List.class).getTitle();
                    String icon = dataSnapshot.getValue(List.class).getIcon();
                    list.add(new List(icon, title));
                    maxid.add(dataSnapshot.getKey());
                }
                adapter = new ListAdapter((ArrayList<List>) list);
                rv.setAdapter(adapter);
                adapter.setClickListener(Lists_Page.this);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Lists_Page.this,"Error:" + error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent i = new Intent(Lists_Page.this, MainActivity.class);
                startActivity(i);
            }
        });

        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Lists_Page.this, Add_List.class);
                startActivity(i);
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(Lists_Page.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
//                Toast.makeText(Lists_Page.this, "on Swiped ", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(Lists_Page.this);
                builder.setMessage("Are you sure you want to delete this 'List'?\nThis will permanently remove it from your list");
                builder.setTitle("Alert !");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int position = viewHolder.getAdapterPosition();
                        ref.child(maxid.get(position)).removeValue();
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.notifyDataSetChanged();
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rv);
    }

    @Override
    public void onClick(View view, int position) {
        final List lists = list.get(position);
        Intent i = new Intent(this, Tasks_Page.class);
        i.putExtra("ID", maxid.get(position));
        i.putExtra("title", lists.title);
        startActivity(i);
    }
}