package com.ntilde.wame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.google.gson.Gson;
import com.ntilde.wame.model.Levels;

import java.io.IOException;
import java.io.InputStream;


public class HomeActivity extends ActionBarActivity {

    protected static int nextLevel=0;
    protected static Levels levels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.home_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextLevel=0;
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, GameActivity.class));
            }
        });

        levels = loadLevels();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextLevel=1;
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, GameActivity.class));
            }
        });
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

}
