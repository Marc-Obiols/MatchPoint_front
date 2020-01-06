package com.example.loginlayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static android.widget.Toast.LENGTH_SHORT;

public class activity_modificar_evento extends AppCompatActivity implements Interfaz {

    private int dia, mes, any, hora, min;

    private Spinner spinnerDeportes;
    private Spinner spinnerNiveles;
    private EditText tv_hora;
    private EditText tv_fecha;
    private EditText numeroParticipantes;
    private EditText numeroAsistentes;
    private EditText descripcionText;
    private String id;
    private RequestQueue queue;
    private int llamada;
    private String idEvento;

    private String spinnerStringNivelesModif;
    private String spinnerStringDeportesModif;
    private String numeroAsistentesModif;
    private String numeroParticipantesModif;
    private String tv_horaModif;
    private String tv_fechaModif;
    private String descripcionTextModif;
    private  String nuevaFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_evento);

        spinnerDeportes = (Spinner)  findViewById(R.id.spinner1);
        spinnerNiveles = (Spinner)  findViewById(R.id.spinner2);
        tv_hora = (EditText) findViewById(R.id.tv_hora);
        tv_fecha = (EditText) findViewById(R.id.tv_fecha);
        numeroParticipantes = (EditText) findViewById(R.id.numeroParticipantes);
        numeroAsistentes = (EditText) findViewById(R.id.numeroAsistentes);
        descripcionText = (EditText) findViewById(R.id.descriptionText);

        Intent i = getIntent();
        idEvento = i.getStringExtra("id");
        //idEvento = "5e0e38171c7b05635eeba0a6";

        String [] list_dep = new String[] {"Tenis", "Futbol", "Baloncesto", "Hockey", "Golf", "Rugby", "PingPong", "Running", "Ciclismo"};

        ArrayAdapter<String> opcionesDeportes;
        opcionesDeportes = new ArrayAdapter<String >(this, R.layout.spinner_item, list_dep);
        opcionesDeportes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDeportes.setAdapter(opcionesDeportes);


        ArrayAdapter<String> opcionesNiveles;
        String [] niv = new String[] {"Ninguno", "Aficionado", "Principiante", "Profesional", "Experto"};

        opcionesNiveles = new ArrayAdapter<String >(this,R.layout.spinner_item, niv); //activity para mostrar, tipo de spinner, listado de valores
        opcionesNiveles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNiveles.setAdapter(opcionesNiveles);

        llamada = 1;
        Connection con = new Connection(this);
        queue = Volley.newRequestQueue(this);
        con.execute("http://10.4.41.144:3000/event/info/" + idEvento,"GET", null);
    }

    public void Modificar(View v){

        String spinnerStringNivelesModif;
        String spinnerStringDeportesModif;
        String numeroAsistentesModif;
        String numeroParticipantesModif;
        String tv_horaModif;
        String tv_fechaModif;
        String descripcionTextModif;
        final Calendar c = Calendar.getInstance();

        spinnerStringNivelesModif = spinnerNiveles.getSelectedItem().toString();
        spinnerStringDeportesModif = spinnerDeportes.getSelectedItem().toString();

        numeroAsistentesModif = numeroAsistentes.getText().toString();
        numeroParticipantesModif = numeroParticipantes.getText().toString();
        tv_horaModif = tv_hora.getText().toString();
        tv_fechaModif = tv_fecha.getText().toString();

        System.out.println("Hora "+tv_horaModif);
        System.out.println("Fecha "+tv_fechaModif);
        descripcionTextModif = descripcionText.getText().toString();

        String url = "http://10.4.41.144:3000/event/modify/" + idEvento;

        JSONObject req = new JSONObject();

        try {
            req.put("sport",spinnerStringDeportesModif);
            req.put("initial_users",numeroAsistentesModif);
            req.put("max_users",numeroParticipantesModif);

            String nuevaFecha = tv_fechaModif + "T" + tv_horaModif;
            req.put("date",nuevaFecha);
            req.put("description",descripcionTextModif);
            req.put("level",spinnerStringNivelesModif);
            System.out.println("PETA DENTRO DEL TRY");
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("PETA DENTRO DEL CATCH");
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, req, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("PETA ON RESPONSE0");
                String responses = response.toString();
                System.out.println("PETA ON RESPONSE1");
                //Toast.makeText(activity_modificar_evento.this, responses, LENGTH_SHORT).show();
                System.out.println(responses);
                System.out.println("PETA ON RESPONSE2");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(activity_modificar_evento.this, error.toString(), LENGTH_SHORT).show();
                System.out.println(error.toString());
            }
        });
        queue.add(request);
        Intent i = new Intent(this, activity_main.class);
        startActivity(i);
    }



    private void Request_sport() {
            String url = "http://10.4.41.144:3000/sport";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String [] ress = new String[response.length()];
                        for(int i = 0; i < response.length(); ++i) {
                            String name = String.valueOf(i);
                            ress[i] = response.getString(name);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity_modificar_evento.this, error.toString(), LENGTH_SHORT).show();
                    System.out.println("ERROR");
                }
            });
            queue.add(request);
       }


    public void MostrarCalendario(View view) {
        final Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        any = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                tv_fecha.setText(dayOfMonth + "/" + month + "/" + year);
                dia = dayOfMonth;
                mes = month;
                any = year;
            }
        }, any, mes, dia);
        datePickerDialog.show();
    }


    public void MostrarReloj(View view) {
        final Calendar c = Calendar.getInstance();
        hora = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tv_hora.setText(hourOfDay + ":" + minute);
                hora = hourOfDay;
                min = minute;
            }
        }, hora, min, false);
        timePickerDialog.show();
    }

    @Override
    public void Respuesta(JSONObject datos) {
        if(llamada==1){
            try {
                numeroAsistentes.setText(Integer.toString(datos.getInt("initial_users")));
                numeroParticipantes.setText(Integer.toString(datos.getInt("max_users")));
                System.out.println(datos.getString("sport"));

                ArrayAdapter myAdapEsp = (ArrayAdapter) spinnerDeportes.getAdapter();
                int spinnerPositionEsport = myAdapEsp.getPosition(datos.getString("sport"));
                spinnerDeportes.setSelection(spinnerPositionEsport);



                ArrayAdapter myAdapNiv = (ArrayAdapter) spinnerNiveles.getAdapter();
                int spinnerPositionNiv = myAdapNiv.getPosition(datos.getString("level"));
                spinnerNiveles.setSelection(spinnerPositionNiv);


                tv_hora.setText(datos.getString("date").substring(11, 16));

                tv_fecha.setText(datos.getString("date").substring(0, 10));
                descripcionText.setText(datos.getString("description"));
                spinnerNiveles.setPrompt(datos.getString("level"));
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
