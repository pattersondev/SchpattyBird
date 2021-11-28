import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

/*
 * Flappy Bird game aka Schpatty Bird.
 * 
 * @author Sam Patterson
 * 
 * @version 11/27/2021
 */
public class FlappyBird implements ActionListener, MouseListener {

    public static FlappyBird flappyBird;
    public final int WIDTH = 1200;
    public final int HEIGHT = 800;
    public Renderer renderer;
    public Rectangle bird;
    public ArrayList<Rectangle> bars;
    public Random rand;
    public int ticks;
    public int yMotion;
    public boolean end;
    public boolean started;

    /**
     * Constructor for flappy bird object. Creates JFrame for the games GUI.
     * Creates bird object, the black square used to play the game. Initializes
     * the bars array list, the bars are the objects the user dodges.
     */
    public FlappyBird() {
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);
        renderer = new Renderer();
        rand = new Random();

        jframe.addMouseListener(this);
        jframe.add(renderer);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setResizable(false);
        jframe.setTitle("Schpatty Bird");
        jframe.setSize(WIDTH, HEIGHT);
        jframe.setVisible(true);

        bird = new Rectangle((WIDTH / 2) - 10, (HEIGHT / 2) - 10, 20, 20);
        bars = new ArrayList<Rectangle>();

        addBar(true);
        addBar(true);
        addBar(true);
        addBar(true);

        timer.start();
    }

    /**
     * Adds the bars for the player to dodge when the game starts.
     * 
     * @param begin if game has started or not
     */
    public void addBar(boolean begin) {
        int space = 300;
        int width = 100;
        int height = 50 + rand.nextInt(300);

        if (begin) {
            bars.add(new Rectangle(WIDTH + width + bars.size() * space,
                    HEIGHT - height - 120, width, height));
            bars.add(new Rectangle(WIDTH + width + (bars.size() - 1) * space, 0,
                    width, HEIGHT - height - space));
        } else {
            bars.add(new Rectangle(bars.get(bars.size() - 1).x + (space * 2),
                    HEIGHT - height - 120, width, height));
            bars.add(new Rectangle(bars.get(bars.size() - 1).x, 0, width,
                    HEIGHT - height - space));
        }

    }

    /**
     * Colors in the bars.
     * 
     * @param g graphic
     * @param bar to paint.
     */
    public void paintBars(Graphics g, Rectangle bar) {
        g.setColor(Color.GREEN.darker());
        g.fillRect(bar.x, bar.y, bar.width, bar.height);
    }

    /**
     * Draws all other game objects.
     * 
     * @param g graphic
     */
    public void repaint(Graphics g) {
        g.setColor(Color.cyan);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.orange);
        g.fillRect(0, HEIGHT - 150, WIDTH, 150);

        g.setColor(Color.green);
        g.fillRect(0, HEIGHT - 150, WIDTH, 20);

        g.setColor(Color.black);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);
        for (Rectangle bar : bars) {
            paintBars(g, bar);
        }
        g.setColor(Color.white);
        if (!started) {
            g.setColor(Color.white);
            g.setFont(new Font("Times", 1, 100));
            g.drawString("Click to start", 250, HEIGHT / 2);
        }

        if (end) {
            g.setColor(Color.white);
            g.setFont(new Font("Times", 1, 100));
            g.drawString("You lost :(", 300, HEIGHT / 2);
        }
    }

    /**
     * Main driver for the game, handles most of the logic that makes the game
     * work... ex: the illusion that the game is moving left to right.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        int speed = 5;
        ticks++;

        if (started) {
            for (int i = 0; i < bars.size(); i++) {
                Rectangle bar = bars.get(i);
                bar.x -= speed;
            }
            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2;
            }
            for (int i = 0; i < bars.size(); i++) {
                Rectangle bar = bars.get(i);
                if (bar.x + bar.width < 0) {
                    bars.remove(bar);
                    if (bar.y == 0) {
                        addBar(false);
                    }
                }
            }

            bird.y += yMotion;

            for (Rectangle bar : bars) {
                if (bar.intersects(bird)) {
                    end = true;
                    bird.x = bar.x - bird.width;
                }
            }

            if (bird.y > HEIGHT - 120 || bird.y < 0) {
                end = true;
            }

            if (end) {
                bird.y = HEIGHT - 120 - bird.height;
            }
        }

        renderer.repaint();
    }

    /**
     * Lets the user "flap" which is just moving up by 10 pixels.
     */
    public void flap() {
        if (end) {
            bird = new Rectangle((WIDTH / 2) - 10, (HEIGHT / 2) - 10, 20, 20);
            bars.clear();
            yMotion = 0;
            addBar(true);
            addBar(true);
            addBar(true);
            addBar(true);
            end = false;
        }

        if (!started) {
            started = true;
        } else if (!end) {
            if (yMotion > 0) {
                yMotion = 0;
            }
            yMotion -= 10;
        }
    }

    /**
     * If mouse is clicked call flap method, which will propel the "bird"
     * upwards.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        flap();
    }

    /**
     * Unused.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /**
     * Unused.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /**
     * Unused.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /**
     * Unused.
     */
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    /**
     * Main method for FlappyBird.
     * 
     * @param args from command line.
     */
    public static void main(String[] args) {
        flappyBird = new FlappyBird();
    }
}
