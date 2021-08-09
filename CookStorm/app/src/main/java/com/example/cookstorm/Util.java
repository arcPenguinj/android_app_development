package com.example.cookstorm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.cookstorm.UserHomePage.UserPhoto;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Util {
    public static ArrayList<UserPhoto> getPhotoList() {
        ArrayList<UserPhoto> photoList = new ArrayList<>();

        photoList.add(new UserPhoto(R.drawable.joey));
        photoList.add(new UserPhoto(R.drawable.obama));
        photoList.add(new UserPhoto(R.drawable.bear));
        photoList.add(new UserPhoto(R.drawable.elonmusk));
        photoList.add(new UserPhoto(R.drawable.kanye));
        return photoList;
    }

    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream ByteStream=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, ByteStream);
        byte [] b=ByteStream.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
