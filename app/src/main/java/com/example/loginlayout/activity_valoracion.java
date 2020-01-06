package com.example.loginlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private String idVoter;
    private String idEvent;
    private TextView sportEvent;
    private RequestQueue queue; //cola de las solicitudes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valoracion);

        valoracionRatingBar = findViewById(R.id.ratingBarValoracion);
        buttonValoracion = findViewById(R.id.buttonValoracion);
        sportEvent = findViewById(R.id.nombreEvento);

        queue = Volley.newRequestQueue(this); //inicializar el requestqueue
        idAdmin = getIntent().getStringExtra("idevento");
        idEvent = getIntent().getStringExtra("id");
        idVoter = UsuariSingleton.getInstance().getId();

        sportEvent.setText( "Evento de " + getIntent().getStringExtra("nombrevento"));
    }

    public void Valorar(View v){
        String url = "http://10.4.41.144:3000/event/rateAndGetCreator/" + idEvent + "/" + idVoter;
        JSONObject req = new JSONObject();

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, req, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String responses = response.toString();
                //Toast.makeText(activity_valoracion.this, responses, LENGTH_SHORT).show();
                System.out.println(responses);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(activity_valoracion.this, error.toString(), LENGTH_SHORT).show();
                System.out.println(error.toString());
            }
        });
        queue.add(request);
        SetEventValorado();
        UpdateUserPoints();
        Toast.makeText(activity_valoracion.this, "Actividad valorada", LENGTH_SHORT).show();
        Intent j = new Intent(activity_valoracion.this,activity_main.class);
        startActivity(j);
    }

    private void SetEventValorado() {
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
                //Toast.makeText(activity_valoracion.this, responses, LENGTH_SHORT).show();
                System.out.println(responses);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(activity_valoracion.this, error.toString(), LENGTH_SHORT).show();
                System.out.println(error.toString());
            }
        });
        queue.add(request);


    }

    public void UpdateUserPoints(){
        System.out.println("HECHO BIENNN");
        String url2 = "http://10.4.41.144:3000/profile/addPoints/" + idVoter;
        JSONObject req2 = new JSONObject();
        try {
            req2.put("points", "30");
        }catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.POST, url2, req2, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response1) {
                String responses1 = response1.toString();
                //Toast.makeText(activity_valoracion.this, responses1, LENGTH_SHORT).show();
                System.out.println(responses1);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(activity_valoracion.this, error.toString(), LENGTH_SHORT).show();
                System.out.println(error.toString());
            }
        });
        queue.add(request2);
    }

}
