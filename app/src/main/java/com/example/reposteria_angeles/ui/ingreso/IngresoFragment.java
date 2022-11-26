package com.example.reposteria_angeles.ui.ingreso;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.reposteria_angeles.MainActivity;
import com.example.reposteria_angeles.R;
import com.example.reposteria_angeles.databinding.FragmentIngresoBinding;

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
    EditText ventaTotal, nombreVenta, productVendido, descripcion;
    ImageButton agregar, editar, eliminar;
    ArrayList<String> ventas, clientes, productos;
    ArrayAdapter<String> adapterCliente, adapterProducto;
    String puntero;
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
        buscarVenta = root.findViewById(R.id.spBuscarVenta);
        buscarCliente = root.findViewById(R.id.spBuscarCliente);
        buscarProducto = root.findViewById(R.id.spBuscarProductoGasto);

        ventaTotal = root.findViewById(R.id.txtNombre);
        nombreVenta = root.findViewById(R.id.txtCantidad);
        productVendido = root.findViewById(R.id.txtPrecio);
        descripcion = root.findViewById(R.id.txtCaduccidad);

        IngresoViewModel ingresoViewModel1 =
                new ViewModelProvider(this).get(IngresoViewModel.class);

        agregar = root.findViewById(R.id.btnAgregarG);
        editar = root.findViewById(R.id.btnEditarG);
        eliminar = root.findViewById(R.id.btnEliminarG);
        //llenado de spinner
        clientes = llenarSpinner(clientes,buscarCliente,"clientes.txt");
        productos = llenarSpinner(productos,buscarProducto,"productos.txt");
        adapterCliente= new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, clientes);
        adapterProducto = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,productos);


        //Acciones de los botones

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buscarCliente.getSelectedItem().toString().equals("Selecciona...")||buscarProducto.getSelectedItem().toString().equals("Selecciona...")||
                   ventaTotal.getText().toString().isEmpty()||nombreVenta.getText().toString().isEmpty()||productVendido.getText().toString().isEmpty()||
                    descripcion.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Llene todos los campos", Toast.LENGTH_LONG).show();
                }else {
                    grabar(buscarCliente.getSelectedItem().toString(), buscarProducto.getSelectedItem().toString(),
                            ventaTotal.getText().toString(), nombreVenta.getText().toString(),
                            productVendido.getText().toString(), descripcion.getText().toString());
                    buscarCliente.setSelection(0);
                    buscarProducto.setSelection(0);
                    ventaTotal.setText("");
                    nombreVenta.setText("");
                    productVendido.setText("");
                    descripcion.setText("");
                    recargarIngresos();
                    Toast.makeText(getContext(), "Ingreso guardado", Toast.LENGTH_LONG).show();
                }
            }//onClick
        });//agregar

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("ingresos.txt"));
                    BufferedReader br = new BufferedReader(archivo);
                    String linea = br.readLine();
                    String todo = "";
                    boolean editar = false;
                    while(linea!=null){
                        if(linea.equals(puntero)){
                            String[] aux = linea.split("-");
                            aux[0] = buscarCliente.getSelectedItem().toString();
                            aux[1] = buscarProducto.getSelectedItem().toString();
                            aux[2] = ventaTotal.getText().toString();
                            aux[3] = nombreVenta.getText().toString();
                            aux[4] = productVendido.getText().toString();
                            aux[5] = descripcion.getText().toString();
                            String resultado = aux[0]+"-"+aux[1]+"-"+aux[2]+"-"+aux[3]+"-"+aux[4]
                                    +"-"+aux[5]+"-"+aux[6]+"\n";
                            todo+=resultado;
                            editar = true;
                        }else{
                            todo = todo + linea +"\n";
                        }
                        linea = br.readLine();
                    }//while
                    br.close();
                    archivo.close();

                    OutputStreamWriter archivo2 = new OutputStreamWriter(getContext().openFileOutput("ingresos.txt",MainActivity.MODE_PRIVATE));
                    archivo2.write(todo);
                    archivo2.flush();
                    archivo2.close();
                    if(editar)
                        Toast.makeText(getContext(), "Ingreso editado", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getContext(), "Seleccione venta", Toast.LENGTH_LONG).show();
                    //Limpieza de componentes
                    buscarCliente.setSelection(0);
                    buscarProducto.setSelection(0);
                    ventaTotal.setText("");
                    nombreVenta.setText("");
                    productVendido.setText("");
                    descripcion.setText("");
                    recargarIngresos();

                }catch (IOException ex){

                }//catch
            }//onClick
        });//editar

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("ingresos.txt"));
                    BufferedReader br = new BufferedReader(archivo);
                    String linea = br.readLine();
                    String todo = "";
                    boolean eliminado = false;
                    while(linea!=null){
                        if(linea.equals(puntero)){
                            eliminado = true;
                        }else{
                            todo = todo + linea +"\n";
                        }//else{
                        linea  = br.readLine();
                    }//while

                    br.close();
                    archivo.close();

                    OutputStreamWriter archivo2 = new OutputStreamWriter(getContext().openFileOutput("ingresos.txt", MainActivity.MODE_PRIVATE));
                    archivo2.write(todo);
                    archivo2.flush();
                    archivo2.close();

                    if(eliminado)
                        Toast.makeText(getContext(), "Ingreso eliminado", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getContext(), "Seleccione venta", Toast.LENGTH_LONG).show();

                    //Limpieza de componentes
                    buscarCliente.setSelection(0);
                    buscarProducto.setSelection(0);
                    ventaTotal.setText("");
                    nombreVenta.setText("");
                    productVendido.setText("");
                    descripcion.setText("");
                    recargarIngresos();

                }catch (IOException ex){

                }//catch
            }//onClick
        });//eliminar
        eventoNumProducto();
        buscarProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int costo = obtenerPrecioProducto(parent.getItemAtPosition(position).toString());
                int num = 0;
                try {
                    num = Integer.parseInt(productVendido.getText().toString());
                }catch (Exception e){

                }
                int total = costo * num;
                ventaTotal.setText(String.valueOf(total));
                ventaTotal.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


       // final TextView textView = binding.textGallery;
       // gastoViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        recargarIngresos();
        return root;
    }//onCreate

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void grabar(String cliente, String producto, String ventaTotal,
                       String nombreVenta, String productVendido, String descripcion){
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(getContext().openFileOutput("ingresos.txt", MainActivity.MODE_APPEND));
            //FECHA DE GUARDADO
            Calendar cal = Calendar.getInstance();
            dia = cal.get(Calendar.DAY_OF_MONTH);
            mes = cal.get(Calendar.MONTH);
            anio = cal.get(Calendar.YEAR);
            String fecha = dia+"/"+(mes+1)+"/"+anio;

            archivo.write(cliente+"-"+producto+"-"+ventaTotal+"-"+nombreVenta+"-"+productVendido+"-"+descripcion+"-"+fecha+"\n");
            archivo.flush();
            archivo.close();
        }catch (IOException ex){

        }//catch
    }//grabar

    public void recargarIngresos(){
        try {
            InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("ingresos.txt"));
            if(archivo==null){
                grabar("cliente","producto","1000","Venta","18","descripcion");

            }else{
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                String todo = "";
                ventas.clear();
                ventas.add("Selecciona...");
                while(linea!=null){
                    String[] split = linea.split("-");
                    Log.d("DATA",split.toString());
                    ventas.add(linea);
                    linea = br.readLine();
                }//while
                br.close();
                archivo.close();

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,ventas);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                buscarVenta.setAdapter(arrayAdapter);
                buscarVenta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String[] ingreso = parent.getItemAtPosition(position).toString().split("-");
                        if(ingreso.length>1){
                            puntero = parent.getItemAtPosition(position).toString();
                            //Cliente
                            int clientPosition = adapterCliente.getPosition(ingreso[0]);
                            buscarCliente.setSelection(clientPosition);
                            //Producto
                            int productPosition = adapterProducto.getPosition(ingreso[1]);
                            buscarProducto.setSelection(productPosition);
                            ventaTotal.setText(ingreso[2]);
                            nombreVenta.setText(ingreso[3]);
                            productVendido.setText(ingreso[4]);
                            descripcion.setText(ingreso[5]);

                        }//if
                        if(buscarVenta.getSelectedItem().toString().equals("Selecciona...")){
                            buscarCliente.setSelection(0);
                            buscarProducto.setSelection(0);
                            ventaTotal.setText("");
                            nombreVenta.setText("");
                            productVendido.setText("");
                            descripcion.setText("");
                            puntero = "";
                        }
                    }//onItemSelected

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }//onNothingSelected
                });
            }//else
        }catch (IOException ex){
            grabar("cliente","producto","500","nombreVenta","15","descripcion");
            Toast.makeText(getContext(), "Se creó el archivo ingresos", Toast.LENGTH_SHORT).show();
            recargarIngresos();
        }
    }//recargarIngresos

    private ArrayList<String> llenarSpinner(ArrayList<String> arrayList,Spinner spinner,String file){
        try {
            InputStreamReader archivo = new InputStreamReader(getContext().openFileInput(file));
            if(archivo!=null) {
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                arrayList.clear();
                arrayList.add("Selecciona...");
                while (linea!=null){
                    String[] split = linea.split("-");
                    arrayList.add(split[0]);
                    linea = br.readLine();
                }//while
                br.close();
                archivo.close();
                // se llena el spinner
                ArrayAdapter<String> adapter= new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }//if
        }catch(IOException ex){

        }//catch
        return arrayList;
    }//llenarSpinner
    private int obtenerPrecioProducto(String producto){
        int costo = 0;
        ArrayList<Integer> venta = new ArrayList<Integer>();

        try {
            InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("productos.txt"));
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();

            int pos = 0;
            while (linea != null) {

                String[] aux = linea.split("-");
                if(producto.equals(aux[0])){
                   costo = (int) Integer.parseInt(aux[2]);
                }
                linea = br.readLine();
            }
            br.close();
            archivo.close();
        } catch (IOException e) {

        }
        return costo;
    }//obtenerPrecioProducto
    private void eventoNumProducto(){
        productVendido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int costo = obtenerPrecioProducto(buscarProducto.getSelectedItem().toString());
                int num = 0;
                try {
                    num = Integer.parseInt(productVendido.getText().toString());
                }catch (Exception e){

                }
                int total = costo * num;
                ventaTotal.setText(String.valueOf(total));
                ventaTotal.setEnabled(false);
            }
        });
    }//eventoNumProducto
}//class