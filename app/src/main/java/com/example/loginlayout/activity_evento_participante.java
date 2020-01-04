package com.example.loginlayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_SHORT;

public class activity_evento_participante extends AppCompatActivity implements Interfaz {

    private TextView deporteText;
    private TextView fechaText;
    private TextView horaText;
    private TextView numeroParticipantes;
    private TextView numeroAsistentes;
    private TextView descripcionText;
    private TextView eventoText;
    private Button buttonApuntarse;
    private String idEvento;
    private String idUser;
    private String assistir;
    private boolean resultado;
    private Context context = this;
    private RequestQueue queue; //cola de las solicitudes
    private int llamada;
    private View view;
    private ImageView imageInfo;
    private String nombreDeporte;

    boolean estaApuntado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_participante);

        imageInfo = findViewById(R.id.infoView);
        deporteText = findViewById(R.id.deporte);
        fechaText = findViewById(R.id.tv_fecha);
        horaText = findViewById(R.id.tv_hora2);
        numeroParticipantes = findViewById(R.id.numeroParticipantes);
        numeroAsistentes = findViewById(R.id.numeroAsistentes);
        descripcionText = findViewById(R.id.descriptionText);
        eventoText = findViewById(R.id.eventoText);
        buttonApuntarse = findViewById(R.id.buttonApuntarse);

        idEvento = getIntent().getStringExtra("idevento");
        idUser = UsuariSingleton.getInstance().getId();

        llamada = 1;
        Connection con = new Connection(this);
        con.execute("http://10.4.41.144:3000/event/info/"+idEvento,"GET", null);
    }


    public void ApuntarseDesapuntarse(View v){
        llamada=2;
        Connection con = new Connection(this);
        con.execute("http://10.4.41.144:3000/event/includes/" + idEvento +"/" + idUser,"GET", null);

        System.out.println("ENTRA A LA FUNCION");

        if(estaApuntado){
            llamada=3;
            con = new Connection(this);
            con.execute("http://10.4.41.144:3000/event/unassign/" + idUser +"/" + idEvento,"POST", null);

            System.out.println("PETA A LA FUNCION APUNTARSE");
        }
        else{
            llamada=4;
            con = new Connection(this);
            con.execute("http://10.4.41.144:3000/event/assign/" + idUser +"/" + idEvento,"POST", null);
            System.out.println("PETA A LA FUNCION DESAPUNTARSE");
        }
        System.out.println("SALE FUNCION");
    }

    public void MostrarInfoDeporte(View v){
        Intent i = new Intent(this, activity_info_deporte.class);
        i.putExtra("nombreDeporte", nombreDeporte);
        startActivity(i);
    }

    @Override
    public void Respuesta(JSONObject datos) {
        if(llamada == 1) {//Obtener información del evento
            try {
                nombreDeporte =  datos.getString("sport");
                deporteText.setText("Evento de " + nombreDeporte);
                descripcionText.setText(datos.getString("description"));
                horaText.setText(datos.getString("date").substring(11, 16));
                fechaText.setText(datos.getString("date").substring(0, 10));
                numeroParticipantes.setText(Integer.toString(datos.getInt("max_users")));
                numeroAsistentes.setText(Integer.toString(datos.getInt("initial_users")));
                eventoText.setText(datos.getString("level"));

                llamada = 2;
                Connection con = new Connection(this);
                con.execute("http://10.4.41.144:3000/event/includes/" + idEvento +"/" + idUser,"GET", null);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(llamada == 2){ //Mirar si esta apuntado
            try {
                estaApuntado = datos.getBoolean("response");
                if(estaApuntado){
                    buttonApuntarse.setText(R.string.Desapuntarse);
                }
                else buttonApuntarse.setText(R.string.Apuntarse);
                System.out.println("ESTA APUNTADO?" + estaApuntado);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(llamada == 3){ //Desapuntarse
            try {
                System.out.println("ERROR: " + datos.getInt("codigo"));
                if (datos.getInt("codigo") == 200) {
                    Toast.makeText(activity_evento_participante.this, "Te has desapuntado del evento correctamente", LENGTH_SHORT).show();
                    Intent i = new Intent(this, activity_main.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(activity_evento_participante.this, "Error", LENGTH_SHORT).show();
                }
                final Connection con = new Connection(this);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(llamada == 4){ //Apuntarse
            try {
                System.out.println("ERROR: " + datos.getInt("codigo"));
                if (datos.getInt("codigo") == 200) {
                    Toast.makeText(activity_evento_participante.this, "Te has apuntado al evento correctamente!", LENGTH_SHORT).show();
                    Intent i = new Intent(this, activity_main.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(activity_evento_participante.this, "Error", LENGTH_SHORT).show();
                }
                final Connection con = new Connection(this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
