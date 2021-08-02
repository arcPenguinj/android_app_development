package com.example.cookstorm.UserHomePage;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cookstorm.R;

import java.util.ArrayList;

public class UserPageActivity extends AppCompatActivity {
    private ArrayList<UserPhoto> photoList;
    private UserPhotoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page);

        initList();

        Spinner spinnerPhotos = findViewById(R.id.spinner_pic);

        mAdapter = new UserPhotoAdapter(this, photoList);
        spinnerPhotos.setAdapter(mAdapter);

        spinnerPhotos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserPhoto clickedItem = (UserPhoto) parent.getItemAtPosition(position);

                Toast.makeText(UserPageActivity.this, "User profile photo selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initList() {
        photoList = new ArrayList<>();

        photoList.add(new UserPhoto(R.drawable.joey));
        photoList.add(new UserPhoto( R.drawable.obama));
        photoList.add(new UserPhoto(R.drawable.bear));
        photoList.add(new UserPhoto( R.drawable.elonmusk));
        photoList.add(new UserPhoto( R.drawable.kanye));

    }
}
