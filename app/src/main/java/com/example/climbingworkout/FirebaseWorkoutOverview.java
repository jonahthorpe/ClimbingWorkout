package com.example.climbingworkout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import java.util.Calendar;
import java.util.List;

public class FirebaseWorkoutOverview extends AppCompatActivity {

    private Calendar calendar;
    private Calendar date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_workout_overview);
        Intent intentGet = getIntent();
        String key = intentGet.getStringExtra("key");
        ProgressBar progressBar = findViewById(R.id.progressBar);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/" + user.getUid() + "/my_workouts/" + key);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.exists()) {
                    FirebaseWorkout workout = dataSnapshot.getValue(FirebaseWorkout.class);
                    TextView title = findViewById(R.id.workout_title);
                    title.setText(workout.getWorkout_name());
                    ArrayList<WorkoutExercise> exercises = workout.getExercises();
                    String text = "";
                    for (WorkoutExercise exercise : exercises) {
                        text += "\n" + exercise.getExercise() + " " +
                                exercise.getSets() + " sets x ";
                        switch(exercise.getRepType()){
                            case 0:
                                text += exercise.getReps() + " reps";
                                break;
                            case 1:
                                text += exercise.getReps() + " seconds";
                                break;
                            case 2:
                                text += exercise.getReps() + ", " + exercise.getRepeaterOn() + " seconds on, " + exercise.getRepeaterOff() + " seconds off";
                                break;
                        }

                    }

                    TextView description = findViewById(R.id.workout_description);
                    progressBar.setVisibility(View.GONE);
                    description.setText(text);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        Button deleteButton = findViewById(R.id.delete);
        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Workout")
                    .setMessage("Are you sure you want to delete this workout?")
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                        myRef.removeValue();
                        finish();
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        });

        Button logButton = findViewById(R.id.logWorkout);
        logButton.setOnClickListener(v -> {
            FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
            if (user1 != null) {
                Log.i("username", user1.getUid());
                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference myRef1 = database1.getReference("users/" + user1.getUid());


                myRef1.child("logged_workouts").push().setValue(new WorkoutLog(key, Calendar.getInstance().getTime(), true));
                Toast.makeText(getApplicationContext(), "Logged",
                        Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Not Logged In",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button start = findViewById(R.id.start);
        start.setOnClickListener(v -> {
            Intent intent = new Intent(this, Workout.class);
            intent.putExtra("myWorkout", true);
            intent.putExtra("workout", key);
            startActivity(intent);
        });

        Button schedule = findViewById(R.id.schedule);


        schedule.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hour1, minute1) -> {
                timePicker.setIs24HourView(true);
                calendar.set(Calendar.HOUR_OF_DAY, hour1);
                calendar.set(Calendar.MINUTE, minute1);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.YEAR, date.get(Calendar.YEAR));
                calendar.set(Calendar.MONTH, date.get(Calendar.MONTH));
                calendar.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
                if (calendar.before(Calendar.getInstance())){
                    Toast.makeText(getApplicationContext(), "Can't schedule for past date",
                            Toast.LENGTH_SHORT).show();
                }else{
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), AlarmReceiverFirebaseWorkout.class);
                    intent.putExtra("key", key);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                        alarmManager.set(AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(), pendingIntent);
                        Toast.makeText(getApplicationContext(), "Scheduled",
                                Toast.LENGTH_SHORT).show();
                }

            }, hour, minute, true);



            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year1, month1, day1) -> {
                date = Calendar.getInstance();
                date.set(Calendar.YEAR, year1);
                date.set(Calendar.MONTH, month1);
                date.set(Calendar.DAY_OF_MONTH, day1);

                timePickerDialog.show();

            }, year, month, day);

            datePickerDialog.show();
            timePickerDialog.setOnCancelListener(dialogInterface ->{
                datePickerDialog.show();
            });


        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("currentScreen", 2);
        startActivity(intent);
        finish();
    }


}
