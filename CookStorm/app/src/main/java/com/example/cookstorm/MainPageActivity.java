package com.example.cookstorm;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;

import com.example.cookstorm.UserHomePage.UserPageActivity;
import com.example.cookstorm.model.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {
    FloatingActionButton addsBtn;
    FloatingActionButton homeBtn;
    RecyclerView recyclerView;
    ArrayList<Post> postArrayList;
    Adapter adapter;
    FirebaseUser user;
    View addPostView;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        addsBtn = findViewById(R.id.addingBtn);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        postArrayList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(this, postArrayList);
        recyclerView.setAdapter(adapter);


        addsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingPost();
            }
        });

        populateRecyclerView();

        homeBtn = findViewById(R.id.homeBtn);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserPageActivity();
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

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setView(addPostView);
        dialog.setPositiveButton("Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainPageActivity.this, "posted!", Toast.LENGTH_SHORT).show();
                String text = textPost.getText().toString();
                int imgPost = picPost.getImageAlpha();
                String title = titlePost.getText().toString();

                // 除了以上三个添加的参数信息， 其他参数需要从database来？
                postArrayList.add(new Post(10032, 13, 20, 2313, imgPost, "new user", "5 hrs ago", title, "Beginner", text));
                adapter.notifyDataSetChanged();
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

    public void populateRecyclerView() {
        Post postObama = new Post(1, 7, 8,
                R.drawable.obama, R.drawable.dumpling,
                "Obama", "10 hours ago", "Chinese dumpling",
                "Master", "buy some fresh dumpling to cook");

        Post postElon = new Post(2, 92, 9,
                R.drawable.elonmusk, R.drawable.bbq,
                "Elon Musk", "12 hours ago", "Texas BBQ",
                "Master", "Step 1 Place ribs in a large pot with enough water to cover. Season with garlic powder, " +
                "black pepper and salt. Bring water to a boil, and cook ribs until tender.\n\nStep 2 " +
                "Preheat oven to 325 degrees F (165 degrees C).\n\nStep 3 " +
                "Remove ribs from pot, and place them in a 9x13 inch baking dish. Pour barbeque sauce over ribs. Cover dish with aluminum foil, " +
                "and bake in the preheated oven for 1 to 1 1/2 hours, or until internal temperature of pork has reached 160 degrees F (70 degrees C)");

        postArrayList.add(postObama);
        postArrayList.add(postElon);

        adapter.notifyDataSetChanged();
    }

}