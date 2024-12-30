import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[]args) throws Exception {

        int boardWidth = 600;
        int boardHeight = 600;

        //create the Frame
        JFrame frame = new JFrame("Snake Game");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null); //set the window at the center of the screen
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create a SnakeGame Object(Label)
        SnakeGame snakegame = new SnakeGame(boardWidth, boardHeight);
        //add snakegame label to Frame
        frame.add(snakegame);
        frame.pack();

        snakegame.requestFocus();
    }
}
