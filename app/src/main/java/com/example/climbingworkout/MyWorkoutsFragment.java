package com.example.climbingworkout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyWorkoutsFragment extends Fragment {
    private View view;
    private RelativeLayout myWorkoutsContainer;
    private LinearLayout loggedInMessageContainer;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_workouts, container, false);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddWorkout.class);
            startActivity(intent);
        });
        progressBar = view.findViewById(R.id.progressBar);

        myWorkoutsContainer = view.findViewById(R.id.my_workouts_contaier);
        loggedInMessageContainer = view.findViewById(R.id.logged_in_message_container);
        Button goToLogInPage = view.findViewById(R.id.go_to_log_in);
        goToLogInPage.setOnClickListener(view ->{
            Intent intent = new Intent(getContext(), LogIn.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Activity activity = getActivity();
            startActivity(intent);
            activity.finish();
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            myWorkoutsContainer.setVisibility(View.VISIBLE);
            loggedInMessageContainer.setVisibility(View.INVISIBLE);
            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users/" + user.getUid() + "/my_workouts");

            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    RelativeLayout container = view.findViewById(R.id.list_container);
                    TextView emptyStateMessage = view.findViewById(R.id.empty_state_message);
                    RecyclerView myWorkoutsList = view.findViewById(R.id.my_workouts_list);
                    if (dataSnapshot.exists()) {

                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        ArrayList<String> workoutNames = new ArrayList<>();
                        ArrayList<String> firstExercises = new ArrayList<>();
                        ArrayList<String> secondExercises = new ArrayList<>();
                        ArrayList<String> thirdExercises = new ArrayList<>();
                        ArrayList<String> keys = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            FirebaseWorkout workout = snapshot.getValue(FirebaseWorkout.class);
                            workoutNames.add(workout.getWorkout_name());
                            List<WorkoutExercise> exercises = workout.getExercises();
                            if (exercises.size() > 3) {
                                firstExercises.add(exercises.get(0).getExercise());
                                secondExercises.add(exercises.get(1).getExercise());
                                thirdExercises.add(exercises.get(2).getExercise() + "...");
                            } else if (exercises.size() == 3) {
                                firstExercises.add(exercises.get(0).getExercise());
                                secondExercises.add(exercises.get(1).getExercise());
                                thirdExercises.add(exercises.get(2).getExercise());
                            } else if (exercises.size() == 2) {
                                firstExercises.add(exercises.get(0).getExercise());
                                secondExercises.add(exercises.get(1).getExercise());
                                thirdExercises.add("");
                            } else {
                                firstExercises.add(exercises.get(0).getExercise());
                                secondExercises.add("");
                                thirdExercises.add("");
                            }
                            keys.add(snapshot.getKey());


                        }

                        if (workoutNames.isEmpty()) {
                            myWorkoutsList.setVisibility(View.INVISIBLE);
                            emptyStateMessage.setText("No workouts found.\nTry clicking the add button to create your first workout!");
                        } else {
                            myWorkoutsList.setVisibility(View.VISIBLE);
                            emptyStateMessage.setText("");
                        }

                        //ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.list_item,R.id.list_textview,workoutNames);

                        //myWorkoutsList.setAdapter(adapter);
                        WorkoutListAdapter adapter = new WorkoutListAdapter(getContext(), workoutNames, firstExercises, secondExercises, thirdExercises, keys);
                        myWorkoutsList.setAdapter(adapter);
                        myWorkoutsList.setLayoutManager(new LinearLayoutManager(getContext()));
                    } else {
                        myWorkoutsList.setVisibility(View.INVISIBLE);
                        emptyStateMessage.setText("No workouts found.\nTry clicking the add button to create your first workout!");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value

                }
            });
        }else{
            progressBar.setVisibility(View.GONE);
            myWorkoutsContainer.setVisibility(View.INVISIBLE);
            loggedInMessageContainer.setVisibility(View.VISIBLE);
        }
    }

}
