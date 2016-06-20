package com.bcc.unifal.emserie.Json;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.bcc.unifal.emserie.HomeActivity;
import com.bcc.unifal.emserie.Serie;
import com.bcc.unifal.emserie.database.DBController;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonSeries extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DownloadJsonAsyncTask()
                .execute("http://emserie.esy.es/getseries.php");

    }


    class DownloadJsonAsyncTask extends AsyncTask<String, Void, List<Serie>> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(JsonSeries.this, "Aguarde",
                    "Atualizando banco de dados, Por Favor Aguarde...");
        }

        @Override
        protected List<Serie> doInBackground(String... params) {
            String urlString = params[0];

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(urlString);

            try {
                HttpResponse response = httpclient.execute(httpget);

                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    InputStream instream = entity.getContent();

                    String json = toString(instream);
                    instream.close();

                    List<Serie> series = getSeries(json);

                    return series;
                }
            } catch (Exception e) {
                Log.e("ERRO", "Falha ao acessar Web service", e);
            }
            return null;
        }

        public List<Serie> getSeries(String jsonString) {
            List<Serie> series = new ArrayList<Serie>();
            try {
                JSONArray seriesJson = new JSONArray(jsonString);
                JSONObject serie;

                for (int i = 0; i < seriesJson.length(); i++) {
                    serie = new JSONObject(seriesJson.getString(i));

                    Serie objetoSerie = new Serie();
                    objetoSerie.setCod(serie.getString("cod"));
                    objetoSerie.setCod_canal(serie.getString("cod_canal"));
                    objetoSerie.setTitulo(serie.getString("titulo"));
                    objetoSerie.setAnoLancamento(serie.getString("anoLancamento"));
                    objetoSerie.setImg(serie.getString("img"));
                    series.add(objetoSerie);
                }

            } catch (JSONException e) {
                Log.e("Erro", "Erro no parsing do JSON", e);
            }
            DBController db = new DBController(getBaseContext());
            db.setAllSeries(series);

            Intent home = new Intent(JsonSeries.this, HomeActivity.class);
            startActivity(home);

            return series;
        }

        private String toString(InputStream is) throws IOException {

            byte[] bytes = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int lidos;
            while ((lidos = is.read(bytes)) > 0) {
                baos.write(bytes, 0, lidos);
            }
            return new String(baos.toByteArray());
        }
    }
}
