package fr.takehere.ethereal.objects;

import fr.takehere.ethereal.Scene;
import fr.takehere.ethereal.display.GameWindow;
import fr.takehere.ethereal.utils.ImageUtil;
import fr.takehere.ethereal.utils.Vector2;
import fr.takehere.ethereal.utils.maths.MathUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends GameObject {

    public Vector2 location;
    public Vector2 rotationAnchor;
    public Dimension dimension;
    public Image texture;
    public double rotation = 0;

    public Vector2 velocity = new Vector2(0, 0);
    public Shape boundingBox;

    public static List<Pawn> pawns = new ArrayList<>();

    public Pawn(Vector2 location, Dimension dimension, Image texture, String name, Scene scene) {
        super(name, 0, scene);

        if (dimension == null){
            this.dimension = new Dimension(texture.getWidth(null), texture.getHeight(null));
            this.texture = texture;
        }else {
            this.dimension = dimension;
            this.texture = ImageUtil.resizeImage(texture, dimension.width, dimension.height);
        }

        this.location = MathUtils.getCenterOfRectangle(new Rectangle(location.toPoint(), this.dimension));
        this.boundingBox = new Rectangle(location.toPoint(), this.dimension);
        pawns.add(this);
    }

    public void destroy(){
        GameWindow.runNextFrame(() -> pawns.remove(this));
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
        texture = ImageUtil.resizeImage(texture, dimension.width, dimension.height);
    }
}
