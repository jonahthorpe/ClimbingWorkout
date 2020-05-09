package com.example.climbingworkout;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WorkoutsCardViewModel extends AndroidViewModel {

    private WorkoutRepository mRepository;

    private LiveData<List<WorkoutCard>> mAllCards;

    public WorkoutsCardViewModel(Application application){
        super(application);
        mRepository = new WorkoutRepository(application);
        mAllCards = mRepository.getAllCards();
    }

    LiveData<List<WorkoutCard>> getAllCards(){ return mAllCards; }

    //public void insert(WorkoutCard card ){ mRepository.insert(card);}

}
