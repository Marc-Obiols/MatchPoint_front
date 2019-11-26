package com.example.loginlayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.widget.Toast.LENGTH_SHORT;

public class fragment_map extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ImageView addbutton;
    private ImageView refreshbutton;
    private int pulsado = 0;
    private HashMap<String, JSONObject> events;
    private RequestQueue queue;

    private void cargareventos() {//cargar todos los eventos de la BD en el mapa y crear map idEvent/JSON

        String url = "http://10.4.41.144:3000/event/all";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    System.out.println(response.length());
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject aux = response.getJSONObject(i); //JSON d 1 evento
                        String idEvent = aux.getString("_id"); //id de ese evento

                        events.put(idEvent, aux); //se guarda en el map de JSONS de eventos

                        String deporte = aux.getString("sport"); //deporte del evento

                        Double lat = aux.getDouble("latitude");
                        Double lng = aux.getDouble("longitude"); //coords

                        String nomevent = "Evento de "+deporte;

                        String infoevent =
                                "Participantes totales: " + aux.getInt("max_users")  + "\n" +
                                "Plazas restantes: " + "Implementar" + "\n" +
                                "Nivel: " + aux.getString("level") + "\n" +
                                "Descripción: " +aux.getString("description");


                        if (deporte == "Futbol")
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.soccer)).snippet(infoevent)).setTag(idEvent); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte == "Baloncesto")
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.soccer)).snippet(infoevent)).setTag(idEvent); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte == "Tenis")
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.soccer)).snippet(infoevent)).setTag(idEvent); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte == "Padel")
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.soccer)).snippet(infoevent)).setTag(idEvent); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte == "Hockey")
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.soccer)).snippet(infoevent)).setTag(idEvent); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte == "Golf")
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.soccer)).snippet(infoevent)).setTag(idEvent); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.soccer)).snippet(infoevent)).setTag(idEvent); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("CODIGOCATCH");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), LENGTH_SHORT).show();
                System.out.println(error.toString());
                System.out.println("CODIGOERROR");
            }
        });
        queue.add(request);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        super.onCreate(savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        addbutton = (ImageView) view.findViewById(R.id.addbutton);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Choose a location", Toast.LENGTH_LONG).show();
                pulsado = 1;
            }
        });
        refreshbutton = (ImageView) view.findViewById(R.id.refreshbutton);
        refreshbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               cargareventos();
            }
        });

        events = new HashMap<>();
        queue = Volley.newRequestQueue(getActivity());
        cargareventos();
        return (view);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getContext()));
                if(marker.isInfoWindowShown()){
                    marker.hideInfoWindow();
                }else{
                    marker.showInfoWindow();
                }
                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (pulsado == 1) {
                    pulsado = 0;
                    Intent i = new Intent(getActivity().getBaseContext(), activity_crear_evento.class);
                    i.putExtra("lat", latLng.latitude);
                    i.putExtra("lng", latLng.longitude);
                    startActivity(i);

                    //para acceder a esto en la otra view: Double lat = getIntent().getDoubleExtra("lat");

                    //mMap.addMarker(new MarkerOptions().position(latLng).title("Marcador")).setTag(0);
                    //añade marcador ^

                }
            }
        });
    }
}
