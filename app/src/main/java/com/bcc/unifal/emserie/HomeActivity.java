package com.bcc.unifal.emserie;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcc.unifal.emserie.database.DBController;

public class HomeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);



        /*DBController db = new DBController(getBaseContext());
        Cursor series = db.getAllSeries();

        series.moveToFirst();
        for(int i = 0; i < 3; i++){
            Serie s = new Serie(series.getString(0), series.getString(1), series.getString(2),
                    series.getString(3), series.getString(4));
            imag = s.getImg();
            texto = s.getTitulo();
            series.moveToNext();
        }
        */
    }

    public void openLista(View view){
        Intent lista = new Intent(this, ListSerie.class);
        startActivity(lista);
    }


    /*class DownloadImagemAsyncTask extends
            AsyncTask<String, Void, Bitmap>{

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(
                    HomeActivity.this,
                    "Aguarde", "Carregando a  imagem...");
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String urlString = params[0];

            try {
                URL url = new URL(urlString);
                HttpURLConnection conexao = (HttpURLConnection)
                        url.openConnection();
                conexao.setRequestMethod("GET");
                conexao.setDoInput(true);
                conexao.connect();

                InputStream is = conexao.getInputStream();
                Bitmap imagem = BitmapFactory.decodeStream(is);
                return imagem;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result != null){
                ImageView img = (ImageView)findViewById(R.id.imageView1);
                img.setImageBitmap(result);
                TextView txt = (TextView)findViewById(R.id.textView);
                txt.setText(texto);
            } else {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(HomeActivity.this).
                                setTitle("Erro").
                                setMessage("Não foi possivel carregar imagem, tente novamente mais tarde!").
                                setPositiveButton("OK", null);
                builder.create().show();
            }
        }
    }*/
}
