import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class XMLDownloadPanel extends JPanel implements ActionListener{
    private XMLDownloadTask downloadTask;   //Make a instance of the Downloader Task

    private String typeMusic = "new-music"; //Variables for the selected menu items, set the defaults
    private int numDisplay = 10;
    private boolean allowExplicit = false;

    private JButton searchButton = new JButton("Download"); //The Big download button
    private JTable displayTable = new JTable(); //Table to show the results
    DefaultTableModel dtm = new DefaultTableModel(0,0); //Table model

    private JLabel timeText = new JLabel("Elapsed Time:");  //Text label for Elapsed Time
    public JLabel timerLabel = new JLabel();    //area to put the time counter

    private int timeMin = 0;    //int for the timer min
    private int timeSec = 0;    //int for the timer second

    //Default constructor for the XML DownLoad Panel
    //Arguments: None
    //Returns: None
    XMLDownloadPanel()
    {
        setLayout(new BorderLayout());

        GridBagConstraints c = new GridBagConstraints();
        searchButton.setPreferredSize(new Dimension(125, 25));  //set size
        searchButton.addActionListener(this);   //add action listener to the dl button
        JPanel buttonPanel = new JPanel();  //Make panel for the buttons and timer
        buttonPanel.setLayout(new GridBagLayout()); //set layout
        c.gridy = 1;
        c.gridx = 0;
        c.insets = new Insets(5,2,5,2);
        buttonPanel.add(searchButton, c);   //add the button
        c.gridx = 1;
        buttonPanel.add(timeText, c);   //add the elapsed time label
        c.gridx = 2;
        buttonPanel.add(timerLabel, c); //add the timer numbers
        add(buttonPanel, BorderLayout.NORTH);


        setUpTable();

    }

    //Function that sets the Type of Albums to get
    //Arguments: String withe the Type in it
    //Returns: None
    public void setType(String type)
    {
        typeMusic = type;
    }

    //Function that sets the number to display of the results
    //Arguments: int with how many
    //Returns: None
    public void setDisplay(int num)
    {
        numDisplay = num;
    }

    //Function that sets if Explicit are Allow
    //Arguments: String withe the Type in it
    //Returns: None
    public void setAllowExplicit(boolean ans)
    {
        allowExplicit = ans;
    }

    //Function that Overloads the ActionPreformed function, when called it calls download()
    //Arguments: ActionEvent
    //Returns: None
    public void actionPerformed(ActionEvent e)
    {
        download();
    }

    //Function gets the URL setup and call the swing worker class
    //Agrument: None
    //Return: None
    private void download()
    {
        dtm.setRowCount(0); //Clear the Table

        String s = "explicit";  //set default explicit String

        if(!allowExplicit)  //If allowExplicit is not true set it to non-Explicit
            s = "non-explicit";

        String URL = "https://rss.itunes.apple.com/api/v1/us/itunes-music/" + typeMusic + "/all/" + numDisplay + "/" + s + ".atom"; //Add all elements to the string

        System.out.println(URL);
        downloadTask = new XMLDownloadTask(this, URL);  //Make a new download task class

        downloadTask.execute(); //send it to the swing worker class

    }

    //Function that sets up the Table before anything is in it
    //Arguments: None
    //Return: None
    private void setUpTable()
    {
        JScrollPane scrollArea = new JScrollPane(displayTable); //Add the table to a scroll Area
        String header[] = new String[] {"Name", "Artist", "Genre", "Cover"};    //Make a string with the headers
        dtm.setColumnIdentifiers(header);   //show the headers
        displayTable.setModel(dtm); //set the table mode
        displayTable.setRowHeight(60);  //Set the row height
        displayTable.setShowVerticalLines(false);   //get rid of the vertical lines
        displayTable.getColumnModel().getColumn(3).setCellRenderer(displayTable.getDefaultRenderer(ImageIcon.class));   //Set the Cover column to render a ImageIcon
        add(scrollArea, BorderLayout.CENTER);   //Add ScrollPane to the JPanel

    }

    //Function that displays each row in the table, It also sets the column width
    //Argument: Album Object
    //Returns: None
    public void displayAlbum(Album a)
    {
        dtm.addRow(new Object[] {a.getName(), a.getArtistName(), a.getGenre(), a.getCover()});  //Add the Album info into the table

        float[] columnWidthPercentage = {50.0f, 30.0f, 10.0f, 10.0f};   //array for percentages of column width

        int tW = displayTable.getWidth();
        TableColumn column;
        TableColumnModel jTableColumnModel = displayTable.getColumnModel(); //get the table width
        int count = jTableColumnModel.getColumnCount();  //get total column counts
        for (int i = 0; i < count; i++) {    //Step through all the columns
            column = jTableColumnModel.getColumn(i);    //get the current column
            int pWidth = Math.round(columnWidthPercentage[i] * tW); //calculate the width
            column.setPreferredWidth(pWidth);   //set the width
        }
    }

    //Function that updates the on screen timer
    //Arguments: None
    //Returns: None
    public void setTime()
    {
        if (timeSec == 61)  //if the second is more then 60
        {
            timeMin++;  //add 1 to the min
            timeSec = timeSec - 60; //subtract 60 from the seconds
        }

        timeSec++;  //add 1 to the seconds
        timerLabel.setText(String.valueOf(timeMin) + ":" + String.format("%02d", timeSec)); //update the label
    }
}
