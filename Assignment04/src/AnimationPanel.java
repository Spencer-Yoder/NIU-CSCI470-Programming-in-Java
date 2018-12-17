import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements  Runnable{
    private ArrayList<Ball> balls = new ArrayList<>();
    private Dimension dimension = null;
    private int speed = 30; //default speed to Normal
    private volatile Thread thread1 = null;


    //Function that start the animation by creating a
    //  new thread to play it in.
    //Arguments: None
    //Returns: None
    public void start() {
        if(thread1 == null) //If there is no thread
        {
            thread1 = new Thread(this); //make a new thread
            thread1.start();    //start the animation on the new thread
        }
    }

    //Function that stops the animation if it is going.
    //Arguments: None
    //Returns: None
    public void stop() {
        if(thread1 != null)
        {
            thread1.interrupt();    //stop the thread
            thread1 = null; //delete thread reference
        }
    }

    //Function that paints that animation frame over and over when
    //  repaint is called. The first time it runs, it creates the
    //  ball objects,
    //Arguments: Graphics Object
    //Returns: None
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);    //call the super class of Paint components to do all other tasks

        if(dimension == null)   //If there is no balls, make them
        {
            balls.add(new Ball(Color.RED, 10, 20, 20, 10, 10)); //add balls to the array
            balls.add(new Ball(Color.GREEN, 10, 200, 100, -10, -10));
            balls.add(new Ball(Color.BLUE, 10, 50, 300, 10, -10));
        }

        dimension = getSize();  //get the current size of the panel

        setBackground(Color.WHITE); //set the background color

        //for all object in the array list move them and repaint them
        for(Ball b : balls)
        {
            b.move(dimension);  //call balls move function
            b.draw(g);  //call balls move function
        }
    }

    //Override the run function for the runnable class.
    //  Keeps calling repaint for the animation then sleeps
    //  for the amount of time the user set/
    //Arguments: None
    //Returns: None
    @Override
    public void run() {
        while (Thread.currentThread() == thread1)   //If the current thread is still there
        {
            try
            {
                repaint();  //repaint the balls
                thread1.sleep(speed);   //send the thread to bed
            }
            catch (InterruptedException e)
            {
                System.out.println(e);
                return;
            }
        }
    }

    //Function that sets the speed set by the user
    //Arguments: speed for the animation
    //Returns: None
    public void setSpeed(int i) {
        speed = i;  //set the speed variable
    }
}
