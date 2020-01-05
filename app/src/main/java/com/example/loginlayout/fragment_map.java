package com.example.loginlayout;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class fragment_map extends Fragment implements OnMapReadyCallback, Interfaz, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private ImageView addbutton;
    private ImageView refreshbutton;
    private ImageView mGps;
    private ImageView filterbutton;
    private int pulsado = 0;
    private int reset = 0;
    private HashMap<String, JSONObject> events;
    private View view;
    private LatLng userlocation;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final String TAG = "MapActivity";

    private EditText mSearchText;

    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        super.onCreate(savedInstanceState);

        getLocationPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        events = new HashMap<>();
        cargareventos();
        return (view);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);

        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        mSearchText = (EditText) view.findViewById(R.id.input_search);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    //execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });

        mGps = (ImageView) view.findViewById(R.id.icgps);
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });

        addbutton = (ImageView) view.findViewById(R.id.addbutton);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UsuariSingleton.getInstance().getId() != null){
                    Toast.makeText(getActivity(),"Choose a location", Toast.LENGTH_LONG).show();
                    pulsado = 1;
                }
                else{
                    Intent i = new Intent(getActivity().getBaseContext(), activity_login.class);
                    i.putExtra("From","main");
                    startActivity(i);
                }
            }
        });

        refreshbutton = (ImageView) view.findViewById(R.id.refreshbutton);
        refreshbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getIntent().putExtra("filtros",0);
                reset = 1;
                cargareventos();
            }
        });

        filterbutton = view.findViewById(R.id.icfilter);
        filterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getBaseContext(), filter.class);
                startActivity(i);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getContext()));
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

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            //Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(getContext(), address.toString(), Toast.LENGTH_SHORT).show();
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()),DEFAULT_ZOOM);

        }
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            statusCheck();
                            if(currentLocation == null){
                                userlocation = new LatLng(41.388907, 2.112771);
                                moveCamera(userlocation,
                                        DEFAULT_ZOOM);
                            }
                            else {
                                userlocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                moveCamera(userlocation,
                                        DEFAULT_ZOOM);
                            }

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(getActivity(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(getContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    private void cargareventos() {//cargar todos los eventos de la BD en el mapa y crear map idEvent/JSON
        if((getActivity().getIntent().getIntExtra("filtros",0) == 1) && reset == 0){ //si tienes filtros te meteras aqui
            String deporte = getActivity().getIntent().getStringExtra("filtrodeporte");
            String nivel = getActivity().getIntent().getStringExtra("filtronivel");
            String fecha = getActivity().getIntent().getStringExtra("filtrofecha");
            Connection con = new Connection(this);
            String url = "http://10.4.41.144:3000/event/filtered/"+fecha+"/"+deporte+"/"+nivel;
            con.execute(url, "GET", null);
        }
        else { //si tienes que cargar todos los eventos entraras aqui
            reset = 0;
            Connection con = new Connection(this);
            con.execute("http://10.4.41.144:3000/event/all", "GET", null);
        }
    }

    @Override
    public void Respuesta(JSONObject datos) {
        try {
            JSONArray response = datos.getJSONArray("array");
            for (int i = 0; i < response.length(); i++) {
                JSONObject aux = response.getJSONObject(i); //JSON d 1 evento
                String idEvent = aux.getString("_id"); //id de ese evento
                events.put(idEvent, aux); //se guarda en el map de JSONS de eventos
                if (aux.getBoolean("sponsored")) { //si es patrocinado los pintas de otro color y pones otra info
                    if (aux.getInt("max_users") - aux.getInt("initial_users") > 0) { //si aun quedan plazas se pintan
                        String deporte = aux.getString("sport"); //deporte del evento
                        Double lat = aux.getDouble("latitude");
                        Double lng = aux.getDouble("longitude"); //coords

                        String date = aux.getString("date");
                        String fecha = date.substring(0, 10);
                        String hora = date.substring(11, 16);
                        String nomevent = "Evento patrocinado de " + deporte;
                        String infoevent =
                                "Fecha: " + date.substring(8, 10) + "-" + date.substring(5, 7) + "-" + date.substring(0, 4) + "\n" +
                                        "Hora: " + hora + "\n" +
                                        "Participantes totales: " + aux.getInt("max_users") + "\n" +
                                        "Plazas restantes: " + (aux.getInt("max_users") - aux.getInt("initial_users"));
                        Pair<String, String> pair = new Pair<>(idEvent, aux.getString("creator")); //pair id evento y creador del evento
                        if (deporte.equals("Futbol"))
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.futpatro)).snippet(infoevent)).setTag(pair); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte.equals("Baloncesto"))
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.basketpatro)).snippet(infoevent)).setTag(pair); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte.equals("Tenis"))
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.tenispatro)).snippet(infoevent)).setTag(pair); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte.equals("PingPong"))
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.pingpatro)).snippet(infoevent)).setTag(pair); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte.equals("Hockey"))
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.hockeypatro)).snippet(infoevent)).setTag(pair); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte.equals("Golf"))
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.golfpatro)).snippet(infoevent)).setTag(pair); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte.equals("Running"))
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.runpatro)).snippet(infoevent)).setTag(pair); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte.equals("Rugby"))
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.rugbypatro)).snippet(infoevent)).setTag(pair); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.bikepatro)).snippet(infoevent)).setTag(pair); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                    }

                }
               else { //eventos normales (sin patrocinar)
                    if (aux.getInt("max_users") - aux.getInt("initial_users") > 0) { //si aun quedan plazas se pinta
                        String deporte = aux.getString("sport"); //deporte del evento

                        Double lat = aux.getDouble("latitude");
                        Double lng = aux.getDouble("longitude"); //coords

                        String date = aux.getString("date");
                        String fecha = date.substring(0, 10);
                        String hora = date.substring(11, 16);
                        String nomevent = "Evento de " + deporte;
                        String infoevent =
                                "Fecha: " + date.substring(8, 10) + "-" + date.substring(5, 7) + "-" + date.substring(0, 4) + "\n" +
                                        "Hora: " + hora + "\n" +
                                        "Participantes totales: " + aux.getInt("max_users") + "\n" +
                                        "Plazas restantes: " + (aux.getInt("max_users") - aux.getInt("initial_users"));
                        Pair<String, String> pair = new Pair<>(idEvent, aux.getString("creator")); //pair id evento y creador del evento
                        if (deporte.equals("Futbol"))
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.futbol)).snippet(infoevent)).setTag(pair); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte.equals("Baloncesto"))
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.baloncesto)).snippet(infoevent)).setTag(pair); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte.equals("Tenis"))
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.tennis)).snippet(infoevent)).setTag(pair); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte.equals("PingPong"))
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.pingpong)).snippet(infoevent)).setTag(pair); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte.equals("Hockey"))
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.hockey)).snippet(infoevent)).setTag(pair); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte.equals("Golf"))
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.golf)).snippet(infoevent)).setTag(pair); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte.equals("Running"))
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.running)).snippet(infoevent)).setTag(pair); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else if (deporte.equals("Rugby"))
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.rugby)).snippet(infoevent)).setTag(pair); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                        else
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(nomevent).icon(BitmapDescriptorFactory.fromResource(R.drawable.ciclismo)).snippet(infoevent)).setTag(pair); //crear un marcador en la coordenada y asignarle la id del evento al marcador para futuras busquedas
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Pair<String, String> p = (Pair<String, String>) marker.getTag(); //first id evento second id creador evento
        if (UsuariSingleton.getInstance().getId() == null) { //si no estas registrado registrate
            Intent i = new Intent(getActivity().getBaseContext(), activity_login.class);
            i.putExtra("From","main");
            startActivity(i);
        } else {
            if(UsuariSingleton.getInstance().getId().equals(p.second)){
                Intent i = new Intent(getActivity().getBaseContext(), activity_evento.class);
                i.putExtra("idevento",p.first);
                startActivity(i);
            }
            else {
                Intent i = new Intent(getActivity().getBaseContext(), activity_evento_participante.class);
                i.putExtra("idevento",p.first);
                startActivity(i);
            }

        }

    }

}
