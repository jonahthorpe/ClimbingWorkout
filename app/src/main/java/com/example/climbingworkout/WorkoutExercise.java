package com.example.climbingworkout;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"workoutID", "exercise"}, tableName = "workout_exercise_table")
public class WorkoutExercise {

    @NonNull
    @ColumnInfo(name="workoutID")
    private int workoutID;
    @NonNull
    @ColumnInfo(name="exercise")
    private String exercise;
    @NonNull
    @ColumnInfo(name="sets")
    private int sets;
    @NonNull
    @ColumnInfo(name="reps")
    private String reps;
    @NonNull
    // 0 is amount, 1 is seconds for example 10 reps of push ups could be 10 push ups or push ups
    // for 10 seconds
    @ColumnInfo(name="repType")
    private Boolean repType;
    @NonNull
    @ColumnInfo(name="rest")
    private int rest;
    @NonNull
    @ColumnInfo(name="position")
    private int position;

    public WorkoutExercise(int workoutID, String exercise, int sets, String reps, @NonNull Boolean repType, int rest, int position) {
        this.workoutID = workoutID;
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.repType = repType;
        this.rest = rest;
        this.position = position;
    }

    public WorkoutExercise() {
    }

    public int getWorkoutID() {
        return workoutID;
    }

    public void setWorkoutID(int workoutID) {
        this.workoutID = workoutID;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    @NonNull
    public Boolean getRepType() {
        return repType;
    }

    public void setRepType(@NonNull Boolean repType) {
        this.repType = repType;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
