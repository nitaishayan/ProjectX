package logic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import Common.MyFile;

public class BookHandlerController {

	public static void isCopyLoaned(String copyID) {	
		ArrayList<String> copyData = new ArrayList<>();
		copyData.add("Check Copy Loan Status");
		copyData.add(copyID);
		Main.client.handleMessageFromClientUI(copyData);
	}

	public static void isCopyExist(String copyID) throws Exception {	
		if(copyID.length() == 0) {
			throw new Exception("Copy ID field can't be empty");
		}	
		ArrayList<String> copyData = new ArrayList<>();
		copyData.add("Check Copy ID Existence");
		copyData.add(copyID);
		Main.client.handleMessageFromClientUI(copyData);
	}

	public static void returnBook(String copyID, String status) {	
		ArrayList<String> memberData = new ArrayList<>();
		memberData.add("Return Book");
		memberData.add(copyID);
		memberData.add(status);
		Main.client.handleMessageFromClientUI(memberData);
	}

	//	public static void isCopyLate(String copyID) {	
	//		ArrayList<String> copyData = new ArrayList<>();
	//		copyData.add("Check If Copy Is Late");
	//		copyData.add(copyID);
	//		Main.client.handleMessageFromClientUI(copyData);
	//	}

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
}
