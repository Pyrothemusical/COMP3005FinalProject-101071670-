import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class BookStoreCreateReport {
	
	private static String ownerID = "";
	private static String ownerName = "";
	private static String reportID = "";
	private static java.sql.Date finalReportDate;
	
	private static void getReportInfo (Connection connection) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int intReportID = 0;
		String repID = "";
		
		Date reportDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date repDate = null;
		try {
			repDate = format.parse(format.format(reportDate));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		finalReportDate = new java.sql.Date(repDate.getTime());
		
		String localQueryString = "SELECT report_id FROM sales_report ORDER BY length(report_id), report_id ASC";
		
		try {
			 ps = connection.prepareStatement(localQueryString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		}
		catch (Exception e) {
			 System.out.println("Get reportID prepare statement failure.");
			 e.printStackTrace();
		}
		
		try {
			 rs = ps.executeQuery();
			 if (rs.last() == true) {
				 repID = rs.getString("report_id");
				 repID = repID.replaceAll("R", "");
				 intReportID = Integer.parseInt(repID);
				 intReportID++;
			 }
			 else {
				 intReportID = 1;
			 }

		 }
		 catch (Exception e) {
			 System.out.println("Get reportID Execute Query statement failure.");
			 e.printStackTrace();
		 }
		 
		reportID = "R" + Integer.toString(intReportID);
	}
	
	private static void insertIntoTables(Connection connection, String reportType) {
		
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		
		String localQueryString = "insert into sales_report (report_id, date_of_report, type_of_report)"
				 + "values (?, ?, ?)";
		String localQueryString2 = "insert into accesses (owner_id, report_id)"
				 + "values (?, ?)";
		String localQueryString3 = "select * from owner where owner_id = ?";
		
		try {
			 ps = connection.prepareStatement(localQueryString);
			 ps.setString(1, reportID);
			 ps.setDate(2, finalReportDate);
			 ps.setString(3, reportType); 
			 
			 ps2 = connection.prepareStatement(localQueryString2);
			 ps2.setString(1, ownerID);
			 ps2.setString(2, reportID);
			 
			 ps3 = connection.prepareStatement(localQueryString3);
			 ps3.setString(1, ownerID);
		}
		catch (Exception e) {
			 System.out.println("Insert Report and Accesses prepare statement failure.");
			 e.printStackTrace();
		}
		
		try {
			ps.executeUpdate();
			ps2.executeUpdate();
			
			rs3 = ps3.executeQuery();
			rs3.next();
			String fName = rs3.getString("first_name");
			String lName = rs3.getString("last_name");
			ownerName = fName + " " + lName;
			
		}
		catch (Exception e) {
			 System.out.println("Insert Report and Accesses execute statement failure.");
			 e.printStackTrace();
		}
		
	}
	
	private static void createReportByTitle(Connection connection, String reportType) {
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		
		String localQueryString = "SELECT * FROM consists NATURAL JOIN order_placement NATURAL JOIN book_collection NATURAL JOIN writes NATURAL JOIN publisher ORDER BY title";
		String localQueryString2 = "insert into contains (report_id, order_item_id, order_id) values(?, ?, ?)";
		String currentTitle = "";
		String stickingTitle = "";
		String isbnNum = "";
		String orderID = "";
		String itemID = "";
		
		int numItemsSold = 0;
		int totalNumItemsSold = 0;
		float itemPrice = 0;
		float totalSalesofItem = 0;
		float totalSalesofReport = 0;
		
		Calendar orderCal = Calendar.getInstance();
		Calendar currentCal = Calendar.getInstance();
		
		Date orderDate = null;
		
		try {
			 ps = connection.prepareStatement(localQueryString);
			 
		}
		catch (Exception e) {
			 System.out.println("Get Language Report Info prepare statement failure.");
			 e.printStackTrace();
		}
		
		try {
			rs = ps.executeQuery();
			
			System.out.printf("\nSales Report: %s", reportID);
			System.out.printf("\nType of Report: %s", reportType);
			System.out.printf("\nRequested by Owner: %s (Owner ID: %s)", ownerName, ownerID);
			System.out.printf("\nRequested on: %s\n", finalReportDate.toString());
			
			while (rs.next()) {
				
				orderDate = rs.getDate("date_of_order");
				orderCal.setTime(orderDate);
				currentCal.setTime(new Date());

				if(orderCal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR)) {
				    if(orderCal.get(Calendar.MONTH) == currentCal.get(Calendar.MONTH)) {
				    	currentTitle = rs.getString("title");
						
						numItemsSold = rs.getInt("item_quantity");
						itemPrice = rs.getFloat("price");
						
						if (!stickingTitle.equals(currentTitle) || rs.isLast()) {
							if (rs.isLast()) {
								int tempTotalItemsSold = totalNumItemsSold + numItemsSold;
								float tempTotalSalesItem = totalSalesofItem + (numItemsSold * itemPrice);
								
								System.out.printf("\n\nBook %s", stickingTitle);
								System.out.printf("\nISBN Num: %s", isbnNum);
								System.out.printf("\nTotal Number of Books Sold: %d", tempTotalItemsSold);
								System.out.printf("\nTotal Sales: $%.2f", tempTotalSalesItem);
								totalSalesofReport = totalSalesofReport + tempTotalSalesItem;
							}
							else if (!stickingTitle.equals("")) {
								System.out.printf("\n\nBook %s", stickingTitle);
								System.out.printf("\nISBN Num: %s", isbnNum);
								System.out.printf("\nTotal Number of Books Sold: %d", totalNumItemsSold);
								System.out.printf("\nTotal Sales: $%.2f", totalSalesofItem);
								
								totalSalesofReport = totalSalesofReport + totalSalesofItem;
								totalSalesofItem = 0;
								totalNumItemsSold = 0;
							}
							stickingTitle = currentTitle;	
						}
						
						isbnNum = rs.getString("isbn_number");
						totalSalesofItem = totalSalesofItem + (numItemsSold * itemPrice);
						totalNumItemsSold = totalNumItemsSold + numItemsSold;
						
						orderID = rs.getString("order_id");
						itemID = rs.getString("order_item_id");
						
						ps2 = connection.prepareStatement(localQueryString2);
						ps2.setString(1, reportID);
						ps2.setString(2, itemID);
						ps2.setString(3, orderID);
						
						ps2.executeUpdate();
						
				    }
				}		
			}
			
			System.out.printf("\n\nFinal Conclusion => Total Sales this month: $%.2f\n\n", totalSalesofReport);
		}
		catch (Exception e) {
			 System.out.println("Get Language Report Info execute statement failure.");
			 e.printStackTrace();
		}
	}
	
	private static void createReportByAuthor(Connection connection, String reportType) {
		
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		
		String localQueryString = "SELECT * FROM consists NATURAL JOIN order_placement NATURAL JOIN book_collection NATURAL JOIN writes NATURAL JOIN publisher ORDER BY author_last_name";
		String localQueryString2 = "insert into contains (report_id, order_item_id, order_id) values(?, ?, ?)";
		String currentAuthor = "";
		String stickingAuthor = "";
		String fName = "";
		String lName = "";
		String orderID = "";
		String itemID = "";
		
		int numItemsSold = 0;
		int totalNumItemsSold = 0;
		float itemPrice = 0;
		float totalSalesofItem = 0;
		float totalSalesofReport = 0;
		
		Calendar orderCal = Calendar.getInstance();
		Calendar currentCal = Calendar.getInstance();
		
		Date orderDate = null;
		
		try {
			 ps = connection.prepareStatement(localQueryString);
			 
		}
		catch (Exception e) {
			 System.out.println("Get Author Report Info prepare statement failure.");
			 e.printStackTrace();
		}
		
		try {
			rs = ps.executeQuery();
			
			System.out.printf("\nSales Report: %s", reportID);
			System.out.printf("\nType of Report: %s", reportType);
			System.out.printf("\nRequested by Owner: %s (Owner ID: %s)", ownerName, ownerID);
			System.out.printf("\nRequested on: %s\n", finalReportDate.toString());
			
			while (rs.next()) {
				
				orderDate = rs.getDate("date_of_order");
				orderCal.setTime(orderDate);
				currentCal.setTime(new Date());

				if(orderCal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR)) {
				    if(orderCal.get(Calendar.MONTH) == currentCal.get(Calendar.MONTH)) {
				    	fName = rs.getString("author_first_name");
						lName = rs.getString("author_last_name");
						
						currentAuthor = fName + " " + lName;
						
						numItemsSold = rs.getInt("item_quantity");
						itemPrice = rs.getFloat("price");
						
						if (!stickingAuthor.equals(currentAuthor) || rs.isLast()) {
							if (rs.isLast()) {
								int tempTotalItemsSold = totalNumItemsSold + numItemsSold;
								float tempTotalSalesItem = totalSalesofItem + (numItemsSold * itemPrice);
								
								System.out.printf("\n\nAuthor: %s", stickingAuthor);
								System.out.printf("\nTotal Number of Books Sold under Author's Name: %d", tempTotalItemsSold);
								System.out.printf("\nTotal Sales: $%.2f", tempTotalSalesItem);
								totalSalesofReport = totalSalesofReport + tempTotalSalesItem;
							}
							else if (!stickingAuthor.equals("")) {
								System.out.printf("\n\nAuthor: %s", stickingAuthor);
								System.out.printf("\nTotal Number of Books Sold under Author's Name: %d", totalNumItemsSold);
								System.out.printf("\nTotal Number of Books Sold: %d", totalNumItemsSold);
								System.out.printf("\nTotal Sales: $%.2f", totalSalesofItem);
								
								totalSalesofReport = totalSalesofReport + totalSalesofItem;
								totalSalesofItem = 0;
								totalNumItemsSold = 0;
							}
							stickingAuthor = currentAuthor;	
						}
						
						totalSalesofItem = totalSalesofItem + (numItemsSold * itemPrice);
						totalNumItemsSold = totalNumItemsSold + numItemsSold;
						
						orderID = rs.getString("order_id");
						itemID = rs.getString("order_item_id");
						
						ps2 = connection.prepareStatement(localQueryString2);
						ps2.setString(1, reportID);
						ps2.setString(2, itemID);
						ps2.setString(3, orderID);
						
						ps2.executeUpdate();
				    }
				}		
			}
			
			System.out.printf("\n\nFinal Conclusion => Total Sales this month: $%.2f\n\n", totalSalesofReport);
		}
		catch (Exception e) {
			 System.out.println("Get Author Report Info execute statement failure.");
			 e.printStackTrace();
		}
		
	}
	
	private static void createReportByGenre (Connection connection, String reportType) {
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		
		
		String localQueryString = "SELECT * FROM consists NATURAL JOIN order_placement NATURAL JOIN book_collection NATURAL JOIN writes NATURAL JOIN publisher ORDER BY genre";
		String localQueryString2 = "insert into contains (report_id, order_item_id, order_id) values(?, ?, ?)";
		String currentGenre = "";
		String stickingGenre = "";
		String orderID = "";
		String itemID = "";
		
		int numItemsSold = 0;
		int totalNumItemsSold = 0;
		float itemPrice = 0;
		float totalSalesofItem = 0;
		float totalSalesofReport = 0;
		
		Calendar orderCal = Calendar.getInstance();
		Calendar currentCal = Calendar.getInstance();
		
		Date orderDate = null;
		
		try {
			 ps = connection.prepareStatement(localQueryString);
			 
		}
		catch (Exception e) {
			 System.out.println("Get Genre Report Info prepare statement failure.");
			 e.printStackTrace();
		}
		
		try {
			rs = ps.executeQuery();
			
			System.out.printf("\nSales Report: %s", reportID);
			System.out.printf("\nType of Report: %s", reportType);
			System.out.printf("\nRequested by Owner: %s (Owner ID: %s)", ownerName, ownerID);
			System.out.printf("\nRequested on: %s\n", finalReportDate.toString());
			
			while (rs.next()) {
				
				orderDate = rs.getDate("date_of_order");
				orderCal.setTime(orderDate);
				currentCal.setTime(new Date());

				if(orderCal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR)) {
				    if(orderCal.get(Calendar.MONTH) == currentCal.get(Calendar.MONTH)) {
				    	currentGenre = rs.getString("genre");
						
						numItemsSold = rs.getInt("item_quantity");
						itemPrice = rs.getFloat("price");
						
						if (!stickingGenre.equals(currentGenre) || rs.isLast()) {
							if (rs.isLast()) {
								int tempTotalItemsSold = totalNumItemsSold + numItemsSold;
								float tempTotalSalesItem = totalSalesofItem + (numItemsSold * itemPrice);
								
								System.out.printf("\n\nMain Genre: %s", stickingGenre);
								System.out.printf("\nTotal Number of Books Sold under Genre: %d", tempTotalItemsSold);
								System.out.printf("\nTotal Sales: $%.2f", tempTotalSalesItem);
								totalSalesofReport = totalSalesofReport + tempTotalSalesItem;
							}
							else if (!stickingGenre.equals("")) {
								System.out.printf("\n\nMain Genre: %s", stickingGenre);
								System.out.printf("\nTotal Number of Books Sold under Genre: %d", totalNumItemsSold);
								System.out.printf("\nTotal Sales: $%.2f", totalSalesofItem);
								
								totalSalesofReport = totalSalesofReport + totalSalesofItem;
								totalSalesofItem = 0;
								totalNumItemsSold = 0;
							}
							stickingGenre = currentGenre;	
						}
						
						totalSalesofItem = totalSalesofItem + (numItemsSold * itemPrice);
						totalNumItemsSold = totalNumItemsSold + numItemsSold;
						
						orderID = rs.getString("order_id");
						itemID = rs.getString("order_item_id");
						
						ps2 = connection.prepareStatement(localQueryString2);
						ps2.setString(1, reportID);
						ps2.setString(2, itemID);
						ps2.setString(3, orderID);
						
						ps2.executeUpdate();
				    }
				}		
			}
			
			System.out.printf("\n\nFinal Conclusion => Total Sales this month: $%.2f\n\n", totalSalesofReport);
		}
		catch (Exception e) {
			 System.out.println("Get Genre Report Info execute statement failure.");
			 e.printStackTrace();
		}
	}
	
	private static void createReportByLanguage (Connection connection, String reportType) {
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		
		String localQueryString = "SELECT * FROM consists NATURAL JOIN order_placement NATURAL JOIN book_collection NATURAL JOIN writes NATURAL JOIN publisher ORDER BY language";
		String localQueryString2 = "insert into contains (report_id, order_item_id, order_id) values(?, ?, ?)";
		String currentLanguage = "";
		String stickingLanguage = "";
		String orderID = "";
		String itemID = "";
		
		int numItemsSold = 0;
		int totalNumItemsSold = 0;
		float itemPrice = 0;
		float totalSalesofItem = 0;
		float totalSalesofReport = 0;
		
		Calendar orderCal = Calendar.getInstance();
		Calendar currentCal = Calendar.getInstance();
		
		Date orderDate = null;
		
		try {
			 ps = connection.prepareStatement(localQueryString);
			 
		}
		catch (Exception e) {
			 System.out.println("Get Language Report Info prepare statement failure.");
			 e.printStackTrace();
		}
		
		try {
			rs = ps.executeQuery();
			
			System.out.printf("\nSales Report: %s", reportID);
			System.out.printf("\nType of Report: %s", reportType);
			System.out.printf("\nRequested by Owner: %s (Owner ID: %s)", ownerName, ownerID);
			System.out.printf("\nRequested on: %s\n", finalReportDate.toString());
			
			while (rs.next()) {
				
				orderDate = rs.getDate("date_of_order");
				orderCal.setTime(orderDate);
				currentCal.setTime(new Date());

				if(orderCal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR)) {
				    if(orderCal.get(Calendar.MONTH) == currentCal.get(Calendar.MONTH)) {
				    	currentLanguage = rs.getString("language");
						
						numItemsSold = rs.getInt("item_quantity");
						itemPrice = rs.getFloat("price");
						
						if (!stickingLanguage.equals(currentLanguage) || rs.isLast()) {
							if (rs.isLast()) {
								int tempTotalItemsSold = totalNumItemsSold + numItemsSold;
								float tempTotalSalesItem = totalSalesofItem + (numItemsSold * itemPrice);
								
								System.out.printf("\n\nMain Language: %s",stickingLanguage);
								System.out.printf("\nTotal Number of Books Sold written in this Language: %d", tempTotalItemsSold);
								System.out.printf("\nTotal Sales: $%.2f", tempTotalSalesItem);
								totalSalesofReport = totalSalesofReport + tempTotalSalesItem;
							}
							else if (!stickingLanguage.equals("")) {
								System.out.printf("\n\nMain Language: %s",stickingLanguage);
								System.out.printf("\nTotal Number of Books Sold written in this Language: %d", totalNumItemsSold);
								System.out.printf("\nTotal Sales: $%.2f", totalSalesofItem);
								
								totalSalesofReport = totalSalesofReport + totalSalesofItem;
								totalSalesofItem = 0;
								totalNumItemsSold = 0;
							}
							stickingLanguage = currentLanguage;	
						}
						
						totalSalesofItem = totalSalesofItem + (numItemsSold * itemPrice);
						totalNumItemsSold = totalNumItemsSold + numItemsSold;
						
						orderID = rs.getString("order_id");
						itemID = rs.getString("order_item_id");
						
						ps2 = connection.prepareStatement(localQueryString2);
						ps2.setString(1, reportID);
						ps2.setString(2, itemID);
						ps2.setString(3, orderID);
						
						ps2.executeUpdate();
				    }
				}		
			}
			
			System.out.printf("\n\nFinal Conclusion => Total Sales this month: $%.2f\n\n", totalSalesofReport);
		}
		catch (Exception e) {
			 System.out.println("Get Language Report Info execute statement failure.");
			 e.printStackTrace();
		}
	}
	
	private static void createReportByPublisher (Connection connection, String reportType) {
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		
		String localQueryString = "SELECT * FROM consists NATURAL JOIN order_placement NATURAL JOIN book_collection NATURAL JOIN writes NATURAL JOIN publisher ORDER BY last_name";
		String localQueryString2 = "insert into contains (report_id, order_item_id, order_id) values(?, ?, ?)";
		String currentPublisher = "";
		String stickingPublisher = "";
		String fName = "";
		String lName = "";
		String orderID = "";
		String itemID = "";
		
		int numItemsSold = 0;
		int totalNumItemsSold = 0;
		float itemPrice = 0;
		float totalSalesofItem = 0;
		float totalSalesofReport = 0;
		
		Calendar orderCal = Calendar.getInstance();
		Calendar currentCal = Calendar.getInstance();
		
		Date orderDate = null;
		
		try {
			 ps = connection.prepareStatement(localQueryString);
			 
		}
		catch (Exception e) {
			 System.out.println("Get Publisher Report Info prepare statement failure.");
			 e.printStackTrace();
		}
		
		try {
			rs = ps.executeQuery();
			
			System.out.printf("\nSales Report: %s", reportID);
			System.out.printf("\nType of Report: %s", reportType);
			System.out.printf("\nRequested by Owner: %s (Owner ID: %s)", ownerName, ownerID);
			System.out.printf("\nRequested on: %s\n", finalReportDate.toString());
			
			while (rs.next()) {
				
				orderDate = rs.getDate("date_of_order");
				orderCal.setTime(orderDate);
				currentCal.setTime(new Date());

				if(orderCal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR)) {
				    if(orderCal.get(Calendar.MONTH) == currentCal.get(Calendar.MONTH)) {
				    	fName = rs.getString("first_name");
						lName = rs.getString("last_name");
						
						currentPublisher = fName + " " + lName;
						
						numItemsSold = rs.getInt("item_quantity");
						itemPrice = rs.getFloat("price");
						
						if (!stickingPublisher.equals(currentPublisher) || rs.isLast()) {
							if (rs.isLast()) {
								int tempTotalItemsSold = totalNumItemsSold + numItemsSold;
								float tempTotalSalesItem = totalSalesofItem + (numItemsSold * itemPrice);
								
								System.out.printf("\n\nAuthor: %s", stickingPublisher);
								System.out.printf("\nTotal Number of Books Sold under Publisher's Name: %d", tempTotalItemsSold);
								System.out.printf("\nTotal Sales: $%.2f", tempTotalSalesItem);
								totalSalesofReport = totalSalesofReport + tempTotalSalesItem;
							}
							else if (!stickingPublisher.equals("")) {
								System.out.printf("\n\nAuthor: %s", stickingPublisher);
								System.out.printf("\nTotal Number of Books Sold under Publisher's Name: %d", totalNumItemsSold);
								System.out.printf("\nTotal Number of Books Sold: %d", totalNumItemsSold);
								System.out.printf("\nTotal Sales: $%.2f", totalSalesofItem);
								
								totalSalesofReport = totalSalesofReport + totalSalesofItem;
								totalSalesofItem = 0;
								totalNumItemsSold = 0;
							}
							stickingPublisher = currentPublisher;	
						}
						
						totalSalesofItem = totalSalesofItem + (numItemsSold * itemPrice);
						totalNumItemsSold = totalNumItemsSold + numItemsSold;
						
						orderID = rs.getString("order_id");
						itemID = rs.getString("order_item_id");
						
						ps2 = connection.prepareStatement(localQueryString2);
						ps2.setString(1, reportID);
						ps2.setString(2, itemID);
						ps2.setString(3, orderID);
						
						ps2.executeUpdate();
				    }
				}		
			}
			
			System.out.printf("\n\nFinal Conclusion => Total Sales this month: $%.2f\n\n", totalSalesofReport);
		}
		catch (Exception e) {
			 System.out.println("Get Publisher Report Info execute statement failure.");
			 e.printStackTrace();
		}
		
	}
	
	public static void createReportInterface (Connection connection, String ownID) {
		
		ownerID = ownID;
		
		boolean checkCondition = false;
		Scanner keyboard = new Scanner (System.in);
		System.out.println("\nCreating new report...");
		
		System.out.println("\nWhat kind of report would you like to create that will cover the current month?\n");
		while (checkCondition == false) {
			System.out.println("(1) Sales by Title (2) Sales by Author (3) Sales by Genre");
			System.out.println("(4) Sales by Language (5) Sales by Publisher (6) Exit Report Menu");
			String response = keyboard.nextLine();
			
			switch (response) {
			case("1"):
				getReportInfo(connection);
				response = "SALES BY TITLE";
				insertIntoTables(connection, response);
				createReportByTitle(connection, response);
				break;
			case("2"):
				getReportInfo(connection);
				response = "SALES BY AUTHOR";
				insertIntoTables(connection, response);
				createReportByAuthor(connection, response);
				break;
			case("3"):
				getReportInfo(connection);
				response = "SALES BY GENRE";
				insertIntoTables(connection, response);
				createReportByGenre(connection, response);
				break;
			case("4"):
				getReportInfo(connection);
				response = "SALES BY LANGUAGE";
				insertIntoTables(connection, response);
				createReportByLanguage(connection, response);
				break;
			case("5"):
				getReportInfo(connection);
				response = "SALES BY PUBLISHER";
				insertIntoTables(connection, response);
				createReportByPublisher(connection, response);
				break;
			case("6"):
				checkCondition = true;
			System.out.println("Exiting Report Menu...\n");
				break;
			default:
				System.out.println("\nInvalid input. Please enter a valid menu option.\ns");
			}
		}
		

	}
}
