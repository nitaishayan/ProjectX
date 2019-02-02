package Common;

public class Librarian extends User {
	/**
	 * This class represents the Librarian object.
	 */
	
	
	private String IsManager;
	/**
	 * Constructor to the Librarian object.
	 * @param memberID - The Librarian's ID.
	 * @param phoneNumber - The Librarian's phoneNumber.
	 * @param email - The Librarian's email.
	 * @param password - The Librarian's password.
	 * @param firstName - The Librarian's firstName.
	 * @param lastName - The Librarian's lastName.
	 * @param manager - Describes if the Librarian is Librarian Manager("true") or not("false").
	 */
	public Librarian(String memberID, String phoneNumber, String email, String password, String firstName,
			String lastName,String isLoggedIn,String manager) {
		super(memberID,phoneNumber,email,password,firstName,lastName,isLoggedIn);
		this.IsManager = manager;
	}
	/**
	 * Getter to the parameter IsManager which describes if the librarian is a librarian manager or not.
	 * @return - return true or false if it's a librarian manager or not.
	 */
	public String getIsManager() {
		return IsManager;
	}

	/**
	 * Setter to the parameter IsManager which describes if the librarian is a librarian manager or not.
	 * @param isManager - true or false if it's a librarian manager or not.
	 */
	public void setIsManager(String isManager) {
		IsManager = isManager;
	}
}
