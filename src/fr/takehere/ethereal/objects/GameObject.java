package fr.takehere.ethereal.objects;

import fr.takehere.ethereal.Scene;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    public String name;
    public int drawLevel;
    public Scene scene;

    public static List<GameObject> gameObjects = new ArrayList<>();

    public GameObject(String name, int drawLevel, Scene scene) {
        this.name = name;
        this.drawLevel = drawLevel;
        this.scene = scene;
    }
}
