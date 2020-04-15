package com.example.climbingworkout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class WorkoutOverview extends AppCompatActivity {

    private int currentSelection = 0;
    private WorkoutOverviewViewModel mOverviewViewModel;
    private String beginnerText;
    private String intermediateText;
    private String advancedText;
    String text;
    private Intent intent;
    private int beginnerWorkoutId;
    private int intermediateWorkoutId;
    private int advancedWorkoutId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_overview);


        Intent intentP = getIntent();
        intent  = new Intent(this, Workout.class);

        TextView workoutDescription = findViewById(R.id.workoutDescription);
        beginnerText = "Beginner\n";
        intermediateText = "Intermediate\n";
        advancedText = "Advanced\n";

        mOverviewViewModel = new ViewModelProvider(this).get(WorkoutOverviewViewModel.class);
        mOverviewViewModel.getWorkouts(intentP.getIntExtra("cardID", 1)).observe(this, workoutDifficulties ->{
                for (WorkoutDifficulty workoutDifficulty : workoutDifficulties){
                    mOverviewViewModel.getExercises(workoutDifficulty.getWorkoutID()).observe(this, exercises -> {
                        text = "";
                        for (WorkoutExercise exercise : exercises) {
                            Log.i("difficulty", String.valueOf(exercise.getExercise()) + exercise.getPosition());
                            text += "\n" + exercise.getExercise() + " " +
                                    exercise.getSets() + " sets x "  +
                                    exercise.getReps() + " reps";

                        }
                        text += "\n\n" + workoutDifficulty.getDescription();
                        switch (workoutDifficulty.getDifficulty()) {
                            case "Beginner":
                                beginnerText += text;
                                beginnerWorkoutId = workoutDifficulty.getWorkoutID();
                                break;
                            case "Intermediate":
                                intermediateText += text;
                                intermediateWorkoutId = workoutDifficulty.getWorkoutID();
                                break;
                            case "Advanced":
                                advancedText += text;
                                advancedWorkoutId = workoutDifficulty.getWorkoutID();
                                break;
                        }
                        workoutDescription.setText(beginnerText);
                    });
                }
        });

        String imagePath = intentP.getStringExtra("imageName");
        ImageView image = findViewById(R.id.overviewImage);
        int imageResource = getResources().getIdentifier(imagePath, null, "com.example.climbingworkout");
        Drawable res = getResources().getDrawable(imageResource);
        image.setImageDrawable(res);

        Button beginnerButton = findViewById(R.id.beginner_button);
        Button intermediateButton = findViewById(R.id.intermediate_button);
        Button advancedButton = findViewById(R.id.advanced_button);
        Button startButton = findViewById(R.id.start);

        beginnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelection != 0){
                    currentSelection = 0;
                    beginnerButton.setBackgroundColor(getResources().getColor(R.color.topGreen));
                    intermediateButton.setBackgroundColor(getResources().getColor(R.color.defaultGrey));
                    advancedButton.setBackgroundColor(getResources().getColor(R.color.defaultGrey));
                    workoutDescription.setText(beginnerText);

                }
            }
        });
        intermediateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelection != 1) {
                    currentSelection = 1;
                    beginnerButton.setBackgroundColor(getResources().getColor(R.color.defaultGrey));
                    intermediateButton.setBackgroundColor(getResources().getColor(R.color.topGreen));
                    advancedButton.setBackgroundColor(getResources().getColor(R.color.defaultGrey));
                    workoutDescription.setText(intermediateText);
                }
            }
        });
        advancedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelection != 2) {
                    currentSelection = 2;
                    beginnerButton.setBackgroundColor(getResources().getColor(R.color.defaultGrey));
                    intermediateButton.setBackgroundColor(getResources().getColor(R.color.defaultGrey));
                    advancedButton.setBackgroundColor(getResources().getColor(R.color.topGreen));
                    workoutDescription.setText(advancedText);
                }
            }
        });

        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
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
                startActivity(intent);
            }
        });
    }
}
