import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

//////////////////////////////////////////////////////////
//  Project: Assignment 2                               //
//  Created: Spencer Yoder                              //
//          Z1814808                                    //
//  Class:  CSCI 470-01                                 //
//  Prof:   McMahon                                     //
//////////////////////////////////////////////////////////
public class MilageRedemptionApp extends JFrame implements ActionListener{

    private static MilesRedeemer airports = new MilesRedeemer();

    //Left Panel
    private JList cityList = new JList();
    private JTextField reqMiles_T = new JTextField(13); //text Field for required miles
    private JTextField milesUpgrade_T = new JTextField(13); //field for amount of miles to upgrade
    private JTextField superSaver_T = new JTextField(13);   //field for super saver miles amount
    private JTextField saverMonth_T = new JTextField(13);   //field for the months of super saver

    //Right panel
    private JTextField accMiles_T = new JTextField(13); //field for user to input amount of miles
    private String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};   //String of months
    private SpinnerListModel model = new SpinnerListModel(months);  //Type of spinner
    private JSpinner monthSpinner = new JSpinner(model);    //Spinner of the months selector
    private JButton redeemTickets = new JButton("Redeem tickets >>>");
    private JTextArea ticketOutput = new JTextArea();   //Text area to show the tickets that are selected
    private JTextField remainMiles_T = new JTextField(13);  //Field to show miles left


    public static void main(String[] args) {
            try {   //Try and open the file that was passed as an argument
                File file = new File(args[0]);
                Scanner scFile = new Scanner(file);
                airports.readDestinations(scFile);
            } catch (FileNotFoundException e) { //catch error
                System.out.println("File Not Found! " + e);
            } catch(Exception e){
                System.out.println("There was an Error with the File. " + e);
            }

            EventQueue.invokeLater(() -> {  //Make a task to on the EDT
                MilageRedemptionApp ex = new MilageRedemptionApp("Airline Miles Redemption");
                ex.setVisible(true);
            });
    }

    public MilageRedemptionApp(String title) {  //default constructor
        super(title);
        createAndShowGUI();
    }

    //Function that will add all the elements to the screen and display them.
    //Arguments: none
    //Returns: none
    private void createAndShowGUI() {
        GridBagConstraints c = new GridBagConstraints();    //Make layout manager constraint var

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Kill program on close
        setResizable(false);    //User can not change size
        setSize(950, 350);  //set size

        JPanel container = new JPanel();    //Make a JPanel for the right and left section to be put in
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
//********LEFT PANEL********************************************************************

        //Create destination lookup Panel
        JPanel lookupPanel = new JPanel();
        lookupPanel.setLayout(new GridBagLayout()); //set GridBag Layout
        Color bgColorL = new Color(154, 209, 178);  //pick the color
        lookupPanel.setBackground(bgColorL);    //set the color
        lookupPanel.setPreferredSize(new Dimension(300,350));   //set the size
        TitledBorder lookupBorder = new TitledBorder("List of destinations Cities");    //Make the title
        lookupBorder.setTitleJustification(TitledBorder.LEFT);  //add the title
        lookupBorder.setTitlePosition(TitledBorder.TOP);    //set the title position
        lookupPanel.setBorder(lookupBorder);    //set a border around the whole thing


        //List of Cites JList
        String cityNames[] = airports.getCityNames();   //get all the city names
        cityList.setListData(cityNames);    //add them to the JList
        cityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //You can only select one at a time
        cityList.setFixedCellWidth(300);    //set size
        cityList.setVisibleRowCount(9);
        cityList.addListSelectionListener(new ListSelectionListener() { //set the action listener

            //Function that gets the selected month from the JList and sets the text field with the correct information.
            //Arguments: Action Event
            //Returns: None
            public void valueChanged(ListSelectionEvent e) {
                try {
                    Destination info = airports.findDestination((String) cityList.getSelectedValue());  //Find the correct city airport

                    String months[] = getMonthStrings();    //get the months

                    reqMiles_T.setText(Integer.toString(info.getMilesReq()));   //set the required miles text field
                    milesUpgrade_T.setText(Integer.toString(info.getUpgrade()));    //set the miles needed to upgrade
                    superSaver_T.setText(Integer.toString(info.getSuperSaver()));   //set the required miles for super saver
                    saverMonth_T.setText(months[info.getSaverStart() - 1] + " to " + months[info.getSaverEnd() - 1]);  //set the months when super saver is
                } catch (Exception error){
                    System.out.println("There was an Error getting the city info. " + error);
                }
            }
        });

        //Display all cities in file
        JScrollPane cityScroll = new JScrollPane(cityList);
        c.gridx = 0;    //set position for element
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(3,0,3,0); //spacing between everything
        lookupPanel.add(cityScroll,c);

        //Required Miles Label
        JLabel reqMiles_L = new JLabel("Required Miles");
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridwidth = 1;
        lookupPanel.add(reqMiles_L,c);

        //Miles for upgrading Label
        JLabel milesUpgrade_L = new JLabel("Miles for Upgrading");
        c.gridx = 0;
        c.gridy = 2;
        lookupPanel.add(milesUpgrade_L,c);

        //Miles for SuperSaver Label
        JLabel superSaver_L = new JLabel("Miles for SuperSaver");
        c.gridx = 0;
        c.gridy = 3;
        lookupPanel.add(superSaver_L,c);

        //Month for SuperSaver Label
        JLabel saverMonth_L = new JLabel("Months for SuperSaver");
        c.gridx = 0;
        c.gridy = 4;
        lookupPanel.add(saverMonth_L,c);

        //TextField for Required Miles
        reqMiles_T.setEditable(false);
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_END; //Push the text areas to the far Left
        lookupPanel.add(reqMiles_T,c);

        //TextField for Needed Miles for Upgrade
        c.gridx = 1;
        c.gridy = 2;
        milesUpgrade_T.setEditable(false);
        lookupPanel.add(milesUpgrade_T,c);

        //TextField for Super saver Miles number
        c.gridx = 1;
        c.gridy = 3;
        superSaver_T.setEditable(false);
        lookupPanel.add(superSaver_T,c);

        ////TextField for Saver Months start and end
        c.gridx = 1;
        c.gridy = 4;
        saverMonth_T.setEditable(false);
        lookupPanel.add(saverMonth_T,c);

//*******RIGHT PANEL*********************************************************************************

        JPanel calcPanel = new JPanel();    //Right panel
        calcPanel.setLayout(new GridBagLayout());   //set layout
        calcPanel.setPreferredSize(new Dimension(600,500)); //set size
        Color bgColorR = new Color(166, 154, 167);  //pick a color
        calcPanel.setBackground(bgColorR);  //set color
        TitledBorder calcBorder = new TitledBorder("List of destinations Cities");  //title
        calcBorder.setTitleJustification(TitledBorder.LEFT);    //border
        calcBorder.setTitlePosition(TitledBorder.TOP);
        calcPanel.setBorder(lookupBorder);


        //Label for Accumulated Miles
        JLabel accMiles_L = new JLabel("Your Accumulated Miles ");
        accMiles_L.setHorizontalAlignment(SwingConstants.RIGHT);
        Dimension a = new Dimension(270,20);
        accMiles_L.setPreferredSize(a);
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(7,0,7,0); //spaceing between everything
        calcPanel.add(accMiles_L, c);

        //Label for Month selector
        JLabel departure_L = new JLabel("Month of Departure ");
        c.gridx = 0;
        c.gridy = 1;
        calcPanel.add(departure_L, c);

        //Label for the reaming miles left
        JLabel remainMiles_L = new JLabel("Your Reaming Miles ");
        c.gridx = 0;
        c.gridy = 4;
        calcPanel.add(remainMiles_L, c);

        //Button to redeem Tickets
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        calcPanel.add(redeemTickets, c);

        //Textarea for what ticket are available at this time
        ticketOutput.setColumns(50);
        ticketOutput.setRows(8);
        ticketOutput.setLineWrap(true);
        ticketOutput.setEditable(false);
        c.gridx =0;
        c.gridy = 3;
        calcPanel.add(ticketOutput, c);


        //Text Field for your accumulated miles
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_START;
        calcPanel.add(accMiles_T, c);

        //JSpinner for the month selector
        c.gridx = 1;
        c.gridy = 1;
        Dimension d = new Dimension(90,20); //set the size
        monthSpinner.setPreferredSize(d);
        calcPanel.add(monthSpinner, c);

        //Text Field for the miles remaining
        c.gridx = 1;
        c.gridy = 4;
        remainMiles_T.setEditable(false);
        calcPanel.add(remainMiles_T, c);


        redeemTickets.addActionListener(this::actionPerformed); //add an action lister for the button press

//****************************************************************************************************
        container.add(lookupPanel); //add right and left panel to the larges panel
        container.add(calcPanel);
        add(container); //add container to the jframe

        setVisible(true);   //show everything
    }

    public void actionPerformed(ActionEvent e) {
        ArrayList <String> tickets = new ArrayList<>();

        try {
            if (!accMiles_T.getText().equals("")) {     //Only run if all the data is correct
                for (int i = 0; i < months.length; i++) //Convert month into a number format
                {
                    if (months[i] == monthSpinner.getValue()) {
                        tickets = airports.redeemMiles(Integer.valueOf(accMiles_T.getText()), i);
                    }
                }

                ticketOutput.setText("");   //Clear the text area

                if (tickets.size() != 0)    //If there are flight with in the search criteria
                {
                    ticketOutput.append("You have accumulated miles can be used to redeem the following air tickets:\n\n"); //print the headder

                    for (int i = 0; i < tickets.size(); i++) //loop and print the returned strings
                    {
                        ticketOutput.append("* " + tickets.get(i) + "\n");
                    }
                } else    //Else there is no flight at this time
                {
                    ticketOutput.append("There are no flight available at this time with this search requirements. \n");
                }

                remainMiles_T.setText(String.valueOf(airports.getRemainingMiles()));    //set remaining miles
            }
            else    //If not all the data was correct
            {
                ticketOutput.setText("");   //Clear the text area
                ticketOutput.append("All of the search requirements were not inputted or are invalid. \n");
                System.out.println("All of the search requirements were not inputted or are invalid.");
            }
        }
        catch (NumberFormatException o)
        {
            ticketOutput.setText("");   //Clear the text area
            ticketOutput.append("All of the search requirements were not inputted or are invalid. \n");
            System.out.println("All of the search requirements were not inputted or are invalid.");
        } catch (Exception o){
            System.out.println("There was an error finding tickets form the info inputted. " + o);

        }
    }

    //Function that get the months and saves them in a string. (Taken from Assignment 2 notes)
    //Arguments: None
    //Returns: String of months
    private String[] getMonthStrings() {
        String[] months = new java.text.DateFormatSymbols().getMonths();
        int lastIndex = months.length - 1;
        if(months[lastIndex] == null || months[lastIndex].length() <= 0) {
            String[] monthStrings = new String[lastIndex];
            System.arraycopy(months, 0, monthStrings, 0, lastIndex);
            return monthStrings;
        } else {
            return months;
        }
    }
}
