package fr.takehere.ethereal.utils;

import javax.sound.sampled.AudioInputStream;
import java.awt.*;
import java.util.HashMap;

public class RessourcesManager {

    private static HashMap<String, Image> images = new HashMap<>();
    private static HashMap<String, AudioInputStream> sounds = new HashMap<>();

    public static Image addImage(String name, String path, Class mainClass){
        Image image = ImageUtil.getImageRessource(path, mainClass);
        images.put(name, image);

        return image;
    }

    public static Image getImage(String name){
        return images.get(name);
    }


    public static AudioInputStream addSound(String name, String path, Class mainClass){
        AudioInputStream sound = SoundUtil.getSoundRessource(path, mainClass);
        sounds.put(name, sound);

        return sound;
    }

    public static AudioInputStream getSound(String name){
        return sounds.get(name);
    }
}
