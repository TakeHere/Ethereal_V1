package fr.takehere.ethereal.display;

import fr.takehere.ethereal.Game;
import fr.takehere.ethereal.objects.Actor;
import fr.takehere.ethereal.objects.Pawn;
import fr.takehere.ethereal.utils.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.List;
import java.util.*;

public class GameWindow extends JFrame{

    public static final double NANO = 1.0e9;

    public String title;
    public int targetFps;

    public boolean displayFps = true;

    public Canvas canvas;
    public Game game;

    public GameWindow(String title, int height, int width, int targetFps, Game game){
        super(title);

        this.title = title;
        this.targetFps = targetFps;
        this.game = game;
        this.canvas = new Canvas();

        this.setSize(width,height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        this.setResizable(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);

        this.canvas.setPreferredSize(this.getSize());
        this.canvas.setMinimumSize(this.getSize());
        this.canvas.setMaximumSize(this.getSize());

        this.add(this.canvas);
        this.pack();
        this.canvas.requestFocus();

        this.canvas.setIgnoreRepaint(true);
        this.canvas.createBufferStrategy(2);

        start();
    }

    public boolean isStopped = false;
    private int fps = 60;
    public double deltaTime = 1;

    private static List<Runnable> runNextFrame = new ArrayList<>();

    private void start(){
        game.init();
        GameWindow gameWindow = this;

        Thread thread = new Thread(new Runnable() {
            long initialLaunchTime = System.nanoTime();
            double targetTime = NANO / targetFps;

            @Override
            public void run() {
                while (!isStopped) {
                    Graphics2D g = getGraphics();
                    long startTime = System.nanoTime();

                    g.clearRect(0,0,500,500);

                    game.gameLoop(deltaTime);
                    pawnRendering();
                    actorMovements();

                    //------< Avoid Concurrent exception >------
                    for (java.lang.Object o : runNextFrame.toArray()) {
                        Runnable runnable = (Runnable) o;
                        if (runnable != null)
                            runnable.run();
                    }
                    runNextFrame.clear();

                    //------< Graphics disposal >------
                    g.dispose();
                    BufferStrategy strategy = canvas.getBufferStrategy();
                    if (!strategy.contentsLost()) {
                        strategy.show();
                    }


                    long elapsedTime = System.nanoTime() - startTime;

                    //--------< Fps calculations >--------
                    if (targetTime > elapsedTime){
                        try {
                            Thread.sleep((int) (targetTime - elapsedTime) / 1000000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    fps++;
                    if (System.nanoTime() - initialLaunchTime > 1.0e9){
                        gameWindow.deltaTime = (double) targetFps / fps;

                        if (displayFps)
                            setTitle(title + " | fps: " + fps);
                        initialLaunchTime += 1.0e9;
                        fps = 0;
                    }
                }
            }
        });
        thread.setDaemon(true);

        thread.start();
    }

    public void pawnRendering(){
        Graphics2D g = getGraphics();

        HashMap<Pawn, Integer> unsortedPawnsDrawLevel = new HashMap();
        Pawn.pawns.forEach(pawn -> unsortedPawnsDrawLevel.put(pawn, pawn.drawLevel));

        LinkedHashMap<Pawn, Integer> sortedPawnsDrawLevel = new LinkedHashMap<>();
        unsortedPawnsDrawLevel.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sortedPawnsDrawLevel.put(x.getKey(), x.getValue()));

        for (Map.Entry<Pawn, Integer> entry : sortedPawnsDrawLevel.entrySet()) {
            Pawn pawn = entry.getKey();
            AffineTransform tr = new AffineTransform();

            tr.translate(pawn.location.x, pawn.location.y);
            tr.rotate(
                    Math.toRadians(pawn.rotation),
                    pawn.getDimension().width / 2,
                    pawn.getDimension().height / 2
            );
            g.drawImage(pawn.texture, tr, null);

            pawn.boundingBox = tr.createTransformedShape(new Rectangle(pawn.getDimension().width, pawn.getDimension().height));
        }
    }

    public void actorMovements(){
        for (Actor actor : Actor.actors) {
            if (actor.gravity){
                actor.velocity.add(new Vector2(0, game.gravity));
            }

            actor.location = actor.location.add(actor.velocity);
        }
    }

    public void runNextFrame(Runnable runnable){
        runNextFrame.add(runnable);
    }

    public Graphics2D getGraphics(){
        try {
            return (Graphics2D) this.canvas.getBufferStrategy().getDrawGraphics();
        }catch (NullPointerException e){
            return this.getGraphics();
        }
    }
}
