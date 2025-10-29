package com.example.myapplicationdeocion;

public class Local {
    private String nombre;
    private String direccion;
    private String tipo;
    private String imagenResId;
    private String musicaUrl; // Futuro: URL de música del backend

    public Local(String nombre, String direccion, String tipo, String imagenResId) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.tipo = tipo;
        this.imagenResId = imagenResId;
        this.musicaUrl = null; // Inicialmente vacío (para futuro uso con Django)
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTipo() { return tipo; }
    public String getImagenResId() { return imagenResId; }
    public String getMusicaUrl() { return musicaUrl; }

    // Setter por si en el futuro Django devuelve una URL de música
    public void setMusicaUrl(String musicaUrl) { this.musicaUrl = musicaUrl; }
}
