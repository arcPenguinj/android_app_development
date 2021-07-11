package edu.neu.madcourse.numad21su_bochenmingyishiyicizhu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private TextView userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userInfo = (TextView)findViewById(R.id.logInfoId);
        userInfo.setText("Logged in as: " + ((StickItApplication)getApplication()).getUsername());
    }

    public void openActivity2(View view) {
        Intent intent = new Intent(this, SentHistoryActivity.class);
        startActivity(intent);
    }

    public void openActivity3(View view) {
        Intent intent = new Intent(this, ReceivedHistoryActivity.class);
        startActivity(intent);
    }

    public void openActivity4(View view) {
        Intent intent = new Intent(this, SendNewActivity.class);
        startActivity(intent);
    }


}