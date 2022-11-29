package com.example.reposteria_angeles.ui.producto;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

//import com.example.reposteria_angeles.databinding.FragmentHomeBinding;

import com.example.reposteria_angeles.ControladorBD;
import com.example.reposteria_angeles.ProductsList;
import com.example.reposteria_angeles.databinding.FragmentProductoBinding;
import com.example.reposteria_angeles.R;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Calendar;

public class ProductoFragment extends Fragment {

    private FragmentProductoBinding binding;
    EditText name;
    EditText date;
    EditText quantity;
    EditText price;
    EditText description;
    EditText productId;
    ImageButton add, edit, delete, search, scan, list, clear;
    String expiration;
    ControladorBD admin;
    private int day, month, year;
    private final static String CHANNEL_ID = "NOTIFICACION";
    public final static int NOTIFICACION_ID = 0;

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
        clear = (ImageButton) root.findViewById(R.id.btnProductClear);


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanComponents();
            }
        });


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
                    if(admin.productUpdateData(productId.getText().toString(),name.getText().toString(),quantity.getText().toString(),price.getText().toString(),date.getText().toString().replace("/","-"),description.getText().toString())){
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
                    String dateAux = date.getText().toString().replace("/","-");
                    if(admin.productInsertData(productId.getText().toString(),name.getText().toString(),quantity.getText().toString(),price.getText().toString(),dateAux,description.getText().toString())){
                        Toast.makeText(root.getContext(), "Producto registrado con éxito", Toast.LENGTH_SHORT).show();
                        crearCanalNotificacion();
                        crearNotificacion(quantity.getText().toString(),name.getText().toString());
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
                scanCode();
            }//onClick
        });//scan

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductoFragment.this.getContext(), ProductsList.class);
                startActivity(intent);
            }
        });//list
        return root;
    }//onCreateView

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
                String dateAux = raw.getString(3).replace("-","/");
                date.setText(dateAux);
                description.setText(raw.getString(4));
                MyBD.close();
            }else{
                Toast.makeText(getContext(), "Error al buscar producto", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(), "Producto no encontrado", Toast.LENGTH_SHORT).show();
        }
    }


    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Subir volumen para activar flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }//scanCode

    ActivityResultLauncher<ScanOptions> barLauncher =  registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null){
            Toast.makeText(getContext(), "Datos leídos.", Toast.LENGTH_SHORT).show();
            productId.setText(result.getContents());
            if(admin.productCheckCode(result.getContents()))
                searchProduct();
        }else{
            Toast.makeText(getContext(), "Lectura cancelada.", Toast.LENGTH_SHORT).show();

        }
    } );

    private void crearCanalNotificacion() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//Nombre del canal
            CharSequence name = "Notificación";
//Instancia para gestionar el canal y el servicio de la notificación
            NotificationChannel notificationChannel = new
                    NotificationChannel(CHANNEL_ID, name,
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

        }//if
    }//crearCanalNotificacion

    private void crearNotificacion(String cant, String nombre) {
//Instancia para generar la notificaciòn, especificando el contexto de la aplicación y el
//canal de comunicación
        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(getContext(),CHANNEL_ID);
//Características a incluir en la notificación
        builder.setSmallIcon(R.mipmap.logo);
        builder.setContentTitle("REPOSTERÍA ANGELES: Productos");
        builder.setContentText("Se agregaron "+cant+" productos de: "+nombre);
        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.RED, 1000,1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);
//Especifica la Activity que aparece al momento de elegir la notificación

//Instancia que gestiona la notificación con el dispositivo
        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(getContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }//crearNotificacion


}//fragment