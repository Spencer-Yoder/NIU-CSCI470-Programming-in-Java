import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.lang.String;

public class MilesRedeemer {
    private ArrayList<Destination> cityList = new ArrayList<>();    //Array list of Destinations classes
    private int milesLeft = 0; //Variable to store the left over miles

    //A Function that read a file that was passed as an argument. Uses Scanner
    //to pars the lines until it finds an ';' or and end of line marker.
    public void readDestinations(Scanner fileScanner){
        String tempCity;
        int tempMilesReq, tempUpgrade, tempSuperSaver, tempSaverStart, tempSaverEnd;

        while(fileScanner.hasNext())    //Loop through all line in the file
        {
            try {   //Try and read the line
                fileScanner.useDelimiter(";|-|\r?\n");  //Got the "\r?\n" idea from StackOverflow
                tempCity = fileScanner.next();  //Get city name
                tempMilesReq = Integer.parseInt(fileScanner.next());    //get the miles required and store as int
                tempUpgrade = Integer.parseInt(fileScanner.next());     //get miles to upgrade
                tempSuperSaver = Integer.parseInt(fileScanner.next());  //get super saver miles
                tempSaverStart = Integer.parseInt(fileScanner.next());  //get super saver start date
                tempSaverEnd = Integer.parseInt(fileScanner.next());    //get super saver end date

                cityList.add(new Destination(tempCity, tempMilesReq, tempUpgrade, tempSuperSaver, tempSaverStart, tempSaverEnd));   //create new Destination class and add it to the MilesRedeemer array list
            }
            catch (NumberFormatException e) //If there is an error converting the string to int
            {
                System.out.println("Number Format Error:" + e);
            }
            catch (NoSuchElementException e)    //error reading the string
            {
                System.out.println("No Such Element Exception:" + e);
            }
        }
    }

    //Function that returns an string array of the city names that are in the array list
    //Arguments: none
    //Returns:   temp - string of city names
    public String[] getCityNames(){
        String[] temp = new String[cityList.size()];    //Make new string array
        int counter = 0;
        for(Destination d : cityList  ) //step through the MilesRedemmer array list and call getCity() from Destination class
        {
            temp[counter] = d.getCity();    //add to the array
            counter++;
        }
        return temp;
    }

    //Function that does all the math for choosing the best flights to take and when to upgrade.
    //Arguments: miles - total miles from the user
    //           month - Month when the trip is planed for
    //Returns:   returnList - array List of strings where the user can go
    public ArrayList<String> redeemMiles(int miles, int month)
    {
        ArrayList<String> returnList = new ArrayList<>();   //Array List for returning final info
        ArrayList<Integer> tripPlan = new ArrayList<>();    //Array list to store the current best trip
        int milesPlan = 0;

        cityList.sort(new MilageComparator());  //Sort the list from Height to Low

        //Nested loops to go through the list and find the trip that will get the most amount of miles.
        for (int i = 0; i < cityList.size(); i++)
        {
            ArrayList<Integer> tempTrip = new ArrayList<>();    //Array List for soring the each iteration trip
            int milesCounter = 0;

            for (int j = 0; j < cityList.size() - i; j++)
            {
                if (month >= cityList.get(j).getSaverStart() && month <= cityList.get(j).getSaverEnd() )    //If month fall in super saver
                {
                    if (cityList.get(j).getSuperSaver() + milesCounter < miles) //If there is more miles than the trip
                    {
                        tempTrip.add(i+j);  //add the trip to the loops array List
                        milesCounter = milesCounter + cityList.get(i+j).getSuperSaver();    //add to the total number mils for the trip
                    }
                }
                else
                {
                    if (cityList.get(j).getMilesReq() + milesCounter < miles)   //If not in super saver, and there is enough miles
                    {
                        tempTrip.add(i+j);  //add to the trip loops array list
                        milesCounter = milesCounter  + cityList.get(i+j).getMilesReq(); //add to the total number mils for the trip
                    }
                }
            }

            if (milesCounter > milesPlan && tempTrip.size() > 0 && milesCounter >= 0)   //If loops total miles is the best trip so-far, save it
            {
                tripPlan = tempTrip;
                milesPlan = milesCounter;
            }
        }

        for (int i = 0; i < tripPlan.size(); i++)   //loop to check to see is any of the tickets can be upgraded to super saver
        {
            if (cityList.get(tripPlan.get(i)).getUpgrade() + milesPlan < miles) //If there is miles left
            {
                milesPlan = milesPlan + cityList.get(tripPlan.get(i)).getUpgrade(); //Change the miles total
                returnList.add("A trip to " + cityList.get(tripPlan.get(i)).getCity() + ", First Class");   //add the city name and ticket type to the return array
            }
            else    //If there is not enough miles to upgrade
            {
                returnList.add("A trip to " + cityList.get(tripPlan.get(i)).getCity() + ", economy Class"); //add the city name and ticket type to the array list
            }
        }

        milesLeft = miles - milesPlan;  //set MilesRedeemer total miles to what is left
        return returnList;
    }

    //Function that returns the miles left
    //Arguments: None
    //Returns:   Miles left
    public int getRemainingMiles(){
        return milesLeft;
    }

    //Function that returns the info of a city name
    //Arguments: String with a city name
    //Returns: Destination object of city
    public Destination findDestination(String cityName)
    {
        for (int i = 0; i < cityList.size(); i++)   //Loop through the arrayList of city Names and get info
        {
            if(cityList.get(i).getCity() == cityName)   //If city name
            {
                 return new Destination(cityName, cityList.get(i).getMilesReq(),cityList.get(i).getSuperSaver(), cityList.get(i).getUpgrade(),  cityList.get(i).getSaverStart(), cityList.get(i).getSaverEnd());   //create new Destination object and return it
            }

        }
        return new Destination("A",'A','A','A','A','A');    //return an empty Object
    }
}





