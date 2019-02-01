package logic;

import java.util.ArrayList;

import Common.Member;
import javafx.scene.control.TableColumnBase;

public class CommonController {

	private static final int ID_SIZE = 9;


	/**
	 * This method checks the format of the id, if it fits to the requirement add it to an array list and send the arraylist to the server.
	 * @param memberID - the ID of the specific member.
	 * @throws Exception
	 */
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
	 * @param memberID - the ID of the member
	 * @param oldStatus - the member's previous status before the change.
	 * @param newStatus - the member's current status after the change.
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
	 * This method gets string of date in the format 0000-00-00 (year-month-day) then cut the string and return ArrayList of integer that contains the date as integer.
	 * @param date - the date received.
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
	 * Check for the current date and time and return it in the following format "yyyy-MM-dd HH:mm:ss".
	 * @return currentTime - the string of the date with the wanted format.
	 */
	public static String getCurrentTime() {
		java.util.Date date= new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(date);
		return currentTime;
	}

	/**
	 * This method checks the propriety of the following fields:
	 * Phone number must be 10 digits long.
	 * Email must contains '@'.
	 * ID must have 9 digits long.
	 * Return the specific error or 'Success' if all the fields meets the requirement.
	 * @param phoneNumber - member's phone number.
	 * @param email - member's email.
	 * @param ID - member's ID.
	 * @return string of the corresponding message.
	 */
	public static String checkInput(String phoneNumber,String email,String ID) {
		if (phoneNumber.length() != 10){
			return "PhoneError";
		}
		if (!email.contains("@")) {
			return "EmailError";
		}
		if (ID.length() != 9){
			return "IDError";
		}
		return "Success";
	}	    	

	/**
	 * This method sends to the server a request to show the reader's details in the GUI.
	 */
	public static void ShowReaderCards () {
		ArrayList<String> ReaderCardsList = new ArrayList<>();
		ReaderCardsList.add("ReaderCard");
		Main.client.handleMessageFromClientUI(ReaderCardsList);
	}

	/**
	 * This method checks the propriety of the following fields:
	 * Phone number must be 10 digits long.
	 * Email must contains '@'.
	 * ID must have 9 digits long.
	 * Return the specific error or 'Success' if all the fields meets the requirement.
	 * @param phoneNumber - member's phone number.
	 * @param email - member's email.
	 * @param ID - member's ID.
	 * @param password - member's password.
	 * @return string of the corresponding message.
	 */
	public static String checkRegistrationInput(String phoneNumber,String email,String ID,String password) {
		if (phoneNumber.length() != 10){
			return "The phone number must include exactly 10 digits!";
		}
		if (!email.contains("@")) {
			return "The email must include '@' sign!";
		}
		if (ID.length() != 9){
			return "The Member ID must include exactly 9 digits!";
		}
		if (password.length() < 6) {
			return "The password must include atleast 6 characters!";
		}
		return "Success";
	}

	/**
	 * This method set the Minimum Width, Preferred Width , Maximum Width  of the TableView
	 * @param col - the column we want to apply the changes.
	 * @param minWidth - the minimum width of the column.
	 * @param prefWidth - the preferred width of the column.
	 * @param maxWidth - the maximum width of the column.
	 */
	public static <S,T> void setColumnWidth(TableColumnBase<S,T> col,double minWidth,double prefWidth, double maxWidth)
	{
		col.setMinWidth(minWidth);
		col.setPrefWidth(prefWidth);
		col.setMaxWidth(maxWidth);
	}
	
	/**
	 * Send request for the server to get the Employee Records .
	 */
	public static void ShowEmployeeRecords () {
		ArrayList<String> EmployeeList = new ArrayList<>();
		EmployeeList.add("EmployeeRecords");
		Main.client.handleMessageFromClientUI(EmployeeList);
	}

	/**
	 * Send request for the server to get the Inbox Message of a specific member.
	 * @param memberID - id of the member.
	 */
	public static void getMessage(String memberID) {
		ArrayList<String> messageList = new ArrayList<>();
		messageList.add("InBoxMessage");
		messageList.add(memberID);
		Main.client.handleMessageFromClientUI(messageList);
	}
}
