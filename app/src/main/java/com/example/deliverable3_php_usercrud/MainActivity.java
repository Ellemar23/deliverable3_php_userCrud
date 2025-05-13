package com.example.deliverable3_php_usercrud;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.*;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText idField, nameField, emailField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idField = findViewById(R.id.editTextId);
        nameField = findViewById(R.id.editTextName);
        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);

        findViewById(R.id.btnCreate).setOnClickListener(v -> {
            String name = nameField.getText().toString();
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            createUser(name, email, password);
        });

        findViewById(R.id.btnRead).setOnClickListener(v -> fetchUsers());

        findViewById(R.id.btnUpdate).setOnClickListener(v -> {
            String idText = idField.getText().toString();
            if (idText.isEmpty()) {
                Toast.makeText(this, "Please enter an ID", Toast.LENGTH_SHORT).show();
                return;
            }
            int id = Integer.parseInt(idText);
            String name = nameField.getText().toString();
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            updateUser(id, name, email, password);
        });

        findViewById(R.id.btnDelete).setOnClickListener(v -> {
            String idText = idField.getText().toString();
            if (idText.isEmpty()) {
                Toast.makeText(this, "Please enter an ID", Toast.LENGTH_SHORT).show();
                return;
            }
            int id = Integer.parseInt(idText);
            deleteUser(id);
        });
    }

    private void createUser(String name, String email, String password) {
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.CREATE,
                response -> {
                    Toast.makeText(this, "User Created: " + response, Toast.LENGTH_LONG).show();
                    clearInputFields();
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("email", email);
                map.put("password", password);
                return map;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void fetchUsers() {
        StringRequest request = new StringRequest(Request.Method.GET, endpoints.READ_ALL,
                response -> {
                    try {
                        JSONArray users = new JSONArray(response);
                        StringBuilder builder = new StringBuilder();

                        for (int i = 0; i < users.length(); i++) {
                            JSONObject obj = users.getJSONObject(i);
                            int id = obj.getInt("id");
                            String name = obj.getString("name");
                            String email = obj.getString("email");
                            builder.append("id:").append(id).append("\nName: ").append(name)
                                    .append("\nEmail: ").append(email).append("\n\n");
                        }

                        new AlertDialog.Builder(this)
                                .setTitle("User List")
                                .setMessage(builder.toString())
                                .setPositiveButton("OK", null)
                                .show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        );

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void updateUser(int id, String name, String email, String password) {
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.UPDATE,
                response -> {
                    Toast.makeText(this, "User Updated: " + response, Toast.LENGTH_LONG).show();
                    clearInputFields();
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(id));
                map.put("name", name);
                map.put("email", email);
                map.put("password", password);
                return map;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void deleteUser(int id) {
        StringRequest request = new StringRequest(Request.Method.POST, endpoints.DELETE,
                response -> {
                    Toast.makeText(this, "User deleted: " + response, Toast.LENGTH_LONG).show();
                    clearInputFields();
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(id));
                return map;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void clearInputFields() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
    }
}
