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

        // Configuramos el RecyclerView con un layout vertical
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Creamos el adaptador y lo conectamos al RecyclerView
        adapter = new LocalAdapter(listaFiltrada);
        recyclerView.setAdapter(adapter);

        // Cargamos los locales (de momento de forma manual)
        cargarLocales();

        // Activamos el buscador para filtrar los locales por nombre
        configurarBuscador();
    }

    // Método para cargar los locales en la lista (más adelante vendrán del servidor Django)
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

    // Este método sirve para activar el buscador y detectar cuando el usuario escribe
    private void configurarBuscador() {
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Antes de escribir no hacemos nada
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cada vez que cambia el texto del buscador, filtramos la lista
                filtrarLocales(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Después de escribir tampoco hacemos nada aquí
            }
        });
    }

    // Este método filtra la lista según lo que el usuario escriba en el buscador
    private void filtrarLocales(String texto) {
        listaFiltrada.clear();

        // Si no hay texto, mostramos todos los locales
        if (texto.isEmpty()) {
            listaFiltrada.addAll(listaOriginal);
        } else {
            // Si hay texto, filtramos por nombre (ignorando mayúsculas y minúsculas)
            for (Local local : listaOriginal) {
                if (local.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                    listaFiltrada.add(local);
                }
            }
        }

        // Avisamos al adaptador de que la lista ha cambiado
        adapter.notifyDataSetChanged();
    }
}
