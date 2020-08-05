//This class will handle all mouse inputs
package newneon;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import newneon.constants.State;

public class MouseListener extends MouseAdapter {
    int mx = 0; //This will track the x coordinate of the mouse button when pressed
    int my = 0; //This will track the y coordinate of our mouse button when pressed
    public static boolean has_paused = false;
    private GameLoop gameLoop;
    public MouseListener(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }
    @Override
    public void mousePressed(MouseEvent e) {
        mx = e.getX();
        my = e.getY();

        if (gameLoop.getState() == State.MENU) //depending on the state we are in we want to ensure we that the clicks do not lead to the wrong state
        {
            if (mx >= 192 && mx <= 1439) //check if they clicked on the x axis where the buttons are
            {
                if (my >= 278 && my <= 367) //these are the y boundaries of the solo player button
                {
                    gameLoop.setNumOfPlayers(1);
                    gameLoop.setGameData(null);
                    gameLoop.setState(State.GAME);
                } else if (my >= 428 && my <= 517) {//these are the y boundaries of the multiplayer button
                    gameLoop.setState(State.MULTIPLAYER_SELECTION);
                } else if (my >= 568 && my <= 657) {//these are the y boundaries of the Instructions button
                    gameLoop.setState(State.INSTRUCTIONS);
                } else if (my >= 710 && my <= 800) {//these are the y boundaries of the Exit button
                    System.exit(0);
                }
            }
        } else if (gameLoop.getState() == State.CONTINUE) {
            if (mx >= 192 && mx <= 1439) {
                if (my >= 216 && my <= 305) //these are the y boundaries of the play again button
                {
                    gameLoop.getGameData().resetMaps();
                    gameLoop.setState(State.GAME);
                } else if (my >= 762 && my <= 851) {
                    gameLoop.setGameData(null);
                    gameLoop.setState(State.MENU);
                }
            }
        } else if (gameLoop.getState() == State.MULTIPLAYER_SELECTION) {
            if (mx >= 192 && mx <= 1439) //x boundaries of all the buttons
            {
                if (my >= 278 && my <= 367) //these are the y boundaries of the corresponding player button
                {
                    gameLoop.setNumOfPlayers(2);
                    gameLoop.getGameData().resetMaps();
                    gameLoop.setState(State.GAME);
                } else if (my >= 428 && my <= 517) {
                    gameLoop.setNumOfPlayers(3);
                    gameLoop.getGameData().resetMaps();
                    gameLoop.setState(State.GAME);
                } else if (my >= 568 && my <= 657) {
                    gameLoop.setNumOfPlayers(4);
                    gameLoop.getGameData().resetMaps();
                    gameLoop.setState(State.GAME);
                } else if (my >= 710 && my <= 800) {//this is the y boundaries of the back button
                    gameLoop.setState(State.MENU);
                }
            }
        } else if (gameLoop.getState() == State.INSTRUCTIONS) //Will display the instructions and goal of the game
        {
            if (mx >= 192 && mx <= 1439) {
                if (my >= 220 && my <= 275) {
                    gameLoop.setState(State.MENU);
                }
            }
        } else if (gameLoop.getState() == State.GAME) //if we are in the game state we will implement a pause
        {
            if (mx >= 15 && mx <= 55) {//x coordinates of our pause button
                if (my >= 15 && my <= 55) {//y coordinates of the button
                    gameLoop.togglePaused();
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) //reset the x and y coordinates of the mouse
    {
        mx = 0;
        my = 0;
    }
}
