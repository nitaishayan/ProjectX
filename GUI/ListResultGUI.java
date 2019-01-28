package GUI;
import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

import Common.GuiInterface;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Main;

public class ListResultGUI implements Initializable, GuiInterface {


	
	@FXML
    private VBox vbox12;

	@Override
	public void showSuccess(String string) {
		// TODO Auto-generated method stub

	}

	@Override
	public void display(Object obj) {
		Label lable1 =new Label("lior");
		vbox12.getChildren().add(lable1);
		Stage primaryStage = new Stage();
		primaryStage.initModality(Modality.APPLICATION_MODAL);
		FXMLLoader loader = new FXMLLoader();
		ScrollPane root;
		try {
			root = loader.load(getClass().getResource("/GUI/ResultGUI.fxml").openStream());
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void showFailed(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Main.client.clientUI = this;
	}

	@Override
	public void freshStart() {
		// TODO Auto-generated method stub
		
	}
}


