package com.example.reposteria_angeles;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ListaIngreso extends AppCompatActivity {
    ControladorBD ingreso;

    private TextView etListado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ingreso);

        etListado = (TextView) findViewById(R.id.txtDetalle);
        ingreso = new ControladorBD(this);
        SQLiteDatabase bd = ingreso.getReadableDatabase();
        Cursor registros = bd.rawQuery("select * from ingreso", null);

        int n = registros.getCount();
        int nr = 1;

        if(n>0){
            registros.moveToFirst();
            do{
                etListado.setText(etListado.getText() + "\nIngreso #"+nr);
                etListado.setText(etListado.getText() + "\nIdentificador: "+ registros.getString(0));
                etListado.setText(etListado.getText() + "\nNombre Cliente: "+ registros.getString(1));
                etListado.setText(etListado.getText() + "\nNombre Producto: "+ registros.getString(2));
                etListado.setText(etListado.getText() + "\nCantidad Productos Vendidos: "+ registros.getString(3));
                etListado.setText(etListado.getText() + "\nNombre ingreso: "+ registros.getString(4));
                etListado.setText(etListado.getText() + "\nVenta Total: "+ registros.getString(5));
                etListado.setText(etListado.getText() + "\nDescripción: "+ registros.getString(6));
                etListado.setText(etListado.getText() + "\n_________________________________");

                nr++;
            }while(registros.moveToNext());
        }else{
            Toast.makeText(this, "Sin registros en la BD", Toast.LENGTH_SHORT).show();
        }
        registros.close();
    }
}