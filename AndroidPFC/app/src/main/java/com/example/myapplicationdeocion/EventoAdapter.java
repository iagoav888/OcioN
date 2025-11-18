package com.example.myapplicationdeocion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.ViewHolder> {

    private List<Evento> listaEventos;

    public EventoAdapter(List<Evento> listaEventos) {
        this.listaEventos = listaEventos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_evento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Evento evento = listaEventos.get(position);

        // Mostrar título
        holder.tvTitulo.setText(evento.getTitulo());

        // Mostrar descripción
        if (evento.getDescripcion() != null && !evento.getDescripcion().isEmpty()) {
            holder.tvDescripcion.setText(evento.getDescripcion());
            holder.tvDescripcion.setVisibility(View.VISIBLE);
        } else {
            holder.tvDescripcion.setVisibility(View.GONE);
        }

        // Mostrar hora
        holder.tvHora.setText("🕐 " + evento.getHora());

        // Mostrar fecha en el calendario
        mostrarFechaEnCalendario(holder, evento.getFecha());
    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    /**
     * Extrae día y mes de la fecha y los muestra en el calendario visual
     */
    private void mostrarFechaEnCalendario(ViewHolder holder, String fecha) {
        try {
            if (fecha != null && fecha.contains(" ")) {
                String[] partes = fecha.split(" ");
                String fechaSola = partes[0]; // "2025-12-25"
                String[] fechaPartes = fechaSola.split("-");

                String dia = fechaPartes[2];  // "25"
                String mes = obtenerMesAbreviado(Integer.parseInt(fechaPartes[1])); // "DIC"

                holder.tvDia.setText(dia);
                holder.tvMes.setText(mes);
            }
        } catch (Exception e) {
            holder.tvDia.setText("--");
            holder.tvMes.setText("---");
        }
    }

    /**
     * Convierte número de mes a abreviatura
     */
    private String obtenerMesAbreviado(int mes) {
        String[] meses = {
                "ENE", "FEB", "MAR", "ABR", "MAY", "JUN",
                "JUL", "AGO", "SEP", "OCT", "NOV", "DIC"
        };

        if (mes >= 1 && mes <= 12) {
            return meses[mes - 1];
        }
        return "---";
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDia, tvMes, tvTitulo, tvDescripcion, tvHora;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDia = itemView.findViewById(R.id.tvDiaEvento);
            tvMes = itemView.findViewById(R.id.tvMesEvento);
            tvTitulo = itemView.findViewById(R.id.tvTituloEvento);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionEvento);
            tvHora = itemView.findViewById(R.id.tvHoraEvento);
        }
    }
}