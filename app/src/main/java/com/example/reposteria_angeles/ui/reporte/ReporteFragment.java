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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.reposteria_angeles.R;
import com.example.reposteria_angeles.databinding.FragmentReporteBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ReporteFragment extends Fragment implements View.OnClickListener {

        private FragmentReporteBinding binding;
        //Componentes del Fragment
        TextView txtInicio, txtFinal;
        Button generar;
        private int dia, mes, anio;

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {


            binding = FragmentReporteBinding.inflate(inflater, container, false);
            View root = binding.getRoot();


            txtInicio = (TextView) root.findViewById(R.id.txtInicioEstadistica);
            txtInicio.setOnClickListener(this);
            txtFinal = (TextView) root.findViewById(R.id.txtFinalEstadistica);
            txtFinal.setOnClickListener(this);

            generar=root.findViewById(R.id.btnReporteReporte);

            generar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        generarTicket("10/03/1999","10/03/1999");
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
        //<SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-aaaa");
        //Date date1 = new Date(format1.parse(fecha1).getTime());
        //Date date2 = new Date(format1.parse(fecha2).getTime());
        PdfDocument pdfDocument=new PdfDocument();
        Log.d("FILES", "ENTRAMOS2");
        Paint paint=new Paint();
        TextPaint titulo=new TextPaint();
        TextPaint descripcion = new TextPaint();
        Bitmap bitmap,bitmapEscala;

        PdfDocument.PageInfo pageInfo=new PdfDocument.PageInfo.Builder(400,600,1).create();
        PdfDocument.Page pagina= pdfDocument.startPage(pageInfo);
        Canvas canvas=pagina.getCanvas();
        bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.logo);
        bitmapEscala =Bitmap.createScaledBitmap(bitmap,80,80,false);
        canvas.drawBitmap(bitmapEscala, 50, 20, paint);
        titulo.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titulo.setTextSize(30);
        canvas.drawText("Reporte de ", 10, 150, titulo);

        descripcion.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        descripcion.setTextSize(20);
        pdfDocument.finishPage(pagina);

        try {
            Log.d("FILES", "try");
            ContextWrapper contextWrapper=new ContextWrapper(getContext());
            File directory=contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(directory, "Reporte.pdf");

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

