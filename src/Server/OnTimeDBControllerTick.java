package Server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class handles the methods which run on a defined time (hours, minutes, seconds, and so on...).
 */
public class OnTimeDBControllerTick {

	private static DBController dbController = null;


	/**
	 * This method creates a thread pool which run defined methods to our choice in times we choose. 
	 */
	public static void runServerApplications() {
		dbController = DBController.getInstance();
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
		executor.scheduleAtFixedRate(() -> {
			try {
				handleLateLoans();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}, 0, 1, TimeUnit.MINUTES);
		executor.scheduleAtFixedRate(() -> {
			try {
				handleReminderMessageDayBeforeReturn();
			} catch (SQLException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}, 0, 1, TimeUnit.MINUTES);
	}


	/**
	 * This method handles books that their expected return date exceed the current date.
	 * If so, perform a number of queries to handle this situation, else does nothing.
	 * @throws SQLException
	 */
	public static void handleLateLoans() throws SQLException {
		Date dt = new java.util.Date();
		SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = inFormat.format(dt);
		ArrayList<String> changeMemberStatus = new ArrayList<String>();
		PreparedStatement ps = DBController.conn.prepareStatement("SELECT MemberID, CopyID, BookName, LoanDate FROM loanbook WHERE ExpectedReturnDate < ? AND IsReturned = ? AND HandleLateLoans = ?");
		ps.setString(1, currentTime);
		ps.setString(2, "false");
		ps.setString(3, "false");
		ResultSet rs = ps.executeQuery();
		if(!rs.isBeforeFirst()) {
			return;
		}
		while(rs.next()) {
			PreparedStatement ps10 = DBController.conn.prepareStatement("UPDATE loanbook SET HandleLateLoans = ? WHERE MemberID = ? AND CopyID = ? AND LoanDate = ?");
			ps10.setString(1, "true");
			ps10.setString(2, rs.getString(1));
			ps10.setString(3, rs.getString(2));
			ps10.setString(4, rs.getString(4));
			ps10.executeUpdate();

			PreparedStatement ps1 = DBController.conn.prepareStatement("INSERT into delayonreturn values(?,?,?,?,?)");
			ps1.setString(1, rs.getString(1));
			ps1.setString(2, rs.getString(2));
			ps1.setString(3, "Delayed");
			ps1.setString(4, rs.getString(3));
			ps1.setString(5, currentTime);
			ps1.executeUpdate();

			PreparedStatement ps3 = DBController.conn.prepareStatement("INSERT into currentdelayonreturn values(?,?,?)");
			ps3.setString(1, rs.getString(1));
			ps3.setString(2, rs.getString(2));
			ps3.setString(3, rs.getString(3));
			ps3.executeUpdate();

			ArrayList<String> getMemberInfo = new ArrayList<String>();
			getMemberInfo.add("");
			getMemberInfo.add(rs.getString(1));
			getMemberInfo = DBController.getInstance().isMemberExist(getMemberInfo);
			if(getMemberInfo.get(7).equals("Active")) {
				PreparedStatement ps2 = DBController.conn.prepareStatement("UPDATE members SET DelayAmount = ?, FreezedOn = ? WHERE MemberID = ?");
				ps2.setString(1, Integer.toString(Integer.parseInt(getMemberInfo.get(9)) + 1));
				ps2.setString(2, rs.getString(2));
				ps2.setString(3, rs.getString(1));
				ps2.executeUpdate();
				changeMemberStatus.add("");
				changeMemberStatus.add(rs.getString(1));
				changeMemberStatus.add("Active");
				changeMemberStatus.add("Frozen");
				DBController.getInstance().changeMemberStatus(changeMemberStatus);
				changeMemberStatus.clear();
			}
			else {
				PreparedStatement ps6 = DBController.conn.prepareStatement("UPDATE members SET DelayAmount = ? WHERE MemberID = ?");
				ps6.setString(1, Integer.toString(Integer.parseInt(getMemberInfo.get(9)) + 1));
				ps6.setString(2, rs.getString(1));
				ps6.executeUpdate();
			}

			if((Integer.parseInt(getMemberInfo.get(9)) + 1) >= 3) {
				PreparedStatement ps4 = DBController.conn.prepareStatement("SELECT LibrarianID FROM librarian WHERE IsManager = ?");
				ps4.setString(1, "true");
				ResultSet rs1 = ps4.executeQuery();
				if(rs1.next()) {
					PreparedStatement ps5 = DBController.conn.prepareStatement("INSERT into usermessages values(?,?,?,?)");
					ps5.setString(1, (rs1.getString(1)));
					ps5.setString(2, "3 times delay");
					ps5.setString(3, currentTime);
					ps5.setString(4, "Member " + rs.getString(1) + " has been late on return for " + (Integer.parseInt(getMemberInfo.get(9)) + 1) + " times and a hearing should be conducted");	
					ps5.executeUpdate();
				}
			}
		}
	}


	/**
	 * This method handles copies that their expected return date is in a range of 24 to 48 hours.
	 * If a copy is in that range that related member receives a message.
	 * @throws SQLException
	 * @throws ParseException
	 */
	public static void handleReminderMessageDayBeforeReturn() throws SQLException, ParseException {
		Date dt = new java.util.Date();
		SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = inFormat.format(dt);

		PreparedStatement ps = DBController.conn.prepareStatement("SELECT MemberID, CopyID, BookName, ExpectedReturnDate, LoanDate FROM loanbook WHERE ExpectedReturnDate > ? AND IsReturned = ? AND HandleReminderMessageDayBeforeReturn = ?");
		ps.setString(1, currentTime);
		ps.setString(2, "false");
		ps.setString(3, "false");
		ResultSet rs = ps.executeQuery();
		if(!rs.isBeforeFirst()) {
			return;
		}
		while(rs.next()) {
			ArrayList<String> getMemberInfo = new ArrayList<String>();
			getMemberInfo.add("");
			getMemberInfo.add(rs.getString(1));
			getMemberInfo = DBController.getInstance().isMemberExist(getMemberInfo);
			String copyExpectedReturnDate = rs.getString(4);
			Date expectedReturnDate = inFormat.parse(copyExpectedReturnDate); 
			Date currentDate = inFormat.parse(currentTime); 
			long diff = expectedReturnDate.getTime() - currentDate.getTime();

			long hours = TimeUnit.MILLISECONDS.toHours(diff);

			if(hours >= 24 && hours <=48) {
				PreparedStatement ps10 = DBController.conn.prepareStatement("UPDATE loanbook SET HandleReminderMessageDayBeforeReturn = ? WHERE MemberID = ? AND CopyID = ? AND LoanDate = ?");
				ps10.setString(1, "true");
				ps10.setString(2, rs.getString(1));
				ps10.setString(3, rs.getString(2));
				ps10.setString(4, rs.getString(5));
				ps10.executeUpdate();
				
				PreparedStatement ps5 = DBController.conn.prepareStatement("INSERT into usermessages values(?,?,?,?)");
				ps5.setString(1, (rs.getString(1)));
				ps5.setString(2, "Return book");
				ps5.setString(3, currentTime);
				ps5.setString(4, "The Copy of the book " + rs.getString(3) + " should be returned in the date " + rs.getString(4));	
				ps5.executeUpdate();
			}
		}
	}
}
