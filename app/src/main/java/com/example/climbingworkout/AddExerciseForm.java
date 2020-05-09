package com.example.climbingworkout;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;

public class AddExerciseForm {

    private EditText exerciseName;
    private EditText setAmount;
    private EditText repAmount;
    private EditText restTime;
    private Spinner repType;
    private AddExerciseForm childExercise;
    private EditText repeaterOn;
    private EditText repeaterOff;



    void createAddExerciseForm(LinearLayout container, Context context, Integer amount){
        LinearLayout infoRow = new LinearLayout(context);
        container.addView(infoRow);


        TextView text = new TextView(context);
        text.setText(context.getResources().getString(R.string.exercise_name));
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
        text.setText(context.getResources().getString(R.string.rest_time_form));
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
        text.setText(context.getResources().getString(R.string.sets_form));
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
        repAmount.setHint("Amount");
        infoRow.addView(repAmount);

        repeaterOn = new EditText(context);
        repeaterOn.setLayoutParams( new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f
                )
        );
        repeaterOn.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        infoRow.addView(repeaterOn);
        repeaterOn.setHint("Time On");
        repeaterOn.setVisibility(View.INVISIBLE);

        repeaterOff = new EditText(context);
        repeaterOff.setLayoutParams( new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f
                )
        );
        repeaterOff.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        infoRow.addView(repeaterOff);
        repeaterOff.setHint("Time Off");
        repeaterOff.setVisibility(View.INVISIBLE);


        repType = new Spinner(context);
        String[] items = new String[]{"Reps", "Seconds", "Repeaters"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items);
        repType.setAdapter(adapter);
        infoRow.addView(repType);

        repType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (repType.getSelectedItem().toString()){
                    case "Reps":
                    case "Seconds":
                        repeaterOn.setVisibility(View.INVISIBLE);
                        repeaterOff.setVisibility(View.INVISIBLE);
                        break;
                    case "Repeaters":
                        repeaterOn.setVisibility(View.VISIBLE);
                        repeaterOff.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        if (amount - 1 != 0){
            childExercise = new AddExerciseForm();
            childExercise.createAddExerciseForm(container, context, amount-1);
        }
    }

    Boolean checkInputsValid(Boolean valid){
        String name = exerciseName.getText().toString();
        String setS = setAmount.getText().toString();
        String repS = repAmount.getText().toString();
        String restS = restTime.getText().toString();

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
            repAmount.setError("Enter a number");
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

        if (repeaterOn.getVisibility() == View.VISIBLE && repeaterOn.getText().toString().length() == 0){
            repeaterOn.setError("Enter a number");
            repeaterOn.setBackgroundResource(R.drawable.error_edit_text);
            valid = false;
        }else{
            repeaterOn.setBackgroundResource(R.drawable.normal_edit_text);
        }

        if (repeaterOff.getVisibility() == View.VISIBLE && repeaterOff.getText().toString().length() == 0){
            repeaterOff.setError("Enter a number");
            repeaterOff.setBackgroundResource(R.drawable.error_edit_text);
            valid = false;
        }else{
            repeaterOff.setBackgroundResource(R.drawable.normal_edit_text);
        }

        if (childExercise != null){
            valid = childExercise.checkInputsValid(valid);
        }

        return valid;
    }

    public ArrayList<WorkoutExercise> getExercises(ArrayList<WorkoutExercise> exercises, int position){
        int repTypeInt = 0;
        int repeaterOnValue = 0;
        int repeaterOffValue = 0;
        switch (repType.getSelectedItem().toString()){
            case "Reps":
                repTypeInt = 0;
                break;
            case "Seconds":
                repTypeInt = 1;
                break;
            case "Repeaters":
                repTypeInt =2;
                repeaterOnValue = Integer.valueOf(repeaterOn.getText().toString());
                repeaterOffValue = Integer.valueOf(repeaterOff.getText().toString());
                break;
        }

        WorkoutExercise exercise = new WorkoutExercise(0, exerciseName.getText().toString(),
                                                       Integer.valueOf(setAmount.getText().toString()),
                                                        repAmount.getText().toString(),
                                                        repTypeInt,
                                                        Integer.valueOf(restTime.getText().toString()),
                                                        position,
                                                        repeaterOnValue,
                                                        repeaterOffValue);
        exercises.add(exercise);
        if (childExercise != null){
            exercises = childExercise.getExercises(exercises, position + 1);
        }
        return exercises;
    }

}
