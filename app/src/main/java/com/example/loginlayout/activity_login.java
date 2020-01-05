package com.example.loginlayout;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

import static android.widget.Toast.LENGTH_SHORT;

public class activity_login extends AppCompatActivity {

    private EditText emailT;
    private EditText pass;
    private RequestQueue queue; //cola de las solicitudes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailT = (EditText) findViewById(R.id.editText);
        pass = (EditText) findViewById(R.id.editText2);

        queue = Volley.newRequestQueue(this); //inicializar el requestqueue

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent i = getIntent();
                String from = i.getStringExtra("From");
                if(from!=null){
                    if(from.equals("main")){
                        i = new Intent(getApplicationContext(),activity_main.class);
                        startActivity(i);
                    }
                }
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void login(View view) {
        String email = emailT.getText().toString();
        String password = pass.getText().toString();
        Request(email,password);
        //llamar a donde sea y tratar el resultado
    }

    public void recovery(View view) {
        //Toast.makeText(activity_login.this, "recuperar", LENGTH_SHORT).show();
        Intent i = new Intent(this, activity_recuperar_contra.class);
        startActivity(i);
    }

    public void signup(View view) {
        Intent i = new Intent(this, activity_register.class);
        startActivity(i);
    }

    private void Request(String email, String password) {
        String url = "http://10.4.41.144:3000/login";
        JSONObject req = new JSONObject();
        try {
            req.put("email",email);
            req.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, req, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String responses = response.toString();
                    System.out.println(responses);
                    if(response.getBoolean("validated")){
                        UsuariSingleton.getInstance().setId(response.getString("_id"));
                        UsuariSingleton.getInstance().setMail(response.getString("email"));
                        UsuariSingleton.getInstance().setNom_usuari(response.getString("username"));
                        Toast.makeText(activity_login.this,"Welcome back!", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(),activity_main.class);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(activity_login.this,"Email no confirmado. Por favor, comprueba tu correo.", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_login.this, "Login incorrecto. Comprueba los datos introducidos.", LENGTH_SHORT).show();
                System.out.println(error.toString());
            }
        });
        queue.add(request);
    }
}
