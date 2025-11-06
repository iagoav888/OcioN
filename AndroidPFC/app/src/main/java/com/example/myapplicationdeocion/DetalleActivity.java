package com.example.myapplicationdeocion;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class DetalleActivity extends AppCompatActivity {

    private static final String TAG = "DetalleActivity";
    private TextView tvNombreDetalle, tvUbicacionDetalle, tvTipoDetalle, tvDescripcionDetalle;
    private ImageView ivLocalDetalle;
    private ImageButton btnPlayPause;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        tvNombreDetalle = findViewById(R.id.tvNombreDetalle);
        tvUbicacionDetalle = findViewById(R.id.tvDireccionDetalle);
        tvTipoDetalle = findViewById(R.id.tvTipoDetalle);
        tvDescripcionDetalle = findViewById(R.id.tvDescripcionDetalle);
        ivLocalDetalle = findViewById(R.id.ivLocalDetalle);
        btnPlayPause = findViewById(R.id.btnPlayPause);

        // Recibir los datos del local desde MainActivity
        int id = getIntent().getIntExtra("id", -1);
        String nombre = getIntent().getStringExtra("nombre");
        String descripcion = getIntent().getStringExtra("descripcion");
        String ubicacion = getIntent().getStringExtra("ubicacion");
        String tipo = getIntent().getStringExtra("tipo");
        String imagenUrl = getIntent().getStringExtra("imagenUrl");
        String imagenLocal = getIntent().getStringExtra("imagenLocal");
        String playlistUrl = getIntent().getStringExtra("playlistUrl");

        // Mostrar los datos en pantalla
        tvNombreDetalle.setText(nombre);
        tvUbicacionDetalle.setText(ubicacion);
        tvTipoDetalle.setText(tipo);

        if (descripcion != null && !descripcion.isEmpty()) {
            tvDescripcionDetalle.setText(descripcion);
        } else {
            tvDescripcionDetalle.setText("Próximamente: descripción completa del local, precios y eventos.");
        }

        cargarImagen(imagenUrl, imagenLocal);
        inicializarReproductor(playlistUrl);
    }

    // Cargar imagen desde servidor o desde drawable
    private void cargarImagen(String imagenUrl, String imagenLocal) {
        if (imagenUrl != null && !imagenUrl.isEmpty()) {
            // Cargar desde el servidor
            Log.d(TAG, "Cargando imagen desde servidor: " + imagenUrl);
            Glide.with(this)
                    .load(imagenUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_image)
                    .into(ivLocalDetalle);

        } else if (imagenLocal != null && !imagenLocal.isEmpty()) {
            // Cargar desde drawable
            String imageName = imagenLocal.replace(".jpg", "").replace(".jpeg", "");
            int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());

            Log.d(TAG, "Cargando imagen desde drawable: " + imageName);

            if (resId != 0) {
                Glide.with(this).load(resId).into(ivLocalDetalle);
            } else {
                ivLocalDetalle.setImageResource(R.drawable.placeholder);
            }
        } else {
            ivLocalDetalle.setImageResource(R.drawable.placeholder);
        }
    }

    // Configurar el reproductor de música (por ahora un audio de prueba)
    private void inicializarReproductor(String playlistUrl) {
        // TODO: En el futuro usar playlistUrl del servidor

        mediaPlayer = MediaPlayer.create(this, R.raw.disco_inferno_edit);

        if (mediaPlayer == null) {
            btnPlayPause.setEnabled(false);
            Toast.makeText(this, "Audio no disponible", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error al crear MediaPlayer");
            return;
        }

        // Botón para reproducir/pausar
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

    // Liberar el MediaPlayer cuando se cierre la activity
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
            Log.d(TAG, "MediaPlayer liberado");
        }
    }
}