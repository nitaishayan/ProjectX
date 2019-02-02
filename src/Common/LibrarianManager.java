package Common;

public class LibrarianManager extends Librarian {
	/**
	 * This class represents the Librarian Manager object.
	 */
	
	
	/**
	 * Constructor to the LibrarianManager object.
	 * @param userName - The LibrarianManager's name.
	 * @param memberID - The LibrarianManager's ID.
	 * @param phoneNumber - The LibrarianManager's phoneNumber.
	 * @param email - The LibrarianManager's email.
	 * @param password - The LibrarianManager's password.
	 * @param firstName - The LibrarianManager's firstName.
	 * @param lastName - The LibrarianManager's lastName.
	 * @param manager - Describes if the Librarian is Librarian Manager("true") or not("false").
	 */
	public LibrarianManager(String userName, String memberID, String phoneNumber, String email, String password, String firstName,
			String lastName,String manager) {
		super(userName,phoneNumber,email,password,firstName,lastName,memberID,manager);
	}
}
