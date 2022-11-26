package com.example.reposteria_angeles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class RegistroActivity extends AppCompatActivity {

    EditText user;
    EditText password;
    EditText name;
    Button signIn;
    ControladorBD admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        user=findViewById(R.id.txtCorreo);
        password=findViewById(R.id.txtContrasenia);
        name=findViewById(R.id.txtNombreRegistro);
        signIn=findViewById(R.id.btnregistrar);
        admin = new ControladorBD(this);

        //onClick
        signIn.setOnClickListener(view -> {
            SQLiteDatabase db = admin.getWritableDatabase();
            String usr=user.getText().toString().trim();
            String pass=password.getText().toString();
            String nam=name.getText().toString();
            if(usr.isEmpty()||pass.isEmpty()||nam.isEmpty()){
                Toast.makeText(RegistroActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            Boolean checkuser = admin.userCheckUserName(usr);
            if(!checkuser) {
                Boolean insert = admin.userInsertData(usr, nam, pass);
                if(insert){
                    Toast.makeText(this, "¡Registrado satisfactoriamente!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(this, "¡Registro fallido!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Este correo ya ha sido registrado anteriormente", Toast.LENGTH_LONG).show();
            }


        });//setOnClickListener


    }
}