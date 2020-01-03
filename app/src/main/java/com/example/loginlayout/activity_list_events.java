package com.example.loginlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class activity_list_events extends Fragment {

    private TabLayout tablayout;
    private ViewPager viewPager;
    private events_list_viewpager_adapter adapter;


    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list_events, container, false);
        tablayout = (TabLayout) view.findViewById(R.id.eventListId);
        viewPager = (ViewPager) view.findViewById(R.id.viewpageId);
        adapter = new events_list_viewpager_adapter(getFragmentManager());

        viewPager.setAdapter(adapter);
        //Añadir fragmentos de las tabs de listar eventos
        adapter.AddFragment(new fragment_events_future(), "Proximos Eventos");
        adapter.AddFragment(new fragment_events_participated(), "Eventos realizados");
        adapter.AddFragment(new fragment_events_created(), "Eventos creados");
        adapter.AddFragment(new fragment_events_valorar(), "Valorar eventos");

        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);

        return view;
    }
}
