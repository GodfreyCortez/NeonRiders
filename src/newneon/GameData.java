package newneon;
import java.awt.Color;
import java.util.Arrays;
public class GameData
{
    //This class holds the data for each specific instance of a game
    private int numOfPlayers;
    private int numAIPlayers;
    private boolean [][] collisionMap;
    private Color [][] map;
    private Player[] playerData;

    //Created every time the player starts a round
    GameData(int numOfPlayers, boolean[][] collisionMap, Color[][] map, Player[] playerData)
    {
        this.numOfPlayers = numOfPlayers;
        this.collisionMap = collisionMap;
        this.map = map;
        this.playerData = playerData;
        numAIPlayers = 4 - numOfPlayers; // Only 4 players are supported for this game
    }

    public int getNumAIPlayers() { return numAIPlayers; }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public boolean[][] getCollisionMap() {
        return collisionMap;
    }

    public void setCollisionMap(boolean[][] collisionMap) {
        this.collisionMap = collisionMap;
    }

    public Color[][] getMap(){
        return map;
    }

    public void setMap(Color[][] map) {
        this.map = map;
    }

    public Player[] getPlayerData() {
        return playerData;
    }
    
    public void setPlayerData(Player[] playerData) {
        this.playerData = playerData;
    }

    public void resetMaps() {
        for(Color[] color : map) Arrays.fill(color, null);
        for(boolean[] collisions : collisionMap) Arrays.fill(collisions, false);
    }
}
