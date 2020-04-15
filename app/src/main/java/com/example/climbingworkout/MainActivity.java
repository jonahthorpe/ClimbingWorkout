package com.example.climbingworkout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    private WorkoutsFragment workoutsPage = new WorkoutsFragment();
    private WorkoutHistoryFragment workoutHistoryPage = new WorkoutHistoryFragment();
    private MyWorkoutsFragment myWorkoutFragment = new MyWorkoutsFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);




        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                workoutsPage).commit();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.workouts_nav:
                        selectedFragment = workoutsPage;
                        break;
                    case R.id.workout_history_nav:
                        selectedFragment = workoutHistoryPage;
                        break;
                    case R.id.my_workouts_nav:
                        selectedFragment = myWorkoutFragment;
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
                return true;
            };
}
