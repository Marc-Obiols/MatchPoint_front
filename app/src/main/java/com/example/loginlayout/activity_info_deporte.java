package com.example.loginlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class activity_info_deporte extends AppCompatActivity implements Interfaz{

    private TextView titleDeporte;
    private int llamada;
    private String deporte;
    private TextView duration;
    private TextView minPlayers;
    private TextView maxPlayers;
    private TextView materialNecesario;
    private TextView calorias;
    private TextView nivelFisico;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_deporte);

        titleDeporte = findViewById(R.id.titleDeporte);
        duration = findViewById(R.id.durationActividad);
        minPlayers = findViewById(R.id.minPlayers);
        maxPlayers = findViewById(R.id.maxPlayers);
        materialNecesario = findViewById(R.id.materialNecesario);
        calorias = findViewById(R.id.calories);
        nivelFisico = findViewById(R.id.physicalLevel);

        deporte = getIntent().getStringExtra("nombreDeporte");
        llamada = 1;
        Connection con = new Connection(this);
        con.execute("http://10.4.41.144:3000/sport/"+ deporte,"GET", null);
    }

    @Override
    public void Respuesta(JSONObject datos) {
        if(llamada==1){
            try {
                titleDeporte.setText(deporte);
                System.out.println("INFO DEL DEPORTE " + deporte);
                duration.setText(datos.getString("duration"));
                minPlayers.setText(datos.getString("min_players"));
                maxPlayers.setText(datos.getString("max_players"));
                materialNecesario.setText(datos.getString("material_needed"));
                calorias.setText(datos.getString("calories"));
                nivelFisico.setText(datos.getString("physical_level"));
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
