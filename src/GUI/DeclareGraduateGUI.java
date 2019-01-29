package GUI;

import java.sql.SQLException;
import java.util.ArrayList;

import Common.Book;
import Common.Copy;
import Common.GuiInterface;
import Common.Member;
import Server.DBController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import logic.BookHandlerController;
import logic.CommonController;
import logic.Main;

public class DeclareGraduateGUI implements GuiInterface{

	private static final int ID_SIZE = 9;
	private boolean flag;

	@FXML
	private TextField txt_MemberID;

	@FXML
	void commitButtonPressed(ActionEvent event) {
		flag = false;
		switch(checkMemberField(txt_MemberID.getText())) {
		case "1":
			showFailed("Member ID field can't be empty");
			break;
		case "2":
			showFailed("Wrong ID size");
			break;
		case "3":
			flag = true;
			break;
		default:
			break;
		}

		if(flag) {
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

	@FXML
	void enterKeyPressed(KeyEvent event) {
		flag = false;
		if (event.getCode()==KeyCode.ENTER) {
			switch(checkMemberField(txt_MemberID.getText())) {
			case "1":
				showFailed("Member ID field can't be empty");
				break;
			case "2":
				showFailed("Wrong ID size");
				break;
			case "3":
				flag = true;
				break;
			default:
				break;
			}

			if(flag) {
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
			freshStart();
		});
	}

	@Override
	public void freshStart() {
		txt_MemberID.clear();
	}

	public String checkMemberField(String memberID) {
		if(memberID.length() == 0) {
			return "1";
		}	

		if(memberID.length() != ID_SIZE) {
			return "2";
		}

		return "3";
	}
}
