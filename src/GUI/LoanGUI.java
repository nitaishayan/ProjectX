package GUI;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Common.Book;
import Common.Copy;
import Common.GuiInterface;
import Common.Member;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import logic.BookHandlerController;
import logic.CommonController;
import logic.Main;

/**
 * The class connect between the input in the GUI to the related controller.
 * The class giving the functionality to loan a book by copy id and a member id.
 */
public class LoanGUI implements Initializable, GuiInterface {

	@FXML
	private TextField txt_MemberID;

	@FXML
	private TextField txt_CopyID;

	@FXML
	private TextField txt_MemberStatus;

	@FXML
	private TextField txt_BookName;

	@FXML
	private TextField txt_MemberName;

	@FXML
	private TextField txt_BookStatus;

	@FXML
	private Button buttonLoan;

	private Copy returnedCopy;
	private Member returnedMember;
	private Book returnedBook;
	private boolean memberFlag, copyFlag;

	/**
	 * The method checks if the key pressed was 'Enter' key, if so calls the checkMemberExistence with the memberID field in the GUI.
	 * @param event - event from any key being pressed.
	 */
	@FXML
	void memberKeyPressed(KeyEvent event) {
		if (event.getCode()==KeyCode.ENTER) {
			try {
				CommonController.checkMemberExistence(txt_MemberID.getText());
			} catch (Exception e) {
				clearMemberFields();
				showFailed(e.getMessage());
			}
		}
	}

	/**
	 * The method checks if the key pressed was 'Enter' key, if so calls the isCopyExist with the copyID field in the GUI.
	 * @param event - event from any key being pressed.
	 */
	@FXML
	void copyKeyPressed(KeyEvent event) {
		if (event.getCode()==KeyCode.ENTER) {
			try {
				BookHandlerController.isCopyExist(txt_CopyID.getText());
			} catch (Exception e) {
				clearCopyFields();
				showFailed(e.getMessage());
			}
		}
	}

	/**
	 * The method calls checkMemberExistence with the memberID field in the GUI.
	 * @param event - event from mouse click on search button.
	 */
	@FXML
	void memberMouse(MouseEvent event) {
		try {
			CommonController.checkMemberExistence(txt_MemberID.getText());
		} catch (Exception e) {
			clearMemberFields();
			showFailed(e.getMessage());
		}
	}

	/**
	 * The method calls isCopyExist with the copyID field in the GUI.
	 * @param event - event from mouse click on search button.
	 */
	@FXML
	void copyMouse(MouseEvent event) {
		try {
			BookHandlerController.isCopyExist(txt_CopyID.getText());
		} catch (Exception e) {
			clearCopyFields();
			showFailed(e.getMessage());
		}
	}

	/**
	 * The method checks if the copy id field and member id field are filled correctly.
	 * If they have been changed it shows a relevant error.
	 * Else, calls "loanBook" method.
	 * @param event - event from mouse click on "Loan" button.
	 */
	@FXML
	void clickLoanButton(ActionEvent event) {
		if(!returnedMember.getId().equals(txt_MemberID.getText())) {
			clearMemberFields();
			showFailed("You changed the Member ID field, to continue reclick enter in Member ID field");
		}
		else {
			if(!returnedCopy.getCopyID().equals(txt_CopyID.getText())) {
				clearCopyFields();
				showFailed("You changed the Copy ID field, to continue reclick enter in Copy ID field");
			}
			else {
				BookHandlerController.loanBook(returnedCopy.getCopyID(),returnedBook.getWanted(),returnedBook.getBookID(),returnedMember.getId(),returnedBook.getBookName(),returnedBook.getAuthorName());
			}
		}
	}

	/**
	 * this method show information pop-up on the screen with given message
	 * @param string- the message that shown in the pop-up.
	 */
	@Override
	public void showSuccess(String message) {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setHeaderText("An successful operation");
			alert.setContentText(message);
			alert.showAndWait();
			freshStart();
		});
	}

	/**
	 * This method uses switch-case to determine which action to perform based on the returned value.
	 * In case "Check Member Existence" - if the returned obj size is 1 it clears the member fields, else it saves the returned 
	 * member information in a Member object and checks the member status.
	 * if the member status is not active it shows an error, if active it checks the copy flag and based on its value determine the button loan status.
	 * In case "Check Copy ID Existence" - if the returned obj size is 1 it clears the copy fields, else it saves the returned
	 * copy information in Copy object. Than it calls isCopyLoaned method and set the bookName field with the returned relvant value.
	 * In case "Check Copy Loan Status" - if the returned obj size if 1 clear the copy fields and show an error pop-up, else call the 
	 * isCopyWanted method .
	 * In case "Check Copy Wanted Status" - if the returned obj size is 1 clear the copy fields and show error pop-up, else
	 * save the returned book values in Book object. If the book is labeled as wanted show in book status field
	 * "yes" else show "no. If memberFlag is true change the button disable status to true.
	 * In case "Loan Book" - if the returned obj size is 2 checks if the 2nd item in the array is "Error" string, if it is clear
	 * clear all the fields and show a error message, else clear only the copy fields and show a error message.
	 * If the size isn't 2 show a success message pop-up. 
	 * @param obj - This is the returned value from the server.
	 */
	@Override
	public void display(Object obj) {
		ArrayList<String> msg = (ArrayList<String>)obj;
		switch (msg.get(0)) {

		case "Check Member Existence":
			if(msg.size() == 1) {
				clearMemberFields();
				showFailed("Member doesn't exist!");
			}
			else {
				returnedMember = new Member(msg.get(1), msg.get(2), msg.get(3), msg.get(4), msg.get(5), 
						msg.get(6),msg.get(7), msg.get(8), msg.get(9), msg.get(10), msg.get(11), msg.get(12));
				txt_MemberStatus.setText(returnedMember.getStatus());
				txt_MemberName.setText(returnedMember.getFirstName() + " " + returnedMember.getLastName());
				if(returnedMember.getStatus().equals("Active")){
					memberFlag = true;
					if(copyFlag == true) {
						buttonLoan.setDisable(false);
					}
				}
				else {
					freshStart();
					showFailed("Member status is not active hence he can't loan a book!");
				}

			}
			break;

		case "Check Copy ID Existence":
			if(msg.size() == 1) {
				clearCopyFields();
				showFailed("Copy doesn't exist");
			}
			else {
				try {
					returnedCopy = new Copy(msg.get(1),msg.get(2),msg.get(3),msg.get(4),msg.get(5));
					BookHandlerController.isCopyLoaned(returnedCopy.getCopyID());
					txt_BookName.setText(returnedCopy.getCopyName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;

		case "Check Copy Loan Status":
			if(msg.size() != 1) {
				clearCopyFields();
				showFailed("Copy is loaned already");
			}
			else {
				BookHandlerController.isCopyWanted(returnedCopy.getBookID());
			}
			break;

		case "Check Copy Wanted Status":
			if(msg.size() == 1) {
				clearCopyFields();
				showFailed("Book doesn't exist!");
			}
			else {
				returnedBook = new Book(msg.get(1), msg.get(2), msg.get(3), msg.get(4), msg.get(5), 
						msg.get(6),msg.get(7), msg.get(8), msg.get(9), msg.get(10), msg.get(11), msg.get(12));
				if(returnedBook.getWanted().equals("true")) {
					txt_BookStatus.setText("Yes");
				}
				else {
					txt_BookStatus.setText("No");
				}
			}

			copyFlag = true;
			if(memberFlag == true) {
				buttonLoan.setDisable(false);
			}
			break;

		case "Loan Book":
			if(msg.size() == 2) {
				if(!msg.get(1).equals("Error")) {
					clearCopyFields();
					showFailed(msg.get(1));
				}
				else {
					freshStart();
					showFailed(msg.get(1));
				}
			}
			else {
				showSuccess("Copy " + returnedCopy.getCopyID() + " loaned successfully by the member " + returnedMember.getFirstName() + " " + returnedMember.getLastName() +
						" and the expected return date is " + msg.get(2));
				copyFlag = memberFlag = false;
			}

			break;

		default:
			break;
		}
	}

	/**
	 * this method show error pop-up on the screen with given message
	 * @param String- the message that shown in the pop-up.
	 */
	@Override
	public void showFailed(String message) {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("An error occurred");
			alert.setContentText(message);
			alert.showAndWait();
		});
	}

	/**
	 * This method clean up the fields on the screen.
	 */
	@Override
	public void freshStart() {
		txt_MemberID.clear();
		txt_CopyID.clear();
		txt_MemberStatus.clear();
		txt_BookName.clear();
		txt_MemberName.clear();
		txt_BookStatus.clear();
		buttonLoan.setDisable(true);
	}

	/**
	 * This method clears the member related fields on the screen.
	 */
	public void clearMemberFields() {
		txt_MemberID.clear();
		txt_MemberStatus.clear();
		txt_MemberName.clear();
		buttonLoan.setDisable(true);
	}

	/**
	 * This method clears the copy related fields on the screen.
	 */
	public void clearCopyFields() {
		txt_CopyID.clear();
		txt_BookName.clear();
		txt_BookStatus.clear();
		buttonLoan.setDisable(true);
	}


	/**
	 * this method set the variable of the GuiInterface in the client to this.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Main.client.clientUI = this;
	}
}
