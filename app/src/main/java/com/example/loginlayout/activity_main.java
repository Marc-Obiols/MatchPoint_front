package com.example.loginlayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class activity_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottom_nav = findViewById(R.id.bottom_navigation);
        bottom_nav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new
                fragment_map()).commit();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.nav_map:
                            selectedFragment = new fragment_map();
                            break;
                        case R.id.nav_events:
                            if(UsuariSingleton.getInstance().getNom_usuari() == null){
                                Intent i = new Intent(getApplicationContext(),activity_login.class);
                                i.putExtra("From","main");
                                startActivity(i);
                                return true;
                            }
                            else {
                                selectedFragment = new activity_list_events();
                            }
                            break;

                        case R.id.nav_msgs:
                            if(UsuariSingleton.getInstance().getNom_usuari() == null){
                                Intent i = new Intent(getApplicationContext(),activity_login.class);
                                i.putExtra("From","main");
                                startActivity(i);
                                return true;
                            }
                            else {
                                selectedFragment = new menu_chats();
                            }
                            break;
                        case R.id.nav_profile:
                            if(UsuariSingleton.getInstance().getNom_usuari() == null){
                                Intent i = new Intent(getApplicationContext(),activity_login.class);
                                i.putExtra("From","main");
                                startActivity(i);
                                return true;
                            }
                            else {
                                selectedFragment = new fragment_profile();
                            }
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

}
