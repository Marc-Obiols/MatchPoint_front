package com.example.loginlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class CrearEvento extends AppCompatActivity {

    private Spinner deportes;
    private int dia, mes, any, hora, min;
    private TextView tv_hora;
    private TextView tv_fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);

        dia = mes = any = hora = min = -1;
        deportes = (Spinner) findViewById(R.id.spinner1);
        tv_fecha = (TextView) findViewById(R.id.tv_fecha);
        tv_hora = (TextView) findViewById(R.id.tv_hora);

        ArrayAdapter<String> aaDep;
        String [] dep = new String[] {"fúrbol", "basquet", "sisu"};
        //llamar al servidor
        aaDep = new ArrayAdapter<String >(this, android.R.layout.simple_spinner_item, dep); //activity para mostrar, tipo de spinner, listado de valores
        deportes.setAdapter(aaDep);
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
        System.out.println(dia + "/" + mes + "/" + any);
        System.out.println(hora + "/" + min);
    }
}
