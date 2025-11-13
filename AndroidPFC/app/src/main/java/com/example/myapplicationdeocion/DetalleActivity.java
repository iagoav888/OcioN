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

import java.io.IOException;

public class DetalleActivity extends AppCompatActivity {

    private static final String TAG = "DetalleActivity";
    private TextView tvNombreDetalle, tvUbicacionDetalle, tvTipoDetalle, tvDescripcionDetalle;
    private ImageView ivLocalDetalle;
    private ImageButton btnPlayPause;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private String playlistUrl;

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
        playlistUrl = getIntent().getStringExtra("playlistUrl");

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
        inicializarReproductor();
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

    // Configurar el reproductor de música desde URL o archivo local
    private void inicializarReproductor() {
        mediaPlayer = new MediaPlayer();

        // Intentar cargar desde URL del servidor
        if (playlistUrl != null && !playlistUrl.isEmpty()) {
            Log.d(TAG, "Intentando cargar música desde URL: " + playlistUrl);
            cargarMusicaDesdeUrl(playlistUrl);
        } else {
            // Si no hay URL, usar audio local de respaldo
            Log.d(TAG, "No hay playlist_url, usando audio local de respaldo");
            cargarMusicaLocal();
        }

        configurarBotonPlayPause();
    }

    // Cargar música desde una URL del servidor
    private void cargarMusicaDesdeUrl(String url) {
        try {
            mediaPlayer.setDataSource(url);

            // Preparar el audio de forma asíncrona
            mediaPlayer.prepareAsync();

            // Listener para cuando termine de prepararse
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d(TAG, "Audio desde URL preparado correctamente");
                    btnPlayPause.setEnabled(true);
                    Toast.makeText(DetalleActivity.this, "Música lista para reproducir", Toast.LENGTH_SHORT).show();
                }
            });

            // Listener por si hay error al cargar
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e(TAG, "Error al cargar música desde URL. What: " + what + ", Extra: " + extra);
                    Toast.makeText(DetalleActivity.this, "Error al cargar música, usando audio local", Toast.LENGTH_SHORT).show();

                    // Si falla, intentar con audio local
                    mediaPlayer.reset();
                    cargarMusicaLocal();
                    return true;
                }
            });

        } catch (IOException e) {
            Log.e(TAG, "IOException al configurar URL: " + e.getMessage());
            Toast.makeText(this, "Error al cargar música desde servidor", Toast.LENGTH_SHORT).show();
            cargarMusicaLocal();
        }
    }

    // Cargar música desde archivo local (respaldo)
    private void cargarMusicaLocal() {
        try {
            mediaPlayer.reset();
            mediaPlayer = MediaPlayer.create(this, R.raw.disco_inferno_edit);

            if (mediaPlayer != null) {
                btnPlayPause.setEnabled(true);
                Log.d(TAG, "Audio local cargado correctamente");
            } else {
                btnPlayPause.setEnabled(false);
                Toast.makeText(this, "Audio no disponible", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error al crear MediaPlayer con audio local");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al cargar música local: " + e.getMessage());
            btnPlayPause.setEnabled(false);
        }
    }

    // Configurar el botón de play/pause
    private void configurarBotonPlayPause() {
        btnPlayPause.setOnClickListener(v -> {
            if (mediaPlayer == null) {
                Toast.makeText(this, "Reproductor no disponible", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isPlaying) {
                // Pausar
                mediaPlayer.pause();
                isPlaying = false;
                btnPlayPause.setImageResource(R.drawable.ic_music_note_white);
                Toast.makeText(this, "Música pausada", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Música pausada");
            } else {
                // Reproducir
                mediaPlayer.start();
                isPlaying = true;
                btnPlayPause.setImageResource(R.drawable.ic_pause_white);
                Toast.makeText(this, "Reproduciendo música del local", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Reproduciendo música");
            }
        });

        // Listener para cuando termine la canción
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                btnPlayPause.setImageResource(R.drawable.ic_music_note_white);
                Toast.makeText(DetalleActivity.this, "Reproducción finalizada", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Reproducción completada");
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

    // Pausar música si la app pasa a segundo plano
    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
            btnPlayPause.setImageResource(R.drawable.ic_music_note_white);
            Log.d(TAG, "Música pausada (onPause)");
        }
    }
}