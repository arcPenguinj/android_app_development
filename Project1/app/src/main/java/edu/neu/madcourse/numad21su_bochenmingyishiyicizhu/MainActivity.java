package edu.neu.madcourse.numad21su_bochenmingyishiyicizhu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText eName;
    private Button eLogin;

    private DatabaseReference mDatabase;

    private List<User> knownUsers;
    private String client_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        eName = findViewById(R.id.editText);
        eLogin = findViewById(R.id.button12);

        // Generate the token for the first time, then no need to do later
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Something is wrong!", Toast.LENGTH_SHORT).show();
                } else {
                    client_token = task.getResult();
                    Log.d("Main", "Generated token: " + client_token);
                    ((StickItApplication)getApplication()).setFirebase_client_token(client_token);
                }
            }
        });

        // Connect with firebase
        //
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                        GenericTypeIndicator<List<User>> users = new GenericTypeIndicator<List<User>>() {};
                        knownUsers = snapshot.getValue(users);
                        if (knownUsers == null) {
                            Toast.makeText(getApplicationContext(),"No existing players!",Toast.LENGTH_SHORT).show();
                            knownUsers = new ArrayList<>();
                        } else {
                            Toast.makeText(getApplicationContext(),"Existing players: " + knownUsers.size(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                    }
                }
        );

        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputName = eName.getText().toString();
                if(inputName.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter user name.", Toast.LENGTH_SHORT).show();
                } else {
                    inputName = inputName.trim();
                    if (knownUsers.contains(new User(inputName))) { // log in as existing user
                        User user = knownUsers.get(knownUsers.indexOf(new User(inputName)));
                        user.token = client_token;
                        ((StickItApplication)getApplication()).setUsername(user.username);
                        mDatabase.child("users").setValue(knownUsers); // update this user's token in db
                        Toast.makeText(MainActivity.this, "Welcome back, " + inputName + "!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else { // new user
                        ((StickItApplication)getApplication()).setUsername(inputName);

                        if (knownUsers == null) {
                            knownUsers = new ArrayList<>();
                        }
                        knownUsers.add(new User(inputName, client_token));
                        mDatabase.child("users").setValue(knownUsers);
                        Toast.makeText(MainActivity.this, "Login successful as new user!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}