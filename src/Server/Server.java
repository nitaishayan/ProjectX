package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//This file contains material supporting section 3.7 of the textbook:
//"Object Oriented Software Engineering" and is issued under the open-source
//license found at www.lloseng.com 

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;


import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import Common.InventoryBook;
import Common.MyFile;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class Server extends AbstractServer 
{
	//Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	//Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public Server(int port) 
	{
		super(port);
	}

	//Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient (Object msg, ConnectionToClient client) {
		System.out.println("Message received: " + msg + " from " + client);
		if (msg instanceof MyFile) {
			System.out.println("filee");
			getPDF(msg);
		}
		else {
			ArrayList<String> arrayObject = (ArrayList<String>)msg; //casting msg-Object to arraylist
			switch (((ArrayList<String>)msg).get(0)) {
			case "Registration":
				try {
					int registrationSuccess = DBController.getInstance().registration((ArrayList<String>) msg);
					((ArrayList<String>) msg).add(Integer.toString(registrationSuccess));
					client.sendToClient(msg);
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
				break;

			case "Login":
				try {
					ArrayList<String> userDetails = DBController.getInstance().login((ArrayList<String>) msg);
					client.sendToClient(userDetails);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case "AddBook":
				try {
					int menu=DBController.getInstance().addBookToInventory((ArrayList<String>) msg);
					((ArrayList<String>)msg).add(Integer.toString(menu));
					client.sendToClient((ArrayList<String>)msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case "RemoveCopy":
				try {
					client.sendToClient(DBController.getInstance().RemoveCopy(arrayObject));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case "InventoryCheckExistense":
				try {
					client.sendToClient(DBController.getInstance().inventoryCheckExistence((ArrayList<String>) msg));
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("error");
				}
				break;
			case "Check Member Existence":
				try {
					client.sendToClient(DBController.getInstance().isMemberExist((ArrayList<String>)msg));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case "Check Copy Loan Status":
				try {
					client.sendToClient(DBController.getInstance().isCopyLoaned((ArrayList<String>)msg));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;


			case "Check Copy ID Existence":
				try {
					client.sendToClient(DBController.getInstance().isCopyExist((ArrayList<String>)msg));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case "Search book":
				try {
					ArrayList<String> answer = DBController.getInstance().searchBook((ArrayList<String>) msg);
					client.sendToClient(answer);
				}catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case "Return Book":
				try {
					client.sendToClient(DBController.getInstance().returnBook((ArrayList<String>)msg));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case "AddCopy":
				try {
					client.sendToClient(DBController.getInstance().addCopyToInventory((ArrayList<String>)msg));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case "SearchMember":
				try {
					ArrayList<String>member;
					member=(ArrayList<String>) DBController.getInstance().memberSearch((ArrayList<String>) msg);
					if (member!=null) {//found an existing member
						try {
							client.sendToClient(member);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					}
					else {
						//Could not found any existing member
						member=new ArrayList<String>();
						member.add("SearchMember");
						member.add("NotExist");
						System.out.println("did not found any memberID");
						try {
							client.sendToClient(member);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case "checkExistenceByCopy":
				try {
					System.out.println(DBController.getInstance().checkExistenceByCopy((ArrayList<String>) msg));
					client.sendToClient(DBController.getInstance().checkExistenceByCopy((ArrayList<String>) msg));
				} catch (SQLException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "SearchBookDetailes":
				try {
					ArrayList<String> answer = DBController.getInstance().searchBookDetailes((ArrayList<String>) msg);
					client.sendToClient(answer);
				} catch (SQLException  | IOException e) {
					e.printStackTrace();
				}
				break;
			case "Logout":
				try {
					System.out.println("inside server - logout");
					System.out.println(msg);
					/*client.sendToClient*/DBController.getInstance().logout((ArrayList<String>) msg);

				}
				catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case "Check If Member Is Late On Return":
				try {
					client.sendToClient(DBController.getInstance().isMemberLateOnReturn((ArrayList<String>)msg));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case "Change Member Status":
				try {
					client.sendToClient(DBController.getInstance().changeMemberStatus((ArrayList<String>)msg));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case "MemberUpdateMemberDetails":
				try {
					DBController.getInstance().MemberUpdateMemberDetails((ArrayList<String>) msg);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "CheckLibrarianManager":
				ArrayList<String> librarianData=null;
				try {
					librarianData=DBController.getInstance().CheckLibrarianManager((ArrayList<String>) msg);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					client.sendToClient(librarianData);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "Edit":
				try {
					client.sendToClient(DBController.getInstance().editBook((ArrayList<String>) msg));
				} catch (SQLException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "librarianUpdateMember":
				ArrayList<String>member=null;
				ArrayList<String>notify=null;
				try {
					notify=new ArrayList<String>();
					notify.add("SearchMember");
					member=(ArrayList<String>) DBController.getInstance().isMemberExist((ArrayList<String>) msg);
					if (member!=null) {
						DBController.getInstance().librarianUpdateMember((ArrayList<String>) msg);
						notify.add("Exist");
					}
					else {
						notify.add("NotExist");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					client.sendToClient(notify);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "Check Copy Wanted Status":
				try {
					client.sendToClient(DBController.getInstance().isCopyWanted((ArrayList<String>)msg));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case "Loan Book":
				try {
					client.sendToClient(DBController.getInstance().loanBook((ArrayList<String>)msg));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case "Reserve":
				try {
					client.sendToClient(DBController.getInstance().reserveBook((ArrayList<String>) msg));
				} catch (IOException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "ViewPersonalHistory":
				try {
					ArrayList<String>loanDetails;
					loanDetails=(ArrayList<String>) DBController.getInstance().viewPersonalHistory((ArrayList<String>) msg);
					try {
						client.sendToClient(loanDetails);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			case "ReaderCard":
				try {
					client.sendToClient(DBController.getInstance().ReaderCards());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "CurrentLoans":
				ArrayList<String> currentLoans = new ArrayList<>();
				try {
					currentLoans = DBController.getInstance().getCurrentLoans(arrayObject);
					client.sendToClient(currentLoans);
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
				break;

			case "get-pdf":
				try {
					System.out.println("pdffffff "+arrayObject.get(1));
					client.sendToClient(sendPDF(arrayObject.get(1)));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "getDelayandLostBooks":
				ArrayList<String>listData;
				try {
					listData=DBController.getInstance().getDelayandLostBooks((ArrayList<String>) msg);
					client.sendToClient(listData);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "getStatusHistory":
				ArrayList<String>listmemberStatusData;
				try {
					listmemberStatusData=DBController.getInstance().getStatusHistory((ArrayList<String>) msg);
					client.sendToClient(listmemberStatusData);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "Extend Loan Period By Member":
				try {
					client.sendToClient(DBController.getInstance().extendLoanPeriodByMember((ArrayList<String>)msg));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case "Extend Loan Period By Librarian":
				try {
					client.sendToClient(DBController.getInstance().extendLoanPeriodByLibrarian((ArrayList<String>)msg));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

				/**
				 * This case send the array list receives from the EmployeeRecords method to the client with the employees details.
				 */
			case "EmployeeRecords":
				try {
					client.sendToClient(DBController.getInstance().EmployeeRecords());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

				/**
				 * This case send the array list receives from the StatisticsShowBooks method to the client with the book details.
				 * The StatisticsShowBooks method receives two values, an option and the book id.
				 */
			case "StatisticsBooks":
				try {
					client.sendToClient(DBController.getInstance().StatisticsShowBooks(arrayObject.get(1),arrayObject.get(2)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

				/**
				 * This case send the array list receives from the showTableViewBooks method to the client with the book details for further dispalying in the table view..
				 */
			case "showTableView":
				try {
					client.sendToClient(DBController.getInstance().showTableViewBooks());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case "InBoxMessage":
				try {
					client.sendToClient(DBController.getInstance().getInBoxMessage(arrayObject));
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
				break;
			case "MemberLostBook":
				try {
					DBController.getInstance().memberLostBook(arrayObject);
				} catch (Exception e){
					e.printStackTrace();
				}
				try {
					ArrayList<String> data=new ArrayList<>();
					data.add("");
					data.add(arrayObject.get(2));
					client.sendToClient(DBController.getInstance().RemoveCopy(data));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case "getActivityReport":
				ArrayList<String> data=new ArrayList<>();
				
				try {
					data=DBController.getInstance().getActivityReport(arrayObject);
					client.sendToClient(data);
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			default:
				break;
			}
		}
	}

	public MyFile sendPDF(String bookid) {
		System.out.println(bookid);
		MyFile msg= new MyFile(bookid);
		String LocalfilePath="./PDF/"+bookid+".pdf";

		try{

			File newFile = new File (LocalfilePath);

			byte [] mybytearray  = new byte [(int)newFile.length()];
			FileInputStream fis = new FileInputStream(newFile);
			BufferedInputStream bis = new BufferedInputStream(fis);			  

			msg.initArray(mybytearray.length);
			msg.setSize(mybytearray.length);

			bis.read(msg.getMybytearray(),0,mybytearray.length);
			return msg;
		}
		catch (Exception e) {
			System.out.println("Error send (Files)msg) to Server");
		}
		return msg;
	}

	public void getPDF(Object msg) {
		MyFile msg2= new MyFile(((MyFile)msg).getFileName());
		msg2=(MyFile) msg;
		String LocalfilePath="./PDF/"+msg2.getFileName()+".pdf";
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
	}


	/**
	 * This method overrides the one in the superclass.  Called
	 * when the server starts listening for connections.
	 */
	protected void serverStarted()
	{
		System.out.println
		("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass.  Called
	 * when the server stops listening for connections.
	 */
	protected void serverStopped()
	{
		System.out.println
		("Server has stopped listening for connections.");
	}



	//Class methods ***************************************************

	/**
	 * This method is responsible for the creation of 
	 * the server instance (there is no UI in this phase).
	 *
	 * @param args[0] The port number to listen on.  Defaults to 5555 
	 *          if no argument is entered.
	 */
	public static void main(String[] args) 
	{
		int port = 0; //Port to listen on

		try
		{
			port = Integer.parseInt(args[0]); //Get port from command line
		}
		catch(Throwable t)
		{
			port = DEFAULT_PORT; //Set port to 5555
		}	
		Server sv = new Server(port);
		try 
		{
			sv.listen(); //Start listening for connections
		} 
		catch (Exception ex) 
		{
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
