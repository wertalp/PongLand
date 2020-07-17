package ch.wertal.pongland;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Sprite  {

    private int x = 0;
    private int xSpeed = 5;
    private GameView gameView;
    private Bitmap bmp;


    public Sprite(GameView gameView, Bitmap bmp) {
        this.gameView = gameView;
        this.bmp = bmp;
    }


    private void update() {

        if (x > gameView.getWidth() - bmp.getWidth() - xSpeed) {
            xSpeed = -5;
        }

        if (x + xSpeed< 0) {
            xSpeed = 5;
        }
        x = x + xSpeed;

    }


    public void onDraw(Canvas canvas) {

        update();
        canvas.drawBitmap(bmp, x , 10, null);

    }
}
