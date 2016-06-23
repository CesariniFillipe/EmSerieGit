package com.bcc.unifal.emserie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bcc.unifal.emserie.Canal;
import com.bcc.unifal.emserie.Episodio;
import com.bcc.unifal.emserie.MinhaSerie;
import com.bcc.unifal.emserie.Serie;
import com.bcc.unifal.emserie.User;

public class DBCreator extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "a14006";
    public static final int VERSAO = 1;
    public static final String sqlSerie = "CREATE TABLE "+ Serie.TABLE+"("
            + Serie.COD + " integer primary key autoincrement,"
            + Serie.COD_CANAL + " text,"
            + Serie.TITULO + " text,"
            + Serie.ANOLANCAMENTO + " text,"
            + Serie.IMAGEM+ " text);";

    public static final String sqlMinhaSerie = "CREATE TABLE " + MinhaSerie.TABLE+"("
            + MinhaSerie.COD_USUARIO + " text,"
            + MinhaSerie.COD_SERIE + " text);";


    public static final String sqlEpisodio = "CREATE TABLE " + Episodio.TABLE+"("
            + Episodio.COD + " integer primary key autoincrement,"
            + Episodio.NOME + " text,"
            + Episodio.COD_SERIE + " text,"
            + Episodio.COD_TEMP + " text);";

    public static final String sqlCanal = "CREATE TABLE " + Canal.TABLE+"("
            + Canal.COD + " integer primary key autoincrement,"
            + Canal.NOME + " text);";

    public DBCreator(Context context){
        super(context,DATABASE_NAME, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+ User.TABLE+"("
                + User.COD+ " integer primary key autoincrement,"
                + User.LOGIN + " text,"
                + User.SENHA + " text);";

        db.execSQL(sql);
        db.execSQL(sqlSerie);
        db.execSQL(sqlMinhaSerie);
        db.execSQL(sqlEpisodio);
        db.execSQL(sqlCanal);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + User.TABLE);
        //db.execSQL("DROP TABLE IF EXISTS" + Serie.TABLE);
        onCreate(db);
    }


}

