package com.example.cookstorm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import com.example.cookstorm.CommentRecyclerView.CommentPageActivity;
import com.example.cookstorm.UserHomePage.UserPhoto;
import com.example.cookstorm.model.Post;
import com.example.cookstorm.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MainPostViewHolder> {
    Context context;
    List<Post> postArrayList = new ArrayList<>();
    RequestManager glide;
    private OnLikeListener listener;
    private DatabaseReference mDatabase;
    List<UserPhoto> userPhotos;
    User currentUser;

    public Adapter(Context context, List<Post> postArrayList, DatabaseReference mDatabase, User currentUser) {
        this.context = context;
        this.postArrayList = postArrayList;
        glide = Glide.with(context);
        this.mDatabase = mDatabase;
        userPhotos = Util.getPhotoList();
        this.currentUser = currentUser;
    }

    @Override
    public MainPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main_page, parent, false);
        MainPostViewHolder viewHolder = new MainPostViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MainPostViewHolder holder, int position) {
        final Post post = postArrayList.get(position);
        String uid = post.getAuthorId();
        mDatabase.child("users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting author data", task.getException());
                } else {
                    User author = task.getResult().getValue(User.class);

                    holder.tv_name.setText(author.getDisplayName());
                    holder.tv_time.setText(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(post.getTime()));


                    holder.tv_likes.setText(String.valueOf(post.getLikes()));
                    holder.tv_comments.setText(post.getComments() + " comments");
                    holder.tv_title.setText(post.getTitle());
                    holder.tv_rankInfo.setText(author.getRanking());
                    holder.tv_recipeField.setText(post.getRecipeField());

                    if (author.getPhotoImg() >= userPhotos.size()) {
                        holder.imgView_proPic.setVisibility(View.GONE);
                    } else {
                        holder.imgView_proPic.setVisibility(View.VISIBLE);
                        // glide.load(author.getPhotoImg()).into(holder.imgView_proPic);
                        holder.imgView_proPic.setImageDrawable(context.getDrawable(userPhotos.get(author.getPhotoImg()).getImg()));
                    }


                    if (post.getPostPic() == null) {
                        holder.imgView_postPic.setVisibility(View.GONE);
                    } else {
                        holder.imgView_postPic.setVisibility(View.VISIBLE);
                        holder.imgView_postPic.setImageBitmap(Util.StringToBitMap(post.getPostPic()));
                        // glide.load(post.getPostPic()).into(holder.imgView_postPic);
                    }
                    holder.post = post;

                    if (currentUser.getFavoritePosts() != null && currentUser.getFavoritePosts().contains(post.getId())) {
                        holder.heart.setLiked(true);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class MainPostViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_time, tv_likes, tv_comments, tv_title, tv_rankInfo, tv_recipeField;
        ImageView imgView_proPic, imgView_postPic;
        LikeButton heart, thumb;
        ImageButton comment;
        Post post;

        public MainPostViewHolder(View itemView) {
            super(itemView);
            imgView_proPic = (ImageView) itemView.findViewById(R.id.imgView_proPic);
            imgView_postPic = (ImageView) itemView.findViewById(R.id.imgView_postPic);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_comments = (TextView) itemView.findViewById(R.id.tv_comment);
            tv_likes = (TextView) itemView.findViewById(R.id.tv_like);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_rankInfo = (TextView) itemView.findViewById(R.id.tv_rankInfo);
            tv_recipeField = (TextView) itemView.findViewById(R.id.tv_recipe_field);
            heart = (LikeButton) itemView.findViewById(R.id.heart_button);
            thumb = (LikeButton) itemView.findViewById(R.id.thumb_button);
            comment = (ImageButton) itemView.findViewById(R.id.comment_button);
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CommentPageActivity.class);
                    intent.putExtra("postId", post.getId());
                    intent.putExtra("uname", currentUser.getDisplayName());
                    context.startActivity(intent);
                }
            });


            thumb.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    // Toast.makeText(context, "liked", Toast.LENGTH_SHORT).show();
                    post.likes();
                    tv_likes.setText(String.valueOf(post.getLikes()));
                    mDatabase.child("posts").child(post.getId()).setValue(post);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    // Toast.makeText(context, "unliked", Toast.LENGTH_SHORT).show();
                    post.unlikes();
                    tv_likes.setText(String.valueOf(post.getLikes()));
                    mDatabase.child("posts").child(post.getId()).setValue(post);
                }
            });

            heart.setOnLikeListener(new OnLikeListener(){
                @Override
                public void liked(LikeButton likeButton) {
                    currentUser.addFavoritePost(post.getId());
                    mDatabase.child("users").child(currentUser.getUid()).setValue(currentUser);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    currentUser.removeFavoritePost(post.getId());
                    mDatabase.child("users").child(currentUser.getUid()).setValue(currentUser);
                }
            });

        }
    }
}
