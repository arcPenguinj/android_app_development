package edu.neu.madcourse.numad21su_bochenmingyishiyicizhu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SendNewActivity extends AppCompatActivity {
    ImageView smile;
    ImageView angry;
    ImageView like;
    ImageView cry;

    Sticker selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_new);
        smile = (ImageView)findViewById(R.id.stickerSmile);
        angry = (ImageView)findViewById(R.id.stickerAngry);
        like = (ImageView)findViewById(R.id.stickerLike);
        cry = (ImageView)findViewById(R.id.stickerCry);
        selected = null;
    }

    public void clickOnSticker(View view) {
        unselectAll();
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable highlight = getResources().getDrawable(R.drawable.highlight);
        switch (view.getId()) {
            case R.id.stickerSmile:
                selected = Sticker.Smile;
                smile.setBackground(highlight);
                break;
            case R.id.stickerLike:
                selected = Sticker.Like;
                like.setBackground(highlight);
                break;
            case R.id.stickerAngry:
                selected = Sticker.Angry;
                angry.setBackground(highlight);
                break;
            case R.id.stickerCry:
                selected = Sticker.Cry;
                cry.setBackground(highlight);
                break;
            default:
                selected = null;
        }
    }

    private void unselectAll() {
        smile.setBackground(null);
        angry.setBackground(null);
        like.setBackground(null);
        cry.setBackground(null);
    }
}