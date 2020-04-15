package com.example.climbingworkout;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercise_table")
public class Exercise {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="exercise")
    private String exercise;
    @NonNull
    @ColumnInfo(name="vidName")
    private String vidName;

    public Exercise(String exercise, @NonNull String vidName) {
        this.exercise = exercise;
        this.vidName = vidName;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    @NonNull
    public String getVidName() {
        return vidName;
    }

    public void setVidName(@NonNull String vidName) {
        this.vidName = vidName;
    }
}
