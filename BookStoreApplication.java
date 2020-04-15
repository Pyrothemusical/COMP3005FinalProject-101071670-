import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class BookStoreApplication {
	
		private static String currUserId = "";
			
		public static void main(String[] args) {
			try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:8000/FinalBookStore", "BookAdmin", "ILoveReading45")) {
				 System.out.println("Successfully connected to PostgreSQL BookStore database.\n");
				 
				 boolean checkCondition = false;
				 OwnerInterface ownerInterface = new OwnerInterface();
				 
				 Scanner keyboard = new Scanner(System.in);
						
				 System.out.println("Welcome to the BookStore. Who are you logging in as?");
				 
				 while (checkCondition == false) {
					 System.out.println("Please enter the correct option for the corresponding user:");
					 System.out.println("(1) Customer (2) Owner (3) Exit Application");
					 String userType = keyboard.nextLine();
					 switch (userType) {
					 	case ("1"):
					 		
					 		boolean checkUser = false;
					 		System.out.println("\nYou have chosen to login as a customer.");
					 		System.out.println("What kind of customer are you trying to login as?\n");
					 		
					 		while (checkUser == false) {
					 			System.out.println("Please enter the correct option for the corresponding user:");
								System.out.println("(1) Registered Customer (2) Unregistered Customer (3) Exit Menu (4) Exit Entire Application");
								String customerType = keyboard.nextLine();
								
								switch (customerType) {
									case ("1"):

										currUserId = UserLogin.userLogin(connection);
								 		if (currUserId != "") {
								 			RegisteredUserInterface.userInterface(connection, currUserId);
								 			checkUser = true;
								 		}
								 		break;
									case ("2"):
								 		System.out.println("Logging in as Unregistered User");
										UnregisteredUserInterface.noUserInterface(connection);
							 			break;
									case ("3"):
										System.out.println("Exiting Customer Menu.\n");
										checkUser = true;
										break;
									case ("4"):
										System.out.println("\nExiting BookStore application. Thank you for your visit and please come again.");
										checkUser = true;
										checkCondition = true;
										break;
									default:
										System.out.println("\nInvalid input.");
									}
					 		}
					 		
					 		break;
					 	case ("2"):
					 		System.out.println("Logging in as Owner...");
					 		currUserId = ownerInterface.ownerLogin(connection);
					 		if (currUserId != "") {
					 			ownerInterface.ownerInterface(connection);
					 		}
					 		break;
					 	case ("3"):
					 		System.out.println("\nExiting BookStore application. Thank you for your visit and please come again.");
					 		checkCondition = true;
					 		break;
					 	default:
					 		System.out.println("\nInvalid input.");
					 }
				 }

			}
			catch (SQLException e) {
			 	System.out.println("Connection failure.");
			 	e.printStackTrace();
			}
		}
}
