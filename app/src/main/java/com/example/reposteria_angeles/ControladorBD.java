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

        String sqlUsers = "create table users (userId int primary key unique, userEmail text unique,userName text, userPassword text)";
        String sqlProduct = "create table product (productId int primary key unique, productName text, productQty int, productPrice real,productDate date, productDescrip text)";
        String sqlExpense = "create table expense (expenseId int primary key unique, productNameExpense text, expenseName text, expenseCost real, expenseNumber int, expenseDescripcion text)";
        String sqlIngreso = "create table ingreso (ingresoId int primary key unique, cliente text, productos text, productosVendidos int, nombreVenta text, ventaTotal real, descripcionVenta text)";
        //admin user
        ContentValues admin = new ContentValues();
        admin.put("userId",1);
        admin.put("userEmail","admin");
        admin.put("userName","admin");
        admin.put("userPassword","admin");
        try{
//Creación de tabla, con campos y clave primaria
            nombreBD.execSQL(sqlClient);
            nombreBD.execSQL(sqlUsers);
            nombreBD.execSQL(sqlProduct);
            nombreBD.execSQL(sqlExpense);
            nombreBD.execSQL(sqlIngreso);

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
        if(result==-1) {
            MyDB.close();
            return false;
        }
        else {
            MyDB.close();
            return true;
        }
    }//userInsertData

    public Boolean userCheckUserName(String userEmail) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where userEmail = ?", new String[]{userEmail});
        if (cursor.getCount() > 0){
            MyDB.close();
            return true;
        }
        else {
            MyDB.close();

            return false;
        }
    }//checkusername

    public Boolean userCheckUserNamePassword(String userEmail, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where userEmail = ? and userPassword = ?", new String[] {userEmail,password});
        if(cursor.getCount()>0) {
            MyDB.close();

            return true;
        }
        else {
            MyDB.close();

            return false;
        }
    }//checkusernamepassword

    //Product Methods
    public Boolean productInsertData(String productId,String productName, String productQty,String productPrice, String productDate, String productDescrip ){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("productId", productId);
        contentValues.put("productName", productName);
        contentValues.put("productQty",productQty);
        contentValues.put("productPrice",productPrice);
        contentValues.put("productDate",productDate);
        contentValues.put("productDescrip",productDescrip);


        long result = MyDB.insert("product", null, contentValues);
        if(result==-1) {
            MyDB.close();

            return false;
        }
        else {
            MyDB.close();

            return true;
        }
    }//userInsertData

    public Boolean productUpdateData(String productId,String productName, String productQty,String productPrice, String productDate, String productDescrip ){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("productId", productId);
        contentValues.put("productName", productName);
        contentValues.put("productQty",productQty);
        contentValues.put("productPrice",productPrice);
        contentValues.put("productDate",productDate);
        contentValues.put("productDescrip",productDescrip);

        int result = MyDB.update("product",contentValues,"productId = ?",new String[]{productId});
        if (result > 0) {
            MyDB.close();
            return true;
        }
        else {
            MyDB.close();
            return false;
        }
    }//userUpdateData

    public Boolean productDeleteData(String productId){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        int result = MyDB.delete("product","productId = ?",new String[]{productId});
        if (result > 0) {
            MyDB.close();
            return true;
        }
        else {
            MyDB.close();
            return false;
        }
    }//userUpdateData


    public Boolean productCheckCode(String productId) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from product where productId = ?", new String[]{productId});
        if (cursor.getCount() > 0) {
            MyDB.close();
            return true;
        }
        else {
            MyDB.close();
            return false;
        }
    }//checkusername
}//class