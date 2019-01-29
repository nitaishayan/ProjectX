package GUI;

import java.net.URL;
import java.util.ResourceBundle;
import Common.GuiInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import logic.CommonController;
import logic.Main;
import logic.RegistrationController;

public class RegistrationGUI implements Initializable, GuiInterface{
	/**
	 * this class is made to give the system the functionality to register members to the library.
	 * the method of this class check the input from the user(librarian) first and then transfer the input
	 * to the controller for the continuing of the process.
	 */
	@FXML
	private AnchorPane Registration;

	@FXML
	private Button btnSave;

	@FXML
	private TextField txtPhone_number;

	@FXML
	private TextField txtID;

	@FXML
	private TextField txtLast_name;

	@FXML
	private TextField txtFirst_name;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtPassword;

	/**
	 * this method get all the input from the user for registration.
	 * the method first check if all the fields are full with the method checkfields and after check if some
	 * of the input is valid (email,id,password,phone number) with method checkRegistrationInput.
	 * if all the test end with success the input go ahead to the RegistrationController.
	 * @param event - event from click on save
	 */
	@FXML
	void onSaveClick(ActionEvent event) {
		String ans = CommonController.checkRegistrationInput(txtPhone_number.getText(), txtEmail.getText(), txtID.getText(),txtPassword.getText());
		if (checkfields())
		{
			showFailed("Fill all the dields");
			return;
		}
		else if(!(ans.equals("Success")))
			showFailed(ans);
		else {	RegistrationController.registration(txtPhone_number.getText(),txtID.getText(),txtLast_name.getText(),txtFirst_name.getText(),txtEmail.getText(),txtPassword.getText());
		}

	}

	@FXML
	void registerEnter(KeyEvent event) {
		if (event.getCode()==KeyCode.ENTER) {
			String ans = CommonController.checkRegistrationInput(txtPhone_number.getText(), txtEmail.getText(), txtID.getText(),txtPassword.getText());
			if (checkfields())
			{
				showFailed("Fill all the dields");
				return;
			}
			else if(!(ans.equals("Success")))
				showFailed(ans);
			else {	RegistrationController.registration(txtPhone_number.getText(),txtID.getText(),txtLast_name.getText(),txtFirst_name.getText(),txtEmail.getText(),txtPassword.getText());
			}
		}
	}
	/**
	 * this method check if all the fields in the screen are full
	 * @return true if yes, false else.
	 */
	private boolean checkfields() {
		if ((txtEmail.getText()).equals(""))
			return true;
		if ((txtFirst_name.getText()).equals(""))
			return true;
		if ((txtID.getText()).equals(""))
			return true;
		if ((txtLast_name.getText()).equals(""))
			return true;
		if ((txtPassword.getText()).equals(""))
			return true;
		if ((txtPhone_number.getText()).equals(""))
			return true;
		return false;
	}

	/**
	 * this method show information pop-up on the screen with given message
	 * @param string- the message that shown in the pop-up.
	 */
	@Override
	public void showSuccess(String string) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Confirm");
		alert.setHeaderText(string);
		alert.showAndWait();	
	}

	/**
	 *  not used method(must implement because the implementation of GuiInterface)
	 */
	@Override
	public void display(Object obj) {
	}

	/**
	 * this method show error pop-up on the screen with given message
	 * @param string- the message that shown in the pop-up.
	 */
	@Override
	public void showFailed(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Message");
		alert.setHeaderText(message);
		alert.showAndWait();	
	}

	/**
	 * this method clean up the fields on the screen.
	 */
	@Override
	public void freshStart() {
		txtID.setText("");
		txtFirst_name.setText("");
		txtLast_name.setText("");
		txtEmail.setText("");
		txtPhone_number.setText("");
		txtPassword.setText("");	
	}

	/**
	 * this method set the variable of the GuiInterface in the client to this.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Main.client.clientUI=this;	
	}
}
