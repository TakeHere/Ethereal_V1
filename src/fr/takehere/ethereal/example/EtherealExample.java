package fr.takehere.ethereal.example;

import fr.takehere.ethereal.Game;
import fr.takehere.ethereal.objects.Actor;
import fr.takehere.ethereal.objects.ParticleGenerator;
import fr.takehere.ethereal.objects.Pawn;
import fr.takehere.ethereal.objects.Title;
import fr.takehere.ethereal.utils.ImageUtil;
import fr.takehere.ethereal.utils.Vector2;

import java.awt.*;

public class EtherealExample extends Game {
    Actor player;
    Pawn pawn;
    ParticleGenerator particleGenerator;

    @Override
    public void init() {
        player = new Actor(new Vector2(250,0), new Dimension(50,50), ImageUtil.getImageRessource("Placeholder.png", this.getClass()), "player", 2, true, true);
        pawn = new Pawn(new Vector2(100, 700 - 20), new Dimension(500,20), ImageUtil.getImageRessource("Placeholder.png", this.getClass()), "pawn", 1);

        particleGenerator = new ParticleGenerator(new Vector2(250,250), new Dimension(10,10), ImageUtil.getImageRessource("Placeholder.png", this.getClass()), true, 20, 10, 1, 5, 5000);
        particleGenerator.generate();

        new Title("Bienvenue !", new Vector2(200, 50), Color.RED, "Bahnschrift", 50, 2000);
    }

    @Override
    public void gameLoop(double deltaTime) {
        player.rotation += 5;
    }


    public EtherealExample(String title, int height, int width, int targetFps) {
        super(title, height, width, targetFps);
    }

    public static void main(String[] args) {
        new EtherealExample("Ethereal", 500, 500, 60);
    }
}
