package fr.takehere.ethereal;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    public String name;
    public int drawLevel;

    public static List<GameObject> gameObjects = new ArrayList<>();

    public GameObject(String name, int drawLevel) {
        this.name = name;
        this.drawLevel = drawLevel;
    }
}
