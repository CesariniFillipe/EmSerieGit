package com.bcc.unifal.emserie;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcc.unifal.emserie.Json.JsonMinhasSeries;
import com.bcc.unifal.emserie.Json.JsonSeries;
import com.bcc.unifal.emserie.database.DBController;
import com.bcc.unifal.emserie.database.SessionManager;


public class HomeActivity extends AppCompatActivity {
    String texto, img, cod;


    String codigo_usuario = "1";

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ListView lista = (ListView) findViewById(R.id.listaMinhaSerie);

        final ArrayList<String> minhasseries = preencherMinhaSerie();
        ArrayAdapter<String> ArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, minhasseries);

        lista.setAdapter(ArrayAdapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DBController db = new DBController(getBaseContext());
                Cursor serie = db.getAllSeries();
                Cursor minhasseries = db.getAllMinhasSeries();

                minhasseries.moveToFirst();
                for(int i = 0; i <= position; i++) {
                    MinhaSerie ms = new MinhaSerie(minhasseries.getString(0), minhasseries.getString(1));
                    serie.moveToFirst();
                    for (int j = 0; j < serie.getCount(); j++) {
                        Serie s = new Serie(serie.getString(0), serie.getString(1), serie.getString(2),
                                serie.getString(3), serie.getString(4));
                        if(ms.getCod_serie().equals(s.getCod())&& ms.getCod_usuario().equals(codigo_usuario)){
                            texto = s.getTitulo();
                            img = s.getImg();
                        }
                        serie.moveToNext();
                    }
                    minhasseries.moveToNext();
                }
                TextView tex = (TextView) findViewById(R.id.SerieNome);
                tex.setText(texto);
                new DownloadImagemAsyncTask().execute(
                        img.toString());
            }
        });
    }

    private ArrayList<String> preencherMinhaSerie(){
        ArrayList<String> data = new ArrayList<String>();

        DBController db = new DBController(getBaseContext());
        Cursor minhasseries = db.getAllMinhasSeries();
        Cursor series = db.getAllSeries();

        minhasseries.moveToFirst();
        for(int i = 0; i < minhasseries.getCount(); i++){
            MinhaSerie ms = new MinhaSerie(minhasseries.getString(0), minhasseries.getString(1));
            series.moveToFirst();
            for(int j = 0; j < series.getCount(); j++){
                Serie s = new Serie(series.getString(0), series.getString(1), series.getString(2),
                        series.getString(3), series.getString(4));
                if(ms.getCod_serie().equals(s.getCod()) && ms.getCod_usuario().equals(codigo_usuario)){
                    texto = s.getTitulo();
                    data.add(texto);
                }
                series.moveToNext();
            }
            minhasseries.moveToNext();
        }
        return data;
    }

    public void listarSeriesClick(View v){
        Intent lista = new Intent(this, ListSerie.class);
        startActivity(lista);
    }

    class DownloadImagemAsyncTask extends
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
                ImageView img = (ImageView)findViewById(R.id.imageMinhaSerie);
                img.setImageBitmap(result);
            } else {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(HomeActivity.this).
                                setTitle("Erro").
                                setMessage("Não foi possivel carregar imagem, tente novamente mais tarde!").
                                setPositiveButton("OK", null);
                builder.create().show();
            }
        }
    }
}