package logic;

import java.util.ArrayList;

public class LoanController {

	public static void getCurrentLoans(String memberID) {
		ArrayList<String> loansArray = new ArrayList<>();
		loansArray.add("CurrentLoans");
		loansArray.add(memberID);
		Main.client.handleMessageFromClientUI(loansArray);
	}
}
