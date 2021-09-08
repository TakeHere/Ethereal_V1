package fr.takehere.ethereal.objects;

import fr.takehere.ethereal.GameObject;
import fr.takehere.ethereal.utils.ImageUtil;
import fr.takehere.ethereal.utils.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends GameObject {

    public Vector2 location;
    private Dimension dimension;
    public Image texture;
    public float rotation = 0;

    public Vector2 velocity = new Vector2(0, 0);
    public Shape boundingBox;

    public static List<Pawn> pawns = new ArrayList<>();

    public Pawn(Vector2 location, Dimension dimension, Image texture, String name, int drawLevel) {
        super(name, drawLevel);

        this.location = location;
        this.dimension = dimension;
        this.texture = ImageUtil.resizeImage(texture, dimension.width, dimension.height);

        this.boundingBox = new Rectangle(location.toPoint(), dimension);
        pawns.add(this);
    }

    public void destroy(){
        pawns.remove(this);
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
        texture = ImageUtil.resizeImage(texture, dimension.width, dimension.height);
    }
}
