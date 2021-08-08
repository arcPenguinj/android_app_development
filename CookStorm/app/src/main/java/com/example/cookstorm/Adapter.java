package com.example.cookstorm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import com.example.cookstorm.CommentRecyclerView.CommentPageActivity;
import com.example.cookstorm.model.Post;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MainPostViewHolder> {
    Context context;
    ArrayList<Post> postArrayList = new ArrayList<>();
    RequestManager glide;
    private OnLikeListener listener;


    public Adapter(Context context, ArrayList<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
        glide = Glide.with(context);

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

        holder.tv_name.setText(post.getName());
        holder.tv_time.setText(post.getTime());


        holder.tv_likes.setText(String.valueOf(post.getLikes()));
        holder.tv_comments.setText(post.getComments() + " comments");
        holder.tv_title.setText(post.getTitle());
        holder.tv_rankInfo.setText(post.getRankInfo());
        holder.tv_recipeField.setText(post.getRecipeField());




        if (post.getProPic() == 0) {
            holder.imgView_proPic.setVisibility(View.GONE);
        } else {
            holder.imgView_proPic.setVisibility(View.VISIBLE);
            glide.load(post.getProPic()).into(holder.imgView_proPic);
        }


        if (post.getPostPic() == 0) {
            holder.imgView_postPic.setVisibility(View.GONE);
        } else {
            holder.imgView_postPic.setVisibility(View.VISIBLE);
            glide.load(post.getPostPic()).into(holder.imgView_postPic);
        }

        holder.post = post;


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
                    context.startActivity(new Intent(context, CommentPageActivity.class));
                }
            });


            thumb.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    Toast.makeText(context, "liked", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    Toast.makeText(context, "unliked", Toast.LENGTH_SHORT).show();

                }
            });


        }
    }
}
