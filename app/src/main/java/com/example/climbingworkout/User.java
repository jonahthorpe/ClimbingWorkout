package com.example.climbingworkout;

public class User {

    public String u_id;
    public String logged_workouts;
    public String my_workouts;

    public User( String logged_workouts, String my_workouts) {
        this.logged_workouts = logged_workouts;
        this.my_workouts = my_workouts;
    }
}
