package logic;

import java.util.ArrayList;
/**
 * Class that unified the Methods sent to the data base and related to the member Card reader
 * both in the librarian page and the member page queries
 * @author nitay shayan
 *
 */
public class MemberCardController {
	/**
	 * Method that send to data base the memberID and get the member tuple from DB
	 * @param memberID
	 */
	public static void searchMember(String memberID) {
		ArrayList<String> searchData=new ArrayList<>();
		searchData.add("SearchMember");
		searchData.add(memberID);
		Main.client.handleMessageFromClientUI(searchData);
	}
	
	/**
	 * Method that update the card reader details based on edit by the member
	 * @param ID
	 * @param phoneNumber
	 * @param Email
	 */
	public  static void memberUpdateDetails(String ID,String phoneNumber, String Email) {
		ArrayList<String> updateData = new ArrayList<>();
		
		updateData.add("MemberUpdateMemberDetails");
		updateData.add(ID);
		updateData.add(phoneNumber);
		updateData.add(Email);
		Main.client.handleMessageFromClientUI(updateData);

	}
	
	/**
	 * 
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
}
