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
    //2 is for repeater style workout for example 5 seconds 0n 5 seconds of 5 times
    @ColumnInfo(name="repType")
    private int repType;
    @NonNull
    @ColumnInfo(name="rest")
    private int rest;
    @NonNull
    @ColumnInfo(name="position")
    private int position;
    @NonNull
    @ColumnInfo(name="repeaterOn")
    private int repeaterOn;
    @NonNull
    @ColumnInfo(name="repeaterOff")
    private int repeaterOff;

    public WorkoutExercise(int workoutID, @NonNull String exercise, int sets, @NonNull String reps, int repType, int rest, int position, int repeaterOn, int repeaterOff) {
        this.workoutID = workoutID;
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.repType = repType;
        this.rest = rest;
        this.position = position;
        this.repeaterOn = repeaterOn;
        this.repeaterOff = repeaterOff;
    }

    public WorkoutExercise() {
    }

    public void setWorkoutID(int workoutID) {
        this.workoutID = workoutID;
    }

    public void setExercise(@NonNull String exercise) {
        this.exercise = exercise;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public void setReps(@NonNull String reps) {
        this.reps = reps;
    }

    public void setRepType(int repType) {
        this.repType = repType;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setRepeaterOn(int repeaterOn) {
        this.repeaterOn = repeaterOn;
    }

    public void setRepeaterOff(int repeaterOff) {
        this.repeaterOff = repeaterOff;
    }

    public int getWorkoutID() {
        return workoutID;
    }

    @NonNull
    public String getExercise() {
        return exercise;
    }

    public int getSets() {
        return sets;
    }

    @NonNull
    public String getReps() {
        return reps;
    }

    public int getRepType() {
        return repType;
    }

    public int getRest() {
        return rest;
    }

    public int getPosition() {
        return position;
    }

    public int getRepeaterOn() {
        return repeaterOn;
    }

    public int getRepeaterOff() {
        return repeaterOff;
    }
}
