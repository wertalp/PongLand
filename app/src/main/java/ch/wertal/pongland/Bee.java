package ch.wertal.pongland;

import android.content.Context;
import android.graphics.Bitmap;

public class Bee
{

    Bitmap bmp ;
    float posX, posY ;
    Context context ;
    String direction ;


    public Bee(Bitmap bmp, float posX, float posY, Context context) {
        this.bmp = bmp;
        this.posX = posX;
        this.posY = posY;
        this.context = context;
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

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
