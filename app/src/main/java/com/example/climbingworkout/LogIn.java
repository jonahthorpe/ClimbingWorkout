package com.example.climbingworkout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {

    private Button logInButton;
    private Button logInGuestButton;
    private Button signUpButtonTop;
    private Button logInButtonTop;
    private Button signUpButton;
    private EditText usernameInputField;
    private EditText passwordInputField;
    private String username;
    private  String password;
    private DatabaseReference usersRef;
    private RelativeLayout signUpArea;
    private RelativeLayout logInArea;
    private int currentPage;
    private int topButtonSelected;
    private int topButtonNotSelected;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        signUpArea = findViewById(R.id.sign_up_area);
        logInArea = findViewById(R.id.log_in_area);

        // on button click, attempt login
        logInButton = findViewById(R.id.log_in_button);
        logInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                logIn();
            }
        });

        // on button click, continue as guest
        logInGuestButton = findViewById(R.id.guest);
        logInGuestButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                changeActivity();
            }
        });

        topButtonSelected = getResources().getColor(R.color.topGreen);
        topButtonNotSelected = getResources().getColor(R.color.defaultGrey);

        signUpButtonTop = findViewById(R.id.sign_up_top);
        signUpButtonTop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (signUpArea.getVisibility() == View.GONE){
                    signUpArea.setVisibility(View.VISIBLE);
                    logInArea.setVisibility(View.GONE);
                    logInButtonTop.setBackgroundColor(topButtonNotSelected);
                    signUpButtonTop.setBackgroundColor(topButtonSelected);
                    currentPage = 1;
                }
            }
        });

        logInButtonTop = findViewById(R.id.log_in_top);
        logInButtonTop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (logInArea.getVisibility() == View.GONE){
                    signUpArea.setVisibility(View.GONE);
                    logInArea.setVisibility(View.VISIBLE);
                    logInButtonTop.setBackgroundColor(topButtonSelected);
                    signUpButtonTop.setBackgroundColor(topButtonNotSelected);
                    currentPage = 0;
                }
            }
        });

        usernameInputField = findViewById(R.id.username);
        passwordInputField = findViewById(R.id.password);

        // set up firebase database
        // save a local version of the database
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            // check the query returned data
            if (dataSnapshot.exists()){
                // iterate through it
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    // get the user details
                    User user = child.getValue(User.class);
                    // check username and password matches
                    if( user.getUsername().equals(username) && user.getPassword().equals(password)){
                        // change screen
                        changeActivity();
                        // return input fields to default state
                        usernameInputField.setBackgroundResource(R.drawable.normal_edit_text);
                        passwordInputField.setBackgroundResource(R.drawable.normal_edit_text);
                    }else{
                        // update user
                        passwordInputField.setError("Incorrect Password");
                        passwordInputField.setBackgroundResource(R.drawable.error_edit_text);
                        usernameInputField.setBackgroundResource(R.drawable.normal_edit_text);
                    }
                }
            }else{
                Log.i("username", username);
                // update user
                usernameInputField.setError("User Not Found");
                usernameInputField.setBackgroundResource(R.drawable.error_edit_text);
                passwordInputField.setBackgroundResource(R.drawable.normal_edit_text);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    public void logIn(){
        // get credentials
        username = usernameInputField.getText().toString().trim();
        password = passwordInputField.getText().toString();
        // if username/password is entered, try log in
        if (!username.isEmpty() && !password.isEmpty()) {
            usersRef = FirebaseDatabase.getInstance().getReference("users");
            // sync database whenever there is a connection
            usersRef.keepSynced(true);
            Query query = usersRef.orderByChild("username").equalTo(username);
            query.addListenerForSingleValueEvent(valueEventListener);
        }else{
            // else, update user
            if (username.isEmpty()){
                usernameInputField.setError("Enter A Username");
                usernameInputField.setBackgroundResource(R.drawable.error_edit_text);
            }else{
                usernameInputField.setBackgroundResource(R.drawable.normal_edit_text);
            }
            if (password.isEmpty()){
                passwordInputField.setError("Enter A Password");
                passwordInputField.setBackgroundResource(R.drawable.error_edit_text);
            }else{
                passwordInputField.setBackgroundResource(R.drawable.normal_edit_text);
            }
        }
    }

    public void changeActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected  void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("currentPage", currentPage);
    }

    @Override
    protected  void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        currentPage = savedInstanceState.getInt("currentPage");
        if (currentPage == 1){
            signUpArea.setVisibility(View.VISIBLE);
            logInArea.setVisibility(View.GONE);
            logInButtonTop.setBackgroundColor(topButtonNotSelected);
            signUpButtonTop.setBackgroundColor(topButtonSelected);
        }
    }
}
