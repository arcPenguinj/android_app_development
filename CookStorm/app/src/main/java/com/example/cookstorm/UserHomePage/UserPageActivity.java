package com.example.cookstorm.UserHomePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.example.cookstorm.Util;
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
    private TextView awardsTextView;
    private EditText phoneNumberTextView;
    private EditText addressTextView;
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
        awardsTextView = (TextView) findViewById(R.id.awards);
        phoneNumberTextView = (EditText) findViewById(R.id.phone_number);
        displayedNameTextView = (EditText) findViewById(R.id.display_name);
        addressTextView = (EditText) findViewById(R.id.user_address);
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
                // Toast.makeText(UserPageActivity.this, "User profile photo selected", Toast.LENGTH_SHORT).show();
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

        currentUser.setPhoneNumber(phoneNumberTextView.getText().toString().trim());
        currentUser.setPhotoImg(userPhotoPosition);
        currentUser.setAddress(addressTextView.getText().toString().trim());
        currentUser.setDisplayName(name.trim());

        updateUser(currentUser);

        Intent intent = new Intent(UserPageActivity.this, MainPageActivity.class);
        startActivity(intent);
    }

    public void getUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            uid = user.getUid();
            currentUser = new User(uid, email);

            mDatabase.child("users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        currentUser = task.getResult().getValue(User.class);

                        if (email != null) emailTextView.setText(email);
                        String name = currentUser.getDisplayName();
                        if (name != null && !name.isEmpty()) {
                            displayedNameTextView.setText(name);
                        } else {
                            displayedNameTextView.setText(email);
                        }
                        phoneNumberTextView.setText(currentUser.getPhoneNumber() == null ? "000-000-0000" : currentUser.getPhoneNumber());
                        addressTextView.setText(currentUser.getAddress() == null ? "address" : currentUser.getAddress());
                        spinnerPhotos.setSelection(currentUser.getPhotoImg());

                        TextView favoritePostsTextView = (TextView) findViewById(R.id.favorite_posts);
                        favoritePostsTextView.setText(currentUser.getFavoritePostSize());
                        TextView myPostsTextView = (TextView) findViewById(R.id.my_posts);
                        myPostsTextView.setText(currentUser.getMyPostsSize());

                        StringBuilder awardsString = new StringBuilder();
                        if (Integer.parseInt(currentUser.getMyPostsSize()) >= 1) {
                            awardsString.append("First Post Award | ");
                        }
                        if (Integer.parseInt(currentUser.getMyPostsSize()) >= 5) {
                            awardsString.append("Five Posts Award | ");
                        }
                        if (Integer.parseInt(currentUser.getFavoritePostSize()) >= 1) {
                            awardsString.append("First Favorite Award | ");
                        }
                        String awards = awardsString.toString();
                        if (!awards.isEmpty() && awards.length() >= 5) {
                            awardsTextView.setText(awards);
                        }
                    }
                }
            });
        }
    }

    public void updateUser(User user) {
        mDatabase.child("users").child(user.getUid()).setValue(user);
        Log.d(TAG, "User details updated.");
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UserPageActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void initList() {
        photoList = Util.getPhotoList();
    }
}
