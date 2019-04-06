//Developed in 2016

package newneon;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class Game_Loop extends JPanel {
    public static sound music = null;
    public Font Mr_Robot = null;
    public ImageIcon pause_icon = new ImageIcon("assets/Pause.jpg");
    public ImageIcon play_icon = new ImageIcon("assets/Play.jpg");
    private static final int WIDTH = 1440;
    private static final int HEIGHT = 900;
    public static boolean runFlag = false;
    public static boolean paused = false;
    private int fps = 60;
    private int frameCount = 0;
    public static byte[] p_direction = {2, 0, 2, 0};
    public static byte num_of_player = 4;
    private Random rand = new Random();
    byte Colour = 1;
    byte animationY = 0;
    //Since we are using a 10x10 square to draw the light, we can reduce the scale of our map by a factor of 10
    //Therefore our imaginary map is 144 x 930 squares which will then scale up to 1440 x 900 when we actually draw, this significantly reduces our required memory
    public static final boolean[][] collision_map = new boolean[146][92]; //This is a map of trues and falses to check whether or not there is a collision
    public static final byte[][] map = new byte[144][90]; //This is a 2D array for the map, which will determine the colour (The object is final, but not the contents inside)
    public static int playerX[] = {144 / 10, 144 * 9 / 10, 144 / 10, 144 * 9 / 10};
    public static int playerY[] = {90 / 5, 90 / 5, 90 * 4 / 5, 90 * 4 / 5};
    //this will contain our array of background images, it mainly consists of the main menu, instruction page, multiplayer select, and continue
    public ImageIcon[] background = {new ImageIcon("assets/Neon Menu.jpg"), new ImageIcon("assets/Neon Instructions.jpg"),
            new ImageIcon("assets/Neon Multiplayer Select.jpg"), new ImageIcon("assets/Neon Continue.jpg")};
    public static Color[] light_cycle = {new Color(41, 171, 226), new Color(255, 105, 39), //this is the array for light cycle colours
            new Color(140, 198, 63), new Color(255, 151, 163),
            new Color(212, 20, 90), new Color(147, 39, 143)};
    public static Color back_color = new Color(1, 1, 15);
    public static Color grid_lines = new Color(77, 77, 255);
    public static boolean[] p_dead = new boolean[4];
    public static int[] p_score = new int[4];
    public static STATE state = STATE.MENU;

    public Game_Loop() //constructor that will add all our components
    {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.LIGHT_GRAY);
        this.setFocusable(true);
        this.requestFocus();
        this.setDoubleBuffered(true);
        this.addKeyListener(new keyListener());
        this.addMouseListener(new mouseListener());
    }

    public enum STATE //enums to control what state the game is in
    {
        MENU,
        GAME,
        MULTIPLAYER_SELECTION,
        INSTRUCTIONS,
        CONTINUE
    }



    public void runGameLoop() //method that will run the whole game loop
    {
        Thread loop = new Thread() //create a new thread named loop
        {
            @Override
            public void run() //overrides the run method
            {
                gameLoop(); //runs the game loop
            }
        };
        loop.start(); //starts loop thread
        Thread sound = new Thread() //create a new thread for the sound
        {
            @Override
            public void run() //method to run music
            {
                music = new sound(); // create a new instance of sound class
                music.playsong(); //play music, this will continuously loop through our playlist of songs at random
            }
        };
        sound.start(); //starts the sound thread
    }


    private void gameLoop() {
        //This value would probably be stored elsewhere.
        final double GAME_HERTZ = 30.0;
        //Calculate how many ns each frame should take for our target game hertz.
        final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
        //At the very most we will update the game this many times before a new render.
        //If you're worried about visual hitches more than perfect timing, set this to 1.
        final int MAX_UPDATES_BEFORE_RENDER = 5;
        //We will need the last update time.
        double lastUpdateTime = System.nanoTime();
        //Store the last time we rendered.
        double lastRenderTime = System.nanoTime();

        //If we are able to get as high as this FPS, don't render again.
        final double TARGET_FPS = 60;
        final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

        //Simple way of finding FPS.
        int lastSecondTime = (int) (lastUpdateTime / 1000000000);
        runFlag = true; //set the runflag of the whole application to true
        try //in this try we have inserted code to apply custom fonts when we draw our string, this code is used from http://docs.oracle.com/javase/tutorial/2d/text/fonts.html
        {
            //create the font to use
            Mr_Robot = Font.createFont(Font.TRUETYPE_FONT, new File("assets/MR ROBOT.ttf")).deriveFont(60f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("assets/MR ROBOT.ttf")));
        } catch (Exception e) {
            e.printStackTrace();
        }


        resetgame();
        resetmap();
        while (runFlag) {
            double now = System.nanoTime(); //the current time
            int updateCount = 0; //the update count of our game

            if (!paused) //check if the user has paused the game
            {
                //Do as many game updates as we need to, potentially playing catchup.
                while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
                    updateGame();
                    lastUpdateTime += TIME_BETWEEN_UPDATES;
                    updateCount++;
                }

                //If for some reason an update takes forever, we don't want to do an insane number of catchups.
                //If you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
                if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
                    lastUpdateTime = now - TIME_BETWEEN_UPDATES;
                }

                //Render. To do so, we need to calculate interpolation for a smooth render.
                //float interpolation = Math.min(1.0f, (float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES) );
                repaint();
                lastRenderTime = now;

                //Update the frames we got.
                int thisSecond = (int) (lastUpdateTime / 1000000000);
                if (thisSecond > lastSecondTime) {
                    System.out.println("NEW SECOND " + thisSecond + " " + frameCount); //output the current fps to the console window
                    fps = frameCount;
                    frameCount = 0;
                    lastSecondTime = thisSecond;
                }

                //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
                while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
                    Thread.yield();

                    //This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
                    //You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
                    //FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
                    try {
                        Thread.sleep(1);
                    } catch (Exception e) {
                    }

                    now = System.nanoTime();
                }
            }
        }
    }

    public void updateGame() //update game's variable
    {
        if (state == STATE.GAME) //check if we are in the game state/ otherwise it is not necessary to do all these updates
        {
            if (num_of_player != 4)
                AI();
            if (!p_dead[0]) //if player one is not dead than we can enter the switch
                switch (p_direction[0]) //PLAYER 1
                {
                    case 0: //update player's position going left
                        if (collision_map[playerX[0] - 1][playerY[0]] || playerX[0] == 1) //check for collision with a light trail or wall
                        {
                            p_dead[0] = true; //set this person to dead
                        }
                        playerX[0] -= 1;
                        map[playerX[0]][playerY[0]] = 1; //set the colour of my light trail
                        collision_map[playerX[0]][playerY[0]] = true; //whatever square the player has traveled on is set to true
                        break;
                    case 1:

                        if (collision_map[playerX[0]][playerY[0] - 1] || playerY[0] == 1) //check if player is going out of bounds or if they collide with a light trail
                        {
                            p_dead[0] = true; //set this person to dead
                        }
                        playerY[0] -= 1;
                        map[playerX[0]][playerY[0]] = 1;//set the colour of my light trail
                        collision_map[playerX[0]][playerY[0]] = true; //whatever square the player has traveled on is set to true
                        break;
                    case 2:
                        if (collision_map[playerX[0] + 1][playerY[0]] || playerX[0] == 143) //check if player is going out of bounds or if they collide with a light trail
                        {
                            p_dead[0] = true; //set this person to dead
                        }
                        playerX[0] += 1;
                        map[playerX[0]][playerY[0]] = 1;//set the colour of my light trail
                        collision_map[playerX[0]][playerY[0]] = true; //whatever square the player has traveled on is set to true
                        break;
                    case 3:
                        if (collision_map[playerX[0]][playerY[0] + 1] || playerY[0] == 89) //check if player is going out of bounds or if they collide with a light trail
                        {
                            p_dead[0] = true; //set this person to dead
                        }
                        playerY[0] += 1;
                        map[playerX[0]][playerY[0]] = 1;//set the colour of my light trail
                        collision_map[playerX[0]][playerY[0]] = true; //whatever square the player has traveled on is set to true
                        break;
                }
            //PlAYER 3 AI/Player
            if (!p_dead[1])
                switch (p_direction[1]) {
                    case 0:
                        if (collision_map[playerX[1] - 1][playerY[1]] || playerX[1] == 1) {
                            p_dead[1] = true;
                        }
                        playerX[1] -= 1;
                        map[playerX[1]][playerY[1]] = 3;
                        collision_map[playerX[1]][playerY[1]] = true;
                        break;
                    case 1:
                        if (collision_map[playerX[1]][playerY[1] - 1] || playerY[1] == 1) {
                            p_dead[1] = true;
                        }
                        playerY[1] -= 1;
                        map[playerX[1]][playerY[1]] = 3;
                        collision_map[playerX[1]][playerY[1]] = true;
                        break;
                    case 2:
                        if (collision_map[playerX[1] + 1][playerY[1]] || playerX[1] == 143) {
                            p_dead[1] = true;
                        }
                        playerX[1] += 1;
                        map[playerX[1]][playerY[1]] = 3;
                        collision_map[playerX[1]][playerY[1]] = true;
                        break;
                    case 3:
                        if (collision_map[playerX[1]][playerY[1] + 1] || playerY[1] == 89) {
                            p_dead[1] = true;
                        }
                        playerY[1] += 1;
                        map[playerX[1]][playerY[1]] = 3;
                        collision_map[playerX[1]][playerY[1]] = true;
                        break;
                }


            //PLAYER 3 AI/PLAYER
            if (!p_dead[2])
                switch (p_direction[2]) {
                    case 0:
                        if (collision_map[playerX[2] - 1][playerY[2]] || playerX[2] == 1) {
                            p_dead[2] = true;
                        }
                        playerX[2] -= 1;
                        map[playerX[2]][playerY[2]] = 2;
                        collision_map[playerX[2]][playerY[2]] = true;
                        break;
                    case 1:
                        if (collision_map[playerX[2]][playerY[2] - 1] || playerY[2] == 1) {
                            p_dead[2] = true;
                        }
                        playerY[2] -= 1;
                        map[playerX[2]][playerY[2]] = 2;
                        collision_map[playerX[2]][playerY[2]] = true;
                        break;
                    case 2:
                        playerX[2] += 1;
                        if (collision_map[playerX[2] + 1][playerY[2]] || playerX[2] == 143) {
                            p_dead[2] = true;
                        }
                        map[playerX[2]][playerY[2]] = 2;
                        collision_map[playerX[2]][playerY[2]] = true;
                        break;
                    case 3:
                        if (collision_map[playerX[2]][playerY[2] + 1] || playerY[2] == 89) {
                            p_dead[2] = true;
                        }
                        playerY[2] += 1;
                        map[playerX[2]][playerY[2]] = 2;
                        collision_map[playerX[2]][playerY[2]] = true;
                        break;
                }

            //PLAYER 4 AI
            if (!p_dead[3])
                switch (p_direction[3]) {
                    case 0:
                        if (collision_map[playerX[3] - 1][playerY[3]] || playerX[3] == 1) {
                            p_dead[3] = true;
                        }
                        playerX[3] -= 1;
                        map[playerX[3]][playerY[3]] = 4;
                        collision_map[playerX[3]][playerY[3]] = true;
                        break;
                    case 1:
                        if (collision_map[playerX[3]][playerY[3] - 1] || playerY[3] == 1) {
                            p_dead[3] = true;
                        }
                        playerY[3] -= 1;
                        map[playerX[3]][playerY[3]] = 4;
                        collision_map[playerX[3]][playerY[3]] = true;
                        break;
                    case 2:
                        if (collision_map[playerX[3] + 1][playerY[3]] || playerX[3] == 143) {
                            p_dead[3] = true;
                        }
                        playerX[3] += 1;
                        map[playerX[3]][playerY[3]] = 4;
                        collision_map[playerX[3]][playerY[3]] = true;
                        break;
                    case 3:
                        if (collision_map[playerX[3]][playerY[3] + 1] || playerY[3] == 89) {
                            p_dead[3] = true;
                        }
                        playerY[3] += 1;
                        map[playerX[3]][playerY[3]] = 4;
                        collision_map[playerX[3]][playerY[3]] = true;
                        break;
                }
            check_win();
        }
        //If we are in any state other than game we can create some animation
        else if (state == STATE.MENU || state == STATE.CONTINUE || state == STATE.INSTRUCTIONS || state == STATE.MULTIPLAYER_SELECTION) {
            map[1][animationY] = Colour;
            map[3][animationY] = Colour;
            animationY++;
            if (animationY >= 89) {
                Colour++;
                animationY = 0;
                if (Colour > light_cycle.length)
                    Colour = 1;
            }
        }

        //check if any player wins
    }

    public void check_win() {
        //check if any player wins
        if (p_dead[1] && p_dead[2] && p_dead[3]) //player 1 wins
        {
            p_score[0]++;
            state = STATE.CONTINUE;
            resetmap(); //resets map colours
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }

        } else if (p_dead[0] && p_dead[2] && p_dead[3]) //This is the case where player 2 wins
        {
            p_score[1]++;
            state = STATE.CONTINUE;
            resetmap(); //resets map colours
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        } else if (p_dead[0] && p_dead[1] && p_dead[3]) //This is the case where player 3 wins
        {
            p_score[2]++;
            state = STATE.CONTINUE;
            resetmap(); //resets map colours
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        } else if (p_dead[0] && p_dead[1] && p_dead[2]) //This is the case where player 4 wins
        {
            p_score[3]++;
            state = STATE.CONTINUE;
            resetmap();
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
    }

    public void AI() //This is the method to run AI
    {
        switch (num_of_player) //depending on the number of players we want to determine which light bikes to control through switching
        {
                /*ALL CODE IN THE AI METHOD IS SIMPLY FOLLOWING A DIFFERENT CASE THAN THE 4 METHODS BELOW IT 
                  A REFACTORING WILL BE CREATED IN A FUTURE SIDE PROJECT*/
            case 1:
                //PLAYER 2
                if (p_direction[1] == 0) //if the AI is facing left
                {
                    if (collision_map[playerX[1] - 1][playerY[1]]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[1]][playerY[1] - 1])//check to see if a collision will be made
                                p_direction[1] = 3;
                            else
                                p_direction[1] = 1;
                        else if (collision_map[playerX[1]][playerY[1] + 1])
                            p_direction[1] = 1;
                        else
                            p_direction[1] = 3;
                    }
                } else if (p_direction[1] == 1) //check if the player is going up
                {
                    if (collision_map[playerX[1]][playerY[1] - 1]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[1] - 1][playerY[1]])
                                p_direction[1] = 2;
                            else
                                p_direction[1] = 0;
                        else if (collision_map[playerX[1] + 1][playerY[1]])
                            p_direction[1] = 0;
                        else
                            p_direction[1] = 2;
                    }
                } else if (p_direction[1] == 2) //check if the player is going right
                {
                    if (collision_map[playerX[1] + 1][playerY[1]]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[1]][playerY[1] - 1])
                                p_direction[1] = 3;
                            else
                                p_direction[1] = 1;
                        else if (collision_map[playerX[1]][playerY[1] + 1])
                            p_direction[1] = 1;
                        else
                            p_direction[1] = 3;
                    }
                } else if (p_direction[1] == 3) //check if the player is going down
                {
                    if (collision_map[playerX[1]][playerY[1] + 1]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[1] - 1][playerY[1]])
                                p_direction[1] = 2;
                            else
                                p_direction[1] = 0;
                        else if (collision_map[playerX[1] + 1][playerY[1]])
                            p_direction[1] = 0;
                        else
                            p_direction[1] = 2;
                    }
                }

                //PLAYER 3
                if (p_direction[2] == 0) {
                    if (collision_map[playerX[2] - 1][playerY[2]]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[2]][playerY[2] - 1])
                                p_direction[2] = 3;
                            else
                                p_direction[2] = 1;
                        else if (collision_map[playerX[2]][playerY[2] + 1])
                            p_direction[2] = 1;
                        else
                            p_direction[2] = 3;
                    }
                } else if (p_direction[2] == 1) {
                    if (collision_map[playerX[2]][playerY[2] - 1]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[2] - 1][playerY[2]])
                                p_direction[2] = 2;
                            else
                                p_direction[2] = 0;
                        else if (collision_map[playerX[2] + 1][playerY[2]])
                            p_direction[2] = 0;
                        else
                            p_direction[2] = 2;
                    }
                } else if (p_direction[2] == 2) {
                    if (collision_map[playerX[2] + 1][playerY[2]]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[2]][playerY[2] - 1])
                                p_direction[2] = 3;
                            else
                                p_direction[2] = 1;
                        else if (collision_map[playerX[2]][playerY[2] + 1])
                            p_direction[2] = 1;
                        else
                            p_direction[2] = 3;
                    }
                } else if (p_direction[2] == 3) {
                    if (collision_map[playerX[2]][playerY[2] + 1]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[1] - 1][playerY[2]])
                                p_direction[2] = 2;
                            else
                                p_direction[2] = 0;
                        else if (collision_map[playerX[2] + 1][playerY[2]])
                            p_direction[2] = 0;
                        else
                            p_direction[2] = 2;
                    }
                }
                //PLAYER 4 AI
                if (p_direction[3] == 0) {
                    if (collision_map[playerX[3] - 1][playerY[3]]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[3]][playerY[3] - 1])
                                p_direction[3] = 3;
                            else
                                p_direction[3] = 1;
                        else if (collision_map[playerX[3]][playerY[3] + 1])
                            p_direction[3] = 1;
                        else
                            p_direction[3] = 3;
                    }
                } else if (p_direction[3] == 1) {
                    if (collision_map[playerX[3]][playerY[3] - 1]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[3] - 1][playerY[3]])
                                p_direction[3] = 2;
                            else
                                p_direction[3] = 0;
                        else if (collision_map[playerX[2] + 1][playerY[2]])
                            p_direction[3] = 0;
                        else
                            p_direction[3] = 2;
                    }
                } else if (p_direction[3] == 2) {
                    if (collision_map[playerX[3] + 1][playerY[3]]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[3]][playerY[3] - 1])
                                p_direction[3] = 3;
                            else
                                p_direction[3] = 1;
                        else if (collision_map[playerX[3]][playerY[3] + 1])
                            p_direction[3] = 1;
                        else
                            p_direction[3] = 3;
                    }
                } else if (p_direction[3] == 3) {
                    if (collision_map[playerX[3]][playerY[3] + 1]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[3] - 1][playerY[3]])
                                p_direction[3] = 2;
                            else
                                p_direction[3] = 0;
                        else if (collision_map[playerX[2] + 1][playerY[2]])
                            p_direction[3] = 0;
                        else
                            p_direction[3] = 2;
                    }
                }
                break;
            case 2: //IF THERE ARE 2 PLAYERS THERE IS NO NEED FOR 3 AI's ONLY 2
                //PLAYER 3
                if (p_direction[2] == 0) {
                    if (collision_map[playerX[2] - 1][playerY[2]]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[2]][playerY[2] - 1])
                                p_direction[2] = 3;
                            else
                                p_direction[2] = 1;
                        else if (collision_map[playerX[2]][playerY[2] + 1])
                            p_direction[2] = 1;
                        else
                            p_direction[2] = 3;
                    }
                } else if (p_direction[2] == 1) {
                    if (collision_map[playerX[2]][playerY[2] - 1]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[2] - 1][playerY[2]])
                                p_direction[2] = 2;
                            else
                                p_direction[2] = 0;
                        else if (collision_map[playerX[2] + 1][playerY[2]])
                            p_direction[2] = 0;
                        else
                            p_direction[2] = 2;
                    }
                } else if (p_direction[2] == 2) {
                    if (collision_map[playerX[2] + 1][playerY[2]]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[2]][playerY[2] - 1])
                                p_direction[2] = 3;
                            else
                                p_direction[2] = 1;
                        else if (collision_map[playerX[2]][playerY[2] + 1])
                            p_direction[2] = 1;
                        else
                            p_direction[2] = 3;
                    }
                } else if (p_direction[2] == 3) {
                    if (collision_map[playerX[2]][playerY[2] + 1]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[1] - 1][playerY[2]])
                                p_direction[2] = 2;
                            else
                                p_direction[2] = 0;
                        else if (collision_map[playerX[2] + 1][playerY[2]])
                            p_direction[2] = 0;
                        else
                            p_direction[2] = 2;
                    }
                }
                //PLAYER 4 AI
                if (p_direction[3] == 0) {
                    if (collision_map[playerX[3] - 1][playerY[3]]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[3]][playerY[3] - 1])
                                p_direction[3] = 3;
                            else
                                p_direction[3] = 1;
                        else if (collision_map[playerX[3]][playerY[3] + 1])
                            p_direction[3] = 1;
                        else
                            p_direction[3] = 3;
                    }
                } else if (p_direction[3] == 1) {
                    if (collision_map[playerX[3]][playerY[3] - 1]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[3] - 1][playerY[3]])
                                p_direction[3] = 2;
                            else
                                p_direction[3] = 0;
                        else if (collision_map[playerX[2] + 1][playerY[2]])
                            p_direction[3] = 0;
                        else
                            p_direction[3] = 2;
                    }
                } else if (p_direction[3] == 2) {
                    if (collision_map[playerX[3] + 1][playerY[3]]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[3]][playerY[3] - 1])
                                p_direction[3] = 3;
                            else
                                p_direction[3] = 1;
                        else if (collision_map[playerX[3]][playerY[3] + 1])
                            p_direction[3] = 1;
                        else
                            p_direction[3] = 3;
                    }
                } else if (p_direction[3] == 3) {
                    if (collision_map[playerX[3]][playerY[3] + 1]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[3] - 1][playerY[3]])
                                p_direction[3] = 2;
                            else
                                p_direction[3] = 0;
                        else if (collision_map[playerX[2] + 1][playerY[2]])
                            p_direction[3] = 0;
                        else
                            p_direction[3] = 2;
                    }
                }
                break;
            case 3: //IF THERE ARE 3 HUMAN PLAYERS ONLY 1 AI IS REQUIRED
                //PLAYER 4 AI
                if (p_direction[3] == 0) {
                    if (collision_map[playerX[3] - 1][playerY[3]]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[3]][playerY[3] - 1])
                                p_direction[3] = 3;
                            else
                                p_direction[3] = 1;
                        else if (collision_map[playerX[3]][playerY[3] + 1])
                            p_direction[3] = 1;
                        else
                            p_direction[3] = 3;
                    }
                } else if (p_direction[3] == 1) {
                    if (collision_map[playerX[3]][playerY[3] - 1]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[3] - 1][playerY[3]])
                                p_direction[3] = 2;
                            else
                                p_direction[3] = 0;
                        else if (collision_map[playerX[2] + 1][playerY[2]])
                            p_direction[3] = 0;
                        else
                            p_direction[3] = 2;
                    }
                } else if (p_direction[3] == 2) {
                    if (collision_map[playerX[3] + 1][playerY[3]]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[3]][playerY[3] - 1])
                                p_direction[3] = 3;
                            else
                                p_direction[3] = 1;
                        else if (collision_map[playerX[3]][playerY[3] + 1])
                            p_direction[3] = 1;
                        else
                            p_direction[3] = 3;
                    }
                } else if (p_direction[3] == 3) {
                    if (collision_map[playerX[3]][playerY[3] + 1]) {
                        if (rand.nextBoolean())
                            if (collision_map[playerX[3] - 1][playerY[3]])
                                p_direction[3] = 2;
                            else
                                p_direction[3] = 0;
                        else if (collision_map[playerX[2] + 1][playerY[2]])
                            p_direction[3] = 0;
                        else
                            p_direction[3] = 2;
                    }
                }
                break;
        }
    }

    public static void resetgame() //sets the whole map to 0 9since we must reference it to other classes, it is static and public
    {
        //Reset all the player positions and directions
        playerX[0] = 144 / 10;
        playerX[1] = 144 * 9 / 10;
        playerX[2] = 144 / 10;
        playerX[3] = 144 * 9 / 10;
        playerY[0] = 90 / 5;
        playerY[1] = 90 / 5;
        playerY[2] = 90 * 4 / 5;
        playerY[3] = 90 * 4 / 5;
        p_direction[0] = 2;
        p_direction[1] = 0;
        p_direction[2] = 2;
        p_direction[3] = 0;
        for (int x = 0; x < WIDTH / 10 - 1; x++) {
            for (int y = 0; y < HEIGHT / 10 - 1; y++) {
                map[x][y] = 0; //reset the whole board to zero removing all colours
                collision_map[x][y] = false; //removing all collisions
            }
        }
        for (int x = 0; x < WIDTH / 10 - 1; x++) //making the walls/adding them back im
        {
            collision_map[x][0] = true; //add collisions to the top wall
            collision_map[x][89] = true; //add collisions to the bottom wall
        }
        for (int y = 0; y < HEIGHT / 10 - 1; y++) //making the walls /adding them back in
        {
            collision_map[0][y] = true; //add collisions to the left wall
            collision_map[143][y] = true; //add collisions to the right wall
        }

        for (int x = 0; x < Game_Loop.p_score.length; x++) //loop to reset the score of each character
        {
            Game_Loop.p_dead[x] = false;
        }
    }

    public void resetmap() //This method is to solely reset the map colours
    {
        for (int x = 0; x < WIDTH / 10 - 1; x++) {
            for (int y = 0; y < HEIGHT / 10 - 1; y++) {
                map[x][y] = 0; //reset the whole board to zero removing all colours
                collision_map[x][y] = false; //removing all collisions
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        if (state != STATE.GAME) //depending on what state we are in we want to render different things
        {
            super.paintComponent(g);
            g.setColor(back_color); //set the back colour
            g.fillRect(0, 0, WIDTH, HEIGHT); //drawing the background(erase anything previously drawn
            if (state == STATE.MENU) //if we are in the menu state
                g.drawImage(background[0].getImage(), 0, 0, WIDTH, HEIGHT, this); //draw image
            else if (state == STATE.INSTRUCTIONS) //if we are in the multiplayer select state draw this
                g.drawImage(background[1].getImage(), 0, 0, WIDTH, HEIGHT, this); //draw image
            else if (state == STATE.MULTIPLAYER_SELECTION)
                g.drawImage(background[2].getImage(), 0, 0, WIDTH, HEIGHT, this);
            else if (state == STATE.CONTINUE) //continue state when everybody has died
            {
                //Output the scores of each of the players
                g.drawImage(background[3].getImage(), 0, 0, WIDTH, HEIGHT, this); //draw the continue screen
                g.setFont(Mr_Robot);
                g.setColor(Color.WHITE);
                g.drawString("Scores", 220, 380);
                g.setColor(light_cycle[0]);
                g.drawString("Blue   " + p_score[0], 220, 470); //display score of blue player
                g.setColor(light_cycle[2]);
                g.drawString("Green  " + p_score[1], 220, 540); //display score of green player
                g.setColor(light_cycle[1]);
                g.drawString("Orange  " + p_score[2], 220, 610); //diplay score of orange player
                g.setColor(light_cycle[3]);
                g.drawString("Pink    " + p_score[3], 220, 680); //display score of pink player
            }
            for (int x = 1; x < 4; x++) //since the animation is only on the left side we only need to check the first four x squares
            {
                for (int y = 0; y < HEIGHT / 10 - 1; y++) {
                    switch (map[x][y]) //depending on the number that we find at the x and y position we will place a different point
                    {
                        case 0:
                            continue; //if 0 skip this iteration of the loop and don't draw anything
                        case 1:
                            g.setColor(light_cycle[0]); //set it to blue
                            break;
                        case 2:
                            g.setColor(light_cycle[1]); //set to green
                            break;
                        case 3:
                            g.setColor(light_cycle[2]);//set to orange
                            break;
                        case 4:
                            g.setColor(light_cycle[3]); //set to pink
                            break;
                        case 5:
                            g.setColor(light_cycle[4]); //set to scarlet
                            break;
                        case 6:
                            g.setColor(light_cycle[5]); //set to purple
                            break;
                    }
                    g.fillRect(x * 10, y * 10, 10, 10); //draw the tile with whatever colour was set
                }
            }

        } else //if we are not in any of the other states than we must be in the game state
        {
            super.paintComponent(g);
            g.setColor(back_color);
            g.fillRect(0, 0, WIDTH, HEIGHT); //drawing the background
            if (state == STATE.GAME) {
                g.setColor(grid_lines); //set the color of the grid lines
                for (double xrect = WIDTH / 15; xrect <= WIDTH; xrect += (WIDTH / 15)) //This will draw our vertical grid lines
                {
                    g.fillRect((int) xrect, 0, 1, HEIGHT);
                }
                for (double yrect = HEIGHT / 10; yrect <= HEIGHT; yrect += (HEIGHT / 10)) //this will darw our horizontal grid lines
                {
                    g.fillRect(0, (int) yrect, WIDTH, 1);
                }
                g.setColor(Color.WHITE); //set the color to white
                g.fillRect(0, 0, WIDTH, 10); //draw the top wall
                g.fillRect(0, 0, 10, HEIGHT);//draw left wall
                g.fillRect(WIDTH - 10, 0, 10, HEIGHT); //draw the right wall
                g.fillRect(0, HEIGHT - 10, WIDTH, 10); //draw bottom Wall
                if (!mouseListener.has_paused)
                    g.drawImage(pause_icon.getImage(), 15, 15, 40, 40, this); //draw our pause icon
                else
                    g.drawImage(play_icon.getImage(), 15, 15, 40, 40, this);
                for (int x = 0; x < WIDTH / 10 - 1; x++) {
                    for (int y = 0; y < HEIGHT / 10 - 1; y++) {
                        switch (map[x][y]) //look at what number is at this position on the map and change colour depending on the number
                        {
                            case 0:
                                continue; //if 0 skip this iteration of the loop and don't draw anything
                            case 1:
                                g.setColor(light_cycle[0]); //set to blue
                                break;
                            case 2:
                                g.setColor(light_cycle[1]); //set to green
                                break;
                            case 3:
                                g.setColor(light_cycle[2]); //set to orange
                                break;
                            case 4:
                                g.setColor(light_cycle[3]); //set to pink
                                break;
                        }
                        g.fillRect(x * 10, y * 10, 10, 10); //draw the square of light
                    }
                }
                if (!p_dead[0])
                    g.setColor(Color.WHITE); //set colour to white
                else
                    g.setColor(light_cycle[4]); //otherwise they are red
                switch (p_direction[0])//direction of head for player 1
                {
                    case 0:
                        g.fillRect(playerX[0] * 10 - 10, playerY[0] * 10, 10, 10);
                        break;
                    case 1:
                        g.fillRect(playerX[0] * 10, playerY[0] * 10 - 10, 10, 10);
                        break;
                    case 2:
                        g.fillRect(playerX[0] * 10 + 10, playerY[0] * 10, 10, 10);
                        break;
                    case 3:
                        g.fillRect(playerX[0] * 10, playerY[0] * 10 + 10, 10, 10);
                        break;
                }
                if (!p_dead[1])

                    g.setColor(Color.WHITE); //the colour of head is white
                else
                    g.setColor(light_cycle[4]);
                switch (p_direction[1]) //(player 2)
                {
                    case 0:
                        g.fillRect(playerX[1] * 10 - 10, playerY[1] * 10, 10, 10);
                        break;
                    case 1:
                        g.fillRect(playerX[1] * 10, playerY[1] * 10 - 10, 10, 10);
                        break;
                    case 2:
                        g.fillRect(playerX[1] * 10 + 10, playerY[1] * 10, 10, 10);
                        break;
                    case 3:
                        g.fillRect(playerX[1] * 10, playerY[1] * 10 + 10, 10, 10);
                        break;
                }
                if (!p_dead[2])
                    g.setColor(Color.WHITE); //set colour to white if alive
                else
                    g.setColor(light_cycle[4]);
                switch (p_direction[2]) //(player 3)
                {
                    case 0:
                        g.fillRect(playerX[2] * 10 - 10, playerY[2] * 10, 10, 10);
                        break;
                    case 1:
                        g.fillRect(playerX[2] * 10, playerY[2] * 10 - 10, 10, 10);
                        break;
                    case 2:
                        g.fillRect(playerX[2] * 10 + 10, playerY[2] * 10, 10, 10); //
                        break;
                    case 3:
                        g.fillRect(playerX[2] * 10, playerY[2] * 10 + 10, 10, 10);
                        break;
                }
                if (!p_dead[3])
                    g.setColor(Color.WHITE); //colour of head is white
                else
                    g.setColor(light_cycle[4]);
                switch (p_direction[3]) //(player 4)
                {
                    case 0:
                        g.fillRect(playerX[3] * 10 - 10, playerY[3] * 10, 10, 10);
                        break;
                    case 1:
                        g.fillRect(playerX[3] * 10, playerY[3] * 10 - 10, 10, 10);
                        break;
                    case 2:
                        g.fillRect(playerX[3] * 10 + 10, playerY[3] * 10, 10, 10);
                        break;
                    case 3:
                        g.fillRect(playerX[3] * 10, playerY[3] * 10 + 10, 10, 10);
                        break;
                }
            }
        }
        frameCount++; //a way to keep track of the framecount
    }

}
