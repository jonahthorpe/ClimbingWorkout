package com.example.climbingworkout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;


public class AddWorkout extends AppCompatActivity {

    private AddExerciseForm exerciseForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);



        LinearLayout container = findViewById(R.id.form_container);
        EditText workoutName = findViewById(R.id.workout_name);
        EditText exerciseNumber = findViewById(R.id.exercise_number);
        Button load_form_button = findViewById(R.id.load_form);
        load_form_button.setOnClickListener(v -> {
            String amountString = exerciseNumber.getText().toString();
            if (amountString.length() != 0){
                Integer amount = Integer.valueOf(amountString);
                if (amount == 0){
                    Toast.makeText(getApplicationContext(), "Can't have 0 exercises",
                            Toast.LENGTH_SHORT).show();
                }else if (amount > 20){
                    Toast.makeText(getApplicationContext(), "Can't have more than 20 exercises",
                            Toast.LENGTH_SHORT).show();
                }else {
                    container.removeAllViews();
                    exerciseForm = new AddExerciseForm();
                    exerciseForm.createAddExerciseForm(container, getApplicationContext(), amount);
                }
            }

        });

        Button addWorkout = findViewById(R.id.add);
        addWorkout.setOnClickListener(v -> {
            if (exerciseForm != null) {
                Boolean validName = true;
                if (workoutName.getText().toString().length() == 0){
                    workoutName.setError("Enter a name");
                    workoutName.setBackgroundResource(R.drawable.error_edit_text);
                    validName = false;
                }else if(workoutName.getText().toString().length() > 25){
                    workoutName.setError("Name limited to 25 characters");
                    workoutName.setBackgroundResource(R.drawable.error_edit_text);
                    validName = false;
                }else{
                    workoutName.setBackgroundResource(R.drawable.normal_edit_text);
                }
                if (exerciseForm.checkInputsValid(true) && validName){
                    ArrayList<WorkoutExercise> exercises = new ArrayList<>();
                    exercises = exerciseForm.getExercises(exercises, 1);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    // Write a message to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    assert user != null;
                    DatabaseReference myRef = database.getReference("users/" + user.getUid());

                    myRef.child("my_workouts").push().setValue(new FirebaseWorkout(workoutName.getText().toString(),
                                                                exercises));
                    finish();
                }
            }
        });


        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> finish());
    }

}
