package com.bcc.unifal.emserie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        String where = MinhaSerie.COD_USUARIO + " LIKE \"" + cod_usuario + "\"";
        long result;

        db = creator.getReadableDatabase();
        cursor = db.query(MinhaSerie.TABLE, fields, where, null, null, null, null, null);

        if (!(cursor.getCount() > 0)) {
            ContentValues values;

            db = creator.getWritableDatabase();
            values = new ContentValues();
            values.put(MinhaSerie.COD_USUARIO, cod_usuario);
            values.put(MinhaSerie.COD_SERIE, cod_serie);

            result = db.insert(User.TABLE, null, values);
            db.close();
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

                return "Sucesso";
            }
        }
        else
            return "Serie já adicionada";
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
}