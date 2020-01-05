package com.example.loginlayout;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_SHORT;

public class activity_recuperar_contra extends AppCompatActivity implements Interfaz {

    private EditText emailT;
    private int llamada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contra);
        emailT = (EditText) findViewById(R.id.editText2);
    }

    public void recuperar(View view){
        String url = "http://10.4.41.144:3000/recovery";
        String email = emailT.getText().toString();
        if(!isEmailValid(email)){
            Toast.makeText(activity_recuperar_contra.this,"Introduce una dirección de correo válida.", LENGTH_SHORT).show();
        }
        else {
            JSONObject req = new JSONObject();
            try {
                req.put("email", email);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            llamada = 1;
            Connection con = new Connection(this);
            con.execute(url, "POST", req.toString());
        }
    }

    public void login(View view){
        Intent i = new Intent(this, activity_login.class);
        startActivity(i);
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void Respuesta(JSONObject datos) {

        if (llamada==1){
            Toast.makeText(activity_recuperar_contra.this,"Se ha enviado un correo a la dirección indicada con los pasos a seguir para recuperar tu" +
                    "contraseña", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, activity_login.class);
            startActivity(i);
        }
    }
}
