import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Class for painting game objects.
 * 
 * @author Sam Patterson
 * @version 11/27/2021
 */
public class Renderer extends JPanel {
    private static final long serialVersionUID = 1L;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        FlappyBird.flappyBird.repaint(g);
    }
}
