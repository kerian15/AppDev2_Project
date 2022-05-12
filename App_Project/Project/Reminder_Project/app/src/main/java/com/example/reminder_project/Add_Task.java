package com.example.reminder_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Add_Task extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    FloatingActionButton fabCalendar, fabTime, fabRepeat, fabPriority;
    CalendarView calendar;
    String date;
    TimePicker time;
    String mtime;
    Button cancelBtn, doneBtn;
    EditText name, note;
    boolean visibleCal, visibleTime, visibleRep, visiblePrio;
    Spinner spinRep, spinPrio;
    String text, text2;
    ArrayAdapter<CharSequence> arrayAdapter1, arrayAdapter2;
    Task task;
    String id;

    int calyear, calmonth, calday, calhour, calmin;
    Calendar calInstance = Calendar.getInstance();
    public static String NOTIFICATION_CHANNEL_ID = "10001";
    private static String default_notification_channel_id = "default";

    FirebaseDatabase db;
    FirebaseUser currentUser;
    DatabaseReference ref;

    int maxid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        cancelBtn = findViewById(R.id.button12);
        doneBtn = findViewById(R.id.button11);
        name = findViewById(R.id.editTextTextPersonName4);
        note = findViewById(R.id.editTextTextPersonName5);
        fabCalendar = findViewById(R.id.floatingActionButton);
        fabTime = findViewById(R.id.floatingActionButton2);
        fabRepeat = findViewById(R.id.floatingActionButton3);
        fabPriority = findViewById(R.id.floatingActionButton4);
        calendar = findViewById(R.id.calendarView);
        time = findViewById(R.id.timePicker);
        spinRep = findViewById(R.id.spinner);
        spinPrio = findViewById(R.id.spinner2);
        visibleCal = false;
        visibleTime = false;
        visibleRep = false;
        visiblePrio = false;

        spinPrio.setVisibility(View.GONE);
        spinRep.setVisibility(View.GONE);
        calendar.setVisibility(View.GONE);
        time.setVisibility(View.GONE);

        task = new Task();

        Intent i = getIntent();
        id = i.getStringExtra("ID");
        db = FirebaseDatabase.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = db.getReference().child(currentUser.getUid()).child("lists").child(id).child("tasks");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        maxid = Integer.parseInt(dataSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fabCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!visibleCal) {
                    calendar.setVisibility(View.VISIBLE);
                    visibleCal = true;
                    fabCalendar.setImageResource(android.R.drawable.dialog_holo_light_frame);
                }else{
                    visibleCal = false;
                    calendar.setVisibility(View.GONE);
                    fabCalendar.setImageResource(android.R.drawable.ic_input_add);
                }
            }
        });

        fabTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!visibleTime) {
                    time.setVisibility(View.VISIBLE);
                    visibleTime = true;
                    fabTime.setImageResource(android.R.drawable.dialog_holo_light_frame);
                }else{
                    visibleTime = false;
                    time.setVisibility(View.GONE);
                    fabTime.setImageResource(android.R.drawable.ic_input_add);
                }
            }
        });

        fabRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!visibleRep) {
                    spinRep.setVisibility(View.VISIBLE);
                    visibleRep = true;
                    fabRepeat.setImageResource(android.R.drawable.dialog_holo_light_frame);
                }else{
                    visibleRep = false;
                    spinRep.setVisibility(View.GONE);
                    fabRepeat.setImageResource(android.R.drawable.ic_input_add);
                }
            }
        });

        fabPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!visiblePrio) {
                    spinPrio.setVisibility(View.VISIBLE);
                    visiblePrio = true;
                    fabPriority.setImageResource(android.R.drawable.dialog_holo_light_frame);
                }else{
                    visiblePrio = false;
                    spinPrio.setVisibility(View.GONE);
                    fabPriority.setImageResource(android.R.drawable.ic_input_add);
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Add_Task.this, Tasks_Page.class);
                Intent y = getIntent();
                i.putExtra("ID", y.getStringExtra("ID"));
                startActivity(i);
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                date = dayOfMonth + "/" + (month + 1) + "/" + year;
                Toast.makeText(Add_Task.this, date, Toast.LENGTH_SHORT).show();
                calyear = year;
                calmonth = month;
                calday = dayOfMonth;
            }
        });

        time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int min) {
                calhour = hour;
                calmin = min;

                String timeStamp;
                if(hour <= 12){
                    timeStamp = " AM";
                } else {
                    timeStamp = " PM";
                }
                mtime = hour + ":" + min + timeStamp;
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Add_Task.this, "Please enter a Task Name", Toast.LENGTH_SHORT).show();
                }else{
                    task.setName(name.getText().toString());

                    if(note.getText().toString().trim().isEmpty()) {
                        task.setNote("");
                    } else {
                        task.setNote(note.getText().toString());
                    }

                    if(date == null || date.isEmpty()) {
                        task.setDate("");
                    } else {
                        task.setDate(date);
                    }

                    if (mtime == null || mtime.isEmpty()) {
                        task.setTime("");
                    } else {
                        task.setTime(mtime);
                    }

                    if (text == null || text.isEmpty() || text.equals("Never")){
                        task.setRepeat("");
                    } else {
                        task.setRepeat(text);
                    }

                    if (text2 == null || text2.isEmpty() || text2.equals("None")) {
                        task.setPriority("");
                    } else {
                        task.setPriority(text2);
                    }

                    Toast.makeText(getApplicationContext(), "Notification set for: "+ calday +"/"+ (calmonth) +"/"+ calyear, Toast.LENGTH_SHORT).show();
                    scheduleNotification();

                    ref.child(String.valueOf(maxid+1)).setValue(task);
                    Intent i = new Intent(Add_Task.this, Tasks_Page.class);
                    i.putExtra("ID", id);
                    startActivity(i);
                }
            }
        });

        arrayAdapter1 = ArrayAdapter.createFromResource(
                this, R.array.repeat, android.R.layout.simple_spinner_item);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinRep.setAdapter(arrayAdapter1);
        spinRep.setOnItemSelectedListener(this);

        arrayAdapter2 = ArrayAdapter.createFromResource(
                this, R.array.priority, android.R.layout.simple_spinner_item);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPrio.setAdapter(arrayAdapter2);
        spinPrio.setOnItemSelectedListener(this);
    }

    private void scheduleNotification() {
        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, getNotification(name.getText().toString()));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;

        calInstance.clear();
        calInstance.set(calyear,calmonth,calday,calhour,calmin);
        long far = calInstance.getTimeInMillis();
        long current = Calendar.getInstance().getTimeInMillis();
        long diff = far - current;

//        Toast.makeText(getApplicationContext(), "" + diff, Toast.LENGTH_SHORT).show();

//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, diff, AlarmManager.INTERVAL_DAY, pendingIntent);

        switch(spinRep.getSelectedItem().toString()) {
            case "None":
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, diff, pendingIntent);
                break;
            case "Daily":
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, diff, AlarmManager.INTERVAL_DAY, pendingIntent);
                break;
            case "Weekly":
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, diff, AlarmManager.INTERVAL_DAY * 7, pendingIntent);
                break;
            case "Bi-Weekly":
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, diff, AlarmManager.INTERVAL_DAY * 14, pendingIntent);
                break;
            case "Monthly":
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, diff, AlarmManager.INTERVAL_DAY * 30, pendingIntent);
                break;
            case "Yearly":
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, diff, AlarmManager.INTERVAL_DAY * 365, pendingIntent);
                break;
            default:
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, diff, pendingIntent);
                break;
        }
    }

    private Notification getNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_channel_id);
        builder.setContentTitle("Reminder!");
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()) {
            case R.id.spinner:
                text = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(),text, Toast.LENGTH_SHORT).show();
                break;
            case R.id.spinner2:
                text2 = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(),text2, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //nothing is here on
    }
}