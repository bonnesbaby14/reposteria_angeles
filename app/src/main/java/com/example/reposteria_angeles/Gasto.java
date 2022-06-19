package com.example.reposteria_angeles;

public class Gasto {
    private String producto, nombre, costo, nuevos, descripcion;

    public Gasto() {
    }

    public Gasto(String producto, String nombre, String costo, String nuevos, String descripcion) {
        this.producto = producto;
        this.nombre = nombre;
        this.costo = costo;
        this.nuevos = nuevos;
        this.descripcion = descripcion;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getNuevos() {
        return nuevos;
    }

    public void setNuevos(String nuevos) {
        this.nuevos = nuevos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
