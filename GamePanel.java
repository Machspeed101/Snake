import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH =600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25; //size of items in the game
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE; //how many objects can fit on the screen
	static final int DELAY = 75; // delay for the timer
	final int x[] = new int[GAME_UNITS];// holds all X coordinates of the body parts
	final int y[] = new int[GAME_UNITS];// holds all Y coordinates of the body parts
	int bodyParts = 6;
	int applesEaten;
	int appleX; // X coordinate of where apple would be located
	int appleY; // Y coordinate of where apple would be located
	char direction = 'R';
	boolean running = false; // checks if the game is running
	Timer timer;
	Random random;
	
	
	GamePanel(){				// constructs the panel
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.green);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		
	}
	public void startGame( ) {		//starts game
		newApple(); //creates new apple on the screen
		running = true;
		timer = new Timer(DELAY,this);
		timer.start(); //starts timer
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		
	}
	public void draw(Graphics g) {
			if(running) {
			
				g.setColor(Color.red);
				g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);// draws apple at randomised coordinate
		
				for(int i = 0; i<bodyParts;i++) {		
					if(i ==0) {
						g.setColor(Color.blue);			//draws head of snake
						g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
					}
					else {
						g.setColor(Color.cyan);			//draws body parts of the snake
						g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
					}
				}
				g.setColor(Color.black);
				g.setFont(new Font("Ink Free", Font.BOLD, 40));
				FontMetrics metric = getFontMetrics(g.getFont());
				g.drawString("Score: " +applesEaten, (SCREEN_WIDTH - metric.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
				
			}
			else {
				gameOver(g);
			}
		}
	public void newApple() {		//generate coordinate of the apple when method is called
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move() {			//how the snake moves
		for (int i = bodyParts;i>0;i--) {
			x[i] =x[i-1];
			y[i] =y[i-1];
		}
		switch(direction) {			//different directions the snake could go
		case 'U' :
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D' :
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'R' :
			x[0] = x[0] + UNIT_SIZE;
			break;
		case 'L' :
			x[0] = x[0] - UNIT_SIZE;
			break;
		}
	}
	public void checkApple() {					//checks if apple is eaten
		if((x[0]== appleX) && y[0] == appleY) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
		
		
	}
	public void checkCollisions() {				//checks if head collides with body
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		if(x[0] < 0) {							//checks if head touches left border
			running = false;
		}
		if(x[0] > SCREEN_WIDTH) {				//checks if head touches right border
			running = false;
		}
		if(y[0] < 0) {							//checks if head touches top border
			running = false;
		}
		if(y[0] > SCREEN_HEIGHT) {				//checks if head touches bottom border
			running = false;
		}
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) { 			//game over text
		g.setColor(Color.black);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score: " +applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		
		g.setColor(Color.black);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metric = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metric.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkApple();
			checkCollisions();
			
		}
		repaint();
		
	}
    public class MyKeyAdapter extends KeyAdapter{
    	@Override
    	public void keyPressed(KeyEvent e) {
    		switch(e.getKeyCode()) {
    		case KeyEvent.VK_LEFT: 				//moves snake left
    			if(direction != 'R') {
    				direction = 'L';
    			}
    			break;
    	case KeyEvent.VK_RIGHT:					//moves snake right
			if(direction != 'L') {
				direction = 'R';
			}
			break;
    	case KeyEvent.VK_UP:					//moves snake up
			if(direction != 'D') {
				direction = 'U';
			}
			break;
    	case KeyEvent.VK_DOWN:					//moves snake down
			if(direction != 'U') {
				direction = 'D';
			}
			break;
    		}
    	}
    }
}
