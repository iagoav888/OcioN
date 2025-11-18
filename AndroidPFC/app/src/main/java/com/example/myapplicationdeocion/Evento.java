package com.example.myapplicationdeocion;

public class Evento {
    private int id;
    private String titulo;
    private String descripcion;
    private String fecha;

    // Constructor completo
    public Evento(int id, String titulo, String descripcion, String fecha) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * Formatea la fecha del servidor a formato legible
     * Ejemplo: "2025-12-25 23:00:00" → "25/12/2025"
     */
    public String getFechaFormateada() {
        try {
            if (fecha != null && fecha.contains(" ")) {
                String[] partes = fecha.split(" ");
                String fechaSola = partes[0]; // "2025-12-25"
                String[] fechaPartes = fechaSola.split("-");

                // Convertir a formato legible: "25/12/2025"
                return fechaPartes[2] + "/" + fechaPartes[1] + "/" + fechaPartes[0];
            }
            return fecha;
        } catch (Exception e) {
            return fecha;
        }
    }

    /**
     * Obtiene la hora del evento
     * Ejemplo: "2025-12-25 23:00:00" → "23:00"
     */
    public String getHora() {
        try {
            if (fecha != null && fecha.contains(" ")) {
                String[] partes = fecha.split(" ");
                String hora = partes[1]; // "23:00:00"
                String[] horaPartes = hora.split(":");

                // Devolver solo hora y minutos: "23:00"
                return horaPartes[0] + ":" + horaPartes[1];
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }
}