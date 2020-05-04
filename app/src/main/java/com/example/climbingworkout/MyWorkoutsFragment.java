package com.example.climbingworkout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.PrimaryKey;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_workouts, container, false);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddWorkout.class);
                startActivity(intent);
            }
        });



        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/" + user.getUid() + "/my_workouts");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView emptyStateMessage = view.findViewById(R.id.empty_state_message);
                if (dataSnapshot.exists()) {


                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    RecyclerView myWorkoutsList = view.findViewById(R.id.my_workouts_list);
                    ArrayList<String> workoutNames = new ArrayList<>();
                    ArrayList<String> firstExercises = new ArrayList<>();
                    ArrayList<String> secondExercises = new ArrayList<>();
                    ArrayList<String> thirdExercises = new ArrayList<>();
                    ArrayList<String> keys = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //Log.i("qwerty", snapshot.getKey());
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

                    if (workoutNames.isEmpty()){
                        emptyStateMessage.setText("No workouts found.\nTry clicking the add button to create your first workout!");
                    }else{
                        emptyStateMessage.setText("");
                    }

                    //ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.list_item,R.id.list_textview,workoutNames);

                    //myWorkoutsList.setAdapter(adapter);
                    WorkoutListAdapter adapter = new WorkoutListAdapter(getContext(), workoutNames, firstExercises, secondExercises, thirdExercises, keys);
                    myWorkoutsList.setAdapter(adapter);
                    myWorkoutsList.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }

}
