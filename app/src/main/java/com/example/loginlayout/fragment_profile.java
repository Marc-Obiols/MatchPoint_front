package com.example.loginlayout;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragment_profile extends Fragment {
    @Nullable

    private TextView nombreApellidos;
    private CircleImageView imageProfile;
    private CircleImageView imageTrophy;
    private TextView opcionGenero;
    private TextView textoGenero;
    private TextView textoNacimiento;
    private TextView nivel;
    private TextView valoracionTexto;
    private TextView valoracionNumero;
    private TextView fechaNacimiento;
    private Button buttonModificar;
    private Button buttonDisconnect;
    private String idUser;

    private DatabaseReference databaseReference;
    private FirebaseDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        idUser = UsuariSingleton.getInstance().getId();
        nombreApellidos = view.findViewById(R.id.nombreApellidos);
        imageProfile = view.findViewById(R.id.imageProfile);
        opcionGenero = view.findViewById(R.id.opcionGenero);
        textoGenero = view.findViewById(R.id.textoGenero);
        textoNacimiento = view.findViewById(R.id.textoNacimiento);
        valoracionTexto = view.findViewById(R.id.valoracionTexto);
        valoracionNumero = view.findViewById(R.id.valoracionNumero);
        fechaNacimiento = view.findViewById(R.id.fechaNacimiento);
        buttonModificar = view.findViewById(R.id.buttonModificar);
        buttonDisconnect = view.findViewById(R.id.buttonDisconnect);
        imageTrophy = view.findViewById(R.id.trophy);
        nivel = view.findViewById(R.id.nivel);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Usuarios").child(UsuariSingleton.getInstance().getId());
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String u = (String) dataSnapshot.getValue();
                System.out.println("NO HA CAMBIADO LA FOTO");
                if (u.charAt(0) == 'h' && u.charAt(1) == 't' && u.charAt(2) == 't' && u.charAt(3) == 'p')
                    Picasso.get().load(u).into(imageProfile);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),activity_profile_completo_modificable.class);
                i.putExtra("id",idUser);
                startActivity(i);
            }
        });

        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(getContext(),activity_main.class);
                UsuariSingleton.getInstance().user_LogOut();
                startActivity(j);
            }
        });

        System.out.println("MAIN");
        queue = Volley.newRequestQueue(getActivity()); //inicializar el requestqueue
        System.out.println("QUEUE");
        Request(UsuariSingleton.getInstance().getId());
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
                    opcionGenero.setText(response.getString("sex"));
                    fechaNacimiento.setText(response.getString("birth_date"));
                    Integer lvl = response.getInt("level");
                    nivel.setText(lvl.toString());
                    if(lvl<11){
                        imageTrophy.setImageResource(R.drawable.bronze);
                    }
                    else if(lvl<21){
                        imageTrophy.setImageResource(R.drawable.silver);
                    }
                    else{
                        imageTrophy.setImageResource(R.drawable.gold);
                    }
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