package com.example.climbingworkout;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import java.util.ArrayList;
import java.util.List;

public class AddExerciseForm {

    private EditText exerciseName;
    private EditText setAmount;
    private EditText repAmount;
    private EditText restTime;
    private Spinner repType;
    private AddExerciseForm childExercise;



    public void createAddExerciseForm(LinearLayout container, Context context, Integer amount){
        LinearLayout infoRow = new LinearLayout(context);
        container.addView(infoRow);


        TextView text = new TextView(context);
        text.setText("Exercise Name: ");
        text.setLayoutParams( new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );
        infoRow.addView(text);
        exerciseName = new EditText(context);
        exerciseName.setLayoutParams( new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );
        infoRow.addView(exerciseName);


        infoRow = new LinearLayout(context);
        container.addView(infoRow);
        text = new TextView(context);
        text.setText("Rest Time (seconds): ");
        text.setLayoutParams( new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );
        infoRow.addView(text);
        restTime = new EditText(context);
        restTime.setLayoutParams( new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );
        restTime.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        infoRow.addView(restTime);

        infoRow = new LinearLayout(context);
        container.addView(infoRow);
        text = new TextView(context);
        text.setText("Sets: ");
        text.setLayoutParams( new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );
        infoRow.addView(text);
        setAmount = new EditText(context);
        setAmount.setLayoutParams( new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );
        setAmount.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL | InputType.TYPE_CLASS_NUMBER);
        infoRow.addView(setAmount);

        infoRow = new LinearLayout(context);
        container.addView(infoRow);
        repAmount = new EditText(context);
        repAmount.setLayoutParams( new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f
                )
        );
        repAmount.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        infoRow.addView(repAmount);



        repType = new Spinner(context);
        String[] items = new String[]{"Reps", "Seconds"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items);
        repType.setAdapter(adapter);
        infoRow.addView(repType);

        if (amount - 1 != 0){
            childExercise = new AddExerciseForm();
            childExercise.createAddExerciseForm(container, context, amount-1);
        }
    }

    protected Boolean checkInputsValid(Boolean valid){
        String name = exerciseName.getText().toString();
        String setS = setAmount.getText().toString();
        String repS = repAmount.getText().toString();
        String restS = restTime.getText().toString();
        String repTypeS = repType.getSelectedItem().toString();

        if (name.length() == 0){
            exerciseName.setError("Enter a name");
            exerciseName.setBackgroundResource(R.drawable.error_edit_text);
            valid = false;
        }else{
            exerciseName.setBackgroundResource(R.drawable.normal_edit_text);
        }

        if (setS.length() == 0){
            setAmount.setError("Enter a number");
            setAmount.setBackgroundResource(R.drawable.error_edit_text);
            valid = false;
        }else{
            setAmount.setBackgroundResource(R.drawable.normal_edit_text);
        }

        if (repS.length() == 0){
            repAmount.setError("Enter a numberv");
            repAmount.setBackgroundResource(R.drawable.error_edit_text);
            valid = false;
        }else{
            repAmount.setBackgroundResource(R.drawable.normal_edit_text);
        }

        if (restS.length() == 0){
            restTime.setError("Enter a number");
            restTime.setBackgroundResource(R.drawable.error_edit_text);
            valid = false;
        }else{
            restTime.setBackgroundResource(R.drawable.normal_edit_text);
        }

        if (childExercise != null){
            valid = childExercise.checkInputsValid(valid);
        }

        return valid;
    }

    public ArrayList<WorkoutExercise> getExercises(ArrayList<WorkoutExercise> exercises, int position){
        boolean repTypeBool;
        repTypeBool = repType.getSelectedItem().toString() != "Reps";
        WorkoutExercise exercise = new WorkoutExercise(0, exerciseName.getText().toString(),
                                                       Integer.valueOf(setAmount.getText().toString()),
                                                        repAmount.getText().toString(),
                                                        repTypeBool,
                                                        Integer.valueOf(restTime.getText().toString()),
                                                        position);
        exercises.add(exercise);
        if (childExercise != null){
            exercises = childExercise.getExercises(exercises, position + 1);
        }
        return exercises;
    }

}
