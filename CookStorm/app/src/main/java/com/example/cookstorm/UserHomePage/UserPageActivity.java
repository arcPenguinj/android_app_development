package com.example.cookstorm.UserHomePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cookstorm.MainActivity;
import com.example.cookstorm.MainPageActivity;
import com.example.cookstorm.R;
import com.example.cookstorm.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserPageActivity extends AppCompatActivity {
    private ArrayList<UserPhoto> photoList;
    private UserPhotoAdapter mAdapter;
    private static final String TAG = "UserPageActivity";
    private EditText displayedNameTextView;
    private TextView emailTextView;
    private EditText phoneNumberTextView;
    private TextView likeNumber;
    private TextView favoriteNumber;
    private int userPhotoPosition;
    private Button signOut;
    private Button updateProfile;
    private DatabaseReference mDatabase;
    private String uid;
    private String email;
    private User currentUser;
    private Spinner spinnerPhotos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        emailTextView = (TextView) findViewById(R.id.user_email);
        phoneNumberTextView = (EditText) findViewById(R.id.phone_number);
        displayedNameTextView = (EditText) findViewById(R.id.display_name);
        getUserProfile();

        //Photo selection
        initList();
        spinnerPhotos = findViewById(R.id.spinner_pic);
        mAdapter = new UserPhotoAdapter(this, photoList);
        spinnerPhotos.setAdapter(mAdapter);
        spinnerPhotos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserPhoto userPhoto = (UserPhoto) parent.getItemAtPosition(position);
                userPhotoPosition = position;
                Toast.makeText(UserPageActivity.this, "User profile photo selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Sign out button
        signOut = findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        //Update profile button
        updateProfile = findViewById(R.id.update_profile);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
    }

    public void updateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String name = displayedNameTextView.getText().toString();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
        currentUser.setPhoneNumber(phoneNumberTextView.getText().toString().trim());
        currentUser.setPhotoImg(userPhotoPosition);
        updateUser(currentUser);
    }

    public void getUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            uid = user.getUid();
            currentUser = new User(uid, email);
            String name = user.getDisplayName();

            mDatabase.child("users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        currentUser = task.getResult().getValue(User.class);
                        int photoNumber = currentUser.getPhotoImg();

                        if (email != null) emailTextView.setText(email);
                        if (name != null && !name.isEmpty()) {
                            displayedNameTextView.setText(name);
                        } else {
                            displayedNameTextView.setText(uid);
                        }
                        phoneNumberTextView.setText(currentUser.getPhoneNumber() == null ? "000-000-0000" : currentUser.getPhoneNumber());
                        spinnerPhotos.setSelection(currentUser.getPhotoImg());
                    }
                }
            });


        }
    }

    public void updateUser(User user) {
        mDatabase.child("users").child(user.getUid()).setValue(user);
        Log.d(TAG, "User details updated.");
    }

    public void sendPasswordReset() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = "user@example.com";

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UserPageActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void initList() {
        photoList = new ArrayList<>();

        photoList.add(new UserPhoto(R.drawable.joey));
        photoList.add(new UserPhoto(R.drawable.obama));
        photoList.add(new UserPhoto(R.drawable.bear));
        photoList.add(new UserPhoto(R.drawable.elonmusk));
        photoList.add(new UserPhoto(R.drawable.kanye));
    }
}
