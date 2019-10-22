package com.example.loginlayout;

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

public class MainActivity extends AppCompatActivity {

    private EditText user;
    private EditText pass;
    private RequestQueue queue; //cola de las solicitudes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = (EditText) findViewById(R.id.editText);
        pass = (EditText) findViewById(R.id.editText2);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        queue = Volley.newRequestQueue(this); //inicializar el requestqueue
        Request();
    }

    public void login(View view) {
        String aux1 = user.getText().toString();
        String aux2 = pass.getText().toString();
        Toast.makeText(this, aux1, LENGTH_SHORT).show();
        Toast.makeText(this, aux2, LENGTH_SHORT).show();

        //llamar a donde sea y tratar el resultado
    }

    public void signup(View view) {
        Intent i = new Intent(this, Registrar.class);
        startActivity(i);
    }

    private void Request() {
        String url = "https://api.androidhive.info/contacts/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray mj = response.getJSONArray("contacts");
                        for (int i = 0; i < mj.length(); i++) {
                            JSONObject obj = mj.getJSONObject(i);
                            String name = obj.getString("name");
                            Toast.makeText(MainActivity.this, name, LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });
        queue.add(request);
    }
}
