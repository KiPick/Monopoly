package hotelgame.model;

/**
 * A Hotel on the board of the game.
 */
public class Hotel {

    /**
     * The hotel group (A,B,C, etc..).
     */
    private final String group;

    /**
     * The hotel number (1,2,3,etc..)
     */
    private final int number;

    /**
     * The rating the hotel has.
     */
    private int starRating;

    /**
     * The price of the hotel.
     */
    private final double price;

    /**
     * The player that owns the hotel.
     */
    private Player owner;

    public Hotel(String group, int number, int price) {
        this.group = group;
        this.number = number;
        this.price = price;
        this.starRating = 0;
    }

    /**
     * Get the Hotel name
     * @return name
     */
    public String getName() {
        return this.group + this.number;
    }

    /**
     * Get the Hotel group
     * @return group
     */
    public String getGroup() {
        return group;
    }

    /**
     * Get the hotel star rating
     * @return starRating
     */
    public int getStarRating() {
        return starRating;
    }

    /**
     * Get the hotel owner
     * @return owner
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Set the owner of this hotel.
     * Pre: owner != null
     * Post: this.owner == owner
     * @param owner The new owner
     */
    public void setOwner(Player owner) {
        assert owner != null;
        this.owner = owner;
        assert this.owner == owner;
    }

    /**
     * Get the hotel price
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Get the hotel overnight fee.
     * @return 10% of price * starRating^2
     */
    public double getOvernightFee() {
        return this.starRating == 0 ? 0.0 : (0.10 * this.price) * Math.pow(this.starRating, 2);
    }

    /**
     * Get the increase rating fee
     * @return 50% of price
     */
    public double getIncreaseRatingFee() {
        return this.price * 0.50;
    }

    /**
     * Can a player afford this hotel?
     * Pre: owner == null, player != null
     * Post: Returns true if player's money >= hotel price, else returns false
     * @param player The player attempting to purchase
     * @return True if the price of the hotel is <= the player's current money.
     */
    public boolean canAffordPurchase(Player player) {
        assert this.owner == null && player != null;
        return this.getPrice() <= player.getMoney();
    }

    /**
     * Can the current owner afford to increase the rating?
     * Pre: owner != null
     * Post: Returns true if owner's money >= increaseRatingFee, else returns false
     * @return True if the owner has enough money to cover the increase rating cost.
     */
    public boolean canAffordRatingIncrease() {
        assert this.owner != null;
        double increaseRatingCost = this.getIncreaseRatingFee();
        return owner.getMoney() >= increaseRatingCost;
    }

    /**
     * Attempt to increase the star rating.
     * Pre: owner != null, starRating < 5
     * Post: if owner's money > increaseRatingCost, starRating is increased by 1 and returns true; else returns false
     * @return True if the rating was successfully increased.
     */
    public boolean increaseStarRating() {
        assert this.owner != null && this.starRating < 5;
        double increaseRatingCost = this.getIncreaseRatingFee();
        if (this.owner.getMoney() > increaseRatingCost) {
            this.owner.deductMoney(increaseRatingCost);
            this.starRating++;
            assert this.starRating <= 5;
            return true;
        } else {
            return false;
        }
    }
}