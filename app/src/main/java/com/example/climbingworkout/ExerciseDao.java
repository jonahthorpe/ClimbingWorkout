package com.example.climbingworkout;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExerciseDao {

    @Insert
    void insert(Exercise exercise);

    @Query("DELETE FROM exercise_table")
    void deleteAll();

    @Query("SELECT vidName FROM exercise_table WHERE exercise =  :exercise")
    LiveData<List<String>> getExerciseVid(String exercise);

}
