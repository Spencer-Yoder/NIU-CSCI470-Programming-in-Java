import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.BorderLayout.*;

public class BouncingBallPanel extends JPanel implements ActionListener {
    private AnimationPanel animation = new AnimationPanel();    //Create the animation panel

    private JButton startButton = new JButton("Start"); //start button
    private JButton stopButton = new JButton("Stop");
    private JComboBox speedBox = new JComboBox(new String[]{"Normal", "Fast", "Slow"});

    private PlayAudio song = new PlayAudio();

    //Default Constructor for the Ball panel.
    //Arguments: None
    //Returns: None
    BouncingBallPanel()
    {
        setLayout(new BorderLayout());  //set layout of the Big right panel

        JPanel buttonPanel = new JPanel();  //Make a panel the buttons
        buttonPanel.setLayout(new GridBagLayout()); //set button layout
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.insets = new Insets(5,2,5,2);
        c.anchor = GridBagConstraints.LINE_END;
        buttonPanel.add(startButton, c);    //add the start button

        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        buttonPanel.add(stopButton, c); //add the stop button

        c.gridx = 2;
        c.anchor = GridBagConstraints.CENTER;
        buttonPanel.add(speedBox);  //add the combo box for the speed

        //add action listeners for all the buttons
        startButton.addActionListener(this);
        stopButton.addActionListener(this);
        speedBox.addActionListener(this);

        //add the button panel to the big panel
        add(buttonPanel, NORTH);
        //add the animation panel to the big panel
        add(animation, CENTER);

        setPreferredSize(new Dimension(500, 600));  //set the size
    }

    //Implements the action preformed function
    //Arguments: action event
    //Returns: None
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startButton)    //If it is the start button
        {
            song.play();    //start the song
            animation.start();  //start the animation
        }
        else if(e.getSource() == stopButton)    //If it is the stop button
        {
            song.stop();    //stop the song
            animation.stop();   //stop the animation
        }
        else if(e.getSource() == speedBox)  //If the combo box changed
        {
            if(speedBox.getSelectedItem() == "Normal")  //Get the selected item and set the speed
            {
                animation.setSpeed(30);
            }

            if(speedBox.getSelectedItem() == "Fast")
            {
                animation.setSpeed(25);
            }

            if(speedBox.getSelectedItem() == "Slow")
            {
                animation.setSpeed(40);
            }

        }
    }
}
