package ch.wertal.pongland;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.ColorLong;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import static android.widget.Toast.*;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener, Runnable{

    Paint  paint;
    Paddle paddle;
    public final static int LEFT   = -1;
    public final static int RIGHT  =  1;
    public final static int STOP   =  0;
    private int direction = LEFT ;
    static boolean  hunterMode = false ;
    static boolean  doHunt     = false ;
    private Thread gameThread = null ;
    private boolean mPlaying = true ;
    private Canvas mCanvas ;
    GameLoop gameLoop = null ;
    private int x      = 0;
    private int xSpeed = 5;
    Bitmap origBmp     = null ;
    Bitmap bmp         = null ;
    private SurfaceHolder surfaceHolder ;
    private boolean isPlaying = true ;
    private Context ctx ;


    public GameView(Context context) {
        super(context);
        doSetup(context);
        gameThread.start();
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        doSetup(context);
        gameThread.start();
    }

    private void updateGame(SurfaceHolder surfaceHolder){

              if (surfaceHolder.getSurface().isValid()) {
                  try {
                      mCanvas = surfaceHolder.lockCanvas(null);

                      synchronized (this) {
                          mCanvas.drawRect(paddle.getRectF(), paddle.getPaint());

                          hunterMode = false;
                          if (paddle.isLeftcorner()) {
                              direction = RIGHT;
                              if (doHunt) {
                                  hunterMode = true;
                              }
                              paddle.setLeftcorner(false);
                          }
                          if (paddle.isRightcorner()) {
                              direction = LEFT;
                              if (doHunt) {
                                  hunterMode = true;
                              }
                              paddle.setRightcorner(false);
                          }
                          Thread.sleep(100);
                          paddle.setMovingState(direction);
                          paddle.update(22);
                      }
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  } finally {
                      if (mCanvas != null)
                          surfaceHolder.unlockCanvasAndPost(mCanvas);
                  }
              }

          }

    @Override
    public void run() {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
         updateGame(holder);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        updateGame(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void doSetup(Context context) {
        gameThread = new Thread(this) ;
        paddle = new Paddle(context);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10);
        this.setBackgroundColor(Color.RED);
        this.ctx = context ;
        this.setBackground(ContextCompat.getDrawable(context, R.drawable.rainbg));

        origBmp = BitmapFactory.decodeResource(getResources(),R.drawable.ball) ;
        bmp  = Bitmap.createScaledBitmap(
                origBmp, 200, 200, false);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("TAG", "touched down");
                paddle.setMovingState(LEFT);
                paddle.update(20);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("TAG", "moving: (" + x + ", " + y + ")");
                Toast.makeText(getContext(), "move", LENGTH_SHORT).show();
                break;
            case MotionEvent.ACTION_UP:
                Log.i("TAG", "touched up");
                Toast.makeText(getContext(), "up", LENGTH_SHORT).show();
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }
}
