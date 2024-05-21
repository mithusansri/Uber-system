import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.HashMap;
import java.util.Iterator;


/*
 * 
 * This class contains the main logic of the system.
 * 
 *  It keeps track of all users, drivers and service requests (RIDE or DELIVERY)
 * 
 */
public class TMUberSystemManager
{
  private Map<String, User> users;
  private ArrayList<Driver> drivers;
  //Have a users list to sort and print out the users in the map
  private ArrayList<User> usersTwo;
  private Queue<TMUberService> [] serviceRequests;

  public double totalRevenue; // Total revenues accumulated via rides and deliveries
  
  // Rates per city block
  private static final double DELIVERYRATE = 1.2;
  private static final double RIDERATE = 1.5;
  // Portion of a ride/delivery cost paid to the driver
  private static final double PAYRATE = 0.1;

  // These variables are used to generate user account and driver ids
  int userAccountId = 900;
  int driverId = 700;

  public TMUberSystemManager()
  {
    usersTwo = new ArrayList<>();
    users = new HashMap<>();
    drivers = new ArrayList<Driver>();
    serviceRequests = new Queue[4];
    for (int i = 0; i < 4; i++) {
      serviceRequests[i] = new LinkedList<>();
    }
  
    totalRevenue = 0;
  }

  // General string variable used to store an error message when something is invalid 
  // (e.g. user does not exist, invalid address etc.)  
  // The methods below will set this errMsg string and then return false
  
  // Generate a new user account id
  private String generateUserAccountId()
  {
    return "" + userAccountId + users.size();
  }
  
  // Generate a new driver id
  private String generateDriverId()
  {
    return "" + driverId + drivers.size();
  }

  // Given user account id, find user in map of users
  public User getUser(String accountId)
  {
    for (String key: users.keySet())
    {
      if (key.equals(accountId)){
        return users.get(key);
      }
    }
    return null;
  }

  //Given driver id, find driver in list of drivers
  public Driver getDriver(String driverId){
    for(int i = 0; i < drivers.size(); i++){
      if (drivers.get(i).getId().equals(driverId)){
        return drivers.get(i);
      }
    }
    return null;
  }
  
  // Check for duplicate user
  private void userExists(User user)
  {
    // Simple way
    // return users.contains(user);
    for (User newUser: users.values())
    {
      if (newUser.equals(user) == true){
        throw new UserExistsException();
      }
    }

  }
  
 // Check for duplicate driver
 private void driverExists(Driver driver) 
 {
   // simple way
   // return drivers.contains(driver);
   for (int i = 0; i < drivers.size(); i++)
   {
     if (drivers.get(i).equals(driver))
       throw new DriverExistsException();
   }
 }
  
 
 // Given a user, check if user ride/delivery request already exists in service requests
 private void existingRequest(TMUberService req)
 {
   // Simple way
   // return serviceRequests.contains(req);
   
   for (int i = 0; i < serviceRequests.length; i++)
   {
     if (serviceRequests[i].contains(req) == true)
       throw  new AdditionalRequestException();
   }
 } 
 
  
  // Calculate the cost of a ride or of a delivery based on distance 
  private double getDeliveryCost(int distance)
  {
    return distance * DELIVERYRATE;
  }

  private double getRideCost(int distance)
  {
    return distance * RIDERATE;
  }

  // Go through all drivers and see if one is available
  // Choose the first available driver
  private Driver getAvailableDriver()
  {
    for (int i = 0; i < drivers.size(); i++)
    {
      Driver driver = drivers.get(i);
      if (driver.getStatus() == Driver.Status.AVAILABLE)
        return driver;
    }
    return null;
  }
  // Print Information (printInfo()) about all registered users in the system
  public void listAllUsers()
  {
    System.out.println();
    int index = 0;
    for (User user: usersTwo)
    {
      index = index + 1;
      System.out.printf("%-2s. ", index);
      user.printInfo();
      System.out.println(); 
    }
  }

  // Print Information (printInfo()) about all registered drivers in the system
  public void listAllDrivers()
  {
    System.out.println();
    
    for (int i = 0; i < drivers.size(); i++)
    {
      int index = i + 1;
      System.out.printf("%-2s. ", index);
      drivers.get(i).printInfo();
      System.out.println(); 
      System.out.println();
    }
  }

  // Print Information (printInfo()) about all current service requests
  public void listAllServiceRequests()
  {
    int position = 0;
    //Loop through the number zones
    for(int i = 0; i < 4; i++){
      System.out.println("\nZONE " + position);
      System.out.println("======");
      int num = 0;
      //loop through each request in zone
      for (TMUberService service: serviceRequests[i]){
        num++;
        System.out.println();
        System.out.print(num + ". ");
        for(int k = 0; k < 60; k++){
          System.out.print("-");
        }
        service.printInfo();
        System.out.println();
      }
      position++;
    }
  }

  // Add a new user to the system
  public void registerNewUser(String name, String address, double wallet)
  {
    // Check to ensure name is valid
    if (name == null || name.equals(""))
    {
      throw new InvalidUserException(name);
    }
    // Check to ensure address is valid
    if (!CityMap.validAddress(address))
    {
      throw new InvalidAddressException(address);
    }
    // Check to ensure wallet amount is valid
    if (wallet < 0)
    {
      throw new InvalidFundsException();
    }
    // Check for duplicate user
    User user = new User(generateUserAccountId(), name, address, wallet);
    try
    {
      userExists(user);
    }
    catch(UserExistsException e){
      throw new UserExistsException();
    }
    users.put(user.getAccountId(),user);   
    usersTwo.add(user); 
  }

  // Add a new driver to the system
  public void registerNewDriver(String name, String carModel, String carLicencePlate, String address)
  {
    // Check to ensure name is valid
    if (name == null || name.equals(""))
    {
      throw new InvalidDriverException(name);
    }
    // Check to ensure car models is valid
    if (carModel == null || carModel.equals(""))
    {
      throw new InvalidCarModelException(carModel);
      
    }
    // Check to ensure car licence plate is valid
    // i.e. not null or empty string
    if (carLicencePlate == null || carLicencePlate.equals(""))
    {
      throw new InvalidCarLicenseException(carLicencePlate);
      
    }
    //Check to ensure address is valid
    if (!CityMap.validAddress(address))
    {
      throw new InvalidAddressException(address);
    }
    Driver driver = new Driver(generateDriverId(), name, carModel, carLicencePlate, address);
    
    // Check for duplicate driver. If not a duplicate, add the driver to the drivers list
    try{
      driverExists(driver);
    }
    catch(DriverExistsException e){
      throw new DriverExistsException();
    }
    drivers.add(driver);   
  }

  // Request a ride. User wallet will be reduced when drop off happens
  public void requestRide(String accountId, String from, String to)
  {
    // Check valid user account
    User user = getUser(accountId);
    //Check if user is in the map
    if (user == null)
    {
      throw new UserNotFoundException(accountId);
    }
    // Check for a valid from and to address
    if (!CityMap.validAddress(from))
    {
      throw new InvalidAddressExceptionFrom(from);
    }
    if (!CityMap.validAddress(to))
    {
      throw new InvalidAddressExceptionTo(to);
    }
    // Get the distance for this ride
    int distance = CityMap.getDistance(from, to);         // city blocks
    // Distance == 0 or == 1 is not accepted - walk!
    if (!(distance > 1))
    {
      throw new InsufficientDistanceException();
    }
    // Check if user has enough money in wallet for this trip
    double cost = getRideCost(distance);
    if (user.getWallet() < cost)
    {
      throw new InsufficientFundsException();
    }
    // Get an available driver
    Driver driver = getAvailableDriver();
    if (driver == null) 
    {
      throw new NoDriversException();
    }
    // Create the request
    TMUberRide req = new TMUberRide(from, to, user, distance, cost);
    
    // Check if existing ride request for this user - only one ride request per user at a time
    try
    {
      existingRequest(req);
    }
    catch(AdditionalRequestException e){
      throw new AdditionalRequestException();
    }
    serviceRequests[CityMap.getCityZone(from)].offer(req);
    user.addRide();
    
  }

  // Request a food delivery. User wallet will be reduced when drop off happens
  public void requestDelivery(String accountId, String from, String to, String restaurant, String foodOrderId)
  {
    // Check for valid user account
    User user = getUser(accountId);
    if (user == null)
    {
      throw new UserNotFoundException(accountId);
    }
    // Check for valid from and to address
    if (!CityMap.validAddress(from))
    {
      throw new InvalidAddressExceptionFrom(from);
    }
    if (!CityMap.validAddress(to))
    {
      throw new InvalidAddressExceptionTo(to);
    }
    // Get the distance to travel
    int distance = CityMap.getDistance(from, to);         // city blocks
    // Distance must be at least 1 city block
    if (!(distance > 1))
    {
      throw new InsufficientDistanceException();
    }
    // Check if user has enough money in wallet for this delivery
    double cost = getDeliveryCost(distance);
    if (user.getWallet() < cost)
    {
      throw new InsufficientFundsException();
    }
    if(restaurant.equals("")){
      throw new InvalidRestaurantException(restaurant);
    }
    if(foodOrderId.equals("")){
      throw new InvalidFoodOrderID(foodOrderId);
    }
    // Find an available driver, if any
    Driver driver = getAvailableDriver();
    if (driver == null) 
    {
      throw new NoDriversException();
    }
    TMUberDelivery delivery = new TMUberDelivery(from, to, user, distance, cost, restaurant, foodOrderId); 
    // Check if existing delivery request for this user for this restaurant and food order #
    try
    {
      existingRequest(delivery);
    }
    catch(AdditionalRequestException e){
      throw new AdditionalDeliveryRequest();
    }
    serviceRequests[CityMap.getCityZone(from)].offer(delivery);
    user.addDelivery();
  }


  //  an existing service request. 
  // parameter request is the index in the serviceRequests array list
  public void cancelServiceRequest(int zone, int request)
  {
    // Check if valid request #
    if(zone < 0 || zone > 3){
      throw new InvalidZoneException(zone);
    }
    if(serviceRequests[zone].size() == 0){
      throw new NoRequestException(zone); 
    }
    if(request < 1 || request > serviceRequests[zone].size()){
      throw new InvalidRequestException(request);
    }
    //Iterator to loop through the requests 
    Iterator it = serviceRequests[zone].iterator();
    int counter = 0;
    while(it.hasNext()){
      it.next();
      //If iterator is on the request number, remove
      if(counter == request-1){
        it.remove();
        break;
      }
      counter++;
    }
  }

  public void pickup(String driverId){
    Driver driver = getDriver(driverId);
    //Check if driver exists
    if(driver == null){
      throw new DriverNotFoundException();
    }
    int zone = driver.getZone();
    //Check if there are requests in the zone
    if(serviceRequests[zone].size()== 0){
      throw new NoRequestException(zone);
    }
    //Remove the request from queue, set driver status to driving and change address to request address and set driver's to address to service's to address
    else{
      driver.setService(serviceRequests[zone].remove());
      driver.setStatus(Driver.Status.DRIVING);
      driver.setAddress(driver.getService().getFrom());
      driver.setTo(driver.getService().getTo());
    }
  }
  
  // Drop off a ride or a delivery. This completes a service.
  // parameter request is the index in the serviceRequests array list
  public void dropOff(String driverId)
  {
    Driver driver = getDriver(driverId);
    //Check if driver exists in drivers list
    if(driver == null){
      throw new DriverNotFoundException();
    }
    //Check if the driver's status is available
    if(driver.getStatus().equals(Driver.Status.AVAILABLE)){
      throw new NotDrivingException();
    }
    TMUberService service = driver.getService();
    //Check if driver has a service to complete
    if(service == null){
      throw new NoServiceException();
    }
    if(driver.getStatus().equals(Driver.Status.DRIVING)){
      totalRevenue += service.getCost();          // add service cost to revenues
      driver.pay(service.getCost()*PAYRATE);      // pay the driver
      totalRevenue -= service.getCost()*PAYRATE;  //Subtract revenue by driver's fee
      driver.setService(null);            
      driver.setStatus(Driver.Status.AVAILABLE);  //Make driver status as availale
      driver.setAddress(driver.getTo());          //Set driver's address to the to address from their service

    }
  }

  public void driveTo(String driverId, String address){
    Driver driver = getDriver(driverId);
    //Check if driver exists in the list
    if(driver == null){
      throw new DriverNotFoundException();
    }
    //Check if address is valid
    if(CityMap.validAddress(address)== false){
      throw new InvalidAddressException(address);
    }
    //Check if driver's status is available
    if(driver.getStatus().equals(Driver.Status.AVAILABLE) == false){
      throw new UnavailableDriverException();
    }
    //Change driver's address and zone
    driver.setAddress(address);
  }




  public void setUsers(ArrayList<User> userList){
    //Loop through users list and add each element to users map
    for(int i = 0; i < userList.size(); i++){
      users.put(userList.get(i).getAccountId(), userList.get(i));
      usersTwo.add(userList.get(i));
    }
  }

  public void setDrivers(ArrayList<Driver> otherDrivers){
    //loop through other drivers list and each element to current drivers list
    for(int i = 0; i < otherDrivers.size(); i++){
      this.drivers.add(otherDrivers.get(i));
    }
  }

  //Sort users list by name
  public void sortByUserName()
  {
    Collections.sort(usersTwo, new NameComparator());
    listAllUsers();
  }


  private class NameComparator implements Comparator<User>
  {
    public int compare(User a, User b)
    {
      return a.getName().compareTo(b.getName());
    }
  }


  // Sort users list by number amount in wallet
  public void sortByWallet()
  {
    Collections.sort(usersTwo, new UserWalletComparator());
    listAllUsers();
  }


  private class UserWalletComparator implements Comparator<User>
  {
    public int compare(User a, User b)
    {
      if (a.getWallet() > b.getWallet()) return 1;
      if (a.getWallet() < b.getWallet()) return -1;
      return 0;
    }
  }

}

class UserNotFoundException extends RuntimeException{
  public UserNotFoundException(String errorMessage){
    super("User Account Not Found "+ errorMessage);
  }

}

class InvalidAddressException extends RuntimeException{
  public InvalidAddressException(String errorMessage){
    super("Invalid Address " + errorMessage);
  }

}

class InsufficientDistanceException extends RuntimeException{
  public InsufficientDistanceException(){
    super("Insufficient Travel Distance");
  }

}

class NotDrivingException extends RuntimeException{
  public NotDrivingException(){
    super("Driver is Not Driving");
  }
}

class InsufficientFundsException extends RuntimeException{
  public InsufficientFundsException(){
    super("Insufficient Funds");
  }

}

class NoDriversException extends RuntimeException{
  public NoDriversException(){
    super("No Drivers Available");
  }
}

class AdditionalRequestException extends RuntimeException{
  public AdditionalRequestException(){
    super("User Already Has Ride Request");
  }
}

class InvalidUserException extends RuntimeException{
  public InvalidUserException(String errorMessage){
    super("Invalid User Name " );
  }
}

class UserExistsException extends RuntimeException{
  public UserExistsException(){
    super("User exists already");
  }
}

class InvalidDriverException extends RuntimeException{
  public InvalidDriverException(String errorMessage){
    super("Invalid Driver Name " + errorMessage);
  }

}
class InvalidCarModelException extends RuntimeException{
  public InvalidCarModelException(String errorMessage){
    super("Invalid Car Model " + errorMessage);
  }
}
class InvalidCarLicenseException extends RuntimeException{
  public InvalidCarLicenseException(String errorMessage){
    super("Invalid Car Licence Plate " + errorMessage);
  }
}

class DriverExistsException extends RuntimeException{
  public DriverExistsException(){
    super("Driver already exists");
  }
}

class InvalidRequestException extends RuntimeException{
  public InvalidRequestException(int errorMessage){
    super("Invalid Request # " + errorMessage);
  }
}

class InvalidZoneException extends RuntimeException{
  public InvalidZoneException(int errorMessage){
    super("Invalid Zone # "+ errorMessage);
  }
}

class NoRequestException extends RuntimeException{
  public NoRequestException(int errorMessage){
    super("No Service Request in Zone " + errorMessage);
  }
}
  
class DriverNotFoundException extends RuntimeException{
  public DriverNotFoundException(){
    super("Driver Not Found");
  }
}

class UnavailableDriverException extends RuntimeException{
  public UnavailableDriverException(){
    super("Driver is unavailable");
  }

}

class NoServiceException extends RuntimeException{
  public NoServiceException(){
    super("Service Not Found");
  }

}

class InvalidAddressExceptionFrom extends RuntimeException{
  public InvalidAddressExceptionFrom(String errorMessage){
    super("Invalid Address " + errorMessage);
  }
}

class InvalidAddressExceptionTo extends RuntimeException{
  public InvalidAddressExceptionTo(String errorMessage){
    super("Invalid Address " + errorMessage);
  }
}

class InvalidFundsException extends RuntimeException{
  public InvalidFundsException(){
    super("Invalid Money in Wallet");
  }
}
class AdditionalDeliveryRequest extends RuntimeException{
  public AdditionalDeliveryRequest(){
    super("User Already Has Delivery Request at Restaurant with this Food Order");
  }
}

class InvalidRestaurantException extends RuntimeException{
  public InvalidRestaurantException(String errorMessage){
    super("Invalid Restaurant " + errorMessage);
  }
}

class InvalidFoodOrderID extends RuntimeException{
  public InvalidFoodOrderID(String errorMessage){
    super("Invalid Food Order Id " + errorMessage);
  }
}