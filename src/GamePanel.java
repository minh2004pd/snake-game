import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    private int bodyParts = 6;
    private int applesEaten;
    protected int appleX;
    protected int appleY;
    private char direction = 'R';
    private boolean running = false;
    public final int x[] = new int[constains.GAME_UNIT];
    public final int y[] = new int[constains.GAME_UNIT];
    Timer timer;
    Random random;
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(constains.SCREEN_WIDTH, constains.SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(constains.DELAY,this);
        timer.start();
    }
    public void draw(Graphics g) {
        if(running) {
//            for(int i=0; i<(constains.SCREEN_HEIGHT/constains.UNIT_SIZE); i++) {
//                g.drawLine(i*constains.UNIT_SIZE, 0, i*constains.UNIT_SIZE,constains.SCREEN_HEIGHT);
//                g.drawLine(0, i*constains.UNIT_SIZE,constains.SCREEN_HEIGHT, i*constains.UNIT_SIZE);
//            }
            g.setColor(Color.red);
            g.fillOval(appleX,appleY,constains.UNIT_SIZE,constains.UNIT_SIZE);

            for(int i=0; i<bodyParts; i++) {
                if(i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i],y[i],constains.UNIT_SIZE,constains.UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i],y[i],constains.UNIT_SIZE,constains.UNIT_SIZE);
                }
            }
        }
        else {
            gameOver(g);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void newApple() {
        appleX = random.nextInt((int) (constains.SCREEN_WIDTH/constains.UNIT_SIZE)) * constains.UNIT_SIZE;
        appleY = random.nextInt((int) (constains.SCREEN_HEIGHT/constains.UNIT_SIZE)) * constains.UNIT_SIZE;
    }

    public void move() {
        for(int i=bodyParts; i>0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case 'U':
                y[0] = y[0] - constains.UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + constains.UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - constains.UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + constains.UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            System.out.println(applesEaten);
            newApple();
        }
    }

    public void checkCollision() {
        //check if head collides with body
        for(int i=bodyParts; i>0; i--) {
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        //check if head touches left border
        if(x[0] < 0) {
            running = false;
        }
        //check if head touches right border
        if(x[0] > constains.SCREEN_WIDTH) {
            running = false;
        }
        //check if head touches bottom border
        if(y[0] > constains.SCREEN_WIDTH) {
            running = false;
        }
        //check if head touches top border
        if(y[0] < 0) {
            running = false;
        }

        if(!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        //Game Over Text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten,(constains.SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2,(constains.SCREEN_HEIGHT)/2);
    }

    public class MyKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }
}
