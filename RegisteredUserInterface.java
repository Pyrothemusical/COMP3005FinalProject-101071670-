import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import static java.util.Calendar.MONTH;

public class RegisteredUserInterface {
	
	private static String currUserId = "";
	private static ArrayList<BookInformation> mainCart = new ArrayList<BookInformation>();
	private static String cardName = "";
	private static String cardNumber = "";
	private static String cardDate = "";
	private static String cardCVC = "";
	private static String delStreet = "";
	private static String delZip = ""; 
	private static String delCity = "";
	private static String delCountry = "";
	
	
	private static void addNumberofBooks(ArrayList<BookInformation> tempCart, Scanner keyboard) {
		boolean checkAddBook = false;
		
		System.out.println("\nWhich book item would you like to add to your cart?");
		while (checkAddBook == false) {
			String addInput = keyboard.nextLine();
			int addOption = Integer.parseInt(addInput);
			
			if (addOption > tempCart.size() || addOption <= 0) {
				System.out.println("\nInvalid input. Please ensure that you input the correct book number that you wish to add to your cart.");
			}
			else {
				BookInformation book = tempCart.get(addOption - 1);
				
				boolean checkQuantity = false;
				System.out.println("\nHow many items of this book would you like to add?");
				
				while (checkQuantity == false) {
					String number = keyboard.nextLine();
					int numBooks = 0;
					numBooks = Integer.parseInt(number);
					
					if (numBooks > book.numBooksStored) {
						System.out.printf("\nYou are trying to add a larger amount of books than "
								+ "the number of books present in the Book Store: %d books.\n", book.numBooksStored);
						System.out.println("Please input a valid number of books you wish to add to your shopping cart.");
					}
					else {
						
						book.quantity = numBooks;
						boolean addedBook = false;
						
						if (mainCart.size() == 0) {
							mainCart.add(book);
							//System.out.println("Adding books when cart is 0.\n");
						}
						else {
							for (int i = 0; i< mainCart.size(); i++) {
								if (mainCart.get(i).isbnNum.equals(book.isbnNum) && addedBook == false) {
									mainCart.get(i).quantity = mainCart.get(i).quantity + numBooks;
									//System.out.printf("\nAdding number of books in middle of loop: %d\n", mainCart.get(i).quantity);
									addedBook = true;
								}
								else if (i == mainCart.size() - 1 && addedBook == false) {
									mainCart.add(book);
									//System.out.printf("\nAdding number of books at end of loop: %d\n", mainCart.get(i).quantity);
									addedBook = true;
								}
							}
							System.out.println();
						}
						
						checkQuantity = true;
					}
				}
				checkAddBook = true;
			}
		}
	}
	
	private static void addBookCart(Connection connection) {
		
		Scanner keyboard = new Scanner(System.in);
		boolean checkCondition = false;
		
		ArrayList<BookInformation> tempCart = new ArrayList<BookInformation>();
		
		System.out.println("How would you like to search for the book that you wish to add to your shopping cart?\n");
		
		while (checkCondition == false) {
			System.out.println("(1) Search by Title (2) Search by ISBN Number (3) Search by Author (Last Name)");
			System.out.println("(4) Search by Genre (5) Search by Language (6) Search by Publisher (Last Name) (7) Stop Adding Books");
			
			String menuOption = keyboard.nextLine();
			tempCart.clear();
			
			switch(menuOption) {
				case("1"):
					BookCollectionSearch.searchTitle(connection, tempCart);
					if (tempCart.size() != 0) {
						viewBookCart(false, tempCart);
						addNumberofBooks(tempCart, keyboard);
					}
					System.out.println();
					break;
				case("2"):
					BookCollectionSearch.searchISBN(connection, tempCart);
					if (tempCart.size() != 0) {
						viewBookCart(false, tempCart);
						addNumberofBooks(tempCart, keyboard);
					}
					System.out.println();
					break;
				case("3"):
					BookCollectionSearch.searchAuthor(connection, tempCart);
					if (tempCart.size() != 0) {
						viewBookCart(false, tempCart);
						addNumberofBooks(tempCart, keyboard);
					}
					System.out.println();
					break;
				case("4"):
					BookCollectionSearch.searchGenre(connection, tempCart);
					if (tempCart.size() != 0) {
						viewBookCart(false, tempCart);
						addNumberofBooks(tempCart, keyboard);
					}
					System.out.println();
					break;
				case("5"):
					BookCollectionSearch.searchLanguage(connection, tempCart);
					if (tempCart.size() != 0) {
						viewBookCart(false, tempCart);
						addNumberofBooks(tempCart, keyboard);
					}
					System.out.println();
					break;
				case("6"):
					BookCollectionSearch.searchPublisher(connection, tempCart);
					if (tempCart.size() != 0) {
						viewBookCart(false, tempCart);
						addNumberofBooks(tempCart, keyboard);
					}
					System.out.println();
					break;
				case("7"):
					checkCondition = true;
					break;
				default:
					System.out.println("\nInvalid input. Please enter a valid search option.");
			}
		}
		
		
	}
	
	private static void removeBookCart() {
		if (mainCart.isEmpty()) {
			System.out.println("You do not have any items in your shopping cart to remove.\n");
		}
		else {
			System.out.println("Viewing all books in your shopping cart.\n");
			viewBookCart(true, mainCart);
			Scanner keyboard = new Scanner(System.in);
			boolean checkCondition = false;
			
			System.out.println("Which book item would you like to remove?");
			
			while (checkCondition == false) {
				String item = keyboard.nextLine();
				int itemNum = Integer.parseInt(item);
				
				if (itemNum > mainCart.size() || itemNum <= 0) {
					System.out.println("Invalid input. Please ensure that you input the correct book number of your cart. \n");
				}
				else {
					BookInformation book = mainCart.get(itemNum - 1);
					
					if (book.quantity == 1) {
						mainCart.remove(itemNum - 1);
					}
					else {
						boolean checkQuantity = false;
						System.out.println("\nHow many items of this book would you like to remove?");
						
						while (checkQuantity == false) {
							String number = keyboard.nextLine();
							int numBooks = Integer.parseInt(number);
							
							if (numBooks > book.quantity) {
								System.out.println("\nYou are trying to remove a larger amount of books than the number you have in the cart.");
								System.out.println("Please input a valid number of books you wish to remove.");
							}
							else {
								mainCart.get(itemNum - 1).quantity = mainCart.get(itemNum - 1).quantity - numBooks;
								
								if (mainCart.get(itemNum - 1).quantity == 0) {
									mainCart.remove(itemNum - 1);
								}
								checkQuantity = true;
							}
						}
					}
					
					checkCondition = true;
				}
			}
		}
	}

	private static void viewBookCart(boolean viewQuantity, ArrayList<BookInformation> cart) {
		
		if (cart.isEmpty()) {
			System.out.println("You do not have any items in your shopping cart to view.\n");
		}
		else {
			for (int i = 0; i < cart.size(); i++) {
				
				int bookNum = i + 1;
				
				System.out.printf("\n[(Book #%d)]", bookNum);
				String authorName = cart.get(i).authorFName + " " + cart.get(i).authorLName;
				String pubName = cart.get(i).pubFName + " " + cart.get(i).pubLName;
				
				System.out.printf("\nISBN Number: %-10.30s  Book Title: %-10.30s   Author: %-10.30s%n", 
						cart.get(i).isbnNum, cart.get(i).title, authorName);
				System.out.printf("Publisher: %-10.30s Genre: %-10.30s  Language: %-10.30s  Price: $%.2f\n", 
						pubName, cart.get(i).genre, cart.get(i).language, cart.get(i).price);
				if (viewQuantity == true) {
					System.out.printf("Number of Items: %d\n", cart.get(i).quantity);
				}
			}
		}
	}
	
	private static void checkOutCart(Connection connection) {
		if (mainCart.size() == 0) {
			System.out.println("Your shopping cart is empty; you cannot check out.\n");
		}
		else {
			System.out.println("Checking out items in shopping cart: \n");
			
			specifyDeliveryBilling(connection);
			
			insertOrderTables(connection, cardName, cardNumber, cardDate, cardCVC, delStreet, delZip, delCity, delCountry);
		}
	}
	
	private static void specifyDeliveryBilling (Connection connection) {
		Scanner keyboard = new Scanner(System.in);
		boolean checkCondition = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String localQueryString = "SELECT * FROM regular_user WHERE user_id = ?";
		
		System.out.println("\nWould you like to use the delivery and billing information from your user profile? (Y/N)");
		while (checkCondition == false) {
			String response = keyboard.nextLine();
			response = response.toUpperCase();
			
			switch(response) {
			case("Y"):
				try {
					 ps = connection.prepareStatement(localQueryString);
					 ps.setString(1, currUserId);
				 }
				catch (Exception e) {
					 System.out.println("Get Billing + Delivery Info prepare statement failure.");
					 e.printStackTrace();
				}
			
				try {
					 rs = ps.executeQuery();
					 rs.next();
					 
					 cardName = rs.getString("billing_card_name");
					 cardNumber = rs.getString("billing_card_number");
					 cardDate = rs.getString("billing_card_expiry_date");
					 cardCVC = rs.getString("billing_card_cvv_cvc");
					 delStreet = rs.getString("delivery_street_address");
					 delZip = rs.getString("delivery_zip_code");
					 delCity = rs.getString("delivery_city");
					 delCountry = rs.getString("delivery_country");
					 
					 
				}
				catch (Exception e) {
					 System.out.println("Get Billing + Delivery Info Execute Query statement failure.");
					 e.printStackTrace();
				}
				
				checkCondition = true;
				break;
			case("N"):
				
				boolean checkEmptyString = false;
				
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
				
				System.out.println("\nPlease enter your Billing Card Expiry Date (yyyy-mm-dd):");
				while (checkEmptyString == false) {
					cardDate = keyboard.nextLine();
					
					if (cardDate.length() == 0) {
						System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
					}
					else {
						try {
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					        Date expDate = format.parse(cardDate);
					        java.sql.Date sqlExpDate = new java.sql.Date(expDate.getTime());
					        checkEmptyString = true;
						} catch (ParseException e1) {
							System.out.println("\nInvalid format. Please ensure that your answer follows the specified input");
						}  
					}
				}
				
				checkEmptyString = false;
				
				System.out.println("\nPlease enter your Billing Card CVV or CVC number:");
				while (checkEmptyString == false) {
					cardCVC = keyboard.nextLine();
					
					if (cardCVC.length() == 0) {
						System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
					}
					else if (cardCVC.length() > 4) {
						System.out.println("\nInput is larger than 4 characters. Please ensure that you inputted a valid answer.");
					}
					else {
						checkEmptyString = true;
					}
				}
				
				checkEmptyString = false;
				
				System.out.println("\nPlease enter your Delivery Street Address:");
				while (checkEmptyString == false) {
					delStreet = keyboard.nextLine();
					
					if (delStreet.length() == 0) {
						System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
					}
					else {
						checkEmptyString = true;
					}
				}
				
				checkEmptyString = false;
				
				System.out.println("\nPlease enter your Delivery Zip Code:");
				while (checkEmptyString == false) {
					delZip = keyboard.nextLine();
					
					if (delZip.length() == 0) {
						System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
					}
					else {
						checkEmptyString = true;
					}
				}
				
				checkEmptyString = false;
				
				System.out.println("\nPlease enter your Delivery City:");
				while (checkEmptyString == false) {
					delCity = keyboard.nextLine();
					
					if (delCity.length() == 0) {
						System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
					}
					else {
						checkEmptyString = true;
					}
				}
				
				checkEmptyString = false;
				
				System.out.println("\nPlease enter your Delivery Country:");
				while (checkEmptyString == false) {
					delCountry = keyboard.nextLine();
					
					if (delCountry.length() == 0) {
						System.out.println("\nEmpty input. Please ensure that you inputted a valid answer.");
					}
					else {
						checkEmptyString = true;
					}
				}
				
				checkCondition = true;
				break;
			default:
				System.out.println("\nInvalid input. Please enter a valid menu option.");
			}
		}

	}
	
	private static void insertOrderTables(Connection connection, String cardName, String cardNumber, String cardDate, String cardCVC,
			String delStreet, String delZip, String delCity, String delCountry) {
		
		PreparedStatement ps0 = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs0 = null;
		ResultSet rs = null;
		
		String localQueryString0 = "SELECT * FROM warehouse";
		String localQueryString = "SELECT order_id FROM order_placement ORDER BY length(order_id), order_id ASC";
		
		String localQueryString2 = "insert into order_placement (order_id, order_item_id, item_quantity, date_of_order, order_current_city, order_current_country, user_id,"
				+ " order_billing_name, order_billing_number, order_billing_expiry_date, order_billing_cvc_cvv, order_delivery_street_address, order_delivery_zip_code, order_delivery_city, order_delivery_country)"
				 + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		String localQueryString3 = "insert into consists (order_id, order_item_id, isbn_number)"
				 + "values (?, ?, ?)";
		
		String isbnNum = "";
		int itemQuantity = 0;
		String orderID = "";
		String orderItemID = "";
		int orderIDNum = 0;
		String currentUser = "";
		String warehouseCity = "";
		String warehouseCountry = "";
		
		Date orderDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date ordDate = null;
        java.sql.Date finalOrdDate = null;
        Date expDate = null;
        java.sql.Date finalExpDate = null;

		try {
			ordDate = format.parse(format.format(orderDate));
			expDate = format.parse(cardDate);
	        finalOrdDate = new java.sql.Date(ordDate.getTime());
	        finalExpDate = new java.sql.Date(expDate.getTime());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}


		try {
			ps0 = connection.prepareStatement(localQueryString0);
			ps = connection.prepareStatement(localQueryString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		}
		catch (Exception e) {
			 System.out.println("Get Warehouse + OrderId prepare statement failure.");
			 e.printStackTrace();
		}
		
		try {
			rs0 = ps0.executeQuery();
			rs0.next();
			warehouseCity = rs0.getString("warehouse_city");
			warehouseCountry = rs0.getString("warehouse_country");
			
			rs = ps.executeQuery();
			if (rs.last() == true) {
				 orderID = rs.getString("order_ID");
				 orderID = orderID.replaceAll("O", "");
				 orderIDNum = Integer.parseInt(orderID);
				 orderIDNum++;
			 }
			 else {
				 orderIDNum = 1;
			 }
		}
		catch (Exception e) {
			 System.out.println("Get Warehouse + OrderId execute statement failure.");
			 e.printStackTrace();
		}
		
		orderID = "O" + Integer.toString(orderIDNum);
		
		for (int i = 0; i < mainCart.size(); i++) {
			isbnNum = mainCart.get(i).isbnNum;
			itemQuantity = mainCart.get(i).quantity;
			currentUser = currUserId;
			orderItemID = "I" + Integer.toString(i + 1);
			
			try {
				ps2 = connection.prepareStatement(localQueryString2);
				ps2.setString(1, orderID);
				ps2.setString(2, orderItemID);
				ps2.setInt(3, itemQuantity);
				ps2.setDate(4, finalOrdDate);
				ps2.setString(5, warehouseCity);
				ps2.setString(6, warehouseCountry);
				ps2.setString(7, currentUser);
				ps2.setString(8, cardName);
				ps2.setString(9, cardNumber);
				ps2.setDate(10, finalExpDate);
				ps2.setString(11, cardCVC);
				ps2.setString(12, delStreet);
				ps2.setString(13, delZip);
				ps2.setString(14, delCity);
				ps2.setString(15, delCountry);
				
				ps3 = connection.prepareStatement(localQueryString3);
				ps3.setString(1, orderID);
				ps3.setString(2, orderItemID);
				ps3.setString(3, isbnNum);
			}
			catch (Exception e) {
				 System.out.println("Insert order + consists prepare statement failure.");
				 e.printStackTrace();
			}
			
			try {
				if (ps2.executeUpdate() != 0 && ps3.executeUpdate() != 0) {
					System.out.printf("\nYour order %s for item %s has been placed.\n", orderID, isbnNum);
					
					updateBookQuantity(connection, isbnNum, itemQuantity);
				}
			}
			catch (Exception e) {
				 System.out.println("Insert order + consists execute query statement failure.");
				 e.printStackTrace();
			}

		}
	}
	
	private static void updateBookQuantity(Connection connection, String isbnNum, int itemQuantitySold) {
		
		String localQueryString = "SELECT quantity FROM book_collection WHERE isbn_number = ?";
		String localQueryString2 = "update book_collection set quantity = ? where isbn_number = ?";
		
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		
		ResultSet rs = null;
		
		int currNumBooks = 0;
		int numBooksLeft = 0;
		
		try {
			ps = connection.prepareStatement(localQueryString);
			ps.setString(1, isbnNum);
		}
		catch (Exception e) {
			 System.out.println("Get quantity prepare statement failure.");
			 e.printStackTrace();
		}
		
		try {
			rs = ps.executeQuery();
			rs.next();
			currNumBooks = rs.getInt("quantity");
		}
		catch (Exception e) {
			 System.out.println("Get quantity execute query statement failure.");
			 e.printStackTrace();
		}
		
		numBooksLeft = currNumBooks - itemQuantitySold;
		
		try {
			ps2 = connection.prepareStatement(localQueryString2);
			ps2.setInt(1, numBooksLeft);
			ps2.setString(2, isbnNum);
		}
		catch (Exception e) {
			 System.out.println("Set quantity prepare statement failure.");
			 e.printStackTrace();
		}
		
		try {
			ps2.executeUpdate();
		}
		catch (Exception e) {
			 System.out.println("Set quantity execute query statement failure.");
			 e.printStackTrace();
		}

		if (numBooksLeft <= 10) {
			sendEmailPublisher(connection, isbnNum);
		}
		
	}
	
	 private static boolean isBeforeMonths(Date aDate) {
		    Calendar calendar = Calendar.getInstance();
		    calendar.add(MONTH, 1);
		    return aDate.compareTo(calendar.getTime()) < 0;
		  }
	
	private static void sendEmailPublisher(Connection connection, String isbnNum) {
		
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		
		Date orderDate = null;
		
		int totalBooksToOrder = 0;
		int numBooksOrdered = 0;
		int currentBookQuantity = 0;
		
		String pubFName = "";
		String pubLName = "";
		String pubName = "";
		String pubEmail = "";
		String title = "";
		
		String localQueryString = "SELECT * FROM book_collection NATURAL JOIN consists "
				+ "NATURAL JOIN order_placement WHERE isbn_number = ?";
		
		try {
			ps = connection.prepareStatement(localQueryString);
			ps.setString(1, isbnNum);
		}
		catch (Exception e) {
			 System.out.println("Set isbnNum to check orders prepare statement failure.");
			 e.printStackTrace();
		}
		
		try {
			rs = ps.executeQuery();
			while (rs.next()) {
				
				orderDate = rs.getDate("date_of_order");
				
				//Revisit this to get exactly one previous month
				if(isBeforeMonths(orderDate)) {
					numBooksOrdered = rs.getInt("item_quantity");
					totalBooksToOrder = totalBooksToOrder + numBooksOrdered;
				}
				
				currentBookQuantity = rs.getInt("quantity");
				
			}
			
		}
		catch (Exception e) {
			 System.out.println("Set isbnNum to check orders execute query statement failure.");
			 e.printStackTrace();
		}
		
		String localQueryString2 = "SELECT * FROM book_collection NATURAL JOIN publisher WHERE isbn_number = ?";
		String localQueryString3 = "UPDATE public.book_collection SET quantity = ? WHERE isbn_number = ?";
		
		try {
			ps2 = connection.prepareStatement(localQueryString2);
			ps2.setString(1, isbnNum);
			
			ps3 = connection.prepareStatement(localQueryString3);
			ps3.setInt(1, totalBooksToOrder);
			ps3.setString(2, isbnNum + currentBookQuantity);
			
		}
		catch (Exception e) {
			 System.out.println("Get isbnNum and update quantity prepare statement failure.");
			 e.printStackTrace();
		}
		
		try {
			rs2 = ps2.executeQuery();
			rs2.next();
			
			pubEmail = rs2.getString("publisher_email_address");
			pubFName = rs2.getString("first_name");
			pubLName = rs2.getString("last_name");
			pubName = pubFName + " " + pubLName;
			title = rs2.getString("title");
			
			System.out.println("[(System Log)]\n");
			System.out.printf("An email was sent to %s using the email address: %s to notify them\n", pubName, pubEmail);
			System.out.printf("of a request to purchase %d copies of %s\n", totalBooksToOrder, title);
			
			ps3.executeUpdate();
			
		}
		catch (Exception e) {
			 System.out.println("Get isbnNum and update quantity execute statement failure.");
			 e.printStackTrace();
		}
	}
	
	private static void trackOrder(Connection connection) {
		
		String trackOrderID = "";
		String isbnNum = "";
		String trackOrderItemID = "";
		String title = "";
		String authorFName = "";
		String authorLName = "";
		String author = "";

		String delStreet = "";
		String delZip = ""; 
		String delCity = "";
		String delCountry = "";
		
		String orderCity = "";
		String orderCountry = "";
		
		float price;
		int itemQuantity = 0;
		float totalCost = 0;
		
		Date orderDate = null;
		String strOrderDate = "";
		
		Scanner keyboard = new Scanner(System.in);
		
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		
		ResultSet rs = null;
		ResultSet rs2 = null;
		
		String localQueryString = "SELECT * FROM consists NATURAL JOIN book_collection NATURAL JOIN order_placement NATURAL JOIN writes WHERE order_id = ? AND user_id = ?";
		
		System.out.println("\nAttempting to track order...");
		System.out.println("\nPlease enter the ID of the order you wish to track (O#):");
		trackOrderID = keyboard.nextLine().toUpperCase();
		
		try {
			ps = connection.prepareStatement(localQueryString);
			ps.setString(1, trackOrderID);
			ps.setString(2, currUserId);
		}
		catch (Exception e) {
			 System.out.println("Get isbnNum prepare statement failure.");
			 e.printStackTrace();
		}
		
		try {
			rs = ps.executeQuery();
			
			if (rs.next() == false) {
				System.out.println("\nNo order found. Please ensure you specificed the correct order ID.\n");
			}
			else {
				isbnNum = rs.getString("isbn_number");
				trackOrderItemID = rs.getString("order_item_id");
				title = rs.getString("title");
				authorFName = rs.getString("author_first_name");
				authorLName = rs.getString("author_last_name");
				author = authorFName + " " + authorLName;
				
				delStreet = rs.getString("order_delivery_street_address");
				delZip = rs.getString("order_delivery_zip_code");
				delCity = rs.getString("order_delivery_city");
				delCountry = rs.getString("order_delivery_country");
				
				price = rs.getFloat("price");
				itemQuantity = rs.getInt("item_quantity");
				totalCost = totalCost + (price * itemQuantity);
				
				orderCity = rs.getString("order_current_city");
				orderCountry = rs.getString("order_current_country");
				
				orderDate = rs.getDate("date_of_order");
				strOrderDate = orderDate.toString();
				
				System.out.printf("\n\nOrder #%s", trackOrderID);
				System.out.printf("\nPlaced on: %s", strOrderDate);
				System.out.printf("\nDelivered to: %s, %s, %s, %s", delStreet, delZip, delCity, delCountry);
				System.out.printf("\nCurrent Order Location: %s, %s\n", orderCity, orderCountry);
				
				printOrder(isbnNum, trackOrderItemID, title, author, price, itemQuantity);
				
				while (rs.next()) {
					isbnNum = rs.getString("isbn_number");
					trackOrderItemID = rs.getString("order_item_id");
					title = rs.getString("title");
					authorFName = rs.getString("author_first_name");
					authorLName = rs.getString("author_last_name");
					author = authorFName + " " + authorLName;
					
					price = rs.getFloat("price");
					itemQuantity = rs.getInt("item_quantity");
					totalCost = totalCost + (price * itemQuantity);
					
					System.out.println();
					printOrder(isbnNum, trackOrderItemID, title, author, price, itemQuantity);
				}
				
				System.out.printf("\n\nTotal Cost: $%.2f", totalCost);
			}
		}
		catch (Exception e) {
			 System.out.println("Get isbnNum execute statement failure.");
			 e.printStackTrace();
		}
		
	}
	
	private static void printOrder(String isbnNum, String trackOrderIDItem, String title, String author, float price, int itemQuantity) {
		
		System.out.printf("\n[(Order Item: %s)]", trackOrderIDItem);
		System.out.printf("\nISBN Number: %s  Book Title: %s   Author: %s", isbnNum, title, author);
		System.out.printf("\nNumber of Items ordered: %d	Price per Item: $%.2f", itemQuantity, price);
	}
	
	
	public static void userInterface(Connection connection, String userId) {
		
		currUserId = userId;
		boolean checkCondition = false;
		Scanner keyboard = new Scanner(System.in);
		
		System.out.printf("This is the user's id: %s\n", currUserId);
		
		System.out.println("\n\nYou have logged in as a registered user.");
		System.out.println("Here are the following options avilable to the user: ");
		
		
		while (checkCondition == false) {
			System.out.println("\n(1) Search for Book (2) Add Book to Cart (3) Remove Book from Cart (4) View Cart");
			System.out.println("(5) Check out Cart (6) Track Order (7) Logout");
			String menuOption = keyboard.nextLine();
			
			
			switch (menuOption) {
				case ("1"):
					BookCollectionSearch.searchForBook(connection);
					break;
				case ("2"):
					addBookCart(connection);
					break;
				case ("3"):					
					removeBookCart();
					break;
				case ("4"):
					viewBookCart(true, mainCart);	
					break;
				case ("5"):
					checkOutCart(connection);
					mainCart.clear();
					break;
				case ("6"):
					trackOrder(connection);
					break;
				case ("7"):
					System.out.println("\nLogging out. Returning to login menu...");
					mainCart.clear();
					checkCondition = true;
					break;
				default:
					System.out.println("\nInvalid input. Please enter a valid menu option.");
				}
		}
	}
}
