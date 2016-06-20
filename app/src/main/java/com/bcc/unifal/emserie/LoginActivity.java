package com.bcc.unifal.emserie;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bcc.unifal.emserie.Json.JsonSeries;
import com.bcc.unifal.emserie.database.DBController;

public class LoginActivity extends AppCompatActivity {
    public static User loggedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = (Button) findViewById(R.id.sendLoginButton);


        if (login != null) {
            login.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    EditText e = (EditText) findViewById(R.id.emailLoginEditText);
                    EditText p = (EditText) findViewById(R.id.passwordLoginEditText);
                    String login = e != null ? e.getText().toString() : null;
                    String password = p != null ? p.getText().toString() : null;
                    String result;
                    Cursor data;
                    DBController dbController = new DBController(getBaseContext());
                    data = dbController.login(login, password);
                    data.moveToFirst();

                    result = "Sucesso, você está sendo redirecionado";
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

                    login = data.getString(1);
                    String cod = data.getString(0);
                    loggedUser = new User(cod, login);

                    Intent jsonserie = new Intent(v.getContext(), HomeActivity.class);
                    startActivity(jsonserie);
                }
            });
        }

    }


    public void openRegister(View view){
        Intent register = new Intent(this, RegisterActivity.class);
        startActivity(register);
    }


    public static User getLoggedUser() {
        return loggedUser;
    }

}
