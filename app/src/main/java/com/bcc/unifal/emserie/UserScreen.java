package com.bcc.unifal.emserie;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bcc.unifal.emserie.database.DBController;
import com.bcc.unifal.emserie.database.SessionManager;

public class UserScreen extends AppCompatActivity {

    private TextView txtName;
    private Button btnLogout;
    private TextView txtSerie, txtEps, txtUsers;
    private DBController db;
    private SessionManager session;

    String nome = LoginActivity.nomeUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);

        txtName = (TextView) findViewById(R.id.nome);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        txtSerie = (TextView) findViewById(R.id.numSeries);
        txtEps = (TextView) findViewById(R.id.numEps);
        txtUsers = (TextView) findViewById(R.id.numUsuarios);

        db = new DBController(getApplicationContext());

        session = new SessionManager(getApplicationContext());

        Cursor usuario = db.getAllUsers();
        int num3 = usuario.getCount();

        if (!session.isLoggedIn()) {
            logoutUser();
        }
        int num1 = HomeActivity.numSerie;
        int num2 = AbrirSerie.numEps;

        String s1 = ""+num1;
        String s2 = ""+num2;
        String s3 = ""+num3;

        txtName.setText(nome);
        txtSerie.setText(s1);
        txtEps.setText(s2);
        txtUsers.setText(s3);


        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(UserScreen.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserScreen.this, HomeActivity.class);
        startActivity(intent);
    }

}
