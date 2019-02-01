package logic;

import java.io.IOException;
import java.util.ArrayList;
import Client.Client;
import GUI.OBLcontroller;


public class RegistrationController {
	/**
	 * This class (RegistrationController) is of type Controller to handle specific functions regarding registration and login.
	 * Is a connector between the GUI and the server, the message is sent with the method handleMessageFromClientUI();
	 */
	
	private static final int ID_SIZE = 9;

	/**
	 * This method sends request for the server for login registration.
	 * @param phoneNumber - member's phone number.
	 * @param id - member's id.
	 * @param lastName - member's last name.
	 * @param firstName - member's first name.
	 * @param Email - member's email.
	 * @param password - member's password.
	 */
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

	/**
	 * This method sends request for the server for login.
	 * @param id - the user's id.
	 * @param password - the user's password
	 */
	public  static void login(String id,String password)
	{
		ArrayList<String> loginData = new ArrayList<>();
		loginData.add("Login");
		loginData.add(id);
		loginData.add(password);
		Main.client.handleMessageFromClientUI(loginData);
	}

	/**
	 * This method sends request for the server for checking if the member exists in the system.
	 * @param memberID - member's id.
	 * @return return a boolean value if the member exists or not.
	 * @throws Exception
	 */
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


	/**
	 * This method sends request for the server for log-out.
	 * @param id - the user's id.
	 * @param password - the user's password
	 */
	public  static void logout(String id,String password)
	{
		ArrayList<String> logoutData = new ArrayList<>();
		logoutData.add("Logout");
		logoutData.add(id);
		logoutData.add(password);
		System.out.println(logoutData+"inside registrationController");
		Main.client.handleMessageFromClientUI(logoutData);
	}

}
