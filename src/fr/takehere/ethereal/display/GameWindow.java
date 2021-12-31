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

    public boolean displayFps = true;

    public String title;
    public int targetFps;
    public Canvas canvas;
    public Dimension size;
    public Scene currentScene;

    private Game game;

    public GameWindow(String title, int height, int width, int targetFps, Game game){
        super(title);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

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

    private static List<Runnable> runNextFrame = new ArrayList<>();
    private List<Integer> pressedKeys = new ArrayList<>();
    private int fps = 60;

    private void start(){
        game.gameWindow = this;
        currentScene.init();

        canvas.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (!pressedKeys.contains(e.getKeyCode()))
                    pressedKeys.add(e.getKeyCode());
            }
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(Integer.valueOf(e.getKeyCode()));
            }
        });

        Thread thread = new Thread(new Runnable() {
            long frameStart;
            long elapsedTime;
            float frameDelay = 1000 / targetFps;
            long initialLaunchTime = System.currentTimeMillis();

            @Override
            public void run() {
                while (true) {
                    frameStart = System.currentTimeMillis();

                    Graphics2D g = getGraphics();
                    g.clearRect(0,0,getWidth(),getHeight());

                    try {
                        for (Runnable runnable : runNextFrame) {
                            if (runnable != null){
                                runnable.run();
                            }
                        }
                        runNextFrame.clear();

                        currentScene.gameLoop(1);
                        pawnRendering();
                        actorMovements();
                        particlesRotation();
                        titlesRendering();
                    }catch (ConcurrentModificationException e){}

                    //------< Graphics disposal >------
                    g.dispose();
                    BufferStrategy strategy = canvas.getBufferStrategy();
                    if (!strategy.contentsLost()) {
                        strategy.show();
                    }

                    //--------< Fps calculations >--------
                    elapsedTime = System.currentTimeMillis() - frameStart;

                    if (frameDelay > elapsedTime){
                        try {
                            Thread.sleep((long) (frameDelay - elapsedTime));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    fps++;
                    if (System.currentTimeMillis() - initialLaunchTime > 1000){
                        if (displayFps)
                            setTitle(title + " | fps: " + fps);
                        initialLaunchTime += 1000;
                        fps = 0;
                    }
                }
            }
        });

        thread.start();
    }

    public void pawnRendering(){
        Graphics2D g = getGraphics();

        //------< Sort pawns by draw level >------
        HashMap<Pawn, Integer> pawnsDrawLevel = new HashMap<>();
        for (Pawn pawn : Pawn.pawns) {
            if (pawn.scene.equals(currentScene))
                pawnsDrawLevel.put(pawn, pawn.drawLevel);
        }
        LinkedHashMap<Pawn, Integer> sortedPawnsDrawLevel = new LinkedHashMap<>();
        pawnsDrawLevel.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sortedPawnsDrawLevel.put(x.getKey(), x.getValue()));


        for (Map.Entry<Pawn, Integer> entry : sortedPawnsDrawLevel.entrySet()) {
            Pawn pawn = entry.getKey();
            AffineTransform tr = new AffineTransform();

            tr.translate(pawn.location.x, pawn.location.y);
            if(pawn.centeredRotationAnchor == true){
                tr.rotate(
                        Math.toRadians(pawn.rotation),
                        pawn.dimension.width / 2,
                        pawn.dimension.height / 2
                );
            }else {
                tr.rotate(
                        Math.toRadians(pawn.rotation),
                        pawn.location.x,
                        pawn.location.y
                );
            }
            g.drawImage(pawn.texture, tr, null);

            pawn.boundingBox = tr.createTransformedShape(new Rectangle(pawn.dimension.width, pawn.dimension.height));

            //Debug
            if (game.debug){
                g.setStroke(new BasicStroke(5));

                /*
                g.setColor(Color.RED);
                g.drawRect((int) pawn.location.x, (int) pawn.location.y, pawn.dimension.width, pawn.dimension.height);
                 */

                g.setColor(Color.BLUE);
                g.draw(pawn.boundingBox);
            }
        }
    }

    public void actorMovements(){
        for (Actor actor : Actor.actors) {
            if (actor.scene.equals(currentScene)){

                //------< Calculate bounciness >------
                if (actor.bounce){
                    if (actor.lastCollision < System.currentTimeMillis()){
                        actor.lastCollision = System.currentTimeMillis() + (15);

                        for (Pawn pawn : Pawn.pawns) {
                            if (!pawn.name.equalsIgnoreCase("particle")){
                                if (actor != pawn){
                                    if (actor.boundingBox.getBounds().intersects(pawn.boundingBox.getBounds())){
                                        Vector2 bounceVector = new Vector2(Math.sin(Math.toRadians(pawn.rotation)) * (actor.bounciness), Math.cos(Math.toRadians(pawn.rotation)) * (actor.bounciness) *-1);

                                        actor.velocity = actor.velocity.add(bounceVector);
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
                g.setFont(title.font);
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
