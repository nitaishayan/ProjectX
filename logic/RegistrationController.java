package logic;

import java.io.IOException;
import java.util.ArrayList;
import Client.Client;
import GUI.OBLcontroller;

public class RegistrationController {

	private static final int ID_SIZE = 9;

	public  static void registration(String phoneNumber,String id,String lastName,String firstName,String Email, String password)
	{

		ArrayList<String> registrationData = new ArrayList<>();
		registrationData.add("Registration");
		registrationData.add(phoneNumber);
		registrationData.add(id);
		registrationData.add(lastName);
		registrationData.add(firstName);
		registrationData.add(Email);
		registrationData.add(password);
		Main.client.handleMessageFromClientUI(registrationData);
	}

	public  static void login(String id,String password)
	{
		ArrayList<String> loginData = new ArrayList<>();
		loginData.add("Login");
		loginData.add(id);
		loginData.add(password);
		Main.client.handleMessageFromClientUI(loginData);
	}

	public static boolean checkMemberExistence(String memberID) throws Exception {

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
		return false;
	}

	public static void searchMember(String memberID) {
		ArrayList<String> searchData=new ArrayList<>();
		searchData.add("SearchMember");
		searchData.add(memberID);
		Main.client.handleMessageFromClientUI(searchData);
	}
	
	public  static void logout(String id,String password)
	{
		ArrayList<String> logoutData = new ArrayList<>();
		logoutData.add("Logout");
		logoutData.add(id);
		logoutData.add(password);
		System.out.println(logoutData+"inside registrationController");
		Main.client.handleMessageFromClientUI(logoutData);
	}
	public  static void updateMemberDetails(String ID,String phoneNumber, String Email) {
		ArrayList<String> updateData = new ArrayList<>();
		
		updateData.add("MemberUpdateMemberDetails");
		updateData.add(ID);
		updateData.add(phoneNumber);
		updateData.add(Email);
		Main.client.handleMessageFromClientUI(updateData);

	}
}
