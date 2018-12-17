import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/******************************************************************
 * Program: Assignment 04
 * Programmer: Spencer Yoder
 *             Z1814808
 *
 * Class: CSCI470
 *   Spring 2018
 *
 * Function:
 *   Java application to download XML, parse it, and display the
 *   results. The XML will contain information about albums
 *   available on the Apple iTunes Store RSS feed.
 *****************************************************************/
public class XMLDownloader extends JFrame implements ActionListener {
    private XMLDownloadPanel uiPanel = new XMLDownloadPanel();
    private BouncingBallPanel animation = new BouncingBallPanel();

    //Main Function for the Program
    //Arguments: None
    //Returns: None
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {  //Build on the EDT
            XMLDownloader ex = new XMLDownloader("iTunes Store RSS Downloader");
            ex.setVisible(true);
        });
    }

    //Default constructor for JFrame
    //Arguments: String for the title
    //Returns: None
    public XMLDownloader(String title) {
        super(title);   //set the title
        createAndShowGUI();
    }

    //Function that Creates the GUI
    //Arguments: None
    //Returns: None
    private void createAndShowGUI()
    {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);    //Set exit operation
        createMenu();   //Call function the make the menu

        JPanel contents = new JPanel();
        contents.setLayout(new BorderLayout());

        contents.add(uiPanel, BorderLayout.CENTER);
        contents.add(animation, BorderLayout.LINE_END);

        add(contents);

        setVisible(true);   //show it
        setSize(1500,600);  //set size
    }

    //Function that Makes the top Menu bar
    //Arguments: None
    //Returns: None
    private void createMenu()
    {
        JMenuBar menuBar = new JMenuBar();  //Make the menu bar

        JMenu typeMenu = new JMenu("Type"); //add Type to the bar
        typeMenu.setToolTipText("Select the Type of Results."); //Make a tool tip for it
        JMenu limitMenu = new JMenu("Limit");   //add Limit to bar
        limitMenu.setToolTipText("Select how many entries you want.");  //add tool tip text
        JMenu explicitMenu = new JMenu("Explicit"); //add section for Explicit to the bar
        explicitMenu.setToolTipText("Display Explicit Results?");   //Tool tip


        ButtonGroup typeGroup = new ButtonGroup();  //Make button groups for all the sections
        ButtonGroup limitGroup = new ButtonGroup();
        ButtonGroup explicitGroup = new ButtonGroup();

        /*
        *  Make all the buttons for the Type music menu bar
         */
        JRadioButtonMenuItem typeNewItem = new JRadioButtonMenuItem("New Music");
        typeNewItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));    //add an accelerator to the menu
        typeNewItem.setSelected(true);  //Select the first item in the list
        typeNewItem.addActionListener(this);    //Set the Action Listener
        typeGroup.add(typeNewItem); //add it to the button group
        typeMenu.add(typeNewItem);  //add it to the Type menu

        JRadioButtonMenuItem typeRecentItem = new JRadioButtonMenuItem("Recent Releases");
        typeRecentItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        typeRecentItem.addActionListener(this);
        typeGroup.add(typeRecentItem);
        typeMenu.add(typeRecentItem);

        JRadioButtonMenuItem typeTopItem = new JRadioButtonMenuItem("Top Albums");
        typeTopItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        typeTopItem.addActionListener(this);
        typeGroup.add(typeTopItem);
        typeMenu.add(typeTopItem);
        menuBar.add(typeMenu);  //add to the menu bar

        /*
        *  Make all the buttons for the Limit menu section
         */
        JRadioButtonMenuItem limit10Item = new JRadioButtonMenuItem("10");  //Make the first menu item
        limit10Item.addActionListener(this);
        limitGroup.add(limit10Item);    //add it to the group
        limitMenu.add(limit10Item);     //add it to the menu
        limit10Item.setSelected(true);  //Select the first item in the list
        JRadioButtonMenuItem limit25Item = new JRadioButtonMenuItem("25");
        limit25Item.addActionListener(this);
        limitGroup.add(limit25Item);
        limitMenu.add(limit25Item);
        JRadioButtonMenuItem limit50Item = new JRadioButtonMenuItem("50");
        limit50Item.addActionListener(this);
        limitGroup.add(limit50Item);
        limitMenu.add(limit50Item);
        JRadioButtonMenuItem limit100Item = new JRadioButtonMenuItem("100");
        limit100Item.addActionListener(this);
        limitGroup.add(limit100Item);
        limitMenu.add(limit100Item);
        menuBar.add(limitMenu); //add to the menu bar

        /*
        *   Create the Explicit menu section
        */
        JRadioButtonMenuItem explicitYesItem = new JRadioButtonMenuItem("Yes");
        explicitYesItem.addActionListener(this);
        explicitGroup.add(explicitYesItem); //add it to the group
        explicitMenu.add(explicitYesItem);  //add it to the menu
        JRadioButtonMenuItem explicitNoItem = new JRadioButtonMenuItem("No");
        explicitNoItem.addActionListener(this);
        explicitNoItem.setSelected(true);
        explicitGroup.add(explicitNoItem);
        explicitMenu.add(explicitNoItem);   //auto select the no
        menuBar.add(explicitMenu);  //add to the menu bar

        setJMenuBar(menuBar);   //show the menu bar
    }

    //Function that Overloads the ActionPreformed
    //Arguments: Action Event
    //Returns: None
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand()) { //Check Action Event to see what happen
            case "New Music":   //If New music was pressed, call set Type function
                uiPanel.setType("new-Music");
                break;
            case "Recent Releases":
                uiPanel.setType("recent-releases");
                break;
            case "Top Albums":
                uiPanel.setType("top-albums");
                break;
            case "10":
                uiPanel.setDisplay(10); //If number of results was pressed, call set Display function
                break;
            case "25":
                uiPanel.setDisplay(25);
                break;
            case "50":
                uiPanel.setDisplay(50);
                break;
            case "100":
                uiPanel.setDisplay(100);
                break;
            case "Yes":
                uiPanel.setAllowExplicit(true); //If the Explicit was pressed, Call Explicit function
                break;
            case "No":
                uiPanel.setAllowExplicit(false);
                break;
        }
    }
}
