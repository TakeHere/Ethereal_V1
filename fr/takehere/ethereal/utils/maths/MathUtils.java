package fr.takehere.ethereal.utils.maths;

import fr.takehere.ethereal.utils.Vector2;

import java.awt.*;
import java.util.Random;

public class MathUtils {
    static Random random = new Random();

    public static double mapDecimal(double val, double in_min, double in_max, double out_min, double out_max) {
        return (val - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public static double invertRadianAngle(double angle){
        return (angle + Math.PI) % (2 * Math.PI);
    }

    public static int randomNumberBetween(int min, int max){
        return random.nextInt(max - min + 1) + min;
    }

    public static Vector2 getCenterFromJavaRectangle(Vector2 origin, Dimension size){
        return new Vector2(origin.x + size.width / 2, origin.y + size.height / 2);
    }
}
