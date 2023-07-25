package hotelgame.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class GameModel extends Observable {

    /**
     * The maximum number of tiles on this board.
     */
    public final static int MAX_TILES = 40;

    /**
     * The total amount of sides for the die.
     */
    private final int DICE_ROLL = 12;

    /**
     * The Player whose turn it currently is
     */
    private Player currentTurn;

    /**
     * The first player.
     */
    private Player playerOne;

    /**
     * The second player.
     */
    private Player playerTwo;

    /**
     * All the tiles in the game.
     * There are always 40 total.
     * If a tile is null, there is not a Hotel located there.
     */
    private Hotel[] tiles;

    /**
     * A HashMap of Hotel group to an array of hotels in the group
     * Example: "A" -> ["A1", "A2", "A3"]
     */
    private final HashMap<String, ArrayList<Hotel>> hotelGroups = new HashMap<>();

    /**
     * The latest dice roll by the current player.
     */
    private int currentRoll;

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    /**
     * Roll the dice for the current player.
     */
    public int rollDice() {
        currentRoll = (int) Math.ceil(Math.random() * DICE_ROLL);
        assert currentRoll >= 1 && currentRoll <= DICE_ROLL;
        return currentRoll;
    }

    /**
     * Determine if the game is over by checking if either player has 0 money left.
     * @return boolean
     */
    public boolean isGameOver() {
        return playerOne.getMoney() <= 0 || playerTwo.getMoney() <= 0;
    }

    /**
     * Get the winner of the game
     * @return The player that still has money remaining.
     */
    public Player getWinner() {
        // Ensure the game is over
        assert isGameOver();

        return playerOne.getMoney() > 0 ? playerOne : playerTwo;
    }

    /**
     * Buy a hotel for the current player.
     * @return String the result of attempting to buy the hotel
     */
    public boolean buyHotel() {
        Hotel hotel = this.getCurrentPlayerPositionHotel();
        Player  player = this.getCurrentTurn();

        // the hotel should not have an owner & the player should be able to afford the hotel.
        assert hotel != null && hotel.getOwner() == null && hotel.canAffordPurchase(player);

        // purchase the hotel
        player.deductMoney(hotel.getPrice());
        if (player.getMoney() > 0) {
            hotel.setOwner(player);
        }

        // The hotel owner should now be the current player.
        assert hotel.getOwner() == player;

        this.setChanged();
        notifyObservers();
        return true;
    }

    /**
     * Increase the rating of a hotel.
     * The current player will be the one increasing.
     */
    public boolean increaseStarRating() {
        Hotel hotel = this.getCurrentPlayerPositionHotel();
        // Ensure player owns hotel.
        assert hotel != null && hotel.getOwner() == currentTurn;
        // Ensure hotel rating is less than 5 before attempting increase.
        assert hotel.getStarRating() < 5;

        if (hotel.increaseStarRating()) {
            this.setChanged();
            this.notifyObservers();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Pay overnight fees for the current player.
     */
    public double payOvernightFee() {
        Hotel hotel = this.getCurrentPlayerPositionHotel();
        Player player = this.getCurrentTurn();

        // Ensure this hotel has an owner that is not the current player.
        assert hotel != null && hotel.getOwner() != null && hotel.getOwner() != player;

        double overnightCost = calculateCurrentHotelFee();
        if (overnightCost > 0) {
            player.deductMoney(overnightCost);
            this.getOpposingTurn().addMoney(overnightCost);
            this.setChanged();
            this.notifyObservers();
            return overnightCost;
        }
        return 0;
    }

    /**
     * Calculate the current player's hotel fees.
     * @return the calculated overnight fee
     *  The fee is 200% if the surrounding hotels are owned by the opponent.
     *  The fee is 50% if the surrounding hotels are owned by the player.
     */
    private double calculateCurrentHotelFee() {
        Hotel hotel = this.getCurrentPlayerPositionHotel();
        Player player = this.getCurrentTurn();

        if (hotel == null || hotel.getOwner() == player) {
            return 0;
        }

        Player opposingPlayer = this.getOpposingTurn();
        double overnightCost = hotel.getOvernightFee();
        List<Hotel> hotelsInGroup = hotelGroups.get(hotel.getGroup());

        boolean anyOwnedByPlayer = false;
        boolean allOthersOwnedByOpposingPlayer = true;
        for (Hotel hotelInGroup : hotelsInGroup) {
            if (hotelInGroup.getOwner() == player) {
                anyOwnedByPlayer = true;
            } else if (hotelInGroup.getOwner() != opposingPlayer) {
                allOthersOwnedByOpposingPlayer = false;
            }
        }

        if (anyOwnedByPlayer) {
            overnightCost /= 2;
        } else if (allOthersOwnedByOpposingPlayer) {
            overnightCost *= 2;
        }

        return overnightCost;
    }

    /**
     * Move the current player.
     * @param amount The amount of tiles to move.
     */
    public void movePlayer(int amount) {
        int currentPosition = this.getCurrentTurn().getPosition();
        int newPosition = currentPosition + amount;
        if (newPosition > MAX_TILES - 1) {
            newPosition -= MAX_TILES;
        }
        this.getCurrentTurn().setPosition(newPosition);

        // Ensure the player moved positions
        assert this.getCurrentTurn().getPosition() == newPosition;

        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Shift the turn to the next player.
     */
    public void nextTurn() {
        if (this.currentTurn == this.playerOne) {
            this.currentTurn = this.playerTwo;
        } else {
            this.currentTurn = this.playerOne;
        }
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Get the hotel (or null) at the current player's position
     * @return The hotel or null value at the current player's position
     */
    public Hotel getCurrentPlayerPositionHotel() {
        return tiles[this.getCurrentTurn().getPosition()];
    }

    /**
     * Create two players for the game.
     * @param playerOneName The name of player one
     * @param playerTwoName The name of player two
     */
    public void createPlayers(String playerOneName, String playerTwoName) {
        playerOne = new Player(playerOneName);
        playerTwo = new Player(playerTwoName);
        currentTurn = Math.ceil(Math.random() * 2) == 1 ? playerOne : playerTwo;
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Reset this model to the beginning of a new game.
     */
    public void reset() {
        playerOne = null;
        playerTwo = null;
        currentTurn = null;

        tiles = generateTiles();
        this.notifyObservers();
    }

    /**
     * Generate the game's Hotel tiles.
     * @return The generated tiles array
     */
    private Hotel[] generateTiles() {
        Hotel[] tiles = new Hotel[MAX_TILES];
        char startingHotelLetter = 65;
        for(int hotelLetter = 0; hotelLetter < 8; hotelLetter++) {
            int hotelNumber = 1;
            for (int tileNumber = 0; tileNumber < 5; tileNumber++) {
                int tilePosition = (hotelLetter * 5) + tileNumber;
                if (tileNumber == 0 || tileNumber == 2) {
                    tiles[tilePosition] = null;
                } else {
                    int price = (hotelLetter + 1) * 50 + (tileNumber == 4 ? 20 : 0);
                    String hotelGroup = String.valueOf((char) (startingHotelLetter + hotelLetter));
                    tiles[tilePosition] = new Hotel(hotelGroup, hotelNumber, price);
                    addHotelToGroup(tiles[tilePosition]);
                    hotelNumber++;
                }
            }
        }
        return tiles;
    }

    /**
     * Add a Hotel to the hotelGroup hashmap.
     * @param hotel The hotel to add
     */
    private void addHotelToGroup(Hotel hotel) {
        if (hotelGroups.containsKey(hotel.getGroup())) {
            hotelGroups.get(hotel.getGroup()).add(hotel);
        } else {
            ArrayList<Hotel> hotelGroup = new ArrayList<>();
            hotelGroup.add(hotel);
            hotelGroups.put(hotel.getGroup(), hotelGroup);
        }
    }

    /**
     * Get the current turn's player.
     * @return The current turn's player
     */
    public Player getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Get the current turn's opposing player.
     * @return The player whose turn it is not
     */
    public Player getOpposingTurn() {
        return currentTurn == playerOne ? playerTwo : playerOne;
    }


    /**
     * Get the current player's dice roll.
     * @return The latest dice roll
     */
    public int getCurrentRoll() {
        return currentRoll;
    }

    /**
     * Get the current hotel tiles.
     * @return The board tiles
     */
    public Hotel[] getTiles() {
        return tiles;
    }
}