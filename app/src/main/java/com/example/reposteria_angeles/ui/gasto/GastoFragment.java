package com.example.reposteria_angeles.ui.gasto;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.reposteria_angeles.ControladorBD;
import com.example.reposteria_angeles.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.reposteria_angeles.databinding.FragmentGastoBinding;
import com.example.reposteria_angeles.ui.cliente.ClienteFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GastoFragment extends Fragment {

    private FragmentGastoBinding binding;
    Spinner buscarGasto, buscarProducto;
    EditText nombre, costo, numProduct, description, identificador;
    ImageButton agregar, editar, eliminar, ver, buscar;
    ArrayList<String> gastosList, productsList;
    ArrayAdapter<String> productAdapter;
    String puntero;
    private int dia, mes, anio;
    ControladorBD gasto;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GastoViewModel gastoViewModel =
                new ViewModelProvider(this).get(GastoViewModel.class);
        //Inicializacion de arrayList
        gastosList = new ArrayList<String>();
        productsList = new ArrayList<String>();


        binding = FragmentGastoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //Vincular componentes
        identificador = root.findViewById(R.id.txtIdentificadorG);
        buscarGasto = root.findViewById(R.id.spBuscarCliente);
        buscarProducto = root.findViewById(R.id.spBuscarProductoGasto);
        nombre = root.findViewById(R.id.txtNombre);
        costo = root.findViewById(R.id.txtCantidad);
        numProduct  = root.findViewById(R.id.txtPrecio);
        description = root.findViewById(R.id.txtCaduccidad);
        //Llenado del spinner
        gasto = new ControladorBD(this.getContext());
        llenarProductos();
        //buttons
        agregar = (ImageButton) root.findViewById(R.id.btnAgregarG);
        editar = (ImageButton) root.findViewById(R.id.btnEditarG);
        eliminar = (ImageButton) root.findViewById(R.id.btnEliminarG);
        ver = (ImageButton) root.findViewById(R.id.btnVerG);
        buscar = (ImageButton) root.findViewById(R.id.btnBuscarG);
        //Acciones botones

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buscarProducto.getSelectedItem()==""||nombre.getText().toString().isEmpty()
                        ||costo.getText().toString().isEmpty()||numProduct.getText().toString().isEmpty()||
                        description.getText().toString().isEmpty() || identificador.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Por favor llenar todos los campos.", Toast.LENGTH_LONG).show();
                }
                else {
                    SQLiteDatabase bd = gasto.getWritableDatabase();

                    String producto = buscarProducto.getSelectedItem().toString();
                    String nombreGasto = nombre.getText().toString();
                    String costoGasto = costo.getText().toString();
                    String numeroGasto = numProduct.getText().toString();
                    String descripcionGasto = description.getText().toString();
                    String identificadorGasto = identificador.getText().toString();


                    ContentValues registro = new ContentValues();

                    registro.put("expenseId", identificadorGasto);
                    registro.put("productNameExpense", producto);
                    registro.put("expenseName", nombreGasto);
                    registro.put("expenseCost", costoGasto);
                    registro.put("expenseNumber", numeroGasto);
                    registro.put("expenseDescripcion", descripcionGasto);

                    if (bd != null) {

                        long x = 0;
                        try {
                            x = bd.insert("expense", null, registro);
                        } catch (SQLException e) {
                            Log.e("Exception", "Error: " + String.valueOf(e.getMessage()));
                        }

                        bd.close();
                    }

                    identificador.setText("");
                    nombre.setText("");
                    buscarProducto.set
                    direccion.setText("");
                    preferencia.setText("");
                    telefono.setText("");
                    identificador.requestFocus();

                    Toast.makeText(GastoFragment.this.getContext(), "¡Gasto registrado de manera exitosa!", Toast.LENGTH_SHORT).show();

                }
            }//onClick
        });//agregar

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }//onClick
        });//editar

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }//onClick
        });//eliminar

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void llenarProductos(){
        SQLiteDatabase bd = gasto.getReadableDatabase();


        Cursor fila = bd.rawQuery("select productName from product ", null);

        int n = fila.getCount();
        int nr = 1;

        if(n>0) {
            fila.moveToFirst();
            do {
                productsList.add(fila.getString(0));
                nr++;
            } while (fila.moveToNext());
        }else{
            Toast.makeText(getContext(), "No hay productos registrados", Toast.LENGTH_SHORT).show();
        }
        bd.close();

        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, productsList){
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
                    if(file=="gastos.txt")
                        arrayList.add(linea);
                    else
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
}//class