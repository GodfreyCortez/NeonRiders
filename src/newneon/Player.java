package newneon;
import java.awt.Point;
import newneon.constants.ColorScheme;
import newneon.constants.Directions;

public class Player
{
    //This class will hold information about the player
    private Directions currentDirection;
    private Point currentPosition;
    private ColorScheme lightCycle;
    private boolean dead = false;
    private int score = 0;
    public Player(Directions currentDirection, int x, int y)
    {
        this.currentDirection = currentDirection;
        currentPosition = new Point(x, y);
    }

    public Player(Directions currentDirection, Point currentPosition, ColorScheme lightCycle)
    {
        this.currentPosition = currentPosition;
        this.currentDirection = currentDirection;
        this.lightCycle = lightCycle;
    }

    public ColorScheme getLightCycle() { return lightCycle; }

    public Point getPosition()
    {
        return currentPosition;
    }

    public Directions getCurrentDirection()
    {
        return currentDirection;
    }

    public void setCurrentPosition(int x, int y)
    {
        currentPosition.setLocation(x, y);
    }

    public void setCurrentDirection(Directions newDirection)
    {
        currentDirection = newDirection;
    }

    public boolean isDead() { return dead; }

    public void setDead(boolean status) { dead = status; }

    public int getScore() { return score; }
    // depending on the direction of the player in its current status, we may
    public void incrementPosition() {
        if(isDead())
            return;
        int x = currentPosition.x;
        int y = currentPosition.y;
        switch(currentDirection){
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
            case UP:
                y++;
                break;
            case DOWN:
                y--;
                break;
        }

        setCurrentPosition(x, y);
    }

    public void incrementScore () { score++; }

    public void changeDirection(Directions keyDirection) {
        switch(keyDirection) {
            case UP:
                if(currentDirection == Directions.DOWN) return;
            case LEFT:
                if(currentDirection == Directions.RIGHT) return;
            case RIGHT:
                if(currentDirection == Directions.LEFT) return;
            case DOWN:
                if(currentDirection == Directions.UP) return;
        }

        setCurrentDirection(keyDirection);
    }
}
