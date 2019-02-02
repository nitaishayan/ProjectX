package logic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import Common.MyFile;

/**
 * @author Idan
 *
 */
public class BookHandlerController {

	/**
	 * This method check if the copy is alredy loaned.
	 * @param copyID - this parameter describes the copy's ID we want to check if it's already loaned.
	 */
	public static void isCopyLoaned(String copyID) {	
		ArrayList<String> copyData = new ArrayList<>();
		copyData.add("Check Copy Loan Status");
		copyData.add(copyID);
		Main.client.handleMessageFromClientUI(copyData);
	}

	/**
	 * This method check if the copy exists in the library.
	 * @param copyID - this parameter describes the copy's ID we want to check if it's already exists in the library.
	 * @throws Exception
	 */
	public static void isCopyExist(String copyID) throws Exception {	
		if(copyID.length() == 0) {
			throw new Exception("Copy ID field can't be empty");
		}	
		ArrayList<String> copyData = new ArrayList<>();
		copyData.add("Check Copy ID Existence");
		copyData.add(copyID);
		Main.client.handleMessageFromClientUI(copyData);
	}

	/**
	 * This method returns a book from a user to the library.
	 * @param copyID - copy ID of the specific copy to return.
	 * @param status - status of the member that return the copy.
	 * @param memberID - member ID of the member that return the copy.
	 */
	public static void returnBook(String copyID, String status, String memberID) {	
		ArrayList<String> memberData = new ArrayList<>();
		memberData.add("Return Book");
		memberData.add(copyID);
		memberData.add(status);
		memberData.add(memberID);
		Main.client.handleMessageFromClientUI(memberData);
	}


	/**
	 * This method checks if member is late on return.
	 * @param memberID - member ID of the member that return the copy.
	 */
	public static void isMemberLateOnReturn(String memberID) {	
		ArrayList<String> memberData = new ArrayList<>();
		memberData.add("Check If Member Is Late On Return");
		memberData.add(memberID);
		Main.client.handleMessageFromClientUI(memberData);
	}

	public static void isCopyWanted(String bookID) {	
		ArrayList<String> bookData = new ArrayList<>();
		bookData.add("Check Copy Wanted Status");
		bookData.add(bookID);
		Main.client.handleMessageFromClientUI(bookData);
	}

	public static void loanBook(String copyID,String bookStatus,String bookID,String memberID, String bookName, String authorName) {	
		ArrayList<String> copyData = new ArrayList<>();
		copyData.add("Loan Book");
		copyData.add(copyID);
		copyData.add(bookStatus);
		copyData.add(bookID);
		copyData.add(memberID);
		copyData.add(bookName);
		copyData.add(authorName);
		Main.client.handleMessageFromClientUI(copyData);
	}

	public static void reserveBook(String bookID, String memberID,String copyID) {
		ArrayList<String> memberData = new ArrayList<>();
		memberData.add("Reserve");
		memberData.add(bookID);
		memberData.add(memberID);		
		memberData.add(copyID);		
		Main.client.handleMessageFromClientUI(memberData);
	}

	public static void sendPdf(File file,String bookid) {
		System.out.println(bookid);
		MyFile msg= new MyFile(bookid);
		String LocalfilePath=file.getPath();

		try{

			File newFile = new File (LocalfilePath);

			byte [] mybytearray  = new byte [(int)newFile.length()];
			FileInputStream fis = new FileInputStream(newFile);
			BufferedInputStream bis = new BufferedInputStream(fis);			  

			msg.initArray(mybytearray.length);
			msg.setSize(mybytearray.length);

			bis.read(msg.getMybytearray(),0,mybytearray.length);
			Main.client.handleMessageFromClientUI(msg);
		}
		catch (Exception e) {
			System.out.println("Error send (Files)msg) to Server");
		}
	}

	public static void getPDF(String bookid) {
		ArrayList<String> requestPDF = new ArrayList<>();
		requestPDF.add("get-pdf");
		requestPDF.add(bookid);
		System.out.println(bookid);
		Main.client.handleMessageFromClientUI(requestPDF);
	}
	
	public static void extendLoanPeriodByMember(String memberID, String copyID, String bookName, String authorsName) {
		ArrayList<String> extendData = new ArrayList<>();
		extendData.add("Extend Loan Period By Member");
		extendData.add(memberID);
		extendData.add(copyID);
		extendData.add(bookName);
		extendData.add(authorsName);		
		Main.client.handleMessageFromClientUI(extendData);
	}

	public static void extendLoanPeriodByLibrarian(String memberID, String copyID, String bookName, String authorsName, String librarianID) {
		ArrayList<String> extendData = new ArrayList<>();
		extendData.add("Extend Loan Period By Librarian");
		extendData.add(memberID);
		extendData.add(copyID);
		extendData.add(bookName);
		extendData.add(authorsName);
		extendData.add(librarianID);		
		Main.client.handleMessageFromClientUI(extendData);
	}
	
	/**
	 * Method that update the data base that a copy is lost by a member
	 * the method update the copy table and delayonreturn table
	 * @param memberID memberID of the member that lost
	 * @param copyID copiId of the book that was lost
	 * @param copyName name of the copy that lost
	 */
	public static void memberLostBook(String memberID,String copyID,String copyName) {
		ArrayList<String> data = new ArrayList<>();
		data.add("MemberLostBook");
		data.add(memberID);
		data.add(copyID);
		data.add(copyName);
		Main.client.handleMessageFromClientUI(data);
	}
}
