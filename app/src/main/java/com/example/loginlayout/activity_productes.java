package com.example.loginlayout;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class activity_productes extends AppCompatActivity implements Interfaz {

    ListView listView;
    ArrayList<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productes);
        listView = (ListView)findViewById(R.id.listview);
        arrayList = new ArrayList<>();
        for (int i = 0; i < 20; ++i) arrayList.add("HOLA");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        //Connection con = new Connection(this);
        //con.execute("http://ec2-3-81-131-156.compute-1.amazonaws.com:8080/api/products/sport_supplemet_products", "GET", null);
    }


    public void Respuesta(JSONObject datos) {
        try {
            JSONArray response = datos.getJSONArray("array");
            for (int i = 0; i < response.length(); i++) {
                JSONObject aux = response.getJSONObject(i); //JSON d 1 evento
                Toast.makeText(this, (CharSequence) aux, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
