package com.example.loginlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_SHORT;

public class Registrar extends AppCompatActivity {
    private EditText user;
    private EditText pass;
    private EditText email;
    private RequestQueue queue; //cola de las solicitudes


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        email = findViewById(R.id.editText4);
        pass = findViewById(R.id.editText5);
        user = findViewById(R.id.editText6);
        queue = Volley.newRequestQueue(this); //inicializar el requestqueue

    }

    public void login(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


    public void register(View view) {
        String username = user.getText().toString();
        String password = pass.getText().toString();
        String useremail = email.getText().toString();
        Request(username,password,useremail);
        //llamar a donde sea y tratar el resultado
    }

    private void Request(String username, String password, String email) {
        String url = "http://10.4.41.144:3000/register";
        JSONObject req = new JSONObject();
        try {
            req.put("email",email);
            req.put("username",username);
            req.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, req, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String responses = response.toString();
                Toast.makeText(Registrar.this, responses, LENGTH_SHORT).show();
                System.out.println(responses);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Registrar.this, error.toString(), LENGTH_SHORT).show();
                System.out.println(error.toString());
            }
        });
        queue.add(request);
    }
}
