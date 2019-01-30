package Server;

import Client.Client;
import Common.GuiInterface;
import GUI.OBLcontroller;
import GUI.RegistrationGUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;


public class ServerMain extends Application implements GuiInterface {

	final public static int DEFAULT_PORT = 5555;
	public static String host;

	@Override
	public void start(Stage primaryStage) throws Exception {
		int port = 0; //Port to listen on
		try {
			port = Integer.parseInt(getParameters().getRaw().get(0)); //Get port from command line
		} catch(Throwable t) {
			port = DEFAULT_PORT; //Set port to 5555
		}	
		Server sv = new Server(port);
		try {
			sv.listen(); //Start listening for connections
		} catch (Exception ex) {
			showFailed("ERROR - Could not listen for clients!");
		}
 		Parent root = FXMLLoader.load(getClass().getResource("/GUI/DeclareGraduate.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("OBL System Server");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();	
	}

	public static void main(String[] args) {
		launch(args);
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
	public void display(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showSuccess(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void freshStart() {
		// TODO Auto-generated method stub
		
	}
}
