package com.example.loginlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_SHORT;

public class activity_ranking_top5 extends AppCompatActivity implements Interfaz{

    private TextView usernamePrimero;
    private TextView scorePrimero;
    private TextView usernameSegundo;
    private TextView scoreSegundo;
    private TextView usernameTercero;
    private TextView scoreTercero;
    private TextView usernameCuarto;
    private TextView scoreCuarto;
    private TextView usernameQuinto;
    private TextView scoreQuinto;

    private RequestQueue queue; //cola de las solicitudes

    private int llamada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_top5);

        usernamePrimero = findViewById(R.id.usernamePrimero);
        usernameSegundo = findViewById(R.id.usernameSegundo);
        usernameTercero = findViewById(R.id.usernameTercero);
        usernameCuarto = findViewById(R.id.usernameCuarto);
        usernameQuinto = findViewById(R.id.usernameQuinto);

        scorePrimero = findViewById(R.id.scorePrimero);
        scoreSegundo = findViewById(R.id.scoreSegundo);
        scoreTercero = findViewById(R.id.scoreTercero);
        scoreCuarto = findViewById(R.id.scoreCuarto);
        scoreQuinto = findViewById(R.id.scoreQuinto);

        queue = Volley.newRequestQueue(this); //inicializar el requestqueue

        Connection con = new Connection(this);
        con.execute("http://10.4.41.144:3000/profile/rate/most_valued/:5","GET", null);
    }

    @Override
    public void Respuesta(JSONObject datos) {
        try{
            JSONArray response = datos.getJSONArray("array");

            usernamePrimero.setText(response.getJSONObject(0).getString("username"));
            usernameSegundo.setText(response.getJSONObject(1).getString("username"));
            usernameTercero.setText(response.getJSONObject(2).getString("username"));
            usernameCuarto.setText(response.getJSONObject(3).getString("username"));
            usernameQuinto.setText(response.getJSONObject(4).getString("username"));

            scorePrimero.setText(response.getJSONObject(0).getString("reputation"));
            scoreSegundo.setText(response.getJSONObject(1).getString("reputation"));
            scoreTercero.setText(response.getJSONObject(2).getString("reputation"));
            scoreCuarto.setText(response.getJSONObject(3).getString("reputation"));
            scoreQuinto.setText(response.getJSONObject(4).getString("reputation"));
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("CODIGOCATCH");
        }

    }

}
