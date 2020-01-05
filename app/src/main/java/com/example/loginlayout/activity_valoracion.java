package com.example.loginlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
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

public class activity_valoracion extends AppCompatActivity {

    private RatingBar valoracionRatingBar;
    private Button buttonValoracion;
    private String idAdmin;
    private RequestQueue queue; //cola de las solicitudes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valoracion);

        valoracionRatingBar = findViewById(R.id.ratingBarValoracion);
        buttonValoracion = findViewById(R.id.buttonValoracion);

        queue = Volley.newRequestQueue(this); //inicializar el requestqueue
        idAdmin = getIntent().getStringExtra("idevento");
    }

    public void Valorar(View v){
        String url = "http://10.4.41.144:3000/profile/rate/" + idAdmin;
        JSONObject req = new JSONObject();
        try {
            req.put("rate",valoracionRatingBar.getRating()*2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, req, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String responses = response.toString();
                Toast.makeText(activity_valoracion.this, responses, LENGTH_SHORT).show();
                System.out.println(responses);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_valoracion.this, error.toString(), LENGTH_SHORT).show();
                System.out.println(error.toString());
            }
        });
        queue.add(request);
    }


}
