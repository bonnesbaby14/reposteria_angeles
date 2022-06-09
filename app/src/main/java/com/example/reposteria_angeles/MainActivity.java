package com.example.reposteria_angeles;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ThemedSpinnerAdapter;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends AppCompatActivity {
    EditText usuario;
    EditText contra;
    TextView registrar;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},200);



        try {
        InputStreamReader archivo = new InputStreamReader(openFileInput("usuarios.txt"));
        if (archivo==null) {

            grabar();
        }
        } catch (IOException e) {
            Toast.makeText(this,"No hay Archivo",Toast.LENGTH_SHORT).show();
            grabar();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void grabar() {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput("usuarios.txt", MainActivity.MODE_PRIVATE));
            archivo.write("admin-admin-admin-admin \n");
            archivo.flush();
            archivo.close();
        } catch (IOException e) {



        }
        Toast t = Toast.makeText(this, "APP INICIADA POR PRIMERA VEZ",Toast.LENGTH_SHORT);
        t.show();

    }

    public String login(String usuario,String contra){
        try {
            InputStreamReader archivo = new InputStreamReader(openFileInput("usuarios.txt"));
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();
            String todo = "";
            while (linea != null) {
                String [] split=linea.split("-");
                Log.d("DATA",split.toString());
                if(split[0].equals(usuario)){
                    if(split[1].equals(contra)){
                        return "correcto";
                    }
                    return "contrasena";


                }
                linea = br.readLine();
            }
            br.close();
            archivo.close();
            return "usuario";


        }catch(IOException e){

        }
        return "null";
    }

    public void registrar(View view){
        registrar = findViewById(R.id.txtRegistrar);
        Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
        startActivity(intent);

    }

    public void iniciarSesion(View view){


        usuario= findViewById(R.id.txtCorreo);
        contra= findViewById(R.id.txtContrasenia);


        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        //startActivity(intent);

        switch (login(usuario.getText().toString(),contra.getText().toString())){

            case "correcto":
                startActivity(intent);
                break;

            case "usuario":
                Toast toast=Toast.makeText(this,"Usuario incorrecta",Toast.LENGTH_SHORT);
                toast.show();

                break;

            case "contrasena":
                Toast toast2=Toast.makeText(this,"Contraseña incorrecta",Toast.LENGTH_SHORT);
toast2.show();
                break;

        }





    }
}
