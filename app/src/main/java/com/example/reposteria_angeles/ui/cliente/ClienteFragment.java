package com.example.reposteria_angeles.ui.cliente;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.reposteria_angeles.MainActivity;
import com.example.reposteria_angeles.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.reposteria_angeles.databinding.FragmentClienteBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class ClienteFragment extends Fragment {

        private FragmentClienteBinding binding;
        //Componentes del fragment
        Spinner buscarCliente;
        EditText nombre, direccion, telefono, preferencia;
        ArrayList<String> clientes;
        ImageButton agregar, editar, eliminar;
        String puntero;
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            //Inicializacion del arraylist
            clientes = new ArrayList<String>();
            binding = FragmentClienteBinding.inflate(inflater, container, false);
            View root = binding.getRoot();
            //Asociacion de los componentes
            buscarCliente = root.findViewById(R.id.spBuscarCliente);
            nombre = root.findViewById(R.id.txtNombre);
            direccion =  root.findViewById(R.id.txtCantidad);
            telefono  = root.findViewById(R.id.txtPrecio);
            preferencia  = root.findViewById(R.id.txtCaduccidad);

            //botones
            agregar = (ImageButton) root.findViewById(R.id.btnAgregarP);
            editar = (ImageButton) root.findViewById(R.id.btnEditarProducto);
            eliminar = (ImageButton) root.findViewById(R.id.btnEliminarProducto);
            //Eventos

            editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("clientes.txt"));
                        BufferedReader br = new BufferedReader(archivo);
                        String linea = br.readLine();
                        String todo = "";
                        while(linea!=null){
                            if(linea.equals(puntero)){
                                String[] aux  =linea.split("/");
                                aux[0] = nombre.getText().toString();
                                aux[1] = direccion.getText().toString();
                                aux[2] = telefono.getText().toString();
                                aux[3] = preferencia.getText().toString();
                                String resultado = aux[0] + "/" + aux[1] + "/" + aux[2] + "/" + aux[3] + "\n";
                                todo += resultado;
                            }else{
                                //Si no es la linea seleccionada se mueve de renglon
                                todo = todo + linea + "\n";
                            }
                            //Se cambia a la linea siguiente
                            linea = br.readLine();

                        }//while
                        br.close();
                        archivo.close();
                        //Se actualizan los datos en el archivo
                        OutputStreamWriter archivo2 = new OutputStreamWriter(getContext().openFileOutput("clientes.txt", MainActivity.MODE_PRIVATE));
                        archivo2.write(todo);
                        archivo2.flush();
                        archivo2.close();
                        Toast.makeText(getContext(), "Cliente editado", Toast.LENGTH_LONG).show();
                        nombre.setText("");
                        direccion.setText("");
                        telefono.setText("");
                        preferencia.setText("");
                        //Se recargan los clientes
                        recargarClientes();
                    }catch (IOException ex){
                        Toast.makeText(getContext(), "Error.", Toast.LENGTH_LONG).show();
                    }
                }//onClick


            });//editar

            //boton agregar
            agregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grabar(nombre.getText().toString(),direccion.getText().toString(),telefono.getText().toString(),preferencia.getText().toString());
                    nombre.setText("");
                    direccion.setText("");
                    telefono.setText("");
                    preferencia.setText("");
                    recargarClientes();
                    Toast.makeText(getContext(),"Cliente Guardado",Toast.LENGTH_LONG).show();
                }//onClick
            });//agregar

            //boton eliminar
            eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("clientes.txt"));
                        BufferedReader br = new BufferedReader(archivo);
                        String linea = br.readLine();
                        String todo = "";
                        while(linea!=null){
                            if(linea.equals(puntero)){

                            }else{
                                todo = todo + linea +"\n";
                            }
                            linea = br.readLine();
                        }//while
                        br.close();
                        archivo.close();

                        OutputStreamWriter archivo2 = new OutputStreamWriter(getContext().openFileOutput("clientes.txt",MainActivity.MODE_PRIVATE));
                        archivo2.write(todo);
                        archivo2.flush();
                        archivo2.close();

                        Toast.makeText(getContext(),"Cliente eliminado",Toast.LENGTH_LONG).show();

                        nombre.setText("");
                        direccion.setText("");
                        telefono.setText("");
                        preferencia.setText("");
                        recargarClientes();

                    }catch (IOException ex){

                    }
                }//onClick
            });//eliminar

            // final TextView textView = binding.textSlideshow;
            //gastoViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
            recargarClientes();
            return root;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }

    public void grabar(String nombre, String direccion, String telefono, String preferencia){
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(getContext().openFileOutput("clientes.txt",MainActivity.MODE_APPEND));
            archivo.write(nombre+"/"+direccion+"/"+telefono+"/"+preferencia+"\n");
            archivo.flush();
            archivo.close();

        }catch(IOException ex){

        }
    }//grabar

    public void recargarClientes(){
        try {
            InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("clientes.txt"));
            if(archivo==null){
                grabar("cliente","direccion","3344556677","preferences");
            }else{
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                String todo = "";
                clientes.clear();
                clientes.add("Selecciona...");
                while(linea!=null){
                    String[] split = linea.split("/");
                    Log.d("DATA",split.toString());
                    clientes.add(linea);
                    linea = br.readLine();
                }//while
                br.close();
                archivo.close();

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,clientes);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                buscarCliente.setAdapter(arrayAdapter);
                //Llenado de datos en componentes al seleccionar un cliente preguardado
                buscarCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String[] cliente = parent.getItemAtPosition(position).toString().split("/");
                        if(cliente.length>1){
                            puntero = parent.getItemAtPosition(position).toString();

                            nombre.setText(cliente[0].toString());
                            direccion.setText(cliente[1].toString());
                            telefono.setText(cliente[2].toString());
                            preferencia.setText(cliente[3].toString());
                        }//if
                    }//onItemSelected

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }//onNothingSelected
                });//Seleccion de items
            }//else
        }catch (IOException ex){
            Log.d("archivo", ex.toString());
            grabar("cliente","direccion","3344556677","preferences");
            Toast.makeText(getContext(),"Se creó el archivo clientes",Toast.LENGTH_SHORT).show();
            recargarClientes();
        }
    }//recargarClientes

    }

