package com.example.myapplicationdeocion;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetalleActivity extends AppCompatActivity {

    private TextView tvNombreDetalle, tvDireccionDetalle, tvTipoDetalle, tvDescripcionDetalle;
    private ImageView ivLocalDetalle;
    private ImageButton btnPlayPause;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        tvNombreDetalle = findViewById(R.id.tvNombreDetalle);
        tvDireccionDetalle = findViewById(R.id.tvDireccionDetalle);
        tvTipoDetalle = findViewById(R.id.tvTipoDetalle);
        tvDescripcionDetalle = findViewById(R.id.tvDescripcionDetalle);
        ivLocalDetalle = findViewById(R.id.ivLocalDetalle);
        btnPlayPause = findViewById(R.id.btnPlayPause);

        // Datos recibidos del intent
        String nombre = getIntent().getStringExtra("nombre");
        String direccion = getIntent().getStringExtra("direccion");
        String tipo = getIntent().getStringExtra("tipo");
        String imagen = getIntent().getStringExtra("imagen");

        tvNombreDetalle.setText(nombre);
        tvDireccionDetalle.setText(direccion);
        tvTipoDetalle.setText(tipo);
        tvDescripcionDetalle.setText("Próximamente: descripción completa del local, precios y eventos.");

        // Carga de imagen con Glide o placeholder
        if (imagen != null && !imagen.isEmpty()) {
            String imageName = imagen.replace(".jpg", "").replace(".jpeg", "");
            int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            Glide.with(this).load(resId).into(ivLocalDetalle);
        } else {
            ivLocalDetalle.setImageResource(R.drawable.placeholder);
        }

        // Reproductor de música (temporal)
        mediaPlayer = MediaPlayer.create(this, R.raw.disco_inferno_edit);


        // Evento del botón
        btnPlayPause.setOnClickListener(v -> {
            if (isPlaying) {
                mediaPlayer.pause();
                isPlaying = false;
                btnPlayPause.setImageResource(R.drawable.ic_music_note_white);
                Toast.makeText(this, "Música pausada", Toast.LENGTH_SHORT).show();
            } else {
                mediaPlayer.start();
                isPlaying = true;
                btnPlayPause.setImageResource(R.drawable.ic_pause_white);
                Toast.makeText(this, "Reproduciendo música del local", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
