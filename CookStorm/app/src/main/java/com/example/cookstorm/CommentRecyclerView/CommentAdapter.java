package com.example.cookstorm.CommentRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.cookstorm.Model;
import com.example.cookstorm.R;
import com.like.OnLikeListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    Context context;
    ArrayList<CommentModel> commentModelArrayList = new ArrayList<>();
    RequestManager glide;
    private OnLikeListener listener;

    public CommentAdapter(Context context, ArrayList<CommentModel> modelArrayList) {
        this.context = context;
        this.commentModelArrayList = modelArrayList;
        glide = Glide.with(context);

    }

    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment, parent, false);
        CommentViewHolder viewHolder = new CommentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        final CommentModel commentModel = commentModelArrayList.get(position);
        holder.tv_name.setText(commentModel.getName());
        holder.tv_time.setText(commentModel.getTime());

        holder.tv_comment.setText(commentModel.getCommentText());

        holder.tv_rankInfo.setText(commentModel.getRankInfo());

    }


    @Override
    public int getItemCount() {
        return commentModelArrayList.size();
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
