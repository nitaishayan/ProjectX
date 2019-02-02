package Common;


public class User  {
	/**
	 * This class represents the User object.
	 */
	
	private String id;
	private String phoneNumber;
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String isLoggedIn;

	/**
	 * Constructor to the User object.
	 * @param id - The user's ID.
	 * @param phoneNumber - The user's phoneNumber.
	 * @param email - The user's email.
	 * @param password - The user's password.
	 * @param firstName - The user's firstName.
	 * @param lastName - The user's lastName.
	 * @param isLoggedIn - Describes if the User is logged-in in the system("true") or not("false").
	 */
	public User(String id, String phoneNumber, String email, String password, String firstName, String lastName, String isLoggedIn) {
		this.id=id;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.isLoggedIn = isLoggedIn;
	}
		
	/**
	 * Getter to the parameter phoneNumber.
	 * @return - return the user's phone number.
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	/**
	 * Setter to the parameter phoneNumber.
	 * @param phoneNumber - user's phone number.
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	/**
	 * Getter to the parameter email.
	 * @return - return the user's email.
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Setter to the parameter email.
	 * @param email - user's email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Getter to the parameter password.
	 * @return - return the user's password.
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Setter to the parameter password.
	 * @param password - user's password.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Getter to the parameter firstName.
	 * @return - return the user's firstName.
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Setter to the parameter firstName.
	 * @param firstName - user's firstName.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Getter to the parameter lastName.
	 * @return - return the user's lastName.
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Setter to the parameter lastName.
	 * @param lastName - user's lastName.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Getter to the parameter isLoggedIn.
	 * @return - return if the user's is logged-in or not to the system.
	 */
	public String getIsLoggedIn() {
		return isLoggedIn;
	}

	/**
	 * Setter to the parameter isLoggedIn.
	 * @param isLoggedIn - Describes if the user's is logged-in or not to the system.
	 */
	public void setIsLoggedIn(String isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	/**
	 * Getter to the parameter id.
	 * @return - return the user's id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Setter to the parameter id.
	 * @param id - user's id.
	 */
	public void setId(String id) {
		this.id = id;
	}
	
}
