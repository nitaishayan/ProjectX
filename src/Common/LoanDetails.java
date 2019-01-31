package Common;
/**
 * Class that appeared as an object in the historyofLoanViewGUI
 * this class is initialized and enter to the table view
 * @author nitay shayan
 *
 */
public class LoanDetails {
	private String bookName;
	private String authorName;
	private String copyID;
	private String loanDate;
	private String actualReturnDate;
	
	public LoanDetails(String bookName, String copyID, String loanDate,String actualReturnDate) {
		super();
		this.bookName = bookName;
		this.copyID = copyID;
		this.loanDate = loanDate;
		this.actualReturnDate=actualReturnDate;
	}
	
	public LoanDetails(String bookName,String authorName, String copyID, String loanDate,String actualReturnDate) {
		super();
		this.bookName = bookName;
		this.authorName = authorName;
		this.copyID = copyID;
		this.loanDate = loanDate;
		this.actualReturnDate=actualReturnDate;
	}

	public LoanDetails() {
		this.bookName="";
		this.copyID="";
		this.loanDate="";
		this.actualReturnDate="";
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getCopyID() {
		return copyID;
	}
	public void setCopyID(String copyID) {
		this.copyID = copyID;
	}
	public String getLoanDate() {
		return loanDate;
	}
	public void setLoanDate(String loanDate) {
		this.loanDate = loanDate;
	}
	public String getActualReturnDate() {
		return actualReturnDate;
	}
	public void setActualReturnDate(String actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}
	
	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorsName(String authorName) {
		this.authorName = authorName;
	}
	
}