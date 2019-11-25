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

import java.text.NumberFormat;

import static android.widget.Toast.LENGTH_SHORT;

public class activity_profile_completo_modificable extends AppCompatActivity {
    private EditText nombreApellidos;
    private ImageView imageProfile;
    private EditText profileDescripcion;
    private EditText opcionGenero;
    private EditText fechaNacimiento;
    private Button buttonModificar;
    private TextView numeroTelefono;

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
        numeroTelefono = findViewById(R.id.telefonoNumero);

        queue = Volley.newRequestQueue(this); //inicializar el requestqueue
        Request("5dcb09294b4cfe0300646aa6");
    }

    public void modificar(View v){

            String nombreApellidosModif;
            String profileDescripcionModif;
            String opcionGeneroModif;
            String fechaNacimientoModif;
            Integer numeroTelefonoModif;

            nombreApellidosModif = nombreApellidos.getText().toString();
            profileDescripcionModif = profileDescripcion.getText().toString();
            opcionGeneroModif = opcionGenero.getText().toString();
            fechaNacimientoModif = fechaNacimiento.getText().toString();
            numeroTelefonoModif = Integer.parseInt( numeroTelefono.getText().toString() );

            String id = "5dcb09294b4cfe0300646aa6";
            String url = "http://10.4.41.144:3000/profile/modify/"+ id;

            JSONObject req = new JSONObject();
            try {
                req.put("real_name",nombreApellidosModif);
                req.put("description",profileDescripcionModif);
                req.put("sex",opcionGeneroModif);
                req.put("birth_date",fechaNacimientoModif);
                req.put("phone_number",numeroTelefonoModif);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, req, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String responses = response.toString();
                    Toast.makeText(activity_profile_completo_modificable.this, responses, LENGTH_SHORT).show();
                    System.out.println(responses);
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
                   nombreApellidos.setText(response.getString("real_name"));
                   profileDescripcion.setText(response.getString("description"));
                   opcionGenero.setText(response.getString("sex"));
                   fechaNacimiento.setText(response.getString("birth_date").substring(0,10));
                   Integer result = response.getInt("phone_number");
                   NumberFormat nm =  NumberFormat.getNumberInstance();
                   numeroTelefono.setText(nm.format(result));
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