import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;  // Import the File class

public class TMUberRegistered
{
    // These variables are used to generate user account and driver ids
    private static int firstUserAccountID = 900;
    private static int firstDriverId = 700;

    // Generate a new user account id
    public static String generateUserAccountId(ArrayList<User> current)
    {
        return "" + firstUserAccountID + current.size();
    }

    // Generate a new driver id
    public static String generateDriverId(ArrayList<Driver> current)
    {
        return "" + firstDriverId + current.size();
    }

    // Database of Preregistered users
    // In Assignment 2 these will be loaded from a file
    // The test scripts and test outputs included with the skeleton code use these
    // users and drivers below. You may want to work with these to test your code (i.e. check your output with the
    // sample output provided). 
    public static ArrayList<User> loadPreregisteredUsers(String fileName) throws IOException
    {
        ArrayList<User> users = new ArrayList<>();
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        //While the file still has lines, create a new user object using the each next line from the file as parameter and then add to the users arraylist
        while(scanner.hasNextLine()){
            users.add(new User(generateUserAccountId(users), scanner.nextLine(), scanner.nextLine(), Double.parseDouble(scanner.nextLine())));
        }
        scanner.close();
        return users;
    }

    // Database of Preregistered users
    // In Assignment 2 these will be loaded from a file
    public static ArrayList<Driver> loadPreregisteredDrivers(String fileName) throws IOException
    {
        ArrayList<Driver> drivers = new ArrayList<>();
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        //While the file still has lines, create a new driver object using the each next line from the file as parameter and then add to the drivers arraylist
        while(scanner.hasNextLine()){
            drivers.add(new Driver(generateDriverId(drivers), scanner.nextLine(), scanner.nextLine(), scanner.nextLine(), scanner.nextLine()));
        }
        scanner.close();
        return drivers;
    }
}

