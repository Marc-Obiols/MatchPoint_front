package com.example.loginlayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class filter extends AppCompatActivity implements Interfaz{

    private int dia, mes, any;
    private int hora, min;
    private Spinner deportes;
    private Spinner nivel;
    private Boolean fecha = false;
    private int llamada;
    private Button botonfecha;

    private String fechaselec;
    private String deporteselec;
    private String nivelselec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        dia = mes = any = -1;
        hora = min = 00;
        deportes = (Spinner) findViewById(R.id.spinnerdeportes);
        nivel = (Spinner) findViewById(R.id.spinnernivel);
        botonfecha = (Button)  findViewById(R.id.buttonfecha);
        botonfecha.setTextSize(11);

        String [] niv = new String[] {"Todos", "Aficionado", "Principiante", "Profesional", "Experto"};
        //llamar al servidor
        ArrayAdapter<String> aaNiv = new ArrayAdapter<String>(this, R.layout.spinner_item, niv); //activity para mostrar, tipo de spinner, listado de valores
        aaNiv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nivel.setAdapter(aaNiv);
        llamada = 1;
        Connection con = new Connection((Interfaz) this);
        con.execute("http://10.4.41.144:3000/sport", "GET", null);
    }



    public void EnviarFiltro(View view) {
        if (fecha) {
            final Calendar c = Calendar.getInstance();
            int minutos_tot = any * 525600 + mes * 43200 + dia * 1440 + hora * 60 + min;
            int minutos_act = c.get(Calendar.YEAR) * 525600 + (c.get(Calendar.MONTH) + 1) * 43200 + c.get(Calendar.DAY_OF_MONTH) * 1440 + (c.get(Calendar.HOUR_OF_DAY) + 1) * 60 + c.get(Calendar.MINUTE);
            if (((minutos_tot - minutos_act) >= 0) || ((any * 525600 + mes * 43200 + dia * 1440) == (c.get(Calendar.YEAR) * 525600 + (c.get(Calendar.MONTH) + 1) * 43200 + c.get(Calendar.DAY_OF_MONTH) * 1440))) {
                //arreglar minutos mes y dia como hora
                String mes_string;
                String dia_string;
                if (mes >= 0 && 9 >= mes) mes_string = "0" + String.valueOf(mes);
                else mes_string = String.valueOf(mes);
                if (dia >= 0 && 9 >= dia) dia_string = "0" + String.valueOf(dia);
                else dia_string = String.valueOf(dia);
                String fecha = String.valueOf(any) + "-" + mes_string + "-" + dia_string;
                fechaselec = fecha + "T23:59:59.000+00:00";
            } else {
                fechaselec = "incorrecta";
            }
        } else {
            fechaselec = "none";
        }
        String dep = deportes.getSelectedItem().toString();
        if(dep != "Todos"){
            deporteselec = dep;
        }
        else deporteselec = "none";
        String niv = nivel.getSelectedItem().toString();
        if(niv !=  "Todos"){
            nivelselec = niv;
        }
        else nivelselec = "none";

        //enviar cosas
        if(fechaselec != "incorrecta"){
            //aqui se envian los 3 campos para el mapa filtrar
            //System.out.println(fechaselec);
            //System.out.println(nivelselec);
            //System.out.println(deporteselec);
            Intent i = new Intent(this, activity_main.class);
            i.putExtra("filtros",1);
            i.putExtra("filtrodeporte",deporteselec);
            i.putExtra("filtronivel",nivelselec);
            i.putExtra("filtrofecha",fechaselec);
            startActivity(i);

        }
        else {
            Toast.makeText(this, "Selecciona el dia de hoy o un dia futuro", Toast.LENGTH_LONG).show();
            //System.out.println(fechaselec);
            //System.out.println(nivelselec);
            //System.out.println(deporteselec);
        }

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
                botonfecha.setText(dayOfMonth + "/" + month + "/" + year);

                fecha = true;
                dia = dayOfMonth;
                mes = month;
                any = year;
            }
        }, any, mes, dia);

        datePickerDialog.show();
    }

    @Override
    public void Respuesta(JSONObject datos) {
        if (llamada == 1) {
            String [] ress = new String[datos.length()];
            ress[0] = "Todos";
            for(int i = 0; i < datos.length()-1; ++i) {
                System.out.println(i);
                String name = String.valueOf(i);
                try {
                    ress[i+1] = datos.getString(name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ArrayAdapter<String> aaDep;
            aaDep = new ArrayAdapter<String >(this,  R.layout.spinner_item, ress); //activity para mostrar, tipo de spinner, listado de valores
            aaDep.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            deportes.setAdapter(aaDep);
        }
    }
}
