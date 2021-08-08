package com.example.cookstorm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private EditText emailTextView;
    private EditText passwordTextView;
    private Button Login;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    private Button forgetPswd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTextView = findViewById(R.id.etEmail);
        passwordTextView = findViewById(R.id.etPassword);
        progressbar = findViewById(R.id.progressBar);
        forgetPswd = findViewById(R.id.forgotPassword);

        // taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        TextView btn = findViewById(R.id.textViewSignUp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        Login = findViewById(R.id.btnLogin);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailTextView.getText().toString();
                String password = passwordTextView.getText().toString();


                // TODO: only for developing, remove this code after release.
                if (email.equals("ad") && password.equals("123")) {
                    Intent intent = new Intent(MainActivity.this, MainPageActivity.class);
                    startActivity(intent);
                } else {
                    if (validate(email, password)) {
                        loginUserAccount(email, password);
                    }
                }
            }
        });

        forgetPswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTextView.getText().toString();
                if (email != null && !email.isEmpty()) {
                    mAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener(new OnSuccessListener() {
                        public void onSuccess(Object result) {
                            // send email succeeded
                            Toast.makeText(getApplicationContext(),
                                    "Reset password succeeded, please check email",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        public void onFailure(Exception e) {
                        // something bad happened
                            Toast.makeText(getApplicationContext(),
                                    "Reset password failed, " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                    }
                });
                }
            }
        });
    }

    // validate the email address and password
    private boolean validate(String email, String password) {
        // validations for input email and password
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(),
                    "Please enter a validate email address!!",
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        return true;
    }

    private void loginUserAccount(String email, String password) {

        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                            "Login successful!!",
                                            Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressbar.setVisibility(View.GONE);

                                    // if sign-in is successful
                                    // intent to home activity
                                    Intent intent = new Intent(MainActivity.this, MainPageActivity.class);
                                    startActivity(intent);
                                } else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                            "Login failed!! " + task.getException().getMessage(),
                                            Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressbar.setVisibility(View.GONE);
                                }
                            }
                        });
    }
}