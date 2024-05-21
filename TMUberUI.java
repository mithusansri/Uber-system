import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.File;  // Import the File class
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.*;
import java.io.IOException;


// Simulation of a Simple Command-line based Uber App 

// This system supports "ride sharing" service and a delivery service

public class TMUberUI
{
  public static void main(String[] args)
  {
    // Create the System Manager - the main system code is in here 

    TMUberSystemManager tmuber = new TMUberSystemManager();
    
    Scanner scanner = new Scanner(System.in);
    System.out.print(">");

    // Process keyboard actions
    while (scanner.hasNextLine())
    {
      String action = scanner.nextLine();

      if (action == null || action.equals("")) 
      {
        System.out.print("\n>");
        continue;
      }
      // Quit the App
      else if (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("QUIT"))
        return;
      // Print all the registered drivers
      else if (action.equalsIgnoreCase("DRIVERS"))  // List all drivers
      {
        tmuber.listAllDrivers(); 
      }
      // Print all the registered users
      else if (action.equalsIgnoreCase("USERS"))  // List all users
      {
        tmuber.listAllUsers(); 
      }
      // Print all current ride requests or delivery requests
      else if (action.equalsIgnoreCase("REQUESTS"))  // List all requests
      {
        tmuber.listAllServiceRequests(); 
      }
      // Register a new driver
      else if (action.equalsIgnoreCase("REGDRIVER")) 
      {
        String name = "";
        System.out.print("Name: ");
        if (scanner.hasNextLine())
        {
          name = scanner.nextLine();
        }
        String carModel = "";
        System.out.print("Car Model: ");
        if (scanner.hasNextLine())
        {
          carModel = scanner.nextLine();
        }
        String license = "";
        System.out.print("Car License: ");
        if (scanner.hasNextLine())
        {
          license = scanner.nextLine();
        }
        String address = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        //Try and catch any runtime exceptions errors when calling the method using the user inputted values
        try{
          tmuber.registerNewDriver(name, carModel, license, address);
          System.out.printf("Driver: %-15s Car Model: %-15s License Plate: %-10s", name, carModel, license);
        }
        catch(RuntimeException e){
          System.out.println(e.getMessage()); 
        }
      }
      // Register a new user
      else if (action.equalsIgnoreCase("REGUSER")) 
      {
        String name = "";
        System.out.print("Name: ");
        if (scanner.hasNextLine())
        {
          name = scanner.nextLine();
        }
        String address = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        double wallet = 0.0;
        System.out.print("Wallet: ");
        if (scanner.hasNextDouble())
        {
          wallet = scanner.nextDouble();
          scanner.nextLine(); // consume nl
        }
        //Try and catch any runtime exceptions errors when calling the method using the user inputted values
        try{
          tmuber.registerNewUser(name, address, wallet);
          System.out.printf("User: %-15s Address: %-15s Wallet: %2.2f", name, address, wallet);
        }
        catch(RuntimeException e){
          System.out.println(e.getMessage()); 
        }
      }
      // Request a ride
      else if (action.equalsIgnoreCase("REQRIDE")) 
      {
        String account = "";
        System.out.print("User Account Id: ");
        if (scanner.hasNextLine())
        {
          account = scanner.nextLine();
        }
        String from = "";
        System.out.print("From Address: ");
        if (scanner.hasNextLine())
        {
          from = scanner.nextLine();
        }
        String to = "";
        System.out.print("To Address: ");
        if (scanner.hasNextLine())
        {
          to = scanner.nextLine();
        }
        //Try and catch any runtime exceptions errors when calling the method using the user inputted values
        try{
          tmuber.requestRide(account, from, to);
          System.out.printf("\nRIDE for: %-15s From: %-15s To: %-15s", tmuber.getUser(account).getName(), from, to);
        }
        catch(RuntimeException e){
          System.out.print(e.getMessage());
          System.out.println();
        }
        
      }
      // Request a food delivery
      else if (action.equalsIgnoreCase("REQDLVY")) 
      {
        String account = "";
        System.out.print("User Account Id: ");
        if (scanner.hasNextLine())
        {
          account = scanner.nextLine();
        }
        String from = "";
        System.out.print("From Address: ");
        if (scanner.hasNextLine())
        {
          from = scanner.nextLine();
        }
        String to = "";
        System.out.print("To Address: ");
        if (scanner.hasNextLine())
        {
          to = scanner.nextLine();
        }
        String restaurant = "";
        System.out.print("Restaurant: ");
        if (scanner.hasNextLine())
        {
          restaurant = scanner.nextLine();
        }
        String foodOrder = "";
        System.out.print("Food Order #: ");
        if (scanner.hasNextLine())
        {
          foodOrder = scanner.nextLine();
        }
        //Try and catch any runtime exceptions errors when calling the method using the user inputted values
        try
        {
          tmuber.requestDelivery(account, from, to, restaurant, foodOrder);
          User user = tmuber.getUser(account);
          System.out.printf("\nDELIVERY for: %-15s From: %-15s To: %-15s", user.getName(), from, to);  
        }
        
        catch(RuntimeException e){
          System.out.print(e.getMessage());
          System.out.println();
        }
        
      }
      //Sort users by name
      else if (action.equalsIgnoreCase("SORTBYNAME")) 
      {
        tmuber.sortByUserName();
      }
      // Sort users by number of ride they have had
      else if (action.equalsIgnoreCase("SORTBYWALLET")) 
      {
        tmuber.sortByWallet();
      }

      //Cancel a current service (ride or delivery) request
      else if (action.equalsIgnoreCase("CANCELREQ")) 
      {
        int request = -1;
        int zone = -1;
        System.out.print("Zone #: ");
        if (scanner.hasNextInt())
        {
          zone = scanner.nextInt();
          scanner.nextLine(); // consume nl character
        }
        System.out.print("Request #: ");
        if (scanner.hasNextInt())
        {
          request = scanner.nextInt();
          scanner.nextLine(); // consume nl character
        }
        //Try and catch any runtime exceptions errors when calling the method using the user inputted values
        try{
          tmuber.cancelServiceRequest(zone, request);
          System.out.println("Service request #" + request + " at Zone " + zone + " cancelled");
        }
        catch(RuntimeException e){
          System.out.println(e.getMessage());
          System.out.println();
        } 
      }
      // Drop-off the user or the food delivery to the destination address
      else if (action.equalsIgnoreCase("DROPOFF")) 
      {
        String id = "";
        System.out.print("Driver Id: ");
        if (scanner.hasNextLine())
        {
          id = scanner.nextLine();
        }
        //Try and catch any runtime exceptions errors when calling the method using the user inputted values
        try{
          tmuber.dropOff(id);
          System.out.println("Driver " + id + " Dropping off");
        }
        catch(RuntimeException e){
          System.out.println(e.getMessage());  
        }
      }
      // Get the Current Total Revenues
      else if (action.equalsIgnoreCase("REVENUES")) 
      {
        System.out.println("Total Revenue: " + tmuber.totalRevenue);
      }
      // Unit Test of Valid City Address 
      else if (action.equalsIgnoreCase("ADDR")) 
      {
        String address = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        System.out.print(address);
        if (CityMap.validAddress(address))
          System.out.println("\nValid Address"); 
        else
          System.out.println("\nBad Address"); 
      }
      // Unit Test of CityMap Distance Method
      else if (action.equalsIgnoreCase("DIST")) 
      {
        String from = "";
        System.out.print("From: ");
        if (scanner.hasNextLine())
        {
          from = scanner.nextLine();
        }
        String to = "";
        System.out.print("To: ");
        if (scanner.hasNextLine())
        {
          to = scanner.nextLine();
        }
        System.out.print("\nFrom: " + from + " To: " + to);
        System.out.println("\nDistance: " + CityMap.getDistance(from, to) + " City Blocks");
      }
      else if (action.equalsIgnoreCase("LOADUSERS")){
        String file = "";
        System.out.print("User File: ");
        if (scanner.hasNextLine()){
          //Try and catch any runtime exceptions errors when calling the method using the user inputted values as well as catching any io exceptions to end the program
          try{
            file = scanner.nextLine();
            tmuber.setUsers(TMUberRegistered.loadPreregisteredUsers(file));
            System.out.print("Users Loaded");
            System.out.println();

          }
          catch(FileNotFoundException e){
            System.out.println("Users File: " + file + " Not Found");
          }
          catch(IOException e){
            System.exit(0);
          }
        }
      }

      else if(action.equalsIgnoreCase("LOADDRIVERS")){
        String file = "";
        System.out.print("Driver File: ");
        if(scanner.hasNextLine()){
          //Try and catch any runtime exceptions errors when calling the method using the user inputted values as well as catching any io exceptions to end the program
          try{
            file = scanner.nextLine();
            ArrayList<Driver> drive = new ArrayList<>();
            drive = TMUberRegistered.loadPreregisteredDrivers(file);
            tmuber.setDrivers(drive);
            System.out.print("Drivers Loaded");
            System.out.println();
          }
          catch(FileNotFoundException e){
            System.out.println("Drivers File: " + file + " Not Found");
          }
          catch(IOException e){
            System.exit(0);
          }
        }
      }

      else if(action.equalsIgnoreCase("PICKUP")){
        String id = "";
        System.out.print("Driver  Id: ");
        id = scanner.nextLine();
        //Try and catch any runtime exceptions errors when calling the method using the user inputted values
        try{
          tmuber.pickup(id);
          System.out.println();
          System.out.print("Driver " + id + " Picking Up in Zone " + tmuber.getDriver(id).getZone());
          System.out.println();
        }
        catch(RuntimeException e){
          System.out.println();
          System.out.print(" "+ e.getMessage());
          System.out.println();
        }
      }
      else if(action.equalsIgnoreCase("DRIVETO")){
        System.out.print("Driver Id: ");
        String id = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        //Try and catch any runtime exceptions errors when calling the method using the user inputted values
        try{
          tmuber.driveTo(id, address);
          System.out.println("Driver " + id + " Now in Zone " + tmuber.getDriver(id).getZone());
        }
        catch(RuntimeException e){
          System.out.print(e.getMessage());
        }
      }
      System.out.print("\n>");
    }
  }
}

