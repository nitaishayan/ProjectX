package logic;

import java.util.ArrayList;

/**
* This class is a Controller that represents the loan requests.
**/
public class LoanController {

	/**
		* This method receives the get current loans request from the user and send it to the server.
	 * @param memberID - member's id.
	 */
	public static void getCurrentLoans(String memberID) {
		ArrayList<String> loansArray = new ArrayList<>();
		loansArray.add("CurrentLoans");
		loansArray.add(memberID);
		Main.client.handleMessageFromClientUI(loansArray);
	}
}
