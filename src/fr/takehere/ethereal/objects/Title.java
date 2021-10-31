package fr.takehere.ethereal.objects;

import fr.takehere.ethereal.Scene;
import fr.takehere.ethereal.utils.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Title {

    public String text;
    public Vector2 location;
    public Color color;
    public String font;
    public int size;
    public Scene scene;

    public static List<Title> titles = new ArrayList<>();

    public Title(String text, Vector2 location, Color colorInput, String font, int size, int speed, Scene scene) {
        this.location = location;
        this.color = colorInput;
        this.font = font;
        this.size = size;
        this.text = text;
        this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 255);
        this.scene = scene;

        titles.add(this);

        Title title = this;
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (color.getAlpha() == 1){
                    titles.remove(title);
                    this.cancel();
                }
                color = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() - 1);
            }
        },0, speed/255);
    }
}
