package com.example.climbingworkout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
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
    private Boolean isMyWorkout;
    private  WorkoutExercise exercise;
    private int repNumber = 1 ;
    private boolean isRepeaterOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        intent = getIntent();
        isMyWorkout = intent.getBooleanExtra("myWorkout", true);


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
                repNumber = 1;
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
        timer_label.setText("Exercise");
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

    private void startTimerRepeaterOn(int repeaterOn, int repeaterOff, int maxReps, int restTime){
        timer_label.setText("On");
        reps.setText("Reps: " + repNumber + "/" + maxReps);
        isRepeaterOn = true;
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
                if (repNumber == maxReps){
                    resting = true;
                    timer_label.setText("Rest");
                    timeLeft = restTime * 1000;
                    startTimerRest();
                }else {
                    repNumber += 1;
                    counter.setText("0:00");
                    timer_label.setText("Rest");
                    timeLeft = repeaterOff * 1000;
                    startTimerRepeaterOff(repeaterOn, repeaterOff, maxReps, restTime);
                }

            }

        }.start();
    }

    private void startTimerRepeaterOff(int repeaterOn, int repeaterOff, int maxReps, int restTime){
        timer_label.setText("Off");
        isRepeaterOn = false;
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
                counter.setText("0:00");
                timer_label.setText("Exercise");
                timeLeft = repeaterOn * 1000;
                startTimerRepeaterOn(repeaterOn, repeaterOff, maxReps, restTime);

            }

        }.start();

    }

    private void pauseTimer(){
        timer.cancel();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("timeLeft", timeLeft);
        outState.putBoolean("playing", playing);
        outState.putLong("endTime", endTime);
        outState.putInt("currentExercise", currentExercise);
        outState.putInt("currentSet", set);
        outState.putBoolean("resting", resting);
        outState.putInt("rest", exercise.getRest());
        outState.putInt("repNumber", repNumber);
        outState.putBoolean("isRepeaterOn", isRepeaterOn);
        outState.putInt("repType", exercise.getRepType());
        outState.putInt("reps", Integer.valueOf(exercise.getReps()));
        outState.putInt("repeaterOn", exercise.getRepeaterOn());
        outState.putInt("repeaterOff", exercise.getRepeaterOff());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        timeLeft = savedInstanceState.getLong("timeLeft");
        playing = savedInstanceState.getBoolean("playing");
        currentExercise = savedInstanceState.getInt("currentExercise");
        set = savedInstanceState.getInt("currentSet");
        resting = savedInstanceState.getBoolean("resting");
        int rest = savedInstanceState.getInt("rest");
        repNumber = savedInstanceState.getInt("repNumber");
        isRepeaterOn = savedInstanceState.getBoolean("isRepeaterOn");
        int reps = savedInstanceState.getInt("reps");
        int repeaterOn = savedInstanceState.getInt("repeaterOn");
        int repeaterOff = savedInstanceState.getInt("repeaterOff");
        int repType = savedInstanceState.getInt("repType");

        test = "a";
        if (playing) {
            endTime = savedInstanceState.getLong("endTime");
            timeLeft = endTime - System.currentTimeMillis() ;
            if (resting) {
                startTimerRest();
            }else if (repType == 1){
                startTimerExercise(rest);
            }else{
                if (isRepeaterOn) {
                    startTimerRepeaterOn(repeaterOn, repeaterOff, reps, rest);
                }else if(repNumber == reps ){
                    startTimerRest();
                }else{
                    startTimerRepeaterOff(repeaterOn, repeaterOff, reps, rest);
                }
            }
            playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
        }
    }


    protected void outputExercise(List<WorkoutExercise> exercises){
        exercise = exercises.get(currentExercise);
        if (set > exercise.getSets()){
            currentExercise += 1;
            if (currentExercise >= exercises.size()){
                Toast.makeText(getApplicationContext(), "Well Done!\nWorkout Completed!",
                        Toast.LENGTH_SHORT).show();
                finish();
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
        if (exercise.getRepType() == 1) {
            reps.setText("Time: " + exercise.getReps() + " seconds");
        }else if (exercise.getRepType() == 0){
            reps.setText("Reps: " + exercise.getReps());
        }else{
            reps.setText("Reps: " + repNumber + "/" + exercise.getReps());
        }
        sets.setText("Sets: "+ set +"/" + exercise.getSets());

        if (!isMyWorkout) {
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
            if (exercise.getRepType() == 1){
                resting = false;
                time = Long.parseLong(exercise.getReps());
            }else if (exercise.getRepType() == 0){
                time = exercise.getRest();
            }else {
                resting = false;
                time = exercise.getRepeaterOn();
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
        }else if (exercise.getRepType() == 2){
            timer_label.setText("On");
        }else{
            timer_label.setText("Exercise");
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
                }else if (exercise.getRepType() == 2){
                    startTimerRepeaterOn(exercise.getRepeaterOn(), exercise.getRepeaterOff(), Integer.valueOf(exercise.getReps()), exercise.getRest());
                }else{
                    startTimerExercise(exercise.getRest());
                }
            }
        });

        setNavigationButtonOnClicks(exercise, exercises);


    }

    protected void updateUI(){
        if (isMyWorkout){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users/" + user.getUid() + "/my_workouts/" + intent.getStringExtra("workout"));
            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    if (dataSnapshot.exists()) {
                        FirebaseWorkout workout = dataSnapshot.getValue(FirebaseWorkout.class);
                        ArrayList<WorkoutExercise> exercises = workout.getExercises();
                        outputExercise(exercises);
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value

                }
            });
        }else {
            mWorkoutViewModel.getExercises(intent.getIntExtra("workout", 0)).observe(this, this::outputExercise);
        }
    }

    protected void setNavigationButtonOnClicks(WorkoutExercise exercise, List<WorkoutExercise> exercises){
        previousExercise.setOnClickListener(v->{
            if (currentExercise >= 1){
                repNumber = 1;
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
                repNumber = 1;
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
                repNumber = 1;
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
                repNumber = 1;
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
