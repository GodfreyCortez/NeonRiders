package newneon;
import newneon.Game_Loop.STATE;

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

    public void updateGame(STATE gameState) {
        if(gameState == STATE.GAME){

        }
    }
}
