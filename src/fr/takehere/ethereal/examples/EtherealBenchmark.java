package fr.takehere.ethereal.examples;

import fr.takehere.ethereal.Game;
import fr.takehere.ethereal.objects.Actor;
import fr.takehere.ethereal.objects.ParticleGenerator;
import fr.takehere.ethereal.objects.Pawn;
import fr.takehere.ethereal.objects.Title;
import fr.takehere.ethereal.utils.ImageUtil;
import fr.takehere.ethereal.utils.MathUtils;
import fr.takehere.ethereal.utils.Vector2;

import java.awt.*;

public class EtherealBenchmark extends Game {

    ParticleGenerator particleGenerator;
    Actor bouncer;

    @Override
    public void init() {
        this.debug = true;

        new Title("Ethereal Benchmark", new Vector2(100, 250), Color.BLUE, new Font("Bahnschrift", Font.PLAIN,30), 2000, this);
        particleGenerator = new ParticleGenerator(new Vector2(250,250), new Dimension(10,10), ImageUtil.placeholder, false, 2, 0,1,100, this);

        Pawn upperWall = new Pawn(new Vector2(0,0), new Dimension(500,50), ImageUtil.placeholder, "wall", this);
        Pawn leftWall = new Pawn(new Vector2(0,0), new Dimension(50,500), ImageUtil.placeholder, "wall", this);
        Pawn rightWall = new Pawn(new Vector2(450,0), new Dimension(50,500), ImageUtil.placeholder, "wall", this);
        Pawn lowerWall = new Pawn(new Vector2(0,450), new Dimension(500,50), ImageUtil.placeholder, "wall", this);

        bouncer = new Actor(new Vector2(250,250), new Dimension(50,50), ImageUtil.placeholder, "bouncer", this);
        bouncer.gravity = true;
        bouncer.bounce = true;
    }

    @Override
    public void gameLoop(double deltaTime) {
        particleGenerator.location = MathUtils.getCenterOfRectangle(MathUtils.getRectangleOfPawn(bouncer));
        particleGenerator.generate();
        bouncer.rotation++;
    }

    public EtherealBenchmark(String title, int height, int width, int targetFps) {
        super(title, height, width, targetFps);
    }
    public static void main(String[] args) {
        new EtherealBenchmark("EtherealBenchmark", 500, 500, 60);
    }
}
