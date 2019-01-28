package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.mysql.fabric.xmlrpc.base.Data;
import com.mysql.jdbc.UpdatableResultSet;
import Client.Client;
import Common.Book;
import Common.InventoryBook;
import GUI.LibrarianMenuGUI;
import logic.CommonController;
import sun.awt.windows.ThemeReader;


//tt

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
	public static int registretion(ArrayList<String> data) throws SQLException
	{
		PreparedStatement preparedRegistretion;
		ResultSet rsRegistretion;

		preparedRegistretion = conn.prepareStatement("SELECT  * FROM members WHERE MemberID=? ");
		preparedRegistretion.setString(1, data.get(2));
		rsRegistretion = preparedRegistretion.executeQuery();
		if ((rsRegistretion.isBeforeFirst()))
		{
			return 0;
		}

		preparedRegistretion = conn.prepareStatement("SELECT  * FROM members WHERE PhoneNumber=? ");
		preparedRegistretion.setString(1, data.get(1));
		rsRegistretion = preparedRegistretion.executeQuery();
		if ((rsRegistretion.isBeforeFirst()))
		{
			return 0;
		}

		preparedRegistretion = conn.prepareStatement("SELECT  * FROM librarian WHERE LibrarianID=? ");
		preparedRegistretion.setString(1, data.get(2));
		rsRegistretion = preparedRegistretion.executeQuery();
		if ((rsRegistretion.isBeforeFirst()))
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


	public static int RemoveCopy(ArrayList<String> data) throws SQLException{
		String bookID,copyid,memberid;
		int answer=0;
		copyid=data.get(1);
		PreparedStatement getbookid = conn.prepareStatement("SELECT BookID FROM copies WHERE CopyID=?");
		getbookid.setString(1, copyid);
		ResultSet rs = getbookid.executeQuery();
		if(rs.next()) {
			bookID=rs.getString(1);
		}
		else return 0;

		PreparedStatement reserved = conn.prepareStatement("SELECT IsActive,MemberID FROM reservations WHERE CopyID=? AND IsActive='true'");
		reserved.setString(1, copyid);
		ResultSet rs2 = reserved.executeQuery();
		if(rs2.next()) {
			memberid=rs2.getString(2);
			PreparedStatement delete = conn.prepareStatement("UPDATE reservations SET IsActive='false' where  CopyID=? AND IsActive='true'");
			delete.setString(1, copyid);
			delete.executeUpdate();//             add send message to member that his reservation is cancelled
			//                                   donttttttttt forgeeeetttttttttt  !!!!!!!!!!!!!!!!!!!!!!!!!
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
		return res;
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

	public static ArrayList<String> returnBook(ArrayList<String> data) throws SQLException {
		java.util.Date dt = new java.util.Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(dt);
		ArrayList<String> returnBook = new ArrayList<>();
		returnBook.add("Return Book");
		ResultSet rs;
		PreparedStatement ps = conn.prepareStatement("UPDATE copies SET isLoaned = ?, ActualReturnDate = ? WHERE CopyID = ?");
		ps.setString(1, "false");
		ps.setString(2, currentTime);
		ps.setString(3, data.get(1));
		PreparedStatement ps1 = conn.prepareStatement("UPDATE loanbook SET isReturned = ?, ActualReturnDate = ? WHERE CopyID = ? AND isReturned = ?");
		ps1.setString(1, "true");
		ps1.setString(2, currentTime);
		ps1.setString(3, data.get(1));
		ps1.setString(4, "false");
		PreparedStatement ps2 = conn.prepareStatement("SELECT COUNT(*) from reservations WHERE CopyID = ? AND CopyReadyForPickUpDate = ? AND IsActive = ?");
		ps2.setString(1, data.get(1));
		ps2.setString(2,null);
		ps2.setString(3, "true");
		rs = ps2.executeQuery();
		if(rs.isBeforeFirst()) {
			PreparedStatement ps22 = conn.prepareStatement("UPDATE reservations SET CopyReadyForPickUpDate = ?");
			ps22.setString(1, currentTime);
			ps22.executeUpdate();
		}

		if(!data.get(2).equals("Active")) {
			String memberID = null;
			String copyID = null;
			String newCopyID = null;

			PreparedStatement ps3 = conn.prepareStatement("SELECT MemberID from delayonreturn WHERE CopyID = ?");
			ps3.setString(1, data.get(1));
			rs = ps3.executeQuery();
			if(rs.next()) {
				memberID = rs.getString(1);
			}

			PreparedStatement ps4 = conn.prepareStatement("DELETE from delayonreturn WHERE CopyID = ?");
			ps4.setString(1, data.get(1));
			ps4.executeUpdate();

			PreparedStatement ps5 = conn.prepareStatement("SELECT FreezedOn from members WHERE MemberID = ?");
			ps5.setString(1, memberID);
			rs = ps5.executeQuery();
			if(rs.next()) {
				copyID = rs.getString(1);
			}

			PreparedStatement ps6 = conn.prepareStatement("SELECT CopyID from delayonreturn WHERE MemberID = ?");
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
			System.out.println("1111111111111111111"+ bookID);
			shelfLocation =rs1.getString(2);
			System.out.println("1111111111111111111" + shelfLocation);
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
				System.out.println("2222222222222222222" + returnDate);
				System.out.println("2222222222222222222" + memberID);
				System.out.println("2222222222222222222" + copyID);
			}
			msg.add("0");
			msg.add(returnDate);
			msg.add(memberID);
			msg.add(bookID);
			msg.add(copyID);
		}
		else {
			System.out.println("7777777777");
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
		PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM delayonreturn WHERE MemberID = ?");
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
		ArrayList<String> changeStatus = new ArrayList<>();
		changeStatus.add("Change Member Status");
		PreparedStatement ps = conn.prepareStatement("UPDATE members SET Status = ? WHERE MemberID = ?");
		ps.setString(1, data.get(2));
		ps.setString(2, data.get(1));
		if(ps.executeUpdate() != 0) {
			changeStatus.add(data.get(2));
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

		String returnDate = sdf.format(c.getTime());
		ArrayList<String> loanBook = new ArrayList<>();
		loanBook.add("Loan Book");
		PreparedStatement ps = conn.prepareStatement("INSERT loanbook values(?,?,?,?,?,?,?,?,?)");
		ps.setString(1, data.get(1));
		ps.setString(2, returnDate);
		ps.setString(3, null);
		ps.setString(4, data.get(4));
		ps.setString(5, data.get(3));
		ps.setString(6,currentTime);
		ps.setString(7,"false");
		ps.setString(8,"false");
		ps.setString(9, data.get(5));
		if(ps.executeUpdate() == 0) {
			return loanBook;
		}

		PreparedStatement ps1 = conn.prepareStatement("UPDATE copies SET isLoaned = ? , ActualReturnDate = ? WHERE CopyID = ?");
		ps1.setString(1, "true");
		ps1.setString(2, null);
		ps1.setString(3, data.get(1));
		ps1.executeUpdate();
		if(ps1.executeUpdate() == 0) {
			return loanBook;
		}

		loanBook.add(currentTime);
		loanBook.add(returnDate);
		return loanBook;
	}

	public Object viewPersonalHistory(ArrayList<String> searchData) throws SQLException {
		PreparedStatement searchLoan;
		//PreparedStatement searchBookName;
		ResultSet rsLoan;
		//ResultSet rsBook;
		searchLoan = conn.prepareStatement("SELECT CopyID,LoanDate,BookName,ActualReturnDate FROM loanbook WHERE MemberID=? ORDER BY ActualReturnDate DESC");
		//searchBookName = conn.prepareStatement("SELECT BookName FROM book WHERE BookID=? ");
		ArrayList<String> loanDetails = new ArrayList<String>();
		searchLoan.setString(1,searchData.get(1));
		rsLoan = searchLoan.executeQuery();
		loanDetails.add("ViewPersonalHistory");
		while(rsLoan.next()) {
			loanDetails.add(rsLoan.getString(1));//CopyID
			loanDetails.add(rsLoan.getString(2));//LoanDate
			loanDetails.add(rsLoan.getString(3));//BookID
			loanDetails.add(rsLoan.getString(4));//ActualReturnDate
			//ArrayList<String> BookID =new ArrayList<String>();
			//BookID.add(rsLoan.getString(3));//BookID
			//searchBookName.setString(1,BookID.get(0));
			//rsBook = searchBookName.executeQuery();
			//			if (rsBook.next()) {
			//				loanDetails.add(rsBook.getString(1));//BookName
			//			}
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
		int copies,answer,reserveamount;
		String bookID=bookdata.get(1),currentTime;
		String copyID = null;
		System.out.println(bookID);
		PreparedStatement ps = conn.prepareStatement("SELECT copies FROM book WHERE BookID = ?");
		ps.setString(1,bookID);
		ResultSet rs= ps.executeQuery();
		if(rs.next()) {
			copies=rs.getInt(1);
		}
		else {
			System.out.println("cannot retrieve copies from book table.");
			bookdata.add("fail-1");
			return bookdata;
		}
		System.out.println(copies);
		PreparedStatement ps2 = conn.prepareStatement("SELECT COUNT(*) FROM reservations WHERE BookID=? AND IsActive='true' ");
		ps2.setString(1,bookID);
		ResultSet rs1= ps2.executeQuery();
		if(rs1.next()) {
			reserveamount=rs1.getInt(1);
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
			ResultSet rs2=ps3.executeQuery();
			if (rs2.next()) {
				copyID=rs2.getString(1);
			}
			else {
				bookdata.add("fail-2");
				return bookdata;
			}
			PreparedStatement insert = conn.prepareStatement("insert into reservations values(?,?,?,?,?)");
			insert.setString(1, copyID);
			insert.setString(2, null);
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

	//	public ArrayList<String> changeMemberToGraduated(ArrayList<String> data) throws SQLException {
	//		ResultSet rs;
	//		PreparedStatement ps = conn.prepareStatement("UPDATE members SET IsGraduated = ? WHERE MemberID = ?");
	//		ps.setString(1, "true");
	//		ps.setString(2, data.get(?)); // ? == memberID
	//		if(ps.executeUpdate() == 0) {
	//			//error
	//		}
	//		
	//		PreparedStatement ps1 = conn.prepareStatement("INSERT into graduatedstudent values(?,?,?)");
	//		ps1.setString(1, x); // x == memberID
	//		ps1.setString(2, y); // y == First Name
	//		ps1.setString(3, z); // z == Last Name
	//		if(ps1.executeUpdate() == 0) {
	//			//error
	//		}
	//		return null;
	//	}

	public ArrayList<String> getCurrentLoans(ArrayList<String> searchData) throws SQLException {
		PreparedStatement searchLoan,searchAuthorName;
		ResultSet rsLoan,rsAuthorName;
		searchLoan = conn.prepareStatement("SELECT CopyID,BookName,LoanDate,ActualReturnDate,BookID FROM loanbook WHERE MemberID=? AND IsReturned = 'false' ORDER BY ActualReturnDate DESC");
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
			currentLoans.add(rsLoan.getString(4));//ActualReturnDate	
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
