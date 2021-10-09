# Ethereal Game Framework
Welcome to Ethereal game framework ! A java game framework that uses Swift as it's graphic renderer.
❗ It's the third version of Ethereal, all ideas are appreciated !

## Features: 
Two objects are currently there:

Pawn: Rendered object with position, size, rotation and texure
Actor: Pawn with velocity and gravity

There are many handy utils for maths, image and sound. Here's what they can be used for:
| MathUtil                      | ImageUtil           | SoundUtil           |  ConsoleColor           |
| ------------------------------|:--------------------| --------------------|  -----------------------|
| Randian Angle Inversion       | Get Image ressource | Get Sound ressource |  Print colored Messages |
| Number mapping                | Resize Image        | Play sound          |                         |
| Random number between bounds  |                     |                     |                         |
| Collision detection           |                     |                     |                         |

Of course, Vector logic has been implemented with many handy functions.

1.3v Update: 
-Actors can now bounce on other objects
-Particles have been added ! Simply create a particleGenerator object to begin using it
-Titles have been added too ! You can now display messages onto screen that disappear after a certain amout of time

## Installation:

Simply import the Ethereal jar into your IDE and inherit the Game class to begin !

## Example:

```java
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
```
