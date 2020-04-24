package com.example.climbingworkout;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WorkoutHistoryFragment extends Fragment {

    private LinearLayout monday;
    private LinearLayout tuesday;
    private LinearLayout wednesday;
    private LinearLayout thursday;
    private LinearLayout friday;
    private LinearLayout saturday;
    private LinearLayout sunday;
    private ImageView previousMonth;
    private ImageView nextMonth;
    int month;
    private TextView currentDateField;
    private Calendar currentCalendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("control", "control");
        View view =  inflater.inflate(R.layout.fragment_workout_history, container, false);

        monday = view.findViewById(R.id.mondayColumn);
        tuesday = view.findViewById(R.id.tuesdayColumn);
        wednesday  = view.findViewById(R.id.wednesdayColumn);
        thursday = view.findViewById(R.id.thursdayColumn);
        friday = view.findViewById(R.id.fridayColumn);
        saturday = view.findViewById(R.id.saturdayColumn);
        sunday = view.findViewById(R.id.sundayColumn);

        currentDateField = view.findViewById(R.id.date);

        currentCalendar = Calendar.getInstance();
        int currentYear = Calendar.YEAR;
        Log.i("year", currentYear+ "");

        month = 0;
        createCalendar(view, month);

        previousMonth = view.findViewById(R.id.prevMonth);
        previousMonth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                month -= 1;
                createCalendar(view, month);
            }
        });

        nextMonth = view.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                month += 1;
                createCalendar(view, month);
            }
        });

        return view;
    }

    private void createCalendar(View view, int month){
        monday.removeAllViews();
        tuesday.removeAllViews();
        wednesday.removeAllViews();
        thursday.removeAllViews();
        friday.removeAllViews();
        saturday.removeAllViews();
        sunday.removeAllViews();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);
        Date currentDate = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM yyyy");
        currentDateField.setText(simpleDateFormat.format(currentDate));

        int day = getPadding(calendar);


        padCalendar(day);


        fillCalendar(calendar, day);
    }

    protected void padCalendar(int day){
        for (int i = 1; i <= day; i++){
            TextView text = new TextView(getContext());
            text.setGravity(Gravity.CENTER);
            text.setText("");
            switch ((i)%7){
                case 1:
                    monday.addView(text);
                    break;
                case 2:
                    tuesday.addView(text);
                    break;
                case 3:
                    wednesday.addView(text);
                    break;
                case 4:
                    thursday.addView(text);
                    break;
                case 5:
                    friday.addView(text);
                    break;
                case 6:
                    saturday.addView(text);
                    break;
                case 0:
                    sunday.addView(text);
                    break;
            }
        }
    }

    protected void fillCalendar(Calendar calendar, int day){
        int maxDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i<=maxDate; i++ ){
            TextView text = new TextView(getContext());
            text.setGravity(Gravity.CENTER);
            text.setText(String.valueOf(i));
            if (calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                    calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
                    currentCalendar.get(Calendar.DAY_OF_MONTH) == i
            ){
                text.setTextColor(Color.RED);
            }
            switch ((i+day)%7){
                case 1:
                    monday.addView(text);
                    break;
                case 2:
                    tuesday.addView(text);
                    break;
                case 3:
                    wednesday.addView(text);
                    break;
                case 4:
                    thursday.addView(text);
                    break;
                case 5:
                    friday.addView(text);
                    break;
                case 6:
                    saturday.addView(text);
                    break;
                case 0:
                    sunday.addView(text);
                    break;
            }
        }
    }

    protected int getPadding(Calendar calendar){
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:
                day = 1;
                break;
            case Calendar.TUESDAY:
                day = 2;
                break;
            case Calendar.WEDNESDAY:
                day = 3;
                break;
            case Calendar.THURSDAY:
                day = 4;
                break;
            case Calendar.FRIDAY:
                day = 5;
                break;
            case Calendar.SATURDAY:
                day = 6;
                break;
            case Calendar.SUNDAY:
                day = 0;
                break;
        }
        return  day;
    }

}
