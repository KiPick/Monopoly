package hotelgame.model.tests;

import hotelgame.model.GameModel;
import hotelgame.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    /**
     * Test if the game is over.
     * Set player one's money to £0
     * Set player two's money to £100
     * Assert if the game is over.
     */
    @Test
    void testGameOver() {
        GameModel model = new GameModel();
        model.createPlayers("one", "two");
        model.getPlayerOne().setMoney(0);
        model.getPlayerTwo().setMoney(100);

        assertTrue(model.isGameOver());
    }

    @Test
    void testRollDice() {
        GameModel model = new GameModel();
        model.createPlayers("one", "two");
        int rollResult = model.rollDice();

        assertTrue(rollResult >= 1 && rollResult <= 12);
    }


    /**
     * Test if the model switches which player's turn it is.
     * Create two players
     * Save the state of the current turn in a local variable
     * Call model function to alternate urns
     * Assert the current turn is not the same
     */
    @Test
    void testNextTurn() {
        GameModel model = new GameModel();
        model.createPlayers("one", "two");
        Player currentTurn = model.getCurrentTurn();
        model.nextTurn();

        assertNotSame(currentTurn, model.getCurrentTurn());
    }

    /**
     * Test player movement
     * Create two players
     * Get the player of the current turn
     * Move this player 10 spaces
     * Assert the players position is now on tile 10
     */
    @Test
    void testMovePlayer() {
        GameModel model = new GameModel();
        model.createPlayers("one", "two");
        Player currentTurn = model.getCurrentTurn();
        model.movePlayer(10);

        assertEquals(currentTurn.getPosition(), 10);
    }

    /**
     * Test buying a hotel
     * Move a player to hotel A1
     * Attempt to buy the hotel
     * Assert a successful buy message and cost
     */
    @Test
    void testBuyHotel() {
        GameModel model = new GameModel();
        model.reset();
        model.createPlayers("one", "two");
        Player currentTurn = model.getCurrentTurn();
        model.movePlayer(1);

        boolean result = model.buyHotel();
        assertTrue(result);
        assertEquals(currentTurn, model.getCurrentPlayerPositionHotel().getOwner());
    }

    /**
     * Test buy hotel failure
     * Set the player of the current turn's money to £0
     * Move player to hotel A1
     * Attempt to buy hotel
     * Assert failure message
     */
    @Test
    void testBuyHotelFailure() {
        GameModel model = new GameModel();
        model.reset();
        model.createPlayers("one", "two");
        Player currentTurn = model.getCurrentTurn();
        currentTurn.setMoney(0);
        model.movePlayer(1);

        assertThrows(AssertionError.class, model::buyHotel);
    }

    /**
     * Test increasing a hotel's star rating
     * Move the current turn's player to A1
     * Buy the A1 hotel
     * Attempt to increase the star rating
     * Assert success message
     */
    @Test
    void testIncreaseStarRating() {
        GameModel model = new GameModel();
        model.reset();
        model.createPlayers("one", "two");
        model.movePlayer(1);
        model.buyHotel();

        boolean result = model.increaseStarRating();
        assertTrue(result);
    }

    /**
     * Test increasing a hotel's star rating
     * Move the current turn's player to A1
     * Buy the A1 hotel
     * Set player's money to £0
     * Attempt to increase the star rating
     * Assert failure message
     */
    @Test
    void testIncreaseStarRatingFailure() {
        GameModel model = new GameModel();
        model.reset();
        model.createPlayers("one", "two");
        model.movePlayer(1);
        model.buyHotel();
        model.getCurrentTurn().setMoney(0);

        boolean result = model.increaseStarRating();
        assertFalse(result);
        assertEquals(0, model.getCurrentPlayerPositionHotel().getStarRating());
    }

    /**
     * Test increase hotel rating assert owner failure
     * Set the player of the current turn's money to £0
     * Move player to hotel A1
     * Attempt to increase teh hotel rating
     * Assert failure message
     */
    @Test
    void testIncreaseStarRatingAssertOwner() {
        GameModel model = new GameModel();
        model.reset();
        model.createPlayers("one", "two");
        model.movePlayer(1);

        assertThrows(AssertionError.class, model::increaseStarRating);
    }

    /**
     * Move current player to A1
     * Buy A1 Hotel
     * Increase A1 star rating to 2/5 stars
     * Switch to other player's turn
     * Move other player to A1
     * Attempt to pay overnight fees
     * Assert successful message
     * Assert other player's money was decreased
     * Assert first player's money was increased
     */
    @Test
    void testPayOvernightFee() {
        GameModel model = new GameModel();
        model.reset();
        model.createPlayers("one", "two");
        model.movePlayer(1);
        model.buyHotel();
        model.increaseStarRating();
        model.increaseStarRating();
        model.nextTurn();
        model.movePlayer(1);

        double amount = model.payOvernightFee();
        // 50 * 0.10 * 2^2 = 20
        assertEquals(20.0, amount);
        assertEquals(1980, model.getCurrentTurn().getMoney());
        assertEquals(1920, model.getOpposingTurn().getMoney());
    }

    /**
     * Move current turn's player to A1
     * Buy A1 hotel
     * Switch to other player's turn
     * Move other player to A1
     * Assert other player does not pay fee due to 0 star rating
     */
    @Test
    void testPayOvernightFeeNoRating() {
        GameModel model = new GameModel();
        model.reset();
        model.createPlayers("one", "two");
        model.movePlayer(1);
        model.buyHotel();

        model.nextTurn();
        model.movePlayer(1);

        double amount = model.payOvernightFee();
        assertEquals(0, amount);
    }

    /**
     * Move current player to A2
     * Buy A2 Hotel
     * Increase A2 star rating to 2/5 stars
     * Switch to other player's turn
     * Move other player to A1
     * Buy A1 Hotel
     * Move to A2 Hotel
     * Attempt to pay overnight fees
     * Assert fee is halved
     * Assert other player's money was decreased
     * Assert first player's money was increased
     */
    @Test
    void testPayOvernightFeeHalf() {
        GameModel model = new GameModel();
        model.reset();
        model.createPlayers("one", "two");
        model.movePlayer(3);
        model.buyHotel();
        model.increaseStarRating();
        model.increaseStarRating();

        model.nextTurn();
        model.movePlayer(1);
        model.buyHotel();
        model.movePlayer(2);

        double amount = model.payOvernightFee();
        // 50 * 0.10 * 2^2 / 2 = 10
        assertEquals(10.0, amount);
        assertEquals(1940, model.getCurrentTurn().getMoney());
        assertEquals(1910, model.getOpposingTurn().getMoney());
    }

    /**
     * Move current player to A1
     * Buy A1 Hotel
     * Increase A1 star rating to 1/5 stars
     * Move current player to A2
     * Buy A2 Hotel
     * Increase A2 star rating to 1/5 stars
     * Move current player to A3
     * Buy A3 Hotel
     * Increase A3 star rating to 1/5 stars
     * Switch to other player's turn
     * Move other player to A1
     * Attempt to pay overnight fees
     * Assert fee is doubled
     * Assert other player's money was decreased
     * Assert first player's money was increased
     */
    @Test
    void testPayOvernightFeeDouble() {
        GameModel model = new GameModel();
        model.reset();
        model.createPlayers("one", "two");
        model.movePlayer(1);
        model.buyHotel();
        model.increaseStarRating();
        model.movePlayer(2);
        model.buyHotel();
        model.increaseStarRating();
        model.movePlayer(1);
        model.buyHotel();
        model.increaseStarRating();

        model.nextTurn();
        model.movePlayer(1);

        double amount = model.payOvernightFee();
        // 50 * 0.10 * 1^2 * 2 = 10
        assertEquals(10.0, amount);
        assertEquals(1990, model.getCurrentTurn().getMoney());
        assertEquals(1755, model.getOpposingTurn().getMoney());
    }

    /**
     * Test failure of paying overnight fee if hotel has no owner
     * Move current turn's player to A1
     * Attempt to pay overnight fee
     * Assert AssertionError thrown for no hotel owner
     */
    @Test
    void testPayOvernightFeeAssertOwner() {
        GameModel model = new GameModel();
        model.reset();
        model.createPlayers("one", "two");
        model.movePlayer(1);

        assertThrows(AssertionError.class, model::payOvernightFee);
    }
}