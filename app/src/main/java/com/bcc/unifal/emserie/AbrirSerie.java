package com.bcc.unifal.emserie;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bcc.unifal.emserie.database.DBController;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AbrirSerie extends AppCompatActivity {

    public static int numEps=0;
    String titulo, temp;

    ArrayList<String> episodios;

    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_serie);

        String imagem = HomeActivity.img;
        String nomeSerie = HomeActivity.texto;

        episodios = preencherEpisodios();

        mostraEp(episodios);

        TextView text2 = (TextView) findViewById(R.id.Titulo);
        text2.setText(nomeSerie);
        TextView anoL = (TextView) findViewById(R.id.ano3);
        anoL.setText(HomeActivity.ano);
        TextView can = (TextView) findViewById(R.id.canal3) ;
        can.setText(HomeActivity.cnl);


        new DownloadImagemAsyncTask().execute(
                imagem.toString());



    }

    private void mostraEp(ArrayList<String> eps){
        final ListView lista = (ListView) findViewById(R.id.listaEpisodios);
        final ArrayAdapter<String> ArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, episodios);
        lista.setAdapter(ArrayAdapter);

        final ArrayList<String> dados = new ArrayList<String>();
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                for(int i=0;i<episodios.size();i++) {
                    if (i == position && !episodios.get(i).toString().contains(" - ASSISTIDO")
                            && !episodios.get(i).toString().contains("Temporada")) {
                        String eps = episodios.get(i).concat(" - ASSISTIDO");
                        numEps++;
                        dados.add(eps);
                    }
                    else{
                        dados.add(episodios.get(i).toString());
                    }
                }
                episodios.clear();
                for(int i = 0;i<dados.size();i++){
                    episodios.add(dados.get(i).toString());
                }
                mostraEp(episodios);
            }

        });
    }
    private ArrayList<String> preencherEpisodios(){
        ArrayList<String> dados = new ArrayList<String>();

        DBController db = new DBController(getBaseContext());
        Cursor episodios = db.getAllEpisodios();

        String codigodaSerie = HomeActivity.codigoSerie;

        temp = "1";
        episodios.moveToFirst();
        dados.add("                            1º Temporada");
        for(int i = 0; i < episodios.getCount(); i++){
            Episodio ep = new Episodio(episodios.getString(0),episodios.getString(1), episodios.getString(2),
                    episodios.getString(3));
            if(ep.getCod_serie().equals(codigodaSerie)){
                titulo = ep.getNome();
                if(ep.getCod_temp().equals(temp)){
                    dados.add(titulo);
                }else{
                    dados.add("                            "+ep.getCod_temp() + "º Temporada");
                    dados.add(titulo);
                    temp = ep.getCod_temp();
                }

            }
            episodios.moveToNext();
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
                    AbrirSerie.this,
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
                ImageView img = (ImageView)findViewById(R.id.imageView);
                img.setImageBitmap(result);
            } else {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(AbrirSerie.this).
                                setTitle("Erro").
                                setMessage("Não foi possivel carregar imagem, tente novamente mais tarde!").
                                setPositiveButton("OK", null);
                builder.create().show();
            }
        }
    }
    public void openHome(View view){
        Intent home = new Intent(this, HomeActivity.class);
        startActivity(home);
    }

    public void UserScreenClick(View v){
        Intent user = new Intent(this, UserScreen.class);
        startActivity(user);
    }
}
