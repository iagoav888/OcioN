package com.example.myapplicationdeocion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private RequestQueue queue;
    private EditText username;
    private EditText password;
    private EditText passwordRepeat;
    private Button register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.etUsuario);
        password = findViewById(R.id.etContrasena);
        passwordRepeat = findViewById(R.id.etRepetirContrasena);
        register = findViewById(R.id.btnRegistrar);

        queue = Volley.newRequestQueue(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String passR = passwordRepeat.getText().toString().trim();

                if (user.isEmpty() || pass.isEmpty() || passR.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!pass.equals(passR)) {
                    Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    JSONObject obj = new JSONObject();
                    obj.put("username", user);
                    obj.put("password", pass);

                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.POST,
                            "http://10.0.2.2:8000/users",
                            obj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(RegisterActivity.this, "Registro completado", Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(RegisterActivity.this, "Error al hacer registro", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );

                    queue.add(request);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Error al crear la solicitud", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
