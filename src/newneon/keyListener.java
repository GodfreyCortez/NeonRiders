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
    public keyListener(Game_Loop gameLoop) {
        if(keyMapping == null) {
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
        }
        this.gameLoop = gameLoop;
    }


    @Override
    public void keyPressed (KeyEvent e) //when the user holds down the key
    {
        Player[] playerList = gameLoop.getGameData().getPlayerData();
        switch (gameLoop.getNumOfPlayers()) //depending on the number of players we want to look at separate cases so that other people cannot take control over AI
        {
            case 1: //if there is only one player
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (playerList[0].getCurrentDirection() != Directions.RIGHT) {
                        playerList[0].setCurrentDirection(Directions.LEFT);//the player direction is going left (0)
                    }
                }
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if (playerList[0].getCurrentDirection() != Directions.LEFT) {
                        playerList[0].setCurrentDirection(Directions.RIGHT); //the direction of the player is going right(2)
                    }
                }
                else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (playerList[0].getCurrentDirection() != Directions.DOWN) {
                        playerList[0].setCurrentDirection(Directions.UP); //the direction of the player is going up (1)
                    }
                }
                else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (playerList[0].getCurrentDirection() != Directions.UP) {
                        playerList[0].setCurrentDirection(Directions.DOWN); //The direction of the character is going down (3)
                    }
                }
                break;
            case 2: //if there are 2 players
                //PLAYER 1
                if (e.getKeyCode() == KeyEvent.VK_LEFT)//left arrow   
                    {
                        if (Game_Loop.p_direction[0] != 2) 
                            {
                                Game_Loop.p_direction[0] = 0;//the player direction is going left (0)
                            }
                    }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)//right arrow  
                    {
                        if (Game_Loop.p_direction[0] != 0) 
                            {
                                Game_Loop.p_direction[0] = 2; //the direction of the player is going right(2)
                            }
                    }
                if (e.getKeyCode() == KeyEvent.VK_UP)//up arrow  
                    {
                        if (Game_Loop.p_direction[0] != 3)
                            {
                                Game_Loop.p_direction[0] = 1; //the direction of the player is going up (1)
                            }
                    }
                else if (e.getKeyCode() == KeyEvent.VK_DOWN)
                    {
                        if (Game_Loop.p_direction[0] != 1)
                            {
                                Game_Loop.p_direction[0] = 3; //The direction of the character is going down (3)
                            }
                    }
                
                //PLAYER 2
                if (e.getKeyCode() == KeyEvent.VK_A)//left arrow   
                    {
                        if (Game_Loop.p_direction[1] != 2) 
                            {
                                Game_Loop.p_direction[1] = 0;//the player direction is going left (0)
                            }
                    }
                if (e.getKeyCode() == KeyEvent.VK_D)//right arrow  
                    {
                        if (Game_Loop.p_direction[1] != 0) 
                            {
                                Game_Loop.p_direction[1] = 2; //the direction of the player is going right(2)
                            }
                    }
                if (e.getKeyCode() == KeyEvent.VK_W)//up arrow  
                    {
                        if (Game_Loop.p_direction[1] != 3)
                            {
                                Game_Loop.p_direction[1] = 1; //the direction of the player is going up (1)
                            }
                    }
                else if (e.getKeyCode() == KeyEvent.VK_S)
                    {
                        if (Game_Loop.p_direction[1] != 1)
                            {
                                Game_Loop.p_direction[1] = 3; //The direction of the character is going down (3)
                            }
                    }
                break;
            case 3: //if there are 3 players
                //PLAYER 1
                if (e.getKeyCode() == KeyEvent.VK_LEFT)//left arrow   
                    {
                        if (Game_Loop.p_direction[0] != 2) 
                            {
                                Game_Loop.p_direction[0] = 0;//the player direction is going left (0)
                            }
                    }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)//right arrow  
                    {
                        if (Game_Loop.p_direction[0] != 0) 
                            {
                                Game_Loop.p_direction[0] = 2; //the direction of the player is going right(2)
                            }
                    }
                if (e.getKeyCode() == KeyEvent.VK_UP)//up arrow  
                    {
                        if (Game_Loop.p_direction[0] != 3)
                            {
                                Game_Loop.p_direction[0] = 1; //the direction of the player is going up (1)
                            }
                    }
                else if (e.getKeyCode() == KeyEvent.VK_DOWN)
                    {
                        if (Game_Loop.p_direction[0] != 1)
                            {
                                Game_Loop.p_direction[0] = 3; //The direction of the character is going down (3)
                            }
                    }
                
                //PLAYER 2
                if (e.getKeyCode() == KeyEvent.VK_A)//left arrow   
                    {
                        if (Game_Loop.p_direction[1] != 2) 
                            {
                                Game_Loop.p_direction[1] = 0;//the player direction is going left (0)
                            }
                    }
                if (e.getKeyCode() == KeyEvent.VK_D)//right arrow  
                    {
                        if (Game_Loop.p_direction[1] != 0) 
                            {
                                Game_Loop.p_direction[1] = 2; //the direction of the player is going right(2)
                            }
                    }
                if (e.getKeyCode() == KeyEvent.VK_W)//up arrow  
                    {
                        if (Game_Loop.p_direction[1] != 3)
                            {
                                Game_Loop.p_direction[1] = 1; //the direction of the player is going up (1)
                            }
                    }
                else if (e.getKeyCode() == KeyEvent.VK_S)
                    {
                        if (Game_Loop.p_direction[1] != 1)
                            {
                                Game_Loop.p_direction[1] = 3; //The direction of the character is going down (3)
                            }
                    }
                //PLAYER 3
                if (e.getKeyCode() == KeyEvent.VK_J)//left arrow   
                    {
                        if (Game_Loop.p_direction[2] != 2) 
                            {
                                Game_Loop.p_direction[2] = 0;//the player direction is going left (0)
                            }
                    }
                if (e.getKeyCode() == KeyEvent.VK_L)//right arrow  
                    {
                        if (Game_Loop.p_direction[2] != 0) 
                            {
                                Game_Loop.p_direction[2] = 2; //the direction of the player is going right(2)
                            }
                    }
                if (e.getKeyCode() == KeyEvent.VK_I)//up arrow  
                    {
                        if (Game_Loop.p_direction[2] != 3)
                            {
                                Game_Loop.p_direction[2] = 1; //the direction of the player is going up (1)
                            }
                    }
                else if (e.getKeyCode() == KeyEvent.VK_K)
                    {
                        if (Game_Loop.p_direction[2] != 1)
                            {
                                Game_Loop.p_direction[2] = 3; //The direction of the character is going down (3)
                            }
                    }
                break;
            case 4://when there are 4 players
                //PLAYER 1
                if (e.getKeyCode() == KeyEvent.VK_LEFT)//left arrow   
                    {
                        if (Game_Loop.p_direction[0] != 2) 
                            {
                                Game_Loop.p_direction[0] = 0;//the player direction is going left (0)
                            }
                    }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)//right arrow  
                    {
                        if (Game_Loop.p_direction[0] != 0) 
                            {
                                Game_Loop.p_direction[0] = 2; //the direction of the player is going right(2)
                            }
                    }
                if (e.getKeyCode() == KeyEvent.VK_UP)//up arrow  
                    {
                        if (Game_Loop.p_direction[0] != 3)
                            {
                                Game_Loop.p_direction[0] = 1; //the direction of the player is going up (1)
                            }
                    }
                else if (e.getKeyCode() == KeyEvent.VK_DOWN)
                    {
                        if (Game_Loop.p_direction[0] != 1)
                            {
                                Game_Loop.p_direction[0] = 3; //The direction of the character is going down (3)
                            }
                    }
                
                //PLAYER 2
                if (e.getKeyCode() == KeyEvent.VK_A)//left arrow   
                    {
                        if (Game_Loop.p_direction[1] != 2) 
                            {
                                Game_Loop.p_direction[1] = 0;//the player direction is going left (0)
                            }
                    }
                if (e.getKeyCode() == KeyEvent.VK_D)//right arrow  
                    {
                        if (Game_Loop.p_direction[1] != 0) 
                            {
                                Game_Loop.p_direction[1] = 2; //the direction of the player is going right(2)
                            }
                    }
                if (e.getKeyCode() == KeyEvent.VK_W)//up arrow  
                    {
                        if (Game_Loop.p_direction[1] != 3)
                            {
                                Game_Loop.p_direction[1] = 1; //the direction of the player is going up (1)
                            }
                    }
                else if (e.getKeyCode() == KeyEvent.VK_S)
                    {
                        if (Game_Loop.p_direction[1] != 1)
                            {
                                Game_Loop.p_direction[1] = 3; //The direction of the character is going down (3)
                            }
                    }
                //PLAYER 3
                if (e.getKeyCode() == KeyEvent.VK_J)//left arrow   
                    {
                        if (Game_Loop.p_direction[2] != 2) 
                            {
                                Game_Loop.p_direction[2] = 0;//the player direction is going left (0)
                            }
                    }
                if (e.getKeyCode() == KeyEvent.VK_L)//right arrow  
                    {
                        if (Game_Loop.p_direction[2] != 0) 
                            {
                                Game_Loop.p_direction[2] = 2; //the direction of the player is going right(2)
                            }
                    }
                if (e.getKeyCode() == KeyEvent.VK_I)//up arrow  
                    {
                        if (Game_Loop.p_direction[2] != 3)
                            {
                                Game_Loop.p_direction[2] = 1; //the direction of the player is going up (1)
                            }
                    }
                else if (e.getKeyCode() == KeyEvent.VK_K)//down arrow
                    {
                        if (Game_Loop.p_direction[2] != 1) 
                            {
                                Game_Loop.p_direction[2] = 3; //The direction of the character is going down (3)
                            }
                    }
                
                //PLAYER 4
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD4)//left arrow   
                    {
                        if (Game_Loop.p_direction[3] != 2) 
                            {
                                Game_Loop.p_direction[3] = 0;//the player direction is going left (0)
                            }
                    }
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD6)//right arrow  
                    {
                        if (Game_Loop.p_direction[3] != 0) 
                            {
                                Game_Loop.p_direction[3] = 2; //the direction of the player is going right(2)
                            }
                    }
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD8)//up arrow  
                    {
                        if (Game_Loop.p_direction[3] != 3)
                            {
                                Game_Loop.p_direction[3] = 1; //the direction of the player is going up (1)
                            }
                    }
                else if (e.getKeyCode() == KeyEvent.VK_NUMPAD5)//down arroe
                    {
                        if (Game_Loop.p_direction[3] != 1) 
                            {
                                Game_Loop.p_direction[3] = 3; //The direction of the character is going down (3)
                            }
                    }                
                break;
                
        }
    }

}
