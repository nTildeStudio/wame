package com.ntilde.wame.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ntilde.wame.GameActivity;
import com.ntilde.wame.model.Level;
import com.ntilde.wame.model.Position;

import java.util.ArrayList;
import java.util.Calendar;


public class wameCanvas extends View{

    private Context context;

    private Canvas canvas;
    private Paint textPaint;

    private int actualOrder;
    private boolean gameOver;
    private boolean gameCompleted;
    private long startedTime;

    private boolean firstExecution=true;

    private Level level;
    private ArrayList<TouchedPoint> touchedPoints;
    private int touchedPointsCount;

    public wameCanvas(Context context) {
        super(context);
        this.context = context;
        start();
    }

    public wameCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        start();
    }

    public void start(){
        this.touchedPoints = new ArrayList<>();
        this.touchedPointsCount = 0;
        this.startedTime = Calendar.getInstance().getTimeInMillis();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initCanvas(canvas);

        drawTargets();
        drawTouchedPoints();
        if(level.getTime() != Level.NO_TIME && !gameOver && !gameCompleted) drawTime();
    }

    public void initCanvas(Canvas canvas){
        this.canvas = canvas;
        this.canvas.drawColor(android.R.color.transparent);
        if (firstExecution){
            initPaints();
            drawTargets();
            firstExecution = false;
        }
        level.setScreenDimensions(getWidth(),getHeight());
    }

    private void initPaints() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(20);
    }

    public void setLevel(Level level){
        this.level=level;
        this.actualOrder = 1;
        this.gameOver = false;
        this.gameCompleted = false;
        restartTargets();
    }

    private void drawTime(){
        long diference = level.getTime() - (Calendar.getInstance().getTimeInMillis() - startedTime) / 1000;
        Paint timePaint = new Paint();
        timePaint.setTypeface(Typeface.createFromAsset(context.getAssets(), "welbut.ttf"));
        timePaint.setTextSize(40);
        timePaint.setColor(Color.BLACK);
        timePaint.setTextAlign(Paint.Align.RIGHT);
        String text = String.valueOf(diference);
        canvas.drawText(text, (float) 0.9 * getWidth(), (float) 0.1 * getHeight(), timePaint);
        if(diference == 0){
            gameOver();
        }else {
            postInvalidateDelayed(100);
        }
    }

    private void drawTargets(){
        Paint targetPaintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint targetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint targetPaintTouched = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint orderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        targetPaintBackground.setStyle(Paint.Style.FILL_AND_STROKE);
        targetPaint.setStyle(Paint.Style.STROKE);
        targetPaintTouched.setStyle(Paint.Style.FILL_AND_STROKE);
        orderPaint.setTextAlign(Paint.Align.CENTER);

        targetPaintBackground.setColor(Color.WHITE);

        for(Position target:level.getPositions()){
            if(target.getOrder()<actualOrder) continue;
            targetPaint.setStrokeWidth(getHeight()*0.007f);
            targetPaintTouched.setStrokeWidth(getHeight()*0.01f);
            orderPaint.setTextSize(target.getPercentageSize()*0.6f);
            int color=context.getResources().getColor(getResources().getIdentifier("targetColor"+target.getOrder(), "color", context.getPackageName()));
            targetPaint.setColor(color);
            targetPaintTouched.setColor(color);
            targetPaintTouched.setAlpha(100);
            orderPaint.setColor(color);

            canvas.drawCircle(target.getPercentageX(), target.getPercentageY(), target.getPercentageSize(), targetPaintBackground);
            canvas.drawCircle(target.getPercentageX(), target.getPercentageY(), target.getPercentageSize(), target.isTouched() ? targetPaintTouched : targetPaint);
            canvas.drawText(target.getOrder()+"",target.getPercentageX(),target.getPercentageY()+target.getSize()*2f,orderPaint);
        }
    }

    private void drawTouchedPoints(){
        final Paint touchedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        touchedPaint.setStyle(Paint.Style.STROKE);
        touchedPaint.setStrokeWidth(15);

        int painted = 0;

        for (int i=0; i<touchedPoints.size(); i++){
            TouchedPoint point = touchedPoints.get(i);

            long timeNow = Calendar.getInstance().getTimeInMillis();
            long radius = (timeNow - point.timestamp) / 5;

            double diagonal = Math.hypot(Math.max(point.x, getWidth()-point.x), Math.max(point.y, getHeight()-point.y));

            if(radius < diagonal){
                painted++;
                touchedPaint.setColor(getColorOfOrder(actualOrder + i));
                canvas.drawCircle(point.x, point.y, radius, touchedPaint);

                int completedPoints = 0;
                for(Position target:level.getPositions()){
                    if(circleCollision(target.getPercentageX(), target.getPercentageY(), target.getPercentageSize(), point.x, point.y, radius) && !target.isCompleted()){
                        if(actualOrder != target.getOrder()){
                            gameOver();
                        }else{
                            target.setTouchedBy(point.touchedBy);
                            completedPoints++;
                        }
                    }else{
                        target.setTouchedBy(Position.DONT_TOUCHED);
                    }
                }

                if (completedPoints == getOrderPointsCount(actualOrder)) {
                    removeOrderPoints(actualOrder);
                    touchedPoints.remove(point);
                    i--;
                    actualOrder++;
                    if (actualOrder > getMaxOrder()) {
                        gameCompleted();
                    }
                }
            }else{
                gameOver();
            }
        }

        if (painted > 0 && !gameOver && !gameCompleted) {
            postInvalidateDelayed(10);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(gameOver || gameCompleted) return false;

        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);

        TouchedPoint f = new TouchedPoint();
        f.x = event.getX(pointerIndex);
        f.y = event.getY(pointerIndex);
        f.timestamp = Calendar.getInstance().getTimeInMillis();
        f.touchedBy = pointerId;

        int maskedAction = event.getActionMasked();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {

                break;
            }

            case MotionEvent.ACTION_MOVE: {

                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                if(touchedPointsCount < getMaxOrder()){
                    touchedPointsCount++;
                    touchedPoints.add(f);
                }
                break;
            }
        }

        invalidate();

        return true;
    }

    /**
     * Remove order points of a order
     */
    private void removeOrderPoints(int order){
        for (Position target : level.getPositions()){
            if (target.getOrder() == order){
                target.setCompleted(true);
            }
        }
    }

    /**
     * Returns number of points of a order
     */
    private int getOrderPointsCount(int order){
        int count = 0;
        for (Position target : level.getPositions()){
            if (target.getOrder() == order){
                count++;
            }
        }
        return count;
    }

    /**
     * Returns color of n order
     */
    private int getColorOfOrder(int order){
        int color = Color.BLACK;
        for (Position target : level.getPositions()){
            if (target.getOrder() == order){
                return context.getResources().getColor(getResources().getIdentifier("targetColor"+target.getOrder(), "color", context.getPackageName()));
            }
        }
        return color;
    }

    /**
     * Returns color of minimum order printable point
     * @return
     */
    private int getColorOfMinOrder(){
        int color = Color.BLACK;
        int min = 50;
        for (Position target : level.getPositions()){
            if (target.getOrder() < min && !target.isCompleted()){
                min = target.getOrder();
                color = context.getResources().getColor(getResources().getIdentifier("targetColor" + target.getOrder(), "color", context.getPackageName()));
            }
        }
        return color;
    }

    /**
     * Return higgest order
     */
    private int getMaxOrder(){
        int max = 0;
        for (Position target : level.getPositions()){
            if (target.getOrder() > max){
                max = target.getOrder();
            }
        }
        return max;
    }

    /**
     *
     * @param x1
     * @param y1
     * @param radius1
     * @param x2
     * @param y2
     * @param radius2
     * @return collision
     */
    private boolean circleCollision(float x1, float y1, float radius1, float x2, float y2, float radius2){
        double xDif = x1 - x2;
        double yDif = y1 - y2;

        double distance = Math.hypot(xDif, yDif);
        boolean collision = (distance < radius1 + radius2) && (radius2 < distance + radius1);
        return collision;
    }

    /**
     * All targets isn't complete and isn't touched by not
     */
    private void restartTargets(){
        for (Position target : level.getPositions()){
            target.restart();
        }
    }

    /**
     * GAME OVER
     */
    private void gameOver(){
        gameOver = true;
        touchedPoints = new ArrayList<>();
        ((GameActivity) context).showLose();
    }

    /**
     * GAME COMPLETED
     */
    private void gameCompleted(){
        gameCompleted = true;
        drawTargets();
        touchedPoints = new ArrayList<>();
        ((GameActivity) context).showWin();
    }

    /**
     * Model to touched point
     */
    class TouchedPoint{
        float x;
        float y;
        long timestamp;
        int touchedBy;
    }

}
