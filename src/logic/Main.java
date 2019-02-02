package logic;

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

/**
 * This method runs a main client
 * @param DEFAULT_PORT - This is the default port in our system.
 * @param host - This holds the host recived ip or localhost.
 * @param client - This holds the Client object that is being created.
 * @param primary - This variable saves the relevant stage.
 */
public class Main extends Application implements GuiInterface {

	final public static int DEFAULT_PORT = 5555;
	public static String host;
	public static Client client;
	public static Stage primary;
	
	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Main.host = getParameters().getRaw().get(0);
		}
		catch(Exception e) {
			Main.host = "localhost";
		}
		client=new Client(host,DEFAULT_PORT,this);
 		Parent root = FXMLLoader.load(getClass().getResource("/GUI/OBL-openScreen.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("OBL System");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	              System.out.println("Stage is closing");
	          }
		});
		primary=primaryStage;
		primaryStage.show();	
	}


	/**
	 * This is a standard class Application method.
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}


	/**
	 * this method show error pop-up on the screen with given message
	 * @param String- the message that shown in the pop-up.
	 */
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

	
	/* (non-Javadoc)
	 * @see Common.GuiInterface#display(java.lang.Object)
	 */
	@Override
	public void display(Object obj) {
		// TODO Auto-generated method stub

	}

	
	/* (non-Javadoc)
	 * @see Common.GuiInterface#showSuccess(java.lang.String)
	 */
	@Override
	public void showSuccess(String message) {
		// TODO Auto-generated method stub

	}

	
	/* (non-Javadoc)
	 * @see Common.GuiInterface#freshStart()
	 */
	@Override
	public void freshStart() {
		// TODO Auto-generated method stub
		
	}
}
