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

public class activity_modificar_evento extends AppCompatActivity {

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
        id = i.getStringExtra("id");

        String [] list_dep = new String[] {"Tenis", "Futbol", "Baloncesto", "Padel", "Hockey", "Golf", "Rugby"};

        ArrayAdapter<String> opcionesDeportes;
        opcionesDeportes = new ArrayAdapter<String >(this, android.R.layout.simple_spinner_item, list_dep);
        spinnerDeportes.setAdapter(opcionesDeportes);


        ArrayAdapter<String> opcionesNiveles;
        String [] niv = new String[] {"Ninguno", "Aficionado", "Principiante", "Profesional", "Experto"};

        opcionesNiveles = new ArrayAdapter<String >(this, android.R.layout.simple_spinner_item, niv); //activity para mostrar, tipo de spinner, listado de valores
        spinnerNiveles.setAdapter(opcionesNiveles);

        queue = Volley.newRequestQueue(this);
        Request(id);
    }

    private void Request(String id) {
        System.out.println("ENTRA PARA PONER SET y link peta");
        String url = "http://10.4.41.144:3000/event/info/" + id;
        System.out.println("ENTRA PARA PONER SET y link peta");

        System.out.println("ENTRA Y PETA ARRAY");
        //JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {   //@Override
            public void onResponse(JSONObject response) {
                System.out.println("ENTRA PARA PONER SET");
                try {
                    numeroAsistentes.setText(Integer.toString(response.getInt("initial_users")));
                    numeroParticipantes.setText(Integer.toString(response.getInt("max_users")));
                    System.out.println(response.getString("sport"));

                    ArrayAdapter myAdapEsp = (ArrayAdapter) spinnerDeportes.getAdapter();
                    int spinnerPositionEsport = myAdapEsp.getPosition(response.getString("sport"));
                    spinnerDeportes.setSelection(spinnerPositionEsport);



                    ArrayAdapter myAdapNiv = (ArrayAdapter) spinnerNiveles.getAdapter();
                    int spinnerPositionNiv = myAdapNiv.getPosition(response.getString("level"));
                    spinnerNiveles.setSelection(spinnerPositionNiv);


                    tv_hora.setText(response.getString("date").substring(11, 16));

                    tv_fecha.setText(response.getString("date").substring(0, 10));
                    descripcionText.setText(response.getString("description"));
                    spinnerNiveles.setPrompt(response.getString("level"));

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

        String spinnerStringNivelesModif;
        String spinnerStringDeportesModif;
        Integer numeroAsistentesModif;
        Integer numeroParticipantesModif;
        String tv_horaModif;
        String tv_fechaModif;
        String descripcionTextModif;
        final Calendar c = Calendar.getInstance();

        spinnerStringNivelesModif = spinnerNiveles.getSelectedItem().toString();
        spinnerStringDeportesModif = spinnerDeportes.getSelectedItem().toString();

        System.out.println("peta aqui");
        numeroAsistentesModif = Integer.parseInt( numeroAsistentes.getText().toString());

        numeroParticipantesModif = Integer.parseInt( numeroParticipantes.getText().toString());
        System.out.println(numeroAsistentesModif+"AAAAA");

        tv_horaModif = tv_hora.getText().toString();
        tv_fechaModif = tv_fecha.getText().toString();


        //numeroAsistentesModif = numeroAsistentes.getText();
        descripcionTextModif = descripcionText.getText().toString();

        String idEvento = id;
        String url = "http://10.4.41.144:3000/event/modify/" + idEvento;

        JSONObject req = new JSONObject();

        try {
            req.put("sport",spinnerStringDeportesModif);
            req.put("initial_users",numeroAsistentesModif);
            req.put("max_users",numeroParticipantesModif);

            //Date convertedDate = new Date();
            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
            String nuevaFecha = tv_fechaModif + "T" + tv_horaModif;
           // convertedDate = dateFormat.parse(dateString);
          //  Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(nuevaFecha);
          //  req.put("date", DateFormat(nuevaFecha));
            //req.put("date",tv_fechaModif);
            req.put("date",nuevaFecha);
            req.put("description",descripcionTextModif);
            req.put("level",spinnerStringNivelesModif);
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
