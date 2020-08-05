package newneon;

import newneon.constants.Directions;

import java.awt.event.KeyEvent;
import java.util.HashMap;

public class DirectionMapper {
    private static HashMap<Integer, Directions> keyMapping;
    private static HashMap<Directions, Directions> opposingDirections;
    static {
        keyMapping = new HashMap<>();
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

        opposingDirections = new HashMap<>();
        opposingDirections.put(Directions.DOWN, Directions.UP);
        opposingDirections.put(Directions.RIGHT, Directions.LEFT);
        opposingDirections.put(Directions.UP, Directions.DOWN);
        opposingDirections.put(Directions.LEFT, Directions.RIGHT);
    }

    // If the player inputted a direction and that direction is not the opposite direction
    // return the inputted direction, otherwise return their current direction
    public static Directions switchDirection(KeyEvent e, Player player) {
        Directions inputDirection = keyMapping.get(e.getKeyCode());
        Directions currentDirection = player.getCurrentDirection();

        return opposingDirections.get(inputDirection).equals(currentDirection) ? currentDirection : inputDirection;
    }
}
