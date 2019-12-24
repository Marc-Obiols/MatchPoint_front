package com.example.loginlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class activity_info_deporte extends AppCompatActivity implements Interfaz{

    private TextView titleDeporte;
    private TextView textTitleInfo;
    private TextView textInfo;
    private int llamada;
    private String deporte;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_deporte);

        titleDeporte = findViewById(R.id.titleDeporte);
        textInfo = findViewById(R.id.textInfo);

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
                textInfo.setText(datos.getString("more_info"));
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
