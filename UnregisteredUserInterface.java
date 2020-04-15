import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class UnregisteredUserInterface {
	private static void registerNewUser(Connection connection) {
		System.out.println("\nAttempting to create a new user:");
		Scanner keyboard = new Scanner(System.in);
		
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		int userId = 0;
		
		String localQueryString = "SELECT user_id FROM regular_user ORDER BY length(user_id), user_id ASC";
		
		//Check for case of 0 registered users
		try {
			 ps = connection.prepareStatement(localQueryString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		 }
		catch (Exception e) {
			 System.out.println("Get UserID prepare statement failure.");
			 e.printStackTrace();
		}
		
		try {
			 resultSet = ps.executeQuery();
			 if (resultSet.last() == true) {
				 userId = resultSet.getInt("user_id");
				 userId++;
				 System.out.printf("\nYour new userId is %d", userId);
			 }
			 else {
				 userId = 1;
			 }

		}
		catch (Exception e) {
			 System.out.println("Login Credentials Execute Query statement failure.");
			 e.printStackTrace();
		}
		 
		String username = UserLogin.insertUserName(connection, keyboard, ps, resultSet);
		String password = UserLogin.insertPassword(connection, keyboard, ps, resultSet);
		 
		boolean checkEmptyString = false;
		String firstName = "";
		String lastName = "";
		String email = "";
		String cardName = "";
		String cardNumber = "";
		String cardExpDate = "";
		String cardCVCNum = "";
		String streetAddress = "";
		String zipCode = "";
		String city = "";
		String country = "";
		
		System.out.println("\nPlease enter your First Name:");
		while (checkEmptyString == false) {
			firstName = keyboard.nextLine();
			
			if (firstName.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else {
				checkEmptyString = true;
			}
		}
		
		checkEmptyString = false;

		System.out.println("\nPlease enter your Last Name:");
		while (checkEmptyString == false) {
			lastName = keyboard.nextLine();
			
			if (lastName.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else {
				checkEmptyString = true;
			}
		}
		
		checkEmptyString = false;
		
		System.out.println("\nPlease enter your Email Address:");
		while (checkEmptyString == false) {
			email = keyboard.nextLine();
			
			if (email.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else {
				checkEmptyString = true;
			}
		}

		checkEmptyString = false;
		
		System.out.println("\nPlease enter your Billing Card Name:");
		while (checkEmptyString == false) {
			cardName = keyboard.nextLine();
			
			if (cardName.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else {
				checkEmptyString = true;
			}
		}
		
		checkEmptyString = false;
		
		System.out.println("\nPlease enter your Billing Card Number:");
		while (checkEmptyString == false) {
			cardNumber = keyboard.nextLine();
			
			if (cardNumber.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else {
				checkEmptyString = true;
			}
		}
		
		checkEmptyString = false;
		Date expDate = null;
		java.sql.Date sqlExpDate = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		System.out.println("\nPlease enter your Billing Card Expiry Date (yyyy-mm-dd):");
		while (checkEmptyString == false) {
			cardExpDate = keyboard.nextLine();
			
			if (cardExpDate.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else {
				try {
			        expDate = format.parse(cardExpDate);
			        sqlExpDate = new java.sql.Date(expDate.getTime());
			        checkEmptyString = true;
				} catch (ParseException e1) {
					System.out.println("\nInvalid format. Please ensure that your answer follows the specified input");
				}  
			}
		}

		checkEmptyString = false;
		
		System.out.println("\nPlease enter your Billing Card CVV or CVC number:");
		while (checkEmptyString == false) {
			cardCVCNum = keyboard.nextLine();
			
			if (cardCVCNum.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else if (cardCVCNum.length() > 4) {
				System.out.println("\nInput is larger than 4 characters. Please ensure that you inputted a valid answer.");
			}
			else {
				checkEmptyString = true;
			}
		}
		
		checkEmptyString = false;
		
		System.out.println("\nPlease enter your Delivery Street Address:");
		while (checkEmptyString == false) {
			streetAddress = keyboard.nextLine();
			
			if (streetAddress.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else {
				checkEmptyString = true;
			}
		}
		
		checkEmptyString = false;
		
		System.out.println("\nPlease enter your Delivery Zip Code:");
		while (checkEmptyString == false) {
			zipCode = keyboard.nextLine();
			
			if (zipCode.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else {
				checkEmptyString = true;
			}
		}
		
		checkEmptyString = false;
		
		System.out.println("\nPlease enter your Delivery City:");
		while (checkEmptyString == false) {
			city = keyboard.nextLine();
			
			if (city.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else {
				checkEmptyString = true;
			}
		}
		
		checkEmptyString = false;
		
		System.out.println("\nPlease enter your Delivery Country:");
		while (checkEmptyString == false) {
			country = keyboard.nextLine();
			
			if (country.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else {
				checkEmptyString = true;
			}
		}
		
		localQueryString = "insert into regular_user (User_ID, First_Name, Last_Name, Email_Address, Username, Password, "
				+ "Billing_Card_Name, Billing_Card_Number, Billing_Card_Expiry_Date, Billing_Card_CVV_CVC, "
				+ "Delivery_Street_Address, Delivery_Zip_Code, Delivery_City, Delivery_Country)"
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		try {
			 ps = connection.prepareStatement(localQueryString);
			 String strUserId = Integer.toString(userId);
			 ps.setString(1, strUserId);
			 ps.setString(2,  firstName);
			 ps.setString(3,  lastName);
			 ps.setString(4,  email);
			 ps.setString(5,  username);
			 ps.setString(6,  password);
			 ps.setString(7,  cardName);
			 ps.setString(8,  cardNumber);
			 ps.setDate(9,  sqlExpDate);
			 ps.setString(10,  cardCVCNum);
			 ps.setString(11,  streetAddress);
			 ps.setString(12,  zipCode);
			 ps.setString(13,  city);
			 ps.setString(14,  country);
			 
		 }
		 catch (Exception e) {
			 System.out.println("Insert Regular User prepare statement failure.");
			 e.printStackTrace();
		 }
		
		try {
			 ps.executeUpdate();

		 }
		 catch (Exception e) {
			 System.out.println("Insert Regular User Execute Query statement failure.");
			 e.printStackTrace();
		 }
		
		 System.out.println("\nSuccessfully created new user.\n");
		
	}

	
	public static void noUserInterface(Connection connection) {
		
		boolean checkCondition = false;
		System.out.println("\n\nYou have logged in as a unregistered user.");
		System.out.println("Here are the following options avilable to you: ");
		
		String currUserId = "";
		
		Scanner keyboard = new Scanner(System.in);
		
		while (checkCondition == false) {

			System.out.println("(1) Search for Book (2) Login as Existing User (3) Register New User (4) Exit");
			String menuOption = keyboard.nextLine();
			
			switch (menuOption) {
				case ("1"):
					BookCollectionSearch.searchForBook(connection);
					break;
				case ("2"):
				 	//Will need to double check this later
					currUserId = UserLogin.userLogin(connection);
					if (currUserId != "") {
						RegisteredUserInterface.userInterface(connection, currUserId);
					}
					break;
				case ("3"):
					registerNewUser(connection);
					break;
				case ("4"):
					System.out.println("\nExiting menu of options. Returning to login menu...");
					checkCondition = true;
					break;
				default:
					System.out.println("\nInvalid input. Please enter a valid login option.");
				}
		}

	}

}
