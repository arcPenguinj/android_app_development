package com.example.cookstorm.CommentRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.cookstorm.R;
import com.example.cookstorm.model.Comment;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    Context context;
    List<Comment> commentArrayList = new ArrayList<>();
    RequestManager glide;
    private OnLikeListener listener;

    public CommentAdapter(Context context, List<Comment> modelArrayList) {
        this.context = context;
        this.commentArrayList = modelArrayList;
        glide = Glide.with(context);

    }

    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment, parent, false);
        CommentViewHolder viewHolder = new CommentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        final Comment comment = commentArrayList.get(position);
        holder.tv_name.setText(comment.getName());
        holder.tv_time.setText(comment.getTime());

        holder.tv_comment.setText(comment.getCommentText());

        holder.tv_rankInfo.setText(comment.getRankInfo());

    }


    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_time, tv_comment, tv_rankInfo;


        public CommentViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_name_comment);
            tv_comment = (TextView) itemView.findViewById(R.id.comment_view);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time_comment);
            tv_rankInfo = (TextView) itemView.findViewById(R.id.tv_rankInfo_comment);
        }
    }
}
