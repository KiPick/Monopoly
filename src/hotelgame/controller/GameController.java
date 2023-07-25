package hotelgame.controller;

import hotelgame.model.Hotel;
import hotelgame.model.GameModel;
import hotelgame.view.GameView;

/**
 * The Hotel controller
 */
public class GameController {

    /**
     * The reference to the HotelModel.
     */
    private final GameModel model;

    /**
     * The reference to the HotelView.
     */
    private final GameView view;

    public GameController(GameView view) {
        this.model = new GameModel();
        this.view = view;
        this.model.addObserver(this.view);
    }

    /**
     * Start a game.
     */
    public void start() {
        this.model.reset();
        this.model.createPlayers("Player One", "Player Two");
    }

    /**
     * Roll the current player's turn.
     */
    public String rollTurn(int amount) {
        int diceRoll = amount > 0 ? amount : model.rollDice();
        model.movePlayer(diceRoll);

        if (model.getCurrentPlayerPositionHotel() != null) {
            return getHotelResult();
        }
        return model.getCurrentTurn().getName() + " moved " + diceRoll + " and landed on an empty space.";
    }

    /**
     * Get the current player's hotel options.
     * @return The result of hotel actions
     */
    public String getHotelResult() {
        Hotel hotel = model.getCurrentPlayerPositionHotel();
        if (hotel.getOwner() == model.getCurrentTurn()) {
            if (hotel.getStarRating() < 5) {
                view.getActionBar().enableIncreaseRating();
            }
            return model.getCurrentTurn().getName() + " landed at your " + hotel.getName() + " hotel. The current rating of your hotel is " + hotel.getStarRating() + "/5 stars.";
        } else if (hotel.getOwner() == model.getOpposingTurn()) {
            double amountPaid = model.payOvernightFee();
            String result = amountPaid > 0 ?
                    "You paid £" + amountPaid + " to stay at this hotel." :
                    "You did not pay to stay at this hotel because its rating is 0.";
            return model.getCurrentTurn().getName() + " landed at " + model.getOpposingTurn().getName() + "'s " + hotel.getName() + " hotel. " + result;
        } else {
            if (hotel.canAffordPurchase(model.getCurrentTurn())) {
                view.getActionBar().enableBuyHotel();
            }
            return model.getCurrentTurn().getName() + " landed at the un-purchased " + hotel.getName() + " hotel.";
        }
    }

    /**
     * Choose to buy a hotel.
     * @return If it was successful
     */
    public String buyHotel() {
        Hotel hotel = model.getCurrentPlayerPositionHotel();
        if(!hotel.canAffordPurchase(model.getCurrentTurn())) {
            return "You cannot afford this hotel";
        }
        model.buyHotel();
        view.getActionBar().disableBuyHotel();

        if (hotel.getOwner() == model.getCurrentTurn()) {
            view.getActionBar().enableIncreaseRating();
        }
        return "You successfully purchased the " + hotel.getName() + " hotel for £" + hotel.getPrice() + ".";
    }

    public String increaseRating(int increaseBy) {
        Hotel hotel = model.getCurrentPlayerPositionHotel();

        if (hotel.getOwner() != model.getCurrentTurn()) {
            return "You do not own this hotel";
        } else if (!hotel.canAffordRatingIncrease()) {
            return "You cannot afford to increase the rating of this hotel.";
        }

        if (hotel.getStarRating() + increaseBy > 5) {
            return "Increasing the rating by this amount would exceed the maximum rating of 5.";
        }

        for (int i = 0; i < increaseBy; i++) {
            if (!hotel.canAffordRatingIncrease()) {
                view.getActionBar().disableIncreaseRating();
                return "You increase the hotel rating to " + hotel.getStarRating() + ". You cannot afford to increase the rating of this hotel any higher.";
            }
            model.increaseStarRating();
        }

        if (hotel.getStarRating() >= 5) {
            view.getActionBar().disableIncreaseRating();
        }

        return "You increase the hotel rating to " + hotel.getStarRating() + " /5 stars.";
    }



    /**
     * End the current player's turn.
     */
    public String endTurn() {
        double money = model.getCurrentTurn().getMoney();
        String player = model.getCurrentTurn().getName();
        view.getActionBar().disableBuyHotel();
        view.getActionBar().disableIncreaseRating();
        view.getActionBar().disableEndTurnButton();

        if (model.isGameOver()) {
            view.getActionBar().disableRollButton();
            return model.getCurrentTurn().getName() + " have ran out of money. Game over! " + model.getOpposingTurn().getName() + " wins!";
        }
        view.getActionBar().enableRollButton();
        model.nextTurn();
        return player + "'s turn has ended with £" + money + ".";
    }

}