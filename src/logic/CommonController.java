package logic;

import java.util.ArrayList;

import Common.Member;
import javafx.scene.control.TableColumnBase;

public class CommonController {

	private static final int ID_SIZE = 9;


	
	public static void checkMemberExistence(String memberID) throws Exception {
		if(memberID.length() == 0) {
			throw new Exception("Member ID field can't be empty");
		}	

		if(memberID.length() != ID_SIZE) {
			throw new Exception("Wrong ID size");
		}

		ArrayList<String> checkMemberExistence = new ArrayList<>();
		checkMemberExistence.add("Check Member Existence");
		checkMemberExistence.add(memberID);
		Main.client.handleMessageFromClientUI(checkMemberExistence);
	}

	/**
	 * get the memberId his previous status and his currently status, put them in an ArrayList and send them to the server
	 * @param String,String,String
	 * @return void
	 */
	public static void changeMemberStatus(String memberID, String oldStatus, String newStatus) {
		ArrayList<String> memberData = new ArrayList<>();
		memberData.add("Change Member Status");
		memberData.add(memberID);
		memberData.add(oldStatus);
		memberData.add(newStatus);
		Main.client.handleMessageFromClientUI(memberData);
	}
	
	/**
	 * Get librarian ID, insert the ID into ArrayList and send it to the server.
	 * @param String
	 * @return void
	 */
	public static void checkManager(String ID) {
		ArrayList<String> librarianData = new ArrayList<>();
		librarianData.add("CheckLibrarianManager");
		librarianData.add(ID);
		Main.client.handleMessageFromClientUI(librarianData);
	}

	/**
	 * Get the details that was insert by the librarian in the "search reader card" GUI and insert them into ArrayList, then send the ArrayList to server.
	 * @param String, String, String, String, Boolean, String
	 * @return void
	 */
	public static void librarianUpdateMember(String status, String ID, String notes, String isManager, boolean changeStatus,String prevStatus) {
		ArrayList<String> memberData = new ArrayList<>();
		if (changeStatus) {
			//if the librarian changed the status of the member 
			memberData.add("librarianUpdateMember");
			memberData.add(ID);
			memberData.add(status);
			memberData.add(notes);
			memberData.add("true");
			memberData.add(prevStatus);
        	System.out.println("Status changed to "+status+" now in common controller");
			Main.client.handleMessageFromClientUI(memberData);			
		}
		else {
			//if the librarian DID NOT changed the status of the member 
			memberData.add("librarianUpdateMember");
			memberData.add(ID);
			memberData.add(status);
			memberData.add(notes);
			memberData.add("false");
			Main.client.handleMessageFromClientUI(memberData);
			
		}

	}

	/**
	 * get string of date in the format 0000-00-00 (year-month-day) then cut the string and return ArrayList of integer that contains the date as integer.
	 * @param String
	 * @return ArrayList<Integer>[0]=year ,ArrayList<Integer>[1]=day ,ArrayList<Integer>[2]=month
	 */
	public static ArrayList<Integer> convertordate(String date) {     
		String year=(String) date.subSequence(0, 4);
		String day=(String) date.subSequence(8, 10);
		String month=(String) date.subSequence(5, 7);
		System.out.println("year-"+year +"day-"+day+"month-"+month);
		int year1,day2,month3;
		year1=Integer.parseInt(year);
		day2=Integer.parseInt(day);
		month3=Integer.parseInt(month);
		ArrayList<Integer> datearray=new ArrayList<>();
		datearray.add(year1);
		datearray.add(day2);
		datearray.add(month3);
		return datearray;
	}

	/**
	 * Get the member ID,insert the ID into ArrayList and then sent the ArrayList to the server.
	 * @param String
	 * @return void
	 */
	public static void viewPersonalHistory(String memberID) {
		ArrayList<String> memberData = new ArrayList<>();
		memberData.add("ViewPersonalHistory");
		memberData.add(memberID);
		System.out.println(memberID);
		Main.client.handleMessageFromClientUI(memberData);
	}

	/**
	 * Check for the current date and time and return it in the following format "yyyy-MM-dd HH:mm:ss".
	 * @param 
	 * @return String
	 */
	public static String getCurrentTime() {
		java.util.Date date= new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(date);
		return currentTime;
	}
	
	/**
	 * Check the propriety of the following fields:
	 * Phone number 10 digits long.
	 * Email contains '@'.
	 * ID 9 digits long.
	 * and return the specific error or 'Success' if all the fields meets the requirement.
	 * @param String, String, String
	 * @return String
	 */
	public static String checkInput(String phoneNumber,String Email,String ID) {
		System.out.println(Email);
		if (phoneNumber.length()!=10){
			System.out.println("in phoneNumber error");
			return "PhoneError";
		}
		if (!Email.contains("@")) {
			System.out.println("in email error");
			return "EmailError";
		}
		if (ID.length()>9){
			System.out.println("IDError");
			return "IDError";
		}
		else
		{
			System.out.println("Success");
			return "Success";
		}
	}	    	

	/**
	 * send to the server a request to show the reader details in the GUI 
	 * @param 
	 * @return void
	 */
	public static void ShowReaderCards () {
		ArrayList<String> ReaderCardsList = new ArrayList<>();
		ReaderCardsList.add("ReaderCard");
		Main.client.handleMessageFromClientUI(ReaderCardsList);
	}

	/**
	 * Check the propriety of the following fields: phoneNumber,email,ID,password
	 * First String is 10 digits long.
	 * Second String contains '@'.
	 * Third String is 9 digits long.
	 * Forth String is 6 digits long.
	 * Then return the specific error or 'Success' if all the fields meets the requirement.
	 * @param String, String, String,String
	 * @return String
	 */
	public static String checkRegistrationInput(String phoneNumber,String email,String ID,String password) {
		if (phoneNumber.length()!=10){
			return "The phone number isn't included 10 digits";
		}
		if (!email.contains("@")) {
			return "The email isn't included @";
		}
		if (ID.length()>9){
			return "IDError";
		}
		if (password.length()<6) {
			return "The password isn't included 6 characters";
		}
		else
		{
			return "Success";
		}
	}
	
	/**
	 * Set the MinWith, PerfWith , MaxWith  of the TableView
	 * @param TableColumnBase<S,T> , double(minWidth), double(prefWidth), double(maxWidth)
	 * @return void
	 */
	public static <S,T> void setColumnWidth(TableColumnBase<S,T> col,double minWidth,double prefWidth, double maxWidth)
	{
		col.setMinWidth(minWidth);
		col.setPrefWidth(prefWidth);
		col.setMaxWidth(maxWidth);
	}
	
	/**
	 * Send request for the server to get the Delay and LostBooks of specific member ID.
	 * the method get the member ID insert the ID into ArratList and send it to the server.
	 * @param String
	 * @return void
	 */
	public static void getDelayandLostBooks(String memberID) {
		ArrayList<String> memberData = new ArrayList<>();
		memberData.add("getDelayandLostBooks");
		memberData.add(memberID);
		Main.client.handleMessageFromClientUI(memberData);		
	}

	/**
	 * Send request for the server to get the Status History of specific member ID.
	 * Get the member ID insert the ID into ArratList and send it to the server.
	 * @param String
	 * @return void
	 */
	public static void getStatusHistory(String memberID) {
		ArrayList<String> memberData = new ArrayList<>();
		memberData.add("getStatusHistory");
		memberData.add(memberID);
		System.out.println("getStatusHistory-now in common controller");
		Main.client.handleMessageFromClientUI(memberData);
	}
	
	/**
	 * Send request for the server to get the Employee Records .
	 * @param
	 * @return void
	 */
	public static void ShowEmployeeRecords () {
		ArrayList<String> EmployeeList = new ArrayList<>();
		EmployeeList.add("EmployeeRecords");
		Main.client.handleMessageFromClientUI(EmployeeList);
	}

	/**
	 * Send request for the server to get the Inbox Message of a specific user.
	 * the method will receive an id for the user
	 * @param
	 * @return void
	 */
	public static void getMessage(String memberID) {
		ArrayList<String> messageList = new ArrayList<>();
		messageList.add("InBoxMessage");
		messageList.add(memberID);
		Main.client.handleMessageFromClientUI(messageList);
	}
}
