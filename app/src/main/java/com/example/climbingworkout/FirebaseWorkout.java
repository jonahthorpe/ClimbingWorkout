package com.example.climbingworkout;

import java.util.ArrayList;

public class FirebaseWorkout {

    public String workout_name;
    public ArrayList<WorkoutExercise> exercises;


    public FirebaseWorkout ( String workout_name, ArrayList<WorkoutExercise> exercises) {
        this.workout_name = workout_name;
        this.exercises = exercises;
    }

    public FirebaseWorkout() { }

    public String getWorkout_name() {
        return workout_name;
    }

    public void setWorkout_name(String workout_name) {
        this.workout_name = workout_name;
    }

    public ArrayList<WorkoutExercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<WorkoutExercise> exercises) {
        this.exercises = exercises;
    }
}
