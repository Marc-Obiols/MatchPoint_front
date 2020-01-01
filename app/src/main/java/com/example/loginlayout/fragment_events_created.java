package com.example.loginlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class fragment_events_created extends Fragment {
    View view;
    private RecyclerView createdRV;
    private List<holder_event_card> listEventsCreated;

    public fragment_events_created(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_events_created, container, false);
        createdRV = view.findViewById(R.id.recyclerId);
        events_recycleview_adapter recyclerAdapter = new events_recycleview_adapter(getContext(), listEventsCreated);
        createdRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        createdRV.setAdapter(recyclerAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listEventsCreated = new ArrayList<>();
        listEventsCreated.add(new holder_event_card("Placeholder", "John", "25/05/1999"));
    }


}
