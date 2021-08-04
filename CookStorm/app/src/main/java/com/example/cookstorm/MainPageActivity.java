package com.example.cookstorm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cookstorm.UserHomePage.UserPageActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.like.LikeButton;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {
    FloatingActionButton addsBtn;
    FloatingActionButton homeBtn;
    RecyclerView recyclerView;
    ArrayList<Model> modelArrayList;
    Adapter adapter;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        addsBtn = findViewById(R.id.addingBtn);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        modelArrayList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(this, modelArrayList);
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
        View view = inflater.inflate(R.layout.add_post, null);

        ImageView picPost = (ImageView) view.findViewById(R.id.imagePost);
        EditText textPost = (EditText) view.findViewById(R.id.et_postText);
        EditText titlePost = (EditText) view.findViewById(R.id.et_postTitle);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setView(view);
        dialog.setPositiveButton("Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainPageActivity.this, "posted!", Toast.LENGTH_SHORT).show();
                String text = textPost.getText().toString();
                int imgPost = picPost.getImageAlpha();
                String title = titlePost.getText().toString();

                // 除了以上三个添加的参数信息， 其他参数需要从database来？
                modelArrayList.add(new Model(10032, 13, 20, 2313, imgPost, "new user", "5 hrs ago", title, "Beginner", text));
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

    public void populateRecyclerView() {
        Model modelObama = new Model(1, 7, 8,
                R.drawable.obama, R.drawable.dumpling,
                "Obama", "10 hours ago", "Chinese dumpling",
                "Master", "buy some fresh dumpling to cook");

        Model modelElon = new Model(2, 92, 9,
                R.drawable.elonmusk, R.drawable.bbq,
                "Elon Musk", "12 hours ago", "Texas BBQ",
                "Master", "Step 1 Place ribs in a large pot with enough water to cover. Season with garlic powder, " +
                "black pepper and salt. Bring water to a boil, and cook ribs until tender.\n\nStep 2 " +
                "Preheat oven to 325 degrees F (165 degrees C).\n\nStep 3 " +
                "Remove ribs from pot, and place them in a 9x13 inch baking dish. Pour barbeque sauce over ribs. Cover dish with aluminum foil, " +
                "and bake in the preheated oven for 1 to 1 1/2 hours, or until internal temperature of pork has reached 160 degrees F (70 degrees C)");

        modelArrayList.add(modelObama);
        modelArrayList.add(modelElon);

        adapter.notifyDataSetChanged();
    }

}