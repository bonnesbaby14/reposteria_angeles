package com.example.reposteria_angeles.ui.ingreso;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.reposteria_angeles.ControladorBD;
import com.example.reposteria_angeles.ListaGasto;
import com.example.reposteria_angeles.ListaIngreso;
import com.example.reposteria_angeles.MainActivity;
import com.example.reposteria_angeles.R;
import com.example.reposteria_angeles.databinding.FragmentIngresoBinding;
import com.example.reposteria_angeles.ui.gasto.GastoFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

public class IngresoFragment extends Fragment {

    //private FragmentIngresoBinding binding;
    private FragmentIngresoBinding binding;
    Spinner buscarVenta, buscarCliente, buscarProducto;
    EditText ventaTotal, nombreVenta, productVendido, descripcion, identificador;
    ImageButton agregar, editar, eliminar, buscar, clean, ver;
    ArrayList<String> ventas, clientes, productos;
    ArrayAdapter<String> adapterCliente, adapterProducto;
    String puntero;
    ControladorBD ingreso;
    int cant;
    private int dia, mes, anio;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        IngresoViewModel ingresoViewModel =
                new ViewModelProvider(this).get(IngresoViewModel.class);
        //Inicializacion de arraylist para spinner
        ventas = new ArrayList<String>();
        clientes = new ArrayList<String>();
        productos = new ArrayList<String>();

        binding = FragmentIngresoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //Vinculacion con componentes
        identificador = root.findViewById(R.id.txtIdentificadorV);
        buscarCliente = root.findViewById(R.id.spBuscarCliente);
        buscarProducto = root.findViewById(R.id.spBuscarProductoGasto);

        ventaTotal = root.findViewById(R.id.txtNombre);
        nombreVenta = root.findViewById(R.id.txtCantidad);
        productVendido = root.findViewById(R.id.txtPrecio);
        descripcion = root.findViewById(R.id.txtCaduccidad);

        agregar = root.findViewById(R.id.btnAgregarI);
        editar = root.findViewById(R.id.btnEditarI);
        eliminar = root.findViewById(R.id.btnEliminarI);
        buscar = root.findViewById(R.id.btnBuscarI);
        ver = root.findViewById(R.id.btnVerI);
        clean = root.findViewById(R.id.btnCleanI);

        //llenado de spinner
        ingreso = new ControladorBD(this.getContext());
        llenarProductos();
        llenarClientes();


        //Acciones de los botones

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase bd = ingreso.getReadableDatabase();


                String identificadorI = identificador.getText().toString();

                if( !identificadorI.isEmpty() ){

                    Cursor fila = bd.rawQuery("select cliente, productos, productosVendidos, nombreVenta, ventaTotal, descripcionVenta from ingreso " +
                            "where ingresoId="+identificadorI, null);

                    if (fila.moveToFirst()){
                        int numero = buscarNumeroProducto(fila.getString(1));
                        buscarProducto.setSelection(numero);
                        numero = buscarNumeroCliente(fila.getString(0));
                        buscarCliente.setSelection(numero);

                        productVendido.setText(fila.getString(2));
                        nombreVenta.setText(fila.getString(3));
                        ventaTotal.setText(fila.getString(4));
                        descripcion.setText(fila.getString(5));

                        bd.close();
                    } else {
                        Toast.makeText(IngresoFragment.this.getContext(),"Identificador de ingreso no existe.",Toast.LENGTH_SHORT).show();
                        identificador.setText("");
                        identificador.requestFocus();
                        bd.close();
                    }//else-if fila
                } else {
                    Toast.makeText(IngresoFragment.this.getContext(),"Ingresa identificador de ingreso",Toast.LENGTH_SHORT).show();
                    identificador.requestFocus();
                }//else
            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buscarProducto.getSelectedItem()==""|| identificador.getText().toString().isEmpty()
                        ||buscarCliente.getSelectedItem()==""|| ventaTotal.getText().toString().isEmpty()||
                        nombreVenta.getText().toString().isEmpty() || productVendido.getText().toString().isEmpty()
                        || descripcion.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Por favor llenar todos los campos.", Toast.LENGTH_LONG).show();
                }
                else {
                    SQLiteDatabase bd = ingreso.getWritableDatabase();

                    String producto = buscarProducto.getSelectedItem().toString();
                    String identificadorI =  identificador.getText().toString();
                    String cliente = buscarCliente.getSelectedItem().toString();
                    String venta = ventaTotal.getText().toString();
                    String nombreV = nombreVenta.getText().toString();
                    String productoV = productVendido.getText().toString();
                    String descripcionV = descripcion.getText().toString();


                    ContentValues registro = new ContentValues();
                    ContentValues registro2 = new ContentValues();

                    String[] parts = producto.split("-");
                    String nombre = parts[0]; //
                    String id = parts[1];

                    registro.put("ingresoId", identificadorI);
                    registro.put("cliente", cliente);
                    registro.put("productos", nombre);
                    registro.put("productosVendidos", productoV);
                    registro.put("nombreVenta", nombreV);
                    registro.put("ventaTotal", venta);
                    registro.put("descripcionVenta", descripcionV);


                    if (bd != null) {

                        //quitar producto

                        Cursor fila = bd.rawQuery("select productQty from product " +
                                "where productId="+id, null);

                        if (fila.moveToFirst()){

                            cant = Integer.parseInt(fila.getString(0));
                            int actual = Integer.parseInt(productoV);


                            if (cant < actual){
                                Toast.makeText(IngresoFragment.this.getContext(), "No puede realizar la venta, NO HAY PRODUCTO SUFICIENTE", Toast.LENGTH_SHORT).show();
                            }else{
                                long x = 0;
                                try {
                                    x = bd.insert("ingreso", null, registro);
                                } catch (SQLException e) {
                                    Log.e("Exception", "Error: " + String.valueOf(e.getMessage()));
                                }
                                int finalProductos = cant - actual;
                                String finalP = String.valueOf(finalProductos);

                                int cantidad=0;
                                registro2.put("productQty", finalP);

                                cantidad = bd.update("product",registro2,"productId="+id,null);

                                bd.close();

                                Toast.makeText(IngresoFragment.this.getContext(), "¡Ingreso registrado de manera exitosa!", Toast.LENGTH_SHORT).show();

                                if( cantidad > 0)
                                    Toast.makeText(IngresoFragment.this.getContext(),"Datos del producto actualizados.",Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(IngresoFragment.this.getContext(),"No se modifico el producto.",Toast.LENGTH_SHORT).show();

                            }
                        }

                        bd.close();
                    }

                    identificador.setText("");
                    ventaTotal.setText("");
                    nombreVenta.setText("");
                    productVendido.setText("");
                    descripcion.setText("");
                    identificador.requestFocus();

                               }
            }//onClick
        });//agregar

        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                identificador.setText("");
                ventaTotal.setText("");
                nombreVenta.setText("");
                productVendido.setText("");
                descripcion.setText("");
                identificador.requestFocus();
            }
        });

        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IngresoFragment.this.getContext(), ListaIngreso.class);
                startActivity(intent);
            }
        });//Ver

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buscarProducto.getSelectedItem()==""|| identificador.getText().toString().isEmpty()
                        ||buscarCliente.getSelectedItem()==""|| ventaTotal.getText().toString().isEmpty()||
                        nombreVenta.getText().toString().isEmpty() || productVendido.getText().toString().isEmpty()
                        || descripcion.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Por favor llenar todos los campos.", Toast.LENGTH_LONG).show();
                }
                else {
                    SQLiteDatabase bd = ingreso.getWritableDatabase();

                    String producto = buscarProducto.getSelectedItem().toString();
                    String identificadorI =  identificador.getText().toString();
                    String cliente = buscarCliente.getSelectedItem().toString();
                    String venta = ventaTotal.getText().toString();
                    String nombreV = nombreVenta.getText().toString();
                    String productoV = productVendido.getText().toString();
                    String descripcionV = descripcion.getText().toString();


                    ContentValues registro = new ContentValues();

                    String[] parts = producto.split("-");
                    String nombre = parts[0]; //
                    String id = parts[1];

                    registro.put("ingresoId", identificadorI);
                    registro.put("cliente", cliente);
                    registro.put("productos", nombre);
                    registro.put("productosVendidos", productoV);
                    registro.put("nombreVenta", nombreV);
                    registro.put("ventaTotal", venta);
                    registro.put("descripcionVenta", descripcionV);

                    int cantidad=0;

                    cantidad = bd.update("ingreso",registro,"ingresoId="+identificadorI,null);

                    bd.close();
                    if( cantidad > 0)
                        Toast.makeText(IngresoFragment.this.getContext(),"Datos del ingreso actualizados.",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(IngresoFragment.this.getContext(),"El identificador del ingreso no existe.",Toast.LENGTH_SHORT).show();

                    identificador.setText("");
                    ventaTotal.setText("");
                    nombreVenta.setText("");
                    productVendido.setText("");
                    descripcion.setText("");
                    identificador.requestFocus();
                }
            }//onClick
        });//editar

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase bd = ingreso.getWritableDatabase();

                String id = identificador.getText().toString();

                if( !id.isEmpty()){

                    int cantidad=0;

                    cantidad = bd.delete("ingreso","ingresoId = "+id, null);

                    bd.close();

                    identificador.setText("");
                    ventaTotal.setText("");
                    nombreVenta.setText("");
                    productVendido.setText("");
                    descripcion.setText("");
                    identificador.requestFocus();

                    if( cantidad > 0)
                        Toast.makeText(IngresoFragment.this.getContext(),"Ingreso eliminado.",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(IngresoFragment.this.getContext(),"El identificador del ingreso no existe.",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(IngresoFragment.this.getContext(),"Ingresa identificador del ingreso",Toast.LENGTH_SHORT).show();
                }//else
            }//onClick
        });//eliminar



        return root;
    }//onCreate

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private int buscarNumeroProducto(String string) {
        int num=0;
        SQLiteDatabase bd = ingreso.getReadableDatabase();


        Cursor fila = bd.rawQuery("select productName from product ", null);

        int n = fila.getCount();
        int cont = 0;

        if(n>0) {
            fila.moveToFirst();
            do {

                if(fila.getString(0).equals(string)) num = cont;
                cont++;
            } while (fila.moveToNext());
        }else{
            Toast.makeText(getContext(), "No hay productos registrados", Toast.LENGTH_SHORT).show();
        }

        bd.close();
        return num;
    }

    private int buscarNumeroCliente(String string) {
        int num=0;
        SQLiteDatabase bd = ingreso.getReadableDatabase();


        Cursor fila = bd.rawQuery("select nombre from cliente ", null);

        int n = fila.getCount();
        int cont = 0;

        if(n>0) {
            fila.moveToFirst();
            do {

                if(fila.getString(0).equals(string)) num = cont;
                cont++;
            } while (fila.moveToNext());
        }else{
            Toast.makeText(getContext(), "No hay clientes registrados", Toast.LENGTH_SHORT).show();
        }

        bd.close();
        return num;
    }


    public void llenarProductos(){
        SQLiteDatabase bd = ingreso.getReadableDatabase();


        Cursor fila = bd.rawQuery("select productName, productId from product ", null);

        int n = fila.getCount();
        int nr = 1;

        if(n>0) {
            fila.moveToFirst();
            do {
                productos.add(fila.getString(0)+"-"+fila.getString(1));
                nr++;
            } while (fila.moveToNext());
        }else{
            Toast.makeText(getContext(), "No hay productos registrados", Toast.LENGTH_SHORT).show();
        }
        bd.close();

        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, productos){
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                //change the color to which ever you want
                ((CheckedTextView) view).setTextColor(Color.BLACK);
                //change the size to which ever you want

                //for using sp values use setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buscarProducto.setAdapter(adapter);
    }

    public void llenarClientes(){
        SQLiteDatabase bd = ingreso.getReadableDatabase();


        Cursor fila = bd.rawQuery("select nombre from cliente", null);

        int n = fila.getCount();
        int nr = 1;

        if(n>0) {
            fila.moveToFirst();
            do {
                clientes.add(fila.getString(0));
                nr++;
            } while (fila.moveToNext());
        }else{
            Toast.makeText(getContext(), "No hay clientes registrados", Toast.LENGTH_SHORT).show();
        }
        bd.close();

        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, clientes){
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                //change the color to which ever you want
                ((CheckedTextView) view).setTextColor(Color.BLACK);
                //change the size to which ever you want

                //for using sp values use setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buscarCliente.setAdapter(adapter);
    }

}//class