package ch.wertal.pongland;

import android.graphics.Bitmap;
import ch.wertal.pongland.Beatle;

public abstract class Animal implements Moveable {
    String name ;
    Bitmap animalBmp ;
    int age ;


    public Animal(String name, Bitmap animalBmp, int age, Beatle beatle) {
        this.name = name;
        this.animalBmp = animalBmp;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getAnimalBmp() {
        return animalBmp;
    }

    public void setAnimalBmp(Bitmap animalBmp) {
        this.animalBmp = animalBmp;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
