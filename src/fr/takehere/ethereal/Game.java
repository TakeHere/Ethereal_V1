package fr.takehere.ethereal;

import fr.takehere.ethereal.display.GameWindow;
import fr.takehere.ethereal.utils.ConsoleColor;

public abstract class Game implements GameInterface{
    public static float ETHEREAl_VERSION = 1.3f;

    public GameWindow gameWindow;
    public float gravity = 0.1f;

    public Game(String title, int height, int width, int targetFps) {
        this.gameWindow = new GameWindow(title, height, width, targetFps, this);

        System.out.println(ConsoleColor.YELLOW + "------------< Ethereal Engine v" + ETHEREAl_VERSION + " >------------");
        System.out.println("");
        System.out.println(ConsoleColor.RED + "This game is made with Ethereal Engine, a game framework for java");
        System.out.println("Made by https://github.com/TakeHere");
        System.out.println("");
        System.out.println(ConsoleColor.YELLOW + "------------< Ethereal Engine v" + ETHEREAl_VERSION + " >------------" + ConsoleColor.RESET);
    }

    public void runNextFrame(Runnable runnable){
        gameWindow.runNextFrame(runnable);
    }
}
