package ch.wertal.pongland;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;


public class Beatle {
    Bitmap bmp ;
    float posX, posY ;
    Context context ;
    String direction ;

    public Beatle(Context context,Bitmap bmp, float posX, float posY) {
        this.context = context;
        this.bmp  = bmp;
        this.posX = posX;
        this.posY = posY;
        this.direction = "DOWN";
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void moveDown(){
       setPosY(getPosY()+20);
    }
    public void moveRight(){
        setPosX(getPosX()+20);
    }
    public void moveLeft(){
        setPosX(getPosX()-20);
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public  static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return  rotatedImg ;
    }
}
