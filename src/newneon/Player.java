package newneon;
import java.awt.Point;
import newneon.constants.directions;

public class Player
{
    //This class will hold information about the player
    private directions currentDirection;
    private Point currentPosition;
    private boolean dead = false;

    public Player(directions currentDirection, int x, int y)
    {
        this.currentDirection = currentDirection;
        currentPosition = new Point(x, y);
    }

    public Player(directions currentDirection, Point currentPosition)
    {
        this.currentPosition = currentPosition;
        this.currentDirection = currentDirection;
    }

    public Point getPosition()
    {
        return currentPosition;
    }

    public directions getCurrentDirection()
    {
        return currentDirection;
    }

    public void setCurrentPosition(int x, int y)
    {
        currentPosition.setLocation(x, y);
    }

    public void setCurrentDirection(directions newDirection)
    {
        currentDirection = newDirection;
    }

    public boolean isDead()
    {
        return dead;
    }

    public void setDead(boolean status) { dead = status; }

    //depending on the direction of the player in its current status, we may 
    public void incrementPosition() {

    }
}
