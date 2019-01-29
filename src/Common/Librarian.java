package Common;

public class Librarian extends User {
	
	
	
	private String IsManager;
	
	public Librarian(String memberID, String phoneNumber, String email, String password, String firstName,
			String lastName,String isLoggedIn,String manager) {
		super(memberID,phoneNumber,email,password,firstName,lastName,isLoggedIn);
		this.IsManager = manager;
	}
	
	public String getIsManager() {
		return IsManager;
	}



	public void setIsManager(String isManager) {
		IsManager = isManager;
	}
}
