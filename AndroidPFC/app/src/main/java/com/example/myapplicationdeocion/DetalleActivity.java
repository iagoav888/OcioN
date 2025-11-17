package com.example.myapplicationdeocion;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetalleActivity extends AppCompatActivity {

    private static final String TAG = "DetalleActivity";
    private TextView tvNombreDetalle, tvUbicacionDetalle, tvTipoDetalle, tvDescripcionDetalle;
    private TextView tvContadorReviews, tvNoReviews;
    private ImageView ivLocalDetalle;
    private ImageButton btnPlayPause;
    private Button btnEscribirResena;
    private RecyclerView rvReviews;
    private String playlistUrl;
    private int localId;
    private RequestQueue queue;
    private List<Review> listaReviews;
    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        // Inicializar RequestQueue
        queue = Volley.newRequestQueue(this);
        listaReviews = new ArrayList<>();

        // Inicializar vistas básicas
        tvNombreDetalle = findViewById(R.id.tvNombreDetalle);
        tvUbicacionDetalle = findViewById(R.id.tvDireccionDetalle);
        tvTipoDetalle = findViewById(R.id.tvTipoDetalle);
        tvDescripcionDetalle = findViewById(R.id.tvDescripcionDetalle);
        ivLocalDetalle = findViewById(R.id.ivLocalDetalle);
        btnPlayPause = findViewById(R.id.btnPlayPause);

        // Inicializar vistas de reseñas
        tvContadorReviews = findViewById(R.id.tvContadorReviews);
        tvNoReviews = findViewById(R.id.tvNoReviews);
        btnEscribirResena = findViewById(R.id.btnEscribirResena);
        rvReviews = findViewById(R.id.rvReviews);

        // Configurar RecyclerView
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(listaReviews);
        rvReviews.setAdapter(reviewAdapter);

        // Obtener datos del Intent
        localId = getIntent().getIntExtra("id", -1);
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
        configurarBotonEscribirResena();

        // Cargar reseñas del servidor
        if (localId != -1) {
            cargarReviews(localId);
        }
    }

    /**
     * Carga la imagen del local desde el servidor o desde recursos locales
     */
    private void cargarImagen(String imagenUrl, String imagenLocal) {
        if (imagenUrl != null && !imagenUrl.isEmpty()) {
            Log.d(TAG, "Cargando imagen desde servidor: " + imagenUrl);
            Glide.with(this)
                    .load(imagenUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_image)
                    .into(ivLocalDetalle);

        } else if (imagenLocal != null && !imagenLocal.isEmpty()) {
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
     * Abre la URL de Spotify
     */
    private void abrirSpotify(String url) {
        try {
            String spotifyUri = convertirUrlAUri(url);

            if (spotifyUri != null) {
                Intent spotifyIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(spotifyUri));
                spotifyIntent.setPackage("com.spotify.music");
                startActivity(spotifyIntent);
                Log.d(TAG, "Abriendo Spotify con URI: " + spotifyUri);
            } else {
                abrirEnNavegador(url);
            }

        } catch (Exception e) {
            Log.e(TAG, "Spotify no instalado, abriendo en navegador: " + e.getMessage());
            abrirEnNavegador(url);
        }
    }

    /**
     * Convierte URL web de Spotify a URI de Spotify
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

    /**
     * Carga las reseñas del servidor
     */
    private void cargarReviews(int localId) {
        String url = "http://10.0.2.2:8000/locales/" + localId + "/reviews/";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        listaReviews.clear();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            Review review = new Review(
                                    obj.getInt("id"),
                                    obj.getString("username"),
                                    obj.getString("contenido"),
                                    obj.getInt("puntuacion"),
                                    obj.getString("fecha")
                            );

                            listaReviews.add(review);
                        }

                        actualizarUIReviews();
                        Log.d(TAG, "Reseñas cargadas: " + listaReviews.size());

                    } catch (JSONException e) {
                        Log.e(TAG, "Error al parsear reseñas: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e(TAG, "Error al cargar reseñas: " + error.toString());
                    actualizarUIReviews();
                }
        );

        queue.add(request);
    }

    /**
     * Actualiza la UI según el número de reseñas
     */
    private void actualizarUIReviews() {
        int numReviews = listaReviews.size();
        tvContadorReviews.setText("(" + numReviews + ")");

        if (numReviews == 0) {
            rvReviews.setVisibility(View.GONE);
            tvNoReviews.setVisibility(View.VISIBLE);
        } else {
            rvReviews.setVisibility(View.VISIBLE);
            tvNoReviews.setVisibility(View.GONE);
            reviewAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Configura el botón para escribir una reseña
     */
    private void configurarBotonEscribirResena() {
        btnEscribirResena.setOnClickListener(v -> mostrarDialogEscribirResena());
    }

    /**
     * Muestra el dialog para escribir una nueva reseña
     */
    private void mostrarDialogEscribirResena() {
        // Inflar el layout del dialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_escribir_resena, null);

        EditText etContenido = dialogView.findViewById(R.id.etContenidoResena);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBarResena);

        // Crear el dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("✍️ Escribe tu reseña");
        builder.setView(dialogView);

        builder.setPositiveButton("Publicar", null);
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        // Configurar el botón Publicar
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            String contenido = etContenido.getText().toString().trim();
            int puntuacion = (int) ratingBar.getRating();

            if (contenido.isEmpty()) {
                Toast.makeText(this, "Escribe un comentario", Toast.LENGTH_SHORT).show();
                return;
            }

            if (puntuacion == 0) {
                Toast.makeText(this, "Selecciona una puntuación", Toast.LENGTH_SHORT).show();
                return;
            }

            enviarResena(contenido, puntuacion);
            dialog.dismiss();
        });
    }

    /**
     * Envía la reseña al servidor
     */
    private void enviarResena(String contenido, int puntuacion) {
        // Obtener token del usuario
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:8000/locales/" + localId + "/reviews/";

        try {
            JSONObject body = new JSONObject();
            body.put("token", token);
            body.put("contenido", contenido);
            body.put("puntuacion", puntuacion);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    body,
                    response -> {
                        Toast.makeText(this, "¡Reseña publicada!", Toast.LENGTH_SHORT).show();
                        cargarReviews(localId); // Recargar reseñas
                    },
                    error -> {
                        Log.e(TAG, "Error al publicar reseña: " + error.toString());
                        Toast.makeText(this, "Error al publicar reseña", Toast.LENGTH_SHORT).show();
                    }
            );

            queue.add(request);

        } catch (JSONException e) {
            Log.e(TAG, "Error al crear JSON: " + e.getMessage());
        }
    }
}