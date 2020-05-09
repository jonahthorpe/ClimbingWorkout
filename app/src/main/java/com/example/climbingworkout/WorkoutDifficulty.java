package com.example.climbingworkout;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout_difficulty_table")
public class WorkoutDifficulty {

    @PrimaryKey
    @ColumnInfo(name="workoutID")
    private int workoutID;
    @NonNull
    @ColumnInfo(name="cardID")
    private int cardID;
    @NonNull
    @ColumnInfo(name="difficulty")
    private String difficulty;
    @NonNull
    @ColumnInfo(name="description")
    private String description;

    public WorkoutDifficulty(int workoutID, int cardID, @NonNull String difficulty, @NonNull String description) {
        this.workoutID = workoutID;
        this.cardID = cardID;
        this.difficulty = difficulty;
        this.description = description;
    }

    public int getWorkoutID() {
        return workoutID;
    }

    public void setWorkoutID(int workoutID) {
        this.workoutID = workoutID;
    }

    @NonNull
    public int getCardID() {
        return cardID;
    }

    public void setCardID( int cardID) {
        this.cardID = cardID;
    }

    @NonNull
    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(@NonNull String difficulty) {
        this.difficulty = difficulty;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }
}
