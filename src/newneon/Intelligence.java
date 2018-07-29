package newneon;
import java.util.*;

import static newneon.Game_Loop.*;
import static newneon.Game_Loop.PlayerDirections.*;


//The purpose of this class is to act as the decision makers for the CPU players
//in the game
public class Intelligence
{
    PlayerDirections playerDirection = LEFT;

    public Intelligence(){}

    public PlayerDirections chooseDirection(PlayerDirections currentDirection)
    {
        if(currentDirection == LEFT)
        {

        }
        return playerDirection;
    }
}
