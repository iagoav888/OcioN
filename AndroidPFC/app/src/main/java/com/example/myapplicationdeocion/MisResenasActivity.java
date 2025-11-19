package com.example.myapplicationdeocion;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MisResenasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReviewAdapter adapter;
    private List<Review> listaReviews;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_resenas);

        // Configurar ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mis Reseñas");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Inicializar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewMisResenas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaReviews = new ArrayList<>();
        adapter = new ReviewAdapter(listaReviews);
        recyclerView.setAdapter(adapter);

        // Inicializar Volley
        queue = Volley.newRequestQueue(this);

        // Cargar reseñas del usuario
        cargarMisResenas();
    }

    private void cargarMisResenas() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "No hay sesión activa", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String url = "http://10.0.2.2:8000/reviews/user/?token=" + token;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    listaReviews.clear();

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            Review review = new Review(
                                    obj.getInt("id"),
                                    obj.getString("local_nombre"),  // Usamos local_nombre
                                    obj.getString("contenido"),
                                    obj.getInt("puntuacion"),
                                    obj.getString("fecha")
                            );

                            listaReviews.add(review);
                        }

                        adapter.notifyDataSetChanged();

                        if (listaReviews.isEmpty()) {
                            Toast.makeText(this, "No tienes reseñas todavía", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(this, "Error al procesar datos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error al cargar reseñas", Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}