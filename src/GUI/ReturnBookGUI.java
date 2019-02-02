package GUI;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Common.GuiInterface;
import Common.Member;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import logic.BookHandlerController;
import logic.CommonController;
import logic.Main;

/**
 * The class connect between the input in the GUI to the related controller.
 * The class giving the functionality to return a book based on copy id.
 */
public class ReturnBookGUI implements Initializable, GuiInterface {

	@FXML
	private TextField txtBook_Name;

	@FXML
	private TextField txtFirst_Name;

	@FXML
	private TextField txtCopy_ID;

	@FXML
	private TextField txtMember_Status;

	@FXML
	private TextField txtLast_name;

	@FXML
	private Button btnReturn;

	@FXML
	private TextField txtMember_ID;


	private String copyID;
	private Member memb;
	private String oldStatus;

	/**
	 * The method checks if the key pressed was 'Enter' key, if so calls the isCopyExist with the copyID field in the GUI.
	 * @param event - event from any key being pressed.
	 */
	@FXML
	void copyKeyPressed(KeyEvent event) {
		if (event.getCode()==KeyCode.ENTER) {
			try {
				BookHandlerController.isCopyExist(txtCopy_ID.getText());
			} catch (Exception e) {
				showFailed(e.getMessage());
			}
		}
	}

	/**
	 * The method calls isCopyExist with the copyID field in the GUI.
	 * @param event - event from mouse click on search button.
	 */
	@FXML
	void copyMouse(MouseEvent event) {
		try {
			BookHandlerController.isCopyExist(txtCopy_ID.getText());
		} catch (Exception e) {
			showFailed(e.getMessage());
		}
	}


	/**
	 * The method checks if the copy id field and member id field are filled correctly.
	 * If they have been changed it shows a relevant error. Else, calls "returnBook" method.
	 * The method also checks the member status, if the member is not active and the member is freezed on the copy that is being returned
	 * it calls the isMemberLateOnReturn with the relevant member id.
	 * @param event - event from mouse click on "Return" button.
	 */
	@FXML
	void clickReturnButton(ActionEvent event) {
		if(txtCopy_ID.getText().equals(copyID)) {
			if(memb.getStatus().equals("Active")) {
				BookHandlerController.returnBook(txtCopy_ID.getText(),memb.getStatus(),memb.getId());
			}
			else {
				if(txtCopy_ID.getText().equals(memb.getFreezedOn())) {
					BookHandlerController.isMemberLateOnReturn(memb.getId());
				}
				else {
					BookHandlerController.returnBook(txtCopy_ID.getText(),memb.getStatus(),memb.getId());
				}
			}
		}
		else {
			showFailed("You changed the Copy ID field, to continue reclick enter in Copy ID field");
		}
	}

	/**
	 * This method uses switch-case to determine which action to perform based on the returned value.
	 * In case "Check Member Existence" - It saves the returned member information in a Member object and checks the member saves member status in
	 * oldStatus variable.
	 * if the member status is not active it shows an error, if active it checks the copy flag and based on its value determine the button loan status.
	 * In case "Check Copy Wanted Status" - if the returned obj size is 1 clear the copy fields and show error pop-up, else
	 * save the returned book values in Book object. If the book is labeled as wanted show in book status field
	 * "yes" else show "no. If memberFlag is true change the button disable status to true.
	 * In case "Check Copy ID Existence" - if the returned obj size is 1 it clears the copy fields, else it sets the book name field
	 * in the GUI and calls isCopyLoaned with the related copy id.
	 * In case "Check Copy Loan Status" - if the returned obj size if 1 clear the copy fields and show an error pop-up, else call the 
	 * checkMemberExistence method .
	 * In case "Return Book" - if the returned obj size is 1 show error pop-up, else checks if the member status change due return and show relevant success message.
	 * In case "Check If Member Is Late On Return" - If the returned value equals to 1 checks if the member is graduated, if the member graduated call changeMemberStatus method with the relevant message.
	 * Else, checks if the member is deep - frozen and if he doesn't call changeMemberStatus with the related values.
	 * Else, call returnBook method.
	 * In case "Change Member Status" - if the returned obj size is 1 show failed pop-up message with error.
	 * else, set the member status to Member object.
	 * * @param obj - This is the returned value from the server.
	 */
	@Override
	public void display(Object obj) {
		ArrayList<String> msg = (ArrayList<String>)obj;
		switch (msg.get(0)) {
		case "Check Member Existence":
			memb = new Member(msg.get(1), msg.get(2), msg.get(3), msg.get(4), msg.get(5), 
					msg.get(6),msg.get(7), msg.get(8), msg.get(9), msg.get(10), msg.get(11), msg.get(12));
			txtMember_Status.setText(memb.getStatus());
			txtFirst_Name.setText(memb.getFirstName());
			txtLast_name.setText(memb.getLastName());
			btnReturn.setDisable(false);
			copyID = txtCopy_ID.getText();
			oldStatus = memb.getStatus();
			break;

		case "Check Copy Loan Status":
			if(((ArrayList<String>)msg).size() == 1) {
				showFailed("Copy isn't loan yet");
			}
			else {
				txtMember_ID.setText(msg.get(4));
				try {
					CommonController.checkMemberExistence(msg.get(4));
				} catch (Exception e1) {
					showFailed(e1.getMessage());
				}
			}
			break;

		case "Check Copy ID Existence":
			if(((ArrayList<String>)msg).size() == 1) {
				showFailed("Copy doesn't exist");
			}
			else {
				txtBook_Name.setText(msg.get(2));
				BookHandlerController.isCopyLoaned(txtCopy_ID.getText());
			}
			break;

		case "Return Book":
			if(((ArrayList<String>)msg).size() == 1) {
				showFailed("Book return was unsuccessful!");
			}
			else {
				if(oldStatus.equals(memb.getStatus())) {
					showSuccess("Copy of the book " + txtBook_Name.getText() + " returned successfully");
				}
				else {
					showSuccess("Copy of the book " + txtBook_Name.getText() + " returned successfully and the member status is now " + memb.getStatus());
				}
				freshStart();
			}
			break;

		case "Check If Member Is Late On Return":
			if(Integer.parseInt(((ArrayList<String>)msg).get(1)) == 1) {
				if(memb.getIsGraduated().equals("true")) {
					CommonController.changeMemberStatus(memb.getId(), memb.getStatus(), "Locked");	
				}
				else {
					if(!memb.getStatus().equals("Deep - Frozen")){
						CommonController.changeMemberStatus(memb.getId(), memb.getStatus(), "Active");
					}
				}
				BookHandlerController.returnBook(txtCopy_ID.getText(),oldStatus,memb.getId());
			}
			else {
				BookHandlerController.returnBook(txtCopy_ID.getText(),memb.getStatus(),memb.getId());
			}
			break;

		case "Change Member Status":
			if(msg.size() == 1) {
				showFailed("Failed to change member status");
			}
			else {
				memb.setStatus(msg.get(1));
			}
			break;

		default:
			break;
		}
	}

	/**
	 * This method show error pop-up on the screen with given message
	 * @param String- the message that shown in the pop-up.
	 */
	@Override
	public void showFailed(String message) {
		Platform.runLater(() -> {
			freshStart();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("An error occurred");
			alert.setContentText(message);
			alert.showAndWait();
		});
	}

	/**
	 * This method set the variable of the GuiInterface in the client to this.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Main.client.clientUI = this;
	}

	/**
	 * This method show information pop-up on the screen with given message
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
		});
	}

	/**
	 * This method clean up the fields on the screen.
	 */
	@Override
	public void freshStart() {
		txtCopy_ID.clear();
		txtMember_ID.clear();
		txtMember_Status.clear();
		txtFirst_Name.clear();
		txtLast_name.clear();
		txtBook_Name.clear();
		btnReturn.setDisable(true);		
	}
}
