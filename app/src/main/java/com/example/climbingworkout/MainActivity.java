package com.example.climbingworkout;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    private WorkoutsFragment workoutsPage = new WorkoutsFragment();
    private WorkoutHistoryFragment workoutHistoryPage = new WorkoutHistoryFragment();
    private MyWorkoutsFragment myWorkoutFragment = new MyWorkoutsFragment();
    private int currentScreen;
    private Fragment selectedFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            try {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            }catch (Exception ignore) {
            }

        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Intent intent = getIntent();
        currentScreen = intent.getIntExtra("currentScreen", 0);

        switch (currentScreen){
            case 0:
                selectedFragment = workoutsPage;
                break;
            case 1:
                selectedFragment = workoutHistoryPage;
                break;
            case 2:
                selectedFragment = myWorkoutFragment;
                break;
        }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sign_out_login_in:
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    FirebaseAuth.getInstance().signOut();
                }
                intent = new Intent(this, LogIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            case R.id.find_gyms:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=climbing+gyms+around+me"));
                startActivity(browserIntent);
                return true;
            case R.id.user_guide_menu_item:
                intent = new Intent(this, UserGuide.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.sign_out_login_in);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            item.setTitle("Sign Out");
        }else{
            item.setTitle("Log In/ Create Account");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.workouts_nav:
                        currentScreen = 0;
                        selectedFragment = workoutsPage;
                        break;
                    case R.id.workout_history_nav:
                        currentScreen = 1;
                        selectedFragment = workoutHistoryPage;
                        break;
                    case R.id.my_workouts_nav:
                        currentScreen = 2;
                        selectedFragment = myWorkoutFragment;
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
                return true;
            };

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentScreen", currentScreen);
        outState.putInt("month", workoutHistoryPage.getMonth());
        outState.putInt("selectedMonth", workoutHistoryPage.getSelectedMonth());
        outState.putInt("selectedYear", workoutHistoryPage.getSelectedYear());
        outState.putInt("selectedDay", workoutHistoryPage.getSelectedDay());
        //Save the fragment's instance

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int month = savedInstanceState.getInt("month");
        int selectedMonth = savedInstanceState.getInt("selectedMonth");
        int selectedDay = savedInstanceState.getInt("selectedDay");
        int selectedYear = savedInstanceState.getInt("selectedYear");
        currentScreen = savedInstanceState.getInt("currentScreen");
        switch (currentScreen){
            case 0:
                selectedFragment = workoutsPage;
                break;
            case 1:
                selectedFragment = workoutHistoryPage;
                workoutHistoryPage.setSelectedCalendar(selectedDay, selectedMonth, selectedYear);
                workoutHistoryPage.setMonth(month);
                break;
            case 2:
                selectedFragment = myWorkoutFragment;
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
               selectedFragment).commit();

    }
}
