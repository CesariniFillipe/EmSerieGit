package com.bcc.unifal.emserie;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bcc.unifal.emserie.Json.JsonMinhasSeries;
import com.bcc.unifal.emserie.Json.JsonSeries;

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        new CountDownTimer(2000, 1000) {
            public void onFinish() {

                Intent startActivity = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(startActivity);

                finish();
            }

            public void onTick(long millisUntilFinished) {
            }

        }.start();
    }
}