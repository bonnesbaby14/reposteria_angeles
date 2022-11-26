package com.example.reposteria_angeles;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ListaGasto extends AppCompatActivity {
    ControladorBD gasto;

    private TextView etListado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_gasto);

        etListado = (TextView) findViewById(R.id.txtDetalle);
        gasto = new ControladorBD(this);
        SQLiteDatabase bd = gasto.getReadableDatabase();
        Cursor registros = bd.rawQuery("select * from expense", null);

        int n = registros.getCount();
        int nr = 1;

        if(n>0){
            registros.moveToFirst();
            do{
                etListado.setText(etListado.getText() + "\nGasto #"+nr);
                etListado.setText(etListado.getText() + "\nIdentificador: "+ registros.getString(0));
                etListado.setText(etListado.getText() + "\nNombre Producto: "+ registros.getString(1));
                etListado.setText(etListado.getText() + "\nNombre Gasto: "+ registros.getString(2));
                etListado.setText(etListado.getText() + "\nCosto: "+ registros.getString(3));
                etListado.setText(etListado.getText() + "\nCantidad productos: "+ registros.getString(4));
                etListado.setText(etListado.getText() + "\nDescripción: "+ registros.getString(5));
                etListado.setText(etListado.getText() + "\n_________________________________");

                nr++;
            }while(registros.moveToNext());
        }else{
            Toast.makeText(this, "Sin registros en la BD", Toast.LENGTH_SHORT).show();
        }
        registros.close();
    }
}