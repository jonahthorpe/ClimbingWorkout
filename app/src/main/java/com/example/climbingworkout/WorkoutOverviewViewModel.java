package com.example.climbingworkout;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WorkoutOverviewViewModel extends AndroidViewModel {

    private WorkoutRepository mRepository;

    private LiveData<List<WorkoutDifficulty>> mWorkouts;

    public WorkoutOverviewViewModel(Application application){
        super(application);
        mRepository = new WorkoutRepository(application);
    }

    LiveData<List<WorkoutDifficulty>> getWorkouts(int id){ return mRepository.getWorkouts(id); }

    LiveData<List<WorkoutExercise>> getExercises(int id){return mRepository.getExercises(id);}

    LiveData<WorkoutCard> getCard(int id){return mRepository.getCard(id);}

}
