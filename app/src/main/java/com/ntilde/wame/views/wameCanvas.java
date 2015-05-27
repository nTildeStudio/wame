package com.ntilde.wame.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ntilde.wame.model.Level;
import com.ntilde.wame.model.Position;


public class wameCanvas extends View{

    private Context context;

    private Canvas canvas;
    private Paint textPaint;

    private boolean firstExecution=true;

    private Level level;

    public wameCanvas(Context context) {
        super(context);
        this.context = context;
    }

    public wameCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initCanvas(canvas);

        drawTargets();
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

}
