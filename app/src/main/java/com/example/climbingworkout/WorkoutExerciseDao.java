package com.example.climbingworkout;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WorkoutExerciseDao {

    @Insert
    void insert(WorkoutExercise workoutExercise);

    @Query("DELETE FROM workout_exercise_table")
    void deleteAll();

    @Query("SELECT * FROM workout_exercise_table where workoutID = :id ORDER BY position ASC")
    LiveData<List<WorkoutExercise>> getWorkoutExercises(int id);


}
