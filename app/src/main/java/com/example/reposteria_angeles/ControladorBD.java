package com.example.reposteria_angeles;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class ControladorBD extends SQLiteOpenHelper {
    public final static String BD_NAME = "CakeShop.db";

    public ControladorBD(Context context) {
        super(context, BD_NAME, null, 1);
    }//constructor

    @Override
    public void onCreate(SQLiteDatabase nombreBD) {
//Query sql
        String sqlClient = "create table cliente (identificador int primary key, nombre text, direccion text, telefono text, preferencia text)";
        String sqlUsers = "create table users (userId int primary key, AUTO_INCREMENT, userEmail text unique,userName text, userPassword text)";
        //admin user
        ContentValues admin = new ContentValues();
        admin.put("userEmail","admin");
        admin.put("userName","admin");
        admin.put("userPassword","admin");
        try{
//Creación de tabla, con campos y clave primaria
            nombreBD.execSQL(sqlClient);
            nombreBD.execSQL(sqlUsers);
            nombreBD.insert("users",null,admin);

        } catch (SQLException e){
            Toast.makeText(null, "Error al crear la base de datos",
                    Toast.LENGTH_SHORT).show();
        }
    }//onCreate
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
    //User Methods
    public Boolean userInsertData(String email,String username, String passw){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("userEmail", email);
        contentValues.put("userName", username);
        contentValues.put("userPassword",passw);

        long result = MyDB.insert("users", null, contentValues);
        if(result==-1)
            return false;
        else
            return true;
    }//userInsertData

    public Boolean userCheckUserName(String userEmail) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where userEmail = ?", new String[]{userEmail});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }//checkusername

    public Boolean userCheckUserNamePassword(String userEmail, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where userEmail = ? and userPassword = ?", new String[] {userEmail,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }//checkusernamepassword
}//class