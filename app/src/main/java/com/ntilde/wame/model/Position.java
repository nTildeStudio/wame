package com.ntilde.wame.model;

import java.io.Serializable;

public class Position implements Serializable{

    public static final int DONT_TOUCHED = -1;

    private int order;
    private int size;
    private int x;
    private int y;
    private float speed;
    private int screenWidth;
    private int screenHeight;
    private int touchedBy = DONT_TOUCHED; //Indicates if touched by any finger
    private boolean completed = false; //Indicates if has been finished

    public int getX(){
        return x;
    }

    public int getPercentageX(){
        return x*screenWidth/100;
    }

    public void setX(int x){
        this.x=x;
    }

    public int getY(){
        return x;
    }

    public void setY(int y){
        this.y=y;
    }

    public int getPercentageY(){
        return y*screenHeight/100;
    }

    public int getOrder(){
        return order;
    }

    public void setOrder(int order){
        this.order=order;
    }

    public void setScreenDimensions(int width, int height){
        screenWidth=width;
        screenHeight=height;
    }

    public void setSize(int size){
        this.size=size;
    }

    public int getSize(){
        return size;
    }

    public int getPercentageSize(){
        return size*screenWidth/100;
    }

    public void setTouchedBy(int id){
        touchedBy = id;
    }

    public int getTouchedBy(){
        return touchedBy;
    }

    public boolean isTouched(){
        return touchedBy != DONT_TOUCHED;
    }

    public void setCompleted (boolean completed){
        this.completed = completed;
    }

    public boolean isCompleted(){
        return completed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void restart() {
        completed = false;
        touchedBy = DONT_TOUCHED;
    }
}
