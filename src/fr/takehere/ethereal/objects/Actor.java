package fr.takehere.ethereal.objects;

import fr.takehere.ethereal.display.GameWindow;
import fr.takehere.ethereal.utils.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Actor extends Pawn{

    public boolean gravity = true;
    public boolean bounce;
    public float bounciness = 2;
    public static List<Actor> actors = new ArrayList<>();

    public long lastCollision = System.currentTimeMillis();

    public Actor(Vector2 location, Dimension dimension, Image texture, String name, int drawLevel, boolean gravity, boolean bounce) {
        super(location, dimension, texture, name, drawLevel);
        this.gravity = gravity;
        this.bounce = bounce;

        actors.add(this);
    }

    public void setBounciness(int bounciness) {
        this.bounciness = bounciness;
    }

    public void destroy(){
        GameWindow.runNextFrame(() -> actors.remove(this));
        super.destroy();
    }
}
