package com.ntilde.wame.model;

import java.io.Serializable;
import java.util.List;

public class Level implements Serializable{

    private List<Position> positions;

    public void setPositions(List<Position> positions){
        this.positions=positions;
    }

    public List<Position> getPositions(){
        return positions;
    }

    public void setScreenDimensions(int width, int height){
        if(positions!=null){
            for(Position pos:positions){
                pos.setScreenDimensions(width,height);
            }
        }
    }

}
