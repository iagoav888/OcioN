package com.example.myapplicationdeocion;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.ViewHolder> {

    private static final String TAG = "LocalAdapter";
    private List<Local> lista;

    public LocalAdapter(List<Local> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_local, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Local local = lista.get(position);

        holder.tvNombre.setText(local.getNombre());
        holder.tvDireccion.setText(local.getUbicacion());
        holder.tvTipo.setText(local.getTipo());

        cargarImagen(holder, local);

        // Al hacer click, abrir la pantalla de detalles
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetalleActivity.class);
            intent.putExtra("id", local.getId());
            intent.putExtra("nombre", local.getNombre());
            intent.putExtra("descripcion", local.getDescripcion());
            intent.putExtra("ubicacion", local.getUbicacion());
            intent.putExtra("tipo", local.getTipo());
            intent.putExtra("imagenUrl", local.getImagenUrl());
            intent.putExtra("imagenLocal", local.getImagenLocal());
            intent.putExtra("playlistUrl", local.getPlaylistUrl());
            v.getContext().startActivity(intent);
        });
    }

    // Cargar imagen desde servidor o desde drawable
    private void cargarImagen(ViewHolder holder, Local local) {
        if (local.tieneImagenServidor()) {
            // Cargar desde URL
            Log.d(TAG, "Cargando imagen desde servidor: " + local.getImagenUrl());
            Glide.with(holder.itemView.getContext())
                    .load(local.getImagenUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_image)
                    .into(holder.ivLocal);

        } else if (local.tieneImagenLocal()) {
            // Cargar desde drawable
            String imageName = local.getImagenLocal().replace(".jpg", "").replace(".jpeg", "");
            int resId = holder.itemView.getContext().getResources().getIdentifier(
                    imageName, "drawable", holder.itemView.getContext().getPackageName()
            );

            Log.d(TAG, "Cargando imagen desde drawable: " + imageName);

            if (resId != 0) {
                Glide.with(holder.itemView.getContext())
                        .load(resId)
                        .into(holder.ivLocal);
            } else {
                holder.ivLocal.setImageResource(R.drawable.placeholder);
            }
        } else {
            holder.ivLocal.setImageResource(R.drawable.placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLocal;
        TextView tvNombre, tvDireccion, tvTipo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivLocal = itemView.findViewById(R.id.ivLocal);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvTipo = itemView.findViewById(R.id.tvTipo);
        }
    }
}