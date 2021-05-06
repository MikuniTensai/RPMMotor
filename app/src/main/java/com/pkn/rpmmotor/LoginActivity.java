package com.pkn.rpmmotor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText edt_username, edt_password;
    Button btn_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.initComponents();

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void initComponents(){
        edt_username = findViewById(R.id.username);
        edt_password = findViewById(R.id.password);
        btn_Login = findViewById(R.id.btn_Login);
    }

    public void login(){
        this.initComponents();

        String username = edt_username.getText().toString();
        String password = edt_password.getText().toString();

        if (username.equals("") || password.equals("")){
            Toast.makeText(this, "Username or Password Blank", Toast.LENGTH_SHORT).show();
        }
        else if(username.equals("mikunitensai") && password.equals("123")){
            Intent login = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(login);
            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Username or Password do not match", Toast.LENGTH_SHORT).show();
        }
    }

}