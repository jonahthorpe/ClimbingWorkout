package com.example.climbingworkout;

import androidx.annotation.RawRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Workout extends AppCompatActivity {

    private WorkoutViewModel mWorkoutViewModel;
    private CountDownTimer timer;
    private long remainingTime;
    private ImageButton playButton;
    private long timeLeft;
    private TextView counter;
    private Boolean playing = false;
    private long endTime;
    private TextView exerciseTitle;
    private TextView reps;
    private TextView sets;
    private String test = null;
    private int set = 1;
    private Intent intent;
    private int currentExercise = 0;
    private VideoView workoutDemoVid;
    private ImageView previousExercise;
    private ImageView previousSet;
    private ImageView nextExercise;
    private ImageView nextSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        intent = getIntent();




        mWorkoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);


        playButton = findViewById(R.id.playButton);
        playButton.setBackgroundResource(android.R.drawable.ic_media_play);
        playButton.setOnClickListener(v -> {
            if (playing){
                playing = false;
                playButton.setBackgroundResource(android.R.drawable.ic_media_play);
                pauseTimer();
            }else{
                playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
                playing = true;
                startTimer();
            }
        });

        previousExercise = findViewById(R.id.previousExercise);
        nextExercise = findViewById(R.id.nextExercise);
        previousSet = findViewById(R.id.previousSet);
        nextSet = findViewById(R.id.nextSet);

        workoutDemoVid = findViewById(R.id.workoutDemo);
        MediaController mediaController = new MediaController(this);
        workoutDemoVid.setMediaController(mediaController);
        mediaController.setAnchorView(workoutDemoVid);
        exerciseTitle = findViewById(R.id.exerciseTitle);
        reps = findViewById(R.id.reps);
        sets = findViewById(R.id.sets);
        counter = findViewById(R.id.counter);

        updateUI();

    }

    private void startTimer() {
        endTime = System.currentTimeMillis() + timeLeft;
        timer = new CountDownTimer(timeLeft, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                long secondsRemaining = timeLeft / 1000;
                double minutes = secondsRemaining / 60;
                double seconds = secondsRemaining % 60;
                counter.setText( (int) minutes + ":" + String.format("%02d", (int) seconds));
            }

            public void onFinish() {
                playButton.setBackgroundResource(android.R.drawable.ic_media_play);
                playing = false;
                set += 1;
                counter.setText("0:00");
                test = null;
                updateUI();
            }

        }.start();
    }

    private void pauseTimer(){
        timer.cancel();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("timeLeft", timeLeft);
        outState.putBoolean("playing", playing);
        outState.putLong("endTime", endTime);
        outState.putInt("currentExercise", currentExercise);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        timeLeft = savedInstanceState.getLong("timeLeft");
        playing = savedInstanceState.getBoolean("playing");
        currentExercise = savedInstanceState.getInt("currentExercise");

        test = "a";
        if (playing) {
            endTime = savedInstanceState.getLong("endTime");
            timeLeft = endTime - System.currentTimeMillis() ;
            startTimer();
            playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
        }
    }

    protected LifecycleOwner getLifecycleOwner(){
        return this;
    }

    protected void testfunc(List<WorkoutExercise> exercises){
        WorkoutExercise exercise = exercises.get(currentExercise);
        if (set > exercise.getSets()){
            currentExercise += 1;
            if (currentExercise >= exercises.size()){
                Intent moveToCompleted = new Intent(getApplicationContext(), WorkoutComplete.class);
                startActivity(moveToCompleted);
            }else {
                set = 1;
                exercise = exercises.get(currentExercise);
            }
        }

        if (set == 1){
            previousSet.setColorFilter(Color.argb(150,200,200,200));
            nextSet.setColorFilter(null);
        }else if(set == exercise.getSets()){
            nextSet.setColorFilter(Color.argb(150,200,200,200));
            previousSet.setColorFilter(null);
        }else{
            nextSet.setColorFilter(null);
            previousSet.setColorFilter(null);
        }

        if (currentExercise == 0){
            previousExercise.setColorFilter(Color.argb(150,200,200,200));
            nextExercise.setColorFilter(null);
        }else if(currentExercise == (exercises.size()-1)){
            nextExercise.setColorFilter(Color.argb(150,200,200,200));
            previousExercise.setColorFilter(null);
        }else{
            nextExercise.setColorFilter(null);
            previousExercise.setColorFilter(null);
        }

        exerciseTitle.setText(exercise.getExercise());
        reps.setText("Reps: " + exercise.getReps());
        sets.setText("Sets: "+ set +"/" + exercise.getSets());


        mWorkoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
        mWorkoutViewModel.getExerciseVid(exercise.getExercise()).observe(this, exerciseVids -> {
            int id = getResources().getIdentifier(exerciseVids.get(0), "raw", getPackageName());
            String videoPath = "android.resource://" + getPackageName() + "/" + id;
            Uri uri = Uri.parse(videoPath);
            workoutDemoVid.setVideoURI(uri);
        });


        if(test == null){
            long restTime = exercise.getRest();
            double minutes = restTime / 60;
            double seconds = restTime % 60;
            counter.setText( (int) minutes + ":" + String.format("%02d", (int) seconds));
            timeLeft = exercise.getRest() * 1000;
        }else {
            long restTime = timeLeft / 1000;
            double minutes = restTime / 60;
            double seconds = restTime % 60;
            counter.setText( (int) minutes + ":" + String.format("%02d", (int) seconds));
        }

        setNavigationButtonOnClicks(exercise, exercises);


    }

    protected void updateUI(){
        mWorkoutViewModel.getExercises(intent.getIntExtra("workout", 0)).observe(this, exercises ->{
            testfunc(exercises);
        });
    }

    protected void setNavigationButtonOnClicks(WorkoutExercise exercise, List<WorkoutExercise> exercises){
        previousExercise.setOnClickListener(v->{
            if (currentExercise >= 1){
                currentExercise -= 1;
                set = 1;
                timer.cancel();
                playButton.setBackgroundResource(android.R.drawable.ic_media_play);
                updateUI();
            }
        });
        nextExercise.setOnClickListener(v->{
            if (currentExercise < (exercises.size()-1)){
                currentExercise += 1;
                set = 1;
                timer.cancel();
                playButton.setBackgroundResource(android.R.drawable.ic_media_play);
                updateUI();
            }
        });
        previousSet.setOnClickListener(v->{
            if (set > 1){
                set -= 1;
                timer.cancel();
                playButton.setBackgroundResource(android.R.drawable.ic_media_play);
                updateUI();
            }
        });
        nextSet.setOnClickListener(v->{
            if (set < exercise.getSets()){
                set +=1;
                timer.cancel();
                playButton.setBackgroundResource(android.R.drawable.ic_media_play);
                updateUI();
            }
        });
    }

}
