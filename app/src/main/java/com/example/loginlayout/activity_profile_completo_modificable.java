package com.example.loginlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class activity_profile_completo_modificable extends AppCompatActivity {
    private EditText nombreApellidos;
    private ImageView imageProfile;
    private EditText profileDescripcion;
    private EditText opcionGenero;
    private EditText fechaNacimiento;
    private Button buttonModificar;

    private RequestQueue queue; //cola de las solicitudes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_modificable);

        nombreApellidos = findViewById(R.id.nombreApellidos);
        imageProfile = findViewById(R.id.imageProfile);
        profileDescripcion = findViewById(R.id.profileDescripcion);
        opcionGenero = findViewById(R.id.opcionGenero);
        fechaNacimiento = findViewById(R.id.fechaNacimiento);
        buttonModificar = findViewById(R.id.buttonModificar);

        queue = Volley.newRequestQueue(this); //inicializar el requestqueue
        Request("5dcb09294b4cfe0300646aa6");


    }

    public void modificar(View v){

            final String nombreApellidosModif;
            String profileDescripcionModif;
            String opcionGeneroModif;
            String fechaNacimientoModif;

            nombreApellidosModif = nombreApellidos.getText().toString();
            profileDescripcionModif = profileDescripcion.getText().toString();
            opcionGeneroModif = opcionGenero.getText().toString();
            fechaNacimientoModif = fechaNacimiento.getText().toString();

            String id = "5dcb09294b4cfe0300646aa6";
            String url = "http://10.4.41.144:3000/profile/"+ id;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity_profile_completo_modificable.this, error.toString(), LENGTH_SHORT).show();
                    System.out.println(error.toString());
                }
            });
            queue.add(request);
    }

   public void Request(String id) {
       String url = "http://10.4.41.144:3000/profile/" + id;

       JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
           //@Override
           public void onResponse(JSONObject response) {
               try {
                   nombreApellidos.setText(response.getString("username"));
                   profileDescripcion.setText(response.getString("description"));
                   opcionGenero.setText(response.getString("sex"));
                   fechaNacimiento.setText(response.getString("birth_date"));
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(activity_profile_completo_modificable.this, error.toString(), LENGTH_SHORT).show();
               System.out.println(error.toString());
           }
       });
       queue.add(request);
   }
}