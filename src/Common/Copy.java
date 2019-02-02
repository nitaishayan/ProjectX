package Common;


/**
 * This class represents the Copy object.
 */
public class Copy {
	private String copyID;
	private String copyName;
	private String isLoaned;
	private String bookID;
	private String actucalReturnDate;
	
	/**
	 * Constructor to the Book object.
	 * @param copyID - The copy's ID.
	 * @param copyName - The copy's name.
	 * @param isLoaned - Describes if the copy is loaned or not.
	 * @param bookID - The book's ID.
	 * @param actucalReturnDate - The copy's actual return date.
	 */
	public Copy(String copyID, String copyName, String isLoaned, String bookID, String actucalReturnDate) {
		super();
		this.copyID = copyID;
		this.copyName = copyName;
		this.isLoaned = isLoaned;
		this.bookID = bookID;
		this.actucalReturnDate = actucalReturnDate;
	}
	
	public String getCopyID() {
		return copyID;
	}
	public void setCopyID(String copyID) {
		this.copyID = copyID;
	}
	public String getCopyName() {
		return copyName;
	}
	public void setCopyName(String copyName) {
		this.copyName = copyName;
	}
	public String getIsLoaned() {
		return isLoaned;
	}
	public void setIsLoaned(String isLoaned) {
		this.isLoaned = isLoaned;
	}
	public String getBookID() {
		return bookID;
	}
	public void setBookID(String bookID) {
		this.bookID = bookID;
	}
	public String getActucalReturnDate() {
		return actucalReturnDate;
	}
	public void setActucalReturnDate(String actucalReturnDate) {
		this.actucalReturnDate = actucalReturnDate;
	}
	
}
