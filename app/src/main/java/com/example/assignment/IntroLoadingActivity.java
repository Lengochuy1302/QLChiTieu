package com.example.assignment;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class IntroLoadingActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 123;
    private TextView tvSkip;
    private FirebaseAuth mAuth;
private ViewPager viewPager;
private RelativeLayout layoutBottom;
//private LinearLayout layoutNext;
private CircleIndicator circleIndicator;
private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_loading);
        iniUI();
        getSupportActionBar().hide();

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
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

    @Override
    protected void onStart() {
        super.onStart();
        clickrequest();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
         return;
        } else {
            startActivity(new Intent(IntroLoadingActivity.this, MainActivity.class));
        }
    }


    private void clickrequest() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return;
        }
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

            }
        };
        TedPermission.with(IntroLoadingActivity.this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("N???u b???n kh??ng cho ph??p th?? m???t s??? ch???c n??ng nh?? 'Nh???p v??n b???n b???ng gi???ng n??i', 'Upload avatar' kh??ng th??? s??? d???ng ???????c!\n \nN???u b???n mu???n b???t l???i n?? th?? h??y v??o ph???n [Setting] > [Quy???n truy c???p] > [B???t c??c quy???n truy c???p]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .check();
    }




    private void iniUI() {
        tvSkip = findViewById(R.id.tv_skip);
        viewPager = findViewById(R.id.view_pager);
        layoutBottom = findViewById(R.id.layout_bottom);
        circleIndicator = findViewById(R.id.circle_indator);

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

    }
}