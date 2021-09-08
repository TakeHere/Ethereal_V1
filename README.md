# Ethereal Game Framework
Welcome to Ethereal game framework ! A java game framework that uses Swift as it's graphic renderer.

## Features: Currently there are two objects:
Pawn: Rendered object with position, size, rotation and texure
Actor: Pawn with velocity and gravity

There are many handy utils for maths, image and sound. Here's what they can be used for:
| MathUtil                      | ImageUtil           | SoundUtil           |  ConsoleColor           |
| ------------------------------|:--------------------| --------------------|  -----------------------|
| Randian Angle Inversion       | Get Image ressource | Get Sound ressource |  Print colored Messages |
| Number mapping                | Resize Image        | Play sound          |                         |
| Random number beetween bounds |                     |                     |                         |

Of course, Vector logic has been implemented with many handy functions.

## Installation:

Simply import the Ethereal jar into your IDE and inherit the Game class to begin !

## Example:

```java
public class EtherealExample extends Game {
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
        new EtherealExample("Ethereal", 500, 500, 60);
    }
}
```
