package com.example.reposteria_angeles;

public class Ingreso {

    private String cliente, producto, ventatotal, nombre, productosvendidos, descripcion;

    public Ingreso(String cliente, String producto, String ventatotal, String nombre, String productosvendidos, String descripcion) {
        this.cliente = cliente;
        this.producto = producto;
        this.ventatotal = ventatotal;
        this.nombre = nombre;
        this.productosvendidos = productosvendidos;
        this.descripcion = descripcion;
    }

    public Ingreso() {
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getVentatotal() {
        return ventatotal;
    }

    public void setVentatotal(String ventatotal) {
        this.ventatotal = ventatotal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProductosvendidos() {
        return productosvendidos;
    }

    public void setProductosvendidos(String productosvendidos) {
        this.productosvendidos = productosvendidos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
