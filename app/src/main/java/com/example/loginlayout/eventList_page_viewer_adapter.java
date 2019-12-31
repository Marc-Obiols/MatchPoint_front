package com.example.loginlayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class eventList_page_viewer_adapter extends FragmentPagerAdapter  {

    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> fragmentsTitles = new ArrayList<>();

    public eventList_page_viewer_adapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentsTitles.get(position);
    }

    public void AddFragment(Fragment fragment, String title){
        fragments.add(fragment);
        fragmentsTitles.add(title);
    }
}
