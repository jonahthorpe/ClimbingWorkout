package com.example.climbingworkout;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class WorkoutRepository {

    private WorkoutCardDao mWorkoutCardDao;
    private WorkoutDifficultyDao mWorkoutDifficultyDao;
    private WorkoutExerciseDao mWorkoutExerciseDao;
    private ExerciseDao mExerciseDao;

    WorkoutRepository(Application application){
        WorkoutsDatabase db = WorkoutsDatabase.getDatabase(application);
        mWorkoutCardDao = db.workoutCardDao();
        mWorkoutDifficultyDao = db.workoutDifficultyDao();
        mWorkoutExerciseDao = db.workoutExerciseDao();
        mExerciseDao = db.exerciseDao();
    }

    LiveData<List<WorkoutCard>> getAllCards(){
        return mWorkoutCardDao.getWorkoutCards();
    }

    void insert(WorkoutCard card){
        WorkoutsDatabase.databaseWriteExecutor.execute(()-> mWorkoutCardDao.insert(card));
    }


    LiveData<List<WorkoutDifficulty>> getWorkouts(int id){return  mWorkoutDifficultyDao.getWorkouts(id);}

    LiveData<List<WorkoutExercise>> getExercises(int id){return mWorkoutExerciseDao.getWorkoutExercises(id);}

    LiveData<List<String>> getExerciseVid(String exercise){return mExerciseDao.getExerciseVid(exercise);}

    LiveData<WorkoutDifficulty> getWorkout(int id){return mWorkoutDifficultyDao.getWorkout(id);}

    LiveData<WorkoutCard> getCard(int id){return mWorkoutCardDao.getCard(id);}

}
