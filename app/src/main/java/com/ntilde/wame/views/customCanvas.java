package com.ntilde.wame.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 0011576 on 27/05/2015.
 */
public class customCanvas extends View {

    public static int NBALLS = 5;
    public static int WITHOUTPOINTER = -1;

    private SparseArray<PointF> mActivePointers;

    private Ball[] balls;
    private int[] xPositions;
    private int[] yPositions;
    private Paint canvasPaint;
    private Paint textPaint;
    private Paint bigTextPaint;

    private int screenWidth;
    private int screenHeight;

    private float touchedX;
    private float touchedY;

    private Canvas canvas = null;
    private Context context;

    private boolean firstExecution = true;
    private int activeFingers = 0;

    public customCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void initCanvas(Canvas canvas){
        this.canvas = canvas;
        this.canvas.drawColor(Color.parseColor("#DDDDDD"));
        if (firstExecution){
            screenWidth = getWidth();
            screenHeight = getHeight();
            initPaints();
            initXvalues();
            initBalls();
            firstExecution = false;
        }
    }

    private void initPaints() {
        mActivePointers = new SparseArray<PointF>();
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(20);
        bigTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bigTextPaint.setTextSize(50);

        canvasPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvasPaint.setColor(Color.parseColor("#DDDDDD"));
    }

    private void initXvalues(){
        xPositions = new int[NBALLS];
        yPositions = new int[NBALLS];

        //For margin
        int interval = screenWidth / (NBALLS + 1);
        int currentX = interval;
        for (int i=0; i<NBALLS; i++){
            xPositions[i] = currentX;
            currentX += interval;
        }

        interval = screenHeight / (NBALLS + 1);
        int currentY = interval;
        for (int i=0; i<NBALLS; i++){
            yPositions[i] = currentY;
            currentY += interval;
        }
    }

    private void initBalls(){
        balls = new Ball[NBALLS];
        Paint activePaint;
        Paint inactivePaint;
        Paint destinyPaint;

        //RED BALL
        activePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        inactivePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        destinyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        activePaint.setColor(Color.parseColor("#CC0000"));
        inactivePaint.setColor(Color.parseColor("#FF4444"));
        destinyPaint.setColor(Color.parseColor("#FF9494"));
        balls[0] = new Ball(xPositions[4], 250, xPositions[3], yPositions[4], screenWidth/15, activePaint, inactivePaint, destinyPaint);

        //YELLOW BALL
        activePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        inactivePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        destinyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        activePaint.setColor(Color.parseColor("#FF8800"));
        inactivePaint.setColor(Color.parseColor("#FFBB33"));
        destinyPaint.setColor(Color.parseColor("#FFD060"));
        balls[1] = new Ball(xPositions[3], 250, xPositions[1], yPositions[2], screenWidth/15, activePaint, inactivePaint, destinyPaint);

        //GREEN BALL
        activePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        inactivePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        destinyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        activePaint.setColor(Color.parseColor("#669900"));
        inactivePaint.setColor(Color.parseColor("#99CC00"));
        destinyPaint.setColor(Color.parseColor("#B6DB49"));
        balls[2] = new Ball(xPositions[2], 250, xPositions[4], yPositions[2], screenWidth/15, activePaint, inactivePaint, destinyPaint);

        //PURPLE BALL
        activePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        inactivePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        destinyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        activePaint.setColor(Color.parseColor("#9933CC"));
        inactivePaint.setColor(Color.parseColor("#AA66CC"));
        destinyPaint.setColor(Color.parseColor("#CF9FE7"));
        balls[3] = new Ball(xPositions[1], 250, xPositions[2], yPositions[1], screenWidth/15, activePaint, inactivePaint, destinyPaint);

        //BLUE BALL
        activePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        inactivePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        destinyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        activePaint.setColor(Color.parseColor("#0099CC"));
        inactivePaint.setColor(Color.parseColor("#33B5E5"));
        destinyPaint.setColor(Color.parseColor("#6DCAEC"));
        balls[4] = new Ball(xPositions[0], 250, xPositions[0], yPositions[3], screenWidth/15, activePaint, inactivePaint, destinyPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        activeFingers = event.getPointerCount();

        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                // We have a new pointer. Lets add it to the list of pointers

                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);

                touchedX = f.x;
                touchedY = f.y;

                for (int i=0; i<balls.length; i++){
                    if (event.getX(pointerIndex) > balls[i].initX - balls[i].diameter / 2 &&
                            event.getX(pointerIndex) < balls[i].initX + balls[i].diameter / 2 &&
                            event.getY(pointerIndex) > balls[i].initY - balls[i].diameter / 2 &&
                            event.getY(pointerIndex) < balls[i].initY + balls[i].diameter / 2){

                        balls[i].active = true;
                        balls[i].pointer = event.getPointerId(pointerIndex);

                        //touched = true;
                        //mActivePointers.put(pointerId, f);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
//            for (int size = event.getPointerCount(), i = 0; i < size; i++) {
//                PointF point = mActivePointers.get(event.getPointerId(i));
//                if (point != null) {
//                    point.x = event.getX(i);
//                    point.y = event.getY(i);
//                }
//            }

                for (int i=0; i<balls.length; i++){
                    for (int j=0; j<event.getPointerCount(); j++){
                        if (event.getX(j) > balls[i].initX - balls[i].diameter / 2 &&
                                event.getX(j) < balls[i].initX + balls[i].diameter / 2 &&
                                event.getY(j) > balls[i].initY - balls[i].diameter / 2 &&
                                event.getY(j) < balls[i].initY + balls[i].diameter / 2){

                            balls[i].active = true;
                            balls[i].pointer = event.getPointerId(j);

                            break;
                        }
                    }
                }

                for (int i=0; i<balls.length; i++){
                    try{
                        if (balls[i].active && balls[i].pointer != WITHOUTPOINTER){
                            balls[i].currentX = event.getX(balls[i].pointer);
                            balls[i].currentY = event.getY(balls[i].pointer);
                            if (balls[i].isOnDestiny()){
                                Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(50);
                            }
                        }
                    }catch(Exception e){

                    }
                }

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                mActivePointers.remove(pointerId);

                for (int i=0; i<balls.length; i++){
                    if (balls[i].pointer == event.getPointerId(pointerIndex)){
                        balls[i].pointerUpBall();
                        balls[i].pointer = WITHOUTPOINTER;
                    }
                }


                break;
            }
        }
        invalidate();

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initCanvas(canvas);
//        this.canvas = canvas;
//        canvas.drawColor(Color.parseColor("#DDDDDD"));

//        if (touched){
//            ball.paint = activePaint;
//        }else{
//            ball.paint = inactivePaint;
//        }

//        if (!touched){
//            canvas.drawCircle(ball.x, ball.y, ball.diameter, ball.paint);
//        }

        // draw all pointers
//        for (int size = balls.length, i = 0; i < size; i++){
//            canvas.drawCircle(balls[i].initX, balls[i].initY, ball.diameter, ball.paint);
//        }

        //Paint destiny balls
        for (int i=0; i<balls.length; i++){
            canvas.drawCircle(balls[i].finalX, balls[i].finalY, balls[i].diameter, balls[i].destinyPaint);
            canvas.drawCircle(balls[i].finalX, balls[i].finalY, balls[i].diameter - 10, canvasPaint);
            canvas.drawCircle(balls[i].finalX, balls[i].finalY, balls[i].diameter - 70, balls[i].destinyPaint);
        }

        //Paint current balls
        for (int i=0; i<balls.length; i++){
            canvas.drawCircle(balls[i].getPrintableX(), balls[i].getPrintableY(), balls[i].diameter, balls[i].getCurrentPaint());
        }

//        for (int size = mActivePointers.size(), i = 0; i < size; i++) {
//            PointF point = mActivePointers.valueAt(i);
//            balls[i].initX = point.x;
//            balls[i].initY = point.y;
//            //if (point != null)
//            //    mPaint.setColor(colors[i % 9]);
//            canvas.drawCircle(balls[i].initX, balls[i].initY, ball.diameter, ball.paint);
//        }

//        float leftX = ball.x - ball.diameter /2;
//        float rightX = ball.x + ball.diameter /2;
//        float topY = ball.y - ball.diameter /2;
//        float bottomY = ball.y + ball.diameter /2;



        canvas.drawText("Active Balls: " + getActiveBalls() + "Pointers: " + activeFingers, 10, 40 , textPaint);
        canvas.drawText("Width: " + screenWidth + " Height: " + screenHeight, 10, 70, textPaint);
        canvas.drawText("Touched x: " + touchedX + "Touched y: " + touchedY, 10, 100, textPaint);
        canvas.drawText("Touched: " + balls[0].active, 10, 130, textPaint);
        canvas.drawText(getCompletedBalls(), screenWidth - 30, 50, bigTextPaint);
    }


    private int getActiveBalls(){
        int count = 0;
        for (int i=0; i<balls.length; i++){
            if (balls[i].active){
                count++;
            }
        }
        return count;
    }

    private String getCompletedBalls(){
        int count = 0;
        for (int i=0; i<balls.length; i++){
            if (balls[i].isOnDestiny()){
                count++;
            }
        }
        return ""+count;
    }

    private class Ball{

        private float initX;
        private float initY;
        private float currentX;
        private float currentY;
        private float finalX;
        private float finalY;

        private int pointer = WITHOUTPOINTER;

        private float diameter;
        private Paint activePaint;
        private Paint inactivePaint;
        private Paint destinyPaint;
        private boolean active;

        public Ball(float init_x, float init_y, float final_x, float final_y, float diameter, Paint activePaint, Paint inactivePaint, Paint destinyPaint){
            this.initX = init_x;
            this.initY = init_y;
            this.currentX = init_x;
            this.currentY = init_y;
            this.finalX = final_x;
            this.finalY = final_y;
            this.diameter = diameter;
            this.activePaint = activePaint;
            this.inactivePaint = inactivePaint;
            this.destinyPaint = destinyPaint;
            this.active = false;
        }

        public Paint getCurrentPaint(){
            if (active){
                return activePaint;
            }else{
                return inactivePaint;
            }
        }

        public float getPrintableX(){
            if (active){
                return currentX;
            }else{
                return initX;
            }
        }

        public float getPrintableY(){
            if (active){
                return currentY;
            }else{
                return initY;
            }
        }

        public boolean isOnDestiny(){
            if (currentX > finalX - diameter / 2 &&
                    currentX < finalX + diameter / 2 &&
                    currentY > finalY - diameter / 2 &&
                    currentY < finalY + diameter / 2){
                return true;
            }else{
                return false;
            }
        }

        public void pointerUpBall(){
            active = false;
            currentX = initX;
            currentY = initY;
        }

    }

}
