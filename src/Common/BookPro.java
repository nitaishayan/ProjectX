package Common;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BookPro {
	/**
	 * this class is for the binding between the presentation of book in the table view to the instance.
	 */

	private StringProperty bookName;
	private StringProperty authorName;
	private StringProperty bookGenre;
	private StringProperty description;
	private StringProperty bookID;
	private StringProperty wanted;
	private StringProperty shelfLocation;
	private StringProperty numberOfCopies;

	/**
	 * constructor to build a instance of the class (use in table view of search book)
	 * @param bookName - the book name.
	 * @param authorName - the author name.
	 * @param bookGenre - the book genre.
	 * @param description - description of the book.
	 * @param bookID - the ID of the book
	 * @param wanted - field that represent if the book is wanted (true if is, false else).
	 * @param shelfLocation - the shelf location of the book in the library.
	 * @param numberOfCopies - number of copies of this book in the library.
	 */
	public BookPro(String bookName, String authorName,String bookGenre,String description,String bookID,String wanted,String shelfLocation ,String numberOfCopies) {
		this.bookName = new SimpleStringProperty(bookName);
		this.authorName = new SimpleStringProperty(authorName);
		this.bookGenre = new SimpleStringProperty(bookGenre);
		this.description = new SimpleStringProperty(description);
		this.bookID = new SimpleStringProperty(bookID);
		this.wanted = new SimpleStringProperty(wanted);
		this.shelfLocation = new SimpleStringProperty(shelfLocation);
		this.numberOfCopies = new SimpleStringProperty(numberOfCopies);
	}

	/**
	 * constructor to build a instance of the class (use in table view of search book)
	 * @param bookID - the ID of the book
	 * @param bookName - the book name.
	 * @param authorName - the author name.
	 * @param bookGenre - the book genre.
	 * @param description description of the book.
	 */
	public BookPro(String bookID,String bookName, String authorName,String bookGenre,String description)
	{
		this.bookID = new SimpleStringProperty(bookID);
		this.bookName = new SimpleStringProperty(bookName);
		this.authorName = new SimpleStringProperty(authorName);
		this.bookGenre = new SimpleStringProperty(bookGenre);
		this.description = new SimpleStringProperty(description);
	}


	public StringProperty getBookGenre() {
		return bookGenre;
	}

	public void setBookGenre(StringProperty bookGenre) {
		this.bookGenre = bookGenre;
	}


	public StringProperty getDescription() {
		return description;
	}


	public void setDescription(StringProperty description) {
		this.description = description;
	}


	public StringProperty getBookID() {
		return bookID;
	}


	public void setBookID(StringProperty bookID) {
		this.bookID = bookID;
	}


	public StringProperty getWanted() {
		return wanted;
	}


	public void setWanted(StringProperty wanted) {
		this.wanted = wanted;
	}


	public StringProperty getShelfLocation() {
		return shelfLocation;
	}


	public void setShelfLocation(StringProperty shelfLocation) {
		this.shelfLocation = shelfLocation;
	}


	public StringProperty getNumberOfCopies() {
		return numberOfCopies;
	}


	public void setNumberOfCopies(StringProperty numberOfCopies) {
		this.numberOfCopies = numberOfCopies;
	}




	public StringProperty getBookName() {
		return bookName;
	}


	public void setBookName(StringProperty bookName) {
		this.bookName = bookName;
	}


	public StringProperty getAuthorName() {
		return authorName;
	}


	public void setAuthorName(StringProperty authorName) {
		this.authorName = authorName;
	}


}
