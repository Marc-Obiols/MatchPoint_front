package com.example.loginlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class activity_list_events extends Fragment {
    private TabLayout tablayout;
    private ViewPager viewPager;
    ////////////////////////////////////
    private eventList_page_viewer_adapter adapter;



    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list_events, container, false);
        tablayout = (TabLayout) view.findViewById(R.id.eventListId);
        viewPager = (ViewPager) view.findViewById(R.id.viewpageId);
        adapter = new eventList_page_viewer_adapter(getFragmentManager());

        //Añadir fragmentos de las tabs de listar eventos
        adapter.AddFragment(new fragment_events_future(), "Proximos Eventos");
        adapter.AddFragment(new fragment_events_participated(), "Eventos participados");
        adapter.AddFragment(new fragment_events_created(), "Eventos creados");

        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);

        return view;
    }
}
