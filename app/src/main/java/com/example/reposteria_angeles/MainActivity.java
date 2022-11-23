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
    EditText user;
    EditText password;
    TextView registrar;
    ControladorBD admin;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user= findViewById(R.id.txtCorreo);
        password= findViewById(R.id.txtContrasenia);
        admin = new ControladorBD(this);
    }



    public String checkCredentials(String usr,String paswd){
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor cursorUser = db.rawQuery("Select * from users where userEmail=?",new String[]{usr});
        Cursor cursorPassw = db.rawQuery("Select * from users where userPassword=?",new String[]{paswd});
        if(cursorUser.getCount()==1 && cursorPassw.getCount()==1) {
            db.close();
            return "correct";
        }
        if(cursorUser.getCount()==1 && cursorPassw.getCount()==0) {
            db.close();
            return "wfield";
        }
        else {
            db.close();
            return "none";
        }
    }//checkCredentials

    public void signin(View view){
        registrar = findViewById(R.id.txtRegistrar);
        Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
        startActivity(intent);

    }

    public void login(View view){
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        if(!user.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
            switch (checkCredentials(user.getText().toString(), password.getText().toString())) {
                case "correct":
                    Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    break;
                case "wfield":
                    Toast.makeText(this, "Usuario/Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    break;
                case "none":
                    Toast.makeText(this, "Usuario inexistente", Toast.LENGTH_SHORT).show();
            }
        }else
            Toast.makeText(this, "Completar todos los campos", Toast.LENGTH_SHORT).show();

    }//login
}
