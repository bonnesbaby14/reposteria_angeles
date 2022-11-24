package com.example.reposteria_angeles;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ProductsList extends AppCompatActivity {
    ControladorBD admin;
    TextView productsList;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);
        productsList = (TextView) findViewById(R.id.tvwProductsListLst);
        admin = new ControladorBD(this);
        SQLiteDatabase MyDB = admin.getReadableDatabase();
        Cursor registers = MyDB.rawQuery("select * from product", null);

        int n = registers.getCount();
        int nr = 1;

        if(n>0){
            registers.moveToFirst();
            do{
                productsList.setText(productsList.getText() + "\n_________________________________");
                productsList.setText(productsList.getText() + "\nProducto #"+nr);
                productsList.setText(productsList.getText() + "\nIdentificador: "+ registers.getString(0));
                productsList.setText(productsList.getText() + "\nNombre: "+ registers.getString(1));
                productsList.setText(productsList.getText() + "\nCantidad: "+ registers.getString(2));
                productsList.setText(productsList.getText() + "\nPrecio: "+ registers.getString(3));
                productsList.setText(productsList.getText() + "\nFecha: "+ registers.getString(4));
                productsList.setText(productsList.getText() + "\nDescripción: "+ registers.getString(5));

                nr++;
            }while(registers.moveToNext());
        }else{
            Toast.makeText(this, "Sin registros en la BD", Toast.LENGTH_SHORT).show();
        }
        registers.close();
    }//onCreate
}//ProductsList