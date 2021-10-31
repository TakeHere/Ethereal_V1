package fr.takehere.ethereal.display;

import fr.takehere.ethereal.Game;
import fr.takehere.ethereal.Scene;
import fr.takehere.ethereal.objects.Actor;
import fr.takehere.ethereal.objects.ParticleGenerator;
import fr.takehere.ethereal.objects.Pawn;
import fr.takehere.ethereal.objects.Title;
import fr.takehere.ethereal.utils.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    public Dimension size;
    public Scene currentScene;
    private Game game;

    private List<Integer> pressedKeys = new ArrayList<>();

    public GameWindow(String title, int height, int width, int targetFps, Game game){
        super(title);

        this.title = title;
        this.targetFps = targetFps;
        this.game = game;
        this.currentScene = game;
        this.canvas = new Canvas();
        this.size = new Dimension(height, width);

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
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!pressedKeys.contains(e.getKeyCode()))
                    pressedKeys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(Integer.valueOf(e.getKeyCode()));

            }
        });

        GameWindow gameWindow = this;
        game.gameWindow = this;
        currentScene.init();

        Thread thread = new Thread(new Runnable() {
            long initialLaunchTime = System.nanoTime();
            double targetTime = NANO / targetFps;

            @Override
            public void run() {
                while (!isStopped) {
                    Graphics2D g = getGraphics();
                    long startTime = System.nanoTime();

                    g.clearRect(0,0,getWidth(),getHeight());

                    //------< Avoid Concurrent exception >------
                    for (java.lang.Object o : runNextFrame.toArray()) {
                        Runnable runnable = (Runnable) o;
                        if (runnable != null)
                            runnable.run();
                    }
                    runNextFrame.clear();

                    currentScene.gameLoop(deltaTime);
                    pawnRendering();
                    actorMovements();
                    particlesRotation();
                    titlesRendering();

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
                            Thread.sleep((int) ((targetTime - elapsedTime) / 1000000) /2);
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
        Pawn.pawns.forEach(pawn -> {
            if (pawn.scene.equals(currentScene))
                unsortedPawnsDrawLevel.put(pawn, pawn.drawLevel);
        });

        LinkedHashMap<Pawn, Integer> sortedPawnsDrawLevel = new LinkedHashMap<>();
        unsortedPawnsDrawLevel.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sortedPawnsDrawLevel.put(x.getKey(), x.getValue()));

        for (Map.Entry<Pawn, Integer> entry : sortedPawnsDrawLevel.entrySet()) {
            Pawn pawn = entry.getKey();
            AffineTransform tr = new AffineTransform();

            tr.translate(pawn.location.x, pawn.location.y);
            if(pawn.rotationAnchor == null){
                tr.rotate(
                        Math.toRadians(pawn.rotation),
                        pawn.dimension.width / 2,
                        pawn.dimension.height / 2
                );
            }else {
                tr.rotate(
                        Math.toRadians(pawn.rotation),
                        pawn.rotationAnchor.x / 2,
                        pawn.rotationAnchor.y / 2
                );
            }
            g.drawImage(pawn.texture, tr, null);

            pawn.boundingBox = tr.createTransformedShape(new Rectangle(pawn.dimension.width, pawn.dimension.height));
        }
    }

    public void actorMovements(){
        for (Actor actor : Actor.actors) {
            if (actor.scene.equals(currentScene)){
                //------< Calculate bounciness >------
                if (actor.bounce){
                    if (actor.lastCollision < System.currentTimeMillis()){
                        actor.lastCollision = (long) (System.currentTimeMillis() + (15 * deltaTime));

                        for (Pawn pawn : Pawn.pawns) {
                            if (!pawn.name.equalsIgnoreCase("particle")){
                                if (actor != pawn){
                                    //TODO Collision projection
                                    if (actor.boundingBox.getBounds().intersects(pawn.boundingBox.getBounds())){
                                        Vector2 bounceVector = new Vector2(Math.sin(Math.toRadians(pawn.rotation)) * (actor.bounciness * deltaTime), Math.cos(Math.toRadians(pawn.rotation)) * (actor.bounciness * deltaTime) *-1);

                                        actor.velocity = actor.velocity.add(bounceVector);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //------< Apply velocity >------
            if (actor.gravity){
                actor.velocity = actor.velocity.add(new Vector2(0, game.gravity));
            }

            actor.location = actor.location.add(actor.velocity);
        }
    }

    public void particlesRotation(){
        for (ParticleGenerator particleGenerator : ParticleGenerator.particleGenerators) {
            if (particleGenerator.scene.equals(currentScene)){
                if (particleGenerator.rotationSpeed != 0){
                    for (Actor particle : particleGenerator.particles) {
                        particle.rotation += particleGenerator.rotationSpeed;
                    }
                }
            }
        }
    }

    public void titlesRendering(){
        Graphics2D g = getGraphics();

        for (Title title : Title.titles) {
            if (title.scene.equals(currentScene)){
                g.setColor(title.color);
                g.setFont(new Font(title.font, Font.PLAIN,title.size));
                g.drawString(title.text, (int) title.location.x, (int) title.location.y);
            }
        }
    }

    public static void runNextFrame(Runnable runnable){
        runNextFrame.add(runnable);
    }

    public Graphics2D getGraphics(){
        try {
            return (Graphics2D) this.canvas.getBufferStrategy().getDrawGraphics();
        }catch (NullPointerException e){
            return this.getGraphics();
        }
    }

    public int getFps() {
        return fps;
    }

    public boolean isPressed(int keyCode){
        return pressedKeys.contains(keyCode);
    }
}
