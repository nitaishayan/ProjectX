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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import logic.BookHandlerController;
import logic.InventoryController;

/**
 * This class remove an existing copy in the library.
 *
 */
public class InventoryRemoveGUI implements Initializable, GuiInterface {

	@FXML
	private AnchorPane MainPane;

	@FXML
	private Button btnRemove;

	@FXML
	private TextField txtBook_Name;

	@FXML
	private TextField txtEdition;

	@FXML
	private TextField txtTheme;

	@FXML
	private TextField txtAuthor;

	@FXML
	private TextField txtPrint_Date;

	@FXML
	private TextField txtCatalog_Number;

	@FXML
	private TextField txtCopies;

	@FXML
	private TextField txtPurchase_Date;

	@FXML
	private TextField txtShelf_Location;

	@FXML
	private TextArea txtDescription;

	@FXML
	private CheckBox CHBOX_YES;

	@FXML
	private CheckBox CHBOX_NO;

	@FXML
	private Button btn_browse;

	@FXML
	private TextField txtPdf;

	String wanted;


	/**
	 * This method checks the "YES" option in the wanted section and clear the "NO" option.
	 * @param event - event from press on CheckBox.
	 */
	@FXML
	void WANTED_YES(ActionEvent event) {
		CHBOX_NO.setSelected(false);
		wanted="true";
	}

	/**
	 * This method checks the "NO" option in the wanted section and clear the "YES" option.
	 * @param event - event from press on CheckBox.
	 */
	@FXML
	void WANTED_NO(ActionEvent event) {
		CHBOX_YES.setSelected(false);
		wanted="false";
	}

	/**
	 * This method call the RemoveCopy() function of the InventoryController with the catalog number chosen.
	 * @param event - event from press the "RemoveCopy" button.
	 */
	@FXML
	void RemoveCopy(ActionEvent event) {
		InventoryController.RemoveCopy(txtCatalog_Number.getText());
	}

	/**
	 *  This method call the checkExistenceByCopy() function of the InventoryController with the catalog number chosen.
	 * @param event - event from press the "ENTER" button on the keyboard.
	 */
	@FXML
	void PressEnter(KeyEvent event) {
		if (event.getCode()==KeyCode.ENTER) {
			InventoryController.checkExistenceByCopy(txtCatalog_Number.getText());
		}
	}

	/**
	 * This method call the checkExistenceByCopy() function of the InventoryController with the catalog number chosen.
	 * @param event - event from press the "Magnifier" Icon.
	 */
	@FXML
	void copyIDMouse(MouseEvent event) {
		InventoryController.checkExistenceByCopy(txtCatalog_Number.getText());
	}

	/**
	 * This method move the user back to the Main Inventory screen.
	 * @param event - event from press on "Back" button.
	 * @throws IOException
	 */
	@FXML
	void BackToInventory(ActionEvent event) throws IOException {
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/Inventory.fxml"));
		MainPane.getChildren().setAll(pane);
	}

	/**
	 * This method enable/disable the fields.
	 * @param choice - a boolean variable that set enable/disable to the corresponding components.
	 */
	public void enable(boolean choice) {
		this.txtBook_Name.setDisable(choice);
		this.txtEdition.setDisable(choice);
		this.txtTheme.setDisable(choice);
		this.txtAuthor.setDisable(choice);
		this.txtPrint_Date.setDisable(choice);
		this.txtCopies.setDisable(choice);
		this.txtPurchase_Date.setDisable(choice);
		this.txtShelf_Location.setDisable(choice);
		this.txtDescription.setDisable(choice);
		this.CHBOX_NO.setDisable(choice);
		this.CHBOX_YES.setDisable(choice);
		this.btnRemove.setDisable(choice);
		}

	/**
	 * This method set the variable of the GuiInterface in the client to this.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Client.clientUI=this;
		txtPdf.setEditable(false);

	}

	/**
	 * This method update the inventory-remove screen with the data that return.
	 * The method give the user the functionality to remove a copy of the specific book in the inventory.
	 * @param obj - ArrayList with the relevant data for create this window (all the information that's needed).
	 */
	@Override
	public void display(Object msg) {
		System.out.println(((ArrayList<String>) msg));
		this.txtBook_Name.setText(((ArrayList<String>) msg).get(2));
		this.txtCopies.setText(((ArrayList<String>) msg).get(3));
		wanted=((ArrayList<String>) msg).get(4);
		if (wanted.equals("true")) {
			CHBOX_YES.setSelected(true);
		}
		else CHBOX_NO.setSelected(true);
		this.txtAuthor.setText(((ArrayList<String>) msg).get(5));
		this.txtEdition.setText(((ArrayList<String>) msg).get(6));
		this.txtPrint_Date.setText(((ArrayList<String>) msg).get(7));
		this.txtTheme.setText(((ArrayList<String>) msg).get(8));
		this.txtDescription.setText(((ArrayList<String>) msg).get(9));
		this.txtPurchase_Date.setText(((ArrayList<String>) msg).get(10));
		this.txtShelf_Location.setText(((ArrayList<String>) msg).get(12));
		this.txtPdf.setText("pdf");
		btnRemove.setDisable(false);
		txtCatalog_Number.setEditable(false);
		btn_browse.setDisable(true);
	}

	/**
	 * This method show an error pop-up on the screen with a given message.
	 * @param String - the message that will be shown in the pop-up.
	 */
	@Override
	public void showFailed(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(message);
		alert.showAndWait();	
		freshStart();
	}

	/**
	 * This method show information pop-up on the screen with the given success message.
	 * @param string - the message that will be shown in the pop-up.
	 */
	@Override
	public void showSuccess(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Confirm");
		alert.setHeaderText(message);
		alert.showAndWait();	
		freshStart();
		txtCatalog_Number.setEditable(true);
	}

	/**
	 * This method clean up the fields on the screen.
	 */
	@Override
	public void freshStart() {
		this.txtBook_Name.clear();
		this.txtCopies.clear();
		CHBOX_NO.setSelected(false);
		CHBOX_YES.setSelected(false);
		this.txtAuthor.clear();
		this.txtEdition.clear();
		this.txtPrint_Date.clear();
		this.txtTheme.clear();
		this.txtDescription.clear();
		this.txtPurchase_Date.clear();
		this.txtShelf_Location.clear();
		btnRemove.setDisable(true);
		btn_browse.setDisable(true);
	}
}

