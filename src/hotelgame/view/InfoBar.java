package hotelgame.view;

import hotelgame.model.GameModel;

import javax.swing.*;
import java.awt.*;

public class InfoBar {

    /**
     * The panel for this info bar
     */
    JPanel panel = new JPanel();

    /**
     * The turn label
     */
    private final JLabel turnLabel = new JLabel("Turn: ");

    /**
     * The label for player one's money
     */
    private final JLabel playerOneMoneyLabel = new JLabel("Player One Money:");

    /**
     * The label for player two's money
     */
    private final JLabel playerTwoMoneyLabel = new JLabel("Player Two Money:");

    /**
     * The label for the roll result
     */
    private final JLabel rollResultLabel = new JLabel("Roll: ");

    public InfoBar() {
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));

        panel.add(turnLabel);
        panel.add(playerOneMoneyLabel);
        panel.add(playerTwoMoneyLabel);
        panel.add(rollResultLabel);
    }

    /**
     * Update the info bar from the hotel model
     * @param model The game model
     */
    public void update (GameModel model) {
        this.setTurnLabel(model.getCurrentTurn().getName());
        this.setPlayerOneMoneyLabel("£" + model.getPlayerOne().getMoney());
        this.setPlayerTwoMoneyLabel("£" + model.getPlayerTwo().getMoney());
        this.setRollResultLabel(model.getCurrentRoll() + "");
    }

    /**
     * Get the panel for the info bar
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * Set the turn label
     * @param turnLabel The player whose turn it now is
     */
    private void setTurnLabel(String turnLabel) {
        this.turnLabel.setText("Turn: " + turnLabel);
    }

    /**
     * Set the label for player one's money
     * @param playerOneMoneyLabel The money for player one
     */
    private void setPlayerOneMoneyLabel(String playerOneMoneyLabel) {
        this.playerOneMoneyLabel.setText("Player One Money: " + playerOneMoneyLabel);
    }

    /**
     * Set the label for player two's money
     * @param playerTwoMoneyLabel The money for player two
     */
    private void setPlayerTwoMoneyLabel(String playerTwoMoneyLabel) {
        this.playerTwoMoneyLabel.setText("Player Two Money: " + playerTwoMoneyLabel);
    }

    /**
     * Set the label for player three's money
     * @param rollResultLabel The money for player three
     */
    private void setRollResultLabel(String rollResultLabel) {
        this.rollResultLabel.setText("Roll: " + rollResultLabel);
    }
}
