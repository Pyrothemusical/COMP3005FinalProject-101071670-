import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class UserLogin {
	public static String userLogin(Connection connection) {
		Scanner keyboard = new Scanner(System.in);
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		String userId = "";
		boolean exitLoginInfo = false;
		
		while (exitLoginInfo == false) {
			System.out.println("\nPlease enter your username:");
	 		String loginUsername = keyboard.nextLine();
	 		System.out.println("Please enter your password:");
	 		String loginPasswd = keyboard.nextLine();

	 		String localQueryString = "SELECT user_id FROM regular_user"
					 + " WHERE username = ? AND password = ?";
	 		
	 		try {
				 ps = connection.prepareStatement(localQueryString);
				 ps.setString(1, loginUsername);
				 ps.setString(2, loginPasswd);
			 }
			 catch (Exception e) {
				 System.out.println("Login Credentials prepare statement failure.");
				 e.printStackTrace();
			 }
	 		
	 		 try {
				 resultSet = ps.executeQuery();
				 if (resultSet.next() == true) {
					 userId = resultSet.getString("user_id");
				 }
			 }
			 catch (Exception e) {
				 System.out.println("Login Credentials Execute Query statement failure.");
				 e.printStackTrace();
			 }
	 		 
	 		 if (userId != "") {
	 			System.out.println("You have successfully logged in.");
	 			exitLoginInfo = true;
	 		 }
	 		 else {
	 			System.out.println("Failed to login. Would you like to try to reenter your credentials (Y/N)?");
	 			boolean validResponse = false;
	 			while (validResponse == false) {
		 			String checkContinue = keyboard.nextLine();
		 			checkContinue = checkContinue.toUpperCase();
	 				switch(checkContinue) {
	 				case("Y"): 
	 					System.out.println("Very well. Starting login process over again.\n\n");
	 					validResponse = true;
	 					break;
	 				case("N"):
	 					System.out.println("Very well. Returning to main menu.\n\n");
		 				validResponse = true;
		 				exitLoginInfo = true;
		 				break;
	 				default:
		 				System.out.println("Invalid entry. Please try to reenter your response and match the given answers (Y/N).");
	 			
	 				}
	 			}
	 		 }
		}
		
 		 return userId;
	}
	
	public static String insertUserName (Connection connection, Scanner keyboard, PreparedStatement ps, ResultSet resultSet) {
		System.out.println("\nPlease enter your Username:");
		String username = "";
		boolean checkUsername = false;
		while (checkUsername == false) {
			username = keyboard.nextLine();
			String localQueryString = "SELECT username FROM regular_user WHERE username = ?";
			try {
				ps = connection.prepareStatement(localQueryString);
				ps.setString(1, username);
			 }
			 catch (Exception e) {
				 System.out.println("Check Username prepare statement failure.");
				 e.printStackTrace();
			 }
			
			try {
				 resultSet = ps.executeQuery();
				 if (resultSet.next() == true) {
					 System.out.println("This username has already been used. Please enter a different username:");
				 }
				 else {
					 checkUsername = true;
				 }

			 }
			catch (Exception e) {
				 System.out.println("Check Username Execute Query statement failure.");
				 e.printStackTrace();
			 }
		}
		
		return username;
	}
	
	public static String insertPassword(Connection connection, Scanner keyboard, PreparedStatement ps, ResultSet resultSet) {
		System.out.println("\nPlease enter your Password:");
		String password = "";
		boolean checkPassword = false;
		while (checkPassword == false) {
			password = keyboard.nextLine();
			String localQueryString = "SELECT password FROM regular_user WHERE password = ?";
			try {
				ps = connection.prepareStatement(localQueryString);
				ps.setString(1, password);
			 }
			 catch (Exception e) {
				 System.out.println("Check Password prepare statement failure.");
				 e.printStackTrace();
			 }
			
			try {
				 resultSet = ps.executeQuery();
				 if (resultSet.next() == true) {
					 System.out.println("This password has already been used. Please enter a different password:");
				 }
				 else {
					 checkPassword = true;
				 }

			 }
			catch (Exception e) {
				 System.out.println("Check Password Execute Query statement failure.");
				 e.printStackTrace();
			 }
		}
		
		return password;
	}

}
