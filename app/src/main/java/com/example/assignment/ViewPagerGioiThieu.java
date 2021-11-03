package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerGioiThieu extends FragmentStatePagerAdapter {
    public ViewPagerGioiThieu(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BlankFragment1();
            case 1:
                return new BlankFragment2();
            case 2:
                return new BlankFragment4();
            default:
                return new BlankFragment1();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
