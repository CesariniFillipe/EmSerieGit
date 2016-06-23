package com.bcc.unifal.emserie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bcc.unifal.emserie.Canal;
import com.bcc.unifal.emserie.Episodio;
import com.bcc.unifal.emserie.MinhaSerie;
import com.bcc.unifal.emserie.Serie;
import com.bcc.unifal.emserie.User;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;


public class DBController {
    private SQLiteDatabase db;
    private DBCreator creator;

    public DBController(Context c) {
        this.creator = new DBCreator(c);
    }

    public String registerUser(String login, String senha) throws IOException {
        Cursor cursor;
        String[] fields = {User.LOGIN};
        String where = User.LOGIN + " LIKE \"" + login+"\"";
        long result;

        db = creator.getReadableDatabase();
        cursor = db.query(User.TABLE, fields, where, null, null, null, null, null);

        if (!(cursor.getCount() > 0)){
            ContentValues values;

            db = creator.getWritableDatabase();
            values = new ContentValues();
            values.put(User.LOGIN, login);
            values.put(User.SENHA, senha);

            result = db.insert(User.TABLE, null, values);
            db.close();
            if (result == -1)
                return "Erro nº" + result;
            else {
                URL url = new URL("http://emserie.esy.es/register.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data = URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(login, "UTF-8") + "&" +
                        URLEncoder.encode("senha", "UTF-8") + "=" + URLEncoder.encode(senha, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();

                inputStream.close();
                httpURLConnection.disconnect();

                return "success";
            }
        }
        else
            return "Login já existe!";
    }

    public String insereSerie(String cod_usuario, String cod_serie) throws IOException {
        Cursor cursor;
        String[] fields = {MinhaSerie.COD_USUARIO};
        String where = MinhaSerie.COD_USUARIO + " LIKE \"" + cod_usuario+"\"";
        long result;

        db = creator.getReadableDatabase();
        cursor = db.query(MinhaSerie.TABLE, fields, where, null, null, null, null, null);

        if (!(cursor.getCount() > 0)){
            result=0;

            if (result == -1)
                return "Erro nº" + result;
            else {
                URL url = new URL("http://emserie.esy.es/insereminhaserie.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data = URLEncoder.encode("cod_usuario", "UTF-8") + "=" + URLEncoder.encode(cod_usuario, "UTF-8") + "&" +
                        URLEncoder.encode("cod_serie", "UTF-8") + "=" + URLEncoder.encode(cod_serie, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();

                inputStream.close();
                httpURLConnection.disconnect();

                return "Série adicionada!";
            }
        }
        else
            return "Série já existe!";
    }

    public Cursor login(String login, String senha){
        Cursor cursor;
        String [] fields = {User.COD, User.LOGIN};
        String where = User.LOGIN+" LIKE \""+login+"\" AND "+ User.SENHA+" LIKE \""+senha+"\"";
        db = creator.getReadableDatabase();
        cursor = db.query(User.TABLE, fields, where, null, null, null, null, null);
        if (cursor.getCount() == 1){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getAllSeries(){
        Cursor serie;
        String f[] = new String[]{Serie.COD,Serie.COD_CANAL,Serie.TITULO,Serie.ANOLANCAMENTO,Serie.IMAGEM};
        db = creator.getReadableDatabase();
        serie = db.query(Serie.TABLE, f, null, null, null, null, null, null);
        serie.moveToFirst();
        db.close();
        return serie;
    }

    public Cursor getAllEpisodios(){
        Cursor episodio;
        String f[] = new String[]{Episodio.COD, Episodio.NOME,Episodio.COD_SERIE, Episodio.COD_TEMP};
        db = creator.getReadableDatabase();
        episodio = db.query(Episodio.TABLE, f, null, null, null, null, null, null);
        episodio.moveToFirst();
        db.close();
        return episodio;
    }

    public String setAllEpisodios(List<Episodio> episodios) {
        Cursor cursor;
        String[] fields = {Episodio.COD, Episodio.NOME,Episodio.COD_SERIE, Episodio.COD_TEMP};
        long result;
        ContentValues values;

        db = creator.getWritableDatabase();
        db.execSQL("DELETE FROM " + Episodio.TABLE);
        values = new ContentValues();
        for (Episodio ep : episodios) {
            values.put(Episodio.COD, ep.getCod());
            values.put(Episodio.NOME, ep.getNome());
            values.put(Episodio.COD_SERIE, ep.getCod_serie());
            values.put(Episodio.COD_TEMP, ep.getCod_temp());
            result = db.insert(Episodio.TABLE, null, values);
            if (result == -1)
                return "Erro nº" + result;
        }
        db.close();
        return "Successo";
    }


    public Cursor getAllMinhasSeries(){
        Cursor minhaserie;
        String f[] = new String[]{MinhaSerie.COD_USUARIO, MinhaSerie.COD_SERIE};
        db = creator.getReadableDatabase();
        minhaserie = db.query(MinhaSerie.TABLE, f, null, null, null, null, null, null);
        minhaserie.moveToFirst();
        db.close();
        return minhaserie;
    }

    public String setSerie(Serie s){
        Cursor cursor;
        String[] fields = {Serie.COD,Serie.COD_CANAL,Serie.TITULO,Serie.ANOLANCAMENTO,Serie.IMAGEM};
        long result;

        ContentValues values;

        db = creator.getWritableDatabase();
        values = new ContentValues();
        values.put(Serie.COD, s.getCod());
        values.put(Serie.COD_CANAL, s.getCod_canal());
        values.put(Serie.TITULO, s.getTitulo());
        values.put(Serie.ANOLANCAMENTO, s.getAnoLancamento());
        values.put(Serie.IMAGEM, s.getImg());
        result = db.insert(Serie.TABLE, null, values);
        db.close();
        if (result == -1)
            return "Erro nº" + result;
        else
            return "Successo";
    }

    public String setMinhaSerie(String cod_usuario, String cod_serie){
        Cursor cursor;
        String[] fields = {MinhaSerie.COD_USUARIO,MinhaSerie.COD_SERIE};
        long result;

        ContentValues values;

        db = creator.getWritableDatabase();
        values = new ContentValues();
        values.put(MinhaSerie.COD_USUARIO, cod_usuario);
        values.put(MinhaSerie.COD_SERIE, cod_serie);
        result = db.insert(MinhaSerie.TABLE, null, values);
        db.close();
        if (result == -1)
            return "Erro nº" + result;
        else
            return "Successo";
    }

    public String setAllSeries(List<Serie> series) {
        Cursor cursor;
        String[] fields = {Serie.COD,Serie.COD_CANAL,Serie.TITULO,Serie.ANOLANCAMENTO,Serie.IMAGEM};
        long result;
        ContentValues values;

        db = creator.getWritableDatabase();
        db.execSQL("DELETE FROM " + Serie.TABLE);
        values = new ContentValues();
        for (Serie s : series) {
            values.put(Serie.COD, s.getCod());
            values.put(Serie.COD_CANAL, s.getCod_canal());
            values.put(Serie.TITULO, s.getTitulo());
            values.put(Serie.ANOLANCAMENTO, s.getAnoLancamento());
            values.put(Serie.IMAGEM, s.getImg());
            result = db.insert(Serie.TABLE, null, values);
            if (result == -1)
                return "Erro nº" + result;
        }
        db.close();
        return "Successo";
    }

    public String setAllCanais(List<Canal> canais) {
        Cursor cursor;
        String[] fields = {Canal.COD,Canal.NOME};
        long result;
        ContentValues values;

        db = creator.getWritableDatabase();
        db.execSQL("DELETE FROM " + Canal.TABLE);
        values = new ContentValues();
        for (Canal c : canais) {
            values.put(Canal.COD, c.getCod());
            values.put(Canal.NOME, c.getNome());
            result = db.insert(Canal.TABLE, null, values);
            if (result == -1)
                return "Erro nº" + result;
        }
        db.close();
        return "Successo";
    }

    public Cursor getAllCanais(){
        Cursor canais;
        String f[] = new String[]{Canal.COD,Canal.NOME};
        db = creator.getReadableDatabase();
        canais = db.query(Canal.TABLE, f, null, null, null, null, null, null);
        canais.moveToFirst();
        db.close();
        return canais;
    }

    public Cursor getAllUsers(){
        Cursor canais;
        String f[] = new String[]{User.COD, User.LOGIN, User.SENHA};
        db = creator.getReadableDatabase();
        canais = db.query(User.TABLE, f, null, null, null, null, null, null);
        canais.moveToFirst();
        db.close();
        return canais;
    }

    public String setAllMinhasSeries(List<MinhaSerie> minhasseries) {
        Cursor cursor;
        String[] fields = {MinhaSerie.COD_USUARIO, MinhaSerie.COD_SERIE};
        long result;
        ContentValues values;

        db = creator.getWritableDatabase();
        db.execSQL("DELETE FROM " + MinhaSerie.TABLE);
        values = new ContentValues();
        for (MinhaSerie ms : minhasseries) {
            values.put(MinhaSerie.COD_USUARIO, ms.getCod_usuario());
            values.put(MinhaSerie.COD_SERIE, ms.getCod_serie());
            result = db.insert(MinhaSerie.TABLE, null, values);
            if (result == -1)
                return "Erro nº" + result;
        }
        db.close();
        return "Successo";
    }

    public String setAllUsers(List<User> users) {
        Cursor cursor;
        String[] fields = {User.COD, User.LOGIN};
        long result;
        ContentValues values;

        db = creator.getWritableDatabase();
        db.execSQL("DELETE FROM " + Serie.TABLE);
        values = new ContentValues();
        for (User u : users) {
            values.put(User.COD, u.getCOD());
            values.put(User.LOGIN, u.getLogin());
            result = db.insert(User.TABLE, null, values);
            if (result == -1)
                return "Erro nº" + result;
        }
        db.close();
        return "Successo";
    }


    public Cursor loadData(String table, String[] fields) {
        Cursor cursor;

        db = creator.getReadableDatabase();
        cursor = db.query(table, fields, null, null, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        db.close();
        return cursor;
    }

    public void deleteUsers() {
        SQLiteDatabase db = this.creator.getWritableDatabase();
        // Delete All Rows
        db.close();

    }
}