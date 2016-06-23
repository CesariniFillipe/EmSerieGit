package com.bcc.unifal.emserie;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bcc.unifal.emserie.Json.JsonSeries;
import com.bcc.unifal.emserie.database.DBController;
import com.bcc.unifal.emserie.database.SessionManager;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btn = (Button) findViewById(R.id.sendRegisterButton);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DBController dbController = new DBController(getBaseContext());
                EditText loginE = (EditText) findViewById(R.id.emailRegisterEditText);
                EditText passwordE = (EditText) findViewById(R.id.passwordRegisterEditText);
                EditText confirmPasswordE = (EditText) findViewById(R.id.confirmPasswordRegisterEditText);

                String login = loginE.getText().toString();
                String password = passwordE.getText().toString();
                String confirmPassword = confirmPasswordE.getText().toString();
                String result = "";

                if(password.compareTo(confirmPassword) == 0){
                    try {
                        result = dbController.registerUser(login, password);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    result = "Sucesso!";
                    Intent loginuser = new Intent(v.getContext(), LoginActivity.class);
                    startActivity(loginuser);

                }
                else
                    result = "As senhas não são iguais!";

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        });
    }
}