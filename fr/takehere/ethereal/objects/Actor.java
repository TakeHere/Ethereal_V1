package fr.takehere.ethereal.objects;

import fr.takehere.ethereal.utils.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Actor extends Pawn{

    public boolean gravity = true;
    public static List<Actor> actors = new ArrayList<>();

    public Actor(Vector2 location, Dimension dimension, Image texture, String name, int drawLevel, boolean gravity) {
        super(location, dimension, texture, name, drawLevel);
        this.gravity = gravity;

        actors.add(this);
    }

    public void destroy(){
        actors.remove(this);
        super.destroy();
    }
}
