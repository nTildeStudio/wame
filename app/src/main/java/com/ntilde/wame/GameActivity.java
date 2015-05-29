package com.ntilde.wame;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.ntilde.wame.model.Level;
import com.ntilde.wame.views.wameCanvas;


public class GameActivity extends ActionBarActivity {

    private wameCanvas board;
    private LinearLayout winBlock;
    private LinearLayout loseBlock;

    private Level level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        loadUI();
        getActualLevel();
    }

    private void loadUI(){
        board=(wameCanvas)findViewById(R.id.game_board);
        winBlock = (LinearLayout) findViewById(R.id.game_win_block);
        loseBlock = (LinearLayout) findViewById(R.id.game_lose_block);
    }

    private void getActualLevel(){
        level=HomeActivity.levels.getLevel(HomeActivity.nextLevel);
        board.setLevel(level);
        board.start();
    }

    public void showWin(){
        winBlock.setVisibility(View.VISIBLE);
        View.OnClickListener winListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.game_win_next:
                        HomeActivity.nextLevel++;
                        getActualLevel();
                        winBlock.setVisibility(View.GONE);
                    break;
                    case R.id.game_win_share:

                    break;
                    case R.id.game_win_quit:
                        finish();
                    break;
                }
            }
        };

        winBlock.findViewById(R.id.game_win_next).setOnClickListener(winListener);
        winBlock.findViewById(R.id.game_win_share).setOnClickListener(winListener);
        winBlock.findViewById(R.id.game_win_quit).setOnClickListener(winListener);
    }

    public void showLose(){
        loseBlock.setVisibility(View.VISIBLE);
        View.OnClickListener loseListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.game_lose_quit:
                        finish();
                        break;
                    case R.id.game_lose_buy:

                        break;
                    case R.id.game_lose_retry:
                        loseBlock.setVisibility(View.GONE);
                        getActualLevel();
                        break;
                }
            }
        };


        loseBlock.findViewById(R.id.game_lose_quit).setOnClickListener(loseListener);
        loseBlock.findViewById(R.id.game_lose_buy).setOnClickListener(loseListener);
        loseBlock.findViewById(R.id.game_lose_retry).setOnClickListener(loseListener);
    }

}
