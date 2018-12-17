import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

//////////////////////////////////////////////////////////
//  Project: Assignment 1                               //
//  Created: Spencer Yoder                              //
//          Z1814808                                    //
//  Class:  CSCI 470-01                                 //
//  Prof:   McMahon                                     //
//////////////////////////////////////////////////////////
public class MilageRedemptionApp {
    public static void main(String[] args) {

        Scanner scTerminal = new Scanner(System.in);    //create a new scanner for reading the terminal input
        char choice = 'y';  //char variable for the loop to keep the program running

        while (choice != 'n')
        {
            MilesRedeemer airports = new MilesRedeemer();   //make a new instance of the of  MilesRedeemer class

            try {   //Try and open the file that was passed as an argument
                File file = new File(args[0]);
                Scanner scFile = new Scanner(file);
                airports.readDestinations(scFile);
            } catch (FileNotFoundException e) { //catch error
                System.out.println("File Not Found Exception!" + e);
            }


            String cityNames[];     //create string to store the city names
            cityNames = airports.getCityNames();

            //Print the city names from the  MilesRedeemer class function getCityNames
            System.out.println("---------------------------------------------");
            System.out.println("List of the cites you can travel to are:");
            for (String s : cityNames) {
                System.out.println(s);
            }
            System.out.println("---------------------------------------------");

            System.out.println("Please input your total accumulated miles:");   //Get the accumlated miles from the user
            int miles = scTerminal.nextInt();

            System.out.println("Please input your month of departure (1-12): ");    //Get the Departure data
            int date = scTerminal.nextInt();

            if (date < 1 || date > 12) {    //Check to see if it is a valid month format
                System.out.println("Invalid Month! Please try again.");
                System.out.println("Please input your month of departure (1-12): ");
                date = scTerminal.nextInt();
            }

            ArrayList<String> redeemChoices = new ArrayList<>();    //Create an Array to store the returned tickets and set type
            redeemChoices = airports.redeemMiles(miles, date);  //Get the tickets and type

            //Print the results and the seat types
            System.out.println("Your accumulated miles can be used to redeem the following tickets: \n");
            for (int i = 0; i < redeemChoices.size(); i++) {
                System.out.println(redeemChoices.get(i));
            }

            System.out.println(" ");
            System.out.println("Your remaining miles: " + airports.getRemainingMiles() + "\n");

            System.out.println("Do you want to continue (y/n)?");   //check to see if the user wants to run it again
            choice = scTerminal.next().charAt(0);

        }
    }
}
