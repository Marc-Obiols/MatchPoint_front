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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static android.widget.Toast.LENGTH_SHORT;


public class activity_crear_evento extends AppCompatActivity implements Interfaz {

    private Spinner deportes;
    private Spinner nivel;
    private int dia, mes, any, hora, min;
    private TextView tv_hora;
    private TextView tv_fecha;
    private EditText Part_total;
    private EditText Part;
    private EditText Descripcion;
    private int llamada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);

        //String dato = getIntent().getStringExtra("nombre");

        dia = mes = any = hora = min = -1;
        deportes = (Spinner) findViewById(R.id.spinner1);
        tv_fecha = (TextView) findViewById(R.id.tv_fecha);
        tv_hora = (TextView) findViewById(R.id.tv_hora);
        nivel = (Spinner) findViewById(R.id.spinner2);
        Part_total = (EditText) findViewById(R.id.editText9);
        Part = (EditText) findViewById(R.id.editText10);
        Descripcion = (EditText) findViewById(R.id.editText7);

        ArrayAdapter<String> aaNiv;
        String [] niv = new String[] {"Ninguno", "Aficionado", "Principiante", "Profesional", "Experto"};
        //llamar al servidor
        aaNiv = new ArrayAdapter<String >(this, android.R.layout.simple_spinner_item, niv); //activity para mostrar, tipo de spinner, listado de valores
        nivel.setAdapter(aaNiv);
        llamada = 1;
        Connection con = new Connection(this);
        con.execute("http://10.4.41.144:3000/sport", "GET", null);

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

    public void CrearEvento(View view) {

        final Calendar c = Calendar.getInstance();
        String dep = deportes.getSelectedItem().toString();
        String niv = nivel.getSelectedItem().toString();
        String pt = Part_total.getText().toString();
        String p = Part.getText().toString();
        String desc = Descripcion.getText().toString();

        //control de errores

        int minutos_tot = any * 525600 + mes * 43200 + dia * 1440 + hora * 60 + min;
        int minutos_act = c.get(Calendar.YEAR) * 525600 + (c.get(Calendar.MONTH)+1) * 43200 + c.get(Calendar.DAY_OF_MONTH) * 1440 + (c.get(Calendar.HOUR_OF_DAY)+1) * 60 + c.get(Calendar.MINUTE);

        if((minutos_tot - minutos_act) > 0) {
            if (Integer.valueOf(p) < Integer.valueOf(pt)) {
                //arreglar minutos mes y dia como hora
                String fecha = any + "-" + mes + "-" + dia;
                String ini;
                if (hora >= 0 && 9 >= hora) ini = "0" + hora + ":" + min;
                else ini = hora + ":" + min;
                JSONObject req = new JSONObject();
                JSONArray users = new JSONArray();
                String date = fecha + "T" + ini;
                System.out.println(date);
                try {

                    users.put("5db49aadffc609546f5091e3");
                    req.put("creator","5db49aadffc609546f5091e3");
                    Double lat = getIntent().getDoubleExtra("lat",99); //pillar la lat y long del new event (pablo)
                    req.put("latitude",lat);
                    Double lng = getIntent().getDoubleExtra("lng",99);
                    req.put("longitude",lng);
                    req.put("sport",dep);
                    req.put("level",niv);
                    req.put("max_users",pt);
                    req.put("initial_users",p);
                    req.put("description",desc);
                    req.put("date", date);
                    req.put("users",users);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                llamada = 2;
                Connection con = new Connection(this);
                con.execute("http://10.4.41.144:3000/event/new", "POST", req.toString());
            }
            else {
                //error de participantes iniciales
            }

        }

        else {
            //mensaje de error en la fecha o hora
        }
    }

    public void Menu_Principal(View view) {
        Intent i = new Intent(this, activity_main.class);
        startActivity(i);
    }

    @Override
    public void Respuesta(JSONObject datos) {
        if (llamada == 2) {
            try {
                System.out.println(datos.getInt("codigo"));
                if (datos.getInt("codigo") == 200) {
                    Intent i = new Intent(this, activity_main.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(activity_crear_evento.this, "error q flipas loco", LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (llamada == 1) {

            String [] ress = new String[datos.length()-1];
            for(int i = 0; i < datos.length()-1; ++i) {
                System.out.println(i);
                String name = String.valueOf(i);
                try {
                    ress[i] = datos.getString(name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            ArrayAdapter<String> aaDep;
            aaDep = new ArrayAdapter<String >(this, android.R.layout.simple_spinner_item, ress); //activity para mostrar, tipo de spinner, listado de valores
            deportes.setAdapter(aaDep);
        }
    }
}
