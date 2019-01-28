package Common;

public class InventoryBook {
	private String bookName;
	private String bookID;
	private String copies;
	private String wanted;
	private String authorName;
	private String edition;
	private String printDate;
	private String theme;
	private String description;
	private String purchaseDate;
	private String location;
	
	public InventoryBook(String bookID, String bookName, String copies, String wanted, String authorName,
			String edition, String printDate, String theme, String description, String purchaseDate, String location)
	{
		this.bookName = bookName;
		this.bookID = bookID;
		this.copies = copies;
		this.wanted = wanted;
		this.authorName = authorName;
		this.edition = edition;
		this.printDate = printDate;
		this.theme = theme;
		this.description = description;
		this.purchaseDate = purchaseDate;
		this.location = location;
	}



	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getBookID() {
		return bookID;
	}

	public void setBookID(String bookID) {
		this.bookID = bookID;
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

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getPrintDate() {
		return printDate;
	}

	public void setPrintDate(String printDate) {
		this.printDate = printDate;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	
}
