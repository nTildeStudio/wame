package com.ntilde.wame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class SplashActivity extends ActionBarActivity{

    private ImageView colorLogo;
    private ImageView whiteLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        colorLogo = (ImageView) findViewById(R.id.splash_color_logo);
    }

    @Override
    protected void onResume(){
        super.onResume();

        whiteLogo = (ImageView) findViewById(R.id.splash_white_logo);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_fade_in);
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                colorLogo.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation fadeInAnimation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_fade_in);
                fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                colorLogo.setVisibility(View.VISIBLE);
                colorLogo.startAnimation(fadeInAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        whiteLogo.startAnimation(fadeInAnimation);
    }
}
