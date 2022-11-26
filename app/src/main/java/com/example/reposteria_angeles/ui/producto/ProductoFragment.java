package com.example.reposteria_angeles.ui.producto;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

//import com.example.reposteria_angeles.databinding.FragmentHomeBinding;

import com.example.reposteria_angeles.ControladorBD;
import com.example.reposteria_angeles.databinding.FragmentProductoBinding;
import com.example.reposteria_angeles.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Calendar;

public class ProductoFragment extends Fragment {

    private FragmentProductoBinding binding;
    EditText name;
    EditText date;
    EditText quantity;
    EditText price;
    EditText description;
    EditText productId;
    ImageButton add, edit, delete, search, scan, list;
    String expiration;
    ControladorBD admin;
    private int day, month, year;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductoBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        name = root.findViewById(R.id.txtNombre);
        date = root.findViewById(R.id.txtCaduccidad);
        quantity = root.findViewById(R.id.txtCantidad);
        price = root.findViewById(R.id.txtPrecio);
        description = root.findViewById(R.id.txtDescripcion);
        productId = root.findViewById(R.id.txtBuscarProducto);
        ProductoViewModel productoViewModel =
                new ViewModelProvider(this).get(ProductoViewModel.class);
        admin  = new ControladorBD(root.getContext());


        add = (ImageButton) root.findViewById(R.id.btnAgregarG);
        edit = (ImageButton) root.findViewById(R.id.btnEditarG);
        delete=(ImageButton) root.findViewById(R.id.btnEliminarG);
        search = (ImageButton) root.findViewById(R.id.btnSearchProduct);
        scan = (ImageButton) root.findViewById(R.id.btnScan);
        list = (ImageButton) root.findViewById(R.id.btnList);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                day = c.get(Calendar.DAY_OF_MONTH);
                month = c.get(Calendar.MONTH);
                year = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        date.setText(i2+"/"+(i1+1)+"/"+i);
                        expiration = i +"-" + (i1 + 1) +"-"+i2;
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productId.getText().toString().isEmpty() || name.getText().toString().isEmpty() || date.getText().toString().isEmpty()
                    || quantity.getText().toString().isEmpty() || price.getText().toString().isEmpty() || description.getText().toString().isEmpty()){
                    Toast.makeText(root.getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }else if (admin.productCheckCode(productId.getText().toString())) {
                    if(admin.productUpdateData(productId.getText().toString(),name.getText().toString(),quantity.getText().toString(),price.getText().toString(),expiration,description.getText().toString())){
                        Toast.makeText(root.getContext(), "Producto actualizado", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(root.getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(root.getContext(), "Producto no encontrado", Toast.LENGTH_SHORT).show();
                }
                cleanComponents();

            }//onclick
        });//edit

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productId.getText().toString().isEmpty() || name.getText().toString().isEmpty() || date.getText().toString().isEmpty()
                        || quantity.getText().toString().isEmpty() || price.getText().toString().isEmpty() || description.getText().toString().isEmpty()){
                    Toast.makeText(root.getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!admin.productCheckCode(productId.getText().toString())){
                    String[] dateAux = date.getText().toString().split("/");
                    String dateExp = dateAux[2] + "-" + dateAux[1] + "-" + dateAux[0];
                    if(admin.productInsertData(productId.getText().toString(),name.getText().toString(),quantity.getText().toString(),price.getText().toString(),dateExp,description.getText().toString())){
                        Toast.makeText(root.getContext(), "Producto registrado con éxito", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(root.getContext(), "Error al registrar producto", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(root.getContext(), "Código de producto ya registrado anteriormente", Toast.LENGTH_SHORT).show();
                }
                cleanComponents();
            }//onCLick
        });//add

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productId.getText().toString().isEmpty()){
                    Toast.makeText(root.getContext(), "Ingresa el código del producto a eliminar", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!admin.productCheckCode(productId.getText().toString())){
                    Toast.makeText(root.getContext(), "No existe el producto", Toast.LENGTH_SHORT).show();
                }else if(admin.productDeleteData(productId.getText().toString())){
                    Toast.makeText(root.getContext(), "Producto eliminado exitosamente", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(root.getContext(), "Error al eliminar producto", Toast.LENGTH_SHORT).show();
                }
                cleanComponents();
            }//onClick
        });//delete

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchProduct();
            }//onClick
        });//search

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setPrompt("Lector de Códigos");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();
            }//onClick
        });//scan
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void cleanComponents(){
        name.setText("");
        date.setText("");
        quantity.setText("");
        price.setText("");
        description.setText("");
        productId.setText("");
        productId.requestFocus();
    }
    private void searchProduct(){
        if(productId.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Ingresae código a buscar", Toast.LENGTH_SHORT).show();
            return;
        }
        if(admin.productCheckCode(productId.getText().toString())){
            SQLiteDatabase MyBD = admin.getReadableDatabase();
            Cursor raw = MyBD.rawQuery("select productName, productQty, productPrice, productDate, productDescrip from product where productId=?",new String[]{productId.getText().toString()});
            if(raw.moveToFirst()){
                name.setText(raw.getString(0));
                quantity.setText(raw.getString(1));
                price.setText(raw.getString(2));
                String[] dateAux = raw.getString(3).split("-");
                date.setText(dateAux[2]+"/"+dateAux[1]+"/"+dateAux[0]);
                description.setText(raw.getString(4));
                MyBD.close();
            }else{
                Toast.makeText(getContext(), "Error al buscar producto", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(), "Producto no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(intentResult != null ){
            if(intentResult.getContents() == null){
                Toast.makeText(getContext(), "Lectura cancelada.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Datos leídos.", Toast.LENGTH_SHORT).show();
                productId.setText(intentResult.getContents());
                if(admin.productCheckCode(productId.getText().toString())){
                    searchProduct();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);

        }
    }



}//fragment