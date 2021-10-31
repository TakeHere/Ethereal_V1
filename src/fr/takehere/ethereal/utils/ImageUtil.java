package fr.takehere.ethereal.utils;

import fr.takehere.ethereal.example.EtherealExample;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class ImageUtil {

    public static Image placeholder = getImageRessource("Placeholder.png", EtherealExample.class);

    public static Image getImageRessource(String name, Class mainClass){
        try {
            return ImageIO.read(mainClass.getResource("ressources/" + name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Image resizeImage(Image originalImage, int targetWidth, int targetHeight){
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        return resultingImage;
    }


}
