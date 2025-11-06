package com.example.myapplicationdeocion;

public class Local {
    private int id;
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private String tipo;
    private String imagenUrl;
    private String imagenLocal;
    private String playlistUrl;

    // Constructor para cuando los datos vienen del servidor
    public Local(int id, String nombre, String descripcion, String ubicacion, String tipo, String imagenUrl, String playlistUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.tipo = tipo;
        this.imagenUrl = imagenUrl;
        this.imagenLocal = null;
        this.playlistUrl = playlistUrl;
    }

    // Constructor para datos hardcodeados (modo local)
    public Local(String nombre, String descripcion, String tipo, String imagenLocal) {
        this.id = -1;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacion = descripcion;
        this.tipo = tipo;
        this.imagenUrl = null;
        this.imagenLocal = imagenLocal;
        this.playlistUrl = null;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getUbicacion() { return ubicacion; }
    public String getTipo() { return tipo; }
    public String getImagenUrl() { return imagenUrl; }
    public String getImagenLocal() { return imagenLocal; }
    public String getPlaylistUrl() { return playlistUrl; }

    // Para saber si tiene imagen del servidor
    public boolean tieneImagenServidor() {
        return imagenUrl != null && !imagenUrl.isEmpty();
    }

    // Para saber si tiene imagen local
    public boolean tieneImagenLocal() {
        return imagenLocal != null && !imagenLocal.isEmpty();
    }
}