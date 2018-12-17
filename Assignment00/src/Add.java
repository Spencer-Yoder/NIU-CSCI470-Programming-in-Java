/**
 * Program to add two numbers... note that input is
 * accepted as a String and then an attempt is made
 * to convert it to a double for calculations. Non*
 numeric input is detected by the Exception
 * mechanism and a default value is assigned to the
 * value.
 *
 * Other methods of the Scanner class can read valid
 * ints, doubles, etc. with methods such as
 * nextDouble()...
 */
import java.util.Scanner;
public class Add {
    public static void main(String[] args) {
        String amountStr;
        double num1,
                num2,
                total;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the first number: ");
        amountStr = sc.next();
        // Try to convert amount String to double for calculation
        try {
            num1 = new Double(amountStr).doubleValue();
        } catch (NumberFormatException e) {
            System.out.println("Bad numeric input; 1st num set to 100");
            num1 = 100;
        }
        System.out.println("Enter the second number: ");
        amountStr = sc.next();
        try {
            num2 = new Double(amountStr).doubleValue();
        } catch (NumberFormatException e) {
            System.out.println("Bad numeric input; 2nd num is set to 50");
            num2 = 50;
        }
        total = num1 + num2;
        System.out.println("Sum is: " + total);
    }
}