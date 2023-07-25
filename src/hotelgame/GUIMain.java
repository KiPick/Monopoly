package hotelgame;

import hotelgame.view.GameView;

public class GUIMain {
    public static void main(String[] args) {
        boolean cheatMode = args.length > 0 && Boolean.parseBoolean(args[0]);
        GameView gameView = new GameView(cheatMode);
        gameView.start();
    }
}
