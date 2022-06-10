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
        Button generar;
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

            opciones.add("Seleciona");
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

            generar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(puntero.equals("") || puntero.equals("Seleciona")){
                        Toast.makeText(getContext(),"Seleciona una entidad",Toast.LENGTH_LONG).show();
                        return;
                    }





                    try {
                        generarTicket(txtInicio.getText().toString(),txtFinal.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });


            return root;
        }

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

        PdfDocument.PageInfo pageInfo=new PdfDocument.PageInfo.Builder(500,600,1).create();
        PdfDocument.Page pagina= pdfDocument.startPage(pageInfo);
        Canvas canvas=pagina.getCanvas();
        bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.logo);
        bitmapEscala =Bitmap.createScaledBitmap(bitmap,80,80,false);
        canvas.drawBitmap(bitmapEscala, 50, 20, paint);
        titulo.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titulo.setTextSize(30);

        canvas.drawText("Reporte de "+puntero, 10, 150, titulo);

        descripcion.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        descripcion.setTextSize(15);

        int y = 300;
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
                        if(date.after(date1) && date.before(date2)){

                            canvas.drawText(aux[0]+"       "+aux[1]+"       "+aux[2]+"       "+aux[3]+"      "+aux[4], 5, y, descripcion);
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
                break;
            case "Gastos":
                break;
            case "Clientes":
                break;
        }


        pdfDocument.finishPage(pagina);

        try {
            Log.d("FILES", "try");
            ContextWrapper contextWrapper=new ContextWrapper(getContext());
            File directory=contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(directory, "Reporte_"+puntero  +".pdf");

            pdfDocument.writeTo(new FileOutputStream(file));

            Toast.makeText(getContext(), "Se creo el PDF correctamente en "+directory.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.d("FILES", "Error escritura de archivo");

            Log.d("FILES", e.toString());
            e.printStackTrace();
        }

        pdfDocument.close();





    }
}

