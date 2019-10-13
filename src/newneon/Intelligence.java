package newneon;
import newneon.constants.Directions;

import java.util.*;

//The purpose of this class is to act as the decision makers for the CPU players
//in the game
public class Intelligence
{
    public Intelligence(){}
    private final int minLookAhead = 1;
    private final int maxLookAhead = 6;
    private Random rand = new Random();
    private int randomWithRange(int min, int max)
    {
      int range = Math.abs(max - min) + 1;
      return (int)(Math.random() * range) + ((min > max) ?  max : min);
    }

    private Directions getNewDirection (Directions direction) {
        if(direction == Directions.LEFT || direction == direction.RIGHT) {
            return rand.nextBoolean() ? Directions.DOWN : Directions.UP;
        } else {
            return rand.nextBoolean() ? Directions.LEFT : Directions.RIGHT;
        }
    }

    public Directions chooseDirection(Player player, GameData gameData)
    {
        //First have the AI choose to look one two or three spaces ahead
        final int range = randomWithRange(minLookAhead, maxLookAhead);

        Directions currentDirection = player.getCurrentDirection();
        int row = player.getPosition().y;
        int column = player.getPosition().x;
        int indexLookAhead; //This is the largest index we'll look at

        // For a specific amount of the time we may want to keep the current direction
        // to sometimes crash (for testing we will keep it at 50%)
        boolean keepDirection = randomWithRange(1, 10) % 2 == 0 ? true : false;
        boolean[][] collisionMap = gameData.getCollisionMap();

        if(keepDirection) return currentDirection;

        switch(currentDirection) {
            case UP:
                indexLookAhead = row + range;

                //If travelling up, then we should ensure that the range we are looking ahead
                //at does not go out of bounds.
                //At the same time we can use these conditions to have the AI sometimes
                //crash into the wall
                if(indexLookAhead >= 0 ) {
                    for(int i = row; i < indexLookAhead; i++) {
                        if(collisionMap[column][i])
                            return getNewDirection(currentDirection);
                    }
                }
                break;
            case LEFT:
                indexLookAhead = column - range;

                if(indexLookAhead >= 0) {
                    for(int i = column; i >= indexLookAhead; i--) {
                        if(collisionMap[i][row])
                            return getNewDirection(currentDirection);
                    }
                }
                break;
            case DOWN:
                indexLookAhead = row - range;

                if(indexLookAhead >= 0) {
                    for(int i = row; i >= indexLookAhead; i--)
                        if(collisionMap[column][i])
                            return getNewDirection(currentDirection);
                }
                break;
            case RIGHT:
                indexLookAhead = column + range;

                if(indexLookAhead < collisionMap.length) {
                    for(int i = row; i < indexLookAhead; i++)
                        if(collisionMap[i][row])
                            return getNewDirection(currentDirection);
                }
                break;
            default:
                return currentDirection;
        }

        return currentDirection;
    }
}
