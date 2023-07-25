package hotelgame.view;

import hotelgame.controller.GameController;
import hotelgame.model.GameModel;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

public class GameView implements Observer {

    /**
     * A cheat mode to allow players to choose how much they move per turn.
     */
    private final boolean cheatMode;

    /**
     * The controller.
     */
    private final GameController controller;

    /**
     * The main Swing frame.
     */
    private final JFrame frame;

    /**
     * The InfoBar containing information for the user.
     */
    private InfoBar infoBar;

    /**
     * The ActionBar containing user actions.
     */
    private ActionBar actionBar;

    /**
     * The graphical representation of the model tiles.
     */
    private Board board;

    /**
     * Setup the game controller and JFrame.
     */
    public GameView(boolean cheatMode) {
        this.cheatMode = cheatMode;
        this.controller = new GameController(this);
        this.frame = new JFrame("Hotel Game");
    }

    /**
     * Set up the Swing frame and start the game.
     */
    public void start() {
        infoBar = new InfoBar();
        actionBar  = new ActionBar(controller, this.cheatMode);
        board = new Board();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.getContentPane().add(infoBar.getPanel());
        frame.getContentPane().add(actionBar.getPanel());
        frame.getContentPane().add(board.getPanel());
        frame.setVisible(true);

        frame.pack();
        frame.setLocationRelativeTo(null);

        this.controller.start();
    }

    /**
     * Update the state of the gui based on the state of the model.
     */
    @Override
    public void update(Observable observable, Object arg) {
        GameModel model = (GameModel) observable;
        infoBar.update(model);
        board.update(model);
    }

    /**
     * Get the action bar.
     * @return The action bar
     */
    public ActionBar getActionBar() {
        return actionBar;
    }
}