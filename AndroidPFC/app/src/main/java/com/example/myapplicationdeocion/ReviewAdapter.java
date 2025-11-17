package com.example.myapplicationdeocion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review> listaReviews;

    public ReviewAdapter(List<Review> listaReviews) {
        this.listaReviews = listaReviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = listaReviews.get(position);

        // Mostrar nombre de usuario
        holder.tvUsername.setText(review.getUsername());

        // Mostrar contenido de la reseña
        holder.tvContenido.setText(review.getContenido());

        // Mostrar fecha (por ahora tal cual, se puede mejorar)
        holder.tvFecha.setText(formatearFecha(review.getFecha()));

        // Mostrar estrellas según puntuación
        mostrarEstrellas(holder, review.getPuntuacion());
    }

    @Override
    public int getItemCount() {
        return listaReviews.size();
    }

    /**
     * Muestra las estrellas según la puntuación (1-5)
     */
    private void mostrarEstrellas(ViewHolder holder, int puntuacion) {
        // Array con las 5 estrellas
        ImageView[] estrellas = {
                holder.estrella1,
                holder.estrella2,
                holder.estrella3,
                holder.estrella4,
                holder.estrella5
        };

        // Pintar estrellas llenas o vacías según puntuación
        for (int i = 0; i < estrellas.length; i++) {
            if (i < puntuacion) {
                // Estrella llena (dorada)
                estrellas[i].setImageResource(android.R.drawable.star_big_on);
            } else {
                // Estrella vacía (gris)
                estrellas[i].setImageResource(android.R.drawable.star_big_off);
            }
        }
    }

    /**
     * Formatea la fecha del servidor a algo más legible
     * Ejemplo: "2025-11-16 18:30:00" → "16/11/2025"
     */
    private String formatearFecha(String fechaServidor) {
        try {
            // Si la fecha viene en formato "2025-11-16 18:30:00"
            if (fechaServidor != null && fechaServidor.contains(" ")) {
                String[] partes = fechaServidor.split(" ");
                String fecha = partes[0]; // "2025-11-16"
                String[] fechaPartes = fecha.split("-");

                // Convertir a formato más legible: "16/11/2025"
                return fechaPartes[2] + "/" + fechaPartes[1] + "/" + fechaPartes[0];
            }

            return fechaServidor;

        } catch (Exception e) {
            return fechaServidor;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvFecha, tvContenido;
        ImageView estrella1, estrella2, estrella3, estrella4, estrella5;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsernameReview);
            tvFecha = itemView.findViewById(R.id.tvFechaReview);
            tvContenido = itemView.findViewById(R.id.tvContenidoReview);

            estrella1 = itemView.findViewById(R.id.estrella1);
            estrella2 = itemView.findViewById(R.id.estrella2);
            estrella3 = itemView.findViewById(R.id.estrella3);
            estrella4 = itemView.findViewById(R.id.estrella4);
            estrella5 = itemView.findViewById(R.id.estrella5);
        }
    }
}