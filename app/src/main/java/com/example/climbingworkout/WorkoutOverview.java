package com.example.climbingworkout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class WorkoutOverview extends AppCompatActivity {

    private int currentSelection;
    private WorkoutOverviewViewModel mOverviewViewModel;
    private String text;
    private Intent intent;
    private int beginnerWorkoutId;
    private int intermediateWorkoutId;
    private int advancedWorkoutId;
    private Calendar calendar;
    private Calendar date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_overview);


        Intent intentP = getIntent();
        currentSelection = intentP.getIntExtra("difficultySelection", 0);
        intent  = new Intent(this, Workout.class);

        Button beginnerButton = findViewById(R.id.beginner_button);
        Button intermediateButton = findViewById(R.id.intermediate_button);
        Button advancedButton = findViewById(R.id.advanced_button);
        Button startButton = findViewById(R.id.start);
        Button logButton = findViewById(R.id.logWorkout);
        TextView beginnerDescription = findViewById(R.id.beginner_workout_description);
        TextView intermediateDescription = findViewById(R.id.intermediate_workout_description);
        TextView advancedDescription = findViewById(R.id.advanced_workout_description);
        switch(currentSelection){
            case 0:
                beginnerButton.setBackgroundColor(getResources().getColor(R.color.topGreen));
                beginnerDescription.setVisibility(View.VISIBLE);
                intermediateDescription.setVisibility(View.INVISIBLE);
                advancedDescription.setVisibility(View.INVISIBLE);
                break;
            case 1:
                intermediateButton.setBackgroundColor(getResources().getColor(R.color.topGreen));
                beginnerDescription.setVisibility(View.INVISIBLE);
                intermediateDescription.setVisibility(View.VISIBLE);
                advancedDescription.setVisibility(View.VISIBLE);
                break;
            case 2:
                advancedButton.setBackgroundColor(getResources().getColor(R.color.topGreen));
                beginnerDescription.setVisibility(View.INVISIBLE);
                intermediateDescription.setVisibility(View.INVISIBLE);
                advancedDescription.setVisibility(View.VISIBLE);
                break;
        }
        mOverviewViewModel = new ViewModelProvider(this).get(WorkoutOverviewViewModel.class);
        mOverviewViewModel.getWorkouts(intentP.getIntExtra("cardID", 1)).observe(this, workoutDifficulties ->{
            beginnerDescription.setText("");
            intermediateDescription.setText("");
            advancedDescription.setText("");
            for (WorkoutDifficulty workoutDifficulty : workoutDifficulties){
                mOverviewViewModel.getExercises(workoutDifficulty.getWorkoutID()).observe(this, exercises -> {
                    for (WorkoutExercise exercise : exercises) {
                        text = "";
                        text += "\n" + exercise.getExercise() + " " +
                                exercise.getSets() + " sets x " ;
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

                        switch (workoutDifficulty.getDifficulty()) {
                            case "Beginner":
                                beginnerDescription.append(text);
                                break;
                            case "Intermediate":
                                intermediateDescription.append(text);
                                break;
                            case "Advanced":
                                advancedDescription.append(text);
                                break;
                        }

                    }
                });

                switch (workoutDifficulty.getDifficulty()) {
                    case "Beginner":
                        beginnerDescription.append("\n" + workoutDifficulty.getDescription() +  "\n" );
                        beginnerWorkoutId = workoutDifficulty.getWorkoutID();
                        break;
                    case "Intermediate":
                        intermediateDescription.append("\n" + workoutDifficulty.getDescription() + "\n" );
                        intermediateWorkoutId = workoutDifficulty.getWorkoutID();
                        break;
                    case "Advanced":
                        advancedDescription.append("\n" + workoutDifficulty.getDescription() + "\n" );
                        advancedWorkoutId = workoutDifficulty.getWorkoutID();
                        break;
                }
            }
        });

        String imagePath = intentP.getStringExtra("imageName");
        ImageView image = findViewById(R.id.overviewImage);
        int imageResource = getResources().getIdentifier(imagePath, null, "com.example.climbingworkout");
        Drawable res = getResources().getDrawable(imageResource);
        image.setImageDrawable(res);


        beginnerButton.setOnClickListener(v -> {
            if (currentSelection != 0){
                currentSelection = 0;
                beginnerButton.setBackgroundColor(getResources().getColor(R.color.topGreen));
                intermediateButton.setBackgroundColor(getResources().getColor(R.color.defaultGrey));
                advancedButton.setBackgroundColor(getResources().getColor(R.color.defaultGrey));
                beginnerDescription.setVisibility(View.VISIBLE);
                intermediateDescription.setVisibility(View.INVISIBLE);
                advancedDescription.setVisibility(View.INVISIBLE);



            }
        });
        intermediateButton.setOnClickListener(v -> {
            if (currentSelection != 1) {
                currentSelection = 1;
                beginnerButton.setBackgroundColor(getResources().getColor(R.color.defaultGrey));
                intermediateButton.setBackgroundColor(getResources().getColor(R.color.topGreen));
                advancedButton.setBackgroundColor(getResources().getColor(R.color.defaultGrey));
                beginnerDescription.setVisibility(View.INVISIBLE);
                intermediateDescription.setVisibility(View.VISIBLE);
                advancedDescription.setVisibility(View.INVISIBLE);
            }
        });
        advancedButton.setOnClickListener(v -> {
            if (currentSelection != 2) {
                currentSelection = 2;
                beginnerButton.setBackgroundColor(getResources().getColor(R.color.defaultGrey));
                intermediateButton.setBackgroundColor(getResources().getColor(R.color.defaultGrey));
                advancedButton.setBackgroundColor(getResources().getColor(R.color.topGreen));
                beginnerDescription.setVisibility(View.INVISIBLE);
                intermediateDescription.setVisibility(View.INVISIBLE);
                advancedDescription.setVisibility(View.VISIBLE);
            }
        });



        startButton.setOnClickListener(v -> {
            switch(currentSelection){
                case 0:
                    intent.putExtra("workout", beginnerWorkoutId);
                    break;
                case 1:
                    intent.putExtra("workout", intermediateWorkoutId);
                    break;
                case 2:
                    intent.putExtra("workout", advancedWorkoutId);
                    break;
            }
            intent.putExtra("myWorkout", false);
            startActivity(intent);
        });

        logButton.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users/" + user.getUid());

                int id = 0;
                switch(currentSelection){
                    case 0:
                        id = beginnerWorkoutId;
                        break;
                    case 1:
                        id = intermediateWorkoutId;
                        break;
                    case 2:
                        id = advancedWorkoutId;
                        break;
                }

                myRef.child("logged_workouts").push().setValue(new WorkoutLog(String.valueOf(id), Calendar.getInstance().getTime(), false));
                Toast.makeText(getApplicationContext(), "Logged",
                        Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Not Logged In",
                        Toast.LENGTH_SHORT).show();
            }
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
                    Intent intent = new Intent(getApplicationContext(), AlarmReceiverWorkout.class);
                    int cardID = intentP.getIntExtra("cardID", 1);
                    intent.putExtra("cardId", cardID);
                    intent.putExtra("difficultySelection", currentSelection);
                    intent.putExtra("imageName", intentP.getStringExtra("imageName"));
                    intent.putExtra("notificationId", Integer.valueOf(String.valueOf(cardID) + String.valueOf(currentSelection)));
                    mOverviewViewModel.getCard(cardID).observe(this, card -> {
                        intent.putExtra("workoutTitle", card.getWorkoutTitle());
                        intent.putExtra("workoutCategory", card.getWorkoutCategory());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                        alarmManager.set(AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(), pendingIntent);
                        Toast.makeText(getApplicationContext(), "Scheduled",
                                Toast.LENGTH_SHORT).show();
                    });
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

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }



}
