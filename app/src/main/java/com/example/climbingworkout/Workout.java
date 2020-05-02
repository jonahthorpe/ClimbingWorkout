package com.example.climbingworkout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
    private Boolean resting = true;
    private TextView timer_label;
    private Boolean isMyWokout;
    private  WorkoutExercise exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        intent = getIntent();
        isMyWokout = intent.getBooleanExtra("myWorkout", true);


        timer_label = findViewById(R.id.timer_label);


        mWorkoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);


        playButton = findViewById(R.id.playButton);
        playButton.setBackgroundResource(android.R.drawable.ic_media_play);


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

    private void startTimerRest() {
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
                if (!resting){
                    resting = true;
                }
                updateUI();
            }

        }.start();
    }

    private void startTimerExercise(int restTime) {
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
                //playButton.setBackgroundResource(android.R.drawable.ic_media_play);
                counter.setText("0:00");
                timer_label.setText("Rest");
                test = null;
                if (!resting){
                    resting = true;
                }
                timeLeft = restTime * 1000;
                startTimerRest();
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
        outState.putInt("currentSet", set);
        outState.putBoolean("resting", resting);
        outState.putInt("rest", exercise.getRest());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        timeLeft = savedInstanceState.getLong("timeLeft");
        playing = savedInstanceState.getBoolean("playing");
        currentExercise = savedInstanceState.getInt("currentExercise");
        set = savedInstanceState.getInt("currentSet");
        resting = savedInstanceState.getBoolean("resting");
        int rest = savedInstanceState.getInt("rest");

        test = "a";
        if (playing) {
            endTime = savedInstanceState.getLong("endTime");
            timeLeft = endTime - System.currentTimeMillis() ;
            if (resting) {
                startTimerRest();
            }else{
                startTimerExercise(rest);
            }
            playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
        }
    }

    protected LifecycleOwner getLifecycleOwner(){
        return this;
    }

    protected void testfunc(List<WorkoutExercise> exercises){
        exercise = exercises.get(currentExercise);
        Log.i("exercise", exercise.getExercise());
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

        // cant use separate if statements as the state could fall into multiple at once
        // for example if there is only 1 exercise, it will be both the first and last thus both
        // if statements need to entered

        nextSet.setColorFilter(null);
        previousSet.setColorFilter(null);
        if (set == 1){
            previousSet.setColorFilter(Color.argb(150,200,200,200));
            //nextSet.setColorFilter(null);
        }
        if(set == exercise.getSets()){
            nextSet.setColorFilter(Color.argb(150,200,200,200));
            //previousSet.setColorFilter(null);
        }


        nextExercise.setColorFilter(null);
        previousExercise.setColorFilter(null);

        if (currentExercise == 0){
            previousExercise.setColorFilter(Color.argb(150,200,200,200));
            //nextExercise.setColorFilter(null);
        }
        if(currentExercise == (exercises.size()-1)){
            nextExercise.setColorFilter(Color.argb(150,200,200,200));
            //previousExercise.setColorFilter(null);
        }




        exerciseTitle.setText(exercise.getExercise());
        if (exercise.getRepType()) {
            reps.setText("Time: " + exercise.getReps() + " seconds");
        }else {
            reps.setText("Reps: " + exercise.getReps());
        }
        sets.setText("Sets: "+ set +"/" + exercise.getSets());

        if (!isMyWokout) {
            mWorkoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
            mWorkoutViewModel.getExerciseVid(exercise.getExercise()).observe(this, exerciseVids -> {
                int id = getResources().getIdentifier(exerciseVids.get(0), "raw", getPackageName());
                String videoPath = "android.resource://" + getPackageName() + "/" + id;
                Uri uri = Uri.parse(videoPath);
                workoutDemoVid.setVideoURI(uri);
            });
        }


        if(test == null){
            long time;
            // rep type return true if time
            if (exercise.getRepType()){
                resting = false;
                time = Long.parseLong(exercise.getReps());
            }else {
                time = exercise.getRest();
            }
            double minutes = time / 60;
            double seconds = time % 60;
            counter.setText((int) minutes + ":" + String.format("%02d", (int) seconds));
            timeLeft = time * 1000;
        }else {
            long restTime = timeLeft / 1000;
            double minutes = restTime / 60;
            double seconds = restTime % 60;
            counter.setText( (int) minutes + ":" + String.format("%02d", (int) seconds));
        }

        if (resting){
            timer_label.setText("Rest");
        }else{
            timer_label.setText("Exercise Time");
        }

        int restTime = exercise.getRest();
        playButton.setOnClickListener(v -> {
            if (playing){
                playing = false;
                playButton.setBackgroundResource(android.R.drawable.ic_media_play);
                pauseTimer();
            }else{
                playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
                playing = true;
                if (resting) {
                    startTimerRest();
                }else{
                    startTimerExercise(restTime);
                }
            }
        });

        setNavigationButtonOnClicks(exercise, exercises);


    }

    protected void updateUI(){
        if (isMyWokout){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users/" + user.getUid() + "/my_workouts/" + intent.getStringExtra("workout"));
            Log.i("exrecise", isMyWokout + "");
            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    if (dataSnapshot.exists()) {
                        FirebaseWorkout workout = dataSnapshot.getValue(FirebaseWorkout.class);
                        ArrayList<WorkoutExercise> exercises = workout.getExercises();
                        testfunc(exercises);
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value

                }
            });
        }else {
            mWorkoutViewModel.getExercises(intent.getIntExtra("workout", 0)).observe(this, exercises -> {
                testfunc(exercises);
            });
        }
    }

    protected void setNavigationButtonOnClicks(WorkoutExercise exercise, List<WorkoutExercise> exercises){
        previousExercise.setOnClickListener(v->{
            if (currentExercise >= 1){
                currentExercise -= 1;
                set = 1;
                test = null;
                if (timer != null){
                    timer.cancel();
                }
                playButton.setBackgroundResource(android.R.drawable.ic_media_play);
                updateUI();
            }
        });
        nextExercise.setOnClickListener(v->{
            if (currentExercise < (exercises.size()-1)){
                currentExercise += 1;
                set = 1;
                test = null;
                if (timer != null){
                    timer.cancel();
                }
                playButton.setBackgroundResource(android.R.drawable.ic_media_play);
                updateUI();
            }
        });
        previousSet.setOnClickListener(v->{
            if (set > 1){
                set -= 1;
                test = null;
                if (timer != null){
                    timer.cancel();
                }
                playButton.setBackgroundResource(android.R.drawable.ic_media_play);
                updateUI();
            }
        });
        nextSet.setOnClickListener(v->{
            if (set < exercise.getSets()){
                set +=1;
                test = null;
                if (timer != null){
                    timer.cancel();
                }
                playButton.setBackgroundResource(android.R.drawable.ic_media_play);
                updateUI();
            }
        });
    }

}
