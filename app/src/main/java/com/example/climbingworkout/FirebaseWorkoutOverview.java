package com.example.climbingworkout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
        if (user != null) {
            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users/" + user.getUid() + "/my_workouts/" + key);

            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    progressBar.setVisibility(View.GONE);
                    if (dataSnapshot.exists()) {
                        FirebaseWorkout workout = dataSnapshot.getValue(FirebaseWorkout.class);
                        TextView title = findViewById(R.id.workout_title);
                        title.setText(workout.getWorkout_name());
                        ArrayList<WorkoutExercise> exercises = workout.getExercises();
                        StringBuilder text = new StringBuilder();
                        for (WorkoutExercise exercise : exercises) {
                            text.append("\n").append(exercise.getExercise()).append(" ").append(exercise.getSets()).append(" sets x ");
                            switch (exercise.getRepType()) {
                                case 0:
                                    text.append(exercise.getReps()).append(" reps");
                                    break;
                                case 1:
                                    text.append(exercise.getReps()).append(" seconds");
                                    break;
                                case 2:
                                    text.append(exercise.getReps()).append(", ").append(exercise.getRepeaterOn()).append(" seconds on, ").append(exercise.getRepeaterOff()).append(" seconds off");
                                    break;
                            }

                        }

                        TextView description = findViewById(R.id.workout_description);
                        description.setText(text.toString());
                    } else {
                        LinearLayout workout_not_found = findViewById(R.id.workout_not_found_message_container);
                        workout_not_found.setVisibility(View.VISIBLE);
                        Button button = findViewById(R.id.go_to_my_workouts);
                        button.setOnClickListener(view ->{
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            intent.putExtra("currentScreen", 2);
                            startActivity(intent);
                            finish();
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value

                }
            });

            Button deleteButton = findViewById(R.id.delete);
            deleteButton.setOnClickListener(v -> new AlertDialog.Builder(this)
                    .setTitle("Delete Workout")
                    .setMessage("Are you sure you want to delete this workout?")
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                        myRef.removeValue();
                        finish();
                    })
                    .setNegativeButton(android.R.string.no, null).show());

            Button logButton = findViewById(R.id.logWorkout);
            logButton.setOnClickListener(v -> {
                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                if (user1 != null) {
                    FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                    DatabaseReference myRef1 = database1.getReference("users/" + user1.getUid());


                    myRef1.child("logged_workouts").push().setValue(new WorkoutLog(key, Calendar.getInstance().getTime(), true));
                    Toast.makeText(getApplicationContext(), "Logged",
                            Toast.LENGTH_SHORT).show();
                } else {
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
                    if (calendar.before(Calendar.getInstance())) {
                        Toast.makeText(getApplicationContext(), "Can't schedule for past date",
                                Toast.LENGTH_SHORT).show();
                    } else {
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
                timePickerDialog.setOnCancelListener(dialogInterface -> datePickerDialog.show());

            });
        }else{
            LinearLayout not_logged_in = findViewById(R.id.logged_in_message_container);
            not_logged_in.setVisibility(View.VISIBLE);
            Button button = findViewById(R.id.go_to_log_in);
            button.setOnClickListener(view ->{
                Intent intent = new Intent(this, LogIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            });
        }

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
