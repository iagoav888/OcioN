package com.example.myapplicationdeocion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private LocalAdapter adapter;
    private List<Local> listaOriginal;
    private List<Local> listaFiltrada;
    private EditText etBuscar;
    private RequestQueue queue;

    // Variables para el Navigation Drawer
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ============ CONFIGURAR DRAWER ============
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Mostrar username en el header del menú
        View headerView = navigationView.getHeaderView(0);
        TextView tvHeaderUsername = headerView.findViewById(R.id.tvHeaderUsername);
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", "Usuario");
        tvHeaderUsername.setText(username);


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

    private void cargarLocalesDesdeServidor() {
        String url = "http://10.0.2.2:8000/locales/";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        listaOriginal.clear();

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
                    Log.e(TAG, "Error al cargar locales del servidor: " + error.toString());
                    Toast.makeText(this, "Usando datos locales (servidor no disponible)", Toast.LENGTH_SHORT).show();
                    cargarLocalesLocales();
                }
        );

        queue.add(request);
    }

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

    private void actualizarLista() {
        listaFiltrada.clear();
        listaFiltrada.addAll(listaOriginal);
        adapter.notifyDataSetChanged();
    }

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

    // ============ MÉTODOS DEL NAVIGATION DRAWER ============
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            Toast.makeText(this, "Ya estás en Inicio", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_mis_resenas) {
            Intent intent = new Intent(MainActivity.this, MisResenasActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_cerrar_sesion) {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}