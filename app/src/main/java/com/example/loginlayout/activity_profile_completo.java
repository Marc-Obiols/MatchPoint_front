package com.example.loginlayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

import static android.widget.Toast.LENGTH_SHORT;

public class activity_profile_completo extends AppCompatActivity {
    private TextView nombreApellidos;
    private ImageView imageProfile;
    private TextView profileDescripcion;
    private TextView opcionGenero;
    private TextView textoGenero;
    private TextView textoNacimiento;
    private TextView valoracionTexto;
    private TextView valoracionNumero;
    private TextView fechaNacimiento;
    private Button buttonModificar;


    private RequestQueue queue; //cola de las solicitudes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile_completo);

        nombreApellidos = findViewById(R.id.nombreApellidos);
        imageProfile = findViewById(R.id.imageProfile);
        profileDescripcion = findViewById(R.id.profileDescripcion);
        opcionGenero = findViewById(R.id.opcionGenero);
        textoGenero = findViewById(R.id.textoGenero);
        textoNacimiento = findViewById(R.id.textoNacimiento);
        valoracionTexto = findViewById(R.id.valoracionTexto);
        valoracionNumero = findViewById(R.id.telefonoNumero);
        fechaNacimiento = findViewById(R.id.fechaNacimiento);
        buttonModificar = findViewById(R.id.buttonModificar);

        queue = Volley.newRequestQueue(this); //inicializar el requestqueue
        Request(UsuariSingleton.getInstance().getId());
    }

    public void Request(String id){
        String url = "http://10.4.41.144:3000/profile/"+ id;

        System.out.println(url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //JSONArray myJsonArray = response.getJSONArray("") per molts usuaris
                    //for(int i=0; i<mJsonArray.length();i++);

                    nombreApellidos.setText(response.getString("username"));
                    profileDescripcion.setText(response.getString("description"));
                    opcionGenero.setText(response.getString("sex"));
                    fechaNacimiento.setText(response.getString("birth_date"));
                    Double result = response.getDouble("reputation");
                    NumberFormat nm =  NumberFormat.getNumberInstance();
                    valoracionNumero.setText(nm.format(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_profile_completo.this, error.toString(), LENGTH_SHORT).show();
                System.out.println(error.toString());
            }
        });
        queue.add(request);
    }
}
