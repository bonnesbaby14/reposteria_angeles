package com.example.reposteria_angeles.ui.reporte;

import android.app.DatePickerDialog;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.reposteria_angeles.MainActivity;
import com.example.reposteria_angeles.R;
import com.example.reposteria_angeles.databinding.FragmentReporteBinding;
import com.example.reposteria_angeles.ui.producto.ProductoViewModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ReporteFragment extends Fragment implements View.OnClickListener {

        private FragmentReporteBinding binding;
        //Componentes del Fragment
        TextView txtInicio, txtFinal;
        Button generar, reporteTotal;
    String puntero="";
    Spinner spinner;
    ArrayList<String> opciones;
        private int dia, mes, anio;

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {


            binding = FragmentReporteBinding.inflate(inflater, container, false);
            View root = binding.getRoot();
            spinner = root.findViewById(R.id.spBuscarCategoriaR);
            opciones = new ArrayList<String>();

            opciones.add("Selecciona");
            opciones.add("Productos");
            opciones.add("Ingresos");
            opciones.add("Gastos");
            opciones.add("Clientes");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, opciones);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String opcion = parent.getItemAtPosition(position).toString();
                    puntero=opcion;


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            ProductoViewModel productoViewModel =
                    new ViewModelProvider(this).get(ProductoViewModel.class);

            txtInicio = (TextView) root.findViewById(R.id.txtInicioEstadistica);
            txtInicio.setOnClickListener(this);
            txtFinal = (TextView) root.findViewById(R.id.txtFinalEstadistica);
            txtFinal.setOnClickListener(this);

            generar=root.findViewById(R.id.btnReporteReporte);
            reporteTotal = root.findViewById(R.id.btnIngresosReporte);
            generar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(puntero.equals("") || puntero.equals("Selecciona")){
                        Toast.makeText(getContext(),"Selecciona una entidad",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(txtFinal.getText().toString().isEmpty()|| txtInicio.getText().toString().isEmpty()){
                        Toast.makeText(getContext(),"Selecciona fechas",Toast.LENGTH_LONG).show();
                        return;
                    }





                    try {
                        generarTicket(txtInicio.getText().toString(),txtFinal.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

            reporteTotal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(txtFinal.getText().toString().isEmpty()|| txtInicio.getText().toString().isEmpty()){
                        Toast.makeText(getContext(),"Selecciona fechas",Toast.LENGTH_LONG).show();
                        return;
                    }
                    try {
                        generarReporteTotal(txtInicio.getText().toString(), txtFinal.getText().toString());
                    }catch (ParseException e){
                        e.printStackTrace();
                    }

                }
            });


            return root;
        }//onCreateView

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }

    @Override
    public void onClick(View view) {
        if(view == txtInicio){
            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            anio = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    txtInicio.setText(i2+"/"+(i1+1)+"/"+i);
                }
            },anio,mes,dia);
            datePickerDialog.show();
        }
        else if(view == txtFinal){
            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            anio = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    txtFinal.setText(i2+"/"+(i1+1)+"/"+i);
                }
            },anio,mes,dia);
            datePickerDialog.show();
        }

    }
    void generarTicket(String fecha1, String fecha2) throws ParseException {
        Log.d("FILES", "ENTRAMOS");

        Log.d("FILES", "fecha"+fecha1);
        Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(fecha1);

        Log.d("FILES", "fechaformato"+date1.toString());
        Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(fecha2);

        PdfDocument pdfDocument=new PdfDocument();
        Log.d("FILES", "ENTRAMOS2");
        Paint paint=new Paint();
        TextPaint titulo=new TextPaint();
        TextPaint descripcion = new TextPaint();
        Bitmap bitmap,bitmapEscala;

        PdfDocument.PageInfo pageInfo=new PdfDocument.PageInfo.Builder(750,700,1).create();
        PdfDocument.Page pagina= pdfDocument.startPage(pageInfo);
        Canvas canvas=pagina.getCanvas();
        bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.logo);
        bitmapEscala =Bitmap.createScaledBitmap(bitmap,80,80,false);
        canvas.drawBitmap(bitmapEscala, 50, 20, paint);
        titulo.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titulo.setTextSize(30);

        canvas.drawText("Reporte de "+puntero, 10, 150, titulo);
        titulo.setTextSize(20);
        canvas.drawText("Del "+fecha1+" al "+fecha2, 10, 180, titulo);

        descripcion.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        descripcion.setTextSize(15);

        int y = 300;
        boolean hayDatos = false;
        switch (puntero){
            case "Productos":
                canvas.drawText("NOMBRE    CANTIDAD    PRECIO    CADUCIDAD     DESCRIPCION", 5, y, descripcion);
                y+=20;

                try {
                    InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("productos.txt"));
                    BufferedReader br = new BufferedReader(archivo);
                    String linea = br.readLine();

                    while (linea != null) {

                            String[] aux = linea.split("-");
                        Date date=new SimpleDateFormat("dd/MM/yyyy").parse(aux[3]);

                        if(date.after(date1) || date.before(date2)){
                            hayDatos = true;
                           aux[0] = espacioCampos("nombre",aux[0]);
                           aux[1] = espacioCampos("cantidad",aux[1]);
                           aux[2] = espacioCampos("precio",aux[2]);
                           aux[3] = espacioCampos("caducidad",aux[3]);
                           aux[4] = espacioCampos("descripcion",aux[4]);


                            canvas.drawText(aux[0]+"              "+aux[1]+"          "+aux[2]+"          "+aux[3]+
                                    "        "+aux[4], 5, y, descripcion);
                            y+=20;
                        }

                        linea = br.readLine();
                    }

                    br.close();
                    archivo.close();


                } catch (IOException e) {

                }

                break;
            case "Ingresos":
                canvas.drawText("CLIENTE    PRODUCTOS    VENTA TOTAL    NOMBRE VENTA     PRODUCTOS VENDIDOS    DESCRIPCION", 5, y, descripcion);
                y+=20;
                try {
                    InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("ingresos.txt"));
                    BufferedReader br = new BufferedReader(archivo);
                    String linea = br.readLine();

                    while (linea != null) {

                        String[] aux = linea.split("-");
                        Date date=new SimpleDateFormat("dd/MM/yyyy").parse(aux[6]);
                        if(date.after(date1) || date.before(date2)){
                            hayDatos = true;
                            aux[0] = espacioCampos("CLIENTE",aux[0]);
                            aux[1] = espacioCampos("PRODUCTOS",aux[1]);
                            aux[2] = espacioCampos("VENTA TOTAL",aux[2]);
                            aux[3] = espacioCampos("NOMBRE VENTA",aux[3]);
                            aux[4] = espacioCampos("PRODUCTOS VENDIDOS",aux[4]);
                            aux[5] = espacioCampos("DESCRIPCION",aux[5]);

                            canvas.drawText(aux[0]+"           "+aux[1]+"               "+aux[2]+"            "+aux[3]+
                                    "           "+aux[4]+"               "+aux[5], 5, y, descripcion);
                            y+=20;
                        }

                        linea = br.readLine();
                    }

                    br.close();
                    archivo.close();


                } catch (IOException e) {

                }
                break;
            case "Gastos":
                canvas.drawText("PRODUCTOS          NOMBRE          COSTO        PRODUCTOS NUEVOS         DESCRIPCION", 5, y, descripcion);
                y+=20;

                try {
                    InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("gastos.txt"));
                    BufferedReader br = new BufferedReader(archivo);
                    String linea = br.readLine();

                    while (linea != null) {

                        String[] aux = linea.split("-");
                        Date date=new SimpleDateFormat("dd/MM/yyyy").parse(aux[5]);
                        if(date.after(date1) || date.before(date2)){
                            hayDatos = true;
                            aux[0] = espacioCampos("PRODUCTOS",aux[0]);
                            aux[1] = espacioCampos("NOMBRE",aux[1]);
                            aux[2] = espacioCampos("COSTO",aux[2]);
                            aux[3] = espacioCampos("PRODUCTOS NUEVOS",aux[3]);
                            aux[4] = espacioCampos("DESCRIPCION",aux[4]);


                            canvas.drawText(aux[0]+"                "+aux[1]+"             "+aux[2]+"                    "+aux[3]+
                                    "                        "+aux[4], 5, y, descripcion);
                            y+=20;
                        }

                        linea = br.readLine();
                    }

                    br.close();
                    archivo.close();


                } catch (IOException e) {

                }

                break;
            case "Clientes":
                canvas.drawText("NOMBRE               DIRECCIÓN               TELÉFONO               PREFERENCIAS", 5, y, descripcion);
                y+=20;

                try {
                    InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("clientes.txt"));
                    BufferedReader br = new BufferedReader(archivo);
                    String linea = br.readLine();

                    while (linea != null) {

                        String[] aux = linea.split("-");
                        Date date=new SimpleDateFormat("dd/MM/yyyy").parse(aux[4]);
                        if(date.after(date1) || date.before(date2)){
                            hayDatos = true;
                            aux[0] = espacioCampos("NOMBRE",aux[0]);
                            aux[1] = espacioCampos("DIRECCIÓN",aux[1]);
                            aux[2] = espacioCampos("TELÉFONO",aux[2]);
                            aux[3] = espacioCampos("PREFERENCIAS",aux[3]);


                            canvas.drawText(aux[0]+"                   "+aux[1]+"                "+aux[2]+"                       "+aux[3], 5, y, descripcion);
                            y+=20;
                        }

                        linea = br.readLine();
                    }

                    br.close();
                    archivo.close();


                } catch (IOException e) {

                }
                break;
        }


        pdfDocument.finishPage(pagina);

        try {
            if(!hayDatos){
                Toast.makeText(getContext(), "No hay registros en estas fechas", Toast.LENGTH_LONG).show();

            }else {
                Log.d("FILES", "try");
                ContextWrapper contextWrapper = new ContextWrapper(getContext());
                File directory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                int num = 1;
                String nameDocument = "Reporte_" + puntero + "_" + num + ".pdf";
                File file = new File(directory, nameDocument);
                file = nombreArchivo(file, nameDocument, directory);


                pdfDocument.writeTo(new FileOutputStream(file));

                Toast.makeText(getContext(), "Se creo el PDF correctamente en " + directory.toString(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.d("FILES", "Error escritura de archivo");

            Log.d("FILES", e.toString());
            e.printStackTrace();
        }

        pdfDocument.close();





    }//generarTicket
    private void generarReporteTotal(String fecha1, String fecha2) throws ParseException{
        Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(fecha1);
        Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(fecha2);

        PdfDocument pdfDocument=new PdfDocument();
        Paint paint=new Paint();
        TextPaint titulo=new TextPaint();
        TextPaint descripcion = new TextPaint();
        Bitmap bitmap,bitmapEscala;

        PdfDocument.PageInfo pageInfo=new PdfDocument.PageInfo.Builder(750,700,3).create();
        PdfDocument.Page pagina= pdfDocument.startPage(pageInfo);
        Canvas canvas=pagina.getCanvas();
        bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.logo);
        bitmapEscala =Bitmap.createScaledBitmap(bitmap,80,80,false);
        canvas.drawBitmap(bitmapEscala, 50, 20, paint);
        titulo.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titulo.setTextSize(30);

        canvas.drawText("Reporte Total", 10, 150, titulo);
        titulo.setTextSize(20);
        canvas.drawText("Del "+fecha1+" al "+fecha2, 10, 180, titulo);
        descripcion.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        descripcion.setTextSize(15);

        int y = 210;
        boolean hayDatos = false;
        //Productos
        //titulo
        titulo.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titulo.setTextSize(20);
        canvas.drawText("Productos", 5, y, titulo);
        y+=20;
        canvas.drawText("NOMBRE    CANTIDAD    PRECIO    CADUCIDAD     DESCRIPCION", 5, y, descripcion);
        y+=20;

        try {
            InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("productos.txt"));
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();

            while (linea != null) {

                String[] aux = linea.split("-");
                Date date=new SimpleDateFormat("dd/MM/yyyy").parse(aux[3]);
                if(date.after(date1) || date.before(date2)){
                    hayDatos = true;
                    aux[0] = espacioCampos("nombre",aux[0]);
                    aux[1] = espacioCampos("cantidad",aux[1]);
                    aux[2] = espacioCampos("precio",aux[2]);
                    aux[3] = espacioCampos("caducidad",aux[3]);
                    aux[4] = espacioCampos("descripcion",aux[4]);


                    canvas.drawText(aux[0]+"              "+aux[1]+"          "+aux[2]+"          "+aux[3]+
                            "        "+aux[4], 5, y, descripcion);
                    y+=20;
                }

                linea = br.readLine();
            }

            br.close();
            archivo.close();


        } catch (IOException e) {

        }
        //Ingresos
        //titulo
        titulo.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titulo.setTextSize(20);
        canvas.drawText("Ingresos", 5, y, titulo);
        y+=20;
        canvas.drawText("CLIENTE    PRODUCTOS    VENTA TOTAL    NOMBRE VENTA     PRODUCTOS VENDIDOS    DESCRIPCION", 5, y, descripcion);
        y+=20;
        try {
            InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("ingresos.txt"));
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();

            while (linea != null) {

                String[] aux = linea.split("-");
                Date date=new SimpleDateFormat("dd/MM/yyyy").parse(aux[6]);
                if(date.after(date1) || date.before(date2)){
                    hayDatos = true;
                    aux[0] = espacioCampos("CLIENTE",aux[0]);
                    aux[1] = espacioCampos("PRODUCTOS",aux[1]);
                    aux[2] = espacioCampos("VENTA TOTAL",aux[2]);
                    aux[3] = espacioCampos("NOMBRE VENTA",aux[3]);
                    aux[4] = espacioCampos("PRODUCTOS VENDIDOS",aux[4]);
                    aux[5] = espacioCampos("DESCRIPCION",aux[5]);

                    canvas.drawText(aux[0]+"           "+aux[1]+"               "+aux[2]+"            "+aux[3]+
                            "           "+aux[4]+"               "+aux[5], 5, y, descripcion);
                    y+=20;
                }

                linea = br.readLine();
            }

            br.close();
            archivo.close();


        } catch (IOException e) {

        }
        //gastos
        //titulo
        titulo.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titulo.setTextSize(20);
        canvas.drawText("Gastos", 5, y, titulo);
        y+=20;
        canvas.drawText("PRODUCTOS          NOMBRE          COSTO        PRODUCTOS NUEVOS         DESCRIPCION", 5, y, descripcion);
        y+=20;

        try {
            InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("gastos.txt"));
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();

            while (linea != null) {

                String[] aux = linea.split("-");
                Date date=new SimpleDateFormat("dd/MM/yyyy").parse(aux[5]);
                if(date.after(date1) || date.before(date2)){
                    hayDatos = true;
                    aux[0] = espacioCampos("PRODUCTOS",aux[0]);
                    aux[1] = espacioCampos("NOMBRE",aux[1]);
                    aux[2] = espacioCampos("COSTO",aux[2]);
                    aux[3] = espacioCampos("PRODUCTOS NUEVOS",aux[3]);
                    aux[4] = espacioCampos("DESCRIPCION",aux[4]);


                    canvas.drawText(aux[0]+"                "+aux[1]+"             "+aux[2]+"                    "+aux[3]+
                            "                        "+aux[4], 5, y, descripcion);
                    y+=20;
                }

                linea = br.readLine();
            }

            br.close();
            archivo.close();


        } catch (IOException e) {

        }
        //clientes
        //titulo
        titulo.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titulo.setTextSize(20);
        canvas.drawText("Clientes", 5, y, titulo);
        y+=20;
        canvas.drawText("NOMBRE               DIRECCIÓN               TELÉFONO               PREFERENCIAS", 5, y, descripcion);
        y+=20;

        try {
            InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("clientes.txt"));
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();

            while (linea != null) {

                String[] aux = linea.split("-");
                Date date=new SimpleDateFormat("dd/MM/yyyy").parse(aux[4]);
                if(date.after(date1) || date.before(date2)){
                    hayDatos = true;
                    aux[0] = espacioCampos("NOMBRE",aux[0]);
                    aux[1] = espacioCampos("DIRECCIÓN",aux[1]);
                    aux[2] = espacioCampos("TELÉFONO",aux[2]);
                    aux[3] = espacioCampos("PREFERENCIAS",aux[3]);


                    canvas.drawText(aux[0]+"                   "+aux[1]+"                "+aux[2]+"                       "+aux[3], 5, y, descripcion);
                    y+=20;
                }

                linea = br.readLine();
            }

            br.close();
            archivo.close();


        } catch (IOException e) {

        }
        pdfDocument.finishPage(pagina);

        try {
            if(!hayDatos){
                Toast.makeText(getContext(), "No hay registros en estas fechas", Toast.LENGTH_LONG).show();

            }else {
                Log.d("FILES", "try");
                ContextWrapper contextWrapper = new ContextWrapper(getContext());
                File directory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                int num = 1;
                puntero = "Total";
                String nameDocument = "Reporte_" + puntero + "_" + num + ".pdf";
                File file = new File(directory, nameDocument);
                file = nombreArchivo(file, nameDocument, directory);


                pdfDocument.writeTo(new FileOutputStream(file));

                Toast.makeText(getContext(), "Se creo el PDF correctamente en " + directory.toString(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.d("FILES", "Error escritura de archivo");

            Log.d("FILES", e.toString());
            e.printStackTrace();
        }

        pdfDocument.close();

    }//generarReporteTotal
    private String espacioCampos(String campo, String dato){
        if(dato.length()<campo.length()){
                for(int i=0;i<campo.length()-dato.length();i++){
                    dato +="  ";
                }
                return dato;
            }
            else
                return dato;
    }//espacioCampos
    //Para nombrar archivos y evitar sobreescribir
    private File nombreArchivo(File file,String nameDocument,File directory){
        if(!file.exists())
            return file;

        String[] name = nameDocument.split("_");
        String auxiliar = name[2];
        String[] pos = auxiliar.split("\\.");
        String position = pos[0];
        int number = Integer.parseInt(position);
        number++;
        nameDocument = "Reporte_"+puntero+"_"+number+".pdf";
        file = new File(directory, nameDocument);

        return nombreArchivo(file,nameDocument,directory);

    }//nombreArchivo
}

