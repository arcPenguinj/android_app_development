package com.example.cookstorm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    Context context;
    ArrayList<Model> modelArrayList = new ArrayList<>();
    RequestManager glide;

    public Adapter(Context context, ArrayList<Model> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
        glide = Glide.with(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main_page, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Model model = modelArrayList.get(position);

        holder.tv_name.setText(model.getName());
        holder.tv_time.setText(model.getTime());
        holder.tv_likes.setText(String.valueOf(model.getLikes()));
        holder.tv_comments.setText(model.getComments() + "comments");
        holder.tv_title.setText(model.getTitle());
        holder.tv_rankInfo.setText(model.getRankInfo());
        holder.tv_recipeField.setText(model.getRecipeField());


        if (model.getProPic() == 0) {
            holder.imgView_proPic.setVisibility(View.GONE);
        } else {
            holder.imgView_proPic.setVisibility(View.VISIBLE);
            glide.load(model.getProPic()).into(holder.imgView_proPic);
        }


        if (model.getPostPic() == 0) {
            holder.imgView_postPic.setVisibility(View.GONE);
        } else {
            holder.imgView_postPic.setVisibility(View.VISIBLE);
            glide.load(model.getPostPic()).into(holder.imgView_postPic);
        }


    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_time, tv_likes, tv_comments, tv_title, tv_rankInfo, tv_recipeField;
        ImageView imgView_proPic, imgView_postPic;

        public MyViewHolder(View itemView) {
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

        }
    }
}
