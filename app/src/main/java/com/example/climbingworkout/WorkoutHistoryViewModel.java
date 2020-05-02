package com.example.climbingworkout;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class WorkoutHistoryViewModel extends AndroidViewModel {

    private WorkoutRepository mRepository;

    public WorkoutHistoryViewModel(Application application){
        super(application);
        mRepository = new WorkoutRepository(application);
    }

    LiveData<WorkoutDifficulty> getWorkout(int id){return mRepository.getWorkout(id);}
    LiveData<WorkoutCard> getCard(int id){return mRepository.getCard(id);}
}
