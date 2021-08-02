package com.example.cookstorm.UserHomePage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cookstorm.R;
import com.example.cookstorm.UserHomePage.UserPhoto;

import java.util.ArrayList;

public class UserPhotoAdapter extends ArrayAdapter<UserPhoto> {

    public UserPhotoAdapter(Context context, ArrayList<UserPhoto> photoList) {
        super(context, 0, photoList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_photo, parent, false
            );
        }

        ImageView imageViewFlag = convertView.findViewById(R.id.image_view_flag);


        UserPhoto currentItem = getItem(position);

        if (currentItem != null) {
            imageViewFlag.setImageResource(currentItem.getImg());
        }

        return convertView;
    }
}
