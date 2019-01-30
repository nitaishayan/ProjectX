package Client;

//This file contains material supporting section 3.7 of the textbook:
//"Object Oriented Software Engineering" and is issued under the open-source
//license found at www.lloseng.com 


import ocsf.client.*;
import java.awt.Desktop;
import java.io.*;
import java.util.ArrayList;
import Common.Copy;
import Common.GuiInterface;
import Common.InventoryBook;
import Common.MyFile;
import GUI.InventoryAddGUI;
import GUI.InventoryRemoveGUI;
import GUI.OBLcontroller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import logic.InventoryController;
import logic.Main;
import logic.RegistrationController;


/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class Client extends AbstractClient
{
	public static GuiInterface clientUI;
	public static  ArrayList<String> arrayUser=new ArrayList<String>();

	public Client(String host, int port,GuiInterface clientUI) {
		super(host, port);
		this.clientUI=clientUI;
		try {
			openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Instance variables **********************************************

	/**
	 * The interface type variable.  It allows the implementation of 
	 * the display method in the client.
	 */
	//	ClientGuiController clientUI; 


	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) 
	{
		System.out.println("Message received: " + msg+" receive on the client side");
		if (msg instanceof MyFile) {
			System.out.println("filee");
			getPDF(msg);
		}
		else {
			ArrayList<String> arrayObject = (ArrayList<String>)msg; //casting msg-Object to arraylist
			switch (arrayObject.get(0)) {
			case "AddBook":
				Platform.runLater(()->{
					if (arrayObject.get(arrayObject.size()-1).equals("1")) {
						InventoryAddGUI.nextBookID=arrayObject.get(arrayObject.size()-4);
						clientUI.showSuccess("Book Added successfull. \n copy id is: "+arrayObject.get(arrayObject.size()-3).toString());
						clientUI.freshStart();
					}
					else 
						clientUI.showFailed("Add failed.");
				});
				break;
			case "RemoveCopy":
				Platform.runLater(()->{
					if (arrayObject.get(arrayObject.size()-1).equals("-1"))
						clientUI.showFailed("The copy is loaned, therefore you need to return the book first\n and then remove it.");
					if (arrayObject.get(arrayObject.size()-1).equals("1"))
						clientUI.showSuccess("Copy Remove successfully");
					if (arrayObject.get(arrayObject.size()-1).equals("2")) {
						System.out.println("delete"+arrayObject);
						clientUI.showSuccess("book Remove from librariy successfully");
						File pdf=new File("./PDF/"+arrayObject.get(1));
						if(pdf.delete())
							System.out.println("File deleted successfully"); 
						else
				            System.out.println("Failed to delete the file"); 
					}
					if (!arrayObject.get(arrayObject.size()-1).equals("2")&&!arrayObject.get(arrayObject.size()-1).equals("1")) {
						clientUI.showFailed("remove failed.");
					}
				});
				break;
			case "InventoryCheckExistense":
				if (arrayObject.get(arrayObject.size()-1).equals("not exist")) {
					Platform.runLater(()->{
						clientUI.showFailed("book doesn't exist in the library.");
					});
				}
				else
					Platform.runLater(()->{
						clientUI.display(arrayObject);
					});
				break;
			case "Login":
				arrayUser.add(arrayObject.get(1));//User ID
				arrayUser.add(arrayObject.get(2));//Password
				arrayUser.add(arrayObject.get(3));//First Name
				arrayUser.add(arrayObject.get(4));//Last Name
				arrayUser.add(arrayObject.get(arrayObject.size()-1));
				System.out.println(arrayUser);
				//System.out.println((ArrayList<String>)msg+"inside Client - login");
				Platform.runLater(()->{
					//System.out.println(clientUI);
					System.out.println("lior");
					clientUI.display(arrayObject);
				});
				break;
			case "SearchMember":
				if (arrayObject.get(1).equals("NotExist")) {
					Platform.runLater(() -> {					
						clientUI.showFailed("Member does not exist in the system");
					});
				}
				else
				{
					Platform.runLater(() -> {					
						clientUI.display(arrayObject);
					});
				}
				break;
			case "Search book":
				if (arrayObject.get(3).equals("-1"))
				{
					Platform.runLater(() -> {
						clientUI.showFailed("No matches results to your search");
					});
				}
				else if (arrayObject.get(3).equals("1"))
				{
					Platform.runLater(() -> {
						clientUI.display(msg);
					});
				}
				break;
			case "Check Member Existence":
				clientUI.display(arrayObject);
				break;
			case "Check Copy Loan Status":
				clientUI.display(arrayObject);
				break;
			case "Check Copy ID Existence":
				clientUI.display(arrayObject);
				break;
			case "Return Book":
				clientUI.display(arrayObject);
				break;
			case "Registration":
				System.out.println(msg);
				if(arrayObject.get(7).equals("0"))
				{
					Platform.runLater(() -> {
						clientUI.showFailed("Some user have this ID or this phone number");
					});
				}
				else {
					Platform.runLater(() -> {
						clientUI.showSuccess("The user have been added successfully");
						clientUI.freshStart();
					});
				}
				break;
			case "AddCopy":
				if (arrayObject.get(arrayObject.size()-1).equals("success")) {
					Platform.runLater(()->{
						clientUI.showSuccess("Copy Added successfuly.   copy id is: "+arrayObject.get(arrayObject.size()-2).toString());
					});
				}else
					Platform.runLater(()->{
						clientUI.showFailed("failed to add copy");
					});

				break;
			case "checkExistenceByCopy":
				Platform.runLater(()->{
					if (arrayObject.get(arrayObject.size()-1).equals("1"))
						clientUI.display(msg);
					else 
						clientUI.showFailed("copy not exist.");
				});
				break;

			case "Check If Member Is Late On Return":
				clientUI.display(arrayObject);
				break;

			case "Change Member Status":
				clientUI.display(arrayObject);
				break;
			case "CheckLibrarianManager":
				clientUI.display(arrayObject);			
				break;
			case "Edit":
				if (arrayObject.get(arrayObject.size()-1).equals("1")) {
					Platform.runLater(()->{
						clientUI.showSuccess("details updated successfully in the system.");
						clientUI.freshStart();
					});
				}
				else {
					Platform.runLater(()->{
						clientUI.showFailed("book not updated in the system correctly.");
					});
				}
				break;
			case "SearchBookDetailes":
				Platform.runLater(()->{
					clientUI.display(msg);
				});
				break;
			case "Check Copy Wanted Status":
				clientUI.display(arrayObject);
				break;
			case "Loan Book":
				clientUI.display(arrayObject);
				break;
			case "Reserve":
				Platform.runLater(()->{
					if (arrayObject.get(arrayObject.size()-1).equals("success"))
						clientUI.showSuccess("resrve successed.");
					else 
						clientUI.showFailed("cannot order, all the copies allready reserved.");
				});
				break;
			case "ViewPersonalHistory":
				Platform.runLater(()->{
					clientUI.display(msg);
				});
				break;
			case "ReaderCard"://show reader card details for read only - tableView
				Platform.runLater(()->{
					clientUI.display(msg);
				});
				break;
			case "CurrentLoans":
				Platform.runLater(()->{
					clientUI.display(msg);
				});
				break;
			case "getDelayandLostBooks":
				ArrayList<String>listData;
				Platform.runLater(()->{
					clientUI.display(msg);
				});
				break;
			case "getStatusHistory":
				Platform.runLater(()->{
					clientUI.display(msg);
				});
				break;

			case "Extend Loan Period By Member"://show reader card details for read only - tableView
				if(((ArrayList<String>)msg).size() != 3) {
					Platform.runLater(() -> {
						clientUI.showFailed(arrayObject.get(1));
					});
				}
				else {
					Platform.runLater(() -> {
						clientUI.showSuccess("The extension preformed susccesfully and the new expected return date is " + ((ArrayList<String>)msg).get(1));
					});
				}
				break;

			case "Extend Loan Period By Librarian"://show reader card details for read only - tableView
				if(arrayObject.size() != 3) {
					clientUI.showFailed(arrayObject.get(1));
				}
				else {
					clientUI.showSuccess("The extension preformed susccesfully and the new expected return date is " + ((ArrayList<String>)msg).get(1));
				}
				break;
				
			case "EmployeeRecords"://show employee details for read only - tableView
				Platform.runLater(()->{
					clientUI.display(msg);
				});
				break;
				
			case "StatisticsBooks"://show book details - tableView
				Platform.runLater(()->{
					clientUI.display(msg);
				});
				break;
				
				
			case "showTableView"://show book details - tableView
				Platform.runLater(()->{
					clientUI.display(msg);
				});
				break;

			default:
				break;
			}
		}
	}

	public void getPDF(Object msg) {
		MyFile msg2= new MyFile(((MyFile)msg).getFileName());
		msg2=(MyFile) msg;
		String LocalfilePath="./src/Client/"+msg2.getFileName()+".pdf";
		FileOutputStream fos=null;
		BufferedOutputStream bos=null;

		try{

			fos=new FileOutputStream(LocalfilePath);
			bos=new BufferedOutputStream(fos);
			bos.write(((MyFile)msg).getMybytearray(),0,((MyFile)msg).getSize());
			bos.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(fos!=null)
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(bos!=null)
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		if (Desktop.isDesktopSupported())
			try {
				Desktop.getDesktop().open(new File(LocalfilePath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	//Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host The server to connect to.
	 * @param port The port number to connect on.
	 * @param clientUI The interface type variable.
	 */



	//Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */


	/**
	 * This method handles all data coming from the UI            
	 *
	 * @param message The message from the UI.    
	 */
	public void handleMessageFromClientUI(Object message)  
	{
		try {
			sendToServer(message);
		} catch(IOException e) {
			clientUI.showFailed("Could not send message to server. Terminating client.");
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit()
	{
		try
		{
			closeConnection();
		}
		catch(IOException e) {}
		System.exit(0);
	}
}
//End of Client class
