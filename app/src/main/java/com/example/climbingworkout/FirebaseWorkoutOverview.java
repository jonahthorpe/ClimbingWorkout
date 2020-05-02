package com.example.climbingworkout;

import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FirebaseWorkoutOverview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_workout_overview);
        Intent intentGet = getIntent();
        String key = intentGet.getStringExtra("key");

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
                                exercise.getSets() + " sets x " +
                                exercise.getReps() + " reps";

                    }

                    TextView description = findViewById(R.id.workout_description);
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
    }
}
