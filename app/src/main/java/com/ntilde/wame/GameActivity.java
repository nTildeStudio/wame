package com.ntilde.wame;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.ntilde.wame.model.Level;
import com.ntilde.wame.views.wameCanvas;


public class GameActivity extends ActionBarActivity {

    private wameCanvas board;

    private Level level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        level=HomeActivity.levels.getLevel(HomeActivity.nextLevel);

        board=(wameCanvas)findViewById(R.id.game_board);
        board.setLevel(level);
    }

}
