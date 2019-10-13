//Developed in 2016
package newneon;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

import newneon.constants.ColorScheme;
import newneon.constants.LightCycleColors;
import newneon.constants.State;
import newneon.constants.Directions;

public class Game_Loop extends JPanel {
    public static sound music = null;
    public Font Mr_Robot = null;
    public ImageIcon pause_icon = new ImageIcon("assets/Pause.jpg");
    public ImageIcon play_icon = new ImageIcon("assets/Play.jpg");
    private static final int WIDTH = 1440;
    private static final int HEIGHT = 900;
    public static boolean runFlag = false;
    private int fps = 60;
    private int frameCount = 0;

    public ImageIcon[] background = {new ImageIcon("assets/Neon Menu.jpg"), new ImageIcon("assets/Neon Instructions.jpg"),
            new ImageIcon("assets/Neon Multiplayer Select.jpg"), new ImageIcon("assets/Neon Continue.jpg")};


    public static int numOfPlayers = 4;
    private boolean paused = false;
    private State state = State.MENU;
    private GameData gameData = null;
    private LightCycleColors cycleColors = LightCycleColors.getInstance();
    private static final int dataScale = 10; // Since squares are 10 x 10 to draw, we can scale data to draw down by 10
    private Intelligence intelligence = new Intelligence();
    public Game_Loop() //constructor that will add all our components
    {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.LIGHT_GRAY);
        this.setFocusable(true);
        this.requestFocus();
        this.setDoubleBuffered(true);
        this.addKeyListener(new keyListener(this));
        this.addMouseListener(new mouseListener(this));
    }

    public State getState() { return state; }
    public void setState(State state) { this.state = state; }
    public GameData getGameData() { return gameData; }
    public void setGameData(GameData gameData) { this.gameData = gameData; }
    public void togglePaused () { paused = !paused; }
    public int getNumOfPlayers() { return numOfPlayers; }
    public void setNumOfPlayers(int numOfPlayers) {
        if(numOfPlayers > 0 && numOfPlayers <= 4) {
            this.numOfPlayers = numOfPlayers;
        } else {
            numOfPlayers = 1;
        }
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


        while (runFlag) {
            double now = System.nanoTime(); //the current time
            int updateCount = 0; //the update count of our game

            if (!paused) {//check if the user has paused the game
                //Do as many game updates as we need to, potentially playing catchup.
                while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
                    update();
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

    private GameData createNewGame() {
        int width = getWidth() / dataScale;
        int height = getHeight() / dataScale;

        // generate the players and their directions
        Point[] initialPosition = new Point[] {
           new Point(width / 10, height / 5),
           new Point(width * 9 / 10, height / 5),
           new Point(width / 10, height * 4/5),
           new Point(width * 9 / 10, height * 4/ 5)
        };
        Player[] players = new Player[] {
            new Player(Directions.RIGHT, initialPosition[0], ColorScheme.TEAL),
            new Player(Directions.LEFT, initialPosition[1], ColorScheme.PINK),
            new Player(Directions.RIGHT, initialPosition[2], ColorScheme.NEON_GREEN),
            new Player(Directions.LEFT, initialPosition[3], ColorScheme.ORANGE)
        };

        Color[][] map = new Color[width][height];
        boolean[][] collisionMap = new boolean[width][height];

        return new GameData(numOfPlayers, collisionMap, map, players);
    }

    //returns true if a player has won the game instance
    //false otherwise
    private boolean checkWin() {
        int numDeadPlayers = 0;
        Player[] playerList = gameData.getPlayerData();

        for(Player player : playerList) {
            if(player.isDead()) {
                numDeadPlayers++;
            }
        }

        if(numDeadPlayers >= (numOfPlayers - 1)) {
            //find the last player alive
            for(Player player : playerList) {
                if(!player.isDead()) {
                    player.incrementScore();
                    return true;
                }
            }
        }
        return false;
    }

    public void update() {
        if(state == State.GAME) {
            if(gameData == null) {
                gameData = createNewGame();
            }
            //Now that we have an instance of this specific game we can look at the players and update them accordingly

            //Step 1: Check if any players have collided with one another or out of bounds
            Player[] playerList = gameData.getPlayerData();
            for(Player player : playerList) {
                int x = player.getPosition().x;
                int y = player.getPosition().y;
                boolean[][] collisionMap = gameData.getCollisionMap();
                Color[][] map = gameData.getMap();
                if(x < 0 || x > collisionMap[0].length - 1 || y < 0 || y > collisionMap.length - 1 || collisionMap[x][y]) {
                    player.setDead(true);
                } else {
                    //increment their position and update the maps
                    player.incrementPosition();
                    x = player.getPosition().x;
                    y = player.getPosition().y;

                    map[x][y] = cycleColors.getColor(player.getLightCycle());
                    collisionMap[x][y] = true;

                    gameData.setMap(map);
                    gameData.setCollisionMap(collisionMap);
                }
            }

            //Step 2: Run intelligence here to see if we should change their direction on the next
            //update
            if(gameData.getNumAIPlayers() != 0) {
                for(int i = gameData.getNumAIPlayers(); i > (numOfPlayers - gameData.getNumAIPlayers()); i--) {
                    playerList[i].setCurrentDirection(intelligence.chooseDirection(playerList[i], gameData));
                }
            }

            //Step 3: Check win conditions
            if(checkWin()) {
                gameData.resetMaps();
                state = State.CONTINUE;
            }
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(cycleColors.getColor(ColorScheme.NEAR_BLACK));
        g.fillRect(0, 0, getWidth(), getHeight()); //drawing the background

        Player[] playerList = gameData.getPlayerData();
        Color[][] map = gameData.getMap();

        if (state != State.GAME) {//depending on what state we are in we want to render different things
            if (state == State.MENU) //if we are in the menu state
                g.drawImage(background[0].getImage(), 0, 0, WIDTH, HEIGHT, this); //draw image
            else if (state == State.INSTRUCTIONS) //if we are in the multiplayer select state draw this
                g.drawImage(background[1].getImage(), 0, 0, WIDTH, HEIGHT, this); //draw image
            else if (state == State.MULTIPLAYER_SELECTION)
                g.drawImage(background[2].getImage(), 0, 0, WIDTH, HEIGHT, this);
            else if (state == State.CONTINUE) {
                //Output the scores of each of the players
                g.drawImage(background[3].getImage(), 0, 0, WIDTH, HEIGHT, this); //draw the continue screen
                g.setFont(Mr_Robot);
                g.setColor(Color.WHITE);
                g.drawString("Scores", 220, 380);
                g.setColor(cycleColors.getColor(ColorScheme.TEAL));
                g.drawString("Blue   " + playerList[0].getScore(), 220, 470); //display score of blue player
                g.setColor(cycleColors.getColor(ColorScheme.PINK));
                g.drawString("Pink  " + playerList[1].getScore(), 220, 540); //display score of green player
                g.setColor(cycleColors.getColor(ColorScheme.NEON_GREEN));
                g.drawString("Green  " + playerList[2].getScore(), 220, 610); //diplay score of orange player
                g.setColor(cycleColors.getColor(ColorScheme.ORANGE));
                g.drawString("Orange    " + playerList[3].getScore(), 220, 680); //display score of pink player
            }
        } else {
            //if we are not in any of the other states than we must be in the game state
            //set the grid lines up
            g.setColor(cycleColors.getColor(ColorScheme.FUTURE_BLUE));
            for(float xrect = getWidth()/ 15; xrect <= getWidth(); xrect += (getWidth() / 15)) {
                g.fillRect((int) xrect, 0, 1, HEIGHT);
            }
            for(float yrect = getHeight() / 10; yrect <= getHeight(); yrect += (getHeight() / 10)) {
                g.fillRect(0, (int) yrect, WIDTH, 1);
            }

            if (!paused)
                g.drawImage(pause_icon.getImage(), 15, 15, 40, 40, this); //draw our pause icon
            else
                g.drawImage(play_icon.getImage(), 15, 15, 40, 40, this);

            for (int x = 0; x < getWidth()/ 10 - 1; x++) {
                for (int y = 0; y < getHeight() / 10 - 1; y++) {
                    if(map[x][y] != null) {
                        g.setColor(map[x][y]);
                        g.fillRect(x * 10, y * 10, 10, 10); //draw the square of light
                    }
                }
            }

            for(Player player: playerList) {
                Color currentColor = player.isDead() ? Color.WHITE : cycleColors.getColor(ColorScheme.RED);
                g.setColor(currentColor);

                int playerX = player.getPosition().x;
                int playerY = player.getPosition().y;
                switch(player.getCurrentDirection()) {
                    case UP:
                        g.fillRect(playerX * 10 - 10, playerY * 10, 10, 10);
                        break;
                    case RIGHT:
                        g.fillRect(playerX * 10, playerY * 10 - 10, 10, 10);
                        break;
                    case DOWN:
                        g.fillRect(playerX * 10 + 10, playerY * 10, 10, 10);
                        break;
                    case LEFT:
                        g.fillRect(playerX * 10, playerY * 10 + 10, 10, 10);
                        break;
                }
            }
        }
        frameCount++; //a way to keep track of the framecount
    }

}
