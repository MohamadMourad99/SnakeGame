import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

//Class to create the Panel
public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    int boardWidth, boardHeight;
    // Panel size is 600x600, we divided it by 24 equal tiles to get a TileSize of 25
    int tileSize = 25;// (A single Tile has a size of 25x25)

    //Tile Object to create the snake head
    Tile snake_head;
    //ArrayList to store the snake's body
    ArrayList<Tile> snake_body;

    //Tile Object to create food
    Tile food;

    Random rand;
    Timer game_loop; //Create a game_loop Timer Object
    int velocityX, velocityY; //Create velocity in x and y direction
    boolean gameOver = false;

    //SnakeGame Constructor
    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        //set panel size and color
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);

        //add KeyListener
        addKeyListener(this);
        setFocusable(true);

        //snake_head coordinates at the start
        snake_head = new Tile(5, 5);
        //snake_body
        snake_body = new ArrayList<Tile>();

        //the food coordinates
        food = new Tile(10, 10);
        //Random object for random food coordinates
        rand = new Random();

        //place food in random location
        placeFood();

        //initial velocity of the snake is 0(Not moving)
        velocityX = 0; velocityY = 0;

        //game_loop will wait 100ms then execute the actionPerformed(ActionEvent e) method
        game_loop = new Timer(100, this);
        game_loop.start();
    }

    //Class to track the x, y positions of tiles
    private class Tile{
        int x, y;
        //Tile constructor
        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

     public void paint(Graphics g){ //we do NOT need to call this method for it to work
        super.paint(g);
        draw(g);
     }

     public void draw(Graphics g){
        //draw grid
        for(int i=0; i<boardWidth/tileSize /* equal to 24 */; i++){
             //(x1, y1, x2, y2)
             g.drawLine(i*tileSize, 0, i*tileSize, boardHeight); //draw vertical line
             g.drawLine(0, i*tileSize, boardWidth, i*tileSize); //draw horizontal line
        }
        //draw snake_head, Always setColor() then fill3DRect()
        g.setColor(Color.blue);
        g.fill3DRect(snake_head.x*tileSize, snake_head.y*tileSize, tileSize, tileSize, true);

        //draw snake_body segments
         for(int i=0; i<snake_body.size(); i++){
             Tile body = snake_body.get(i);
             g.setColor(Color.green);
             g.fill3DRect(body.x*tileSize, body.y*tileSize, tileSize, tileSize, true);
         }

         //draw food
         g.setColor(Color.yellow);
         g.fillRect(food.x* tileSize, food.y* tileSize, tileSize, tileSize);

         //draw score and "game over"
         g.setFont(new Font("Arial", Font.PLAIN, 16));
         if(gameOver){
             g.setColor(Color.red);
             g.drawString("Game Over: " + String.valueOf(snake_body.size()), tileSize, tileSize);
         }
         else{
             g.setColor(Color.green);
             g.drawString("Score: " + String.valueOf(snake_body.size()), tileSize, tileSize);
         }
     }

    //function to randomly set food x and y coordinates
    private void placeFood() {
        food.x = rand.nextInt(boardWidth/ tileSize);
        food.y = rand.nextInt(boardHeight/ tileSize);
    }

    //method to move the snake
    public void move(){
        // FIRST  detect if a collison happens between the snake_head and food
        if(collison(snake_head, food)){
            //add a new segment to the snake_body ArrayList(without attaching it)
            snake_body.add(new Tile(food.x, food.y));
            //place another food Tile to replace the eaten one
            placeFood();
        }
        //attach the new segment to the snake's body
        for(int i = snake_body.size()-1; i>=0; i--){
            Tile segment = snake_body.get(i);
            if(i == 0){ //for the first segment
                segment.x = snake_head.x;
                segment.y = snake_head.y;
            }
            else{ //for the rest of the segments
                Tile prevSegment = snake_body.get(i-1);
                segment.x = prevSegment.x;
                segment.y = prevSegment.y;
            }
        }
        //change snake_head coordinates(move snake_head)
        snake_head.x += velocityX;
        snake_head.y += velocityY;

        //If snakes head collides with the body
        for(int i=0; i<snake_body.size(); i++){
            Tile part = snake_body.get(i);
            if(collison(snake_head, part)){
                gameOver = true;
            }
        }
        //If the snake head touches the walls
        if(snake_head.x*tileSize < 0 || snake_head.x*tileSize > boardWidth
            || snake_head.y*tileSize < 0 || snake_head.y*tileSize > boardWidth){
            gameOver = true;
        }
    }

    //method to detect collision
    public boolean collison(Tile tile1, Tile tile2){
        if(tile1.x == tile2.x && tile1.y == tile2.y){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move(); // update the x and y coordinates of the snake, (make it move)
        repaint(); //this method calls the paint() method, (we don't need to call it)

        if(gameOver){
            game_loop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //control the snake's movement
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP:
                if (velocityY != 1 /*this allows the snake not to move into it's body*/){
                    velocityX = 0;
                    velocityY = -1;
                }
            break;

            case KeyEvent.VK_DOWN:
                if(velocityY != -1 /*this allows the snake not to move into it's body*/) {
                    velocityX = 0;
                    velocityY = 1;
                }
            break;

            case KeyEvent.VK_LEFT:
                if(velocityX != 1 /*this allows the snake not to move into it's body*/) {
                    velocityX = -1;
                    velocityY = 0;
                }
            break;

            case KeyEvent.VK_RIGHT:
                if(velocityX != -1 /*this allows the snake not to move into it's body*/) {
                    velocityX = 1;
                    velocityY = 0;
                }
            break;
        }
    }
    //We do not need these methods, but will keep since we are implementing the KeyListener interface
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

}
