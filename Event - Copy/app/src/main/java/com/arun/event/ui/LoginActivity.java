package com.arun.event.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arun.event.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edUserName, edPassword;
    Button btnLogin;
    String userName, password;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edUserName = findViewById(R.id.edUserName);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("position");
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            userName = edUserName.getText().toString().trim();
            password = edPassword.getText().toString().trim();

            if (userName.length() != 0 && password.length() != 0) {
                if (userName.equalsIgnoreCase("dean") && password.equalsIgnoreCase("dean")) {
                    Intent bundle = new Intent();
                    bundle.putExtra("position", position);
                    bundle.putExtra("login", true);
                    setResult(RESULT_OK, bundle);
                    finish();
                } else Toast.makeText(this, "Incorrect username and password", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(this, "Please enter the username and password", Toast.LENGTH_SHORT).show();
        }
    }
}