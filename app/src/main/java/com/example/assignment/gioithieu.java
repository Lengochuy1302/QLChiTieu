package com.example.assignment;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import me.relex.circleindicator.CircleIndicator;

public class gioithieu extends AppCompatActivity {
    private TextView tvSkip;
    private ViewPager viewPager;
    private RelativeLayout layoutBottom;
    //private LinearLayout layoutNext;
    private CircleIndicator circleIndicator;
    private ViewPagerGioiThieu viewPagerGioiThieu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_loading);
        iniUI();
        getSupportActionBar().hide();

        viewPagerGioiThieu = new ViewPagerGioiThieu(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerGioiThieu);
        circleIndicator.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    tvSkip.setVisibility(View.GONE);
                    layoutBottom.setVisibility(View.GONE);
                } else {
                    tvSkip.setVisibility(View.VISIBLE);
                    layoutBottom.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    private void iniUI() {
        tvSkip = findViewById(R.id.tv_skip);
        viewPager = findViewById(R.id.view_pager);
        layoutBottom = findViewById(R.id.layout_bottom);
        circleIndicator = findViewById(R.id.circle_indator);
//        layoutNext = findViewById(R.id.layout_next);

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

//        layoutNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (viewPager.getCurrentItem() < 2) {
//                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
//                }
//            }
//        });
    }
}