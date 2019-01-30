package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Client.Client;
import Common.GuiInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import logic.CommonController;
import logic.Main;
import logic.RegistrationController;



public class MemberPersonalDataGUI implements Initializable,GuiInterface{
	@FXML
	private TextField txtFirst_Name;

	@FXML
	private TextField txtLast_Name;

	@FXML
	private TextField txtID;

	@FXML
	private TextField txtPhone_Number;

	@FXML
	private TextField txtEmail;
	@FXML
	private Button btnHistory;
	@FXML
	private TextField txtStatus;
	@FXML
	private Button btnSave;
	@FXML
	void updateMemberDetails(ActionEvent event) {
		String ans=CommonController.checkInput(txtPhone_Number.getText(),txtEmail.getText(),txtID.getText());
		if (ans.equals("EmailError")) {
			showFailed("Wrong Email, please enter new parameters");
		}
		else if (ans.equals("PhoneError")) {
			showFailed("Wrong phone number, please enter new parameters");
		}
		else {
			RegistrationController.updateMemberDetails(txtID.getText(),txtPhone_Number.getText(), txtEmail.getText());
			showSuccess("Details updated successfully");
		}
		txtEmail.setEditable(false);
		txtPhone_Number.setEditable(false);
	}

	@FXML
	void emailEdit(ActionEvent event) {
		txtEmail.setEditable(true);
	}

	@FXML
	void phoneEdit(ActionEvent event) {
		txtPhone_Number.setEditable(true);
	}

	@FXML
	void viewPersonalHistory(ActionEvent event) throws IOException {
		//Load page of loan history
		Parent parent=FXMLLoader.load(getClass().getResource("/GUI/HistoryOfLoanTableView.fxml"));
		Scene scene=new Scene(parent);
		Stage stage=new Stage();
		stage.setScene(scene);
		stage.setMaxHeight(571);
		stage.setMinHeight(571);
		stage.setMinWidth(904);
		stage.setMaxWidth(904);
		stage.show();
	}
	Common.Member member;// object of Member details
	@Override
	public void showSuccess(String string) {
		Alert alert=new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(string);
		alert.showAndWait();			
	}

	@Override
	public void display(Object obj) {
		ArrayList<String>memberData=(ArrayList<String>) obj;
		member=new Common.Member(memberData.get(1), memberData.get(2), memberData.get(3), memberData.get(4), memberData.get(5), memberData.get(6), memberData.get(7), memberData.get(8), memberData.get(9), memberData.get(10), memberData.get(11), memberData.get(12));
		setCardMember(memberData);
//		setEditableMember();
	}

	@Override
	public void showFailed(String message) {
		Alert alert=new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(message);
		alert.showAndWait();			
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Main.client.clientUI=this;
		init();
	}
	private void init() {
		RegistrationController.searchMember(Client.arrayUser.get(0));
	}
	private void setEditableMember() {
		txtFirst_Name.setEditable(false);
		txtLast_Name.setEditable(false);
		txtPhone_Number.setEditable(true);
		txtEmail.setEditable(true);
		txtStatus.setEditable(false);
	}
	private void setCardMember(ArrayList<String> memberData) {
		txtID.setText(memberData.get(1));
		txtFirst_Name.setText(memberData.get(5));
		txtLast_Name.setText(memberData.get(6));
		txtPhone_Number.setText(memberData.get(2));
		txtEmail.setText(memberData.get(3));
		txtStatus.setText(memberData.get(7));
	}
	/*		private void setFields(Boolean cond) {
			txtFirst_Name.setDisable(cond);
			txtLast_Name.setDisable(cond);
			txtPhone_Number.setDisable(cond);
			txtEmail.setDisable(cond);
			//cmbStatus.setDisable(cond);
			btnHistory.setDisable(cond);
		//	btnLates_Lostbook.setDisable(cond);
			btnSave.setDisable(cond);
		}*/
	@Override
	public void freshStart() {
		// TODO Auto-generated method stub

	}
}
