import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.*;

//////////////////////////////////////////////////////////
//  Project: Assignment 5                               //
//  Created: Spencer Yoder                              //
//          Z1814808                                    //
//  Class:  CSCI 470-01                                 //
//  Prof:   McMahon                                     //
//////////////////////////////////////////////////////////
public class CustomerClient extends JFrame implements ActionListener {

    // GUI components
    private JButton connectButton = new JButton("Connect"); //Create all the buttons
    private JButton getAllButton = new JButton("Get All");
    private JButton addButton = new JButton("Add");
    private JButton deleteButton = new JButton("Delete");
    private JButton updateButton = new JButton("Update Address");

    private JTextField nameF = new JTextField();    //TextFields for the data
    private JTextField ssnF = new JTextField();
    private JTextField addressF = new JTextField();
    private JTextField zipCodeF = new JTextField();

    private JLabel logOutPut = new JLabel();    //Label for status and errors

    private JTextArea outArea = new JTextArea(12, 70);  //Area for all output data
    private JScrollPane outPane = new JScrollPane(outArea);

    private Socket socket;  //Sockets
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private static final long serialVersionUID = 1L;

    //Function that creates the GUI on the EDT
    //Arguments: Program arguments
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            CustomerClient client = new CustomerClient();
            client.createAndShowGUI();
            client.setVisible(true);
        });
    }

    //Function that names the JFrame
    //Arguments: None
    //Return: None
    private CustomerClient() {
        super("Customer Database");
    }

    //Function creates and displays the GUI to the users eyes
    //Arguments: None
    //Return: None
    private void createAndShowGUI() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);    //set end action

        JPanel bigPanel = new JPanel(); //JPanel to set all smaller panels
        bigPanel.setLayout(new GridBagLayout());    //set layout to gridBagLayout

        connectButton.addActionListener(this);  //Add all the action listeners to the buttons
        getAllButton.addActionListener(this);
        addButton.addActionListener(this);
        deleteButton.addActionListener(this);
        updateButton.addActionListener(this);

        getAllButton.setEnabled(false); //all buttons to off when the program starts
        addButton.setEnabled(false);
        deleteButton.setEnabled(false);
        updateButton.setEnabled(false);

        //****Input Panel***************************************************************
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel nameL = new JLabel("Name:"); //Set up labels and set text
        JLabel ssnL = new JLabel("SSN:");
        JLabel addressL = new JLabel("Address:");
        JLabel zipCodeL = new JLabel("Zip Code:");

        c.gridx = 0;    //add Name label to panel
        c.gridy = 0;
        c.insets = new Insets(10,0,0,50);
        c.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(nameL, c);

        c.gridx = 1;    //add name text field to panel
        nameF.setPreferredSize( new Dimension( 200, 24 ) );
        c.anchor = GridBagConstraints.LINE_END;
        inputPanel.add(nameF, c);

        c.gridx = 2;    //add SSN label to panel
        c.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(ssnL, c);

        c.gridx = 3;    //add SSN text field
        ssnF.setPreferredSize( new Dimension( 200, 24 ) );
        c.anchor = GridBagConstraints.LINE_END;
        inputPanel.add(ssnF, c);

        c.gridx = 0;    //add address Label
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(addressL, c);

        c.gridx = 1;    //add address text field
        addressF.setPreferredSize( new Dimension( 200, 24 ) );
        c.anchor = GridBagConstraints.LINE_END;
        inputPanel.add(addressF, c);

        c.gridx = 2;    //add zip code label
        c.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(zipCodeL, c);

        c.gridx = 3;    //add zip code field to panel
        zipCodeF.setPreferredSize( new Dimension( 200, 24 ) );
        c.anchor = GridBagConstraints.LINE_END;
        inputPanel.add(zipCodeF, c);

        //****buttons***************************************************************
        JPanel buttonPanel = new JPanel();  //set up new panel with flow layout
        buttonPanel.setLayout(new FlowLayout());

        buttonPanel.add(connectButton); //add all the buttons
        buttonPanel.add(getAllButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        //*****Putting it all together********************************************

        c.gridx = 0;    //add the input panel to the large panel
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(5,0,5,0);
        bigPanel.add(inputPanel, c);

        c.gridy = 1;    //add the buttons to the big panel
        bigPanel.add(buttonPanel, c);

        c.gridy = 2;    //add the output label
        bigPanel.add(logOutPut, c);

        c.gridy = 3;       //add the output scroll pane and text area
        bigPanel.add(outPane, c);


        //*******************************************************************
        add(bigPanel);  //add it all to the frame

        setVisible(true);   //set visible
        setSize(850, 450);  //set the size

    }

    @Override
    //Function the implements the Action preformed. Take the event and call the correct action.
    //Arguments: action event
    //Return: None
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Connect")) {   //If it is the connect button
            connect();
        } else if (e.getActionCommand().equals("Disconnect")) { //or the disconnect button
            disconnect();
        } else if (e.getSource() == getAllButton) { //When the get all button is pressed
            handleGetAll();
        } else if (e.getSource() == addButton) {    //Add record button
            handleAdd();
        } else if (e.getSource() == updateButton) { //or when update it pressed
            handleUpdate();
        } else if (e.getSource() == deleteButton) { //lastly if delete it pressed
            handleDelete();
        }
    }

    //Function that connects to the server and opens the sockets
    //Arguments: None
    //Return: None
    private void connect() {
        try {
            // Replace 97xx with your port number
            socket = new Socket("hopper.cs.niu.edu", 9757); //connect to the host

            System.out.println("LOG: Socket opened");

            out = new ObjectOutputStream(socket.getOutputStream()); //Make new output and input stream
            in = new ObjectInputStream(socket.getInputStream());

            System.out.println("LOG: Streams opened");

            connectButton.setText("Disconnect");    //Set connect button to Disconnect

            getAllButton.setEnabled(true);  //turn on all other buttons
            addButton.setEnabled(true);
            deleteButton.setEnabled(true);
            updateButton.setEnabled(true);

        } catch (UnknownHostException e) {
            System.err.println("Exception resolving host name: " + e);
        } catch (IOException e) {
            System.err.println("Exception establishing socket connection: " + e);
        }
    }

    //Function safely disconnect from the server
    //Arguments: None
    //Return: None
    private void disconnect() {
        connectButton.setText("Connect");   //change the button text

        getAllButton.setEnabled(false); //turn off all other buttons
        addButton.setEnabled(false);
        deleteButton.setEnabled(false);
        updateButton.setEnabled(false);
        logOutPut.setText("");  //clear the text field
        outArea.setText("");    //clear the text field

        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Exception closing socket: " + e);
        }
    }

    //Function that handel the get all request. It send the GETALL in a object
    //  and gets a ArrayList of objects back.
    //Arguments: None
    //Return: None
    private void handleGetAll() {
        outArea.setText("");    //Clear out the text area
        MessageObject toSend = new MessageObject("GETALL"); //make a new object with type of Get all

        try {
            out.writeObject(toSend);    //try and send it
            out.flush();    //clear the output stream
            out.reset();

            ArrayList<MessageObject> receivedList = (ArrayList<MessageObject>) in.readObject(); //Get the returned ArrayList

            logOutPut.setText(receivedList.get(0).getStatus()); //get the total size that is stored in the first object

            for(int i = 0; i < receivedList.size(); i++)    //Loop through the whole thing and print it out to the text area
            {
                if (receivedList.get(i).getName() != null) {    //If the first element is not all nulls
                    outArea.append(receivedList.get(i).getName() + "; " +   //append the data into the text area
                            receivedList.get(i).getSsn() + "; " +
                            receivedList.get(i).getAddress() + "; " +
                            receivedList.get(i).getZipCode() + "\n");
                }
            }

        } catch (Exception e)
        {
            System.out.println(e);
        }
        nameF.setText("");  //Clear all the text fields
        ssnF.setText("");
        addressF.setText("");
        zipCodeF.setText("");
    }

    //Function that handles the request to add a element into the data base
    //Arguments: None
    //Return: None
    private void handleAdd() {
        try {
            if (nameF.getText().length() < 1 && nameF.getText().length() > 20)  //Check the name length, If any of the elements are wrong throw
                throw new Exception("Invalid Name");

            if (!(ssnF.getText().matches("\\d{3}[-]\\d{2}[-]\\d{4}")))  //Check the SSN that it is the correct format
                throw new Exception("Invalid SSN");

            if (addressF.getText().length() < 1 && addressF.getText().length() > 40)    //Check the address lengths
                throw new Exception("Invalid Address");

            if (zipCodeF.getText().length() != 5 || !(zipCodeF.getText().matches("\\d{5}")))    //check the zip code length, and it is all numbers
                throw new Exception("Invalid ZipCode");

            MessageObject toSend = new MessageObject(nameF.getText(), ssnF.getText(), addressF.getText(), zipCodeF.getText(), "ADD");   //make a new object with data from text fields

            try {
                out.writeObject(toSend);    //send it
                out.flush();
                out.reset();

                MessageObject received = (MessageObject) in.readObject();   //look for the returned object
                logOutPut.setText(received.getStatus());    //get the return status from the object
            } catch (Exception e) {
                System.out.println(e);
            }
        }catch(Exception e){    //If any of the Text checks we failed, print out invalid data
            logOutPut.setText("Invalid Data");
            System.out.println(e);
        }
        nameF.setText("");  //Clear out text fields
        ssnF.setText("");
        addressF.setText("");
        zipCodeF.setText("");
    }

    //Function handles the delete records, If receives a object back with the status of the delete
    //Arguments: None
    //Return: None
    private void handleDelete() {
        try {
            if (!(ssnF.getText().matches("\\d{3}[-]\\d{2}[-]\\d{4}")))  //Check the ssn that is in valid format
                throw new Exception("Invalid SSN");

            MessageObject toSend = new MessageObject(ssnF.getText(), "DELETE"); //Make a new object with the ssn

            try {
                out.writeObject(toSend);    //send it
                out.flush();    //clear output stream
                out.reset();

                MessageObject received = (MessageObject) in.readObject(); //look for the return object
                logOutPut.setText(received.getStatus());    //display the status output
            } catch (Exception e) {
                System.out.println(e);
            }
        }catch (Exception e){
            logOutPut.setText("Invalid SSN");
            System.out.println(e);
        }
        nameF.setText("");  //clear the fields
        ssnF.setText("");
        addressF.setText("");
        zipCodeF.setText("");
    }

    //Function handles the Update records, If receives a object back with the status of the update
    //Arguments: None
    //Return: None
    private void handleUpdate() {
        try {
            if (!(ssnF.getText().matches("\\d{3}[-]\\d{2}[-]\\d{4}")))  //Check the ssn format
                throw new Exception("Invalid SSN");

            if (addressF.getText().length() < 1 && addressF.getText().length() > 40)    //Check the address length
                throw new Exception("Invalid Address");

            if (zipCodeF.getText().length() != 5)   //Check the zipcode length and it is all numbers
                throw new Exception("Invalid ZipCode");

            MessageObject toSend = new MessageObject(nameF.getText(), ssnF.getText(), addressF.getText(), zipCodeF.getText(), "UPDATE");    //Make a new object

            try {
                out.writeObject(toSend);    //send it
                out.flush();
                out.reset();

                MessageObject received = (MessageObject) in.readObject();   //save returned object
                logOutPut.setText(received.getStatus());    //print the return string
            } catch (Exception e)
            {
                System.out.println(e);
            }
        }catch (Exception e){   //Catch if the data was wrong
            logOutPut.setText("Invalid Data");
            System.out.println(e);
        }
        nameF.setText("");  //clear the text fields
        ssnF.setText("");
        addressF.setText("");
        zipCodeF.setText("");
    }
}