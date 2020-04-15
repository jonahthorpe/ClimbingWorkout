package com.example.climbingworkout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.List;

public class Workout extends AppCompatActivity {

    private WorkoutViewModel mWorkoutViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        Intent intent = getIntent();
        Log.i("workout", String.valueOf(intent.getIntExtra("workout", 0)));
        VideoView workoutDemoVid = findViewById(R.id.workoutDemo);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.scapular_pull_up_no_sound;
        Uri uri = Uri.parse(videoPath);
        workoutDemoVid.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        workoutDemoVid.setMediaController(mediaController);
        mediaController.setAnchorView(workoutDemoVid);

        mWorkoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
        mWorkoutViewModel.getExerciseVid("Scapular Pull Up").observe(this, exerciseVids -> {
            for (String vid : exerciseVids) {
                Log.i("exercise", vid);
            }

        });

        mWorkoutViewModel.getExercises(intent.getIntExtra("workout", 0)).observe(this, exercises ->{
            WorkoutExercise exercise = exercises.get(0);
            Log.i("exercise",exercise.getExercise());
            exercise = exercises.get(1);
            Log.i("exercise",exercise.getExercise());
            exercise = exercises.get(2);
            Log.i("exercise",exercise.getExercise());
        });
    }
}
