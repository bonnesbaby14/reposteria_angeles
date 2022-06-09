package com.example.reposteria_angeles.ui.producto;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

//import com.example.reposteria_angeles.databinding.FragmentHomeBinding;

import com.example.reposteria_angeles.MainActivity;
import com.example.reposteria_angeles.databinding.FragmentProductoBinding;
import com.example.reposteria_angeles.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

public class ProductoFragment extends Fragment {

    private FragmentProductoBinding binding;
    EditText nombre;
    EditText caducidad;
    EditText cantidad;
    EditText precio;
    EditText descripcion;
    ImageButton agregar, editar, eliminar;
    private int dia, mes, anio;

    Spinner spinner;
    ArrayList<String> productos;
    String puntero;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        productos = new ArrayList<String>();
        binding = FragmentProductoBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        nombre = root.findViewById(R.id.txtNombre);
        caducidad = root.findViewById(R.id.txtCaduccidad);
        cantidad = root.findViewById(R.id.txtCantidad);
        precio = root.findViewById(R.id.txtPrecio);
        descripcion = root.findViewById(R.id.txtDescripcion);
        spinner = root.findViewById(R.id.spBuscarProducto);
        ProductoViewModel productoViewModel =
                new ViewModelProvider(this).get(ProductoViewModel.class);


        agregar = (ImageButton) root.findViewById(R.id.btnAgregarP);
        editar = (ImageButton) root.findViewById(R.id.btnEditarProducto);
        eliminar=(ImageButton) root.findViewById(R.id.btnEliminarProducto);

        caducidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                anio = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        caducidad.setText(i2+"/"+(i1+1)+"/"+i);
                    }
                },anio,mes,dia);
                datePickerDialog.show();
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("productos.txt"));
                    BufferedReader br = new BufferedReader(archivo);
                    String linea = br.readLine();
                    String todo = "";
                    boolean editado = false;
                    while (linea != null) {
                        if (linea.equals(puntero)) {
                            String[] aux = linea.split("-");
                            aux[0] = nombre.getText().toString();
                            aux[1] = cantidad.getText().toString();
                            aux[2] = precio.getText().toString();
                            aux[3] = caducidad.getText().toString().replace("/","/");
                            aux[4] = descripcion.getText().toString();
                            String resultado = aux[0] + "-" + aux[1] + "-" + aux[2] + "-" + aux[3] + "-" + aux[4] + "\n";
                            todo = todo + resultado;
                            editado = true;

                        } else {
                            todo = todo + linea + "\n";
                        }


                        linea = br.readLine();
                    }

                    br.close();
                    archivo.close();

                    OutputStreamWriter archivo2 = new OutputStreamWriter(getContext().openFileOutput("productos.txt", MainActivity.MODE_PRIVATE));
                    archivo2.write(todo);
                    archivo2.flush();
                    archivo2.close();
                    if(editado)
                        Toast.makeText(getContext(), "Producto editado", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getContext(), "Seleccione producto", Toast.LENGTH_LONG).show();

                    nombre.setText("");
                    caducidad.setText("");
                    cantidad.setText("");
                    precio.setText("");
                    caducidad.setText("");
                    descripcion.setText(" ");
                    recargarProductos();
                } catch (IOException e) {

                }


            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nombre.getText().toString().isEmpty()||cantidad.getText().toString().isEmpty()||
                        precio.getText().toString().isEmpty()||caducidad.getText().toString().isEmpty()||
                        descripcion.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Llene los campos", Toast.LENGTH_LONG).show();

                }else {
                    String caduc = caducidad.getText().toString().replace("/","/");

                    grabar(nombre.getText().toString(), cantidad.getText().toString(), precio.getText().toString(), caduc, descripcion.getText().toString());

                    nombre.setText("");
                    caducidad.setText("");
                    cantidad.setText("");
                    precio.setText("");
                    caducidad.setText("");
                    descripcion.setText(" ");
                    recargarProductos();
                    Toast tost = Toast.makeText(getContext(), "Producto Guardado", Toast.LENGTH_LONG);
                    tost.show();
                }
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("productos.txt"));
                    BufferedReader br = new BufferedReader(archivo);
                    String linea = br.readLine();
                    String todo = "";
                    boolean eliminado = false;
                    while (linea != null) {
                        if (linea.equals(puntero)) {
                            eliminado  = true;
                        } else {
                            todo = todo + linea + "\n";
                        }


                        linea = br.readLine();
                    }

                    br.close();
                    archivo.close();

                    OutputStreamWriter archivo2 = new OutputStreamWriter(getContext().openFileOutput("productos.txt", MainActivity.MODE_PRIVATE));
                    archivo2.write(todo);
                    archivo2.flush();
                    archivo2.close();
                    if(eliminado)
                        Toast.makeText(getContext(), "Producto eliminado", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getContext(), "Seleccione producto", Toast.LENGTH_LONG).show();


                    nombre.setText("");
                    caducidad.setText("");
                    cantidad.setText("");
                    precio.setText("");
                    caducidad.setText("");
                    descripcion.setText(" ");
                    recargarProductos();
                } catch (IOException e) {

                }
            }
        });

        recargarProductos();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void grabar(String nombre, String cantidad, String precio, String fecha, String descipcion) {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(getContext().openFileOutput("productos.txt", MainActivity.MODE_APPEND));
            archivo.write(nombre + "-" + cantidad + "-" + precio + "-" + fecha + "-" + descipcion + "\n");
            archivo.flush();
            archivo.close();

        } catch (IOException e) {
        }


    }

    public void recargarProductos() {
        try {
            InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("productos.txt"));
            if (archivo == null) {

                grabar("producto", "100", "10", "00/00/0000", "productoDescripcion");
            } else {

                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                String todo = "";
                productos.clear();
                productos.add("Selecciona...");
                while (linea != null) {
                    String[] split = linea.split("-");
                    Log.d("DATA", split.toString());
                    productos.add(linea);
                    linea = br.readLine();
                }
                br.close();
                archivo.close();

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, productos);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String[] producto = parent.getItemAtPosition(position).toString().split("-");
                        if (producto.length > 1) {
                            puntero = parent.getItemAtPosition(position).toString();

                            nombre.setText(producto[0].toString());
                            cantidad.setText(producto[1].toString());
                            precio.setText(producto[2].toString());
                            caducidad.setText(producto[3].toString().replace("-","/"));
                            descripcion.setText(producto[4].toString());
                        }
                        if(spinner.getSelectedItem().toString()=="Selecciona..."){
                            nombre.setText("");
                            caducidad.setText("");
                            cantidad.setText("");
                            precio.setText("");
                            caducidad.setText("");
                            descripcion.setText(" ");
                            puntero = "";
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        } catch (IOException e) {
            Log.d("archivo", e.toString());

            grabar("producto", "100", "10", "00/00/0000", "productoDescripcion");
            Toast tost = Toast.makeText(getContext(), "Se creo el archivo productos", Toast.LENGTH_SHORT);
            tost.show();
            recargarProductos();
        }
    }

}