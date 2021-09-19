package fr.takehere.ethereal.objects;

import fr.takehere.ethereal.display.GameWindow;
import fr.takehere.ethereal.utils.Vector2;
import fr.takehere.ethereal.utils.maths.MathUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ParticleGenerator {

    public int number;
    public int rotation;
    public int minSpeed;
    public int maxSpeed;
    public long lifeTime;

    public Vector2 location;
    public Dimension dimension;
    public Image texture;
    public boolean gravity;

    public static List<ParticleGenerator> particleGenerators = new ArrayList<>();

    public ParticleGenerator(Vector2 location, Dimension dimension, Image texture, boolean gravity, int number, int rotation, int minSpeed, int maxSpeed, long lifeTime) {
        this.location = MathUtils.getCenterOfRectangle(new Rectangle(location.toPoint(), dimension));
        this.dimension = dimension;
        this.texture = texture;
        this.gravity = gravity;
        this.number = number;
        this.rotation = rotation;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.lifeTime = lifeTime;

        particleGenerators.add(this);
    }

    public List<Actor> particles = new ArrayList<>();

    public void generate(){
        GameWindow.runNextFrame(() -> {
            List<Actor> createdParticles = new ArrayList<>();

            for (int i = 0; i < number; i++) {
                Actor particle = new Actor(location, dimension, texture, "particle" , 999, gravity, false);
                particle.velocity = MathUtils.randomDirection().multiply(MathUtils.randomNumberBetween(minSpeed, maxSpeed));

                particles.add(particle);
                createdParticles.add(particle);
            }

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    for (Actor createdParticle : createdParticles) {
                        createdParticle.destroy();
                        particles.remove(createdParticle);
                    }

                    createdParticles.clear();
                }
            }, lifeTime);
        });
    }
}
