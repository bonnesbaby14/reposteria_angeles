package com.example.reposteria_angeles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ThemedSpinnerAdapter;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void iniciarSesion(View view){


        Intent intent = new Intent(MainActivity.this, VentaActivity.class);
        DBHELPER dbhelper=new DBHELPER(MainActivity.this);
        SQLiteDatabase db= dbhelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from usuarios where usuario='admin' and password='admin'",null);


        if(cursor.moveToFirst()){
            startActivity(intent);
        }else{
            Toast toast=Toast.makeText(this,"El usuario no se encuestra en la base de datos",Toast.LENGTH_LONG);
            toast.show();
        }

    }
}
