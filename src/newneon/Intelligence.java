package newneon;
import newneon.constants.directions;

import java.util.*;

//The purpose of this class is to act as the decision makers for the CPU players
//in the game
public class Intelligence
{
    public Intelligence(){}
    private final int minLookAhead = 1;
    private final int maxLookAhead = 4;
    private Random rand = new Random();
    private int randomWithRange(int min, int max)
    {
      int range = Math.abs(max - min) + 1;
      return (int)(Math.random() * range) + ((min > max) ?  max : min);
    }

    private directions getNewDirection (directions direction) {
        if(direction == directions.LEFT || direction == direction.RIGHT) {
            return rand.nextBoolean() ? directions.DOWN : directions.UP;
        } else {
            return rand.nextBoolean() ? directions.LEFT : directions.RIGHT;
        }
    }

    public directions chooseDirection(Player player, Game_Data gameData)
    {
        //First have the AI choose to look one two or three spaces ahead
        final int range = randomWithRange(minLookAhead, maxLookAhead);

        directions currentDirection = player.getCurrentDirection();
        int row = player.getPosition().y;
        int column = player.getPosition().x;
        int indexLookAhead; //This is the largest index we'll look at

        boolean[][] collisionMap = gameData.getCollisionMap();

        switch(currentDirection) {
            case UP:
                indexLookAhead = row - range;

                //If travelling up, then we should ensure that the range we are looking ahead
                //at does not go out of bounds.
                //At the same time we can use these conditions to have the AI sometimes
                //crash into the wall
                if(indexLookAhead >= 0 ) {
                    for(int i = row; i >= indexLookAhead; i--) {
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
                indexLookAhead = row + range;

                if(indexLookAhead < collisionMap[column].length) {
                    for(int i = row; i < indexLookAhead; i++)
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
