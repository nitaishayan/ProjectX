package Common;

public class Member extends User{
	private String status;
	private String notes;
	private String freezedOn;
	private String delayAmount;
	private String isGraduated;



	public Member(String id, String phoneNumber, String email, String password, String firstName,
			String lastName,String status, String notes, String delayAmount, String isLoggedIn, String freezedOn, String isGraduated) {
		super(id,phoneNumber,email,password,firstName,lastName,isLoggedIn);
		this.status = status;
		this.notes = notes;
		this.setDelayAmount(delayAmount);
		this.freezedOn = freezedOn;
		this.setIsGraduated(isGraduated);
	}

	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getFreezedOn() {
		return freezedOn;
	}


	public void setFreezedOn(String freezedOn) {
		this.freezedOn = freezedOn;
	}


	public String getNotes() {
		return notes;
	}


	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getDelayAmount() {
		return delayAmount;
	}

	public void setDelayAmount(String delayAmount) {
		this.delayAmount = delayAmount;
	}

	public String getIsGraduated() {
		return isGraduated;
	}

	public void setIsGraduated(String isGraduated) {
		this.isGraduated = isGraduated;
	}
}
