package ch.wertal.pongland;

import android.content.Context;
import android.graphics.Bitmap;

public class Basket {

    Bitmap bmp ;
    float posX, posY ;
    Context context ;

    public Basket(Context context, Bitmap bmp, float posX, float posY) {
        this.bmp = bmp;
        this.posX = posX;
        this.posY = posY;
        this.context = context;
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
}
