package com.example.loginlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

import static android.widget.Toast.LENGTH_SHORT;

public class fragment_profile extends Fragment {
    @Nullable

    private TextView nombreApellidos;
    private ImageView imageProfile;
    private TextView profileDescripcion;
    private TextView opcionGenero;
    private TextView textoGenero;
    private TextView textoNacimiento;
    private TextView valoracionTexto;
    private TextView valoracionNumero;
    private TextView fechaNacimiento;
    private Button buttonModificar;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_profile_completo, container, false);

        nombreApellidos = view.findViewById(R.id.nombreApellidos);
        imageProfile = view.findViewById(R.id.imageProfile);
        profileDescripcion = view.findViewById(R.id.profileDescripcion);
        opcionGenero = view.findViewById(R.id.opcionGenero);
        textoGenero = view.findViewById(R.id.textoGenero);
        textoNacimiento = view.findViewById(R.id.textoNacimiento);
        valoracionTexto = view.findViewById(R.id.valoracionTexto);
        valoracionNumero = view.findViewById(R.id.telefonoNumero);
        fechaNacimiento = view.findViewById(R.id.fechaNacimiento);
        buttonModificar = view.findViewById(R.id.buttonModificar);

        System.out.println("MAIN");
        queue = Volley.newRequestQueue(getActivity()); //inicializar el requestqueue
        System.out.println("QUEUE");
        Request("5dcb09294b4cfe0300646aa6");
        System.out.println("PETA");
        return view;
    }
    public void Request(String id){
        String url = "http://10.4.41.144:3000/profile/"+ id;

        System.out.println(url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //JSONArray myJsonArray = response.getJSONArray("") per molts usuaris
                    //for(int i=0; i<mJsonArray.length();i++);

                    nombreApellidos.setText(response.getString("username"));
                    profileDescripcion.setText(response.getString("description"));
                    opcionGenero.setText(response.getString("sex"));
                    fechaNacimiento.setText(response.getString("birth_date"));
                    Double result = response.getDouble("reputation");
                    NumberFormat nm =  NumberFormat.getNumberInstance();
                    valoracionNumero.setText(nm.format(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });
        queue.add(request);
    }

    private RequestQueue queue; //cola de las solicitudes
}
