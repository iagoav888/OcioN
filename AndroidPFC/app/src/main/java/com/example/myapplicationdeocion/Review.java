package com.example.myapplicationdeocion;

public class Review {
    private int id;
    private String username;
    private String contenido;
    private int puntuacion;
    private String fecha;

    // Constructor completo (cuando vienen del servidor)
    public Review(int id, String username, String contenido, int puntuacion, String fecha) {
        this.id = id;
        this.username = username;
        this.contenido = contenido;
        this.puntuacion = puntuacion;
        this.fecha = fecha;
    }

    // Constructor sin ID (para crear nuevas reseñas)
    public Review(String username, String contenido, int puntuacion, String fecha) {
        this.id = -1;
        this.username = username;
        this.contenido = contenido;
        this.puntuacion = puntuacion;
        this.fecha = fecha;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getContenido() {
        return contenido;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public String getFecha() {
        return fecha;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * Convierte la fecha del servidor a formato legible
     * Ejemplo: "2025-11-16 18:30:00" → "hace 2 días"
     */
    public String getFechaRelativa() {
        
        return fecha;
    }
}