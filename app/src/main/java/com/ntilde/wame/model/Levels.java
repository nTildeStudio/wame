package com.ntilde.wame.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 0011576 on 27/05/2015.
 */
public class Levels implements Serializable{

    private List<Level> levels;

    public List<Level> getLevels(){
        return levels;
    }

    public void setLevels(List<Level> levels){
        this.levels=levels;
    }

    public Level getLevel(int level){
        if(levels!=null&&levels.size()>level){
            return levels.get(level);
        }
        return null;
    }

}
