package logic;
import java.util.ArrayList;

public class InventoryController {

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

	public static void addCopy(String copylocation,String bookname,String bookid) 
	{
		ArrayList<String> inventoryData = new ArrayList<>();
		inventoryData.add("AddCopy");
		inventoryData.add(bookname);
		inventoryData.add(copylocation);
		inventoryData.add(bookid);
		Main.client.handleMessageFromClientUI(inventoryData);
	}

	public  static void RemoveCopy(String catalognumber)
	{
		ArrayList<String> inventoryData = new ArrayList<>();
		inventoryData.add("RemoveCopy");
		inventoryData.add(catalognumber);
		Main.client.handleMessageFromClientUI(inventoryData);
	}

	public  static void checkExistence(ArrayList<String> msg)
	{
		ArrayList<String> inventoryData = new ArrayList<>();
		inventoryData.add("InventoryCheckExistense");
		inventoryData.addAll(msg);
		Main.client.handleMessageFromClientUI(inventoryData);
	}	

	public  static void checkExistenceByCopy(String bookid)
	{
		ArrayList<String> inventoryData = new ArrayList<>();
		inventoryData.add("checkExistenceByCopy");
		inventoryData.add(bookid);
		Main.client.handleMessageFromClientUI(inventoryData);
	}

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
