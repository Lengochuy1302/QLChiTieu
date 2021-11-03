package com.example.assignment.AdapterThuChi;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.assignment.FragmentThuChi.Tab_KhoanChi_Fragment;
import com.example.assignment.FragmentThuChi.Tab_LoaiChi_Fragment;


public class KhoanChi_ViewPagerAdapter extends FragmentStatePagerAdapter {
    int numberTab = 2;



    public KhoanChi_ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Tab_KhoanChi_Fragment tab_khoanChi_fragment = new Tab_KhoanChi_Fragment();
                return tab_khoanChi_fragment;
            case 1:
                Tab_LoaiChi_Fragment tab_loaiChi_fragment = new Tab_LoaiChi_Fragment();
                return tab_loaiChi_fragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return numberTab;
    }
}
