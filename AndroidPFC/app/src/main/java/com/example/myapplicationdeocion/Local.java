package com.example.myapplicationdeocion;

public class Local {
    private String nombre;
    private String direccion;
    private String tipo;
    private String imagenResId;


    public Local(String nombre, String direccion, String tipo, String imagenResId){
        this.nombre = nombre;
        this.direccion = direccion;
        this.tipo = tipo;
        this.imagenResId = imagenResId;

    }

    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTipo() { return tipo; }
    public String getImagenResId() { return imagenResId; }
}
