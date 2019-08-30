package newneon;
import newneon.constants.State;

public class Updater {

    private GameData game = null;

    Updater(GameData gameInstance) {
        game = gameInstance;
    }

    private void AI() {

    }

    private void checkWin() {
        Player[] playerData = game.getPlayerData();
        int numOfPlayers = game.getNumOfPlayers();


    }

    public void updateGame(State gameState) {
        if(gameState == State.GAME){

        }
    }
}
