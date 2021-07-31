package com.example.cookstorm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Name = (EditText) findViewById(R.id.etUserName);
        Password = (EditText) findViewById(R.id.etPassword);

        TextView btn=findViewById(R.id.textViewSignUp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

        Login = (Button) findViewById(R.id.btnLogin);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });
    }


    // need further implement
    private void validate(String userName, String userPassword) {
        if (userName.equals("ad") && (userPassword.equals("12"))) {
            Intent intent = new Intent(MainActivity.this, MainPageActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(MainActivity.this, "Invalid Username or Password",Toast.LENGTH_SHORT).show();
        }
    }
}