package com.example.reposteria_angeles.ui.producto;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

//import com.example.reposteria_angeles.databinding.FragmentHomeBinding;

import com.example.reposteria_angeles.MainActivity;
import com.example.reposteria_angeles.databinding.FragmentProductoBinding;
import com.example.reposteria_angeles.R;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ProductoFragment extends Fragment {

    private FragmentProductoBinding binding;
    EditText nombre;
    EditText caducidad;
    EditText cantidad;
    EditText precio;
    EditText descripcion;
    ImageButton agregar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProductoViewModel productoViewModel =
                new ViewModelProvider(this).get(ProductoViewModel.class);
        try {
            InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("productos.txt"));
            if (archivo==null) {

                grabar("producto","100","10","00-00-0000","productoDescripcion");
            }
        } catch (IOException e) {
            Log.d("archivo",e.toString());

            grabar("producto","100","10","00-00-0000","productoDescripcion");
            Toast tost = Toast.makeText(getContext(),"Se creo el archivo productos",Toast.LENGTH_SHORT);
            tost.show();
        }
        binding = FragmentProductoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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