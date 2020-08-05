//This class will handle all inputs from the keyboard
package newneon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import newneon.constants.Directions;

public class KeyListener extends KeyAdapter
{
    private GameLoop gameLoop;

    public KeyListener(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }

    public void setGameLoop(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }

    @Override
    public void keyPressed (KeyEvent e) {
        Player[] playerList = gameLoop.getGameData().getPlayerData();
        int playerCount = gameLoop.getNumOfPlayers();
        int playerKeyMapping = PlayerKeyMap.getKeyMapping(e);
        if(playerKeyMapping < playerCount) {
            Directions newDirection = DirectionMapper.switchDirection(e, playerList[playerKeyMapping]);
            playerList[playerKeyMapping].setCurrentDirection(newDirection);
        }
    }

}
