package com.example.reposteria_angeles.ui.gasto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reposteria_angeles.MainActivity;
import com.example.reposteria_angeles.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.reposteria_angeles.databinding.FragmentGastoBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class GastoFragment extends Fragment {

    private FragmentGastoBinding binding;
    Spinner buscarGasto, buscarProducto;
    EditText nombre, costo, numProduct, description;
    ImageButton agregar, editar, eliminar;
    ArrayList<String> gastosList, productsList;
    ArrayAdapter<String> productAdapter;
    String puntero;

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
        buscarGasto = root.findViewById(R.id.spBuscarCliente);
        buscarProducto = root.findViewById(R.id.spBuscarProductoGasto);
        nombre = root.findViewById(R.id.txtNombre);
        costo = root.findViewById(R.id.txtCantidad);
        numProduct  = root.findViewById(R.id.txtPrecio);
        description = root.findViewById(R.id.txtCaduccidad);
        //Llenado del spinner
        productsList = llenarSpinner(productsList,buscarProducto,"productos.txt");
        productAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,productsList);
        //buttons
        agregar = (ImageButton) root.findViewById(R.id.btnAgregarP);
        editar = (ImageButton) root.findViewById(R.id.btnEditarProducto);
        eliminar = (ImageButton) root.findViewById(R.id.btnEliminarProducto);

       // final TextView textView = binding.textSlideshow;
        //gastoViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Métodos
    public void grabar(String producto, String nombre, String costo, String numProduct, String description ){
        try{
            OutputStreamWriter archivo = new OutputStreamWriter(getContext().openFileOutput("gastos.txt", MainActivity.MODE_APPEND));
            archivo.write(producto+"/"+nombre+"/"+costo+"/"+numProduct+"/"+description+"\n");
            archivo.flush();
            archivo.close();
        }catch (IOException ex){

        }//catch
    }//grabar

    public void recargarGastos(){
        try {
            InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("gastos.txt"));
            if(archivo==null){
                grabar("product","name","100","3","description");
            }else{
               llenarSpinner(gastosList,buscarGasto,"gastos.txt");
               buscarGasto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                   @Override
                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       String[] gasto = parent.getItemAtPosition(position).toString().split("/");
                       if(gasto.length>1){
                           puntero = parent.getItemAtPosition(position).toString();
                           //Producto
                           int productPosition = productAdapter.getPosition(gasto[0]);
                           buscarProducto.setSelection(productPosition);
                           nombre.setText(gasto[1]);
                           costo.setText(gasto[2]);
                           numProduct.setText(gasto[3]);
                           description.setText(gasto[4]);
                       }
                   }//onItemSelected

                   @Override
                   public void onNothingSelected(AdapterView<?> parent) {
                   }//onNothingSelected
               });//buscarGasto

            }//else
        }catch (IOException ex){
            grabar("product","name","100","3","description");
            Toast.makeText(getContext(), "Se creó el archivo gastos", Toast.LENGTH_SHORT).show();
            recargarGastos();
        }
    }//recargarGastos

    private ArrayList<String> llenarSpinner(ArrayList<String> arrayList,Spinner spinner,String file){
        try {
            InputStreamReader archivo = new InputStreamReader(getContext().openFileInput(file));
            if(archivo!=null) {
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                arrayList.clear();
                arrayList.add("Selecciona...");
                while (linea!=null){
                    String[] split = linea.split("/");
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