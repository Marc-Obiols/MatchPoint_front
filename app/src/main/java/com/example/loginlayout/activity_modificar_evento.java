package com.example.loginlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Vector;

import static android.widget.Toast.LENGTH_SHORT;

public class activity_modificar_evento extends AppCompatActivity {

    private int dia, mes, any, hora, min;

    private Spinner spinnerDeportes;
    private Spinner spinnerNiveles;
    private EditText tv_hora;
    private EditText tv_fecha;
    private EditText numeroParticipantes;
    private EditText numeroAsistentes;
    private EditText descripcionText;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_evento);

        spinnerDeportes = (Spinner)  findViewById(R.id.spinner1);
        //spinnerNiveles = (Spinner)  findViewById(R.id.spinner1);
        tv_hora = (EditText) findViewById(R.id.tv_hora);
        tv_fecha = (EditText) findViewById(R.id.tv_fecha);
        numeroParticipantes = (EditText) findViewById(R.id.numeroParticipantes);
        numeroAsistentes = (EditText) findViewById(R.id.numeroAsistentes);
        descripcionText = (EditText) findViewById(R.id.descriptionText);


        String [] list_dep = new String[] {"Tenis", "Futbol", "Baloncesto", "Padel", "Hockey", "Golf", "Rugby"};

        ArrayAdapter<String> opcionesDeportes;
        opcionesDeportes = new ArrayAdapter<String >(this, android.R.layout.simple_spinner_item, list_dep);
        spinnerDeportes.setAdapter(opcionesDeportes);


        ArrayAdapter<String> opcionesNiveles;
        String [] niv = new String[] {"Ninguno", "Aficionado", "Principiante", "Profesional", "Experto"};

        opcionesDeportes = new ArrayAdapter<String >(this, android.R.layout.simple_spinner_item, niv); //activity para mostrar, tipo de spinner, listado de valores
        //spinner.setAdapter(opcionesDeportes);

        queue = Volley.newRequestQueue(this);
        Request("5dcb0b24081dfc145cee33cd");
    }

    private void Request(String id) {

        String url = "http://10.4.41.144:3000/event/info/" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            //@Override
            public void onResponse(JSONObject response) {
                try {
                    numeroAsistentes.setText(response.getInt("initial_users"));
                    numeroParticipantes.setText(response.getInt("max_users"));
                    spinnerDeportes.setPrompt(response.getString("sport"));
                    tv_hora.setText(response.getString("hour")); //modificar, s'ha d'afegir a la BD
                    tv_fecha.setText(response.getString("date").substring(0, 10));
                    descripcionText.setText(response.getString("description"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_modificar_evento.this, error.toString(), LENGTH_SHORT).show();
                System.out.println(error.toString());
            }
        });
        queue.add(request);
    }

    public void Modificar(View v){

        System.out.println("Entra en modificar");

        String spinnerStringDeportesModif;
        Integer numeroAsistentesModif;
        Integer numeroParticipantesModif;
        String tv_horaModif;
        String tv_fechaModif;
        String descripcionTextModif;
        final Calendar c = Calendar.getInstance();

        System.out.println(spinnerDeportes.getSelectedItem().toString());



        spinnerStringDeportesModif = spinnerDeportes.getSelectedItem().toString();

        System.out.println(spinnerDeportes.getSelectedItem().toString());

        numeroAsistentesModif = Integer.parseInt( numeroAsistentes.getText().toString());
        numeroParticipantesModif = Integer.parseInt( numeroParticipantes.getText().toString());


        tv_horaModif = tv_hora.getText().toString();
        tv_fechaModif = tv_fecha.getText().toString();
        descripcionTextModif = descripcionText.getText().toString();

        //String idCreator = "5dbd924d784f2e5873f2c148";
        String idEvento = "5dcc79e7b6d2e43b8d92bf8f";
        String url = "http://10.4.41.144:3000/event/modify/" + idEvento;

        JSONObject req = new JSONObject();

        try {
            req.put("sport",spinnerStringDeportesModif);
            req.put("initial_users",numeroAsistentesModif);
            req.put("max_users",numeroParticipantesModif);
            req.put("date",tv_fechaModif);
            req.put("hour",tv_horaModif);
            req.put("description",descripcionTextModif);
            System.out.println("Peta en put");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, req, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String responses = response.toString();
                Toast.makeText(activity_modificar_evento.this, responses, LENGTH_SHORT).show();
                System.out.println(responses);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_modificar_evento.this, error.toString(), LENGTH_SHORT).show();
                System.out.println(error.toString());
            }
        });
        queue.add(request);
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

}
