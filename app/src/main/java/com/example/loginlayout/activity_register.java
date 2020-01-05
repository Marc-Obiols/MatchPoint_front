package com.example.loginlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static android.widget.Toast.LENGTH_SHORT;


public class activity_register extends AppCompatActivity implements Interfaz{

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

        if(!isEmailValid(useremail)){
            Toast.makeText(activity_register.this,"Introduce una dirección de correo válida.", LENGTH_SHORT).show();
        }
        else{
            Request(username,password,useremail, nombreAp, spinnerGen, fechaNac);
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void Request(String username, String password, String email, String nombreAp, String spinnerGen, String fechaNac) {
        JSONObject req = new JSONObject();
        try {
            req.put("email",email);
            req.put("username",username);
            req.put("password",password);
            req.put("real_name", nombreAp);
            req.put("sex", spinnerGen);
            req.put("birth_date", fechaNac);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Connection con = new Connection(this);
        con.execute("http://10.4.41.144:3000/register", "POST", req.toString());
    }

    @Override
    public void Respuesta(JSONObject datos) {
        try {
            if (datos.getInt("codigo") == 200) {
                Usuari usuari = new Usuari();
                usuari.setMail(datos.getString("email"));
                usuari.setFotoPerfil("https://firebasestorage.googleapis.com/v0/b/fir-chat-f10b9.appspot.com/o/fotos_perfiles%2Fperfil.png?alt=media&token=15f5670d-1f8f-47d6-bcbe-add28cc6980b");
                usuari.setNom_usuari(datos.getString("username"));
                referenceUsuarios.child(datos.getString("_id")).setValue(usuari);
                UsuariSingleton.getInstance().setMail(datos.getString("email"));
                UsuariSingleton.getInstance().setNom_usuari(datos.getString("username"));
                UsuariSingleton.getInstance().setId(datos.getString("_id"));
                Toast.makeText(activity_register.this,"Te has registrado correctamente!", LENGTH_SHORT).show();

                Intent i = new Intent(this, activity_main.class);
                startActivity(i);
            }
            else {
                System.out.println("error");
                System.out.println("Codigo de error: " + datos.getInt("codigo"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
