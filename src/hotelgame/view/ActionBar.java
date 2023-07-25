package hotelgame.view;

import hotelgame.controller.GameController;

import javax.swing.*;
import java.awt.*;

public class ActionBar {

    /**
     * A cheat mode setting to allow players to choose the amount of spaces to move
     */
    private final boolean cheatMode;

    /**
     * The action bar's panel
     */
    JPanel panel = new JPanel();

    /**
     * A button for the current player to roll their turn
     */
    private final JButton rollButton = new JButton("Roll Dice");

    /**
     * A button for the current player to buy a hotel
     */
    private final JButton buyHotelButton = new JButton("Buy Hotel");

    /**
     * A button for the current player to increase a hotel star rating
     */
    private final JButton increaseRatingButton = new JButton("Increase Rating");

    /**
     * A button for the current player to end their turn
     */
    private final JButton endTurnButton = new JButton("End Turn");

    /**
     * The action result of a player's action
     */
    private final JLabel actionResult = new JLabel("");

    /**
     * Setup the ActionBar components
     * @param controller The game controller
     */
    public ActionBar(GameController controller, boolean cheatMode) {
        this.cheatMode = cheatMode;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
        buttonPanel.add(rollButton);
        buttonPanel.add(buyHotelButton);
        buttonPanel.add(increaseRatingButton);
        buttonPanel.add(endTurnButton);

        JPanel actionResultPanel = new JPanel();
        actionResultPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        actionResultPanel.add(actionResult);

        disableBuyHotel();
        disableIncreaseRating();
        disableEndTurnButton();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(buttonPanel);
        panel.add(actionResultPanel);

        this.setupActions(controller);
    }

    /**
     * Setup the ActionBar action listeners
     * @param controller The game controller
     */
    private void setupActions(GameController controller) {
        this.rollButton.addActionListener(e -> {
            actionResult.setText(controller.rollTurn(
                cheatMode ? getCheatModeInput() : 0
            ));
            disableRollButton();
            enableEndTurnButton();
        });

        this.buyHotelButton.addActionListener(e -> {
            actionResult.setText(controller.buyHotel());
        });

        this.increaseRatingButton.addActionListener(e -> {
            actionResult.setText(controller.increaseRating(getIncreaseRatingInput()));
        });

        this.endTurnButton.addActionListener(e -> {
            actionResult.setText(controller.endTurn());
        });
    }

    /**
     * Prompt the user for cheat mode input to move a specific amount of spaces.
     * @return The validated input from the user.
     */
    private int getCheatModeInput() {
        int amount = 0;
        while (amount == 0) {
            String input = JOptionPane.showInputDialog(panel, "Enter the amount of spaces to move (1-12): ");
            try {
                amount = Integer.parseInt(input);
                amount = Math.min(Math.max(amount, 1), 12);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(panel, "Please enter a valid number between 1 and 12.");
            }
        }
        return amount;
    }

    /**
     * Prompt the user for input to increase the rating.
     * @return The validated input from the user.
     */
    private int getIncreaseRatingInput() {
        int rating = 0;
        while (rating < 1 || rating > 5) {
            String input = JOptionPane.showInputDialog(panel, "Enter the desired rating (1-5): ");
            try {
                rating = Integer.parseInt(input);
                if (rating < 1 || rating > 5) {
                    JOptionPane.showMessageDialog(panel, "Please enter a valid number between 1 and 5.");
                    rating = 0; // reset the rating back to 0 if it's not in the 1-5 range
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(panel, "Please enter a valid number between 1 and 5.");
            }
        }
        return rating;
    }



    /**
     * Get the panel for the action bar
     * @return The panel for the action bar
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * Enable the roll button
     */
    public void enableRollButton() {
        this.rollButton.setEnabled(true);
    }

    /**
     * Enable the buy hotel button
     */
    public void enableBuyHotel() {
        this.buyHotelButton.setEnabled(true);
    }

    /**
     * Enable the increase rating button
     */
    public void enableIncreaseRating() {
        this.increaseRatingButton.setEnabled(true);
    }

    /**
     * Enable the end turn button
     */
    public void enableEndTurnButton() {
        this.endTurnButton.setEnabled(true);
    }

    /**
     * Disable the roll button
     */
    public void disableRollButton() {
        this.rollButton.setEnabled(false);
    }

    /**
     * Disable the buy hotel button
     */
    public void disableBuyHotel() {
        this.buyHotelButton.setEnabled(false);
    }

    /**
     * Disable the increase rating button
     */
    public void disableIncreaseRating() {
        this.increaseRatingButton.setEnabled(false);
    }

    /**
     * Disable the end turn button
     */
    public void disableEndTurnButton() {
        this.endTurnButton.setEnabled(false);
    }
}
