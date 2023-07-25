package hotelgame.model.tests;

import hotelgame.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {

    /**
     * Test deducting player money
     * Set player money to £100
     * Deduct £5
     * Assert player money equals £95
     */
    @Test
    void testDeductMoney() {
        Player player = new Player("test");
        player.setMoney(100);
        player.deductMoney(5);
        assertEquals(95, player.getMoney());
    }

    /**
     * Test deducting more money than player has
     * Set player money to £100
     * Deduct £500
     * Assert player money is £0
     */
    @Test
    void testDeductMoneyNegative() {
        Player player = new Player("test");
        player.setMoney(100);
        player.deductMoney(500);
        assertEquals(0, player.getMoney());
    }

    /**
     * Test adding player money
     * Set player money to £100
     * Add £5
     * Assert player money equals £105
     */
    @Test
    void testAddMoney() {
        Player player = new Player("test");
        player.setMoney(100);
        player.addMoney(5);
        assertEquals(105, player.getMoney());
    }

}
