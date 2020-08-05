package newneon;
import newneon.constants.Directions;

import java.awt.event.KeyEvent;
import java.util.HashMap;

public class PlayerKeyMap {
    public static HashMap<Integer, Integer> playerKeyMap;
    static {
        playerKeyMap = new HashMap<>();
        playerKeyMap.put(KeyEvent.VK_LEFT, 0);
        playerKeyMap.put(KeyEvent.VK_A, 1);
        playerKeyMap.put(KeyEvent.VK_3, 2);
        playerKeyMap.put(KeyEvent.VK_NUMPAD4, 3);

        playerKeyMap.put(KeyEvent.VK_RIGHT, 0);
        playerKeyMap.put(KeyEvent.VK_D, 1);
        playerKeyMap.put(KeyEvent.VK_L, 2);
        playerKeyMap.put(KeyEvent.VK_NUMPAD6, 3);

        playerKeyMap.put(KeyEvent.VK_UP, 0);
        playerKeyMap.put(KeyEvent.VK_W, 1);
        playerKeyMap.put(KeyEvent.VK_I, 2);
        playerKeyMap.put(KeyEvent.VK_NUMPAD8, 3);

        playerKeyMap.put(KeyEvent.VK_DOWN, 0);
        playerKeyMap.put(KeyEvent.VK_S, 1);
        playerKeyMap.put(KeyEvent.VK_K, 2);
        playerKeyMap.put(KeyEvent.VK_NUMPAD5, 3);
    }

    public static Integer getKeyMapping(KeyEvent e) {
        return playerKeyMap.get(e.getKeyCode());
    }
}
