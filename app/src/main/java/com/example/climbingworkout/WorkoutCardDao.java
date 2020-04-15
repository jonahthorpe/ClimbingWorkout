package com.example.climbingworkout;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WorkoutCardDao {

    @Insert
    void insert(WorkoutCard card);

    @Query("DELETE FROM workout_card_table")
    void deleteAll();

    @Query("SELECT * from workout_card_table ORDER BY workoutCategory ASC")
    LiveData<List<WorkoutCard>> getWorkoutCards();

}
