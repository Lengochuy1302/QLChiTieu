package com.example.assignment;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AnimationLoading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_loading);
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.timi);
        mediaPlayer.start();
        getSupportActionBar().hide();
        Animation avt = AnimationUtils.loadAnimation(this, R.anim.splash_anim_img);
        Animation avttext2 = AnimationUtils.loadAnimation(this, R.anim.splast_text2);
        ImageView avatarimg = this.findViewById(R.id.avatar);
        TextView texttesst2 = findViewById(R.id.text2);
        Animation avttext = AnimationUtils.loadAnimation(AnimationLoading.this, R.anim.splast_text);
        TextView texttesst = findViewById(R.id.text);
        ImageView texttesst3 = findViewById(R.id.text3);

        avatarimg.setAnimation(avt);
        avt.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                texttesst.setAnimation(avttext);
                texttesst3.setAnimation(avttext2);
                texttesst2.setAnimation(avttext2);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        avttext2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                texttesst.setVisibility(View.VISIBLE);
                texttesst3.setVisibility(View.VISIBLE);
                texttesst2.setVisibility(View.VISIBLE);

                Intent intent = new Intent(AnimationLoading.this, IntroLoadingActivity.class);
                startActivity(intent);
                finishAffinity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }
}