package com.bcc.unifal.emserie.Json;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.bcc.unifal.emserie.AbrirSerie;
import com.bcc.unifal.emserie.Canal;
import com.bcc.unifal.emserie.HomeActivity;
import com.bcc.unifal.emserie.ListSerie;
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

public class JsonCanais extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DownloadJsonAsyncTask()
                .execute("http://emserie.esy.es/getcanal.php");

    }


    class DownloadJsonAsyncTask extends AsyncTask<String, Void, List<Canal>> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(JsonCanais.this, "Aguarde",
                    "Atualizando banco de dados, Por Favor Aguarde...");
        }

        @Override
        protected List<Canal> doInBackground(String... params) {
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

                    List<Canal> canais = getCanais(json);

                    return canais;
                }
            } catch (Exception e) {
                Log.e("ERRO", "Falha ao acessar Web service", e);
            }
            return null;
        }

        public List<Canal> getCanais(String jsonString) {
            List<Canal> canais = new ArrayList<Canal>();
            try {
                JSONArray canaisJson = new JSONArray(jsonString);
                JSONObject canal;

                for (int i = 0; i < canaisJson.length(); i++) {
                    canal = new JSONObject(canaisJson.getString(i));

                    Canal objetoCanal = new Canal();
                    objetoCanal.setCod(canal.getString("cod"));
                    objetoCanal.setNome(canal.getString("nome"));
                    canais.add(objetoCanal);
                }

            } catch (JSONException e) {
                Log.e("Erro", "Erro no parsing do JSON", e);
            }
            DBController db = new DBController(getBaseContext());
            db.setAllCanais(canais);

            Intent home = new Intent(JsonCanais.this, HomeActivity.class);
            startActivity(home);

            return canais;
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
