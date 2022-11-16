package com.example.reposteria_angeles.ui.cliente;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reposteria_angeles.Cliente;
import com.example.reposteria_angeles.ControladorBD;
import com.example.reposteria_angeles.ListaClientes;
import com.example.reposteria_angeles.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.reposteria_angeles.databinding.FragmentClienteBinding;

import java.util.ArrayList;
import java.util.List;


public class ClienteFragment extends Fragment {

        private FragmentClienteBinding binding;
        //Componentes del fragment
        TextView listaClientes;
        EditText nombre, direccion, telefono, preferencia, identificador;
        ImageButton agregar, editar, eliminar, buscar, ver;
        ControladorBD cliente;
        private int dia, mes, anio;
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            //Inicializar controlador
            cliente = new ControladorBD(this.getContext(),"reposteria.db",null, 1);

            binding = FragmentClienteBinding.inflate(inflater, container, false);
            View root = binding.getRoot();
            //Asociacion de los componentes

            nombre = root.findViewById(R.id.txtNombre);
            direccion =  root.findViewById(R.id.txtCantidad);
            telefono  = root.findViewById(R.id.txtPrecio);
            preferencia  = root.findViewById(R.id.txtCaduccidad);
            identificador = root.findViewById(R.id.txtIdentificadorCliente);


            //botones
            agregar = (ImageButton) root.findViewById(R.id.btnAgregarP);
            editar = (ImageButton) root.findViewById(R.id.btnEditarProducto);
            eliminar = (ImageButton) root.findViewById(R.id.btnEliminarProducto);
            buscar = (ImageButton) root.findViewById(R.id.btnBuscarCliente);
            ver = (ImageButton) root.findViewById(R.id.btnVerCliente);
            //Eventos

            editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SQLiteDatabase bd = cliente.getWritableDatabase();

                    String nombreCliente = nombre.getText().toString();
                    String direccionCliente = direccion.getText().toString();
                    String telefonoCliente = telefono.getText().toString();
                    String preferenciaCliente = preferencia.getText().toString();
                    String identificadorCliente = identificador.getText().toString();

                    if( !nombreCliente.isEmpty() && !direccionCliente.isEmpty() && !telefonoCliente.isEmpty() &&
                            !preferenciaCliente.isEmpty()){

                        ContentValues registro = new ContentValues();

                        registro.put("nombre",nombreCliente);
                        registro.put("direccion",direccionCliente);
                        registro.put("telefono",telefonoCliente);
                        registro.put("preferencia",preferenciaCliente);

                        int cantidad=0;

                        cantidad = bd.update("cliente",registro,"identificador="+identificadorCliente,null);

                        bd.close();
                        if( cantidad > 0)
                            Toast.makeText(ClienteFragment.this.getContext(),"Datos del cliente actualizados.",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(ClienteFragment.this.getContext(),"El identificador de cliente no existe.",Toast.LENGTH_SHORT).show();

                        identificador.setText("");
                        nombre.setText("");
                        direccion.setText("");
                        preferencia.setText("");
                        telefono.setText("");
                        identificador.requestFocus();
                    } else {
                        Toast.makeText(ClienteFragment.this.getContext(),"Debes registrar primero los datos",Toast.LENGTH_SHORT).show();
                    }//else
                }//onClick


            });//editar

            //boton agregar
            agregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SQLiteDatabase bd = cliente.getWritableDatabase();

                    String nombreCliente = nombre.getText().toString();
                    String direccionCliente = direccion.getText().toString();
                    String telefonoCliente = telefono.getText().toString();
                    String preferenciaCliente = preferencia.getText().toString();
                    String identificadorCliente = identificador.getText().toString();

                    if( !nombreCliente.isEmpty() && !direccionCliente.isEmpty() && !telefonoCliente.isEmpty() &&
                            !preferenciaCliente.isEmpty()){

                        ContentValues registro = new ContentValues();

                        registro.put("identificador", identificadorCliente);
                        registro.put("nombre",nombreCliente);
                        registro.put("direccion",direccionCliente);
                        registro.put("telefono",telefonoCliente);
                        registro.put("preferencia",preferenciaCliente);
                        if( bd != null){

                            long x = 0;
                            try {
                                x = bd.insert("cliente",null,registro);
                            } catch (SQLException e){
                                Log.e("Exception","Error: "+String.valueOf(e.getMessage()));
                            }

                            bd.close();
                        }

                        identificador.setText("");
                        nombre.setText("");
                        direccion.setText("");
                        preferencia.setText("");
                        telefono.setText("");
                        identificador.requestFocus();

                        Toast.makeText(ClienteFragment.this.getContext(),"Cliente registrado.",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ClienteFragment.this.getContext(),"Debes registrar primero los datos",Toast.LENGTH_SHORT).show();
                    }//else
                }//onClick
            });//agregar

            buscar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SQLiteDatabase bd = cliente.getReadableDatabase();


                    String identificadorCliente = identificador.getText().toString();

                    if( !identificadorCliente.isEmpty() ){

                        Cursor fila = bd.rawQuery("select nombre, direccion, telefono, preferencia from cliente " +
                                "where identificador="+identificadorCliente, null);

                        if (fila.moveToFirst()){

                            nombre.setText(fila.getString(0));
                            direccion.setText(fila.getString(1));
                            telefono.setText(fila.getString(2));
                            preferencia.setText(fila.getString(3));
                            bd.close();
                        } else {
                            Toast.makeText(ClienteFragment.this.getContext(),"Identificador de cliente no existe.",Toast.LENGTH_SHORT).show();
                            identificador.setText("");
                            identificador.requestFocus();
                            bd.close();
                        }//else-if fila
                    } else {
                        Toast.makeText(ClienteFragment.this.getContext(),"Ingresa identificador de cliente",Toast.LENGTH_SHORT).show();
                        identificador.requestFocus();
                    }//else
                }//onClick
            });//buscar

            //boton eliminar
            eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SQLiteDatabase bd = cliente.getWritableDatabase();

                    String id = identificador.getText().toString();

                    if( !id.isEmpty()){

                        int cantidad=0;

                        cantidad = bd.delete("cliente","identificador = "+id, null);

                        bd.close();

                        identificador.setText("");
                        nombre.setText("");
                        direccion.setText("");
                        preferencia.setText("");
                        telefono.setText("");
                        identificador.requestFocus();

                        if( cantidad > 0)
                            Toast.makeText(ClienteFragment.this.getContext(),"Cliente eliminado.",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(ClienteFragment.this.getContext(),"El identificador del cliente no existe.",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ClienteFragment.this.getContext(),"Ingresa identificador cliente",Toast.LENGTH_SHORT).show();
                    }//else
                }//onClick
            });//eliminar

            ver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ClienteFragment.this.getContext(), ListaClientes.class);
                    startActivity(intent);
                }
            });//Ver


            return root;
        }



        @Override
        public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }

    }

