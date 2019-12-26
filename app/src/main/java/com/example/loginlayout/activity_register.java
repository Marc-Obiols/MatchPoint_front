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
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static android.widget.Toast.LENGTH_SHORT;

public class activity_register extends AppCompatActivity {

    private int dia, mes, any;

    private EditText user;
    private EditText pass;
    private EditText email;
    private EditText nombreApellidos;
    private Spinner spinnerGénero;
    private EditText fechaNacimiento;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference referenceUsuarios;
    private RequestQueue queue; //cola de las solicitudes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        email = findViewById(R.id.editEmail);
        pass = findViewById(R.id.editPassword);
        user = findViewById(R.id.editUsername);
        nombreApellidos = findViewById(R.id.editNombreApellidos);
        spinnerGénero = findViewById(R.id.editGénero);
        fechaNacimiento = (EditText) findViewById(R.id.editFechaNacimiento);

        String [] list_gen = new String[] {"Masculino", "Femenino"};

        ArrayAdapter<String> opcionesGenero;
        opcionesGenero = new ArrayAdapter<String >(this, android.R.layout.simple_spinner_item, list_gen);
        spinnerGénero.setAdapter(opcionesGenero);

        queue = Volley.newRequestQueue(this); //inicializar el requestqueue
        database = FirebaseDatabase.getInstance();
        referenceUsuarios = database.getReference("Usuarios");
        System.out.println(referenceUsuarios.toString());
    }

    public void login(View view) {
        Intent i = new Intent(this, activity_login.class);
        i.putExtra("From","register");
        startActivity(i);
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
                fechaNacimiento.setText(dayOfMonth + "/" + month + "/" + year);
                dia = dayOfMonth;
                mes = month;
                any = year;
            }
        }, any, mes, dia);
        datePickerDialog.show();
    }


    public void register(View view) {
        String username = user.getText().toString();
        String password = pass.getText().toString();
        String useremail = email.getText().toString();
        String nombreAp = nombreApellidos.getText().toString();
        String spinnerGen = spinnerGénero.getSelectedItem().toString();
        String fechaNac = fechaNacimiento.getText().toString();

        System.out.println("peta agafar valors");

        UsuariSingleton.getInstance().setMail(useremail);
        UsuariSingleton.getInstance().setNom_usuari(username);

        System.out.println("peta Singleton");
        //UsuariSingleton.getInstance().setFotoPerfil("https://firebasestorage.googleapis.com/v0/b/fir-chat-f10b9.appspot.com/o/fotos_perfiles%2Fperfil.png?alt=media&token=15f5670d-1f8f-47d6-bcbe-add28cc6980b");

        Intent i = new Intent(this, activity_main.class);
        startActivity(i);

        System.out.println("peta abans Request");
        Request(username,password,useremail, nombreAp, spinnerGen, fechaNac);
    }

    private void Request(String username, String password, String email, String nombreAp, String spinnerGen, String fechaNac) {
        String url = "http://10.4.41.144:3000/register";
        JSONObject req = new JSONObject();
        System.out.println("peta aqui");
        try {
            req.put("email",email);
            req.put("username",username);
            req.put("password",password);
            req.put("real_name", nombreAp);
            req.put("sex", spinnerGen);
            req.put("birth_date", fechaNac);
            System.out.println("request");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, req, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String responses = response.toString();
                Toast.makeText(activity_register.this, responses, LENGTH_SHORT).show();
                System.out.println(responses);
                try {
                    System.out.println("SISU1");
                    Usuari usuari = new Usuari();
                    usuari.setMail(response.getString("email"));
                    usuari.setFotoPerfil("https://firebasestorage.googleapis.com/v0/b/fir-chat-f10b9.appspot.com/o/fotos_perfiles%2Fperfil.png?alt=media&token=15f5670d-1f8f-47d6-bcbe-add28cc6980b");
                    usuari.setNom_usuari(response.getString("username"));
                    referenceUsuarios.child(response.getString("_id")).setValue(usuari);
                    System.out.println("SISU2");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_register.this, error.toString(), LENGTH_SHORT).show();
                System.out.println(error.toString());
            }
        });
        queue.add(request);
    }

}
