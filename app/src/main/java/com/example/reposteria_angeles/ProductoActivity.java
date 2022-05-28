package com.example.reposteria_angeles;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;

public class ProductoActivity extends AppCompatActivity {
    Spinner producto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        producto = findViewById(R.id.spVenta);
        producto.setPrompt("Select your favorite Planet!");
    }
}