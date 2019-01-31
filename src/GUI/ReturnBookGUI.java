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

	  @FXML
	    void copyMouse(MouseEvent event) {
		  try {
				BookHandlerController.isCopyExist(txtCopy_ID.getText());
			} catch (Exception e) {
				showFailed(e.getMessage());
			}
	    }
	  
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
					if(Integer.parseInt(memb.getDelayAmount()) != 3){
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Main.client.clientUI = this;
	}

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
