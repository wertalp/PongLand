package ch.wertal.pongland;

import android.graphics.Canvas;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class GameLoop extends Thread {
    GameView gameView = null;
    Boolean running = true ;
    static final long FPS = 10 ;

    public GameLoop(GameView gameView) {
        this.gameView = gameView ;
    }

    public Boolean getRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        super.run();
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;

        while (running) {
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try {
                c = gameView.getHolder().lockCanvas();
                synchronized (gameView.getHolder()) {
                    Log.d(TAG, "run: ");
                   // gameView.onDraw(c);
                }
            } finally {
                if (c != null) {
                    gameView.getHolder().unlockCanvasAndPost(c);
                }
            }
            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception e) {}

        }

    }
}
