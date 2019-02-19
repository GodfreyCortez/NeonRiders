package newneon;

import static newneon.Game_Loop.*;
import newneon.directions;
import newneon.directions.*;
import newneon.Player;
public class Game_Data
{
    //This class holds the data for each specific instance of a game
    private int numOfPlayers;
    private boolean [][] collisionMap;
    private int [][] map;
    private Player[] playerData;

    //Created every time the player starts a round
    Game_Data(int numOfPlayers, boolean[][] collisionMap, int[][] map, Player[] playerData)
    {
        this.numOfPlayers = numOfPlayers;
        this.collisionMap = collisionMap;
        this.map = map;
        this.playerData = playerData;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public boolean[][] getCollisionMap() {
        return collisionMap;
    }

    public void setCollisionMap(boolean[][] collisionMap) {
        this.collisionMap = collisionMap;
    }

    public int[][] getMap(){
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public Player[] getPlayerData() {
        return playerData;
    }
    
    public void setPlayerData(Player[] playerData) {
        this.playerData = playerData;
    }
}
