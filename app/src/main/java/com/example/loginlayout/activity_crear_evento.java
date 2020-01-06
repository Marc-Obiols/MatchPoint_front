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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private ToggleButton CapacidadReducida;

    private int llamada;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

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
        CapacidadReducida = (ToggleButton) findViewById(R.id.CapacidadReducida);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chat");


        String [] niv = new String[] {"Ninguno", "Aficionado", "Principiante", "Profesional", "Experto"};
        //llamar al servidor
        ArrayAdapter<String> aaNiv = new ArrayAdapter<String>(this, R.layout.spinner_item, niv); //activity para mostrar, tipo de spinner, listado de valores
        aaNiv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                String mes_string;
                String dia_string;
                if (mes >= 0 && 9 >= mes) mes_string = "0" + String.valueOf(mes);
                else mes_string = String.valueOf(mes);
                if (dia >= 0 && 9 >= dia) dia_string = "0" + String.valueOf(dia);
                else dia_string = String.valueOf(dia);
                String fecha = String.valueOf(any) + "-" + mes_string + "-" + dia_string;
                String ini;
                String hora_string;
                String min_string;
                if (hora >= 0 && 9 >= hora) hora_string = "0" + String.valueOf(hora);
                else hora_string = String.valueOf(hora);
                if (min >= 0 && 9 >= min) min_string = "0" + String.valueOf(min);
                else min_string = String.valueOf(min);
                ini = hora_string + ":" + min_string;
                System.out.println(ini);
                JSONObject req = new JSONObject();
                JSONArray users = new JSONArray();
                String date = fecha + "T" + ini;

                try {
                    //users.put(UsuariSingleton.getInstance().getId());
                    req.put("creator",UsuariSingleton.getInstance().getId());
                    Double lat = getIntent().getDoubleExtra("lat",99); //pillar la lat y long del new event (pablo)
                    System.out.println("LAT DEL CREARRRRR");
                    System.out.println(lat);
                    req.put("latitude",lat);
                    Double lng = getIntent().getDoubleExtra("lng",99);
                    System.out.println("LNG DEL CREARRRRR");
                    System.out.println(lng);
                    req.put("longitude",lng);
                    req.put("sport",dep);
                    req.put("level",niv);
                    req.put("max_users",pt);
                    req.put("initial_users",p);
                    req.put("description",desc);
                    req.put("date", date);
                    req.put("users",users);
                    req.put("reduced_mobility", CapacidadReducida.isChecked());
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
                    String idEvento = datos.getString("_id");
                    String temaEvento = datos.getString("sport");
                    Mensaje mensaje = new Mensaje();
                    mensaje.setContieneFoto(false);
                    mensaje.setMensaje("Se ha creado el grupo");
                    mensaje.setKeyEmisor(UsuariSingleton.getInstance().getId());
                    String fechaEvento = datos.getString("date");
                    String nombreEvento = temaEvento + " ";
                    for (int i = 0; i < 10; ++i) {
                        nombreEvento = nombreEvento + fechaEvento.charAt(i);
                    }
                    Chat c = new Chat(nombreEvento);
                    databaseReference.child(idEvento).setValue(c);
                    databaseReference.child(idEvento).push().setValue(mensaje);

                    llamada = 3;
                    JSONObject req = new JSONObject();
                    req.put("idUser", UsuariSingleton.getInstance().getId());
                    req.put("idEvent", idEvento);
                    Connection con = new Connection(this);
                    con.execute("http://10.4.41.144:3000/event/assign/" + UsuariSingleton.getInstance().getId() + "/" + idEvento, "POST", req.toString());

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
            aaDep = new ArrayAdapter<String >(this, R.layout.spinner_item, ress); //activity para mostrar, tipo de spinner, listado de valores
            aaDep.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            deportes.setAdapter(aaDep);
        }
        else if (llamada == 3) {
            try {
                System.out.println(datos.getInt("codigo"));
                if (datos.getInt("codigo") == 200) {
                    Intent i = new Intent(this, activity_main.class);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
