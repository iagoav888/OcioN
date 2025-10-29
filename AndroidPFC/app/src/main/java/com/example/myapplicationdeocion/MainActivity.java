package com.example.myapplicationdeocion;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LocalAdapter adapter;
    private List<Local> listaOriginal;
    private List<Local> listaFiltrada;
    private EditText etBuscar;

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

        cargarLocales();
        configurarBuscador();
    }

    private void cargarLocales() {
        listaOriginal.clear();
        listaOriginal.add(new Local("Pelícano", "Discoteca de gran capacidad", "Discoteca", "pelicano.jpeg"));
        listaOriginal.add(new Local("Brétema", "Pub con ambiente tranquilo y buena música", "Pub", "bretema.jpg"));
        listaOriginal.add(new Local("Nouf", "Local moderno con copas y DJ", "Pub", "nouf.jpg"));
        listaOriginal.add(new Local("Chaston", "Discoteca elegante con eventos temáticos", "Discoteca", "chaston.jpg"));
        listaOriginal.add(new Local("Twin Fin", "Bar con cócteles y ambiente surfero", "Bar", "twinfin.jpg"));

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
}
