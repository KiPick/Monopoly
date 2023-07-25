package hotelgame.view;

import javax.swing.*;
import java.awt.*;

public class PlayerIndicator extends JComponent {

    /**
     * The Color of this player
     */
    private final Color color;

    private static final long serialVersionUID = 1L;

    /**
     * Initialize a Player Indicator with a Color
     * @param color The color of this player indicator
     */
    public PlayerIndicator(Color color) {
        this.color = color;
        setPreferredSize(new Dimension(15, 15));
    }

    /**
     * Paint a circle on this component if it is enabled.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.isEnabled()) {
            g.setColor(this.color);
            g.fillOval(0, 0, 15, 15);
        }
    }
}