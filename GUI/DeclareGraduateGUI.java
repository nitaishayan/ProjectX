package GUI;

import Common.GuiInterface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import logic.CommonController;

public class DeclareGraduateGUI implements GuiInterface{

    @FXML
    private TextField txt_MemberID;

    @FXML
    void commitButtonPressed(ActionEvent event) {
    	try {
			CommonController.checkMemberExistence(txt_MemberID.getText());
		} catch (Exception e) {
			showFailed(e.getMessage());
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
		});
	}

	@Override
	public void freshStart() {
		
	}
}
