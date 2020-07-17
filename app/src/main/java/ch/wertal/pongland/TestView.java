package ch.wertal.pongland;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.concurrent.ThreadLocalRandom;

import static ch.wertal.pongland.GameConfig.ANZ_COLS;
import static ch.wertal.pongland.GameConfig.ANZ_ROW;
import static ch.wertal.pongland.GameConfig.BEE_SIZE;
import static ch.wertal.pongland.GameConfig.CANVAS_MARGIN;
import static ch.wertal.pongland.GameConfig.CANVAS_TOP_MARGIN;
import static ch.wertal.pongland.GameConfig.CANVAS_WIDTH;

public class TestView  extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    SurfaceHolder holder ;
    boolean surfaceReady = false ;
    Canvas mCanvas = null ;
    Paddle paddle  = null ;
    Paint  paint   = null ;
    Beatle beatle  = null ;
    Bitmap beatleBmp = null ;
    Beatle[] beatles = new Beatle[10] ;
    boolean gameOn = false ;
    int anzBeatles = 2;
    Thread drawThread ;
    private static final int MAX_FRAME_TIME = (int) (1000.0 / 60.0);
    private String LOG_TAG = "LOG_TESTVIEW";
    private Context appcontext ;
    private int canvasWidth, canvasHight ;
    int borderLEFT = 0;
    Basket basket ;
    MediaPlayer mediaPlayer ;
    SoundPool soundPool ;
    Bee[][] bees = new Bee[ANZ_ROW][GameConfig.ANZ_COLS] ;
    long FRAME_DELAY = 80000 ;
    Animal animal ;
    Bee hornet ;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestView(Context context) {
        super(context);
        paint = stylePaint();
        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSLUCENT);
        holder.addCallback(this);
        //loadBeatles();
        loadrndBeatles();

        paddle = new Paddle(context);
        appcontext = context;
        setZOrderOnTop(true);
        loadMusik();
        bees = loadBees();
        basket = new Basket(appcontext,getBasketBitmap("basket.png",200,200),500,1600);
        //this.setBackground(ContextCompat.getDrawable(appcontext, R.drawable.rainbg));
      //   this.setBackgroundColor(Color.GREEN) ;
        //this.setBackground(ContextCompat.getDrawable(context, R.drawable.dropbg));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context);
        paint = stylePaint();
        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSLUCENT);
        holder.addCallback(this);
        loadrndBeatles();
        paddle = new Paddle(context);
        appcontext = context;
        bees = loadBees();
        setZOrderOnTop(true);
        loadMusik();
        basket = new Basket(appcontext,getBasketBitmap("basket.png",200,200),400,1600);
        //hornet = new Bee(getBeeBitmap("hornet",200,200), 800, 400, appcontext);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceReady = true;
        if (drawThread != null)
        {
            Log.d("THREAD", "draw thread still active..");
            gameOn = false;
            try
            {
              drawThread.join();
            } catch (InterruptedException e)
            {
                // do nothing
            }
        }
        startDrawThread();
        Log.d("THREAD", "started Thread created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        holder.unlockCanvasAndPost(mCanvas);
        mCanvas = holder.lockCanvas(null);
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        holder.getSurface().release();
        this.holder  = null;
        surfaceReady = false;
    }

    private Paint stylePaint(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(80);
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeWidth(40);
        return paint ;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updateGameView() {

        long frameStartTime = System.nanoTime();

        this.requestFocus();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        Log.d("FRAMESTART", "Draw thread started");
        while (gameOn ) {
            if (surfaceReady) {
              long  timeNow = System.currentTimeMillis();
                long l = frameStartTime + FRAME_DELAY - timeNow;
                delta += (timeNow - frameStartTime) / ns;
                frameStartTime = timeNow;
                if (holder == null)
                {
                    return;
                }
               // mCanvas = null;
                try {
                    Thread.sleep(300);
                    mCanvas = holder.lockCanvas(null);
                    canvasHight = mCanvas.getHeight();
                    canvasWidth = mCanvas.getWidth() ;
                    synchronized (holder) {
                        makedraw(mCanvas);
                        if (l > 0L) {
                            try {
                                Thread.sleep(l);
                                Log.d("sleeper ", "makeDraw: "+l);
                            }
                            catch (Exception exception) {
                            }
                        } else {
                            // something long kept us from updating, reset delays
                            frameStartTime = timeNow;
                            l = FRAME_DELAY;
                        }

                        frameStartTime = timeNow + l;
                        // be polite, let others play
                        Thread.yield();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (mCanvas != null) {
                        holder.unlockCanvasAndPost(mCanvas);
                    }
                }
                // calculate the time required to draw the frame in ms

            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void makedraw(Canvas canvas) {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawText("Wertal-Solutions",mCanvas.getWidth()/2,200,paint);
       // canvas.drawRect(paddle.getRectF(),paddle.getPaint());
        canvas.drawBitmap(basket.getBmp(),basket.getPosX(),basket.getPosY(), new Paint());
        drawall(canvas);

    }

    private void drawBees(Canvas canvas) {
    }


    private Bitmap getBitmap(String bmpName, int sizex, int sizey) {

        try {
            beatleBmp = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ladybug) ;
            Matrix matrix = new Matrix();
            matrix.postRotate(180);
            Bitmap rotatedImg = Bitmap.createBitmap(beatleBmp, 0, 0, beatleBmp.getWidth(), beatleBmp.getHeight(), matrix, true);
                return     Bitmap.createScaledBitmap(rotatedImg,sizex,sizey, false);
           } catch (Exception e) {
            e.printStackTrace();
            return null ;
        }
    }

    private Bitmap getBasketBitmap(String bmpName, int sizex, int sizey) {

        try {
            beatleBmp = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.basket) ;
        } catch (Exception e) {
            e.printStackTrace();
            return null ;
        }
        return Bitmap.createScaledBitmap(beatleBmp,sizex,sizey, false);
    }

    private Bitmap getBeeBitmap(String bmpName, int sizex, int sizey) {

        try {
            beatleBmp = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.honeybee) ;
        } catch (Exception e) {
            e.printStackTrace();
            return null ;
        }
        return Bitmap.createScaledBitmap(beatleBmp,sizex,sizey, false);
    }

     private Beatle[] loadBeatles(){
        int i = 0 ;
        int offset = 200;
        int posx   = 100;
        int posy   = 600;

         while (i < anzBeatles ){
             if  ( posx > 800 ){posx = 100 ; posy = posy + 140 ;};
                beatles[i] =  new Beatle(appcontext,getBitmap("someName",120,120),posx,posy);
                posx = posx + offset;
                i++;
         }
        return  beatles;
    }

    private Beatle[] loadrndBeatles(){
        int i = 0 ;
        int offset = 200;

        while (i < anzBeatles ){

            int posx   = (int)(Math.random() * 1000-120) + 50;
            int posy   = (int)(Math.random() * 1800-120) + 50;
            beatles[i] =  new Beatle(appcontext,getBitmap("someName",120,120),posx,posy);
            posx = posx + offset;
            i++;
        }
        return  beatles;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawall(Canvas canvas){

        for ( Bee[] beerow : bees) {

            for (  Bee bee :  beerow){
                canvas.drawBitmap(bee.getBmp(),bee.getPosX(),bee.getPosY(), new Paint());
            }
        }
        for ( Beatle beatle : beatles) {
            int randomDirection = ThreadLocalRandom.current().nextInt(0, 3);
            canvas.drawBitmap(beatle.getBmp(),beatle.getPosX(),beatle.getPosY(), new Paint());
            beatleMove(beatle,randomDirection);
        }

    }

    private void beatleMove(final Beatle beatle, int randomDirection) {

        checkInBorder(beatle);
        paddle.setMovingState(1);
        paddle.update(10);
        switch(randomDirection) {
            case 0:
                try {
                    if (beatle.getDirection().equals("LEFT")){
                       beatle.setBmp(Beatle.rotateImage(beatle.getBmp(),270));
                    }
                    if (beatle.getDirection().equals("RIGHT")){
                        beatle.setBmp(Beatle.rotateImage(beatle.getBmp(),90));
                    }
                   Thread.sleep(1);
                    beatle.moveDown();
                    beatle.setDirection("DOWN");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {

                    if (beatle.getDirection().equals("LEFT")){
                        beatle.setBmp(Beatle.rotateImage(beatle.getBmp(),180));
                    }
                    if (beatle.getDirection().equals("DOWN")){
                        beatle.setBmp(Beatle.rotateImage(beatle.getBmp(),270));
                    }
                   Thread.sleep(1);
                    soundPool.play(1,1,1,0,1,1);

                    beatle.moveRight();
                    beatle.setDirection("RIGHT");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    if ( beatle.getDirection().equals("DOWN")){
                        beatle.setBmp(Beatle.rotateImage(beatle.getBmp(),90));
                    }
                    if ( beatle.getDirection().equals("RIGHT")){
                        beatle.setBmp(Beatle.rotateImage(beatle.getBmp(),180));
                    }
                    Thread.sleep(1);
                    beatle.moveRight();
                    beatle.setDirection("LEFT");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void run() {
        Log.d("RUNRUN", "THREAD HAS STARTED");
        updateGameView();
    }

    public void startDrawThread()
    {
        if (surfaceReady && drawThread == null)
        {
            drawThread = new Thread(this, "Draw thread");
            gameOn = true;
            drawThread.start();
        }
    }

    private void checkInBorder(Beatle beatle){
       if (beatle.getPosY() >= canvasHight){
           beatle.setPosY(beatle.getPosY()-canvasHight);
       }
        if (beatle.getPosX() >= canvasWidth){
            beatle.setPosX(beatle.getPosX()-canvasWidth);
        }

        if (beatle.getPosX() == borderLEFT) {
            beatle.setPosX(beatle.getPosX()+canvasWidth);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void loadMusik() {

        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(attrs)
                .build();
        int soundIds[] = new int[10];
        soundIds[0] = soundPool.load(appcontext, R.raw.birds, 1);
        soundIds[1] = soundPool.load(appcontext, R.raw.birds, 1);
        //soundPool.play(1,1,1,0,1,1);
    }

     private Bee[][] loadBees(){

        try {
            for  (int row = 0; row < ANZ_ROW; row++ ){
                for (int col = 0 ; col < ANZ_COLS; col++){
                    bees[row][col] = createBee(row,col);
                }
            }
            return bees ;

        } catch (Exception e){
            Log.e("YOUR_APP_LOG_TAG", "I got an error", e);
        }
            return null ;
    }

    private Bee createBee(int row ,int col) {
        Bee bee = null ;
        try {

            int itemWith = CANVAS_WIDTH/ANZ_COLS;
            int columne = col ;

            int posX = itemWith * columne + BEE_SIZE + CANVAS_MARGIN;
            int posY = CANVAS_TOP_MARGIN + (BEE_SIZE * row) ;
            bee = new Bee(getBeeBitmap("Bee",100,100),posX,posY,appcontext);
            return bee ;
        }
        catch (Exception e){

        }

        return null ;
    }


}
