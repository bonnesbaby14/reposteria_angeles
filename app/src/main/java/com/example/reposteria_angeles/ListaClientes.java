package com.example.reposteria_angeles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ListaClientes extends AppCompatActivity {

    ControladorBD admin;

    private TextView etListado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);

        etListado = (TextView) findViewById(R.id.txtDetalle);
        admin = new ControladorBD(this);
        SQLiteDatabase bd = admin.getReadableDatabase();
        Cursor registros = bd.rawQuery("select * from cliente", null);

        int n = registros.getCount();
        int nr = 1;

        if(n>0){
            registros.moveToFirst();
            do{
                etListado.setText(etListado.getText() + "\nCliente #"+nr);
                etListado.setText(etListado.getText() + "\nIdentificador: "+ registros.getString(0));
                etListado.setText(etListado.getText() + "\nNombre: "+ registros.getString(1));
                etListado.setText(etListado.getText() + "\nDomicilio: "+ registros.getString(2));
                etListado.setText(etListado.getText() + "\nTelefono: "+ registros.getString(3));
                etListado.setText(etListado.getText() + "\nPreferencias: "+ registros.getString(4));
                etListado.setText(etListado.getText() + "\n_________________________________");

                nr++;
            }while(registros.moveToNext());
        }else{
            Toast.makeText(this, "Sin registros en la BD", Toast.LENGTH_SHORT).show();
        }
        registros.close();
    }


}