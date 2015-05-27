package com.ntilde.wame.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ntilde.wame.model.Level;
import com.ntilde.wame.model.Position;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class wameCanvas extends View{

    private Context context;

    private Canvas canvas;
    private Paint textPaint;

    private boolean firstExecution=true;

    private Level level;
    private ArrayList<TouchedPoint> touchedPoints;

    public wameCanvas(Context context) {
        super(context);
        this.context = context;
    }

    public wameCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.touchedPoints = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initCanvas(canvas);

        Log.i("XXX", "onDraw");
        drawTargets();
        drawTouchedPoints();
    }

    public void initCanvas(Canvas canvas){
        this.canvas = canvas;
        this.canvas.drawColor(Color.parseColor("#ffffff"));
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
    }

    private void drawTargets(){
        Paint targetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint orderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        targetPaint.setStyle(Paint.Style.STROKE);
        targetPaint.setStrokeWidth(level.getPositions().get(0).getSize()*0.4f);
        orderPaint.setTextSize(level.getPositions().get(0).getSize()*4f);
        orderPaint.setTextAlign(Paint.Align.CENTER);
        for(Position target:level.getPositions()){
            targetPaint.setColor(Color.parseColor(target.getColor()));
            orderPaint.setColor(Color.parseColor(target.getColor()));
            canvas.drawCircle(target.getPercentageX(), target.getPercentageY(), target.getPercentageSize(), targetPaint);
            canvas.drawText(target.getOrder()+"",target.getPercentageX(),target.getPercentageY()+target.getSize()*2f,orderPaint);
        }
    }

    private void drawTouchedPoints(){
        final Paint touchedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        touchedPaint.setStyle(Paint.Style.STROKE);
        touchedPaint.setStrokeWidth(15);
        touchedPaint.setColor(Color.GREEN);

        for(final TouchedPoint point : touchedPoints){
            long time = Calendar.getInstance().getTimeInMillis();
            long diameter = 100 * (time - point.timestamp) / 1000;

            double diagonal = Math.hypot(getWidth(), getHeight());

            if(diameter < diagonal){
                canvas.drawCircle(point.x, point.y, diameter, touchedPaint);
            }else{
                Log.i("XXX", "Se pasa!");
            }

            postInvalidateDelayed(10);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.i("XXX", "Toco");

        int pointerIndex = event.getActionIndex();

        TouchedPoint f = new TouchedPoint();
        f.x = event.getX(pointerIndex);
        f.y = event.getY(pointerIndex);
        f.timestamp = Calendar.getInstance().getTimeInMillis();

        int pointerId = event.getPointerId(pointerIndex);

        int maskedAction = event.getActionMasked();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {

                break;
            }

            case MotionEvent.ACTION_MOVE: { // a pointer was moved

                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                touchedPoints.add(f);
                break;
            }
        }

        invalidate();

        return true;
    }


    class TouchedPoint{
        float x;
        float y;
        long timestamp;
    }

}
