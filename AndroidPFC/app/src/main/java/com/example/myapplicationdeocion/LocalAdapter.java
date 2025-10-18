package com.example.myapplicationdeocion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.ViewHolder> {

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
        holder.tvDireccion.setText(local.getDireccion());
        holder.tvTipo.setText(local.getTipo());

        String imageName = local.getImagenResId().replace(".jpg", "").replace(".jpeg", "");

        int resId = holder.itemView.getContext().getResources().getIdentifier(
                imageName, "drawable", holder.itemView.getContext().getPackageName()
        );

        Glide.with(holder.itemView.getContext())
                .load(resId)
                .into(holder.ivLocal);
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
