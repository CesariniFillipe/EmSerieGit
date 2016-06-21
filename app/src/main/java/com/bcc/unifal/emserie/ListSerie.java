package com.bcc.unifal.emserie;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
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

import com.bcc.unifal.emserie.database.DBController;

import java.util.ArrayList;


public class ListSerie extends AppCompatActivity {

    String texto, img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_serie);

        ListView lista = (ListView) findViewById(R.id.listaSeries);

        final ArrayList<String> series = preencherSerie();
        final ArrayList<String> imagens = preencherImagem();

        ArrayAdapter<String> ArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, series);
        
        lista.setAdapter(ArrayAdapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DBController db = new DBController(getBaseContext());
                Cursor serie = db.getAllSeries();

                serie.moveToFirst();
                for(int i = 0; i <= position; i++){
                    Serie s = new Serie(serie.getString(0), serie.getString(1), serie.getString(2),
                            serie.getString(3), serie.getString(4));
                    serie.moveToNext();
                    img=s.getImg();
                }

                new DownloadImagemAsyncTask().execute(
                        img.toString());
            }
        });
    }

    private ArrayList<String> preencherSerie(){
        ArrayList<String> dados = new ArrayList<String>();

        DBController db = new DBController(getBaseContext());
        Cursor series = db.getAllSeries();

        series.moveToFirst();
        for(int i = 0; i < series.getCount(); i++){
            Serie s = new Serie(series.getString(0), series.getString(1), series.getString(2),
                    series.getString(3), series.getString(4));
            texto = s.getTitulo();
            dados.add(texto);
            series.moveToNext();
        }
        return dados;
    }

    private ArrayList<String> preencherImagem(){
        ArrayList<String> dados = new ArrayList<String>();

        String imagem;
        DBController db = new DBController(getBaseContext());
        Cursor series = db.getAllSeries();

        series.moveToFirst();
        for(int i = 0; i < series.getCount(); i++){
            Serie s = new Serie(series.getString(0), series.getString(1), series.getString(2),
                    series.getString(3), series.getString(4));
            imagem = s.getImg();
            dados.add(imagem);
            series.moveToNext();
        }
        return dados;
    }


    class DownloadImagemAsyncTask extends
            AsyncTask<String, Void, Bitmap>{

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(
                    ListSerie.this,
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
            } else {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(ListSerie.this).
                                setTitle("Erro").
                                setMessage("Não foi possivel carregar imagem, tente novamente mais tarde!").
                                setPositiveButton("OK", null);
                builder.create().show();
            }
        }
    }
}
