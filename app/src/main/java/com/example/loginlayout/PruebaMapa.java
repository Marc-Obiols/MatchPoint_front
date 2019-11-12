package com.example.loginlayout;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.widget.Toast.LENGTH_SHORT;

public class PruebaMapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int pulsado = 0;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        button = (Button) findViewById(R.id.button);


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
        final int[] cont = {0};
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(pulsado == 1) {
                    pulsado = 0;
                    ++cont[0];
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Marcador " + cont[0]).icon(BitmapDescriptorFactory.fromResource(R.drawable.soccer)));

                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(PruebaMapa.this,"Choose a location", Toast.LENGTH_LONG).show();
                pulsado = 1;
            }
        });
        // Add a marker in Sydney and move the camera
        /*
        LatLng sydney = new LatLng(41.385806, 2.117858);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marcador %d"+ cont[0]));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */

    }
}
