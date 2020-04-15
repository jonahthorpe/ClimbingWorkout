package com.example.climbingworkout;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WorkoutViewModel extends AndroidViewModel {

    private WorkoutRepository mRepository;

    private LiveData<List<WorkoutDifficulty>> mExcerciseVid;

    public WorkoutViewModel(Application application){
        super(application);
        mRepository = new WorkoutRepository(application);
    }

    LiveData<List<String>> getExerciseVid(String exercise){return mRepository.getExerciseVid(exercise);}

    LiveData<List<WorkoutExercise>> getExercises(int id){return mRepository.getExercises(id);}

}
