package com.example.climbingworkout;

import java.util.Date;

public class WorkoutLog {

    public String workout_id;
    public Date logged_date;
    public Boolean my_workout;

    WorkoutLog(String workout_id, Date logged_date, Boolean my_workout) {
        this.workout_id = workout_id;
        this.logged_date = logged_date;
        this.my_workout = my_workout;
    }

    public WorkoutLog() {}

    String getWorkout_id() {
        return workout_id;
    }

    public void setWorkout_id(String workout_id) {
        this.workout_id = workout_id;
    }

    Date getLogged_date() {
        return logged_date;
    }

    public void setLogged_date(Date logged_date) {
        this.logged_date = logged_date;
    }

    Boolean isMy_workout() {
        return my_workout;
    }

    public void setMy_workout(Boolean my_workout) {
        this.my_workout = my_workout;
    }
}
