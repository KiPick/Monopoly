package hotelgame.model;

/**
 * This class represents a player of the Hotel Game.
 * Invariant: money >= 0
 * Invariant: name never changes
 * Invariant: position >= 0
 */

public class Player {

    /**
     * The amount of money this player has.
     */
    private double money;

    /**
     * This player's display name.
     */
    private final String name;

    /**
     * This player's current position in the game.
     */
    private int position;

    public Player(String name) {
        assert name != null && !name.isEmpty();
        this.name = name;
        this.money = 2000;
        this.position = 0;
    }

    /**
     * Get the player's current amount of money
     * @return The player's money
     */
    public double getMoney() {
        return money;
    }

    /**
     * Get the player's name
     * @return The player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the player's current position
     * @return The player's position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Set the player's money
     * Pre: money >= 0
     * Post: this.money == money
     * @param money The new value of the player's money
     */
    public void setMoney(double money) {
        assert money >= 0;
        this.money = money;
        assert this.money == money;
    }

    /**
     * Deduct money from the player
     * Pre: amount >= 0
     * Post: this.money == oldMoney - amount (or 0 if result is negative)
     * @param amount The amount to deduct from the player's money
     */
    public void deductMoney(double amount) {
        assert amount >= 0 : "Deduction amount should be non-negative.";
        double oldMoney = this.money;
        this.money -= amount;
        this.money = this.money < 0 ? 0 : this.money;
        assert this.money <= oldMoney : "Player's money should decrease or remain the same after deduction.";
    }

    /**
     * Add money to the player
     * Pre: amount >= 0
     * Post: this.money == oldMoney + amount
     * @param amount The amount to add to the player's money
     */
    public void addMoney(double amount) {
        assert amount >= 0;
        double oldMoney = this.money;
        this.money += amount;
        assert this.money == oldMoney + amount;
    }

    /**
     * Set the player's position
     * @param position The new position for the player
     */
    public void setPosition(int position) {
        this.position = position;
    }
}