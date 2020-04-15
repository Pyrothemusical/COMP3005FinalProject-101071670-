import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

public class BookCollectionSearch {
	
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
	
	private static void executeSearch(PreparedStatement ps, ArrayList<BookInformation> bookCart) {
		
		ResultSet resultSet = null;
		
		try {
			resultSet = ps.executeQuery();
			
			if (resultSet.next() == true) {
				BookInformation book = new BookInformation();
				
				book.authorFName = resultSet.getString("author_first_name");
				book.authorLName = resultSet.getString("author_last_name");
				book.genre = resultSet.getString("genre");
				book.isbnNum = resultSet.getString("isbn_number");
				book.language = resultSet.getString("language");
				book.price = resultSet.getFloat("price");
				book.pubEarn = resultSet.getFloat("publisher_earnings");
				book.pubFName = resultSet.getString("first_name");
				book.pubLName = resultSet.getString("last_name");
				book.pubID = resultSet.getString("publisher_id");
				book.title = resultSet.getString("title");
				book.wareName = resultSet.getString("warehouse_name");
				book.numBooksStored = resultSet.getInt("quantity");
				
				bookCart.add(book);
				
				while (resultSet.next() == true) {
					
					BookInformation newBook = new BookInformation();
					
					newBook.authorFName = resultSet.getString("author_first_name");
					newBook.authorLName = resultSet.getString("author_last_name");
					newBook.genre = resultSet.getString("genre");
					newBook.isbnNum = resultSet.getString("isbn_number");
					newBook.language = resultSet.getString("language");
					newBook.price = resultSet.getFloat("price");
					newBook.pubEarn = resultSet.getFloat("publisher_earnings");
					newBook.pubFName = resultSet.getString("first_name");
					newBook.pubLName = resultSet.getString("last_name");
					newBook.pubID = resultSet.getString("publisher_id");
					newBook.title = resultSet.getString("title");
					newBook.wareName = resultSet.getString("warehouse_name");
					newBook.numBooksStored = resultSet.getInt("quantity");
					
					bookCart.add(newBook);
				}
				
			}
			else {
				System.out.println("\nSearching showed no results. Please ensure that you correctly inputted your keyword.");
			}
		}
		catch (Exception e) {
			 System.out.println("Search Book Execute Query statement failure.");
			 e.printStackTrace();
		}
		
		System.out.println();
	}
	
	private static void printResults(ArrayList<BookInformation> bookCart) {
		
		for (int i = 0; i < bookCart.size(); i++) {
			String authorName = bookCart.get(i).authorFName + " " + bookCart.get(i).authorLName;
			String pubName = bookCart.get(i).pubFName + " " + bookCart.get(i).pubLName;

			System.out.printf("\nISBN Number: %-10.30s  Book Title: %-10.30s   Author: %-10.30s%n", 
					bookCart.get(i).isbnNum, bookCart.get(i).title, authorName);

			System.out.printf("Publisher: %-10.30s Genre: %-10.30s  Language: %-10.30s  Price: $%.2f\n", 
			pubName, bookCart.get(i).genre, bookCart.get(i).language, bookCart.get(i).price);
		}
		
		System.out.println();
	}
	
	public static void searchTitle(Connection connection, ArrayList<BookInformation> bookCart) {
		Scanner keyboard = new Scanner(System.in);
		String localQueryString = "";
		
		PreparedStatement ps = null;
		
		localQueryString = "SELECT * FROM writes NATURAL JOIN book_collection NATURAL JOIN publisher WHERE title LIKE '%' || ? || '%'";
		
		System.out.println("\nSearching book by title...");
		System.out.println("Please enter the title or keyword of the book you wish to search:");
		
		String titleSearch = keyboard.nextLine();
		titleSearch = titleSearch.toUpperCase();
		
		try {
			ps = connection.prepareStatement(localQueryString);
			ps.setString(1, titleSearch);
		}
		catch (Exception e) {
			System.out.println("Search Title prepare statement failure.");
			e.printStackTrace();
		}
		
		executeSearch(ps, bookCart);
	}
	
	public static void searchISBN(Connection connection, ArrayList<BookInformation> bookCart) {
		Scanner keyboard = new Scanner(System.in);
		String localQueryString = "";
		
		PreparedStatement ps = null;
		
		localQueryString = "SELECT * FROM writes NATURAL JOIN book_collection NATURAL JOIN publisher WHERE isbn_number LIKE '%' || ? || '%'";
		
		System.out.println("\nSearching book by ISBN Number...");
		System.out.println("Please enter the ISBN Number of the book you wish to search (XXX-X-XXXX-XXXX-X):");
		
		String isbnNum = keyboard.nextLine();
		String checkISBNNum = isbnNum.replaceAll( "-", "" );
		
		if (isValidISBN(checkISBNNum)) {
			try {
				ps = connection.prepareStatement(localQueryString);
				ps.setString(1, isbnNum);
			}
			catch (Exception e) {
				System.out.println("Search ISBN Num prepare statement failure.");
				e.printStackTrace();
			}
			
			executeSearch(ps, bookCart);
		}
		else {
			System.out.println("This is not a valid ISBN Number. Please attempt to input a correctly formatted ISBN Number.\n");
		}
		
	}
	
	public static void searchAuthor(Connection connection, ArrayList<BookInformation> bookCart) {
		Scanner keyboard = new Scanner(System.in);
		String localQueryString = "";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		localQueryString = "SELECT * FROM writes NATURAL JOIN book_collection NATURAL JOIN publisher WHERE author_last_name LIKE '%' || ? || '%'";
		
		System.out.println("\nSearching book by author...");
		System.out.println("Please enter the author's last name of the book you wish to search:");
		
		String authorSearch = keyboard.nextLine();
		authorSearch = authorSearch.toUpperCase();
		
		try {
			ps = connection.prepareStatement(localQueryString);
			ps.setString(1, authorSearch);
		}
		catch (Exception e) {
			System.out.println("Search Author prepare statement failure.");
			e.printStackTrace();
		}
		
		try {
			rs = ps.executeQuery();
			
			if (rs.next()) {	
				executeSearch(ps, bookCart);
			}
			else {
				System.out.println("Did not find any authors in the book collection.");
				System.out.println("Please ensure that you submitted the correct author's last name.\n");
			}
		}
		catch (Exception e) {
			System.out.println("Search Author execute statement failure.");
			e.printStackTrace();
		}
		
		
	}
	
	public static void searchGenre(Connection connection, ArrayList<BookInformation> bookCart) {
		Scanner keyboard = new Scanner(System.in);
		String localQueryString = "";
		
		PreparedStatement ps = null;
		
		localQueryString = "SELECT * FROM writes NATURAL JOIN book_collection NATURAL JOIN publisher WHERE genre LIKE '%' || ? || '%'";
		
		System.out.println("\nSearching book by genre...");
		System.out.println("Please enter the genre of the book you wish to search:");
		
		String genreSearch = keyboard.nextLine();
		genreSearch = genreSearch.toUpperCase();
		
		try {
			ps = connection.prepareStatement(localQueryString);
			ps.setString(1, genreSearch);
		}
		catch (Exception e) {
			System.out.println("Search Genre prepare statement failure.");
			e.printStackTrace();
		}
		
		executeSearch(ps, bookCart);
	}
	
	public static void searchLanguage(Connection connection, ArrayList<BookInformation> bookCart) {
		Scanner keyboard = new Scanner(System.in);
		String localQueryString = "";
		
		PreparedStatement ps = null;
		
		localQueryString = "SELECT * FROM writes NATURAL JOIN book_collection NATURAL JOIN publisher WHERE language LIKE '%' || ? || '%'";
		
		System.out.println("\nSearching book by language...");
		System.out.println("Please enter the language of the book you wish to search:");
		
		String langSearch = keyboard.nextLine();
		langSearch = langSearch.toUpperCase();
		
		try {
			ps = connection.prepareStatement(localQueryString);
			ps.setString(1, langSearch);
		}
		catch (Exception e) {
			System.out.println("Search Language prepare statement failure.");
			e.printStackTrace();
		}
		
		executeSearch(ps, bookCart);
	}
	
	public static void searchPublisher(Connection connection, ArrayList<BookInformation> bookCart) {
		Scanner keyboard = new Scanner(System.in);
		String localQueryString = "";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		localQueryString = "SELECT * FROM writes NATURAL JOIN book_collection NATURAL JOIN publisher WHERE last_name LIKE '%' || ? || '%'";
		
		System.out.println("\nSearching book by publisher...");
		System.out.println("Please enter the publisher's last name of the book you wish to search:");
		
		String pubSearch = keyboard.nextLine();
		pubSearch = pubSearch.toUpperCase();
		
		try {
			ps = connection.prepareStatement(localQueryString);
			ps.setString(1, pubSearch);
		}
		catch (Exception e) {
			System.out.println("Search Publisher prepare statement failure.");
			e.printStackTrace();
		}
		
		try {
			rs = ps.executeQuery();
			
			if (rs.next()) {	
				executeSearch(ps, bookCart);
			}
			else {
				System.out.println("Did not find any publishers in the book collection.");
				System.out.println("Please ensure that you submitted the correct publisher's last name.\n");
			}
		}
		catch (Exception e) {
			System.out.println("Search Publisher execute statement failure.");
			e.printStackTrace();
		}
	}
	
	public static void searchForBook(Connection connection) {
		System.out.println("\n\nAttempting to search for a book...");
		System.out.println("How would you like to search for the book?");
		
		boolean checkCondition = false;
		Scanner keyboard = new Scanner(System.in);
		
		ArrayList<BookInformation> bookCart = new ArrayList<BookInformation>();
		
		while (checkCondition == false) {
			
			bookCart.clear();
			
			System.out.println("(1) Search by Title (2) Search by ISBN Number (3) Search by Author (Last Name)");
			System.out.println("(4) Search by Genre (5) Search by Language (6) Search by Publisher (Last Name) (7) Exit Menu");
			
			String menuOption = keyboard.nextLine();
			
			switch (menuOption) {
				case("1"):
					searchTitle(connection, bookCart);
					if (bookCart.size() != 0) {
						printResults(bookCart);
					}
					break;
				case("2"):
					searchISBN(connection, bookCart);
					if (bookCart.size() != 0) {
						printResults(bookCart);
					}
					break;
				case("3"):
					searchAuthor(connection, bookCart);
					if (bookCart.size() != 0) {
						printResults(bookCart);
					}
					break;
				case("4"):
					searchGenre(connection, bookCart);
					if (bookCart.size() != 0) {
						printResults(bookCart);
					}
					break;
				case("5"):
					searchLanguage(connection, bookCart);
					if (bookCart.size() != 0) {
						printResults(bookCart);
					}
					break;
				case("6"):
					searchPublisher(connection, bookCart);
					if (bookCart.size() != 0) {
						printResults(bookCart);
					}
					break;
				case("7"):
					System.out.println("\nExiting menu of search options. Returning to options menu...");
					checkCondition = true;
					break;
				default:
					System.out.println("\nInvalid input. Please enter a menu input option.");
			}
		}
	}

}
