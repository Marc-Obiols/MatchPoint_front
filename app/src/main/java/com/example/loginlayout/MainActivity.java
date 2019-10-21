package com.example.loginlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText user;
    private EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = (EditText) findViewById(R.id.editText);
        pass = (EditText) findViewById(R.id.editText2);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    public void login(View view) {
        String aux1 = user.getText().toString();
        String aux2 = pass.getText().toString();
        Toast.makeText(this, aux1, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, aux2, Toast.LENGTH_SHORT).show();
        //Pasar datos al back, en función de si existe o no el usuario

        //llamar a donde sea y tratar el resultado
    }

    public void signup(View view) {
        Intent i = new Intent(this, Registrar.class);
        startActivity(i);
    }
}
