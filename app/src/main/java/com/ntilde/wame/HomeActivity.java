package com.ntilde.wame;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ntilde.wame.model.Levels;
import com.ntilde.wame.views.PTextView;

import java.io.IOException;
import java.io.InputStream;


public class HomeActivity extends ActionBarActivity {

    public static int nextLevel=0;
    protected static int maxLevel=6;
    protected static Levels levels;
    private MediaPlayer music;

    @Override
    protected void onResume() {
        super.onResume();

        music = MediaPlayer.create(this, R.raw.arcade);
        music.setLooping(true);
        music.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        levels = loadLevels();

        nextLevel=maxLevel;
        ((TextView)findViewById(R.id.home_level_selected)).setText("Level "+(HomeActivity.maxLevel+1));

        findViewById(R.id.home_level_selector_spinner).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                findViewById(R.id.home_level_selector_block).setVisibility(View.VISIBLE);
                LinearLayout selector=(LinearLayout)findViewById(R.id.home_level_selector);
                selector.removeAllViews();

                DisplayMetrics displayMetrics = HomeActivity.this.getResources().getDisplayMetrics();
                float dpHeight = displayMetrics.heightPixels / displayMetrics.density;

                for(int i=0;i<levels.getLevels().size();i++){
                    LinearLayout ll=new LinearLayout(HomeActivity.this);
                    ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int)dpHeight/7));
                    ll.setBackgroundColor(i % 2 == 0 ? Color.WHITE : Color.LTGRAY);
                    ll.setGravity(Gravity.CENTER);
                    PTextView tv=new PTextView(getApplicationContext());
                    tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
                    tv.setText("Level " + (i + 1));
                    tv.setTextColor(i <= maxLevel ? Color.BLACK : Color.GRAY);
                    tv.setPHeight(3f);
                    tv.setGravity(Gravity.CENTER);
                    tv.setAssetFont("welbut.ttf");
                    ll.addView(tv);
                    if(i<=maxLevel) {
                        ll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                HomeActivity.nextLevel = Integer.parseInt(((TextView) ((ViewGroup) v).getChildAt(0)).getText().toString().split(" ")[1]) - 1;
                                findViewById(R.id.home_level_selector_block).setVisibility(View.GONE);
                                ((TextView)findViewById(R.id.home_level_selected)).setText("Level "+(HomeActivity.nextLevel+1));
                            }
                        });
                    }
                    selector.addView(ll);
                }
            }
        });

        Animation fadeInAnimation2 = AnimationUtils.loadAnimation(this, R.anim.logo_pulse);
        findViewById(R.id.home_play_icon).startAnimation(fadeInAnimation2);

        findViewById(R.id.home_level_selector_block).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.home_play_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, GameActivity.class));
            }
        });

        findViewById(R.id.home_play_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, GameActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        View level_block=findViewById(R.id.home_level_selector_block);
        if(level_block.getVisibility()==View.VISIBLE){
            level_block.setVisibility(View.GONE);
        }
        else{
            super.onBackPressed();
        }
    }

    public Levels loadLevels() {
        String json = null;
        try {
            InputStream is = getAssets().open("levels.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return new Gson().fromJson(json,Levels.class);
    }

    @Override
    protected void onPause() {
        super.onPause();
        music.stop();
    }
}
