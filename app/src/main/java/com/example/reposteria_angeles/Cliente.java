package com.example.reposteria_angeles;

public class Cliente {
    private String nombre;
    private String direccion;
    private String telefono;
    private String preferencias;

    public Cliente(String nombre, String direccion, String telefono, String preferencias) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.preferencias = preferencias;
    }

    public Cliente() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(String preferencias) {
        this.preferencias = preferencias;
    }

}
