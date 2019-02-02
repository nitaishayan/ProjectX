package logic;

import java.util.ArrayList;

/**
* This class is a Controller that represents the search book request.
**/
public class SearchBookController  {


	/**
	 * This method gets the search book request from the user and sends it to the server.
	 * @param searchPick - the search's option choose by the user.
	 * @param data - the search's data typed by the user.
	 */
	public static void searchBook(String searchPick,String data)
	{
		ArrayList<String> searchBook = new ArrayList<>();
		searchBook.add("Search book");
		searchBook.add(searchPick);
		searchBook.add(data);
		Main.client.handleMessageFromClientUI(searchBook);
	}

	/**
	 * This method gets the search book details request from the user and sends it to the server.
	 * @param bookID - book's id.
	 * @param bookName - book's name
	 * @param authorName - book's author name.
	 * @param description - book's description.
	 */
	public static void searchBookDetailes(String bookID, String bookName, String authorName, String description) {
		ArrayList<String> searchBookDetailes = new ArrayList<>();
		searchBookDetailes.add("SearchBookDetailes");
		searchBookDetailes.add(bookName);
		searchBookDetailes.add(authorName);
		searchBookDetailes.add(bookID);
		searchBookDetailes.add(description);
		Main.client.handleMessageFromClientUI(searchBookDetailes);
	}

}
