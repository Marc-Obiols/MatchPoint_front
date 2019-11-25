package com.example.loginlayout;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
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

import java.text.NumberFormat;
import java.util.HashMap;

import static android.widget.Toast.LENGTH_SHORT;


public class PruebaMapa extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ImageView addbutton;
    private int pulsado = 0;
    private HashMap<Integer, JSONObject> events;

    private void cargareventos() {//cargar todos los eventos de la BD en el mapa y crear map idEvent/JSON
        String url = "http://10.4.41.144:3000/event/all";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray vecJSONS = response.getJSONArray(""); //obtener vec JSONS
                    for (int i = 0; i < vecJSONS.length(); i++) {
                        JSONObject aux = vecJSONS.getJSONObject(i); //JSON d 1 evento
                        int idEvent = aux.getInt("id"); //id de ese evento
                        events.put(idEvent, aux); //se guarda en el map de JSONS de eventos
                        String deporte = aux.getString("sport"); //deporte del evento
                        Double lat = aux.getDouble("latitude");
                        Double lng = aux.getDouble("longitude"); //coords
                        if (deporte == "Futbol")
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Nom Event").icon(BitmapDescriptorFactory.fromResource(R.drawable.soccer))).setTag(idEvent); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte == "Baloncesto")
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Nom Event").icon(BitmapDescriptorFactory.fromResource(R.drawable.soccer))).setTag(idEvent); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte == "Tenis")
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Nom Event").icon(BitmapDescriptorFactory.fromResource(R.drawable.soccer))).setTag(idEvent); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte == "Padel")
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Nom Event").icon(BitmapDescriptorFactory.fromResource(R.drawable.soccer))).setTag(idEvent); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte == "Hockey")
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Nom Event").icon(BitmapDescriptorFactory.fromResource(R.drawable.soccer))).setTag(idEvent); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte == "Golf")
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Nom Event").icon(BitmapDescriptorFactory.fromResource(R.drawable.soccer))).setTag(idEvent); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte == "Rugby")
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Nom Event").icon(BitmapDescriptorFactory.fromResource(R.drawable.soccer))).setTag(idEvent); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PruebaMapa.this, error.toString(), LENGTH_SHORT).show();
                System.out.println(error.toString());
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        addbutton = (ImageView) findViewById(R.id.addbutton);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PruebaMapa.this,"Choose a location", Toast.LENGTH_LONG).show();
                pulsado = 1;
            }
        });
        cargareventos();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Integer clickCount = (Integer) marker.getTag();
                if (clickCount != null) {
                    clickCount = clickCount + 1;
                    marker.setTag(clickCount);
                    Toast.makeText(PruebaMapa.this,
                            marker.getTitle() +
                                    " has been clicked " + clickCount + " times.",
                            Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (pulsado == 1) {
                    pulsado = 0;
                    Intent i = new Intent(getBaseContext(), activity_crear_evento.class);
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
