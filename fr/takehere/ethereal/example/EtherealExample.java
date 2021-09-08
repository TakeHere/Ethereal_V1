package fr.takehere.ethereal.example;

import fr.takehere.ethereal.Game;
import fr.takehere.ethereal.objects.Actor;
import fr.takehere.ethereal.objects.Pawn;
import fr.takehere.ethereal.utils.ImageUtil;
import fr.takehere.ethereal.utils.Vector2;

import java.awt.*;

public class EtherealExample extends Game {
    private static EtherealExample instance;

    Actor player;
    Pawn pawn;

    @Override
    public void init() {
        player = new Actor(new Vector2(100,0), new Dimension(50,50), ImageUtil.getImageRessource("Placeholder.png", this.getClass()), "player", 2, true);
        pawn = new Pawn(new Vector2(200, 50), new Dimension(50,100), ImageUtil.getImageRessource("Placeholder.png", this.getClass()), "pawn", 1);
    }

    @Override
    public void gameLoop(double deltaTime) {
        player.rotation += 5;
    }

    public EtherealExample(String title, int height, int width, int targetFps) {
        super(title, height, width, targetFps);
    }

    public static void main(String[] args) {
        instance = new EtherealExample("Ethereal", 500, 500, 60);
    }

    public static EtherealExample getInstance() {
        return instance;
    }
}
