package Common;

public class Book {
	private String bookID;
	private String bookName;
	private String copies;
	private String wanted;
	private String authorName;
	private String editionNumber;
	private String printDate;
	private String bookGenere;
	private String description;
	private String purchaseDate;
	private String tableOfContent;
	private String shelfLocation;
	
	public Book(String bookID, String bookName, String copies, String wanted, String authorName, String editionNumber,
			String printDate, String bookGenere, String description, String purchaseDate, String tableOfContent,
			String shelfLocation) {
		super();
		this.bookID = bookID;
		this.bookName = bookName;
		this.copies = copies;
		this.wanted = wanted;
		this.authorName = authorName;
		this.editionNumber = editionNumber;
		this.printDate = printDate;
		this.bookGenere = bookGenere;
		this.description = description;
		this.purchaseDate = purchaseDate;
		this.tableOfContent = tableOfContent;
		this.shelfLocation = shelfLocation;
	}

	public String getBookID() {
		return bookID;
	}
	public void setBookID(String bookID) {
		this.bookID = bookID;
	}
	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getCopies() {
		return copies;
	}
	public void setCopies(String copies) {
		this.copies = copies;
	}
	public String getWanted() {
		return wanted;
	}
	public void setWanted(String wanted) {
		this.wanted = wanted;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getEditionNumber() {
		return editionNumber;
	}
	public void setEditionNumber(String editionNumber) {
		this.editionNumber = editionNumber;
	}
	public String getPrintDate() {
		return printDate;
	}
	public void setPrintDate(String printDate) {
		this.printDate = printDate;
	}
	public String getBookGenere() {
		return bookGenere;
	}
	public void setBookGenere(String bookGenere) {
		this.bookGenere = bookGenere;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getTableOfContent() {
		return tableOfContent;
	}
	public void setTableOfContent(String tableOfContent) {
		this.tableOfContent = tableOfContent;
	}
	public String getShelfLocation() {
		return shelfLocation;
	}
	public void setShelfLocation(String shelfLocation) {
		this.shelfLocation = shelfLocation;
	}

}
