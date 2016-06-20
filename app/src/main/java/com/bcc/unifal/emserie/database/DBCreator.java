package com.bcc.unifal.emserie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS" + User.TABLE);
        //db.execSQL("DROP TABLE IF EXISTS" + Serie.TABLE);
        onCreate(db);
    }


}

