package com.example.reposteria_angeles.ui.estadistica;

import android.app.DatePickerDialog;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.reposteria_angeles.R;
import com.example.reposteria_angeles.databinding.FragmentEstadisticaBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class EstadisticaFragment extends Fragment implements View.OnClickListener {

        private FragmentEstadisticaBinding binding;
        //Componentes del Fragment
        private TextView txtInicio, txtFinal;
        private int dia, mes, anio;
        private ArrayList barArrayList;
        private Button filtrar, ticket;
        private Spinner categorias;
        private ArrayList<String> opciones;
        private String puntero = "";
        BarChart barChart, charTicket;
        boolean presionado = false;
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {


            binding = FragmentEstadisticaBinding.inflate(inflater, container, false);
            View root = binding.getRoot();

            filtrar = (Button) root.findViewById(R.id.btnIngresosReporte);
            ticket = (Button) root.findViewById(R.id.btnTicketEstadistica);
            txtInicio = (TextView) root.findViewById(R.id.txtInicioEstadistica);
            txtInicio.setOnClickListener(this);
            txtFinal = (TextView) root.findViewById(R.id.txtFinalEstadistica);
            txtFinal.setOnClickListener(this);
            //Spinner
            categorias = (Spinner) root.findViewById(R.id.spBuscarCategoria);
            opciones = new ArrayList<String>();
            opciones.add("Selecciona");
            opciones.add("Productos");
            opciones.add("Ingresos");
            opciones.add("Gastos");
            opciones.add("Clientes");
            ArrayAdapter<String> opcionesAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,opciones);
            opcionesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorias.setAdapter(opcionesAdapter);
            categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String item = parent.getItemAtPosition(position).toString();
                    puntero = item;
                }//onItemSelected

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });//Categoria setOnItemSelectedListener
            //Grafica
            barChart = root.findViewById(R.id.barChart);
            getData();
            BarDataSet barDataSet = new BarDataSet(barArrayList, "Estadísticas");
            BarData barData = new BarData (barDataSet);
            barChart.setData(barData);
            barChart.getLegend().setEnabled(false);
            barChart.getDescription().setText("Estadísticas");
            barChart.getDescription().setTextSize(20);
            barDataSet.setColors(ColorTemplate.PASTEL_COLORS);
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(8f);

            //button
            filtrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(puntero.equals("Selecciona")){
                        Toast.makeText(getContext(), "Selecciona una categoría", Toast.LENGTH_SHORT).show();
                        presionado = false;
                    }else if(txtInicio.getText().toString().isEmpty()||txtFinal.getText().toString().isEmpty()) {
                        presionado = false;
                        Toast.makeText(getContext(), "Selecciona fechas", Toast.LENGTH_SHORT).show();
                    }else{
                        try {
                            generarEstadistica(txtInicio.getText().toString(), txtFinal.getText().toString());
                            presionado = true;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });//filtrar
            //button ticket
            ticket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!presionado){
                        Toast.makeText(getContext(), "Genera primero el gráfico", Toast.LENGTH_SHORT).show();
                    }else{
                        PdfDocument pdfDocument=new PdfDocument();
                        Paint paint=new Paint();
                        TextPaint titulo=new TextPaint();
                        TextPaint descripcion = new TextPaint();
                        Bitmap bitmap,bitmapEscala;
                        PdfDocument.PageInfo pageInfo=new PdfDocument.PageInfo.Builder(750,700,1).create();
                        PdfDocument.Page pagina= pdfDocument.startPage(pageInfo);
                        Canvas canvas=pagina.getCanvas();
                        bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.logo);
                        bitmapEscala =Bitmap.createScaledBitmap(bitmap,90,90,false);
                        canvas.drawBitmap(bitmapEscala, 650, 610, paint);
                        titulo.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        titulo.setTextSize(30);
                        charTicket.draw(canvas);
                        canvas.drawText("Estadística "+puntero, 10, 420, titulo);
                        titulo.setTextSize(20);
                        canvas.drawText("Del "+txtInicio.getText().toString()+" al "+txtFinal.getText().toString(), 10, 450, titulo);
                        descripcion.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        descripcion.setTextSize(15);

                        pdfDocument.finishPage(pagina);

                        try {
                                Log.d("FILES", "try");
                                ContextWrapper contextWrapper = new ContextWrapper(getContext());
                                File directory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                                int num = 1;
                                String nameDocument = "Estadistica_" + puntero + "_" + num + ".pdf";
                                File file = new File(directory, nameDocument);
                                file = nombreArchivo(file, nameDocument, directory);


                                pdfDocument.writeTo(new FileOutputStream(file));

                                Toast.makeText(getContext(), "Se creo el PDF correctamente en " + directory.toString(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.d("FILES", "Error escritura de archivo");

                            Log.d("FILES", e.toString());
                            e.printStackTrace();
                        }

                        pdfDocument.close();
                    }
                }
            });
            //barChart.getDescription().setEnabled(true);

            return root;
        }//onCreateView

        private void getData(){
            barArrayList = new ArrayList();
            barArrayList.add(new BarEntry(2f, 3));
            barArrayList.add(new BarEntry(4f, 2));
            barArrayList.add(new BarEntry(6f, 12));
            barArrayList.add(new BarEntry(8f, 5));
            barArrayList.add(new BarEntry(10f, 10));
        }//getData
    private BarData getBarData(int[] datosY){
        BarDataSet barDataSet=(BarDataSet)getDataSame(new BarDataSet(getBarEntries(datosY),""));
        BarData barData=new BarData(barDataSet);
        barData.setBarWidth(0.20f);
        return barData;
    }//getBarData
    private DataSet getDataSame(DataSet dataSet){
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10);
        return dataSet;
    }//getDataSame

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }//onDestroyView
    private Chart getSameChart(Chart chart, String description, int textColor, int background, int animateY,String[] datosX){
            chart.getDescription().setText(description);
            chart.getDescription().setTextSize(15);
            chart.setBackgroundColor(background);
            chart.animateY(animateY);
            legend(chart,datosX);
            return chart;
    }//getSameChart
    private void legend(Chart chart,String[] datosX){
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        ArrayList<LegendEntry> entries = new ArrayList<>();
        for(int i=0; i< datosX.length;i++){
            LegendEntry entry = new LegendEntry();
            entry.label=datosX[i];
            entries.add(entry);
        }
        legend.setCustom(entries);
    }//legend
    private ArrayList<BarEntry> getBarEntries(int[] datosY){
            ArrayList<BarEntry> entries = new ArrayList<>();
            for(int i=0; i<datosY.length;i++){
                entries.add(new BarEntry(i,datosY[i]));
            }
            return entries;
    }//getBarEntries
    private void axisX(XAxis axis,String[] datosX){
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(datosX));
    }//axisX
    private void axisLeft(YAxis axis){
        axis.setSpaceTop(50);
        axis.setAxisMinimum(0);
       // axis.setEnabled(false);
    }//axisY
    private void axisRight(YAxis axis){
        axis.setEnabled(false);
    }//axisY


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
    }//onClick
    private File nombreArchivo(File file,String nameDocument,File directory){
        if(!file.exists())
            return file;

        String[] name = nameDocument.split("_");
        String auxiliar = name[2];
        String[] pos = auxiliar.split("\\.");
        String position = pos[0];
        int number = Integer.parseInt(position);
        number++;
        nameDocument = "Estadistica_"+puntero+"_"+number+".pdf";
        file = new File(directory, nameDocument);

        return nombreArchivo(file,nameDocument,directory);

    }//nombreArchivo
    private void generarEstadistica(String fecha1, String fecha2) throws ParseException {
        Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(fecha1);
        Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(fecha2);
        switch (puntero){
            case "Productos":
                String fechaux1="";
                String fechaux2="";
                ArrayList<String> fecha = new ArrayList<String>();
                ArrayList<Integer> cantProduct = new ArrayList<Integer>();

                try {
                    InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("productos.txt"));
                    BufferedReader br = new BufferedReader(archivo);
                    String linea = br.readLine();

                    int pos = 0;
                    while (linea != null) {

                        String[] aux = linea.split("-");
                        Date date=new SimpleDateFormat("dd/MM/yyyy").parse(aux[3]);

                        if(date.after(date1)  || date.before(date2)){
                            fechaux1 = aux[3];
                            if(fechaux1.equals(fechaux2)){
                                cantProduct.set(pos-1,cantProduct.get(pos-1)+Integer.parseInt(aux[1]));
                            }else{
                                fecha.add(aux[3]);
                                cantProduct.add(Integer.parseInt(aux[1]));
                                pos++;
                            }
                            fechaux2 = aux[3];

                        }

                        linea = br.readLine();
                    }
                    br.close();
                    archivo.close();
                } catch (IOException e) {

                }
                //Pasamos los arraylist a arreglos
                String [] arrayFechas = fecha.toArray(new String[fecha.size()]);
                int[] arrayCant = new int[cantProduct.size()];
                for(int i=0;i<arrayCant.length;i++){
                    arrayCant[i] = (int) cantProduct.get(i);
                }
                barChart=(BarChart)getSameChart(barChart,"Productos a caducar",Color.WHITE,Color.WHITE,3000,arrayFechas);
                barChart.setDrawGridBackground(true);
                barChart.setDrawBarShadow(true);
                barChart.setData(getBarData(arrayCant));
                barChart.invalidate();
                barChart.getLegend().setEnabled(false);
                axisX(barChart.getXAxis(),arrayFechas);
                axisLeft(barChart.getAxisLeft());
                axisRight(barChart.getAxisRight());
                charTicket = barChart;


                break;
            case "Ingresos":
                 fechaux1="";
                 fechaux2="";
                 fecha = null;
                 fecha = new ArrayList<String>();
                ArrayList<Integer> venta = new ArrayList<Integer>();

                try {
                    InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("ingresos.txt"));
                    BufferedReader br = new BufferedReader(archivo);
                    String linea = br.readLine();

                    int pos = 0;
                    while (linea != null) {

                        String[] aux = linea.split("-");
                        Date date=new SimpleDateFormat("dd/MM/yyyy").parse(aux[6]);

                        if(date.after(date1) || date.before(date2)){
                            fechaux1 = aux[6];
                            if(fechaux1.equals(fechaux2)){
                                int valor = venta.get(pos-1) + Integer.parseInt(aux[2]);
                                venta.set(pos-1,valor);
                            }else{
                                fecha.add(aux[6]);
                                venta.add(pos,Integer.parseInt(aux[2]));
                                pos++;
                            }
                            fechaux2 = aux[6];
                        }
                        linea = br.readLine();
                    }
                    br.close();
                    archivo.close();
                } catch (IOException e) {

                }
                //Pasamos los arraylist a arreglos
                arrayFechas = fecha.toArray(new String[fecha.size()]);
                int[] arrayVenta = new int[venta.size()];
                for(int i=0;i<arrayVenta.length;i++){
                    arrayVenta[i] = (int) venta.get(i);
                }
                barChart=(BarChart)getSameChart(barChart,"$ Ingresos",Color.WHITE,Color.WHITE,3000,arrayFechas);
                barChart.setDrawGridBackground(true);
                barChart.setDrawBarShadow(true);
                barChart.setData(getBarData(arrayVenta));
                barChart.invalidate();
                barChart.getLegend().setEnabled(false);
                axisX(barChart.getXAxis(),arrayFechas);
                axisLeft(barChart.getAxisLeft());
                axisRight(barChart.getAxisRight());
                charTicket = barChart;

                break;
            case "Gastos":
                fechaux1="";
                fechaux2="";
                fecha = null;
                fecha = new ArrayList<String>();
                ArrayList<Integer> gasto = new ArrayList<Integer>();

                try {
                    InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("gastos.txt"));
                    BufferedReader br = new BufferedReader(archivo);
                    String linea = br.readLine();

                    int pos = 0;
                    while (linea != null) {

                        String[] aux = linea.split("-");
                        Date date=new SimpleDateFormat("dd/MM/yyyy").parse(aux[5]);

                        if(date.after(date1) || date.before(date2)){
                            fechaux1 = aux[5];
                            if(fechaux1.equals(fechaux2)){
                                int valor = gasto.get(pos-1) + Integer.parseInt(aux[2]);
                                gasto.set(pos-1,valor);
                            }else{
                                fecha.add(aux[5]);
                                gasto.add(pos,Integer.parseInt(aux[2]));
                                pos++;
                            }
                            fechaux2 = aux[5];
                        }
                        linea = br.readLine();
                    }
                    br.close();
                    archivo.close();
                } catch (IOException e) {

                }
                //Pasamos los arraylist a arreglos
                arrayFechas = fecha.toArray(new String[fecha.size()]);
                int[] arrayGasto = new int[gasto.size()];
                for(int i=0;i<arrayGasto.length;i++){
                    arrayGasto[i] = (int) gasto.get(i);
                }
                barChart=(BarChart)getSameChart(barChart,"$ Gastos",Color.WHITE,Color.WHITE,3000,arrayFechas);
                barChart.setDrawGridBackground(true);
                barChart.setDrawBarShadow(true);
                barChart.setData(getBarData(arrayGasto));
                barChart.invalidate();
                barChart.getLegend().setEnabled(false);
                axisX(barChart.getXAxis(),arrayFechas);
                axisLeft(barChart.getAxisLeft());
                axisRight(barChart.getAxisRight());
                charTicket = barChart;

                break;
            case "Clientes":
                fechaux1="";
                fechaux2="";
                fecha = null;
                fecha = new ArrayList<String>();
                ArrayList<Integer> nuevoCliente = new ArrayList<Integer>();

                try {
                    InputStreamReader archivo = new InputStreamReader(getContext().openFileInput("clientes.txt"));
                    BufferedReader br = new BufferedReader(archivo);
                    String linea = br.readLine();

                    int pos = 0;
                    while (linea != null) {

                        String[] aux = linea.split("-");
                        Date date=new SimpleDateFormat("dd/MM/yyyy").parse(aux[4]);

                        if(date.after(date1) || date.before(date2)){
                            fechaux1 = aux[4];
                            if(fechaux1.equals(fechaux2)){
                                int valor = nuevoCliente.get(pos-1) + 1;
                                nuevoCliente.set(pos-1,valor);
                            }else{
                                fecha.add(aux[4]);
                                nuevoCliente.add(1);
                                pos++;
                            }
                            fechaux2 = aux[4];
                        }
                        linea = br.readLine();
                    }
                    br.close();
                    archivo.close();
                } catch (IOException e) {

                }
                //Pasamos los arraylist a arreglos
                arrayFechas = fecha.toArray(new String[fecha.size()]);
                int[] arrayCliente = new int[nuevoCliente.size()];
                for(int i=0;i<arrayCliente.length;i++){
                    arrayCliente[i] = (int) nuevoCliente.get(i);
                }
                barChart=(BarChart)getSameChart(barChart,"Nuevos Clientes",Color.WHITE,Color.WHITE,3000,arrayFechas);
                barChart.setDrawGridBackground(true);
                barChart.setDrawBarShadow(true);
                barChart.setData(getBarData(arrayCliente));
                barChart.invalidate();
                barChart.getLegend().setEnabled(false);
                axisX(barChart.getXAxis(),arrayFechas);
                axisLeft(barChart.getAxisLeft());
                axisRight(barChart.getAxisRight());
                charTicket = barChart;

                break;
        }
    }//generarEstadistica
}//class

