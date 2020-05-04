package com.example.climbingworkout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.OnConflictStrategy;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    int month = 0;
    private TextView currentDateField;
    private Calendar currentCalendar;
    private List<WorkoutLog> loggedWorkouts;
    private Calendar loggedCalendar;
    private Resources r;
    private Calendar selectedCalendar = Calendar.getInstance();
    private View view;
    private int row;
    private int selectedId;
    private WorkoutHistoryViewModel mHistoryViewModel;
    private Context mContext;
    private LifecycleOwner mLifeCycleOwner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_workout_history, container, false);
        r = getResources();
        selectedId = 0;
        mHistoryViewModel = new ViewModelProvider(this).get(WorkoutHistoryViewModel.class);
        mContext = getContext();
        mLifeCycleOwner = getViewLifecycleOwner();

        monday = view.findViewById(R.id.mondayColumn);
        tuesday = view.findViewById(R.id.tuesdayColumn);
        wednesday  = view.findViewById(R.id.wednesdayColumn);
        thursday = view.findViewById(R.id.thursdayColumn);
        friday = view.findViewById(R.id.fridayColumn);
        saturday = view.findViewById(R.id.saturdayColumn);
        sunday = view.findViewById(R.id.sundayColumn);

        currentDateField = view.findViewById(R.id.date);

        loggedCalendar = Calendar.getInstance();
        currentCalendar = Calendar.getInstance();


        createCalendar(month);

        previousMonth = view.findViewById(R.id.prevMonth);
        previousMonth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                month -= 1;
                createCalendar( month);
            }
        });

        nextMonth = view.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                month += 1;
                createCalendar( month);
            }
        });


        return view;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getSelectedYear(){
        return selectedCalendar.get(Calendar.YEAR);
    }

    public int getSelectedMonth(){
        return selectedCalendar.get(Calendar.MONTH);
    }

    public int getSelectedDay(){
        return selectedCalendar.get(Calendar.DAY_OF_MONTH);
    }

    public void setSelectedCalendar(int day, int month, int year) {
        selectedCalendar.set(Calendar.MONTH, month);
        selectedCalendar.set(Calendar.DAY_OF_MONTH, day);
        selectedCalendar.set(Calendar.YEAR, year);
    }

    private void createCalendar(int month){
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

        padCalendarStart(day);

        fillCalendar(calendar, day);
    }

    private void padCalendarStart(int day){
        for (int i = 1; i <= day; i++){
            LinearLayout dayContainer = new LinearLayout(mContext);
            dayContainer.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            Gravity.CENTER
                    )
            );
            TextView text = new TextView(mContext);
            text.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )
            );
            text.setGravity(Gravity.CENTER);
            text.setText("");
            dayContainer.setPadding(0,Utility.dpToPx(10f,r),0,Utility.dpToPx(10f,r));
            dayContainer.addView(text);
            switch ((i)%7){
                case 1:
                    monday.addView(dayContainer);
                    break;
                case 2:
                    tuesday.addView(dayContainer);
                    break;
                case 3:
                    wednesday.addView(dayContainer);
                    break;
                case 4:
                    thursday.addView(dayContainer);
                    break;
                case 5:
                    friday.addView(dayContainer);
                    break;
                case 6:
                    saturday.addView(dayContainer);
                    break;
                case 0:
                    sunday.addView(dayContainer);
                    break;
            }
        }
    }

    private void padCalendarEnd(Calendar calendar){
        int daysLeft = 0;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                daysLeft = 6;
                break;
            case Calendar.TUESDAY:
                daysLeft = 5;
                break;
            case Calendar.WEDNESDAY:
                daysLeft = 4;
                break;
            case Calendar.THURSDAY:
                daysLeft = 3;
                break;
            case Calendar.FRIDAY:
                daysLeft = 2;
                break;
            case Calendar.SATURDAY:
                daysLeft = 1;
                break;
        }
        for (int i = 0; i< daysLeft; i++){
            LinearLayout dayContainer = new LinearLayout(mContext);
            dayContainer.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            Gravity.CENTER
                    )
            );
            TextView text = new TextView(mContext);
            text.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )
            );
            text.setGravity(Gravity.CENTER);
            text.setText("");
            dayContainer.setPadding(0,Utility.dpToPx(10f,r),0,Utility.dpToPx(10f,r));
            dayContainer.addView(text);
            addTextToDay(6 - i, 1, dayContainer);

        }

    }

    private void fillCalendar(Calendar calendar, int day){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        loggedWorkouts = new ArrayList<>();
        // Write a message to the database
        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users/" + user.getUid() + "/logged_workouts");
            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        int maxDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        for (int i = 1; i<=maxDate; i++ ){
                            calendar.set(Calendar.DAY_OF_MONTH, i);


                            LinearLayout dayContainer = new LinearLayout(mContext);
                            dayContainer.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    Gravity.CENTER
                                    )
                            );


                            TextView text = new TextView(mContext);
                            text.setId(i);
                            text.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT
                                    )
                            );

                            text.setGravity(Gravity.CENTER);
                            text.setText(String.valueOf(i));
                            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                            dayContainer.setPadding(0,Utility.dpToPx(10f,r),0,Utility.dpToPx(10f,r));
                            text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TextView text = view.findViewById(selectedId);
                                    Log.i("selected", selectedId + "");
                                    try{
                                    text.setBackgroundResource(0);
                                    }catch (Exception e){

                                    }
                                    selectedId =  v.getId();
                                    selectedCalendar = calendar;
                                    selectedCalendar.set(Calendar.DAY_OF_MONTH,selectedId);
                                    v.setBackgroundColor(Color.parseColor("#27FF0000"));

                                    TextView selectedDateText = view.findViewById(R.id.selected_date);
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy");
                                    selectedDateText.setText(simpleDateFormat.format(selectedCalendar.getTime()));
                                    showLoggedWorkouts(dataSnapshot);
                                }
                            });
                            dayContainer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    text.performClick();
                                }
                            });
                            dayContainer.addView(text);
                            if (calendar.get(Calendar.YEAR) == selectedCalendar.get(Calendar.YEAR) &&
                                    calendar.get(Calendar.MONTH) == selectedCalendar.get(Calendar.MONTH) &&
                                    selectedCalendar.get(Calendar.DAY_OF_MONTH) == i){
                                text.setBackgroundColor(Color.parseColor("#27FF0000"));
                                selectedId = i;
                                showLoggedWorkouts(dataSnapshot);
                            }

                            addLoggedWorkoutsIcon(dataSnapshot, calendar, dayContainer, i);
                            checkCurrentDate(calendar, text, i);
                            addTextToDay(day, i, dayContainer);
                        }

                        padCalendarEnd(calendar);

                    }


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }

            });
        }
    }

    private int getPadding(Calendar calendar){
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:
                day = 0;
                break;
            case Calendar.TUESDAY:
                day = 1;
                break;
            case Calendar.WEDNESDAY:
                day = 2;
                break;
            case Calendar.THURSDAY:
                day = 3;
                break;
            case Calendar.FRIDAY:
                day = 4;
                break;
            case Calendar.SATURDAY:
                day = 5;
                break;
            case Calendar.SUNDAY:
                day = 6;
                break;
        }
        return  day;
    }

    private void addTextToDay(int day, int i, LinearLayout dayContainer){
        switch ((i+day)%7){
            case 1:
                monday.addView(dayContainer);
                break;
            case 2:
                tuesday.addView(dayContainer);
                break;
            case 3:
                wednesday.addView(dayContainer);
                break;
            case 4:
                thursday.addView(dayContainer);
                break;
            case 5:
                friday.addView(dayContainer);
                break;
            case 6:
                saturday.addView(dayContainer);
                break;
            case 0:
                sunday.addView(dayContainer);
                row += 1;
                break;
        }
    }

    private void checkCurrentDate(Calendar calendar, TextView text, int i){
        if (calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
                currentCalendar.get(Calendar.DAY_OF_MONTH) == i
        ){
            text.setTextColor(Color.RED);
        }
    }

    private void addLoggedWorkoutsIcon(DataSnapshot loggedWorkouts, Calendar calendar, LinearLayout text, int i){
        for (DataSnapshot snapshot : loggedWorkouts.getChildren()) {
            Date loggedDate = snapshot.getValue(WorkoutLog.class).getLogged_date();
            loggedCalendar.setTime(loggedDate);
            if (calendar.get(Calendar.YEAR) == loggedCalendar.get(Calendar.YEAR) &&
                    calendar.get(Calendar.MONTH) == loggedCalendar.get(Calendar.MONTH) &&
                    loggedCalendar.get(Calendar.DAY_OF_MONTH) == i){
                text.setBackground(r.getDrawable(R.drawable.calendar_event));
            }
        }
    }

    private void showLoggedWorkouts(DataSnapshot dataSnapshot){
        LinearLayout selected_date_info = view.findViewById(R.id.selected_date_info);
        selected_date_info.removeAllViews();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
            WorkoutLog workout = snapshot.getValue(WorkoutLog.class);
            Calendar tempCalendar = Calendar.getInstance();
            tempCalendar.setTime(workout.getLogged_date());
            if (selectedCalendar.get(Calendar.YEAR) == tempCalendar.get(Calendar.YEAR) &&
                    selectedCalendar.get(Calendar.MONTH) == tempCalendar.get(Calendar.MONTH) &&
                    tempCalendar.get(Calendar.DAY_OF_MONTH) == selectedId) {
                TextView workoutText = new TextView(mContext);
                if (workout.isMy_workout()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    // Write a message to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("users/" + user.getUid() + "/my_workouts/" + workout.getWorkout_id());
                    // Read from the database
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("h:mm a");
                            if (dataSnapshot.exists()) {
                                FirebaseWorkout workoutInfo = dataSnapshot.getValue(FirebaseWorkout.class);

                                workoutText.setText(
                                        workoutInfo.getWorkout_name() + " " +
                                                simpleTimeFormat.format(workout.getLogged_date()));

                            }else{
                                workoutText.setText(
                                         "Deleted " +
                                                simpleTimeFormat.format(workout.getLogged_date()));
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value

                        }
                    });

                }else {
                    mHistoryViewModel.getWorkout(Integer.parseInt(workout.getWorkout_id())).observe(mLifeCycleOwner, workoutInfo -> {
                        mHistoryViewModel.getCard(workoutInfo.getCardID()).observe(mLifeCycleOwner, card -> {
                            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("h:mm a");

                            workoutText.setText(
                                    workoutInfo.getDifficulty() + " " +
                                            card.getWorkoutCategory() + " " +
                                            card.getWorkoutTitle() + " " +
                                            simpleTimeFormat.format(workout.getLogged_date()));
                        });
                    });
                }
                selected_date_info.addView(workoutText);
            }

        }
    }




}





