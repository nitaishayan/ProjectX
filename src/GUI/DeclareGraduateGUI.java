package GUI;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import Common.GuiInterface;
import Server.DBController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


/**
 * The class connect between the input in the GUI to the DBController.
 * The class giving the functionality to change member status to graduated by his member id.
 * @ID_SIZE - Default ID size
 * @param txt_MemberID - Member ID field in the GUI.
 */
public class DeclareGraduateGUI implements GuiInterface{

	private static final int ID_SIZE = 9;

	@FXML
	private TextField txt_MemberID;

	/**
	 * Once the commit button is pressed method checks the txt_MemberID field and catch exception if neeeded.
	 * If exception is thrown by the inner method this method will call another method called showFailed, else 
	 * the method checks if the member exist in the DB, if the member doesn't exist it calls showFailed method, else
	 * call changeMemberToGraduated method who do the relevant changes in the DB and shows a relevant fail or success pop-up.
	 * @param event - event from commit button pressed.
	 */
	@FXML
	void commitButtonPressed(ActionEvent event) {
		try {
			checkMemberField(txt_MemberID.getText());
		} catch (Exception e) {
			showFailed(e.getMessage());
			return;
		}		

		ArrayList<String> checkMemberExistence = new ArrayList<>();
		ArrayList<String> changeMemberToGraduated = new ArrayList<>();

		checkMemberExistence.add("");
		checkMemberExistence.add(txt_MemberID.getText());
		try {
			checkMemberExistence = DBController.getInstance().isMemberExist(checkMemberExistence);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if(checkMemberExistence.size() == 1) {
			showFailed("Member doesn't exist!");
			return;
		}
		else {
			try {
				changeMemberToGraduated = DBController.getInstance().changeMemberToGraduated(checkMemberExistence);
				if(changeMemberToGraduated.size() == 1 || changeMemberToGraduated.size() == 2){
					if(changeMemberToGraduated.size() == 1) {
						showFailed(changeMemberToGraduated.get(0));
					}
					else {
						showFailed(changeMemberToGraduated.get(1));
					}
				}
				else {
					showSuccess("Member is now graduated and his status is " + changeMemberToGraduated.get(0) + ".");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * If the key pressed is enter the method checks the txt_MemberID field and catch exception if neeeded.
	 * If exception is thrown by the inner method this method will call another method called showFailed, else 
	 * the method checks if the member exist in the DB, if the member doesn't exist it calls showFailed method, else
	 * call changeMemberToGraduated method who do the relevant changes in the DB and shows a relevant fail or success pop-up.
	 * If the key pressed isn't enter the method doesn't do anything.
	 * @param event - event from keyboard, key pressed.
	 */
	@FXML
	void enterKeyPressed(KeyEvent event) {
		if (event.getCode()==KeyCode.ENTER) {
			try {
				checkMemberField(txt_MemberID.getText());
			} catch (Exception e) {
				showFailed(e.getMessage());
				return;
			}		

			ArrayList<String> checkMemberExistence = new ArrayList<>();
			ArrayList<String> changeMemberToGraduated = new ArrayList<>();

			checkMemberExistence.add("");
			checkMemberExistence.add(txt_MemberID.getText());
			try {
				checkMemberExistence = DBController.getInstance().isMemberExist(checkMemberExistence);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if(checkMemberExistence.size() == 1) {
				showFailed("Member doesn't exist!");
				return;
			}
			else {
				try {
					changeMemberToGraduated = DBController.getInstance().changeMemberToGraduated(checkMemberExistence);
					if(changeMemberToGraduated.size() == 1 || changeMemberToGraduated.size() == 2){
						if(changeMemberToGraduated.size() == 1) {
							showFailed(changeMemberToGraduated.get(0));
						}
						else {
							showFailed(changeMemberToGraduated.get(1));
						}
					}
					else {
						showSuccess("Member is now graduated and his status is " + changeMemberToGraduated.get(0) + ".");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
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
	 *  not used method(must implement because the implementation of GuiInterface)
	 */
	@Override
	public void display(Object obj) {

	}

	/**
	 * this method show error pop-up on the screen with given message
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
			freshStart();
		});
	}

	/**
	 * this method clean up the memberID field on the screen.
	 */
	@Override
	public void freshStart() {
		txt_MemberID.clear();
	}

	/**
	 * The method 
	 * @param memberID - memberID to do the validtion on.
	 * @throws IOException - throws input exception
	 */
	public void checkMemberField(String memberID) throws IOException {
		if(memberID.length() == 0) {
			throw new IOException("Member ID field can't be empty");
		}	

		if(memberID.length() != ID_SIZE) {
			throw new IOException("Wrong ID size");
		}
	}
}
