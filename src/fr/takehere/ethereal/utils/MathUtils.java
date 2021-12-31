package fr.takehere.ethereal.utils;

import fr.takehere.ethereal.objects.Pawn;

import java.awt.*;
import java.util.Random;

public class MathUtils {
    public static Random random = new Random();

    public static double mapDecimal(double val, double in_min, double in_max, double out_min, double out_max) {
        return (val - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public static double invertRadianAngle(double angle){
        return (angle + Math.PI) % (2 * Math.PI);
    }

    public static int randomNumberBetween(int min, int max){
        return random.nextInt(max - min + 1) + min;
    }

    public static boolean isBetween(float value, float min, float max){
        return value < max && value > min;
    }

    public static Vector2 getCenterOfRectangle(Rectangle rectangle){
        return new Vector2(rectangle.x + rectangle.width/2, rectangle.y + rectangle.width/2);
    }

    public static Rectangle getRectangleOfPawn(Pawn pawn){
        return new Rectangle(pawn.location.toPoint(), new Dimension(pawn.dimension.width, pawn.dimension.height));
    }

    public static Vector2 getCenterOfPawn(Pawn pawn){
        return new Vector2(pawn.location.x + pawn.dimension.width/2, pawn.location.y + pawn.dimension.width/2);
    }

    public static Vector2 getForwardVector(double rotation){
        Vector2 forwardVector = new Vector2(
                Math.sin(Math.toRadians(rotation)),
                Math.cos(Math.toRadians(rotation)) * -1
        );

        return forwardVector;
    }

    public static Vector2 randomDirection(){
        return new Vector2(randomNumberBetween(-1000,1000), randomNumberBetween(-1000,1000)).normalize();
    }

    public static boolean isColliding(Rectangle rec1, Rectangle rec2){
        return rec1.x < rec2.x + rec2.width &&
                rec1.x + rec1.width > rec2.x &&
                rec1.y < rec2.y + rec2.height &&
                rec1.y + rec1.height > rec2.y;
    }

    public static Vector2 pointToVector2(Point point){
        return new Vector2(point.x, point.y);
    }
}
