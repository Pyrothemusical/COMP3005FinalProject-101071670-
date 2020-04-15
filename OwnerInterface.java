import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class OwnerInterface {

	private static String ownerID = "";

	public static String ownerLogin(Connection connection) {
		Scanner keyboard = new Scanner(System.in);
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		String owner = "";
		boolean exitLoginInfo = false;
		
		while (exitLoginInfo == false) {
			System.out.println("\nPlease enter your username:");
	 		String loginUsername = keyboard.nextLine();
	 		System.out.println("Please enter your password:");
	 		String loginPasswd = keyboard.nextLine();

	 		String localQueryString = "SELECT owner_id FROM owner"
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
					 owner = resultSet.getString("owner_id");
				 }
			 }
			 catch (Exception e) {
				 System.out.println("Login Credentials Execute Query statement failure.");
				 e.printStackTrace();
			 }
	 		 
	 		 if (owner != "") {
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
		
		ownerID = owner;
 		return owner;
	}
	
	private static boolean isValidISBN(String isbn) 
    { 

		if ( isbn.length() != 13 )
        {
            System.out.println("Input is less than 13 characters (Excluding Hyphens):");
			return false;
        }
		
		try
        {
            int tot = 0;
            for ( int i = 0; i < 12; i++ )
            {
                int digit = Integer.parseInt( isbn.substring( i, i + 1 ) );
                tot += (i % 2 == 0) ? digit * 1 : digit * 3;
            }

            //checksum must be 0-9. If calculated as 10 then = 0
            int checksum = 10 - (tot % 10);
            if ( checksum == 10 )
            {
                checksum = 0;
            }

            return checksum == Integer.parseInt( isbn.substring( 12 ) );
        }
        catch ( NumberFormatException nfe )
        {
            System.out.println("Contains non numeric characters.");
        	return false;
        }
    } 
	
	public static boolean isNumeric(String str) { 
		  try {  
			  Double.parseDouble(str);  
			  return true;
		  } catch(NumberFormatException e){  
			  return false;  
		  }  
	}
	
	public static boolean isFloat(String str) { 
		  try {  
			  Float.parseFloat(str);  
			  return true;
		  } catch(NumberFormatException e){  
			  return false;  
		  }  
	}
	
	private static void addNewBook (Connection connection) {
		
		boolean checkCondition = false;
		Scanner keyboard = new Scanner(System.in);
		String localQueryString = "";
		
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		
		System.out.println("\nAttempting to enter a new book into BookStore...");
		String isbnNum = "";
		
		while (checkCondition == false) {
			System.out.println("Please enter the book's ISBN Number 13 (XXX-X-XXXX-XXXX-X):");
			isbnNum = keyboard.nextLine();
			String checkISBNNum = isbnNum.replaceAll( "-", "" );
			
			if (isValidISBN(checkISBNNum)) {
				checkCondition = true;
			}
			else {
				System.out.println("Invalid input. Please follow the requested input format and ensure the number follows the ISBN Number rules:\n");
			}
		}
		
		boolean checkEmptyString = false;
		String title = "";
		String authorFName = "";
		String authorLName = "";
		String genre = "";
		String language = "";
		String cost = "";
		float price = 0;
		String numBooks = "";
		int quantity = 0;
		String earn = "";
		float earning  = 0;
		
		
		System.out.println("\nPlease enter the book's title: ");
		while (checkEmptyString == false) {
			title = keyboard.nextLine();
			title = title.toUpperCase();
			
			if (title.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else {
				checkEmptyString = true;
			}
		}

		checkEmptyString = false;
		
		System.out.println("\nPlease enter the book's author's first name: ");
		while (checkEmptyString == false) {
			authorFName = keyboard.nextLine();
			authorFName = title.toUpperCase();
			
			if (authorFName.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else {
				checkEmptyString = true;
			}
		}
		
		checkEmptyString = false;
		
		System.out.println("\nPlease enter the book's author's last name: ");
		while (checkEmptyString == false) {
			authorLName = keyboard.nextLine();
			authorLName = title.toUpperCase();
			
			if (authorLName.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else {
				checkEmptyString = true;
			}
		}
		
		checkEmptyString = false;
		
		System.out.println("\nPlease enter the book's genre: ");
		while (checkEmptyString == false) {
			genre = keyboard.nextLine();
			genre = title.toUpperCase();
			
			if (genre.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else {
				checkEmptyString = true;
			}
		}
		
		checkEmptyString = false;
		
		System.out.println("\nPlease enter the book's language: ");
		while (checkEmptyString == false) {
			language = keyboard.nextLine();
			language = title.toUpperCase();
			
			if (language.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else {
				checkEmptyString = true;
			}
		}
		
		checkEmptyString = false;
		
		System.out.println("\nPlease enter the book's price (XX.XX): ");
		while (checkEmptyString == false) {
			cost = keyboard.nextLine();
			
			if (cost.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else if (isFloat(cost) == false) {
				System.out.println("\nInvalid format. Please ensure that your answer follows the specified format.");
			}
			else {
				checkEmptyString = true;
			}
		}
		price = Float.parseFloat(cost);

		checkEmptyString = false;
		
		System.out.println("\nPlease enter the quantity of books stored in the book store: ");
		while (checkEmptyString == false) {
			numBooks = keyboard.nextLine();
			
			if (numBooks.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else if (isNumeric(numBooks) == false) {
				System.out.println("\nInvalid format. Please ensure that your answer follows the specified format.");
			}
			else {
				checkEmptyString = true;
			}
		}
		quantity = Integer.parseInt(numBooks);
		
		checkEmptyString = false;
		
		System.out.println("\nPlease enter the publisher's earning for this book (XX.XX): ");
		while (checkEmptyString == false) {
			earn = keyboard.nextLine();
			
			if (earn.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else if (isFloat(earn) == false) {
				System.out.println("\nInvalid format. Please ensure that your answer follows the specified format.");
			}
			else {
				checkEmptyString = true;
			}
		}
		earning = Float.parseFloat(earn);
		
		boolean checkPID = false;
		System.out.println("\nPlease enter the publisher's id for this book (P#): ");
		String pubID = "";
		
		while (checkPID == false) {
			pubID = keyboard.nextLine();
			
			localQueryString = "SELECT publisher_id FROM publisher WHERE publisher_id = ?";
			
			try {
				ps = connection.prepareStatement(localQueryString);
				ps.setString(1, pubID);
			}
			catch (Exception e) {
				System.out.println("Check PID prepare statement failure.");
				e.printStackTrace();
			}
			
			try {
				resultSet = ps.executeQuery();
				if (resultSet.next() == false) {
					 boolean validResponse = false;
					 while (validResponse == false) {
						 System.out.println("\nThis publisher does not exist. Do you wish to register a publisher to this book? (Y/N)");
						 
						 String response = keyboard.nextLine();
						 
						 switch (response.toUpperCase()) {
							 case("N"):
								System.out.println("\nPlease attempt to enter the book's Publisher.");
							 	validResponse = true;
								break;
							 case("Y"):
								System.out.println("\nPlease register the new Publisher.");
							 	addPublisher(connection);
							 	validResponse = true;
							 	checkPID = true;
						 		break;
							 default:
								System.out.println("\nInvalid input. Please enter a valid response.");
						 }
					 }
				 }
				 else {
					 checkPID = true;
				 }
			}
			catch (Exception e) {
				 System.out.println("Check PID Execute Query statement failure.");
				 e.printStackTrace();
			}
		}
		
		boolean checkWareName = false;
		
		System.out.println("\nPlease enter the Warehouse name in which this book will be stored at: ");
		String wareName = "";
		while (checkWareName == false) {
			wareName = keyboard.nextLine();
			
			localQueryString = "SELECT name FROM warehouse WHERE name = ?";
			
			try {
				ps = connection.prepareStatement(localQueryString);
				ps.setString(1, wareName);
			}
			catch (Exception e) {
				System.out.println("Check Warehouse prepare statement failure.");
				e.printStackTrace();
			}
			
			try {
				resultSet = ps.executeQuery();
				if (resultSet.next() == false) {
					 System.out.println("\nThis warehouse name has not been registered. Please enter a different warehouse:");
				 }
				 else {
					 checkWareName = true;
				 }
			}
			catch (Exception e) {
				 System.out.println("Check Warehouse Execute Query statement failure.");
				 e.printStackTrace();
			}
		}
		
		localQueryString = "insert into book_collection (isbn_number, title, genre, language, price, quantity, publisher_earnings, publisher_id, warehouse_name)" +
				"values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			ps = connection.prepareStatement(localQueryString);
			ps.setString(1, isbnNum);
			ps.setString(2, title);
			ps.setString(3, genre);
			ps.setString(4, language);
			ps.setFloat(5, price);
			ps.setInt(6, quantity);
			ps.setFloat(7, earning);
			ps.setString(8, pubID);
			ps.setString(9, wareName);
		}
		catch (Exception e) {
			System.out.println("Insert Book prepare statement failure.");
			e.printStackTrace();
		}
		
		try {
			ps.executeUpdate();
		}
		catch (Exception e) {
			 System.out.println("Insert Book Execute Query statement failure.");
			 e.printStackTrace();
		}
		
		localQueryString = "insert into author (first_name, last_name) values (?, ?)";
		
		try {
			ps = connection.prepareStatement(localQueryString);
			ps.setString(1, authorFName);
			ps.setString(2, authorLName);
		}
		catch (Exception e) {
			System.out.println("Insert Author prepare statement failure.");
			e.printStackTrace();
		}
		
		try {
			ps.executeUpdate();
		}
		catch (Exception e) {
			 System.out.println("Insert Author Execute Query statement failure.");
			 e.printStackTrace();
		}
		
		localQueryString = "insert into writes (author_first_name, author_last_name, isbn_number) values (?, ?, ?)";
		
		try {
			ps = connection.prepareStatement(localQueryString);
			ps.setString(1, authorFName);
			ps.setString(2, authorLName);
			ps.setString(3, isbnNum);
		}
		catch (Exception e) {
			System.out.println("Insert Writes prepare statement failure.");
			e.printStackTrace();
		}
		
		try {
			ps.executeUpdate();
		}
		catch (Exception e) {
			 System.out.println("Insert Writes Execute Query statement failure.");
			 e.printStackTrace();
		}
		
		localQueryString = "insert into manages (owner_id, isbn_number) values(?, ?)";
		try {
			ps = connection.prepareStatement(localQueryString);
			ps.setString(1, ownerID);
			ps.setString(2, isbnNum);
		}
		catch (Exception e) {
			System.out.println("Insert Manages prepare statement failure.");
			e.printStackTrace();
		}
		
		try {
			ps.executeUpdate();
		}
		catch (Exception e) {
			 System.out.println("Insert Manages Execute Query statement failure.");
			 e.printStackTrace();
		}
		
		System.out.println("Book was successfully added to Book Store Collection.\n");
		
	}
	
	private static void removeBook (Connection connection) {
		
		Scanner keyboard = new Scanner(System.in);
		
		System.out.println("\nAttempting to remove a book from BookStore...");
		String isbnNum = "";
		boolean checkCondition = false;
		int numCurrentBooks = 0;
		
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		
		String localQueryString = "SELECT count(*) FROM book_collection";
		String localQueryString2 = "SELECT * FROM consists WHERE isbn_number = ?";
		String localQueryString3 = "DELETE FROM writes WHERE isbn_number = ?";
		String localQueryString4 = "DELETE FROM book_collection WHERE isbn_number = ?";
		
		try {
			ps = connection.prepareStatement(localQueryString);
		}
		catch (Exception e) {
			System.out.println("Check Book prepare statement failure.");
			e.printStackTrace();
		}
		try {
			 resultSet = ps.executeQuery();
			 resultSet.next();
			 numCurrentBooks = resultSet.getInt("count");
			 
			 if (numCurrentBooks == 0) {
				 System.out.println("No books exist in the bookstore yet. Please add a few books before removing them.\n");
			 }
			 else { 
				 
				 while (checkCondition == false) {
					 System.out.println("Please enter the book's ISBN Number 13 (XXX-X-XXXX-XXXX-X):");
					 isbnNum = keyboard.nextLine();
					 String checkISBNNum = isbnNum.replaceAll( "-", "" );
					 
					 if (isValidISBN(checkISBNNum)) {
						 
						 ps2 = connection.prepareStatement(localQueryString2);
						 ps2.setString(1, isbnNum);
						 resultSet2 = ps2.executeQuery();
						 if (resultSet2.next()) {
							 System.out.println("There are orders containing this item that are still in progess. Please wait until these orders are completed.\n");
						 }
						 else {
							 try {
								 ps3 = connection.prepareStatement(localQueryString3); 
								 ps3.setString(1, isbnNum);
								 ps4 = connection.prepareStatement(localQueryString4);
								 ps4.setString(1, isbnNum);
							 }
							 catch (Exception e) {
								 System.out.println("Remove Book prepare statement failure.");
								 e.printStackTrace();
							 }
							 
							 try {
								 ps3.executeUpdate();
								 ps4.executeUpdate();
							 }
							 catch (Exception e) {
								 System.out.println("Remove Book remove statement failure.");
								 e.printStackTrace();
							 }
							 //Handle Remove update return message
							 System.out.println("Successfully removed book from BookStore.\n");
						 }
						 checkCondition = true; 
					 }
					 else {
						 System.out.println("Invalid input. Please follow the requested input format and ensure the number follows the ISBN Number rules:\n");
					 }
				 }
			 }

		 }
		 catch (Exception e) {
			 System.out.println("Check Book Execute Query statement failure.");
			 e.printStackTrace();
		 }
		
	}
	
	private static void registerBankAccount (Connection connection) {
		Scanner keyboard = new Scanner(System.in);
		
		System.out.println("Attempting to register a new publisher's bank account...");
		
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		boolean checkCondition = false;
		String accountID = "";
		
		String localQueryString = "SELECT account_ID FROM bank_account WHERE account_ID = ?";
		
		System.out.println("\nPlease enter the publisher's bank account ID: ");
		while (checkCondition == false) {
			accountID = keyboard.nextLine();
			
			try {
				ps = connection.prepareStatement(localQueryString);
				ps.setString(1, accountID);
			}
			catch (Exception e) {
				System.out.println("Get PublisherID prepare statement failure.");
				e.printStackTrace();
			}
			
			try {
				 resultSet = ps.executeQuery();
				 if (resultSet.next() == true) {
					 System.out.println("This account ID has already been registered to a publisher. Please enter a different bank account ID:");
				 }
				 else {
					 checkCondition = true;
				 }

			 }
			catch (Exception e) {
				 System.out.println("Check Account ID Execute Query statement failure.");
				 e.printStackTrace();
			 }
		}
		
		boolean checkEmptyString = false;
		String bankName = "";
		String branchName = "";
		
		System.out.println("\nPlease enter the bank's name: ");
		while (checkEmptyString  == false) {
			bankName = keyboard.nextLine();
			if (bankName.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else {
				checkEmptyString = true;
			}
		}
		
		checkEmptyString = false;
		
		System.out.println("\nPlease enter the bank branch's name: ");
		while (checkEmptyString  == false) {
			branchName = keyboard.nextLine();
			if (branchName.length() == 0) {
				System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
			}
			else {
				checkEmptyString = true;
			}
		}
		
		localQueryString = "insert into bank_account (account_id, bank_name, branch_name)" + 
				"values (?, ?, ?)";
		
		try {
			ps = connection.prepareStatement(localQueryString);
			ps.setString(1, accountID);
			ps.setString(2,  bankName);
			ps.setString(3,  branchName);
		}
		catch (Exception e) {
			System.out.println("Create account prepare statement failure.");
			e.printStackTrace();
		}
		
		try {
			 ps.executeUpdate();
		 }
		 catch (Exception e) {
			 System.out.println("Create account Execute Query statement failure.");
			 e.printStackTrace();
		 }

	}
	
	private static void addPublisher (Connection connection) {

		Scanner keyboard = new Scanner(System.in);
		
		System.out.println("Attempting to register a new publisher...");
		
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		int publisherID = 0;
		String pubID = "";
		
		String localQueryString = "SELECT publisher_id FROM publisher ORDER BY length(publisher_id), publisher_id ASC";
		
		try {
			 ps = connection.prepareStatement(localQueryString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		}
		catch (Exception e) {
			 System.out.println("Get PublisherID prepare statement failure.");
			 e.printStackTrace();
		}
		
		try {
			 resultSet = ps.executeQuery();
			 if (resultSet.last() == true) {
				 pubID = resultSet.getString("publisher_ID");
				 pubID = pubID.replaceAll("P", "");
				 publisherID = Integer.parseInt(pubID);
				 publisherID++;
				 System.out.printf("\nYour new pubId is %d\n", publisherID);
			 }
			 else {
				 publisherID = 1;
				 System.out.printf("\nYour new pubId from 0 publishers is %d\n", publisherID);
			 }

		 }
		 catch (Exception e) {
			 System.out.println("PublisherID Execute Query statement failure.");
			 e.printStackTrace();
		 }
		 
		 pubID = "P" + Integer.toString(publisherID);
		 
		 boolean checkEmptyString = false;
		 String firstName = "";
		 String lastName = "";
		 String streetAddress = "";
		 String zipCode = "";
		 String city = "";
		 String country = "";
		 String email = "";
		 String phoneNum = "";
		 
		 System.out.println("\nPlease enter the publisher's first name: ");
		 while (checkEmptyString == false) {
			 firstName = keyboard.nextLine();
			 firstName = firstName.toUpperCase();
			 if (firstName.isEmpty()) {
					System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
				}
			else {
				checkEmptyString = true;
			}
		 }
		 
		 checkEmptyString = false;
		 
		 System.out.println("\nPlease enter the publisher's last name: ");
		 while (checkEmptyString == false) {
			 lastName = keyboard.nextLine();
			 lastName = lastName.toUpperCase();
			 if (lastName.isEmpty()) {
					System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
				}
			else {
				checkEmptyString = true;
			}
		 }

		 checkEmptyString = false;
		 
		 System.out.println("\nPlease enter the publisher's street address: ");
		 while (checkEmptyString == false) {
			 streetAddress = keyboard.nextLine();
			 streetAddress = streetAddress.toUpperCase();
			 if (streetAddress.isEmpty()) {
					System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
				}
			else {
				checkEmptyString = true;
			}
		 }
		 
		 checkEmptyString = false;
		 
		 System.out.println("\nPlease enter the publisher's zip code: ");
		 while (checkEmptyString == false) {
			 zipCode = keyboard.nextLine();
			 zipCode = zipCode.toUpperCase();
			 if (zipCode.isEmpty()) {
					System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
				}
			else {
				checkEmptyString = true;
			}
		 }
		 
		 checkEmptyString = false;
		 
		 System.out.println("\nPlease enter the publisher's residing city: ");
		 while (checkEmptyString == false) {
			 city = keyboard.nextLine();
			 city = city.toUpperCase();
			 if (city.isEmpty()) {
					System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
				}
			else {
				checkEmptyString = true;
			}
		 }
		 
		 checkEmptyString = false;
		 
		 System.out.println("\nPlease enter the publisher's residing country: ");
		 while (checkEmptyString == false) {
			 country = keyboard.nextLine();
			 country = country.toUpperCase();
			 if (country.isEmpty()) {
					System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
				}
			else {
				checkEmptyString = true;
			}
		 }
		 
		 checkEmptyString = false;
		 
		 System.out.println("\nPlease enter the publisher's email address: ");
		 while (checkEmptyString == false) {
			 email = keyboard.nextLine();
			 email = email.toUpperCase();
			 if (email.isEmpty()) {
					System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
				}
			else {
				checkEmptyString = true;
			}
		 }
		 
		 checkEmptyString = false;
		 
		 System.out.println("\nPlease enter the publisher's phone number: ");
		 while (checkEmptyString == false) {
			 phoneNum = keyboard.nextLine();
			 phoneNum = phoneNum.toUpperCase();
			 if (phoneNum.isEmpty()) {
					System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
				}
			else {
				checkEmptyString = true;
			}
		 }
		 
		 boolean checkCondition = false;
		 String localQueryString2 = "";
		 String bankID = "";
			
		 while (checkCondition == false) {
			 System.out.println("\nPlease enter the publisher's bank account ID: ");
			 bankID = keyboard.nextLine();
			 
			 localQueryString = "SELECT account_ID FROM bank_account WHERE account_ID = ?";
			 localQueryString2 = "SELECT banking_account_id FROM publisher WHERE banking_account_id = ?";
			 
			 try {
				 ps = connection.prepareStatement(localQueryString);
				 ps.setString(1, bankID);
				 
				 ps2 = connection.prepareStatement(localQueryString2);
				 ps2.setString(1, bankID);
			 }
			 catch (Exception e) {
				 System.out.println("Check Bank Account prepare statement failure.");
				 e.printStackTrace();
			 }
			
			try {
				 resultSet = ps.executeQuery();
				 resultSet2 = ps2.executeQuery();
				 if (resultSet.next() == false) {
					 System.out.println("This bank account does not exist. Do you wish to register a bank account to this publisher(Y/N)?");
					 
					 boolean validResponse = false;
					 while (validResponse == false) {
						 String response = keyboard.nextLine();
						 
						 switch (response.toUpperCase()) {
							 case("N"):
								System.out.println("Please attempt to enter the Publisher's Bank Account ID.");
							 	validResponse = true;
								break;
							 case("Y"):
								//Check if adding new bank account is valid
								System.out.println("Please register the Publisher's bank account.");
							 	registerBankAccount(connection);
							 	validResponse = true;
							 	checkCondition = true;
						 		break;
							 default:
								System.out.println("\nInvalid input. Please enter a valid response.");
						 }
					 }

				 }
				 else if (resultSet2.next() == true) {
					 System.out.println("This bank account has already been assigned to a publisher.");
					 System.out.println("Please enter a different valid banking account ID.");
				 }
				 else {
					 checkCondition = true;
				 }

			 }
			catch (Exception e) {
				 System.out.println("Check Bank Account Execute Query statement failure.");
				 e.printStackTrace();
			 }
		 }
		 
		 localQueryString = "insert into publisher (publisher_id, first_name, last_name, "
		 		+ "street_address, zip_code, city, country, "
		 		+ "publisher_email_address, main_phone_number, banking_account_id)"
		 		+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		 try {
			 ps = connection.prepareStatement(localQueryString);
			 ps.setString(1, pubID);
			 ps.setString(2, firstName);
			 ps.setString(3, lastName);
			 ps.setString(4, streetAddress);
			 ps.setString(5, zipCode);
			 ps.setString(6, city);
			 ps.setString(7, country);
			 ps.setString(8, email);
			 ps.setString(9, phoneNum);
			 ps.setString(10, bankID);
		 }
		 catch (Exception e) {
			 System.out.println("Insert Publisher prepare statement failure.");
			 e.printStackTrace();
		 }
		 try {

			 ps.executeUpdate();
		 }
		 catch (Exception e) {
			 System.out.println("Insert Publisher execute update statement failure.");
			 e.printStackTrace();
		 }
		 
	}
	
	
	public static void ownerInterface(Connection connection) {
		
		boolean checkCondition = false;
		System.out.println("\n\nYou have logged in as an owner.");
		System.out.println("Here are the following options avilable to the owner: ");
		Scanner keyboard = new Scanner(System.in);
		
		while (checkCondition == false) {
			System.out.println("(1) Add Book to BookStore (2) Remove Book from Bookstore");
			System.out.println("(3) Add Publisher (4) Create Report (5) Search for Book (6) Logout");
			String menuOption = keyboard.nextLine();
			
			switch (menuOption) {
				case("1"):
					addNewBook(connection);
					break;
				case("2"):
					removeBook(connection);
					break;
				case("3"):
					addPublisher(connection);
					break;
				case("4"):
					BookStoreCreateReport.createReportInterface(connection, ownerID);
					break;
				case("5"):
					BookCollectionSearch.searchForBook(connection);
					break;
				case("6"):
					checkCondition = true;
					System.out.println("\nLogging out of owner account...\n");
					break;
				default:
					System.out.println("\nInvalid input. Please enter a valid menu option.");
			}
		}

	}
}
