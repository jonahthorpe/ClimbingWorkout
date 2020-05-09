package com.example.climbingworkout;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkoutListAdapter extends RecyclerView.Adapter<WorkoutListAdapter.ViewHolder>{

    private ArrayList<String> workoutNames = new ArrayList<>();
    private ArrayList<String> firstExercises = new ArrayList<>();
    private ArrayList<String> secondExercises = new ArrayList<>();
    private ArrayList<String> thirdExercise = new ArrayList<>();
    private ArrayList<String> keys = new ArrayList<>();
    private Context context;

    WorkoutListAdapter(Context context, ArrayList<String> workoutNames, ArrayList<String> firstExercises, ArrayList<String> secondExercises, ArrayList<String> thirdExercise, ArrayList<String> keys) {
        this.context = context;
        this.workoutNames = workoutNames;
        this.firstExercises = firstExercises;
        this.secondExercises = secondExercises;
        this.thirdExercise = thirdExercise;
        this.keys = keys;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("size", position + "");
        holder.workoutName.setText( workoutNames.get(position));
        holder.exercise1.setText( firstExercises.get(position));
        holder.exercise2.setText( secondExercises.get(position));
        holder.exercise3.setText( thirdExercise.get(position));

        holder.containerLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, FirebaseWorkoutOverview.class);
            intent.putExtra("key", keys.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return workoutNames.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView workoutName;
        TextView exercise1;
        TextView exercise2;
        TextView exercise3;
        LinearLayout containerLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutName = itemView.findViewById(R.id.workout_name);
            exercise1 = itemView.findViewById(R.id.exercise1);
            exercise2 = itemView.findViewById(R.id.exercise2);
            exercise3 = itemView.findViewById(R.id.exercise3);
            containerLayout = itemView.findViewById(R.id.list_item_container);
        }
    }
}
