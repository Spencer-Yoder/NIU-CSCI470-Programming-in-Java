import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

//////////////////////////////////////////////////////////
//  Project: Assignment 5                               //
//  Created: Spencer Yoder                              //
//          Z1814808                                    //
//  Class:  CSCI 470-01                                 //
//  Prof:   McMahon                                     //
//////////////////////////////////////////////////////////
public class CustomerServer extends Thread {
    private ServerSocket listenSocket;

    public static void main(String args[]) {
        new CustomerServer();
    }

    //Function that sets up the server and starts it on a new thread
    private CustomerServer() {
        // Replace 97xx with your port number
        int port = 9757;    //set the port
        try {
            listenSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Exception creating server socket: " + e);
            System.exit(1);
        }

        System.out.println("LOG: Server listening on port " + port);
        this.start();   //Make the rest of the server run on a different thread
    }

    /**
     * run()
     * The body of the server thread. Loops forever, listening for and
     * accepting connections from clients. For each connection, create a
     * new Conversation object to handle the communication through the
     * new Socket.
     */

    public void run() {
        try {
            while (true) {
                Socket clientSocket = listenSocket.accept();

                System.out.println("LOG: Client connected");

                // Create a Conversation object to handle this client and pass
                // it the Socket to use.  If needed, we could save the Conversation
                // object reference in an ArrayList. In this way we could later iterate
                // through this list looking for "dead" connections and reclaim
                // any resources.
                new Conversation(clientSocket); //Make a new connection
            }
        } catch (IOException e) {
            System.err.println("Exception listening for connections: " + e);
        }
    }
}

/**
 * The Conversation class handles all communication with a client.
 */
class Conversation extends Thread {

    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    // Where JavaCustXX is your database name
    private static final String URL = "jdbc:mysql://courses:3306/JavaCust57";

    private Statement getAllStatement = null;   //Area for the SQL statements
    private PreparedStatement addStatement = null;
    private PreparedStatement deleteStatement = null;
    private PreparedStatement updateStatement = null;

    /**
     * Constructor
     *
     * Initialize the streams and start the thread.
     */
    Conversation(Socket socket) {
        clientSocket = socket;

        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("LOG: Streams opened");
        } catch (IOException e) {
            try {
                clientSocket.close();
            } catch (IOException e2) {
                System.err.println("Exception closing client socket: " + e2);
            }

            System.err.println("Exception getting socket streams: " + e);
            return;
        }

        try {
            System.out.println("LOG: Trying to create database connection");
            Connection connection = DriverManager.getConnection(URL);

            // Create your Statements and PreparedStatements here
            getAllStatement = connection.createStatement(); //Create a select statement
            getAllStatement.addBatch("SELECT * FROM customer");
            addStatement = connection.prepareStatement("INSERT INTO customer VALUES (?, ?, ?, ?)"); //SQL for insert
            deleteStatement = connection.prepareStatement("DELETE FROM customer WHERE ssn = ?");    //SQL for delete
            updateStatement = connection.prepareStatement("UPDATE customer SET address = ?, zipCode = ? WHERE ssn = ?");    //SQL for UPdate

            System.out.println("LOG: Connected to database");

        } catch (SQLException e) {
            System.err.println("Exception connecting to database manager: " + e);
            return;
        }

        // Start the run loop.
        System.out.println("LOG: Connection achieved, starting run loop");
        this.start();
    }

    /**
     * run()
     *
     * Reads and processes input from the client until the client disconnects.
     */
    public void run() {
        System.out.println("LOG: Thread running");

        try {
            while (true) {
                // Read and process input from the client.
                MessageObject mesg = (MessageObject) in.readObject();

                //Check the object and get the action from with in
                if (mesg.getType().equals("ADD"))   //If add, call handel add
                    handleAdd(mesg);
                else if (mesg.getType().equals("GETALL"))   //if it is to get all elements
                    handleGetAll();
                else if(mesg.getType().equals("DELETE"))    //check for delete
                    handleDelete(mesg);
                else if(mesg.getType().equals("UPDATE"))    //Check for update
                    handleUpdate(mesg);

            }
        } catch (IOException e) {
            System.err.println("IOException: " + e);
            System.out.println("LOG: Client disconnected");
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException: " + e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Exception closing client socket: " + e);
            }
        }
    }

    //Function that handles the request for all records in the database
    //Arguments: None
    //Returns: None
    private void handleGetAll() {
        ArrayList<MessageObject> queryList = new ArrayList<>(); //Make a empty array List
        int i = 0;  //Counter for the total return number

        try {
            ResultSet resultSet = getAllStatement.executeQuery("SELECT * FROM customer");   //execute the select query

            while (resultSet.next()){   //Loop through the returned elements
                queryList.add(new MessageObject(resultSet.getString("name"),    //add them into the array list
                        resultSet.getString("ssn"),
                        resultSet.getString("address"),
                        resultSet.getString("zipCode"), "RETURN"));
                i++;    //add 1 to the counter
            }

            if(queryList.size() != 0)   //If the is a record returned set the return status to the number of records returned
                queryList.get(0).setStatus(i + " Records Returned");

            else {  //If no records
                queryList.add(new MessageObject(null)); //Make a new NULL object
                queryList.get(0).setStatus("0 Records Returned");   //set that there was 0 returned
            }

            out.writeObject(queryList); //send it back to the client
            out.flush();
            out.reset();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Function that add 1 record into the database
    private void handleAdd(MessageObject clientMsg) {
        int returnCode = 0; //temp var that saves what the SQL statement returned

        try {
            addStatement.setString(1, clientMsg.getName()); //Fill in the blanks in the SQL statement
            addStatement.setString(2, clientMsg.getSsn());
            addStatement.setString(3, clientMsg.getAddress());
            addStatement.setString(4, clientMsg.getZipCode());
            returnCode =  addStatement.executeUpdate(); //Run the SQL command
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (returnCode >= 1)    //Check to see it was added to the data base
                clientMsg.setStatus("1 record add to the Database");
            else    //Else there was an error
                clientMsg.setStatus("Unable to add record into Database");

            out.writeObject(clientMsg); //Send the result back to the client
            out.flush();
            out.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Function that delete elements from the database
    private void handleDelete(MessageObject clientMsg) {
        int returnCode = 0; //Return code from the sql command

        try {
            deleteStatement.setString(1, clientMsg.getSsn());   //Fill in the ssn in the sql statement
            returnCode = deleteStatement.executeUpdate();   //run the SQL command
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (returnCode >= 1)    //If it worked
                clientMsg.setStatus("1 record Deleted");    //set the status
            else
                clientMsg.setStatus("Unable to Delete the record"); //else set status to bad

            out.writeObject(clientMsg); //send it back
            out.flush();
            out.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Function that manages the update
    private void handleUpdate(MessageObject clientMsg) {
        int returnCode = 0; //Return code from the command

        try {
            updateStatement.setString(1, clientMsg.getAddress());   //Fill in the blanks in the SQL command
            updateStatement.setString(2, clientMsg.getZipCode());
            updateStatement.setString(3, clientMsg.getSsn());
            returnCode = updateStatement.executeUpdate();   //Run the command
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (returnCode >= 1)    //If it updated
                clientMsg.setStatus("1 record Updated");//set the status to good
            else
                clientMsg.setStatus("Unable to Update the record"); //Set the status to bad

            out.writeObject(clientMsg); //send it back
            out.flush();
            out.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}