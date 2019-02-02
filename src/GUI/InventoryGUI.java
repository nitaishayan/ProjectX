package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Common.GuiInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.InventoryController;
import logic.Main;

/**
 * This class present the main inventory GUI and his options to select.
 *
 */
public class InventoryGUI implements Initializable, GuiInterface {
	@FXML
	private AnchorPane MainPane;


	/**
	 * This method shows the user the Inventory Add screen.	
	 * @param event - event cause from pressing on "Add" button.
	 * @throws IOException
	 */
	@FXML
	void AddScreen(ActionEvent event) throws IOException {
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/InventoryAdd.fxml"));
		MainPane.getChildren().setAll(pane);
	}

	/**
	 * This method shows the user the Inventory Edit screen.	
	 * @param event - event cause from pressing on "Edit" button.
	 * @throws IOException
	 */
	@FXML
	void EditScreen(ActionEvent event) throws IOException {
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/InventoryEdit.fxml"));
		MainPane.getChildren().setAll(pane);
	}

	/**
	 * This method shows the user the Inventory Remove screen.	
	 * @param event - event cause from pressing on "Remove" button.
	 * @throws IOException
	 */
	@FXML
	void RemoveScreen(ActionEvent event) throws IOException {
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/InventoryRemove.fxml"));
		MainPane.getChildren().setAll(pane);
	}

	/**
	 * This method set the variable of the GuiInterface in the client to this.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.client.clientUI=this;		

	}

	/**
	 * Not used method(must implement because the implementation of GuiInterface)
	 */
	@Override
	public void showSuccess(String string) {
		// TODO Auto-generated method stub

	}

	/**
	 * Not used method(must implement because the implementation of GuiInterface)
	 */
	@Override
	public void display(Object obj) {
		// TODO Auto-generated method stub

	}

	/**
	 * Not used method(must implement because the implementation of GuiInterface)
	 */
	@Override
	public void showFailed(String message) {
		// TODO Auto-generated method stub

	}

	/**
	 * Not used method(must implement because the implementation of GuiInterface)
	 */
	@Override
	public void freshStart() {
		// TODO Auto-generated method stub

	}


}
