package com.example.reposteria_angeles.ui.producto;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class ProductoFragment extends Fragment {

    private FragmentProductoBinding binding;
    EditText nombre;
    EditText caducidad;
    EditText cantidad;
    EditText precio;
    EditText descripcion;
    ImageButton agregar;
    Spinner spinner;
    ArrayList<String> productos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        productos=new ArrayList<String>();
        binding = FragmentProductoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ProductoViewModel productoViewModel =
                new ViewModelProvider(this).get(ProductoViewModel.class);
        try {
            InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("productos.txt"));
            if (archivo==null) {

                grabar("producto","100","10","00-00-0000","productoDescripcion");
            }else{

                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                String todo = "";
                while (linea != null) {
                    String [] split=linea.split("/");
                    Log.d("DATA",split.toString());
                    productos.add(linea);
                    linea = br.readLine();
                }
                br.close();
                archivo.close();
                spinner =root.findViewById(R.id.spBuscarProducto);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, productos);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
spinner.setAdapter(arrayAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String tutorialsName = parent.getItemAtPosition(position).toString();
                        Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,          Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onNothingSelected(AdapterView <?> parent) {
                    }
                });
            }
        } catch (IOException e) {
            Log.d("archivo",e.toString());

            grabar("producto","100","10","00-00-0000","productoDescripcion");
            Toast tost = Toast.makeText(getContext(),"Se creo el archivo productos",Toast.LENGTH_SHORT);
            tost.show();
        }


        agregar= (ImageButton) root.findViewById(R.id.btnAgregarP);

agregar.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {


         nombre= root.findViewById(R.id.txtNombre);
        caducidad= root.findViewById(R.id.txtCaduccidad);
         cantidad= root.findViewById(R.id.txtCantidad);
         precio= root.findViewById(R.id.txtPrecio);
         descripcion= root.findViewById(R.id.txtDescripcion);

grabar(nombre.getText().toString(),cantidad.getText().toString(),precio.getText().toString(),caducidad.getText().toString(),descripcion.getText().toString());

nombre.setText("");
caducidad.setText("");
cantidad.setText("");
precio.setText("");
caducidad.setText("");
descripcion.setText(" ");

        Toast tost = Toast.makeText(getContext(),"Producto Gurdado",Toast.LENGTH_LONG);
        tost.show();
    }
});



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void grabar(String nombre,String cantidad, String  precio, String fecha,String descipcion) {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(getContext().openFileOutput("productos.txt", MainActivity.MODE_APPEND));
            archivo.write("\n"+nombre+"/"+cantidad+"/"+precio+"/"+fecha+"/"+descipcion);
            archivo.flush();
            archivo.close();

        } catch (IOException e) {
        }


    }

}