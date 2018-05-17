//This class will handle all mouse inputs
package newneon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class mouseListener extends MouseAdapter
{
    int mx = 0; //This will track the x coordinate of the mouse button when pressed
    int my = 0; //This will track the y coordinate of our mouse button when pressed
    public static boolean has_paused = false; 
    public static boolean music_paused = false; 
            @Override
            public void mousePressed (MouseEvent e)
                {
                    mx = e.getX(); 
                    my = e.getY(); 
                        if (Game_Loop.state == Game_Loop.STATE.MENU) //depending on the state we are in we want to ensure we that the clicks do not lead to the wrong state
                            {
                                if (mx >= 192 && mx <= 1439) //check if they clicked on the x axis where the buttons are
                                {
                                    if (my >= 278 && my <= 367) //these are the y boundaries of the solo player button
                                        {
                                            Game_Loop.num_of_player = 1; 
                                            Game_Loop.resetgame(); 
                                            Game_Loop.state = Game_Loop.STATE.GAME;
                                        }
                                    else if (my >= 428 && my <= 517) //these are the y boundaries of the multiplayer button
                                        {
                                            Game_Loop.state = Game_Loop.STATE.MULTIPLAYER_SELECT; 
                                        }
                                    else if (my >= 568 && my <= 657) //these are the y boundaries of the Instructions button
                                        {
                                            Game_Loop.state = Game_Loop.STATE.INSTRUCTIONS; 
                                        }
                                    else if (my >= 710 && my <= 800) //these are the y boundaries of the Exit button
                                        {
                                            System.exit(0); 
                                        }
                                }
                            }
                        else if (Game_Loop.state == Game_Loop.STATE.CONTINUE) 
                            {
                                if (mx >= 192 && mx <= 1439) 
                                {
                                    if (my >= 216 && my <= 305) //these are the y boundaries of the play again button
                                        {
                                            Game_Loop.resetgame(); 
                                            Game_Loop.state = Game_Loop.STATE.GAME; 
                                        }                         
                                    else if (my >= 762 && my <= 851) //these are the y boundaries of the main menu button
                                        {
                                                for (int x = 0; x < Game_Loop.p_score.length; x++) //loop to reset the score of each character
                                                    {
                                                        Game_Loop.p_score[x] = 0; //when we go back to main menu we must remove previous scores
                                                    }
                                            Game_Loop.state = Game_Loop.STATE.MENU;
                                        }
                                }
                            }
                        else if (Game_Loop.state == Game_Loop.STATE.MULTIPLAYER_SELECT) 
                            {
                                if (mx >= 192 && mx <= 1439) //x boundaries of all the buttons
                                    {
                                        if (my >= 278 && my <= 367) //these are the y boundaries of the corresponding player button
                                            {
                                                Game_Loop.num_of_player = 2; 
                                                Game_Loop.resetgame(); 
                                                Game_Loop.state = Game_Loop.STATE.GAME; 
                                            }
                                        else if (my >= 428 && my <= 517) 
                                            {
                                                Game_Loop.num_of_player = 3; 
                                                Game_Loop.resetgame(); 
                                                Game_Loop.state = Game_Loop.STATE.GAME; 
                                            }
                                        else if (my >= 568 && my <= 657) 
                                            {
                                                Game_Loop.num_of_player = 4; 
                                                Game_Loop.resetgame();
                                                Game_Loop.state = Game_Loop.STATE.GAME; 
                                            }
                                        else if (my >= 710 && my <= 800) //this is the y boundaries of the back button
                                            {
                                                Game_Loop.state = Game_Loop.STATE.MENU; 
                                            }
                                    }
                            }
                        else if (Game_Loop.state == Game_Loop.STATE.INSTRUCTIONS) //Will display the instructions and goal of the game
                            {
                                if (mx >= 192 && mx <= 1439) 
                                    {
                                        if (my>=220 && my<=275) 
                                            {
                                                Game_Loop.state = Game_Loop.STATE.MENU; 
                                            }
                                    }
                            }
                        else if (Game_Loop.state == Game_Loop.STATE.GAME) //if we are in the game statewe will implement a pause 
                            {
                                if (mx >= 15 && mx <= 55) //x coordinates of our pause button
                                    {
                                        if (my >= 15 && my <= 55) //y coordinates of the button
                                            {
                                                if (!has_paused) //control the pause state of the game
                                                    {
                                                        try
                                                            {
                                                                has_paused = true; 
                                                                Thread.sleep(25); 
                                                                Game_Loop.paused = true; 
                                                            }
                                                        catch (Exception ex){} 
                                                    }
                                                else 
                                                    {
                                                        try
                                                            {
                                                                has_paused = false; 
                                                                Thread.sleep(25);
                                                                Game_Loop.paused = false; 
                                                            }
                                                        catch (Exception ex){} 
                                                    }
                                            }
                                            
                                    }
                            }
                }

        @Override
        public void mouseReleased (MouseEvent e) //reset the x and y coordinates of the mouse
            {
                mx = 0; 
                my = 0;
            }
}
