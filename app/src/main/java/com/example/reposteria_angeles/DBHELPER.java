package com.example.reposteria_angeles;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHELPER extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String NOMBRE="REPORSTERIA";
    public DBHELPER(@Nullable Context context) {
        super(context, NOMBRE, null, VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
sqLiteDatabase.execSQL("CREATE TABLE usuarios(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, usuario TEXT NOT NULL, password TEXT NOT NULL)");
        sqLiteDatabase.execSQL("INSERT INTO usuarios( nombre, usuario, password) VALUES ('admin','admin','admin') ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {




    }
}
