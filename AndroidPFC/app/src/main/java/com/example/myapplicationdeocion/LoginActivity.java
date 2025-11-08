package com.example.myapplicationdeocion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private Context context;
    private RequestQueue queue;
    private EditText username;
    private EditText password;
    private TextView noTienesCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Comprobar si ya hay sesión activa
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        context = this;
        username = findViewById(R.id.etUsuarioL);
        password = findViewById(R.id.etContrasenaL);
        Button ingresar = findViewById(R.id.btnIngresarL);
        noTienesCuenta = findViewById(R.id.btnNoTienesCuenta);

        queue = Volley.newRequestQueue(this);

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarLogin();
            }
        });

        noTienesCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, RegisterActivity.class));
            }
        });
    }

    // Método para hacer el login del usuario
    private void realizarLogin() {
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        // Validar campos vacíos
        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Crear JSON con las credenciales
            JSONObject obj = new JSONObject();
            obj.put("username", user);
            obj.put("password", pass);

            // Petición POST al servidor
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    "http://10.0.2.2:8000/session/",
                    obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Guardar el token que devuelve el servidor
                                String token = response.getString("token");

                                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("token", token);
                                editor.putBoolean("isLoggedIn", true);
                                editor.apply();

                                Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                                // Ir a la pantalla principal
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } catch (JSONException e) {
                                Toast.makeText(LoginActivity.this, "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Mostrar diferentes mensajes según el error
                            if (error.networkResponse != null) {
                                int statusCode = error.networkResponse.statusCode;
                                if (statusCode == 404) {
                                    Toast.makeText(LoginActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                                } else if (statusCode == 403) {
                                    Toast.makeText(LoginActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Error de servidor (" + statusCode + ")", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );

            queue.add(request);

        } catch (JSONException e) {
            Toast.makeText(LoginActivity.this, "Error al crear solicitud", Toast.LENGTH_SHORT).show();
        }
    }
}