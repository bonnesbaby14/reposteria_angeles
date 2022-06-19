package com.example.reposteria_angeles;

public class Producto {
    private String nombre, cantidad, precio, caducidad, descripcion;

    public Producto(String nombre, String cantidad, String precio, String caducidad, String descripcion) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.caducidad = caducidad;
        this.descripcion = descripcion;
    }

    public Producto() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCaducidad() {
        return caducidad;
    }

    public void setCaducidad(String caducidad) {
        this.caducidad = caducidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


}
