package com.ntilde.wame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
    private boolean sound = true;

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
        findViewById(R.id.home_ranking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.home_points).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.home_share_google_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String shareBody = "https://play.google.com/store/apps/details?id=com.ntilde.wame";

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Wame");

                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.home_share_google_play)));
            }
        });
        findViewById(R.id.home_rate_google_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.ntilde.wame"));
                startActivity(intent);
            }
        });
        findViewById(R.id.home_sound).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound){
                    //mute audio
                    AudioManager  amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
                    amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
                    amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
                    amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                    amanager.setStreamMute(AudioManager.STREAM_RING, true);
                    amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
                    ((ImageView)findViewById(R.id.home_sound)).setImageDrawable(getResources().getDrawable(R.drawable.home_sound_off));
                    sound = false;
                }else{
                    //unmute audio
                    AudioManager amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
                    amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
                    amanager.setStreamMute(AudioManager.STREAM_ALARM, false);
                    amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                    amanager.setStreamMute(AudioManager.STREAM_RING, false);
                    amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
                    ((ImageView)findViewById(R.id.home_sound)).setImageDrawable(getResources().getDrawable(R.drawable.home_sound_on));
                    sound = true;
                }
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
