package fr.takehere.ethereal.examples;

import fr.takehere.ethereal.Game;
import fr.takehere.ethereal.Scene;
import fr.takehere.ethereal.objects.Actor;
import fr.takehere.ethereal.objects.ParticleGenerator;
import fr.takehere.ethereal.objects.Pawn;
import fr.takehere.ethereal.objects.Title;
import fr.takehere.ethereal.utils.ImageUtil;
import fr.takehere.ethereal.utils.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;

public class EtherealExample extends Game {
    Actor player;
    Pawn pawn;
    ParticleGenerator particleGenerator;

    @Override
    public void init() {
        player = new Actor(new Vector2(250,0), new Dimension(50,50), ImageUtil.getImageRessource("Placeholder.png", this.getClass()), "player", this);
        player.gravity = true;
        player.bounce = true;

        pawn = new Pawn(new Vector2(100, 700 - 20), new Dimension(500,20), ImageUtil.getImageRessource("Placeholder.png", this.getClass()), "pawn", this);

        particleGenerator = new ParticleGenerator(new Vector2(250,250), new Dimension(10,10), ImageUtil.getImageRessource("Placeholder.png", this.getClass()), true, 20, 1, 5, 5000, this);
        particleGenerator.generate();

        new Title("Bienvenue !", new Vector2(200, 50), Color.RED, new Font("Bahnschrift", Font.PLAIN,50), 2000, this);
    }

    @Override
    public void gameLoop(double deltaTime) {
        player.rotation += 5;
        particleGenerator.generate();

        camera.setLocation(new Vector2(-150,0));

        if (this.gameWindow.isPressed(KeyEvent.VK_E)){
            this.switchScene(new Scene() {
                @Override
                public void init() {
                    new Title("Deuxieme scene !", new Vector2(100, 50), Color.RED, new Font("Bahnschrift", Font.PLAIN,50), 2000, this);
                }

                @Override
                public void gameLoop(double deltaTime) {

                }
            });
        }
    }


    public EtherealExample(String title, int height, int width, int targetFps) {
        super(title, height, width, targetFps);
    }

    public static void main(String[] args) {
        new EtherealExample("Ethereal", 500, 500, 60);
    }
}
