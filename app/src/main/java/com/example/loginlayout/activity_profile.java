package com.example.loginlayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import 	android.graphics.Bitmap;
import android.text.Editable.Factory;
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

public class activity_profile extends AppCompatActivity {
    private ImageView fotoProfile;
    private TextView  nombreProfile;
    private TextView  valoracionText;
    private TextView  valoracionNumero;
    private Button modificarButton;
    private RequestQueue queue; //cola de las solicitudes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nombreProfile = findViewById(R.id.nombreApellidos);
        valoracionNumero = findViewById(R.id.valoraciónNumero);
        valoracionText = findViewById(R.id.valoraciónText);
        fotoProfile = findViewById(R.id.imageProfile);
        modificarButton = findViewById(R.id.modificarButton);
        queue = Volley.newRequestQueue(this); //inicializar el requestqueue
    }

   /* public void getPerfil(View view){
        String
    }*/

    public void Request(String id){
        String url = "http://10.4.41.144:3000/usuarios/:"+ id; //no sé si es correcto

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //JSONArray myJsonArray =  response.getJSONArray("usuari");
               // nombreProfile.setText(response.username());
                //valoracionNumero.setText(response.valoracion());

               /* String responses = response.();
                Toast.makeText(activity_profile.this, responses, LENGTH_SHORT).show();
                System.out.println(responses);*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_profile.this, error.toString(), LENGTH_SHORT).show();
                System.out.println(error.toString());
            }
        });
        queue.add(request);
    }
}
