package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Client.Client;
import Common.GuiInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.BookHandlerController;
import logic.CommonController;
import logic.Main;
import logic.RegistrationController;
/**
 * This class show a details launched by search member in the system and returning details about that member.
 * Details such as: his reader card info. and his history of activity such as: delay/lost books, status and notes written by librarian
 * @author nitay shayan
 *
 */
public class MemberCardGUI implements Initializable,GuiInterface{
	public static String memberIDHistory=null;
	public static String memberFirstName=null;
	public static String memberLastName=null;
	String isManager;
	ObservableList<String> list;
	boolean update=false;
	private  String memberStatus=null;
	@FXML
	private ComboBox cmbStatus;
	@FXML
	private TextField txtMember_ID;

	@FXML
	private TextField txtFirst_Name;

	@FXML
	private TextField txtLast_Name;

	@FXML
	private TextField txtPhone_Number;

	@FXML
	private TextField txtEmail;

	@FXML
	private Button btnHistory;

	@FXML
	private Button btnLates_Lostbook;

	@FXML
	private TextArea txtArea_Notes;

	@FXML
	private Button btnSave;


	@FXML
	private Button btnStatus;
/**
 * This method is launch a query in data base to get all the books 
 * information by a specific member that is delayed or lost books in the library.
 * @param event Event happend by click on the button
 * @throws IOException
 *  
 */
	@FXML
	void getDelayandLostBooks(ActionEvent event) throws IOException {
		//load table view that present the late and lost book by member
		memberIDHistory=txtMember_ID.getText();
		memberFirstName=txtFirst_Name.getText();
		memberLastName=txtLast_Name.getText();
		Parent parent=FXMLLoader.load(getClass().getResource("/GUI/DelayandLostTableView.fxml"));
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
	 *  Method that launch a query to find status activity of a specific member in the data base.
	 * eventually display the data in table view window
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void getStatusHistory(ActionEvent event) throws IOException {
		//load table view that present the the member status
		memberIDHistory=txtMember_ID.getText();
		memberFirstName=txtFirst_Name.getText();
		memberLastName=txtLast_Name.getText();
		Parent parent=FXMLLoader.load(getClass().getResource("/GUI/MemberStatusHistory.fxml"));
		Scene scene=new Scene(parent);
		Stage stage=new Stage();
		stage.setScene(scene);
		stage.setMaxHeight(564);
		stage.setMinHeight(564);
		stage.setMinWidth(1005);
		stage.setMaxWidth(1005);
		stage.show();
	}
	/**
	 * Method that launch a query to find the requested memberID inserted by the member
	 * @param event launch by press on button
	 */
    @FXML
    void mouseClick(MouseEvent event) {
		RegistrationController.searchMember(txtMember_ID.getText());
    }
	/**
	 * Method that launch a query to find the requested memberID inserted by the member
	 * @param event launch by press enter on keyboard
	 */
	@FXML
	void searchMember(KeyEvent event) {
		if (event.getCode()==KeyCode.ENTER) {
			RegistrationController.searchMember(txtMember_ID.getText());
		}
	}
	/**
	 * Method that launch a query to update the details send by librarian and memberID inserted by the member
	 * eventually display a success message or failed message
	 * @param event launch by press enter on keyboard
	 */
	@FXML
	void librarianUpdateMember(ActionEvent event) {
		update=true;
		if (!memberStatus.equals(cmbStatus.getValue().toString())) {
			CommonController.librarianUpdateMember(cmbStatus.getValue().toString(),txtMember_ID.getText(),txtArea_Notes.getText(),isManager,true,memberStatus);//should be true			
			System.out.println("Status changed to "+cmbStatus.getValue().toString()+" now in display");
		}
		else
		{
			CommonController.librarianUpdateMember(cmbStatus.getValue().toString(),txtMember_ID.getText(),txtArea_Notes.getText(),isManager,false," ");//should be false			
		}
	}
	/**
	 *  Method that launch a query to find a loan history by a memberID inserted by the librarian and recieve the data from the data base
	 * eventually display the data in table view window
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void viewLoanPersonalHistory(ActionEvent event) throws IOException {
		//Load page of loan history
		memberIDHistory=txtMember_ID.getText();
		memberFirstName=txtFirst_Name.getText();
		memberLastName=txtLast_Name.getText();
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Client.clientUI=this;
		setMsStatusComboBox();
		CommonController.checkManager(Client.arrayUser.get(0));

	}
	/**
	 * this method show information pop-up on the screen with given message
	 * @param string- the message that shown in the pop-up.
	 */
	@Override
	public void showSuccess(String string) {
		Alert alert=new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(string);
		alert.showAndWait();
	}
	/**
	 * this method show information pop-up on the screen with given message
	 * @param string- the message that shown in the pop-up.
	 */
	@Override
	public void showFailed(String message) {
		freshStart();
		Alert alert=new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(message);
		alert.showAndWait();
		setFields(true);
	}
	/**
	 * Interface method that print the Object received by the Server
	 * the display is check which Librarian is enter, if it's the Librarian manager is enable to edit other details from the member card reader.
	 *  
	 */
	@Override
	public void display(Object obj) {
		ArrayList<String>userData=(ArrayList<String>) obj;
		if (userData.get(0).equals("CheckLibrarianManager")) {
			isManager=userData.get(1);
			setFields(true);
		}
		else if (userData.get(0).equals("SearchMember"))
		{
			System.out.println(userData);
			if (update==false) {
				setCardMember(userData);
			}
			if (userData.get(1).equals("NotExist")) {

				showFailed("Member does not exist");
			}
			else {
				if (update) {
					showSuccess("The member "+txtFirst_Name.getText()+" "+txtLast_Name.getText()+" Details updated successfully");
					memberStatus=cmbStatus.getValue().toString();
					update=false;

				}
			}
			if (isManager.equals("true")) {
				setEditableLibrarianManager();
			}
			else {
				setEditableLibrarian();
			}
		}
	}
	private void setEditableLibrarian() {
		txtFirst_Name.setEditable(false);
		txtLast_Name.setEditable(false);
		txtPhone_Number.setEditable(false);
		txtEmail.setEditable(false);
		txtArea_Notes.setEditable(true);
		cmbStatus.setEditable(false);
		btnSave.setDisable(false);//librarian cannot edit details
		txtFirst_Name.setDisable(true);
		txtLast_Name.setDisable(true);
		txtPhone_Number.setDisable(true);
		txtEmail.setDisable(true);
		cmbStatus.setDisable(true);
		txtArea_Notes.setDisable(false);
		btnHistory.setDisable(false);
		btnLates_Lostbook.setDisable(false);
		btnStatus.setDisable(false);
	}
	private void setEditableLibrarianManager() {
		txtFirst_Name.setEditable(false);
		txtLast_Name.setEditable(false);
		txtPhone_Number.setEditable(false);
		txtEmail.setEditable(false);
		txtArea_Notes.setEditable(true);
		cmbStatus.setEditable(false);
		txtArea_Notes.setDisable(false);
		btnSave.setDisable(false);//librarian cannot edit details
		cmbStatus.setDisable(false);
		btnHistory.setDisable(false);
		btnLates_Lostbook.setDisable(false);
		btnStatus.setDisable(false);
	}
	private void setCardMember(ArrayList<String> memberData) {
		txtMember_ID.setEditable(false);
		txtFirst_Name.setText(memberData.get(5));
		txtLast_Name.setText(memberData.get(6));
		txtPhone_Number.setText(memberData.get(2));
		txtEmail.setText(memberData.get(3));
		cmbStatus.setValue(memberData.get(7));
		txtArea_Notes.setText(memberData.get(8));
		memberStatus=cmbStatus.getValue().toString();
		System.out.println(memberStatus+" member status in card reader");


	}

	private void setFields(Boolean cond) {
		txtFirst_Name.setDisable(cond);
		txtLast_Name.setDisable(cond);
		txtPhone_Number.setDisable(cond);
		txtEmail.setDisable(cond);
		txtArea_Notes.setDisable(cond);
		cmbStatus.setDisable(cond);
		btnHistory.setDisable(cond);
		btnLates_Lostbook.setDisable(cond);
		btnSave.setDisable(cond);
	}
	public void resetField() {
		txtFirst_Name.setText("");
		txtLast_Name.setText("");
		txtPhone_Number.setText("");
		txtEmail.setText("");
		txtArea_Notes.setText("");
		cmbStatus.setValue("");
	}

	private void setMsStatusComboBox() {
		ArrayList<String> msStatusList = new ArrayList<String>();	
		msStatusList.add("Locked");
		msStatusList.add("Frozen");
		msStatusList.add("Active");

		list = FXCollections.observableArrayList(msStatusList);
		cmbStatus.setItems(list);

	}

	@Override
	public void freshStart() {
		txtFirst_Name.setText("");
		txtLast_Name.setText("");
		txtPhone_Number.setText("");
		txtEmail.setText("");
		txtArea_Notes.setText("");
		cmbStatus.setValue("");			
	}
	public String getMemberStatus() {
		return memberStatus;
	}
	public void setMemberStatus(String memberStatus) {
		this.memberStatus = memberStatus;
	}

}
