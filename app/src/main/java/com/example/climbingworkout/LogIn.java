package com.example.climbingworkout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class LogIn extends AppCompatActivity {

    private Button signUpButtonTop;
    private Button logInButtonTop;
    private EditText emailInputField;
    private EditText passwordInputField;
    private EditText signUpPassword;
    private EditText signUpPasswordConfirm;
    private EditText signUpEmail;
    private String email;
    private  String password;
    private RelativeLayout signUpArea;
    private RelativeLayout logInArea;
    private int currentPage;
    private int topButtonSelected;
    private int topButtonNotSelected;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.log_in);

        signUpArea = findViewById(R.id.sign_up_area);
        logInArea = findViewById(R.id.log_in_area);

        // on button click, attempt login
        Button logInButton = findViewById(R.id.log_in_button);
        logInButton.setOnClickListener(view -> logIn());

        // on button click, continue as guest
        Button logInGuestButton = findViewById(R.id.guest);
        logInGuestButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            changeActivity();
        });

        // on button click, attempt login
        Button signUpButton = findViewById(R.id.sign_up);
        signUpButton.setOnClickListener(view -> signUp());

        topButtonSelected = getResources().getColor(R.color.topGreen);
        topButtonNotSelected = getResources().getColor(R.color.defaultGrey);

        signUpButtonTop = findViewById(R.id.sign_up_top);
        signUpButtonTop.setOnClickListener(view -> {
            if (signUpArea.getVisibility() == View.GONE){
                signUpArea.setVisibility(View.VISIBLE);
                logInArea.setVisibility(View.GONE);
                logInButtonTop.setBackgroundColor(topButtonNotSelected);
                signUpButtonTop.setBackgroundColor(topButtonSelected);
                currentPage = 1;
            }
        });

        logInButtonTop = findViewById(R.id.log_in_top);
        logInButtonTop.setOnClickListener(view -> {
            if (logInArea.getVisibility() == View.GONE){
                signUpArea.setVisibility(View.GONE);
                logInArea.setVisibility(View.VISIBLE);
                logInButtonTop.setBackgroundColor(topButtonSelected);
                signUpButtonTop.setBackgroundColor(topButtonNotSelected);
                currentPage = 0;
            }
        });

        emailInputField = findViewById(R.id.email);
        passwordInputField = findViewById(R.id.password);
        signUpPassword = findViewById(R.id.sign_up_password);
        signUpPasswordConfirm = findViewById(R.id.sign_up_password_confirm);
        signUpEmail = findViewById(R.id.sign_up_email);

    }





    public void logIn() {
        // get credentials
        email = emailInputField.getText().toString().trim();
        password = passwordInputField.getText().toString();
        Log.i("email", email);
        // if email/password is entered, try log in
        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            changeActivity();
                            emailInputField.setText("");
                            passwordInputField.setText("");
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                            switch (Objects.requireNonNull(task.getException()).toString()){
                                case "com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted.":
                                    emailInputField.setError("User not found!");
                                    emailInputField.setBackgroundResource(R.drawable.error_edit_text);
                                    break;
                                case "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password.":
                                    passwordInputField.setError("Invalid password!");
                                    passwordInputField.setBackgroundResource(R.drawable.error_edit_text);
                                    break;

                            }
                        }
                    });
        }else{
            // else, update user
            if (email.isEmpty()){
                emailInputField.setError("Enter A Email");
                emailInputField.setBackgroundResource(R.drawable.error_edit_text);
            }else{
                emailInputField.setBackgroundResource(R.drawable.normal_edit_text);
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
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    @Override
    protected  void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("currentPage", currentPage);
    }

    @Override
    protected  void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        currentPage = savedInstanceState.getInt("currentPage");
        if (currentPage == 1){
            signUpArea.setVisibility(View.VISIBLE);
            logInArea.setVisibility(View.GONE);
            logInButtonTop.setBackgroundColor(topButtonNotSelected);
            signUpButtonTop.setBackgroundColor(topButtonSelected);
        }
    }

    protected void signUp(){
        password = signUpPassword.getText().toString();
        String confirmPassword = signUpPasswordConfirm.getText().toString();
        email = signUpEmail.getText().toString();
        boolean valid = true;

        if (email.isEmpty()) {
            signUpEmail.setError("Enter An Email");
            signUpEmail.setBackgroundResource(R.drawable.error_edit_text);
            valid = false;
        } else {
            signUpEmail.setBackgroundResource(R.drawable.normal_edit_text);

        }
        if (password.isEmpty()) {
            signUpPassword.setError("Enter A Password");
            signUpPassword.setBackgroundResource(R.drawable.error_edit_text);
            valid = false;
        } else {
            signUpPassword.setBackgroundResource(R.drawable.normal_edit_text);
        }
        if (confirmPassword.isEmpty()){
            signUpPasswordConfirm.setError("Enter A Password");
            signUpPasswordConfirm.setBackgroundResource(R.drawable.error_edit_text);
            valid = false;
        } else {
            signUpPasswordConfirm.setBackgroundResource(R.drawable.normal_edit_text);
        }

        if (!password.equals(confirmPassword)){
            valid = false;
            signUpPasswordConfirm.setError("Passwords Don't Match");
            signUpPasswordConfirm.setBackgroundResource(R.drawable.error_edit_text);
        }

        if (valid){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Write a message to the database
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users");

                            myRef.child(user.getUid()).setValue(new User("empty", "empty"));
                            changeActivity();
                            signUpEmail.setText("");
                            signUpPassword.setText("");
                            signUpPasswordConfirm.setText("");
                        } else {
                            // If sign in fails, display a message to the user.
                            switch (Objects.requireNonNull(task.getException()).toString()){
                                case "com.google.firebase.auth.FirebaseAuthWeakPasswordException: The given password is invalid. [ Password should be at least 6 characters ]":
                                    signUpPassword.setError("Password Too Weak, Should be at least 6 characters");
                                    signUpPassword.setBackgroundResource(R.drawable.error_edit_text);
                                    break;
                                case "com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.":
                                    signUpEmail.setError("Email Already In Use");
                                    signUpEmail.setBackgroundResource(R.drawable.error_edit_text);
                                    break;
                                case "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.":
                                    signUpEmail.setError("Email Badly Formatted");
                                    signUpEmail.setBackgroundResource(R.drawable.error_edit_text);
                                    break;
                            }
                        }

                        // ...
                    });
        }

    }

}
