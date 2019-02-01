package logic;

import java.util.ArrayList;

public class SearchBookController  {


	public static void searchBook(String searchPick,String data)
	{
		ArrayList<String> searchBook = new ArrayList<>();
		searchBook.add("Search book");
		searchBook.add(searchPick);
		searchBook.add(data);
		Main.client.handleMessageFromClientUI(searchBook);
	}

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
