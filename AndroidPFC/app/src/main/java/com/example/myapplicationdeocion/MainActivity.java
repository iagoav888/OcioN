package com.example.myapplicationdeocion;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private LocalAdapter adapter;
    private List<Local> listaOriginal;
    private List<Local> listaFiltrada;
    private EditText etBuscar;
    private RequestQueue queue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rvLocales);
        etBuscar = findViewById(R.id.etBuscar);

        listaOriginal = new ArrayList<>();
        listaFiltrada = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LocalAdapter(listaFiltrada);
        recyclerView.setAdapter(adapter);

        queue = Volley.newRequestQueue(this);

        configurarBuscador();
        cargarLocalesDesdeServidor();
    }

    // Intenta cargar locales desde Django, si falla usa datos locales
    private void cargarLocalesDesdeServidor() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", "");

        String url = "http://10.0.2.2:8000/locales/";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        listaOriginal.clear();

                        // Parsear el JSON que viene del servidor
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            Local local = new Local(
                                    obj.getInt("id"),
                                    obj.getString("nombre"),
                                    obj.optString("descripcion", ""),
                                    obj.getString("ubicacion"),
                                    obj.optString("tipo", "Local"),
                                    obj.optString("imagen_url", ""),
                                    obj.optString("playlist_url", "")
                            );

                            listaOriginal.add(local);
                        }

                        Log.d(TAG, "Locales cargados desde servidor: " + listaOriginal.size());
                        Toast.makeText(this, "Locales cargados desde servidor", Toast.LENGTH_SHORT).show();

                        actualizarLista();

                    } catch (JSONException e) {
                        Log.e(TAG, "Error al parsear JSON: " + e.getMessage());
                        cargarLocalesLocales();
                    }
                },
                error -> {
                    // Si no hay conexión con el servidor, usar datos hardcodeados
                    Log.e(TAG, "Error al cargar locales del servidor: " + error.toString());
                    Toast.makeText(this, "Usando datos locales (servidor no disponible)", Toast.LENGTH_SHORT).show();
                    cargarLocalesLocales();
                }
        );

        queue.add(request);
    }

    // Datos de respaldo si el servidor no está disponible
    private void cargarLocalesLocales() {
        listaOriginal.clear();

        listaOriginal.add(new Local("Pelícano", "Discoteca de gran capacidad", "Discoteca", "pelicano.jpeg"));
        listaOriginal.add(new Local("Brétema", "Pub con ambiente tranquilo y buena música", "Pub", "bretema.jpg"));
        listaOriginal.add(new Local("Nouf", "Local moderno con copas y DJ", "Pub", "nouf.jpg"));
        listaOriginal.add(new Local("Chaston", "Discoteca elegante con eventos temáticos", "Discoteca", "chaston.jpg"));
        listaOriginal.add(new Local("Twin Fin", "Bar con cócteles y ambiente surfero", "Bar", "twinfin.jpg"));

        Log.d(TAG, "Locales cargados desde drawable: " + listaOriginal.size());

        actualizarLista();
    }

    // Copiar todos los locales a la lista que se muestra
    private void actualizarLista() {
        listaFiltrada.clear();
        listaFiltrada.addAll(listaOriginal);
        adapter.notifyDataSetChanged();
    }

    // Configurar el buscador para que filtre mientras escribes
    private void configurarBuscador() {
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarLocales(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Buscar locales que contengan el texto (sin importar mayúsculas)
    private void filtrarLocales(String texto) {
        listaFiltrada.clear();

        if (texto.isEmpty()) {
            listaFiltrada.addAll(listaOriginal);
        } else {
            for (Local local : listaOriginal) {
                if (local.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                    listaFiltrada.add(local);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }
}