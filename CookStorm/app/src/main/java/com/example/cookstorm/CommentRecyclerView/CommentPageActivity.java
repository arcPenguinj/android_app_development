package com.example.cookstorm.CommentRecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cookstorm.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CommentPageActivity extends AppCompatActivity {
    FloatingActionButton cmtBnt;
    RecyclerView commentRecyclerView;
    ArrayList<CommentModel> commentModelArrayList;
    CommentAdapter commentAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_page);
        commentRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_comment);
        commentModelArrayList = new ArrayList<>();

        cmtBnt = findViewById(R.id.commentBtn);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        commentRecyclerView.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter(this, commentModelArrayList);
        commentRecyclerView.setAdapter(commentAdapter);

        cmtBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingComment();
            }
        });

        populateCommentRecyclerView();


    }


    public void addingComment() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.add_comment, null);


        EditText commentPost = (EditText) view.findViewById(R.id.et_postCommentText);


        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setView(view);
        dialog.setPositiveButton("Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CommentPageActivity.this, "posted!", Toast.LENGTH_SHORT).show();

                String comment = commentPost.getText().toString();

                // 除了以上三个添加的参数信息， 其他参数需要从database来？
                commentModelArrayList.add(new CommentModel(10032, "Ben", "beginner", "20hrs ago", comment));
                commentAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });


        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(CommentPageActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

            }
        });

        dialog.create();
        dialog.show();
    }


    public void populateCommentRecyclerView() {
        CommentModel comment1 = new CommentModel(1, "ken", "beginner", "1hrs ago", "very nice dish");
        CommentModel comment2 = new CommentModel(2, "rick", "average", "2hrs ago", "like your post!");



        commentModelArrayList.add(comment1);
        commentModelArrayList.add(comment2);

        commentAdapter.notifyDataSetChanged();
    }
}
