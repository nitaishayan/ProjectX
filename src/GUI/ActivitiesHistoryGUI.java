package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Common.GuiInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ActivitiesHistoryGUI implements Initializable,GuiInterface {

	@FXML
	private Button btnHistory;

	@FXML
	void delayHistory(ActionEvent event) throws IOException {
		Parent parent=FXMLLoader.load(getClass().getResource("/GUI/DelayandLostTableView.fxml"));
		Scene scene=new Scene(parent);
		Stage stage=new Stage();
		stage.setScene(scene);
		stage.setMaxHeight(639);
		stage.setMinHeight(639);
		stage.setMinWidth(922);
		stage.setMaxWidth(922);
		stage.show();
	}

	@FXML
	void loanHistory(ActionEvent event) throws IOException {
		//Load page of loan history
		Parent parent=FXMLLoader.load(getClass().getResource("/GUI/HistoryOfLoanTableView.fxml"));
		Scene scene=new Scene(parent);
		Stage stage=new Stage();
		stage.setScene(scene);
		stage.setMaxHeight(628);
		stage.setMinHeight(628);
		stage.setMinWidth(916);
		stage.setMaxWidth(916);
		stage.show();
	}

	@FXML
	void statusHistory(ActionEvent event) throws IOException {
		Parent parent=FXMLLoader.load(getClass().getResource("/GUI/MemberStatusHistory.fxml"));
		Scene scene=new Scene(parent);
		Stage stage=new Stage();
		stage.setScene(scene);
		stage.setMaxHeight(571);
		stage.setMinHeight(571);
		stage.setMinWidth(806);
		stage.setMaxWidth(806);
		stage.show();
	}


	@Override
	public void showSuccess(String string) {
		Alert alert=new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(string);
		alert.showAndWait();						
	}
	@Override
	public void display(Object obj) {
		// TODO Auto-generated method stub

	}
	@Override
	public void showFailed(String message) {
		// TODO Auto-generated method stub

	}
	@Override
	public void freshStart() {
		// TODO Auto-generated method stub

	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}
}
