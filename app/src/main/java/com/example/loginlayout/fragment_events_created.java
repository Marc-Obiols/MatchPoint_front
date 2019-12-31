package com.example.loginlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;

public class fragment_events_created extends Fragment {
    View view;
    private events_list_adapter adapter;
    private RecyclerView recView;
    private ArrayList<holder_event_card> evList;
    private RequestQueue recQueue;


    public fragment_events_created(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_events_created, container, false);

        recView = view.findViewById(R.id.recyclerId);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        evList = new ArrayList<>();

        recQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        parseJSON();

        //recQueue.add(stringRequest);
        return view;
    }

    private void parseJSON(){
        String url = "http://10.4.41.144:3000/event/all";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray vecJSONS = response.getJSONArray(""); //obtener vec JSONS
                    for (int i = 0; i < vecJSONS.length(); i++) {
                        JSONObject aux = vecJSONS.getJSONObject(i); //JSON d 1 evento
                        int idEvent = aux.getInt("id"); //id de ese evento
                        String deporte = aux.getString("sport"); //deporte del evento

                        String auxdata = aux.getString("date");
                        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+ee:ff");
                        //String data = dateFormat.format(auxdata);
                        String creator = aux.getString("creator");

                        evList.add(new holder_event_card(deporte, creator, auxdata));
                    }

                    adapter = new events_list_adapter(getActivity().getApplicationContext(), evList);
                    recView.setAdapter(adapter);

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

        recQueue.add(request);
    }
}
