package com.example.reposteria_angeles;

import androidx.appcompat.app.AppCompatActivity;

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

    EditText usuario;
    EditText contra;
    EditText nombre;
    Button registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        usuario=findViewById(R.id.txtCorreo);
        contra=findViewById(R.id.txtContrasenia);
        nombre=findViewById(R.id.txtNombreRegistro);
        registrar=findViewById(R.id.btnregistrar);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user=usuario.getText().toString();
                String contrasena=contra.getText().toString();
                String nombr=nombre.getText().toString();
                if(user.isEmpty()||contrasena.isEmpty()||nombr.isEmpty()){
                    Toast.makeText(RegistroActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }


                try {
                    InputStreamReader archivo = new InputStreamReader(openFileInput("usuarios.txt"));
                    BufferedReader br = new BufferedReader(archivo);
                    String linea = br.readLine();
                    String todo = "";
                    while (linea != null) {
                        String [] split=linea.split("-");
                        Log.d("DATA",split.toString());
                        if(split[0].equals(user)){
                            Toast.makeText(getApplicationContext(),"El usurio ya existe",Toast.LENGTH_LONG).show();
                            return;
                        }
                        linea = br.readLine();
                    }
                    br.close();
                    archivo.close();

                    OutputStreamWriter archivo2 = new OutputStreamWriter(openFileOutput("usuarios.txt", MainActivity.MODE_APPEND));
                    archivo2.write(""+user+"-"+contrasena+"-"+nombr+"/admin \n");
                    archivo2.flush();
                    archivo2.close();

                    Toast.makeText(getApplicationContext(),"Se registro correctamente",Toast.LENGTH_LONG).show();
                    finish();


                }catch(IOException e){
                    Toast.makeText(getApplicationContext(),"Ocurrio un error"+e.toString(),Toast.LENGTH_LONG).show();
                }


            }
        });


    }
}