package newneon;
import java.util.*;

import static newneon.Game_Loop.*;


//The purpose of this class is to act as the decision makers for the CPU players
//in the game
public class Intelligence
{
    PlayerDirections playerDirection = null;

    public Intelligence(){}

    public PlayerDirections chooseDirection(PlayerDirections currentDirection)
    {
        return playerDirection;
    }
}
