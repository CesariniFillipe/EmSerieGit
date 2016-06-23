package com.bcc.unifal.emserie;

import android.content.Intent;
import android.database.Cursor;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcc.unifal.emserie.Json.JsonSeries;
import com.bcc.unifal.emserie.database.DBController;

import java.util.ArrayList;


public class ListSerie extends AppCompatActivity {

    String texto, img, cod, ano, can, cnl;

    @Override
    public void onBackPressed() {
    }

    String codigoUsuario = LoginActivity.codUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_serie);

        ListView lista = (ListView) findViewById(R.id.listaSeries);

        final ArrayList<String> series = preencherSerie();

        ArrayAdapter<String> ArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, series);

        lista.setAdapter(ArrayAdapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DBController db = new DBController(getBaseContext());
                Cursor serie = db.getAllSeries();
                Cursor canal = db.getAllCanais();

                serie.moveToFirst();
                for(int i = 0; i <= position; i++){
                    Serie s = new Serie(serie.getString(0), serie.getString(1), serie.getString(2),
                            serie.getString(3), serie.getString(4));
                    serie.moveToNext();
                    img=s.getImg();
                    texto=s.getTitulo();
                    cod=s.getCod();
                    ano=s.getAnoLancamento();
                    can=s.getCod_canal();
                }
                canal.moveToFirst();
                for(int i = 0; i < canal.getCount(); i++) {
                    Canal c = new Canal(canal.getString(0), canal.getString(1));
                    if(c.getCod().equals(can)){
                        cnl=c.getNome();
                    }
                    canal.moveToNext();
                }

                TextView tex = (TextView) findViewById(R.id.nomeSerie);
                tex.setText(texto);
                TextView anoL = (TextView) findViewById(R.id.ano);
                anoL.setText(ano);
                TextView can = (TextView) findViewById(R.id.canal) ;
                can.setText(cnl);

                new DownloadImagemAsyncTask().execute(
                        img.toString());


                Button btn = (Button) findViewById(R.id.Adicionar);

                btn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        DBController dbController = new DBController(getBaseContext());


                        String codigo_usuario = codigoUsuario.toString();
                        String codigo_serie = cod.toString();
                        String result = "";
                        try {
                                result = dbController.insereSerie(codigo_usuario,codigo_serie);
                                result = dbController.setMinhaSerie(codigo_usuario,codigo_serie);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }

    public void openHome(View view){
        Intent home = new Intent(this, HomeActivity.class);
        startActivity(home);
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
                ImageView img = (ImageView)findViewById(R.id.imageSerie);
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
