package hotelgame.model.tests;

import hotelgame.model.Hotel;
import hotelgame.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HotelTest {

    /**
     * Test that hotel name is "{group}{number}"
     */
    @Test
    void testHotelName() {
        Hotel hotel = new Hotel("A", 1,60);
        assertEquals("A1", hotel.getName());
    }

    /**
     * Test overnight fee calculation with 0 star rating
     */
    @Test
    void testGetOvernightFeeNoRating() {
        Hotel hotel = new Hotel("A", 1,60);
        assertEquals(0, hotel.getOvernightFee());
    }

    /**
     * Test overnight fee calculation with 1 star rating
     * Create a Hotel and a Player
     * Set the hotel owner to the player
     * Increase hotel rating to 2/5 stars
     * Assert the correct base overnight fee for the hotel
     */
    @Test
    void testGetOvernightFeeWithRating() {
        Hotel hotel = new Hotel("A", 1,60);
        Player player = new Player("Player one");
        hotel.setOwner(player);

        // Increase rating to 2/5 stars.
        hotel.increaseStarRating();
        hotel.increaseStarRating();

        assertEquals(24, hotel.getOvernightFee());
    }

    /**
     * Test fee calculation for increasing hotel rating
     */
    @Test
    void testGetIncreaseRatingFee() {
        Hotel hotel = new Hotel("A", 1,60);
        assertEquals(30, hotel.getIncreaseRatingFee());
    }

    /**
     * Test if a player with enough money can afford a hotel purchase.
     * Create a Hotel and a Player
     * Player will have money of 2000
     * Assert player can afford hotel
     */
    @Test
    void testCanAffordPurchase() {
        Hotel hotel = new Hotel("A", 1,60);
        Player player = new Player("Player one");

        assertTrue(hotel.canAffordPurchase(player));
    }

    /**
     * Test if a player with not enough money can afford a hotel purchase.
     * Create a Hotel and a Player
     * Set the player money to 30
     * Assert player cannot afford hotel
     */
    @Test
    void testCanAffordPurchaseFailure() {
        Hotel hotel = new Hotel("A", 1 ,60);
        Player player = new Player("Player one");
        player.setMoney(30);

        assertFalse(hotel.canAffordPurchase(player));
    }

    /**
     * Test that a hotel with an owner cannot test affording hotel purchase for anothe player.
     * Create a hotel and player one
     * Set hotel owner to player one
     * Create a second player
     * Assert a canAffordPurchase for player two on hotel attempt throws AssertException
     */
    @Test
    void testCanAffordPurchaseAssertOwner() {
        Hotel hotel = new Hotel("A", 1 ,60);
        Player playerOne = new Player("Player one");
        hotel.setOwner(playerOne);
        Player playerTwo = new Player("Player two");
        assertThrows(AssertionError.class, () -> hotel.canAffordPurchase(playerTwo));
    }

    /**
     * Test if a player with enough money can increase the star rating
     * Create a Hotel and a Player
     * Set the hotel owner to the player
     * Assert hotel owner is player
     * Assert player with 2000 money can afford increase rating price
     */
    @Test
    void testCanIncreaseRating() {
        Hotel hotel = new Hotel("A", 1 ,60);
        Player player = new Player("Player one");
        hotel.setOwner(player);

        assertEquals(player, hotel.getOwner());
        assertTrue(hotel.canAffordRatingIncrease());
    }

    /**
     * Test if a player with not enough money can increase the star rating
     * Create a Hotel and a Player
     * Set the hotel owner to the player
     * Set player money to 20
     * Assert hotel owner is player
     * Assert player cannot afford hotel rating increase
     */
    @Test
    void testCanIncreaseRatingFailure() {
        Hotel hotel = new Hotel("A", 1,60);
        Player player = new Player("Player one");
        player.setMoney(20);
        hotel.setOwner(player);

        assertEquals(player, hotel.getOwner());
        assertFalse(hotel.canAffordRatingIncrease());
    }

    /**
     * Test successful increase of hotel star rating
     * Create a Hotel and a Player
     * Set the hotel owner to the player
     * Assert successful hotel rating increase
     * Assert correct hotel rating
     * Assert correct player money after purchasing rating increase
     */
    @Test
    void testCanAffordRatingIncrease() {
        Hotel hotel = new Hotel("A", 1,60);
        Player player = new Player("Player one");
        hotel.setOwner(player);

        assertEquals(player, hotel.getOwner());
        assertTrue(hotel.increaseStarRating());
        assertEquals(1, hotel.getStarRating());
        assertEquals(1970, player.getMoney());
    }

    /**
     * Test if a player with not enough money can increase the star rating
     * Create a Hotel and a Player
     * Set player money to 20
     * Set player as hotel owner
     * Assert player does not own hotel
     * Assert hotel cannot afford rating increase with no owner
     */
    @Test
    void testCanAffordRatingIncreaseFailure() {
        Hotel hotel = new Hotel("A", 1,60);
        Player player = new Player("Player one");
        player.setMoney(20);
        hotel.setOwner(player);

        assertFalse(hotel.canAffordRatingIncrease());
    }

    /**
     * Test that a hotel without an owner cannot increase the rating.
     * Create a hotel
     * Assert a increaseStarRating attempt throws AssertException
     */
    @Test
    void testCanAffordRatingIncreaseAssertOwner() {
        Hotel hotel = new Hotel("A", 1 ,60);
        assertThrows(AssertionError.class, hotel::canAffordRatingIncrease);
    }

    /**
     * Test that a hotel rating cannot be increased above 5.
     * Create a Hotel and a Player
     * Set player as hotel owner
     * Increase rating to 5/5 stars
     * Assert a 6th rating increase causes an AssertException
     */
    @Test
    void testIncreaseStarRatingAssertMaxRating() {
        Hotel hotel = new Hotel("A", 1,60);
        Player player = new Player("Player one");
        hotel.setOwner(player);
        hotel.increaseStarRating();
        hotel.increaseStarRating();
        hotel.increaseStarRating();
        hotel.increaseStarRating();
        hotel.increaseStarRating();

        assertThrows(AssertionError.class, hotel::increaseStarRating);
    }

    /**
     * Test that a hotel without an owner cannot increase the rating.
     * Create a hotel
     * Assert a increaseStarRating attempt throws AssertException
     */
    @Test
    void testIncreaseStarRatingAssertOwner() {
        Hotel hotel = new Hotel("A", 1 ,60);
        assertThrows(AssertionError.class, hotel::increaseStarRating);
    }
}
