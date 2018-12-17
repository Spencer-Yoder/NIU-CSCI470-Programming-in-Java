import java.applet.Applet;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class Ball {
    private Color color;
    private int radius;
    private int x;
    private int y;
    private int dx;
    private int dy;
    private java.applet.AudioClip clip;

    //Default constructor for the ball objects. Sets up all the info
    //Arguments: X position for the ball
    //           Y position for the ball
    //           DX direction for the x axis
    //           DY direction for the Y axis
    //Returns: None
    Ball(Color color, int radius, int x, int y, int dx, int dy)
    {
        //set all of the variables
        this.color = color;
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;

        try {
            clip = Applet.newAudioClip(new URL("File:ball.wav"));   //set up the audio sound for when the ball hits the wall
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    //Function that moves the ball by the dx and dy value
    //Argument: A reference to a Dimension value of the panel size
    //Return: None
    public void move(Dimension d)
    {
        if(x <= radius || x >= d.width - radius)    //If the Ball is less than OR more than the radius of the ball to the wall flip the direction
        {
            dx = dx * -1;   //Flip the direction
            clip.play();    //Play the hit sound effect

        }

        if(y <= radius || y >= d.height - radius)   //If the Ball is less than OR more than the radius of the ball to the wall flip the direction
        {
            dy = dy * -1;   //Flip the direction
            clip.play();    //Play the hit sound effect
        }

        x = x + dx; //Move the x and y value the specified amount from the dx and dy
        y = y + dy;
    }

    //Function that redraws the circles on the screen
    //Arguments: x and y value for where the ball is and size of the ball
    //Returns: None
    private void fillOval(int x, int y, int width, int height)
    {
        Graphics t = null;
        t.drawOval(x, y, width, height);

    }

    //Function that redraws the circles
    //Arguments: Reference to the Graphics object
    //Requires: None
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, radius*2, radius*2);
    }
}
