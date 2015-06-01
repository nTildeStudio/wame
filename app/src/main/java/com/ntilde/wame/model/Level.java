package com.ntilde.wame.model;

import java.io.Serializable;
import java.util.List;

public class Level implements Serializable{

    public static final int NO_TIME = -1;

    private List<Position> positions;
    private int time;
    private List<Integer> speed;

    public void setPositions(List<Position> positions){
        this.positions=positions;
    }

    public List<Position> getPositions(){
        return positions;
    }

    public void setTime(int time){
        this.time = time;
    }

    public int getTime(){
        return time;
    }

    public void setSpeed(List<Integer> speed){
        this.speed = speed;
    }

    public List<Integer> getSpeed(){
        return speed;
    }

    public void setScreenDimensions(int width, int height){
        if(positions!=null){
            for(Position pos:positions){
                pos.setScreenDimensions(width,height);
            }
        }
    }

}
