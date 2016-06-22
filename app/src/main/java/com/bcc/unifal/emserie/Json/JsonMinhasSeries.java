package com.bcc.unifal.emserie.Json;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.bcc.unifal.emserie.HomeActivity;
import com.bcc.unifal.emserie.ListSerie;
import com.bcc.unifal.emserie.MainActivity;
import com.bcc.unifal.emserie.MinhaSerie;
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

public class JsonMinhasSeries extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DownloadJsonAsyncTask()
                .execute("http://emserie.esy.es/getminhasseries.php");

    }


    class DownloadJsonAsyncTask extends AsyncTask<String, Void, List<MinhaSerie>> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(JsonMinhasSeries.this, "Aguarde",
                    "Atualizando banco de dados, Por Favor Aguarde...");
        }

        @Override
        protected List<MinhaSerie> doInBackground(String... params) {
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

                    List<MinhaSerie> minhasseries = getMinhasSeries(json);

                    return minhasseries;
                }
            } catch (Exception e) {
                Log.e("ERRO", "Falha ao acessar Web service", e);
            }
            return null;
        }

        public List<MinhaSerie> getMinhasSeries(String jsonString) {
            List<MinhaSerie> minhasseries = new ArrayList<MinhaSerie>();
            try {
                JSONArray minhasseriesJson = new JSONArray(jsonString);
                JSONObject minhaserie;

                for (int i = 0; i < minhasseriesJson.length(); i++) {
                    minhaserie = new JSONObject(minhasseriesJson.getString(i));

                    MinhaSerie objetoMinhaSerie = new MinhaSerie();
                    objetoMinhaSerie.setCod_usuario(minhaserie.getString("cod_usuario"));
                    objetoMinhaSerie.setCod_serie(minhaserie.getString("cod_serie"));
                    minhasseries.add(objetoMinhaSerie);
                }

            } catch (JSONException e) {
                Log.e("Erro", "Erro no parsing do JSON", e);
            }
            DBController db = new DBController(getBaseContext());
            db.setAllMinhasSeries(minhasseries);

            Intent json = new Intent(JsonMinhasSeries.this, JsonSeries.class);
            startActivity(json);

            return minhasseries;

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
