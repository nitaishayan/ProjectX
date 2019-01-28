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
import logic.BookHandlerController;
import logic.CommonController;
import logic.Main;

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
				BookHandlerController.loanBook(returnedCopy.getCopyID(),returnedBook.getWanted(),returnedBook.getBookID(),returnedMember.getId(),returnedBook.getBookName());
			}
		}
	}

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
			if(msg.size() == 1) {
				freshStart();
				showFailed("Error occured!");
			}
			else {
				showSuccess("Copy " + returnedCopy.getCopyID() + " loaned successfully by the member " + returnedMember.getFirstName() + " " + returnedMember.getLastName() +
						" and the expected return date is " + msg.get(2));
			}
			break;

		default:
			break;
		}
	}

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

	public void clearMemberFields() {
		txt_MemberID.clear();
		txt_MemberStatus.clear();
		txt_MemberName.clear();
		buttonLoan.setDisable(true);
	}

	public void clearCopyFields() {
		txt_CopyID.clear();
		txt_BookName.clear();
		txt_BookStatus.clear();
		buttonLoan.setDisable(true);
	}



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Main.client.clientUI = this;
	}
}
