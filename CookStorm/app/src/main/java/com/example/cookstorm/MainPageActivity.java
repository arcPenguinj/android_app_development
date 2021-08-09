package com.example.cookstorm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.example.cookstorm.UserHomePage.UserPageActivity;
import com.example.cookstorm.model.Post;
import com.example.cookstorm.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MainPageActivity extends AppCompatActivity {
    FloatingActionButton addsBtn;
    FloatingActionButton homeBtn;
    RecyclerView recyclerView;
    List<Post> postArrayList;
    Adapter adapter;
    User currentUser;
    View addPostView;

    private DatabaseReference mDatabase;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    private static final int PICK_IMAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        addsBtn = findViewById(R.id.addingBtn);
        addsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingPost();
            }
        });

        homeBtn = findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserPageActivity();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        postArrayList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        mDatabase.child("users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(MainPageActivity.this, "Error getting author data!" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    currentUser = task.getResult().getValue(User.class);
                    loadPosts();
                }
            }
        });
    }

    public void openUserPageActivity() {
        Intent intent = new Intent(this, UserPageActivity.class);
        startActivity(intent);
    }

    public void addingPost() {
        LayoutInflater inflater = LayoutInflater.from(this);
        addPostView = inflater.inflate(R.layout.add_post, null);

        ImageView picPost = (ImageView) addPostView.findViewById(R.id.imagePost);
        picPost.setDrawingCacheEnabled(true);
        EditText textPost = (EditText) addPostView.findViewById(R.id.et_postText);
        EditText titlePost = (EditText) addPostView.findViewById(R.id.et_postTitle);
        Button photoButton = (Button) addPostView.findViewById(R.id.photoButton);
        Button galleryButton = (Button) addPostView.findViewById(R.id.galleryButton);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openGallery();
            }
        });

        picPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setView(addPostView);
        dialog.setPositiveButton("Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainPageActivity.this, "posted!", Toast.LENGTH_SHORT).show();
                String text = textPost.getText().toString();
                picPost.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) picPost.getDrawable()).getBitmap();
                String title = titlePost.getText().toString();
                String id = UUID.randomUUID().toString();
                Post newPost = new Post(
                        id,
                        0,
                        0,
                        Util.BitMapToString(bitmap),
                        new Date(),
                        title,
                        text,
                        currentUser.getUid());
                mDatabase.child("posts").child(id).setValue(newPost);
                // reCalculateMyPosts();
                currentUser.addPost(id);
                mDatabase.child("users").child(currentUser.getUid()).setValue(currentUser);
                loadPosts();
                dialog.dismiss();
            }
        });


        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(MainPageActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

            }
        });

        dialog.create();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            if (addPostView != null) {
                ImageView picPost = (ImageView) addPostView.findViewById(R.id.imagePost);
                picPost.setImageBitmap(photo);
            }
        }
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            if (addPostView != null) {
                ImageView picPost = (ImageView) addPostView.findViewById(R.id.imagePost);
                picPost.setImageURI(imageUri);
            }
        }
    }

    private void openGallery() {
        Intent gallery =
                new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void loadPosts() {
        mDatabase.child("posts").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                 GenericTypeIndicator<HashMap<String, Post>> t = new GenericTypeIndicator<HashMap<String, Post>>() {};
                 HashMap<String, Post> postsMap = task.getResult().getValue(t);
                 if (postsMap == null || postsMap.size() == 0) {
                     Toast.makeText(getApplicationContext(),"no posts yet!",Toast.LENGTH_SHORT).show();
                 } else {
                     postArrayList = new ArrayList<>(postsMap.values());
                     RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainPageActivity.this);
                     recyclerView.setLayoutManager(layoutManager);
                     adapter = new Adapter(MainPageActivity.this, postArrayList, mDatabase, currentUser);
                     recyclerView.setAdapter(adapter);
                 }
             }
         }
        );
    }

    private void reCalculateMyPosts() {
        List<String> myPosts = new ArrayList<>();
        for(Post p : postArrayList) {
            if (p.getAuthorId().equalsIgnoreCase(currentUser.getUid())) {
                myPosts.add(p.getId());
            }
        }
        currentUser.setMyPosts(myPosts);
    }

}