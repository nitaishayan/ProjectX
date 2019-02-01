package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import com.mysql.fabric.xmlrpc.base.Data;

import Client.Client;
import Common.Book;
import Common.InventoryBook;
import Common.LoanDetails;
import GUI.LibrarianMenuGUI;
import logic.CommonController;

public class DBController {

	private static DBController dbController = null;
	public static Connection conn;
	public static int bookGenerate;
	public static int copyGenerate;


	private DBController(Connection conn)
	{
		this.conn = conn;	
	}

	public static DBController getInstance()
	{
		if (dbController == null)
			dbController = new DBController(connectToDatabase());
		return dbController;
	}

	// register new user in database
	public static int registration(ArrayList<String> data) throws SQLException
	{
		PreparedStatement preparedRegistration;
		ResultSet rsRegistration;
		PreparedStatement UpdateStatus;

		preparedRegistration = conn.prepareStatement("SELECT  * FROM members WHERE MemberID=? ");
		preparedRegistration.setString(1, data.get(2));
		rsRegistration = preparedRegistration.executeQuery();
		if ((rsRegistration.isBeforeFirst()))
		{
			return 0;
		}

		preparedRegistration = conn.prepareStatement("SELECT  * FROM members WHERE PhoneNumber=? ");
		preparedRegistration.setString(1, data.get(1));
		rsRegistration = preparedRegistration.executeQuery();
		if ((rsRegistration.isBeforeFirst()))
		{
			return 0;
		}

		preparedRegistration = conn.prepareStatement("SELECT  * FROM librarian WHERE LibrarianID=? ");
		preparedRegistration.setString(1, data.get(2));
		rsRegistration = preparedRegistration.executeQuery();
		if ((rsRegistration.isBeforeFirst()))
		{
			return 0;
		}

		PreparedStatement insert = conn.prepareStatement("insert into members values(?,?,?,?,?,?,?,?,?,?,?,?)");
		insert.setString(1, data.get(2));
		insert.setString(2, data.get(1));
		insert.setString(3, data.get(5));
		insert.setString(4, data.get(6));
		insert.setString(5, data.get(4));
		insert.setString(6, data.get(3));
		insert.setString(7, "Active");
		insert.setString(8, null);
		insert.setString(9, "0");
		insert.setString(10, "false");
		insert.setString(11, null);
		insert.setString(12, "false");
		insert.executeUpdate();
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(dt);
		UpdateStatus = conn.prepareStatement("INSERT memberstatus values(?,?,?,?)");
		UpdateStatus.setString(1, data.get(2));//memberID
		UpdateStatus.setString(2, "Active");//prev status
		UpdateStatus.setString(3, "Active");//current status
		UpdateStatus.setString(4,currentTime);
		UpdateStatus.executeUpdate();
		return 1;
	}

	//add book to inventory database
	public static int addBookToInventory(ArrayList<String> data) throws SQLException{
		int Result,maxBookID=0;
		String string = "SELECT MAX(BookID) FROM book";
		PreparedStatement getMaxID = conn.prepareStatement(string);
		ResultSet rs=getMaxID.executeQuery();
		if(rs.next()) {
			if (rs.getString(1)!=null) {
				maxBookID=Integer.parseInt(rs.getString(1));
			}
		}
		PreparedStatement insert = conn.prepareStatement("insert into book values(?,?,?,?,?,?,?,?,?,?,?,?)");
		insert.setString(1, Integer.toString(++maxBookID));// book id
		insert.setString(2, data.get(1));//book name
		insert.setString(3, "0");//copies
		insert.setString(4, data.get(3));//wanted
		insert.setString(5, data.get(4));//author name
		insert.setString(6, data.get(5));//edition
		insert.setString(7, data.get(6));//print date
		insert.setString(8, data.get(7));//book genre
		insert.setString(9, data.get(8));//description
		insert.setString(10, data.get(9));//purchase date
		insert.setString(11, "pdf");//pdf
		insert.setString(12, data.get(2));//shelf location
		Result=insert.executeUpdate();
		data.add(Integer.toString(maxBookID));
		addCopyToInventory(data);
		return Result;
	}

	public static ArrayList<String> addCopyToInventory(ArrayList<String> data) throws SQLException{
		int maxCopyID=0;
		String copyid,bookid;
		bookid=data.get(data.size()-1);
		PreparedStatement stmt = conn.prepareStatement("SELECT MAX(CopyID) FROM copies WHERE BookID = ?");
		stmt.setString(1, bookid);
		ResultSet rs = stmt.executeQuery();

		if(rs.next()) {
			if (rs.getString(1)!=null) {
				maxCopyID=Integer.parseInt(rs.getString(1).substring((rs.getString(1).indexOf("-"))+1));
			}
		}
		PreparedStatement insert = conn.prepareStatement("insert into copies values(?,?,?,?,?)");
		copyid=(data.get(data.size()-1)) + "-" + Integer.toString(++maxCopyID);
		insert.setString(1, copyid);
		insert.setString(2, data.get(1));
		insert.setString(3, "false");
		//		insert.setString(4, data.get(2));
		insert.setString(4, data.get(data.size()-1));
		insert.setString(5, null);
		insert.executeUpdate();
		data.add(copyid);
		data.add("success");
		updatecopyamount("increase",bookid);
		return data;
	}

	public static int updatecopyamount(String action,String bookid) throws SQLException {
		int amountcopies=0,answer;
		switch (action) {
		case "increase":
			PreparedStatement increse=conn.prepareStatement("UPDATE book SET copies=copies+1 WHERE bookID=?");
			increse.setString(1, bookid);
			increse.executeUpdate();
			break;
		case "decrease":
			PreparedStatement decrease=conn.prepareStatement("UPDATE book SET copies=copies-1 WHERE bookID=?");
			decrease.setString(1, bookid);
			decrease.executeUpdate();
			PreparedStatement stmt = conn.prepareStatement("SELECT copies FROM book WHERE BookID = ?");
			stmt.setString(1, bookid);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				if (rs.getString(1).equals("0")) {
					PreparedStatement delete=conn.prepareStatement("DELETE FROM book WHERE bookID = ?");
					delete.setString(1, bookid);
					answer=delete.executeUpdate();
					return answer;
				}
			}
			break;
		default:
			break;
		}
		return 0;
	}


	public static ArrayList<String> RemoveCopy(ArrayList<String> data) throws SQLException{
		String bookID,copyid,memberid;
		ArrayList<String> reStrings=new ArrayList<>();
		reStrings.add("RemoveCopy");
		int answer=0;
		copyid=data.get(1);

		PreparedStatement checkloan = conn.prepareStatement("SELECT * FROM loanbook WHERE CopyID=? AND IsReturned='false'");
		checkloan.setString(1, copyid);
		ResultSet rs1=checkloan.executeQuery();
		if (rs1.next()) {
			reStrings.add("-1");
			return reStrings;
		}

		PreparedStatement getbookid = conn.prepareStatement("SELECT BookID FROM copies WHERE CopyID=?");
		getbookid.setString(1, copyid);
		ResultSet rs = getbookid.executeQuery();
		if(rs.next()) {
			bookID=rs.getString(1);
		}
		else {
			reStrings.add("0");
			return reStrings;
		}
		PreparedStatement reserved = conn.prepareStatement("SELECT IsActive,MemberID FROM reservations WHERE CopyID=? AND IsActive='true'");
		reserved.setString(1, copyid);
		ResultSet rs2 = reserved.executeQuery();
		if(rs2.next()) {
			memberid=rs2.getString(2);
			PreparedStatement delete = conn.prepareStatement("UPDATE reservations SET IsActive='false' where  CopyID=? AND IsActive='true'");
			delete.setString(1, copyid);
			delete.executeUpdate();
			PreparedStatement sendMessage = conn.prepareStatement("INSERT INTO usermessages values(?,?,?,?)");
			sendMessage.setString(1, memberid);
			sendMessage.setString(2, "Reservation canceled");
			java.util.Date date= new java.util.Date();
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(date);
			sendMessage.setString(3, currentTime);
			sendMessage.setString(4, "Your reservation is canceled, please try to reserv again.");
			sendMessage.executeUpdate();
		}
		String deleteSQL = "DELETE FROM copies WHERE CopyID = ?";
		PreparedStatement removestmt = conn.prepareStatement(deleteSQL);
		removestmt.setString(1, copyid);
		int res=removestmt.executeUpdate();
		if (res==1) {
			answer=updatecopyamount("decrease",bookID);
			if (answer==1) {
				res=2;
			}
		}
		reStrings.add(bookID);
		reStrings.add(String.valueOf(res));
		return reStrings;
	}

	public static ArrayList<String> checkExistenceByCopy(ArrayList<String> msg) throws SQLException {
		String bookid,location;
		ArrayList<String> result=new ArrayList<String>();
		result.add("checkExistenceByCopy");
		//		PreparedStatement getbook = conn.prepareStatement("SELECT BookID,ShelfLocation FROM copies WHERE CopyID=?");
		PreparedStatement getbook = conn.prepareStatement("SELECT BookID FROM copies WHERE CopyID=?");

		getbook.setString(1, msg.get(1));
		ResultSet rs = getbook.executeQuery();
		if(rs.next()) {
			bookid=(rs.getString(1));
			//			location=rs.getString(2);
		}
		else
			return result;
		PreparedStatement getbookdetails=conn.prepareStatement("SELECT * FROM book WHERE bookID=?");
		getbookdetails.setString(1, bookid);
		ResultSet rs1=getbookdetails.executeQuery();
		if(rs1.next()) {
			result.add(rs1.getString(1));
			result.add(rs1.getString(2));
			result.add(rs1.getString(3));
			result.add(rs1.getString(4));
			result.add(rs1.getString(5));
			result.add(rs1.getString(6));
			result.add(rs1.getString(7));
			result.add(rs1.getString(8));
			result.add(rs1.getString(9));
			result.add(rs1.getString(10));
			result.add(rs1.getString(11));
			result.add(rs1.getString(12));
		}
		else
			return result;
		result.add("1");
		return result;
	}

	public static ArrayList<String> inventoryCheckExistence(ArrayList<String> data) throws SQLException{
		ArrayList<String> newData = new ArrayList<>();
		newData.add("InventoryCheckExistense");
		if (data.size()==3) {
			String string = "SELECT * FROM book WHERE BookName=? AND AuthorsName=?";
			PreparedStatement checkExistence = conn.prepareStatement(string);
			checkExistence.setString(1, data.get(1));
			checkExistence.setString(2, data.get(2));
			ResultSet rs = checkExistence.executeQuery();

			if(rs.next()) {
				newData.add(rs.getString(1));
				newData.add(rs.getString(2));
				newData.add(rs.getString(3));
				newData.add(rs.getString(4));
				newData.add(rs.getString(5));
				newData.add(rs.getString(6));
				newData.add(rs.getString(7));
				newData.add(rs.getString(8));
				newData.add(rs.getString(9));
				newData.add(rs.getString(10));
				newData.add(rs.getString(11));
				newData.add(rs.getString(12));
			}
			else {
				newData.add("not exist");
			}
		}
		else if (data.size()==2) {
			String string = "SELECT * FROM book WHERE BookID=?";
			PreparedStatement checkExistence = conn.prepareStatement(string);
			checkExistence.setString(1, data.get(1));
			ResultSet rs = checkExistence.executeQuery();
			if(rs.next()) {
				newData.add(rs.getString(1));
				newData.add(rs.getString(2));
				newData.add(rs.getString(3));
				newData.add(rs.getString(4));
				newData.add(rs.getString(5));
				newData.add(rs.getString(6));
				newData.add(rs.getString(7));
				newData.add(rs.getString(8));
				newData.add(rs.getString(9));
				newData.add(rs.getString(10));
				newData.add(rs.getString(11));
				newData.add(rs.getString(12));
			}
			else {
				newData.add("not exist");
			}
		}
		return newData;
	}

	public static ArrayList<String>  login(ArrayList<String> data) throws SQLException
	{
		ArrayList<String> userDetails = null;
		PreparedStatement login;
		ResultSet rs;
		String status;
		//First we search in librarian table if the user is librarian
		login = conn.prepareStatement("SELECT LibrarianID,Password,FirstName,LastName,IsLoggedIn,IsManager FROM librarian WHERE LibrarianID=? AND Password=?");
		login.setString(1,data.get(1));//LibrarianID
		login.setString(2,data.get(2));//Password
		rs = login.executeQuery();

		if(rs.next()) {
			//If the user is a librarian
			userDetails=new ArrayList<String>();
			userDetails.add("Login");
			userDetails.add(rs.getString(1));//Add LibrarianID
			userDetails.add(rs.getString(2));//Add Password
			userDetails.add(rs.getString(3));//Add FirstName
			userDetails.add(rs.getString(4));//Add LastName

			//System.out.println(userDetails+"userDetails");


			//////////////////Check if user (librarian) is connected from another device

			if(rs.getString(5).equals("true")) { 
				//System.out.println("librarian is already connceted from another device");
				userDetails.add("0");//Add 0 to the arrayList to identify that the user is already connected
				return userDetails;
			}
			else {
				//If the librarian isn't connected - update IsLoggedIn to 'true' (connected)
				login = conn.prepareStatement("UPDATE librarian SET IsLoggedIn='true' where LibrarianID=?");
				login.setString(1, data.get(1));
				login.executeUpdate();
				//System.out.println("librarian connceted");
			}
			userDetails.add("1");//Add 1 to the arrayList to identify that the librarian is connected
			userDetails.add(rs.getString(6));
			return userDetails;
		}

		else {
			//If the user is a member
			userDetails=new ArrayList<String>();
			login = conn.prepareStatement("SELECT * FROM members WHERE MemberID=? AND Password=?");
			login.setString(1,data.get(1));//MemberID
			login.setString(2,data.get(2));//Password
			rs = login.executeQuery();
			if(rs.next()) {
				userDetails.add("Login");
				userDetails.add(rs.getString(1));//Add MemberID
				userDetails.add(rs.getString(4));//Add Password
				userDetails.add(rs.getString(5));//Add FirstName
				userDetails.add(rs.getString(6));//Add LastName
				status = rs.getString(7);
				//////////////////Check if user (member) is connected from another device

				if (status.equals("Locked")) {
					userDetails.add("4");
					return userDetails;
				}

				if(rs.getString(10).equals("true")) { 
					//System.out.println("librarian is already connceted from another device");
					userDetails.add("0");//Add 0 to the arrayList to identify that the member is already connected
					return userDetails;
				}
				else {
					login = conn.prepareStatement("UPDATE members SET IsLoggedIn='true' where MemberID=?");
					login.setString(1, data.get(1));
					login.executeUpdate();
				}

				if(rs.getString(12).equals("true")) { 
					userDetails.add("3");
					return userDetails;
				}
				userDetails.add("2");//Add 2 to the arrayList to identify that the member is connected
				return userDetails;
			}
			else {
				//Enter null values if the user doesn't exists
				userDetails.add("Login");
				userDetails.add(null);
				userDetails.add(null);
				userDetails.add(null);
				userDetails.add(null);
				userDetails.add("-1");
				return userDetails;
			}	
		}
	}

	public static ArrayList<String> editBook(ArrayList<String> searchData) throws SQLException{
		int answer;
		PreparedStatement updatebook=conn.prepareStatement("UPDATE book SET BookName=?,EditionNumber=?,BookGenre=?,PDFLink=?,AuthorsName=?,ShelfLocation=?,Description=?,Wanted=?,PurchaseDate=?,PrintDate=? WHERE BookID=?");
		updatebook.setString(1, searchData.get(1));	
		updatebook.setString(2, searchData.get(2));	
		updatebook.setString(3, searchData.get(3));	
		updatebook.setString(4, searchData.get(4));	
		updatebook.setString(5, searchData.get(5));	
		updatebook.setString(6, searchData.get(6));	
		updatebook.setString(7, searchData.get(7));	
		updatebook.setString(8, searchData.get(8));	
		updatebook.setString(9, searchData.get(9));	
		updatebook.setString(10, searchData.get(10));	
		updatebook.setString(11, searchData.get(11));	
		answer=updatebook.executeUpdate();
		searchData.add(Integer.toString(answer));
		return searchData;
	}


	public static ArrayList<String> searchBook(ArrayList<String> searchData) throws SQLException
	{
		System.out.println(searchData);
		System.out.println(searchData.get(2));
		PreparedStatement searchBook;
		ResultSet rsBook;
		String bookID = null;
		switch (searchData.get(1)) {
		case "Book Name":
			searchBook = conn.prepareStatement("SELECT BookName,AuthorsName,BookGenre,Description,BookID,Copies,Wanted,ShelfLocation FROM book WHERE BookName LIKE'%"+searchData.get(2)+"%'");
			//	searchBook.setString(1,searchData.get(2));
			rsBook = searchBook.executeQuery();
			if (!(rsBook.isBeforeFirst()))
			{
				searchData.add("-1");
				return searchData; 		// the book not found, handle with this....
			}
			else {
				searchData.add("1");
				while(rsBook.next())
				{
					searchData.add(rsBook.getString(1));
					searchData.add(rsBook.getString(2));
					searchData.add(rsBook.getString(3));
					searchData.add(rsBook.getString(4));
					searchData.add(rsBook.getString(5));
					searchData.add(rsBook.getString(6));
					searchData.add(rsBook.getString(7));
					searchData.add(rsBook.getString(8));
				}
				return searchData;
			}
		case "Authors Name":
			searchBook = conn.prepareStatement("SELECT BookName,AuthorsName,BookGenre,Description,BookID,Copies,Wanted,ShelfLocation FROM book WHERE AuthorsName LIKE '%"+searchData.get(2)+"%'");
			//	searchBook.setString(1,searchData.get(2));
			rsBook = searchBook.executeQuery();
			if (!(rsBook.isBeforeFirst()))
			{
				searchData.add("-1");
				return searchData; 		// the book not found, handle with this....
			}
			else {
				searchData.add("1");
				while(rsBook.next())
				{
					searchData.add(rsBook.getString(1));
					searchData.add(rsBook.getString(2));
					searchData.add(rsBook.getString(3));
					searchData.add(rsBook.getString(4));
					searchData.add(rsBook.getString(5));
					searchData.add(rsBook.getString(6));
					searchData.add(rsBook.getString(7));
					searchData.add(rsBook.getString(8));
				}
				return searchData;
			}
		case "Book Theme":
			searchBook = conn.prepareStatement("SELECT BookName,AuthorsName,BookGenre,Description,BookID,Copies,Wanted,ShelfLocation FROM book WHERE BookGenre LIKE '%"+searchData.get(2)+"%'");
			//	searchBook.setString(1,searchData.get(2));
			rsBook = searchBook.executeQuery();
			if (!(rsBook.isBeforeFirst()))
			{
				searchData.add("-1");
				return searchData; 		// the book not found, handle with this....
			}
			else {
				searchData.add("1");
				while(rsBook.next())
				{
					searchData.add(rsBook.getString(1));
					searchData.add(rsBook.getString(2));
					searchData.add(rsBook.getString(3));
					searchData.add(rsBook.getString(4));
					searchData.add(rsBook.getString(5));
					searchData.add(rsBook.getString(6));
					searchData.add(rsBook.getString(7));
					searchData.add(rsBook.getString(8));
				}
				return searchData;
			}	
		case "Free text":
			searchBook = conn.prepareStatement("SELECT BookName,AuthorsName,BookGenre,Description,BookID,Copies,Wanted,ShelfLocation FROM book WHERE Description LIKE '%"+searchData.get(2)+"%'");
			//		searchBook.setString(1,searchData.get(2));
			rsBook = searchBook.executeQuery();
			if (!(rsBook.isBeforeFirst()))
			{
				searchData.add("-1");
				return searchData; 		// the book not found, handle with this....
			}
			else {
				searchData.add("1");
				while(rsBook.next())
				{
					searchData.add(rsBook.getString(1));
					searchData.add(rsBook.getString(2));
					searchData.add(rsBook.getString(3));
					searchData.add(rsBook.getString(4));
					searchData.add(rsBook.getString(5));
					searchData.add(rsBook.getString(6));
					searchData.add(rsBook.getString(7));
					searchData.add(rsBook.getString(8));
				}
				return searchData;
			}
		case "Copy ID":
			searchBook = conn.prepareStatement("SELECT BookID FROM copies WHERE CopyID=? ");
			searchBook.setString(1,searchData.get(2));
			rsBook = searchBook.executeQuery();
			if (!(rsBook.isBeforeFirst()))
			{
				searchData.add("-1");
				return searchData; 		// the book not found, handle with this....
			}
			else {
				while(rsBook.next())
				{
					bookID =rsBook.getString(1);
				}
				searchBook = conn.prepareStatement("SELECT BookName,AuthorsName,BookGenre,Description,BookID,Copies,Wanted,ShelfLocation FROM book WHERE BookID=? ");
				searchBook.setString(1,bookID);
				rsBook = searchBook.executeQuery();
				searchData.add("1");
				while(rsBook.next())
				{
					searchData.add(rsBook.getString(1));
					searchData.add(rsBook.getString(2));
					searchData.add(rsBook.getString(3));
					searchData.add(rsBook.getString(4));
					searchData.add(rsBook.getString(5));
					searchData.add(rsBook.getString(6));
					searchData.add(rsBook.getString(7));
					searchData.add(rsBook.getString(8));
				}
				return searchData;
			}

		default:
			searchData.add("-1");
			break;
		}
		return searchData;
	}

	public static Object memberSearch(ArrayList<String> stu) throws SQLException
	{
		PreparedStatement execute;
		ResultSet rs;
		ArrayList<String>member=new ArrayList<String>();
		member.add("SearchMember");
		execute = conn.prepareStatement("SELECT * FROM members WHERE MemberID=?");
		execute.setString(1,stu.get(1));
		rs = execute.executeQuery();
		if(rs.next()) { 
			member.add(rs.getString(1));
			member.add(rs.getString(2));
			member.add(rs.getString(3));
			member.add(rs.getString(4));
			member.add(rs.getString(5));
			member.add(rs.getString(6));
			member.add(rs.getString(7));
			member.add(rs.getString(8));
			member.add(rs.getString(9));
			member.add(rs.getString(10));
			member.add(rs.getString(11));
			member.add(rs.getString(12));
			System.out.println(member);
			return member;
		}
		else
			return null;
	}


	public static ArrayList<String> isMemberExist(ArrayList<String> data) throws SQLException {
		ArrayList<String> checkMemberExistence = new ArrayList<>();
		checkMemberExistence.add("Check Member Existence");
		ResultSet rs;
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM members WHERE MemberID = ?");
		ps.setString(1, data.get(1));
		rs = ps.executeQuery();
		if (!rs.isBeforeFirst() ) {    
			return checkMemberExistence;
		}
		if(rs.next()) {
			checkMemberExistence.add(rs.getString(1));
			checkMemberExistence.add(rs.getString(2));
			checkMemberExistence.add(rs.getString(3));
			checkMemberExistence.add(rs.getString(4));
			checkMemberExistence.add(rs.getString(5));
			checkMemberExistence.add(rs.getString(6));
			checkMemberExistence.add(rs.getString(7));
			checkMemberExistence.add(rs.getString(8));
			checkMemberExistence.add(rs.getString(9));
			checkMemberExistence.add(rs.getString(10));
			checkMemberExistence.add(rs.getString(11));
			checkMemberExistence.add(rs.getString(12));
		}
		return checkMemberExistence;
	}


	public static ArrayList<String> isCopyLoaned(ArrayList<String> data) throws SQLException {
		ArrayList<String> checkCopyLoanStatus = new ArrayList<>();
		checkCopyLoanStatus.add("Check Copy Loan Status");
		ResultSet rs;
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM loanbook WHERE CopyID = ? AND IsReturned = ?");
		ps.setString(1, data.get(1));
		ps.setString(2, "false");
		rs = ps.executeQuery();
		if (!rs.isBeforeFirst() ) {    
			return checkCopyLoanStatus;
		}

		if(rs.next()) {
			checkCopyLoanStatus.add(rs.getString(1));
			checkCopyLoanStatus.add(rs.getString(2));
			checkCopyLoanStatus.add(rs.getString(3));
			checkCopyLoanStatus.add(rs.getString(4));
			checkCopyLoanStatus.add(rs.getString(5));
			checkCopyLoanStatus.add(rs.getString(6));
			checkCopyLoanStatus.add(rs.getString(7));
			checkCopyLoanStatus.add(rs.getString(8));
			checkCopyLoanStatus.add(rs.getString(9));
			checkCopyLoanStatus.add(rs.getString(10));
		}
		return checkCopyLoanStatus;
	}

	public static ArrayList<String> isCopyExist(ArrayList<String> data) throws SQLException {
		ArrayList<String> checkCopyLoanStatus = new ArrayList<>();
		checkCopyLoanStatus.add("Check Copy ID Existence");
		ResultSet rs;
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM copies WHERE CopyID = ?");
		ps.setString(1, data.get(1));
		rs = ps.executeQuery();
		if (!rs.isBeforeFirst() ) {    
			return checkCopyLoanStatus;
		}
		if(rs.next()) {
			checkCopyLoanStatus.add(rs.getString(1));
			checkCopyLoanStatus.add(rs.getString(2));
			checkCopyLoanStatus.add(rs.getString(3));
			checkCopyLoanStatus.add(rs.getString(4));
			checkCopyLoanStatus.add(rs.getString(5));
		}
		return checkCopyLoanStatus;
	}

	public static ArrayList<String> returnBook(ArrayList<String> data) throws SQLException, ParseException {
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(dt);
		ArrayList<String> returnBook = new ArrayList<>();
		returnBook.add("Return Book");
		ResultSet rs,rs2;
		PreparedStatement ps = conn.prepareStatement("UPDATE copies SET isLoaned = ?, ActualReturnDate = ? WHERE CopyID = ?");
		ps.setString(1, "false");
		ps.setString(2, currentTime);
		ps.setString(3, data.get(1));
		PreparedStatement ps1 = conn.prepareStatement("UPDATE loanbook SET isReturned = ?, ActualReturnDate = ? WHERE CopyID = ? AND isReturned = ?");
		ps1.setString(1, "true");
		ps1.setString(2, currentTime);
		ps1.setString(3, data.get(1));
		ps1.setString(4, "false");
		PreparedStatement ps2 = conn.prepareStatement("SELECT * from reservations WHERE CopyID = ? AND CopyReadyForPickUpDate = ? AND IsActive = ?");
		ps2.setString(1, data.get(1));
		ps2.setString(2,"0000-00-00 00:00:00");
		ps2.setString(3, "true");
		rs = ps2.executeQuery();
		if(rs.isBeforeFirst()) {
			PreparedStatement ps22 = conn.prepareStatement("UPDATE reservations SET CopyReadyForPickUpDate = ? WHERE CopyID = ? AND IsActive = ?");
			ps22.setString(1, currentTime);
			ps22.setString(2, data.get(1));
			ps22.setString(3, "true");
			ps22.executeUpdate();
			PreparedStatement ps6 = conn.prepareStatement("SELECT CopyName FROM copies WHERE CopyID = ?");
			ps6.setString(1, data.get(1));
			rs2 = ps6.executeQuery();
			if (rs2.next())
			{
				if (rs.next())
				{
					PreparedStatement ps5 = conn.prepareStatement("INSERT into usermessages values(?,?,?,?)");
					ps5.setString(1, rs.getString(3)); //memberID
					ps5.setString(2, "Your reservation is arrived.");
					ps5.setString(3, currentTime);
					Date pickUpDate = sdf.parse(currentTime); 
					Calendar c = Calendar.getInstance();
					c.setTime(pickUpDate);
					c.add(Calendar.DATE, 2);
					String newpickUpDate = sdf.format(c.getTime());
					ps5.setString(4, "The book: " + rs2.getString(1) + " that you reserved arrived to the library. the copy will be avilable until " + newpickUpDate +".");	
					ps5.executeUpdate();
				}
			}
		}

		if(!data.get(2).equals("Active")) {
			String memberID = null;
			String copyID = null;
			String newCopyID = null;

			PreparedStatement ps3 = conn.prepareStatement("SELECT MemberID from currentdelayonreturn WHERE CopyID = ?");
			ps3.setString(1, data.get(1));
			rs = ps3.executeQuery();
			if(rs.next()) {
				memberID = rs.getString(1);
			}

			PreparedStatement ps4 = conn.prepareStatement("DELETE from currentdelayonreturn WHERE CopyID = ?");
			ps4.setString(1, data.get(1));
			ps4.executeUpdate();

			PreparedStatement ps5 = conn.prepareStatement("SELECT FreezedOn from members WHERE MemberID = ?");
			ps5.setString(1, memberID);
			rs = ps5.executeQuery();
			if(rs.next()) {
				copyID = rs.getString(1);
			}

			PreparedStatement ps6 = conn.prepareStatement("SELECT CopyID from currentdelayonreturn WHERE MemberID = ?");
			ps6.setString(1, memberID);
			rs = ps6.executeQuery();
			if (!rs.isBeforeFirst() ) {    
				newCopyID = null;
			}
			else {
				rs.next();
				newCopyID = rs.getString(1);
			}


			if(data.get(1).equals(copyID) && newCopyID != null && memberID != null) {
				PreparedStatement ps7 = conn.prepareStatement("UPDATE members SET FreezedOn = ? WHERE MemberID = ?");
				ps7.setString(1, newCopyID);
				ps7.setString(2, memberID);
				ps7.executeUpdate();
			}
			else {
				if(data.get(1).equals(copyID) && newCopyID == null && memberID != null) {
					PreparedStatement ps8 = conn.prepareStatement("UPDATE members SET FreezedOn = ? WHERE MemberID = ?");
					ps8.setString(1, newCopyID);
					ps8.setString(2, memberID);
					ps8.executeUpdate();
				}
			}
		}

		if(ps.executeUpdate() != 0 && ps1.executeUpdate() != 0) {
			returnBook.add(currentTime);
		}

		return returnBook;
	}

	public ArrayList<String> searchBookDetailes(ArrayList<String> msg) throws SQLException{
		ResultSet 	rs1,rs2,rs3;
		String 		bookID   	  = null;
		String		shelfLocation = null;
		String		returnDate	  = null;
		String		memberID	  = null;
		String		copyID	 	  = null;

		PreparedStatement ps1 = conn.prepareStatement("SELECT BookID,ShelfLocation FROM book WHERE AuthorsName = ? AND BookName = ?");
		ps1.setString(1, msg.get(2));
		ps1.setString(2, msg.get(1));
		rs1 = ps1.executeQuery();
		while(rs1.next()) {
			bookID =rs1.getString(1);
			shelfLocation =rs1.getString(2);
		}
		PreparedStatement ps2 = conn.prepareStatement("SELECT BookID FROM copies WHERE BookID = ? AND IsLoaned = ?");
		ps2.setString(1, bookID);
		ps2.setString(2, "false");
		rs2 = ps2.executeQuery();

		if (!(rs2.isBeforeFirst()) ) // all the copies are loan
		{
			java.util.Date date= new java.util.Date();
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(date);
			System.out.println(currentTime);
			PreparedStatement ps3 = conn.prepareStatement("SELECT ExpectedReturnDate,MemberID,CopyID FROM loanbook WHERE ExpectedReturnDate > ? AND BookID = ? AND IsReturned = ? ORDER BY ExpectedReturnDate LIMIT 1");

			ps3.setString(1, currentTime);
			ps3.setString(2, bookID);
			ps3.setString(3, "false");
			rs3 = ps3.executeQuery();
			while(rs3.next()) {
				returnDate = rs3.getString(1);
				memberID = rs3.getString(2);
				copyID = rs3.getString(3);
			}
			msg.add("0");
			msg.add(returnDate);
			msg.add(memberID);
			msg.add(bookID);
			msg.add(copyID);
		}
		else {
			msg.add("1");
			msg.add(shelfLocation);
		}

		return msg;
	}

	public static  void logout(ArrayList<String> data) throws SQLException {
		ArrayList<String> result=new ArrayList<String>();
		PreparedStatement login;
		ResultSet rs;
		login = conn.prepareStatement("SELECT LibrarianID,Password,FirstName,LastName,IsLoggedIn  FROM librarian WHERE LibrarianID=? AND Password=?");
		login.setString(1,data.get(1));
		login.setString(2,data.get(2));
		rs = login.executeQuery();

		if(rs.next()) {
			System.out.println("inside DBcontroller - logout");
			login = conn.prepareStatement("UPDATE librarian SET IsLoggedIn='false' where LibrarianID=?");
			login.setString(1,data.get(1));
			login.executeUpdate();
			System.out.println("inside DBcontroller2 - librarian logged out");
		}

		else {
			login = conn.prepareStatement("SELECT * FROM members WHERE MemberID=? AND Password=?");
			login.setString(1,data.get(1));
			login.setString(2,data.get(2));
			rs = login.executeQuery();

			if(rs.next()) {
				login = conn.prepareStatement("UPDATE members SET IsLoggedIn='false' where MemberID=?");
				login.setString(1,data.get(1));
				login.executeUpdate();
			}
		}
		result.add("Logout");
		//return result;
	}

	public static ArrayList<String> isMemberLateOnReturn(ArrayList<String> data) throws SQLException {
		ArrayList<String> checkMemberReturns = new ArrayList<>();
		checkMemberReturns.add("Check If Member Is Late On Return");
		ResultSet rs;
		PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM currentdelayonreturn WHERE MemberID = ?");
		ps.setString(1, data.get(1));
		rs = ps.executeQuery();
		if (rs.next()) {
			checkMemberReturns.add(Integer.toString(rs.getInt(1)));
		}
		else {
			checkMemberReturns.add("0");
		}
		return checkMemberReturns;
	}



	public static ArrayList<String> changeMemberStatus(ArrayList<String> data) throws SQLException {
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(dt);
		ArrayList<String> changeStatus = new ArrayList<>();
		changeStatus.add("Change Member Status");
		PreparedStatement UpdateStatus = conn.prepareStatement("INSERT memberstatus values(?,?,?,?)");
		UpdateStatus.setString(1, data.get(1));//memberID
		UpdateStatus.setString(2, data.get(2));//prev status
		UpdateStatus.setString(3, data.get(3));//current status
		UpdateStatus.setString(4,currentTime);
		UpdateStatus.executeUpdate();
		PreparedStatement ps = conn.prepareStatement("UPDATE members SET Status = ? WHERE MemberID = ?");
		ps.setString(1, data.get(3));
		ps.setString(2, data.get(1));
		if(ps.executeUpdate() != 0) {
			changeStatus.add(data.get(3));
		}
		return changeStatus;
	}

	public static ArrayList<String> CheckLibrarianManager(ArrayList<String> msg) throws SQLException {
		ArrayList<String> CheckLibrarianManager = new ArrayList<String>();
		CheckLibrarianManager.add("CheckLibrarianManager");
		ResultSet rs=null;
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM librarian WHERE LibrarianID = ?");
		ps.setString(1,((String)msg.get(1)));
		rs = ps.executeQuery();
		if(rs.next()) {
			CheckLibrarianManager.add(rs.getString(7));
			System.out.println(CheckLibrarianManager);
			return CheckLibrarianManager;

		}
		else
			return null;
	}
	public void MemberUpdateMemberDetails(ArrayList<String> member) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("UPDATE members SET PhoneNumber = ?, Email = ? WHERE MemberID = ?");
		ps.setString(1, member.get(2));
		ps.setString(2, member.get(3));
		ps.setString(3, member.get(1));
		ps.executeUpdate();
	}
	public void librarianUpdateMember(ArrayList<String> member) throws SQLException {
		PreparedStatement ps;
		PreparedStatement UpdateStatus;
		ps = conn.prepareStatement("UPDATE members SET Status = ?, Notes = ? WHERE MemberID = ?");
		ps.setString(1, member.get(2));
		ps.setString(2, member.get(3));
		ps.setString(3, member.get(1));
		ps.executeUpdate();
		if (member.get(4).equals("true")) {
			java.util.Date dt = new java.util.Date();
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(dt);
			UpdateStatus = conn.prepareStatement("INSERT memberstatus values(?,?,?,?)");
			UpdateStatus.setString(1, member.get(1));//memberID
			UpdateStatus.setString(2, member.get(5));//prev status
			UpdateStatus.setString(3, member.get(2));//current status
			UpdateStatus.setString(4,currentTime);
			UpdateStatus.executeUpdate();
		}
	}

	public static ArrayList<String> isCopyWanted(ArrayList<String> data) throws SQLException {
		ArrayList<String> checkCopyWantedStatus = new ArrayList<>();
		checkCopyWantedStatus.add("Check Copy Wanted Status");
		ResultSet rs;
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM book WHERE BookID = ?");
		ps.setString(1, data.get(1));
		rs = ps.executeQuery();
		if (!rs.isBeforeFirst() ) {    
			return checkCopyWantedStatus;
		}
		if(rs.next()) {
			checkCopyWantedStatus.add(rs.getString(1));
			checkCopyWantedStatus.add(rs.getString(2));
			checkCopyWantedStatus.add(rs.getString(3));
			checkCopyWantedStatus.add(rs.getString(4));
			checkCopyWantedStatus.add(rs.getString(5));
			checkCopyWantedStatus.add(rs.getString(6));
			checkCopyWantedStatus.add(rs.getString(7));
			checkCopyWantedStatus.add(rs.getString(8));
			checkCopyWantedStatus.add(rs.getString(9));
			checkCopyWantedStatus.add(rs.getString(10));
			checkCopyWantedStatus.add(rs.getString(11));
			checkCopyWantedStatus.add(rs.getString(12));
		}
		return checkCopyWantedStatus;
	}

	public static ArrayList<String> loanBook(ArrayList<String> data) throws SQLException {
		ArrayList<String> loanBook = new ArrayList<>();
		loanBook.add("Loan Book");
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(dt);
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(currentTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(data.get(2).equals("true")) {
			c.add(Calendar.DATE, 3);
		}
		else {
			c.add(Calendar.DATE, 14);
		}


		ResultSet rs;
		PreparedStatement ps2 = conn.prepareStatement("SELECT * from loanbook WHERE MemberID = ? AND BookID = ? AND IsReturned = ?");
		ps2.setString(1, data.get(4));
		ps2.setString(2, data.get(3));
		ps2.setString(3, "false");
		rs = ps2.executeQuery();
		if(rs.isBeforeFirst()) {
			loanBook.add("Member already loaned a copy of the same book!");
			return loanBook;
		}

		PreparedStatement ps10 = conn.prepareStatement("SELECT MemberID from reservations WHERE CopyID = ? AND IsActive = ?");
		ps10.setString(1, data.get(1));
		ps10.setString(2, "true");
		rs = ps10.executeQuery();
		if(rs.isBeforeFirst()) {
			if(rs.next()) {
				if(!rs.getString(1).equals(data.get(4))) {
					loanBook.add("The copy of the book is reserved to another member!");
					return loanBook;
				}
			}
		}



		String returnDate = sdf.format(c.getTime());
		PreparedStatement ps = conn.prepareStatement("INSERT loanbook values(?,?,?,?,?,?,?,?,?,?)");
		ps.setString(1, data.get(1));
		ps.setString(2, returnDate);
		ps.setString(3, null);
		ps.setString(4, data.get(4));
		ps.setString(5, data.get(3));
		ps.setString(6,currentTime);
		ps.setString(7,"false");
		ps.setString(8,"false");
		ps.setString(9, data.get(5));
		ps.setString(10, data.get(6));
		if(ps.executeUpdate() == 0) {
			loanBook.add("Error");
			return loanBook;
		}

		PreparedStatement ps1 = conn.prepareStatement("UPDATE copies SET isLoaned = ? , ActualReturnDate = ? WHERE CopyID = ?");
		ps1.setString(1, "true");
		ps1.setString(2, null);
		ps1.setString(3, data.get(1));
		ps1.executeUpdate();
		if(ps1.executeUpdate() == 0) {
			loanBook.add("Error");
			return loanBook;
		}

		loanBook.add(currentTime);
		loanBook.add(returnDate);
		return loanBook;
	}

	public Object viewPersonalHistory(ArrayList<String> searchData) throws SQLException {
		PreparedStatement searchLoan;
		ResultSet rsLoan;
		searchLoan = conn.prepareStatement("SELECT CopyID,LoanDate,BookName,ActualReturnDate,AuthorName FROM loanbook WHERE MemberID=? ORDER BY ActualReturnDate DESC");
		ArrayList<String> loanDetails = new ArrayList<String>();
		searchLoan.setString(1,searchData.get(1));
		rsLoan = searchLoan.executeQuery();
		loanDetails.add("ViewPersonalHistory");
		while(rsLoan.next()) {
			loanDetails.add(rsLoan.getString(1));//CopyID
			loanDetails.add(rsLoan.getString(2));//LoanDate
			loanDetails.add(rsLoan.getString(3));//BookName
			loanDetails.add(rsLoan.getString(4));//ActualReturnDate
			loanDetails.add(rsLoan.getString(5));//AuthorName
		}
		if (loanDetails.size()==1) {
			loanDetails.add("NotExist");
			System.out.println("Member not exist");
			return loanDetails;
		}
		else {
			return loanDetails;
		}
	}

	public ArrayList<String> reserveBook(ArrayList<String> bookdata) throws SQLException {
		int copies,answer,reserveamount=0;
		String bookID=bookdata.get(1),currentTime,memberID=bookdata.get(2);
		String copyID = null;

		PreparedStatement checkAlreadyReserve = conn.prepareStatement("SELECT * FROM reservations WHERE BookID = ? AND MemberID=? AND IsActive='true'");
		checkAlreadyReserve.setString(1,bookID);
		checkAlreadyReserve.setString(2,memberID);
		ResultSet rs= checkAlreadyReserve.executeQuery();
		if (rs.next()) {
			bookdata.add("Already reserve");
			return bookdata;
		}
		PreparedStatement checkForUser = conn.prepareStatement("SELECT * FROM loanbook WHERE BookID = ? AND MemberID=? AND IsReturned='false'");
		checkForUser.setString(1,bookID);
		checkForUser.setString(2,memberID);
		ResultSet rs1= checkForUser.executeQuery();
		if (rs1.next()) {
			bookdata.add("fail");
			return bookdata;
		}
		PreparedStatement ps = conn.prepareStatement("SELECT copies FROM book WHERE BookID = ?");
		ps.setString(1,bookID);
		ResultSet rs2= ps.executeQuery();
		if(rs2.next()) {
			copies=rs2.getInt(1);
		}
		else {
			System.out.println("cannot retrieve copies from book table.");
			bookdata.add("fail-1");
			return bookdata;
		}
		System.out.println(copies);
		PreparedStatement ps2 = conn.prepareStatement("SELECT COUNT(*) FROM reservations WHERE BookID=? AND IsActive='true' ");
		ps2.setString(1,bookID);
		ResultSet rs3= ps2.executeQuery();
		if(rs3.next()) {
			reserveamount=rs3.getInt(1);
			System.out.println(reserveamount);
		}
		else {
			bookdata.add("all the copies are allready reserved.");
			return bookdata;
		}

		if (reserveamount<copies) {
			currentTime=CommonController.getCurrentTime();
			PreparedStatement ps3 = conn.prepareStatement("SELECT CopyID FROM loanbook WHERE ExpectedReturnDate > ? AND BookID = ? AND IsReserved='false' ORDER BY ExpectedReturnDate LIMIT 1");
			ps3.setString(1, currentTime);
			ps3.setString(2, bookID);
			ResultSet rs4=ps3.executeQuery();
			if (rs4.next()) {
				copyID=rs4.getString(1);
			}
			else {
				bookdata.add("fail-2");
				return bookdata;
			}
			PreparedStatement insert = conn.prepareStatement("insert into reservations values(?,?,?,?,?)");
			insert.setString(1, copyID);
			insert.setString(2, "0000-00-00 00:00:00");
			insert.setString(3, bookdata.get(2));
			insert.setString(4, bookID);
			insert.setString(5, "true");
			answer=insert.executeUpdate();
			if (answer==1) {
				bookdata.add("success");
			}
			PreparedStatement update = conn.prepareStatement("UPDATE loanbook SET IsReserved = ? WHERE CopyID = ?");
			update.setString(1, "true");
			update.setString(2, copyID);
			answer=update.executeUpdate();
			if (answer!=1) {
				bookdata.add("fail-3");
			}
		}
		else bookdata.add("all the copies are allready reserved.");
		return bookdata;	
	}

	/**
	 * Function - ReaderCards, retrieve the card member's details form the DataBase
	 * @return ArrayList<String> listMember
	 * @throws SQLException
	 */
	public ArrayList<String> ReaderCards () throws SQLException{
		ArrayList<String> listMember = new ArrayList<String>();
		PreparedStatement stmt = conn.prepareStatement("SELECT MemberID,PhoneNumber,Email,FirstName,LastName,Status,Notes FROM members");
		ResultSet rs = stmt.executeQuery();
		listMember.add("ReaderCard");
		while(rs.next()) {
			listMember.add(rs.getString(1));//member ID
			listMember.add(rs.getString(2));//phone number
			listMember.add(rs.getString(3));//email
			listMember.add(rs.getString(4));//first name
			listMember.add(rs.getString(5));//last name
			listMember.add(rs.getString(6));//status
			listMember.add(rs.getString(7));//notes
		}
		return listMember;
	}

	public ArrayList<String> changeMemberToGraduated(ArrayList<String> data) throws SQLException {
		Date dt = new java.util.Date();
		SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = inFormat.format(dt),ManagerID=null;

		ArrayList<String> changeMemberToGraduated = new ArrayList<String>();
		if(data.get(12).equals("true")) {
			changeMemberToGraduated.add("Member already graduated!");
			return changeMemberToGraduated;
		}

		ArrayList<String> getMemberLoans = new ArrayList<String>();
		getMemberLoans.add("CurrentLoans");
		getMemberLoans.add(data.get(1));
		getMemberLoans = getCurrentLoans(getMemberLoans);
		int numberOfBook = (getMemberLoans.size()-1)/5;
		int i = 0;
		int j = 0;

		PreparedStatement ps12 = conn.prepareStatement("SELECT LibrarianID FROM librarian WHERE IsManager='true'");
		ResultSet rs12=ps12.executeQuery();
		if (rs12.next()) {
			ManagerID=rs12.getString(1);
		}
		PreparedStatement ps5 = conn.prepareStatement("INSERT into usermessages values(?,?,?,?)");
		ps5.setString(1, ManagerID);
		ps5.setString(2, "Graduated");
		ps5.setString(3, currentTime);
		ps5.setString(4, "MemberID " + data.get(1) + " has graduated.");	
		ps5.executeUpdate();
		while(i<numberOfBook) {
			if(i==0)
			{
				PreparedStatement ps7 = conn.prepareStatement("UPDATE members SET FreezedOn = ? WHERE MemberID = ?");
				ps7.setString(1, getMemberLoans.get(j+1));
				ps7.setString(2, data.get(1));
				ps7.executeUpdate();
			}

			PreparedStatement ps1 = conn.prepareStatement("INSERT into delayonreturn values(?,?,?,?,?)");
			ps1.setString(1, data.get(1));
			ps1.setString(2, getMemberLoans.get(j+1));
			ps1.setString(3, "Delayed");
			ps1.setString(4, getMemberLoans.get(j+2));
			ps1.setString(5, currentTime);
			ps1.executeUpdate();

			PreparedStatement ps3 = conn.prepareStatement("INSERT into currentdelayonreturn values(?,?,?)");
			ps3.setString(1, data.get(1));
			ps3.setString(2, getMemberLoans.get(j+1));
			ps3.setString(3, getMemberLoans.get(j+2));
			ps3.executeUpdate();
			i++;
			j+=5;
		}	

		if(getMemberLoans.size() <= 2) {
			PreparedStatement ps = conn.prepareStatement("UPDATE members SET IsGraduted = ? WHERE MemberID = ?");
			ps.setString(1, "true");
			ps.setString(2, data.get(1)); // ? == memberID
			if(ps.executeUpdate() == 0) {
				changeMemberToGraduated.add("Couldn't update memeber status!");
				return changeMemberToGraduated;
			}
			ArrayList<String> changeStatus = new ArrayList<String>();
			changeStatus.add("Change Member Status");
			changeStatus.add(data.get(1));
			changeStatus.add(data.get(7));
			changeStatus.add("Locked");
			changeStatus = changeMemberStatus(changeStatus);
			changeMemberToGraduated.add("Locked");
		}
		else {
			PreparedStatement ps = conn.prepareStatement("UPDATE members SET IsGraduted = ? WHERE MemberID = ?");
			ps.setString(1, "true");
			ps.setString(2, data.get(1)); // ? == memberID
			if(ps.executeUpdate() == 0) {
				changeMemberToGraduated.add("Couldn't update memeber status!");
				return changeMemberToGraduated;
			}
			if(!data.get(7).equals("Frozen")) {
				ArrayList<String> changeStatus = new ArrayList<String>();
				changeStatus.add("Change Member Status");
				changeStatus.add(data.get(1));
				changeStatus.add(data.get(7));
				changeStatus.add("Frozen");
				changeStatus = changeMemberStatus(changeStatus);
			}
			changeMemberToGraduated.add("Frozen");
		}


		PreparedStatement ps1 = conn.prepareStatement("INSERT into graduatedstudent values(?,?,?)");
		ps1.setString(1, data.get(1)); // x == memberID
		ps1.setString(2, data.get(5)); // y == First Name
		ps1.setString(3, data.get(6)); // z == Last Name
		if(ps1.executeUpdate() == 0) {
			changeMemberToGraduated.add("Couldn't insert memeber to graduated table!");
			return changeMemberToGraduated;
		}

		changeMemberToGraduated.add("Succsess!");
		changeMemberToGraduated.add("Succsess!");
		return changeMemberToGraduated;
	}

	public ArrayList<String> getCurrentLoans(ArrayList<String> searchData) throws SQLException {
		PreparedStatement searchLoan,searchAuthorName;
		ResultSet rsLoan,rsAuthorName;
		searchLoan = conn.prepareStatement("SELECT CopyID,BookName,LoanDate,ExpectedReturnDate,BookID FROM loanbook WHERE MemberID=? AND IsReturned = 'false' ORDER BY ExpectedReturnDate DESC");
		searchLoan.setString(1,searchData.get(1));
		rsLoan = searchLoan.executeQuery();
		ArrayList<String> currentLoans = new ArrayList<>();
		currentLoans.add("CurrentLoans");

		while(rsLoan.next()) {
			searchAuthorName = conn.prepareStatement("SELECT AuthorsName FROM book WHERE BookID=?");
			searchAuthorName.setString(1,rsLoan.getString(5));
			rsAuthorName = searchAuthorName.executeQuery();

			currentLoans.add(rsLoan.getString(1));//CopyID
			currentLoans.add(rsLoan.getString(2));//BookName
			while(rsAuthorName.next())
				currentLoans.add(rsAuthorName.getString(1));//AuthorsName
			currentLoans.add(rsLoan.getString(3));//LoanDate
			currentLoans.add(rsLoan.getString(4));//ExpectedReturnDate	
		}

		if (currentLoans.size()==1) {
			currentLoans.add("NotExist");
		}
		return currentLoans;
	}

	public ArrayList<String> getDelayandLostBooks(ArrayList<String> msg) throws SQLException {
		PreparedStatement searchData;
		ResultSet rsData;
		searchData = conn.prepareStatement("SELECT CopyID,IsLostedOrDelayed,CopyName FROM delayonreturn WHERE MemberID=? ");
		ArrayList<String> dataDetails = new ArrayList<String>();
		searchData.setString(1,msg.get(1));
		rsData = searchData.executeQuery();
		dataDetails.add("getDelayandLostBooks");
		while(rsData.next()) {
			dataDetails.add(rsData.getString(1));//CopyID
			dataDetails.add(rsData.getString(2));//IsLostOrDelayed
			dataDetails.add(rsData.getString(3));//BookID
		}
		if (dataDetails.size()==1) {
			dataDetails.add("NotExist");
			System.out.println("Member not exist");
			return dataDetails;
		}
		else {
			return dataDetails;
		}
	}

	public ArrayList<String> extendLoanPeriodByMember(ArrayList<String> data) throws SQLException, ParseException {
		ArrayList<String> msg = new ArrayList<>();
		msg.add("Get BookID");
		msg.add(data.get(3));
		msg.add(data.get(4));
		msg = inventoryCheckExistence(msg);
		Date dt = new java.util.Date();
		SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = inFormat.format(dt);
		ArrayList<String> extendLoan = new ArrayList<String>();
		extendLoan.add("Extend Loan Period By Member");
		ArrayList<String> checkWanted = new ArrayList<String>();
		checkWanted.add("Check Copy Wanted Status");
		checkWanted.add(msg.get(1));
		checkWanted = isCopyWanted(checkWanted);
		if(checkWanted.size() == 1) {
			extendLoan.add("Book doesn't exist!");
			return extendLoan;
		}
		if(checkWanted.get(4).equals("true")) {
			extendLoan.add("Copy labeled as \"Wanted\" hence can't be extend!");
			return extendLoan;
		}
		ArrayList<String> checkDate = new ArrayList<String>();
		checkDate.add("Check Copy Date");
		checkDate.add(data.get(2));
		checkDate = isCopyLoaned(checkDate);
		if(checkDate.size() == 1) {
			extendLoan.add("Copy isn't loan yet!");
			return extendLoan;
		}

		String copyExpectedReturnDate = checkDate.get(2);
		Date expectedReturnDate = inFormat.parse(copyExpectedReturnDate); 
		Date currentDate = inFormat.parse(currentTime); 
		long diff = expectedReturnDate.getTime() - currentDate.getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000);
		if(diffDays >= 7) {
			extendLoan.add("The copy expected return date is more than a week\nhence extension can't be made!");
			return extendLoan;
		}

		if(checkDate.get(8).equals("true")) {
			extendLoan.add("The copy is reserved hence extension can't be made!");
			return extendLoan;
		}

		Calendar c = Calendar.getInstance();
		c.setTime(expectedReturnDate);
		c.add(Calendar.DATE, 7);
		String newExpectedReturnDate = inFormat.format(c.getTime());


		PreparedStatement ps = conn.prepareStatement("UPDATE loanbook SET ExpectedReturnDate = ? WHERE MemberID = ? AND CopyID = ? AND IsReturned = ?");
		ps.setString(1, newExpectedReturnDate);
		ps.setString(2, data.get(1));
		ps.setString(3, data.get(2));
		ps.setString(4, "false");
		if(ps.executeUpdate() == 0) {
			extendLoan.add("Couldn't update the new expected return date!");
			return extendLoan;
		}


		PreparedStatement ps4 = conn.prepareStatement("SELECT LibrarianID FROM librarian WHERE IsManager = ?");
		ps4.setString(1, "false");
		ResultSet rs1 = ps4.executeQuery();
		while(rs1.next()) {
			PreparedStatement ps5 = conn.prepareStatement("INSERT into usermessages values(?,?,?,?)");
			ps5.setString(1, rs1.getString(1));
			ps5.setString(2, "Book extension");
			ps5.setString(3, currentTime);
			ps5.setString(4, "Member " + data.get(1) + " has extended the loan period of the copy " + data.get(2) + ".");	
			ps5.executeUpdate();
		}

		extendLoan.add(newExpectedReturnDate);
		extendLoan.add("Success");
		return extendLoan;
	}

	public ArrayList<String> extendLoanPeriodByLibrarian(ArrayList<String> data) throws SQLException, ParseException {
		ArrayList<String> msg = new ArrayList<>();
		msg.add("Get BookID");
		msg.add(data.get(3));
		msg.add(data.get(4));
		msg = inventoryCheckExistence(msg);
		Date dt = new java.util.Date();
		SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = inFormat.format(dt);
		ArrayList<String> extendLoan = new ArrayList<String>();
		extendLoan.add("Extend Loan Period By Librarian");
		ArrayList<String> checkWanted = new ArrayList<String>();
		checkWanted.add("Check Copy Wanted Status");
		checkWanted.add(msg.get(1));
		checkWanted = isCopyWanted(checkWanted);
		if(checkWanted.size() == 1) {
			extendLoan.add("Book doesn't exist!");
			return extendLoan;
		}
		if(checkWanted.get(4).equals("true")) {
			extendLoan.add("Copy labeled as \"Wanted\" hence can't be extend!");
			return extendLoan;
		}
		ArrayList<String> checkDate = new ArrayList<String>();
		checkDate.add("Check Copy Date");
		checkDate.add(data.get(2));
		checkDate = isCopyLoaned(checkDate);
		if(checkDate.size() == 1) {
			extendLoan.add("Copy isn't loan yet!");
			return extendLoan;
		}

		String copyExpectedReturnDate = checkDate.get(2);
		Date expectedReturnDate = inFormat.parse(copyExpectedReturnDate); 
		Date currentDate = inFormat.parse(currentTime); 
		long diff = expectedReturnDate.getTime() - currentDate.getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000);
		if(diffDays >= 7) {
			extendLoan.add("The copy expected return date is more than a week\nhence extension can't be made!");
			return extendLoan;
		}

		if(checkDate.get(8).equals("true")) {
			extendLoan.add("The copy is reserved hence extension can't be made!");
			return extendLoan;
		}

		Calendar c = Calendar.getInstance();
		c.setTime(expectedReturnDate);
		c.add(Calendar.DATE, 7);
		String newExpectedReturnDate = inFormat.format(c.getTime());


		PreparedStatement ps = conn.prepareStatement("UPDATE loanbook SET ExpectedReturnDate = ? WHERE MemberID = ? AND CopyID = ? AND IsReturned = ?");
		ps.setString(1, newExpectedReturnDate);
		ps.setString(2, data.get(1));
		ps.setString(3, data.get(2));
		ps.setString(4, "false");
		if(ps.executeUpdate() == 0) {
			extendLoan.add("Couldn't update the new expected return date!");
			return extendLoan;
		}

		PreparedStatement ps1 = conn.prepareStatement("INSERT into manualextend values(?,?,?,?)");
		ps1.setString(1, data.get(1));
		ps1.setString(2, data.get(2));
		ps1.setString(3, currentTime);
		ps1.setString(4, data.get(5));
		if(ps1.executeUpdate() == 0) {
			extendLoan.add("Couldn't insert new row in manualextend!");
			return extendLoan;
		}

		extendLoan.add(newExpectedReturnDate);
		extendLoan.add("Success");
		return extendLoan;
	}

	public ArrayList<String> getStatusHistory(ArrayList<String> msg) throws SQLException {
		PreparedStatement searchData;
		ResultSet rsData;
		searchData = conn.prepareStatement("SELECT PreviousStatus,CurrentStatus,ExecutionDate FROM memberstatus WHERE MemberID=? ");
		ArrayList<String> dataDetails = new ArrayList<String>();
		searchData.setString(1,msg.get(1));
		rsData = searchData.executeQuery();
		dataDetails.add("getStatusHistory");
		while(rsData.next()) {
			dataDetails.add(rsData.getString(1));//PreviousStatus
			dataDetails.add(rsData.getString(2));//CurrentStatus
			dataDetails.add(rsData.getString(3));//ExecutionDate
		}
		if (dataDetails.size()==1) {
			dataDetails.add("NotExist");
			System.out.println("Member status did not changed");
			return dataDetails;
		}
		else {
			System.out.println(dataDetails);
			return dataDetails;
		}
	}

	public ArrayList<String> EmployeeRecords () throws SQLException{
		ArrayList<String> listLibrarians = new ArrayList<String>();
		PreparedStatement stmt = conn.prepareStatement("SELECT LibrarianID,FirstName,LastName,Email,IsManager FROM librarian");
		ResultSet rs = stmt.executeQuery();
		listLibrarians.add("EmployeeRecords");
		while(rs.next()) {
			listLibrarians.add(rs.getString(1));//librarian ID
			listLibrarians.add(rs.getString(2));//first name
			listLibrarians.add(rs.getString(3));//last name
			listLibrarians.add(rs.getString(4));//email
			listLibrarians.add(rs.getString(5));//is manager
		}
		return listLibrarians;
	}

	//data for statistics - late in return
	public ArrayList<String> StatisticsShowBooks (String option,String bookID) throws SQLException, ParseException{

		ArrayList<String> listBooks = new ArrayList<String>();
		listBooks.add("StatisticsBooks");
		listBooks.add(option);

		double sumOfDays=0,sumOfDays2=0,maxOfDays=0,maxOfDays2=0;
		int counter=0,counter2=0;
		int counterEmpty=0;
		Date dt = new java.util.Date();
		Date expectedReturnDate;
		Date actualReturnDate;
		Date LoanDate;
		Date CurrentTime;
		SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd HH");//:mm:ss");
		String currentTime = inFormat.format(dt);
		String copyActualReturnDate;
		String copyExpectedReturnDate;
		String copyLoanDate;
		double diff1,diff2;
		double diffDays,diffHours;

		PreparedStatement st;
		ResultSet rs;

		switch (option) {
		case "Total":

			listBooks.add("labels");

			//Add SUM, COUNT and MAX of days and hours late in return - for books which haven been returned
			sumOfDays=0;
			maxOfDays=0;
			counter=0;
			st = conn.prepareStatement("SELECT ActualReturnDate,ExpectedReturnDate FROM loanbook WHERE IsReturned = 'true' AND ActualReturnDate>ExpectedReturnDate");
			rs = st.executeQuery();

			if (!(rs.isBeforeFirst())) {
				counterEmpty++;
			}

			while(rs.next()) {
				copyActualReturnDate = rs.getString(1);
				copyExpectedReturnDate = rs.getString(2);

				expectedReturnDate = inFormat.parse(copyExpectedReturnDate); 
				actualReturnDate = inFormat.parse(copyActualReturnDate); 

				diff1 = actualReturnDate.getTime() - expectedReturnDate.getTime();
				diffDays = diff1 / (24 * 60 * 60 * 1000);

				diff2 = actualReturnDate.getTime() - expectedReturnDate.getTime();
				diffHours = (diff2 / (60 * 60 * 1000) % 24)*0.01;

				//SUM
				sumOfDays += (diffDays+diffHours);
				//COUNT
				counter++;
				//MAX
				if ((diffDays+diffHours)>maxOfDays) {
					maxOfDays = (diffDays+diffHours);
				}
			}

			//Add SUM, COUNT and MAX of days and hours late in return - for books which yet haven't been returned
			sumOfDays2=0;
			maxOfDays2=0;
			counter2=0;
			CurrentTime = inFormat.parse(currentTime);

			st = conn.prepareStatement("SELECT ExpectedReturnDate FROM loanbook WHERE IsReturned = 'false' AND ExpectedReturnDate < ?");
			st.setString(1,currentTime);

			rs = st.executeQuery();

			if (!(rs.isBeforeFirst())) {
				counterEmpty++;
			}
			if (counterEmpty==2) {
				listBooks.add("Fail");
			}

			while(rs.next()) {
				copyExpectedReturnDate = rs.getString(1);

				expectedReturnDate = inFormat.parse(copyExpectedReturnDate); 

				diff1 = CurrentTime.getTime() - expectedReturnDate.getTime();
				diffDays = (diff1 / (24 * 60 * 60 * 1000));
				String s = String.valueOf(diffDays);
				int indexOfDot = s.indexOf(".");
				String ss = s.substring(0, indexOfDot);
				diffDays = Double.parseDouble(ss);

				diff2 = CurrentTime.getTime() - expectedReturnDate.getTime();
				diffHours = (diff2 / (60 * 60 * 1000) % 24)*0.01;
				s = String.valueOf(diffHours);
				indexOfDot = s.indexOf(".");
				ss = s.substring(0, indexOfDot);
				diffHours = Double.parseDouble(ss);

				//SUM
				sumOfDays2 += (diffDays+diffHours);
				//COUNT
				counter2++;
				//MAX
				if ((diffDays+diffHours)>maxOfDays2) {
					maxOfDays2 = (diffDays+diffHours);
				}
			}

			listBooks.add(String.valueOf(sumOfDays+sumOfDays2));
			listBooks.add(String.valueOf(counter+counter2));

			if(maxOfDays > maxOfDays2)
				listBooks.add(String.valueOf(maxOfDays));
			else
				listBooks.add(String.valueOf(maxOfDays2));


			//Add the difference (days and hours) between ActualReturnDate and ExpectedReturnDate to the SortedSet - for books which have been returned
			st = conn.prepareStatement("SELECT ActualReturnDate,ExpectedReturnDate FROM loanbook WHERE IsReturned = 'true' AND ActualReturnDate>ExpectedReturnDate ORDER BY (ActualReturnDate-ExpectedReturnDate) ASC");
			rs = st.executeQuery();

			SortedSet<Double> sortedLoanCopies1 = new TreeSet<>();

			while(rs.next()) {
				copyActualReturnDate = rs.getString(1);
				copyExpectedReturnDate = rs.getString(2);

				expectedReturnDate = inFormat.parse(copyExpectedReturnDate); 
				actualReturnDate = inFormat.parse(copyActualReturnDate); 

				diff1 = actualReturnDate.getTime() - expectedReturnDate.getTime();
				diffDays = diff1 / (24 * 60 * 60 * 1000);

				diff2 = actualReturnDate.getTime() - expectedReturnDate.getTime();
				diffHours = (diff2 / (60 * 60 * 1000) % 24)*0.01;

				sortedLoanCopies1.add(diffDays+diffHours);//All delayed copies in ascending order
			}


			CurrentTime = inFormat.parse(currentTime);
			//Add the difference (days and hours) between Current Date and ExpectedReturnDate to the SortedSet - for books which yet haven't been returned
			st = conn.prepareStatement("SELECT ExpectedReturnDate FROM loanbook WHERE IsReturned = 'false' AND  ExpectedReturnDate < ? ");
			st.setString(1,currentTime);
			rs = st.executeQuery();

			while(rs.next()) {
				copyExpectedReturnDate = rs.getString(1);
				expectedReturnDate = inFormat.parse(copyExpectedReturnDate); 

				////
				diff1 = CurrentTime.getTime() - expectedReturnDate.getTime();
				diffDays = (diff1 / (24 * 60 * 60 * 1000));
				String s = String.valueOf(diffDays);
				int indexOfDot = s.indexOf(".");
				String ss = s.substring(0, indexOfDot);
				diffDays = Double.parseDouble(ss);
				////

				////
				diff2 = CurrentTime.getTime() - expectedReturnDate.getTime();
				diffHours = (diff2 / (60 * 60 * 1000) % 24)*0.01;
				s = String.valueOf(diffHours);
				indexOfDot = s.indexOf(".");
				ss = s.substring(0, indexOfDot);
				diffHours = Double.parseDouble(ss);
				////


				sortedLoanCopies1.add(diffDays+diffHours);
			}

			for (Double doubleTemp : sortedLoanCopies1) {
				listBooks.add(String.valueOf(doubleTemp));
				System.out.println("List Books:**** "+doubleTemp);
			}



			break;

		case "Specific Book":

			listBooks.add("labels");

			//Add SUM, COUNT and MAX of days and hours late in return - specific book
			sumOfDays=0;
			maxOfDays=0;
			counter=0;
			counterEmpty=0;
			st = conn.prepareStatement("SELECT ActualReturnDate,ExpectedReturnDate FROM loanbook WHERE IsReturned = 'true' AND ActualReturnDate>ExpectedReturnDate AND  CopyID LIKE'"+bookID+"%'");
			rs = st.executeQuery();

			if (!(rs.isBeforeFirst())) {
				counterEmpty++;
			}

			while(rs.next()) {
				copyActualReturnDate = rs.getString(1);
				copyExpectedReturnDate = rs.getString(2);

				expectedReturnDate = inFormat.parse(copyExpectedReturnDate); 
				actualReturnDate = inFormat.parse(copyActualReturnDate); 

				diff1 = actualReturnDate.getTime() - expectedReturnDate.getTime();
				diffDays = diff1 / (24 * 60 * 60 * 1000);

				diff2 = actualReturnDate.getTime() - expectedReturnDate.getTime();
				diffHours = (diff2 / (60 * 60 * 1000) % 24)*0.01;

				//SUM
				sumOfDays += (diffDays+diffHours);
				//COUNT
				counter++;
				//MAX
				if ((diffDays+diffHours)>maxOfDays) {
					maxOfDays = (diffDays+diffHours);
				}
			}


			//Add SUM, COUNT and MAX of days and hours late in return - specific book not yet returned
			sumOfDays2=0;
			maxOfDays2=0;
			counter2=0;
			CurrentTime = inFormat.parse(currentTime);

			st = conn.prepareStatement("SELECT ExpectedReturnDate FROM loanbook WHERE IsReturned = 'false' AND ExpectedReturnDate < ? AND  CopyID LIKE'"+bookID+"%'");
			st.setString(1,currentTime);

			rs = st.executeQuery();

			if (!(rs.isBeforeFirst())) {
				counterEmpty++;
			}
			if (counterEmpty==2) {
				listBooks.add("Fail");
			}

			while(rs.next()) {
				copyExpectedReturnDate = rs.getString(1);

				expectedReturnDate = inFormat.parse(copyExpectedReturnDate); 

				diff1 = CurrentTime.getTime() - expectedReturnDate.getTime();
				diffDays = (diff1 / (24 * 60 * 60 * 1000));
				String s = String.valueOf(diffDays);
				int indexOfDot = s.indexOf(".");
				String ss = s.substring(0, indexOfDot);
				diffDays = Double.parseDouble(ss);

				diff2 = CurrentTime.getTime() - expectedReturnDate.getTime();
				diffHours = (diff2 / (60 * 60 * 1000) % 24)*0.01;
				s = String.valueOf(diffHours);
				indexOfDot = s.indexOf(".");
				ss = s.substring(0, indexOfDot);
				diffHours = Double.parseDouble(ss);

				//SUM
				sumOfDays2 += (diffDays+diffHours);
				//COUNT
				counter2++;
				//MAX
				if ((diffDays+diffHours)>maxOfDays2) {
					maxOfDays2 = (diffDays+diffHours);
				}
			}

			listBooks.add(String.valueOf(sumOfDays+sumOfDays2));
			listBooks.add(String.valueOf(counter+counter2));

			if(maxOfDays > maxOfDays2)
				listBooks.add(String.valueOf(maxOfDays));
			else
				listBooks.add(String.valueOf(maxOfDays2));


			//Add the difference (days and hours) between ActualReturnDate and ExpectedReturnDate to the list - specific returned book
			st = conn.prepareStatement("SELECT ActualReturnDate,ExpectedReturnDate FROM loanbook WHERE IsReturned = 'true' AND ActualReturnDate>ExpectedReturnDate AND CopyID LIKE'"+bookID+"%'");
			rs = st.executeQuery();

			SortedSet<Double> sortedLoanCopies2 = new TreeSet<>();

			while(rs.next()) {
				copyActualReturnDate = rs.getString(1);
				copyExpectedReturnDate = rs.getString(2);

				expectedReturnDate = inFormat.parse(copyExpectedReturnDate); 
				actualReturnDate = inFormat.parse(copyActualReturnDate); 

				diff1 = actualReturnDate.getTime() - expectedReturnDate.getTime();
				diffDays = diff1 / (24 * 60 * 60 * 1000);

				diff2 = actualReturnDate.getTime() - expectedReturnDate.getTime();
				diffHours = (diff2 / (60 * 60 * 1000) % 24)*0.01;

				sortedLoanCopies2.add(diffDays+diffHours);//All delayed copies in ascending order
			}


			CurrentTime = inFormat.parse(currentTime);
			//Add the difference (days and hours) between Current Date and ExpectedReturnDate to the SortedSet - for books which yet haven't been returned
			st = conn.prepareStatement("SELECT ExpectedReturnDate FROM loanbook WHERE IsReturned = 'false' AND  ExpectedReturnDate < ? AND CopyID LIKE'"+bookID+"%'");
			st.setString(1,currentTime);
			rs = st.executeQuery();

			while(rs.next()) {
				copyExpectedReturnDate = rs.getString(1);
				expectedReturnDate = inFormat.parse(copyExpectedReturnDate); 

				////
				diff1 = CurrentTime.getTime() - expectedReturnDate.getTime();
				diffDays = (diff1 / (24 * 60 * 60 * 1000));
				String s = String.valueOf(diffDays);
				int indexOfDot = s.indexOf(".");
				String ss = s.substring(0, indexOfDot);
				diffDays = Double.parseDouble(ss);
				////

				////
				diff2 = CurrentTime.getTime() - expectedReturnDate.getTime();
				diffHours = (diff2 / (60 * 60 * 1000) % 24)*0.01;
				s = String.valueOf(diffHours);
				indexOfDot = s.indexOf(".");
				ss = s.substring(0, indexOfDot);
				diffHours = Double.parseDouble(ss);
				////


				sortedLoanCopies2.add(diffDays+diffHours);
			}

			for (Double doubleTemp : sortedLoanCopies2) {
				listBooks.add(String.valueOf(doubleTemp));
				System.out.println("List Books:**** "+doubleTemp);
			}
			break;

		case "Specific LoanBook":

			listBooks.add("labels");

			//Add SUM, COUNT and MAX of days and hours loans - for books which have been returned
			sumOfDays=0;
			maxOfDays=0;
			counter=0;
			counterEmpty=0;
			st = conn.prepareStatement("SELECT ActualReturnDate,LoanDate FROM loanbook WHERE IsReturned = 'true' AND ActualReturnDate > ExpectedReturnDate AND  CopyID LIKE'"+bookID+"%'");
			rs = st.executeQuery();

			if (!(rs.isBeforeFirst())) {
				counterEmpty++;
			}

			while(rs.next()) {
				copyActualReturnDate = rs.getString(1);
				copyLoanDate = rs.getString(2);

				LoanDate = inFormat.parse(copyLoanDate); 
				actualReturnDate = inFormat.parse(copyActualReturnDate); 

				diff1 = actualReturnDate.getTime() - LoanDate.getTime();
				diffDays = diff1 / (24 * 60 * 60 * 1000);

				diff2 = actualReturnDate.getTime() - LoanDate.getTime();
				diffHours = (diff2 / (60 * 60 * 1000) % 24)*0.01;

				//SUM
				sumOfDays += (diffDays+diffHours);
				//COUNT
				counter++;
				//MAX
				if ((diffDays+diffHours)>maxOfDays) {
					maxOfDays = (diffDays+diffHours);
				}
			}

			//Add SUM, COUNT and MAX of days and hours loans - for books which yet haven't been returned
			sumOfDays2=0;
			maxOfDays2=0;
			counter2=0;
			st = conn.prepareStatement("SELECT LoanDate FROM loanbook WHERE IsReturned = 'false' AND  CopyID LIKE'"+bookID+"%'");
			rs = st.executeQuery();

			if (!(rs.isBeforeFirst())) {
				counterEmpty++;
			}
			if (counterEmpty==2) {
				listBooks.add("Fail");
			}

			while(rs.next()) {
				copyLoanDate = rs.getString(1);

				LoanDate = inFormat.parse(copyLoanDate); 
				CurrentTime = inFormat.parse(currentTime); 

				diff1 = CurrentTime.getTime() - LoanDate.getTime();
				diffDays = (diff1 / (24 * 60 * 60 * 1000));
				String s = String.valueOf(diffDays);
				int indexOfDot = s.indexOf(".");
				String ss = s.substring(0, indexOfDot);
				diffDays = Double.parseDouble(ss);

				diff2 = CurrentTime.getTime() - LoanDate.getTime();
				diffHours = (diff2 / (60 * 60 * 1000) % 24)*0.01;
				s = String.valueOf(diffHours);
				indexOfDot = s.indexOf(".");
				ss = s.substring(0, indexOfDot);
				diffHours = Double.parseDouble(ss);

				//SUM
				sumOfDays2 += (diffDays+diffHours);
				//COUNT
				counter2++;
				//MAX
				if ((diffDays+diffHours)>maxOfDays2) {
					maxOfDays2 = (diffDays+diffHours);
				}
			}

			listBooks.add(String.valueOf(sumOfDays+sumOfDays2));
			listBooks.add(String.valueOf(counter+counter2));

			if(maxOfDays > maxOfDays2)
				listBooks.add(String.valueOf(maxOfDays));
			else
				listBooks.add(String.valueOf(maxOfDays2));

			//Add the difference (days and hours) between LoanDate and Current Date to the SortedSet - for books which have been returned
			sumOfDays=0;
			maxOfDays=0;
			counter=0;
			st = conn.prepareStatement("SELECT ActualReturnDate,LoanDate FROM loanbook WHERE IsReturned = 'true' AND CopyID LIKE'"+bookID+"%'");
			rs = st.executeQuery();

			SortedSet<Double> sortedLoanCopies = new TreeSet<>();

			while(rs.next()) {
				copyActualReturnDate = rs.getString(1);
				copyLoanDate = rs.getString(2);

				LoanDate = inFormat.parse(copyLoanDate); 
				actualReturnDate = inFormat.parse(copyActualReturnDate); 

				diff1 = actualReturnDate.getTime() - LoanDate.getTime();
				diffDays = diff1 / (24 * 60 * 60 * 1000);

				diff2 = actualReturnDate.getTime() - LoanDate.getTime();
				diffHours = (diff2 / (60 * 60 * 1000) % 24)*0.01;

				sortedLoanCopies.add(diffDays+diffHours);
			}

			//Add the difference (days and hours) between LoanDate and Current Date to the SortedSet - for books which yet haven't been returned
			sumOfDays=0;
			maxOfDays=0;
			counter=0;
			st = conn.prepareStatement("SELECT LoanDate FROM loanbook WHERE IsReturned = 'false' AND  CopyID LIKE'"+bookID+"%'");
			rs = st.executeQuery();

			while(rs.next()) {
				copyLoanDate = rs.getString(1);

				LoanDate = inFormat.parse(copyLoanDate); 
				CurrentTime = inFormat.parse(currentTime); 

				////
				diff1 = CurrentTime.getTime() - LoanDate.getTime();
				diffDays = (diff1 / (24 * 60 * 60 * 1000));
				String s = String.valueOf(diffDays);
				int indexOfDot = s.indexOf(".");
				String ss = s.substring(0, indexOfDot);
				diffDays = Double.parseDouble(ss);
				////

				////
				diff2 = CurrentTime.getTime() - LoanDate.getTime();
				diffHours = (diff2 / (60 * 60 * 1000) % 24)*0.01;
				s = String.valueOf(diffHours);
				indexOfDot = s.indexOf(".");
				ss = s.substring(0, indexOfDot);
				diffHours = Double.parseDouble(ss);
				////


				sortedLoanCopies.add(diffDays+diffHours);
			}

			for (Double doubleTemp : sortedLoanCopies) {
				listBooks.add(String.valueOf(doubleTemp));
				System.out.println("List Books:**** "+doubleTemp);
			}
			break;

		default:
			break;
		}

		return listBooks;
	}



	public void memberLostBook(ArrayList<String> data) throws SQLException {
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(dt);
		PreparedStatement insert = conn.prepareStatement("insert into delayonreturn values(?,?,?,?,?)");
		insert.setString(1, data.get(1));//MemberID
		insert.setString(2, data.get(2));//CopyID
		insert.setString(3,"Lost");//IslostedorDelayed
		insert.setString(4, data.get(3));//CopyName
		insert.setString(5,currentTime);//current time
		insert.executeUpdate();			
	}		
	//get BOOKS from DB to show in tableView - for statistics-lateInReturn
	public ArrayList<String> showTableViewBooks () throws SQLException{

		ArrayList<String> listBooks2 = new ArrayList<String>();
		listBooks2.add("showTableView");
		listBooks2.add(" ");
		listBooks2.add("tableView");

		PreparedStatement stmtB3 = conn.prepareStatement("SELECT BookID,BookName,Wanted,AuthorsName,EditionNumber FROM book");
		ResultSet rsB3 = stmtB3.executeQuery();

		while(rsB3.next()) {
			listBooks2.add(rsB3.getString(1));//Book ID
			listBooks2.add(rsB3.getString(2));//Book Name
			listBooks2.add(rsB3.getString(3));//Wanted
			listBooks2.add(rsB3.getString(4));//Authors Name
			listBooks2.add(rsB3.getString(5));//Edition Number
		}
		return listBooks2;
	}
	public ArrayList<String> getInBoxMessage(ArrayList<String> arrayMessage) throws SQLException {
		PreparedStatement MessageData;
		ResultSet rsMessage;
		MessageData = conn.prepareStatement("SELECT ExecutionDate,Topic,Contents FROM usermessages WHERE UserID=? ");
		ArrayList<String> dataDetails = new ArrayList<String>();
		dataDetails.add("InBoxMessage");
		MessageData.setString(1,arrayMessage.get(1));
		rsMessage = MessageData.executeQuery();

		while(rsMessage.next()) {
			dataDetails.add(rsMessage.getString(1));//ExecutionDate
			dataDetails.add(rsMessage.getString(2));//Topic
			dataDetails.add(rsMessage.getString(3));//Contents
		}	
		return dataDetails;
	}

	public static void handleLateLoans() throws SQLException {
		Date dt = new java.util.Date();
		SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = inFormat.format(dt);

		PreparedStatement ps = conn.prepareStatement("SELECT MemberID, CopyID, BookName FROM loanbook WHERE ExpectedReturnDate < ? AND ActualReturnDate = ? AND IsReturned = ?");
		ps.setString(1, currentTime);
		ps.setString(2, null);
		ps.setString(3, "false");
		ResultSet rs = ps.executeQuery();
		if(!rs.isBeforeFirst()) {
			return;
		}
		while(rs.next()) {
			PreparedStatement ps1 = conn.prepareStatement("INSERT into delayonreturn values(?,?,?,?,?)");
			ps.setString(1, rs.getString(1));
			ps.setString(2, rs.getString(2));
			ps.setString(3, "Delayed");
			ps.setString(4, rs.getString(3));
			ps.setString(5, currentTime);
			ps.executeUpdate();

			PreparedStatement ps3 = conn.prepareStatement("INSERT into currentdelayonreturn values(?,?,?)");
			ps3.setString(1, rs.getString(1));
			ps3.setString(2, rs.getString(2));
			ps3.setString(3, rs.getString(3));
			ps3.executeUpdate();

			ArrayList<String> getMemberInfo = new ArrayList<String>();
			getMemberInfo.add("");
			getMemberInfo.add(rs.getString(1));
			getMemberInfo = isMemberExist(getMemberInfo);
			if(getMemberInfo.get(7).equals("Active")) {
				PreparedStatement ps2 = conn.prepareStatement("UPDATE members SET Status = ?, DelayAmount = ?, FreezedOn = ? WHERE MemberID = ?");
				ps2.setString(1, "Frozen");
				ps2.setString(2, Integer.toString(Integer.parseInt(rs.getString(9)) + 1));
				ps2.setString(3, rs.getString(2));
				ps2.setString(4, rs.getString(1));
				ps2.executeUpdate();
			}
			else {
				PreparedStatement ps2 = conn.prepareStatement("UPDATE members SET DelayAmount = ? WHERE MemberID = ?");
				ps2.setString(1, Integer.toString(Integer.parseInt(rs.getString(9)) + 1));
				ps2.setString(2, rs.getString(1));
				ps2.executeUpdate();
			}

			if((Integer.parseInt(rs.getString(9)) + 1) >= 3) {
				PreparedStatement ps4 = conn.prepareStatement("SELECT LibrarianID FROM librarian WHERE IsManager = ?");
				ps4.setString(1, "true");
				ResultSet rs1 = ps4.executeQuery();
				if(rs1.next()) {
					PreparedStatement ps5 = conn.prepareStatement("INSERT into usermessage values(?,?,?,?)");
					ps5.setString(1, (rs1.getString(1)));
					ps5.setString(2, "3 times delay");
					ps5.setString(3, currentTime);
					ps5.setString(4, "Member " + rs.getString(1) + " has been late on return for " + Integer.toString(Integer.parseInt(rs.getString(9)) + 1) + " times.");	
					ps5.executeUpdate();
				}
			}
		}
	}


	public static void handleReminderMessageDayBeforeReturn() throws SQLException, ParseException {
		Date dt = new java.util.Date();
		SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = inFormat.format(dt);

		PreparedStatement ps = conn.prepareStatement("SELECT MemberID, CopyID, BookName, ExpectedReturnDate FROM loanbook WHERE ExpectedReturnDate > ? AND ActualReturnDate = ? AND IsReturned = ?");
		ps.setString(1, currentTime);
		ps.setString(2, null);
		ps.setString(3, "false");
		ResultSet rs = ps.executeQuery();
		if(!rs.isBeforeFirst()) {
			return;
		}
		while(rs.next()) {
			ArrayList<String> getMemberInfo = new ArrayList<String>();
			getMemberInfo.add("");
			getMemberInfo.add(rs.getString(1));
			getMemberInfo = isMemberExist(getMemberInfo);
			String copyExpectedReturnDate = rs.getString(4);
			Date expectedReturnDate = inFormat.parse(copyExpectedReturnDate); 
			Date currentDate = inFormat.parse(currentTime); 
			long diff = expectedReturnDate.getTime() - currentDate.getTime();

			long hours = TimeUnit.MILLISECONDS.toHours(diff);

			if(hours >= 24 && hours <=48) {
				PreparedStatement ps5 = conn.prepareStatement("INSERT into usermessage values(?,?,?,?)");
				ps5.setString(1, (rs.getString(1)));
				ps5.setString(2, "Return book");
				ps5.setString(3, currentTime);
				ps5.setString(4, "The Copy of the book " + rs.getString(3) + "should be returned in the date " + rs.getString(4));	
				ps5.executeUpdate();
			}
		}
	}
	public ArrayList<String> getActivityReport(ArrayList<String> arrayObject) throws SQLException  {
		ArrayList<String>data=new ArrayList<>();
		data.add("getActivityReport");
		int val;
		System.out.println(arrayObject);
		PreparedStatement ps = conn.prepareStatement("SELECT COUNT(DISTINCT MemberID) FROM memberstatus WHERE ExecutionDate>=? AND ExecutionDate<=? AND CurrentStatus=? ");
		ps.setString(1,arrayObject.get(1));//startTime
		ps.setString(2,arrayObject.get(2));//endTime
		ps.setString(3,"Active");//requestedStatus
		ResultSet rs1 = ps.executeQuery();
		if (rs1.next()) {
			System.out.println(rs1.getInt(1));
			data.add(String.valueOf(rs1.getInt(1)));//return number of active members in time area between startDate and endDate 
		}
		ps.setString(3,"Frozen");//requestedStatus
		ResultSet rs2 = ps.executeQuery();
		if (rs2.next()) {
			System.out.println(rs2.getInt(1));
			data.add(String.valueOf(rs2.getInt(1)));//return number of frozen members in time area between startDate and endDate 
		}
		ps.setString(3,"Locked");//requestedStatus
		ResultSet rs3 = ps.executeQuery();
		if (rs3.next()) {
			System.out.println(rs3.getInt(1));
			data.add(String.valueOf(rs3.getInt(1)));//return number of locked members in time area between startDate and endDate 
		}
		PreparedStatement ps2 = conn.prepareStatement("SELECT COUNT(DISTINCT CopyID) FROM loanbook WHERE LoanDate>=? AND LoanDate<=? ");
		ps2.setString(1,arrayObject.get(1));//startTime
		ps2.setString(2,arrayObject.get(2));//endTime
		ResultSet rs4 = ps2.executeQuery();
		if (rs4.next()) {
			System.out.println(rs4.getInt(1));
			data.add(String.valueOf(rs4.getInt(1)));//return num of copies loaned in the between startDate and endDate 
		}
		PreparedStatement ps3 = conn.prepareStatement("SELECT COUNT(DISTINCT MemberID) FROM delayonreturn WHERE ExecutionDate>=? AND ExecutionDate<=? AND IsLostedOrDelayed=? ");
		ps3.setString(1,arrayObject.get(1));//startTime
		ps3.setString(2,arrayObject.get(2));//endTime
		ps3.setString(3,"Delay");//endTime
		ResultSet rs5 = ps3.executeQuery();
		if (rs5.next()) {
			System.out.println(rs5.getInt(1));
			data.add(String.valueOf(rs5.getInt(1)));//return number of members that delay on return in between startDate and endDate 
		}
		updateActivityReportTable(data,arrayObject);
		return data;
	}

	private void updateActivityReportTable(ArrayList<String> data, ArrayList<String> arrayObject) throws SQLException {
		Date dt = new java.util.Date();
		SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = inFormat.format(dt);
		PreparedStatement ps1 = conn.prepareStatement("INSERT into activityreport values(?,?,?,?,?,?,?,?)");
		ps1.setString(1, currentTime);
		ps1.setString(2, arrayObject.get(1));//startTime
		ps1.setString(3, arrayObject.get(2));//endTime
		ps1.setString(4, data.get(1));//numActive
		ps1.setString(5, data.get(2));//numFreeze
		ps1.setString(6, data.get(3));//numLocked
		ps1.setString(7, data.get(4));//numLoanCopies
		ps1.setString(8, data.get(5));//numDelayonReturn
		ps1.executeUpdate();
		
	}
	public ArrayList<String> getActivityReportHistory(ArrayList<String> arrayObject) throws SQLException {
		PreparedStatement searchData = conn.prepareStatement("SELECT* FROM activityreport");
		ResultSet rsData;
		ArrayList<String> dataDetails = new ArrayList<String>();
		rsData = searchData.executeQuery();
		dataDetails.add("getDelayandLostBooks");
		while(rsData.next()) {
			dataDetails.add(rsData.getString(1));//CurrentTime
			dataDetails.add(rsData.getString(2));////startTime
			dataDetails.add(rsData.getString(3));//endTime
			dataDetails.add(rsData.getString(4));//numActive
			dataDetails.add(rsData.getString(5));//numFreeze
			dataDetails.add(rsData.getString(6));//numLocked
			dataDetails.add(rsData.getString(7));//numLoanCopies
			dataDetails.add(rsData.getString(8));//numDelayonReturn
		}
		if (dataDetails.size()==1) {
			dataDetails.add("NotExist");
			System.out.println("Report not exist");
			return dataDetails;
		}
		else {
			return dataDetails;
		}
	}
	private static Connection connectToDatabase() {
		try 
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}

		try 
		{
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/obl","root","Aa123456");
			System.out.println("SQL connection succeed");
			return conn;
		} catch (SQLException ex) 
		{	/* handle any errors*/
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return null;
	}


	
}
