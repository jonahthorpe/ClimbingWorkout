package com.example.climbingworkout;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WorkoutDifficultyDao {

    @Insert
    void insert(WorkoutDifficulty difficulty);

    @Query("DELETE FROM workout_difficulty_table")
    void deleteAll();

    @Query("SELECT * from workout_difficulty_table WHERE cardID = :id")
    LiveData<List<WorkoutDifficulty>> getWorkouts(int id);

}
