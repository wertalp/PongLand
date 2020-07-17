package ch.wertal.pongland;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.Display;

public class Paddle {

    private float posX ;
    private float posY ;
    private float dispWith;
    private float dispHeigth;
    private RectF rectF;
    private float PADDLEHIGH = 40 ;
    private float PADDLEWIDTH = 250;
    private Context c1;
    private float marginBootom = 400f;
    private Paint paint;
    private float paddleSpeed;
    private int   paddleMoving;
    private boolean leftcorner  = false ;
    private boolean rightcorner = false ;

    public Paddle(Context context) {
        c1 = context;
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        calcPaddle();
        rectF = new RectF(posX,posY,posX+PADDLEWIDTH,posY+PADDLEHIGH);
        //rectF = new RectF(800,1000,800+PADDLEWIDTH,1000+PADDLEHIGH);
    }

    private void calcPaddle(){
        DisplayMetrics metrics = c1.getResources().getDisplayMetrics();
        dispWith    = metrics.widthPixels;
        dispHeigth  = metrics.heightPixels;
        paddleSpeed = dispWith ;
        posX = dispWith/2 ;
        posY = dispHeigth- marginBootom;
    }

    public void setMovingState(int moveState){
       this.paddleMoving = moveState;

    }

    public void update(long fps){
        float xcoord = rectF.left;

      switch (paddleMoving){

          case GameView.LEFT:
              xcoord = xcoord-6;
              break;
          case GameView.RIGHT:
              xcoord= xcoord+6;
              break;
           case GameView.STOP:
               xcoord = xcoord ;
               break;
      }
      // position out of frame LEFT
       if (xcoord < 0){
           leftcorner= true;
           return;
       }
       // position out of frame Right
       if (xcoord > dispWith-PADDLEWIDTH){
           rightcorner= true;
           return;
       }
       if (GameView.hunterMode){
           rectF.top = rectF.top-60;
           rectF.bottom = rectF.bottom-60;
       }

       rectF.left = xcoord;
       rectF.right = xcoord+ PADDLEWIDTH;

    }


    public RectF getRectF() {
        return rectF;
    }

    public Context getC1() {
        return c1;
    }

    public void setC1(Context c1) {
        this.c1 = c1;
    }

    public Paint getPaint() {
        return paint;
    }

    public boolean isLeftcorner() {
        return leftcorner;
    }

    public boolean isRightcorner() {
        return rightcorner;
    }

    public void setLeftcorner(boolean leftcorner) {
        this.leftcorner = leftcorner;
    }

    public void setRightcorner(boolean rightcorner) {
        this.rightcorner = rightcorner;
    }
}
