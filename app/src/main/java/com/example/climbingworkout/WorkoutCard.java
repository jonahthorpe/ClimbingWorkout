package com.example.climbingworkout;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "workout_card_table")
public class WorkoutCard {

    @PrimaryKey
    @ColumnInfo(name="cardID")
    private int cardID;
    @NonNull
    @ColumnInfo(name="workoutTitle")
    private String workoutTitle;
    @NonNull
    @ColumnInfo(name="workoutCategory")
    private String workoutCategory;
    @NonNull
    @ColumnInfo(name="imageName")
    private String imageName;

    public WorkoutCard(int cardID, String workoutTitle, String workoutCategory, String imageName) {
        this.cardID = cardID;
        this.workoutTitle = workoutTitle;
        this.workoutCategory = workoutCategory;
        this.imageName = imageName;
    }

    public int getCardID() {
        return cardID;
    }

    public String getWorkoutTitle() {
        return workoutTitle;
    }

    public String getWorkoutCategory() {
        return workoutCategory;
    }

    public String getImageName() {
        return imageName;
    }
}
