package logic;
import java.util.ArrayList;


/**
 * This class connect between the input in the GUI to the server.
 * The class giving the functionality to add/edit/remove book by is name and by author name.
 */
public class InventoryController {
	
	
	
	/**
	 * This method gives the functionality of adding a book to the inventory.
	 * @param bookname - book's name.
		* @param edition - book's edition.
		* @param theme - book's theme.
	 * @param author - book's author  name.
	 * @param printdate - book's printdate. 
	 * @param copies - book's quantity of copies.
	 * @param purchasedate - book's purchase date.
	 * @param shelflocation - book's shelf location.
	 * @param wanted - describes if the book is wanted or not.
	 * @param description - book's description.
	 */
	public  static void addBook(String bookname,String edition,String theme,String author,String printdate, String copies, String purchasedate, String shelflocation, String wanted, String description)
	{
		ArrayList<String> inventoryData = new ArrayList<>();
		inventoryData.add("AddBook");
		inventoryData.add(bookname);
		inventoryData.add(shelflocation);
		inventoryData.add(wanted);
		inventoryData.add(author);
		inventoryData.add(edition);
		inventoryData.add(printdate);
		inventoryData.add(theme);
		inventoryData.add(description);
		inventoryData.add(purchasedate);
		Main.client.handleMessageFromClientUI(inventoryData);
	}

	/**
	 * This method gives the functionality of adding a copy to the inventory.
	 * @param copylocation - copy's shelf location.
	 * @param bookname - book's name.
	 * @param bookid - book's id.
	 */
	public static void addCopy(String copylocation,String bookname,String bookid) 
	{
		ArrayList<String> inventoryData = new ArrayList<>();
		inventoryData.add("AddCopy");
		inventoryData.add(bookname);
		inventoryData.add(copylocation);
		inventoryData.add(bookid);
		Main.client.handleMessageFromClientUI(inventoryData);
	}

	/**
	 * This method gives the functionality of removing a copy from the inventory.
	 * @param catalognumber - copy's catalog number.
	 */
	public  static void RemoveCopy(String catalognumber)
	{
		ArrayList<String> inventoryData = new ArrayList<>();
		inventoryData.add("RemoveCopy");
		inventoryData.add(catalognumber);
		Main.client.handleMessageFromClientUI(inventoryData);
	}

	/**
	 * This method gives the functionality of checking if the book exists in the library.
	 * @param msg - an array list with the book details.
	 */
	public  static void checkExistence(ArrayList<String> msg)
	{
		ArrayList<String> inventoryData = new ArrayList<>();
		inventoryData.add("InventoryCheckExistense");
		inventoryData.addAll(msg);
		Main.client.handleMessageFromClientUI(inventoryData);
	}	

	/**
	 * This method gives the functionality of checking if a copy exists in the library.
	 * @param bookid - book's id.
	 */
	public  static void checkExistenceByCopy(String bookid)
	{
		ArrayList<String> inventoryData = new ArrayList<>();
		inventoryData.add("checkExistenceByCopy");
		inventoryData.add(bookid);
		Main.client.handleMessageFromClientUI(inventoryData);
	}

	/**
	 * This method gives the functionality of removing a copy from the inventory.
		 * @param bookname - book's name.
		 * @param edition - book's edition.
		 * @param theme - book's theme.
		 * @param PDF - book's table of contents (PDF file).
		 * @param Authors - book's author name.
		 * @param location - book's location.
		 * @param description - book's description.
		 * @param wanted - if the book is wanted or not.
		 * @param purchasedate - book's purchase date.
		 * @param printdate - book's print date.
		 * @param bookid - book's id.
		*/
	public static void editCopy(String bookname,String edition,String theme,String PDF,String Authors,String location,String description,String wanted,String purchasedate,String printdate,String bookid) {
		ArrayList<String> inventoryData = new ArrayList<>();
		inventoryData.add("Edit");
		inventoryData.add(bookname);//1
		inventoryData.add(edition);//2
		inventoryData.add(theme);//3
		inventoryData.add(PDF);//4
		inventoryData.add(Authors);//5
		inventoryData.add(location);//6
		inventoryData.add(description);//7
		inventoryData.add(wanted);//8
		inventoryData.add(purchasedate);//9
		inventoryData.add(printdate);//10
		inventoryData.add(bookid);//11
		Main.client.handleMessageFromClientUI(inventoryData);
	}
}
