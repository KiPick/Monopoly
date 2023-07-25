package hotelgame.view;

import hotelgame.model.GameModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Board {

    /**
     * THe panel for the board.
     */
    JPanel panel = new JPanel();

    /**
     * The top row of the board.
     */
    JPanel topRow = new JPanel(new GridLayout(1, 11));

    /**
     * The bottom row of the board.
     */
    JPanel bottomRow = new JPanel(new GridLayout(1, 11));

    /**
     * The left column of the board.
     */
    JPanel leftColumn = new JPanel(new GridLayout(9, 1));

    /**
     * The right column of the board.
     */
    JPanel rightColumn = new JPanel(new GridLayout(9, 1));

    /**
     * An array of all the board squares/tiles.
     */
    private final BoardSquare[] boardSquares = new BoardSquare[GameModel.MAX_TILES];

    /**
     * Create a board of empty squares.
     */
    public Board() {
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(1070, 850));

        ArrayList<BoardSquare> bottomList = new ArrayList<>();
        ArrayList<BoardSquare> leftList = new ArrayList<>();

        for (int i = 0; i < GameModel.MAX_TILES; i++) {
            BoardSquare square = new BoardSquare(i);
            boardSquares[i] = square;
            if (i < 11) {
                bottomList.add(square);
                topRow.add(square);
            } else if (i < 20) {
                leftList.add(square);
            } else if (i < 31) {
                topRow.add(square);
            } else {
                rightColumn.add(square);
            }
        }

        Collections.reverse(bottomList);
        for(BoardSquare square : bottomList) {
            bottomRow.add(square);
        }

        Collections.reverse(leftList);
        for(BoardSquare square : leftList) {
            leftColumn.add(square);
        }

        panel.add(topRow, BorderLayout.NORTH);
        panel.add(bottomRow, BorderLayout.SOUTH);
        panel.add(leftColumn, BorderLayout.WEST);
        panel.add(rightColumn, BorderLayout.EAST);
    }

    /**
     * Update this board from the hotel model.
     * @param model The game model
     */
    public void update (GameModel model) {
        for (int i = 0; i < GameModel.MAX_TILES; i++) {
            BoardSquare square = boardSquares[i];
            if (model.getTiles()[i] != null) {
                square.updateHotel(model.getTiles()[i]);
            }

            if (model.getPlayerOne().getPosition() == i) {
                square.enablePlayerOne();
            } else {
                square.disablePlayerOne();
            }

            if (model.getPlayerTwo().getPosition() == i) {
                square.enablePlayerTwo();
            } else {
                square.disablePlayerTwo();
            }
        }
    }

    /**
     * Get the panel for this board.
     * @return The panel for the Board
     */
    public JPanel getPanel() {
        return panel;
    }
}
