package com.example.cookstorm.CommentRecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cookstorm.Adapter;
import com.example.cookstorm.MainActivity;
import com.example.cookstorm.MainPageActivity;
import com.example.cookstorm.R;
import com.example.cookstorm.RegisterActivity;
import com.example.cookstorm.model.Comment;
import com.example.cookstorm.model.Post;
import com.example.cookstorm.model.PostComments;
import com.example.cookstorm.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CommentPageActivity extends AppCompatActivity {
    FloatingActionButton cmtBnt;
    FloatingActionButton backBnt;
    RecyclerView commentRecyclerView;
    List<Comment> commentArrayList;
    CommentAdapter commentAdapter;

    String postId;
    String userName;

    private DatabaseReference mDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_page);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            postId = b.getString("postId");
            userName = b.getString("uname");
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();

        commentRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_comment);

        cmtBnt = findViewById(R.id.commentBtn);

        cmtBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingComment();
            }
        });

        backBnt = findViewById(R.id.backBtn);
        backBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CommentPageActivity.this, MainPageActivity.class));
            }
        });

        loadComments();
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

                String comment = commentPost.getText().toString();

                commentArrayList.add(
                        new Comment(
                                UUID.randomUUID().toString(),
                                userName,
                                "beginner",
                                new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()),
                                comment));
                mDatabase.child("comments").child(postId).setValue(commentArrayList);
                reCalculateCommmentsForPost();
                commentAdapter.notifyDataSetChanged();
                Toast.makeText(CommentPageActivity.this, "posted!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // Toast.makeText(CommentPageActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

            }
        });

        dialog.create();
        dialog.show();
    }

    private void loadComments() {
        mDatabase.child("comments").child(postId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DataSnapshot> task) {
                 if (!task.isSuccessful()) {
                     Toast.makeText(CommentPageActivity.this, "Error getting comments!" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                 } else {
                     GenericTypeIndicator<List<Comment>> t = new GenericTypeIndicator<List<Comment>>() {};
                     commentArrayList = task.getResult().getValue(t);
                     if (commentArrayList == null) commentArrayList = new ArrayList<>();

                     RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CommentPageActivity.this);
                     commentRecyclerView.setLayoutManager(layoutManager);
                     commentAdapter = new CommentAdapter(CommentPageActivity.this, commentArrayList);
                     commentRecyclerView.setAdapter(commentAdapter);
                 }
             }
         }
        );
    }

    private void reCalculateCommmentsForPost() {
        mDatabase.child("posts").child(postId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(CommentPageActivity.this, "Error getting posts!" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Post post = task.getResult().getValue(Post.class);
                    post.setComments(commentArrayList.size());
                    mDatabase.child("posts").child(postId).setValue(post);
                }
            }
        });
    }
}
