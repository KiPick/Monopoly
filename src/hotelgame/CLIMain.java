package hotelgame;

import hotelgame.model.Hotel;
import hotelgame.model.GameModel;

import java.util.Scanner;

public class CLIMain {

    /**
     * Whether cheat mode is on or not
     */
    private final boolean cheatMode;

    /**
     * The game model
     */
    private final GameModel model;

    /**
     * A Scanner to get input from the user
     */
    private final Scanner reader;

    /**
     * Instantiate the model and reader
     */
    public CLIMain(boolean cheatMode) {
        this.cheatMode = cheatMode;
        this.model = new GameModel();
        this.reader = new Scanner(System.in);
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Game!");

        boolean cheatMode = args.length > 0 && Boolean.parseBoolean(args[0]);

        CLIMain cli = new CLIMain(cheatMode);
        cli.promptNewGame();
    }

    /**
     * Prompt the user to start a new game.
     */
    private void promptNewGame() {
        System.out.print("Press s to start a new game or q to quit: ");
        String answer = reader.nextLine();

        if (answer.equalsIgnoreCase("s")) {
            startNewGame();
        } else if (answer.equalsIgnoreCase("q"))  {
            quitGame();
        } else {
            System.out.println("Invalid input, please try again");
            promptNewGame();
        }
    }

    /**
     * Quit the current game.
     */
    private void quitGame() {
        System.out.println("Thanks for playing the Hotel Game!");
        reader.close();
    }

    /**
     * Start a new game.
     */
    private void startNewGame() {
        model.reset();

        String playerOneName = "";
        String playerTwoName = "";

        while(playerOneName.equalsIgnoreCase(playerTwoName)) {
            playerOneName = getPlayerName("one");
            playerTwoName = getPlayerName("two");

            if (playerOneName.equalsIgnoreCase(playerTwoName)) {
                System.out.println("The player names cannot be the same, try again.");
            }
        }

        model.createPlayers(playerOneName, playerTwoName);
        System.out.println("Player " + model.getCurrentTurn().getName() + " will go first.");
        System.out.println("At the start of the game:");
        System.out.println(model.getPlayerOne().getName() + " has " + model.getPlayerOne().getMoney() + " money.");
        System.out.println(model.getPlayerTwo().getName() + " has " + model.getPlayerTwo().getMoney() + " money.");
        System.out.println();
        printBoard();
        playGame();
    }

    /**
     * Get a player name input from the user.
     * @param number which player is getting this name
     * @return The user inputted player name.
     */
    private String getPlayerName(String number) {
        String playerName = "";
        while (playerName.equals("")) {
            System.out.print("Press enter player " + number + "'s name: ");
            playerName = reader.nextLine();
            if (playerName.equals("")) {
                System.out.println("Invalid player name, please try again.");
            }
        }
        return playerName;
    }

    /**
     * Run the game loop (play the game).
     */
    private void playGame() {
        while(!model.isGameOver()) {

            int diceRoll;
            if (cheatMode) {
                diceRoll = getCheatModeRoll();
            } else {
                System.out.print(model.getCurrentTurn().getName() + " press any key to roll the dice for your turn: ");
                reader.next();
                diceRoll = model.rollDice();
            }

            model.movePlayer(diceRoll);
            System.out.println("You moved " + diceRoll + " spaces.");

            if (model.getCurrentPlayerPositionHotel() != null) {
                showHotelOptions();
            } else {
                // Tile numbers are 0-based, so we subtract 1 when printing the tile number
                System.out.println("You landed on a blank tile: " + (model.getCurrentTurn().getPosition() + 1));
            }

            System.out.println("Your turn has ended with " + model.getCurrentTurn().getMoney() + ".");
            System.out.println();

            printBoard();

            model.nextTurn();
        }
        System.out.println(model.getWinner().getName() + " wins the game!");
    }


    /**
     * Retrieve the cheat mode input for moving a player.
     * @return An input from the player that is constrained to 1-12.
     */
    private int getCheatModeRoll() {
        int diceRoll = 0;
        while(diceRoll == 0) {
            System.out.print(model.getCurrentTurn().getName() + " enter the amount of spaces to move(1-12): ");
            try {
                diceRoll = Integer.parseInt(reader.next());
                diceRoll = Math.min(Math.max(diceRoll, 1), 12);
            } catch (Exception e) {
                System.out.println("Invalid input entered. Please enter a number of spaces to move from 1-12.");
            }
        }
        return diceRoll;
    }

    /**
     * Shop options for a hotel.
     */
    private void showHotelOptions() {
        Hotel hotel = model.getCurrentPlayerPositionHotel();

        if (hotel.getOwner() == model.getCurrentTurn()) {
            System.out.println("You landed at your " + hotel.getName() + " hotel.");
            showIncreaseRatingOptions();
        } else if (hotel.getOwner() == model.getOpposingTurn()) {
            System.out.println("You landed at " + model.getOpposingTurn().getName() + "'s " + hotel.getName() + " hotel.");
            if (hotel.getStarRating() == 0) {
                System.out.println("You do not have to pay an overnight fee because the hotel rating is 0.");
            } else {
                double amountPaid = model.payOvernightFee();
                System.out.println("You paid £" + amountPaid + " for staying at this hotel.");
            }
            System.out.println();
        } else {
            System.out.println("You landed at the un-purchased " + hotel.getName() + " hotel.");
            showPurchaseOptions();
        }
    }

    /**
     * Show options for purchasing a hotel.
     */
    public void showPurchaseOptions() {
        Hotel hotel = model.getCurrentPlayerPositionHotel();
        System.out.println("Would you like to buy this hotel for " + hotel.getPrice() + " ?");
        System.out.print("Press y to buy or any other key to skip: ");
        String answer = reader.next();
        if (answer.equalsIgnoreCase("y")) {
            if (hotel.canAffordPurchase(model.getCurrentTurn())) {
                boolean purchaseResult = model.buyHotel();
                if (purchaseResult) {
                    System.out.println("You successfully purchased the " + hotel.getName() + " hotel for £" + hotel.getPrice() + ".");
                    System.out.println();
                    showIncreaseRatingOptions();
                }
            } else {
                System.out.println("You cannot afford this hotel.");
                System.out.println();
            }
        }
    }

    /**
     * Show options for increasing the rating of a hotel.
     */
    private void showIncreaseRatingOptions() {
        Hotel hotel = model.getCurrentPlayerPositionHotel();
        System.out.println("The current rating of your hotel is " + hotel.getStarRating() + "/5 stars.");

        if (!hotel.canAffordRatingIncrease()) {
            System.out.println("You cannot afford to increase this hotel's star rating.");
            System.out.println();
        } else if (hotel.getStarRating() < 5) {
            int desiredRating = -1;  // Set initial value to -1 to allow entering the loop
            while(desiredRating < 0 || desiredRating > 5) {
                System.out.print("Enter the desired rating (1-5) or 0 to cancel: ");
                try {
                    desiredRating = Integer.parseInt(reader.next());
                    desiredRating = Math.min(Math.max(desiredRating, 0), 5);  // Allow 0 as valid input
                    if(desiredRating == 0) {
                        System.out.println("Upgrade cancelled.");
                        return;  // Exit the method if the user chooses to cancel
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input entered. Please enter a desired rating from 1-5 or 0 to cancel.");
                }
            }

            int difference = desiredRating - hotel.getStarRating();
            double costForIncrease = hotel.getIncreaseRatingFee() * difference;

            if(model.getCurrentTurn().getMoney() >= costForIncrease) {
                for (int i = 0; i < difference; i++) {
                    if (hotel.canAffordRatingIncrease()) {
                        model.increaseStarRating();
                    } else {
                        System.out.println("You cannot afford to further increase the hotel's star rating.");
                        break;
                    }
                }
                System.out.println("You increased the hotel rating to " + hotel.getStarRating() + "/5 stars. You now have £" + model.getCurrentTurn().getMoney());
                System.out.println();
            } else {
                System.out.println("You cannot afford to increase the hotel's star rating to " + desiredRating);
                System.out.println();
            }
        }
    }

    /**
     * Print the entire state of the game board to the cli.
     */
    private void printBoard() {
        Hotel[] tiles = model.getTiles();
        for(int position = 0; position < tiles.length; position++) { // We start from 0 since the positions are 0-based
            Hotel hotel = tiles[position];
            if (hotel != null) {
                System.out.print("Tile " + (position + 1) + " is hotel " + hotel.getName() + ".");
                System.out.print(" Price: " + hotel.getPrice() + ". ");
                System.out.print("Rating: " + hotel.getStarRating() + "/5 stars. ");
                System.out.print("Owner: " + (hotel.getOwner() != null ? hotel.getOwner().getName() : "None."));
            } else if (position > 0) {
                System.out.print("Tile " + (position + 1) + " is a blank tile.");
            } else {
                System.out.print("Tile 1 is the GO tile.");
            }
            if (model.getPlayerOne().getPosition() == position) {
                System.out.print(" " + model.getPlayerOne().getName() + " is on this tile.");
            }
            if (model.getPlayerTwo().getPosition() == position) {
                System.out.print(" " + model.getPlayerTwo().getName() + " is on this tile.");
            }
            System.out.println();
        }
        System.out.println(model.getPlayerOne().getName() + " has " + model.getPlayerOne().getMoney() + " money left remaining.");
        System.out.println(model.getPlayerTwo().getName() + " has " + model.getPlayerTwo().getMoney() + " money left remaining.");
    }






}