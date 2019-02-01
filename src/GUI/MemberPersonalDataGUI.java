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
import logic.MemberCardController;
import logic.RegistrationController;


/**
 * Class that control the GUI components of the fxml page ReaderPersonalData.fxml
 * this page present the card reader details as they received from the data base
 * this class is for the member menu as presented to him in the program.
 * @author nitay shayan
 *
 */
public class MemberPersonalDataGUI implements Initializable,GuiInterface{
	Common.Member member;// object of Member details
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
	/**
	 * Method that update the member details as they were edited by the ember himself
	 * based on the story he is enable to update his email and phone number.
	 * this method is call to a validation method that checks the values entered by the member
	 * @param event
	 */
	@FXML
	void updateMemberDetails(ActionEvent event) {
		String ans=CommonController.checkInput(txtPhone_Number.getText(),txtEmail.getText(),txtID.getText());
		System.out.println(ans+" fgfdgsdfsdasfsa");
		if (ans.equals("EmailError")) {
			showFailed("Wrong Email, please enter new parameters");
		}
		else if (ans.equals("PhoneError")) {
			showFailed("Wrong phone number, please enter new parameters");
		}
		else {
			MemberCardController.memberUpdateDetails(txtID.getText(),txtPhone_Number.getText(), txtEmail.getText());
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
/**
 * Method that send a query to the data base to show the loan details history by the member himself
 * present the data in a table view 
 * @param event if press the loan history button
 * @throws IOException
 */
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
	/**
	 * a GUIInterface method that notify a success message based on the even
	 */
	@Override
	public void showSuccess(String string) {
		Alert alert=new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(string);
		alert.showAndWait();			
	}
	/**
	 * a GUIInterface method that upload the card member details as they returned from the data base
	 * this method initialize the details of the member and present them at the GUI controller
	 * @param obj is the memberObject as he returned from the data base
	 * 
	 */
	@Override
	public void display(Object obj) {
		ArrayList<String>memberData=(ArrayList<String>) obj;
		member=new Common.Member(memberData.get(1), memberData.get(2), memberData.get(3), memberData.get(4), memberData.get(5), memberData.get(6), memberData.get(7), memberData.get(8), memberData.get(9), memberData.get(10), memberData.get(11), memberData.get(12));
		setCardMember(memberData);
//		setEditableMember();
	}
/**
  * a GUIInterface method that notify a faild message based on the even
 */
	@Override
	public void showFailed(String message) {
		Alert alert=new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(message);
		alert.showAndWait();			
	}
/**
 * Method that implements from initialize class, present the values before the launch
 * set the static client field interface to this current field.
 * call init method 
 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Main.client.clientUI=this;
		init();
	}
	/**
	 * method that call for search member to present the member details based on the memberID in the arrayUser value
	 */
	private void init() {
		MemberCardController.searchMember(Client.arrayUser.get(0));
	}
	/**
	 * set the editable fields to the member
	 */
	private void setEditableMember() {
		txtFirst_Name.setEditable(false);
		txtLast_Name.setEditable(false);
		txtPhone_Number.setEditable(true);
		txtEmail.setEditable(true);
		txtStatus.setEditable(false);
	}
	/**
	 * set the values in the GUI fxml page based on the values received from the DB
	 * @param memberData
	 */
	private void setCardMember(ArrayList<String> memberData) {
		txtID.setText(memberData.get(1));
		txtFirst_Name.setText(memberData.get(5));
		txtLast_Name.setText(memberData.get(6));
		txtPhone_Number.setText(memberData.get(2));
		txtEmail.setText(memberData.get(3));
		txtStatus.setText(memberData.get(7));
	}
	/**
	 * to initialize values in the GUI controller classes
	 */
	@Override
	public void freshStart() {

	}
}
