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


public class fragment_map extends Fragment implements OnMapReadyCallback, Interfaz {

    private GoogleMap mMap;
    private ImageView addbutton;
    private ImageView refreshbutton;
    private ImageView mGps;
    private int pulsado = 0;
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
        Connection con = new Connection(this);
        con.execute("http://10.4.41.144:3000/event/all", "GET", null);
    }

    @Override
    public void Respuesta(JSONObject datos) {
        try {
            JSONArray response = datos.getJSONArray("array");
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
                                "Plazas restantes: " + (aux.getInt("max_users")-aux.getInt("initial_users"))+ "\n" +
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

}
