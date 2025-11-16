package com.example.myapplicationdeocion;

import android.content.Intent;
import android.net.Uri;
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
    private String playlistUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        // Inicializar vistas
        tvNombreDetalle = findViewById(R.id.tvNombreDetalle);
        tvUbicacionDetalle = findViewById(R.id.tvDireccionDetalle);
        tvTipoDetalle = findViewById(R.id.tvTipoDetalle);
        tvDescripcionDetalle = findViewById(R.id.tvDescripcionDetalle);
        ivLocalDetalle = findViewById(R.id.ivLocalDetalle);
        btnPlayPause = findViewById(R.id.btnPlayPause);

        // Obtener datos del Intent
        int id = getIntent().getIntExtra("id", -1);
        String nombre = getIntent().getStringExtra("nombre");
        String descripcion = getIntent().getStringExtra("descripcion");
        String ubicacion = getIntent().getStringExtra("ubicacion");
        String tipo = getIntent().getStringExtra("tipo");
        String imagenUrl = getIntent().getStringExtra("imagenUrl");
        String imagenLocal = getIntent().getStringExtra("imagenLocal");
        playlistUrl = getIntent().getStringExtra("playlistUrl");

        // Mostrar información del local
        tvNombreDetalle.setText(nombre);
        tvUbicacionDetalle.setText(ubicacion);
        tvTipoDetalle.setText(tipo);

        if (descripcion != null && !descripcion.isEmpty()) {
            tvDescripcionDetalle.setText(descripcion);
        } else {
            tvDescripcionDetalle.setText("Próximamente: descripción completa del local, precios y eventos.");
        }

        cargarImagen(imagenUrl, imagenLocal);
        configurarBotonMusica();
    }

    /**
     * Carga la imagen del local desde el servidor o desde recursos locales
     */
    private void cargarImagen(String imagenUrl, String imagenLocal) {
        if (imagenUrl != null && !imagenUrl.isEmpty()) {
            // Cargar imagen desde servidor con Glide
            Log.d(TAG, "Cargando imagen desde servidor: " + imagenUrl);
            Glide.with(this)
                    .load(imagenUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_image)
                    .into(ivLocalDetalle);

        } else if (imagenLocal != null && !imagenLocal.isEmpty()) {
            // Cargar imagen desde drawable
            String imageName = imagenLocal.replace(".jpg", "").replace(".jpeg", "");
            int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());

            Log.d(TAG, "Cargando imagen local: " + imageName);

            if (resId != 0) {
                Glide.with(this).load(resId).into(ivLocalDetalle);
            } else {
                ivLocalDetalle.setImageResource(R.drawable.placeholder);
            }
        } else {
            ivLocalDetalle.setImageResource(R.drawable.placeholder);
        }
    }

    /**
     * Configura el botón de música para abrir Spotify
     */
    private void configurarBotonMusica() {
        btnPlayPause.setOnClickListener(v -> {
            if (playlistUrl != null && !playlistUrl.isEmpty()) {
                abrirSpotify(playlistUrl);
            } else {
                Toast.makeText(this, "No hay playlist disponible para este local", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Abre la URL de Spotify (playlist o canción)
     * Primero intenta abrir la app de Spotify, si no está instalada abre el navegador
     */
    private void abrirSpotify(String url) {
        try {
            // Convertir URL web de Spotify a URI de Spotify
            String spotifyUri = convertirUrlAUri(url);

            if (spotifyUri != null) {
                // Intentar abrir con la app de Spotify
                Intent spotifyIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(spotifyUri));
                spotifyIntent.setPackage("com.spotify.music");
                startActivity(spotifyIntent);
                Log.d(TAG, "Abriendo Spotify con URI: " + spotifyUri);

            } else {
                // Si no se puede convertir, abrir URL en navegador
                abrirEnNavegador(url);
            }

        } catch (Exception e) {
            // Si Spotify no está instalado, abrir en navegador
            Log.e(TAG, "Spotify no instalado, abriendo en navegador: " + e.getMessage());
            abrirEnNavegador(url);
        }
    }

    /**
     * Convierte URL web de Spotify a URI de Spotify
     * Ejemplo: https://open.spotify.com/playlist/37i9dQZF1DXcBWIGoYBM5M
     *       -> spotify:playlist:37i9dQZF1DXcBWIGoYBM5M
     */
    private String convertirUrlAUri(String url) {
        try {
            if (url.contains("open.spotify.com/playlist/")) {
                String playlistId = url.split("playlist/")[1].split("\\?")[0];
                return "spotify:playlist:" + playlistId;

            } else if (url.contains("open.spotify.com/track/")) {
                String trackId = url.split("track/")[1].split("\\?")[0];
                return "spotify:track:" + trackId;

            } else if (url.contains("open.spotify.com/album/")) {
                String albumId = url.split("album/")[1].split("\\?")[0];
                return "spotify:album:" + albumId;
            }

            return null;

        } catch (Exception e) {
            Log.e(TAG, "Error al convertir URL: " + e.getMessage());
            return null;
        }
    }

    /**
     * Abre la URL de Spotify en el navegador
     */
    private void abrirEnNavegador(String url) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
            Log.d(TAG, "Abriendo en navegador: " + url);
            Toast.makeText(this, "Abriendo playlist en navegador", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e(TAG, "Error al abrir navegador: " + e.getMessage());
            Toast.makeText(this, "No se puede abrir la URL", Toast.LENGTH_SHORT).show();
        }
    }
}