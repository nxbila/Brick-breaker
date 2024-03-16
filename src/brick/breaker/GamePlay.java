package brick.breaker;

import javax.swing.JPanel;
import java.awt.event.KeyListener;
import javax.swing.Timer;

import org.w3c.dom.css.Rect;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;


public class GamePlay extends JPanel implements KeyListener, ActionListener{
    private boolean play = false; //when game starts it shouldn't start by itself
    private int score = 0; //in the start of game score is 0
    private int totalBricks = 21;
    private Timer timer; //setting time of ball for the speed of ball
    private int delay = 8; //speed given to timer
    private int playerX = 310; //starting position of slider
    private MapGenerator map;
    private int paddleWidth = 100;
  
    private int ballposX = 120;
    private int ballposY = 350;

    private int ballXdir = -1;
    private int ballYdir = -2;
    
    public GamePlay(){
        map = new MapGenerator(3,7);//creating object for MapGenerator class
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }
       
    public void paint(Graphics g){
        //background
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, 692, 592);
        //drawing map
        map.Draw((Graphics2D)g);
        //borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);
        //scores
        g.setColor(Color.WHITE);
        g.setFont(new Font ("serif", Font.BOLD, 25));
        g.drawString(""+score, 590, 30);


        //paddle
        // Draw the paddle with the current width
           g.setColor(Color.GREEN);
           g.fillRect(playerX, 550, paddleWidth, 8);
   

        //ball
        g.setColor(Color.yellow);
        g.fillRect(ballposX, ballposY, 20, 20);
        
        if(totalBricks <= 0){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("YOU WON! Scores:"+ score, 190,300);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press ENTER to Restart:", 250,350);
        }

        if(ballposY > 570){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("GAME OVER. Scores:"+ score, 190,300);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press ENTER to Restart:", 250,350);
        }

        g.dispose();

    }
   
  
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(playerX >= 600){
                playerX = 600;
            }
            else{
                moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX <= 10){
                playerX = 10;
            }
            else{
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);
                repaint();
            }
        }
    }
    public void moveRight(){
        play = true;
        playerX += 20;
    }
    public void moveLeft(){
        play = true;
        playerX -= 20;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        //if left or right key is pressed
        if(play){
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX,530,100,8))){
                ballYdir =- ballYdir;
            }
           A: for(int i = 0; i<map.map.length; i++){
                for( int j = 0; j< map.map[0].length; j++){
                    //if map[i][j] is greater than 0 ,then detect intersection
                    if(map.map[i][j] > 0){
                        int brickX = j* map.brickWidth +80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickY);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY,20,20);
                        Rectangle brickRect = rect;
                        
                        if(ballRect.intersects(brickRect)){
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;
                            if(ballposX +19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width){
                                ballXdir = -ballXdir;
                            }else{
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }
            ballposX += ballXdir;
            ballposY += ballYdir;
            //for left border
            if(ballposX < 0){
                ballXdir = -ballXdir;
            }
            //for top border
            if(ballposY < 0){
                ballYdir = -ballYdir;
            }
            //for right border
            if(ballposX > 670){
                ballXdir = -ballXdir;
            }
        }
        repaint();
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }

}
