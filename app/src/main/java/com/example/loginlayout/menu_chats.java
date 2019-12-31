package com.example.loginlayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class menu_chats extends Fragment implements Interfaz {

    private RecyclerView rvChats;
    private AdapterChats adapter;
    private View view;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<String> eventosPart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_menu_chats, container, false);

        rvChats = view.findViewById(R.id.rvChats);
        eventosPart = new ArrayList<>();
        Connection con = new Connection(this);
        con.execute("http://10.4.41.144:3000/participant/coming/" + UsuariSingleton.getInstance().getId(), "GET", null);
        //System.out.println("LO ESTOY ENVIANDO AQUI: http://10.4.41.144:3000/participant/coming/"+ UsuariSingleton.getInstance().getId());

        //con.execute("http://10.4.41.144:3000/participant/coming/5e04ffd8e1e9905a26087d0b", "GET", null);
        System.out.println("CONTINUO EJECUTANDO");

        adapter = new AdapterChats(view.getContext());
        LinearLayoutManager l = new LinearLayoutManager(view.getContext());
        rvChats.setLayoutManager(l);
        rvChats.setAdapter(adapter);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        return view;
    }

    private void setScrollbar() {
        rvChats.scrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    public void Respuesta(JSONObject datos) {
        try {
            if (datos.getInt("codigo") == 200) {
                System.out.println("HE ENTRADO 200");
                if (datos.getJSONArray("array") == null) {
                    return;
                }
                JSONArray response = datos.getJSONArray("array");
                System.out.println("TAMAÑO DE LA RESPUESTA: " + response.length());
                for(int i = 0; i < response.length(); i++) {
                    JSONObject event = response.getJSONObject(i);
                    System.out.println(event.getString("_id"));
                    eventosPart.add(event.getString("_id"));
                }
                OperacionAuxiliar();
                System.out.println("HE SALIDO");
            }
            else System.out.println(datos.getInt("codigo"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void OperacionAuxiliar() {
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chat");//Sala de chat

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (eventosPart.contains(dataSnapshot.getKey())) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    adapter.addChat(chat, dataSnapshot.getKey());
                }
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
    }
}
