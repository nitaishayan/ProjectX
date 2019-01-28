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

public class InventoryGUI implements Initializable, GuiInterface {
	@FXML
	private AnchorPane MainPane;



	@FXML
	void AddScreen(ActionEvent event) throws IOException {
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/InventoryAdd.fxml"));
		MainPane.getChildren().setAll(pane);
	}

	@FXML
	void EditScreen(ActionEvent event) throws IOException {
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/InventoryEdit.fxml"));
		MainPane.getChildren().setAll(pane);
	}

	@FXML
	void RemoveScreen(ActionEvent event) throws IOException {
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/InventoryRemove.fxml"));
		MainPane.getChildren().setAll(pane);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.client.clientUI=this;		

	}

	@Override
	public void showSuccess(String string) {
		// TODO Auto-generated method stub

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


}
