package hotelgame.view;

import hotelgame.model.Hotel;

import javax.swing.*;
import java.awt.*;

public class BoardSquare extends JPanel {

    /**
     * The Hotel on this square
     */
    private Hotel hotel;

    /**
     * The Hotel name label
     */
    private final JLabel nameLabel;

    /**
     * The Hotel price & rating label
     */
    private final JLabel priceRatingLabel;

    /**
     * The owner label
     */
    private final JLabel ownerLabel;

    /**
     * The indicator for player one
     */
    private final PlayerIndicator playerOneIndicator = new PlayerIndicator(Color.RED);

    /**
     * The indicator for player two
     */
    private final PlayerIndicator playerTwoIndicator = new PlayerIndicator(Color.BLUE);

    /**
     * Initialize a BoardSquare with a position and setup components
     * @param position The position of this board square.
     */
    public BoardSquare(int position) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(100, 100));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        nameLabel = new JLabel(position == 0 ? "GO" : "", SwingConstants.CENTER);
        priceRatingLabel = new JLabel("", SwingConstants.CENTER);
        ownerLabel = new JLabel("", SwingConstants.CENTER);

        playerOneIndicator.setEnabled(false);
        playerTwoIndicator.setEnabled(false);

        JPanel indicatorPanel = new JPanel();
        indicatorPanel.setLayout(new BoxLayout(indicatorPanel, BoxLayout.X_AXIS));
        indicatorPanel.add(playerOneIndicator);
        indicatorPanel.add(playerTwoIndicator);

        add(nameLabel);
        add(priceRatingLabel);
        add(ownerLabel);
        add(indicatorPanel);
    }

    /**
     * Set the Hotel of this square
     * @param hotel The reference to this square's hotel.
     */
    public void updateHotel(Hotel hotel) {
        this.hotel = hotel;
        nameLabel.setText(hotel.getName());
        priceRatingLabel.setText("£" + hotel.getPrice() + " - " + hotel.getStarRating() + "/5");
        if (hotel.getOwner() != null) {
            ownerLabel.setText(hotel.getOwner().getName());
        }
    }

    /**
     * Get the Hotel of this square
     * @return this square's hotel
     */
    public Hotel getHotel() {
        return this.hotel;
    }

    /**
     * Enable player one indicator
     */
    public void enablePlayerOne() {
        this.playerOneIndicator.setEnabled(true);
    }

    /**
     * Disable player one indicator
     */
    public void disablePlayerOne() {
        this.playerOneIndicator.setEnabled(false);
    }

    /**
     * Enable player two indicator
     */
    public void enablePlayerTwo() {
        this.playerTwoIndicator.setEnabled(true);
    }

    /**
     * Disable player two indicator
     */
    public void disablePlayerTwo() {
        this.playerTwoIndicator.setEnabled(false);
    }
}
