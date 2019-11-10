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



import org.json.JSONArray;

import org.json.JSONException;

import org.json.JSONObject;



import static android.widget.Toast.LENGTH_SHORT;



public class MainActivity extends AppCompatActivity {



    private EditText emailT;

    private EditText pass;

    private RequestQueue queue; //cola de las solicitudes



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        emailT = (EditText) findViewById(R.id.editText);

        pass = (EditText) findViewById(R.id.editText2);



        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        //getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        queue = Volley.newRequestQueue(this); //inicializar el requestqueue

    }



    public void login(View view) {

        String email = emailT.getText().toString();

        String password = pass.getText().toString();

        Request(email,password);
    }



    public void signup(View view) {

        Intent i = new Intent(this, Registrar.class);

        startActivity(i);

    }



    private void Request(String email, String password) {

        String url = "http://10.4.41.144:3000/login";
        JSONObject req = new JSONObject();

        try {
            req.put("email",email);
            req.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, req, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String responses = response.toString();
                Toast.makeText(MainActivity.this, responses, LENGTH_SHORT).show();
                System.out.println(responses);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), LENGTH_SHORT).show();
                System.out.println(error.toString());
            }

        });

        queue.add(request);

    }

}