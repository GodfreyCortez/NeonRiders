//This class will handle all inputs from the keyboard
package newneon;
import java.awt.event.KeyAdapter;
import java.security.Key;
import java.util.HashMap;
import java.awt.event.KeyEvent;
import newneon.constants.Directions;
public class keyListener extends KeyAdapter
{
    
    private Game_Loop gameLoop;
    private static HashMap<Integer, Directions> keyMapping;
    private static HashMap<Integer, Integer> playerKeyMapping;
    public keyListener(Game_Loop gameLoop) {
        if(keyMapping == null) {
            keyMapping = new HashMap<>();
            playerKeyMapping = new HashMap<>();
            keyMapping.put(KeyEvent.VK_LEFT, Directions.LEFT);
            keyMapping.put(KeyEvent.VK_A, Directions.LEFT);
            keyMapping.put(KeyEvent.VK_3, Directions.LEFT);
            keyMapping.put(KeyEvent.VK_NUMPAD4, Directions.LEFT);

            keyMapping.put(KeyEvent.VK_RIGHT, Directions.RIGHT);
            keyMapping.put(KeyEvent.VK_D, Directions.RIGHT);
            keyMapping.put(KeyEvent.VK_L, Directions.RIGHT);
            keyMapping.put(KeyEvent.VK_NUMPAD6, Directions.RIGHT);

            keyMapping.put(KeyEvent.VK_UP, Directions.UP);
            keyMapping.put(KeyEvent.VK_W, Directions.UP);
            keyMapping.put(KeyEvent.VK_I, Directions.UP);
            keyMapping.put(KeyEvent.VK_NUMPAD8, Directions.UP);

            keyMapping.put(KeyEvent.VK_DOWN, Directions.DOWN);
            keyMapping.put(KeyEvent.VK_S, Directions.DOWN);
            keyMapping.put(KeyEvent.VK_K, Directions.DOWN);
            keyMapping.put(KeyEvent.VK_NUMPAD5, Directions.DOWN);

            playerKeyMapping.put(KeyEvent.VK_LEFT, 4);
            playerKeyMapping.put(KeyEvent.VK_A, 3);
            playerKeyMapping.put(KeyEvent.VK_3, 2);
            playerKeyMapping.put(KeyEvent.VK_NUMPAD4, 1);

            playerKeyMapping.put(KeyEvent.VK_RIGHT, 4);
            playerKeyMapping.put(KeyEvent.VK_D, 3);
            playerKeyMapping.put(KeyEvent.VK_L, 2);
            playerKeyMapping.put(KeyEvent.VK_NUMPAD6, 1);

            playerKeyMapping.put(KeyEvent.VK_UP, 4);
            playerKeyMapping.put(KeyEvent.VK_W, 3);
            playerKeyMapping.put(KeyEvent.VK_I, 2);
            playerKeyMapping.put(KeyEvent.VK_NUMPAD8, 1);

            playerKeyMapping.put(KeyEvent.VK_DOWN, 4);
            playerKeyMapping.put(KeyEvent.VK_S, 3);
            playerKeyMapping.put(KeyEvent.VK_K, 2);
            playerKeyMapping.put(KeyEvent.VK_NUMPAD5, 1);
        }
        this.gameLoop = gameLoop;
    }

    @Override
    public void keyPressed (KeyEvent e) //when the user holds down the key
    {
        Player[] playerList = gameLoop.getGameData().getPlayerData();
        int numOfPlayers = gameLoop.getNumOfPlayers();
        Directions directionPressed = keyMapping.get(e.getKeyCode());
        // Depending on the number of players we want to look at separate cases so that other people cannot take control over AI
        switch (numOfPlayers)
        {
            case 4:
                if(isKeyForPlayer(e, 4)) playerList[3].changeDirection(directionPressed);
            case 3:
                if(isKeyForPlayer(e, 3)) playerList[2].changeDirection(directionPressed);
            case 2:
                if(isKeyForPlayer(e, 2)) playerList[1].changeDirection(directionPressed);
            case 1:
                if(isKeyForPlayer(e, 1)) playerList[0].changeDirection(directionPressed);
        }
    }

    // returns if the key that was pressed is for that specific player
    private boolean isKeyForPlayer(KeyEvent e, int player) {
        return playerKeyMapping.get(e.getKeyCode()) == player;
    }
}
