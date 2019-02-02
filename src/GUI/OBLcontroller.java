package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.net.ssl.SSLException;
import Client.Client;
import Common.GuiInterface;
import Common.User;
import GUI.LibrarianMenuGUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.Main;
import logic.RegistrationController;

public class OBLcontroller implements Initializable, GuiInterface {
	/**
	 * This class connect between the input in the GUI to the controller of login,log-out and registration process.
	 * The class giving the functionality to login/log-out to the system and registration of a new member.
	 */
	@FXML
	private Pane pane;

	@FXML
	private Button btnLogin;

	@FXML
	private Button btnSearchBook;

	@FXML
	private TextField txtUserName;

	@FXML
	private TextField txtPassword;

	public static Stage librarianStage;
	public static Stage memberStage;
	public static Stage searchForReader;


	/**
	 * This method call to a function from RegistrationController that execute the login process - by pressing the "Login" button.
	 * Also, checks if the user didn't fill one of the fields.
	 * @param event - event from pressing on "Login" button.
	 * @throws IOException
	 */
	public void login(ActionEvent event) throws IOException {
		Main.client.clientUI=this;
		if(!(txtUserName.getText().isEmpty()==false&&txtPassword.getText().isEmpty()==false))
			showFailed("Some fields are empty");
		else {
			RegistrationController.login(txtUserName.getText(),txtPassword.getText());
			freshStart();
		}

	}


	public void forgot(MouseEvent  event) throws IOException {
	}

	/**
	 * This method call to a function from RegistrationController that execute the login process - by pressing "ENTER" on the keyboard.
	 * Also, checks if the user didn't fill one of the fields.
	 * @param event - event from pressing on "ENTER" on the keyboard.
	 * @throws IOException 
	 */
    @FXML
    void loginEnter(KeyEvent event) {
		if (event.getCode()==KeyCode.ENTER) {
			Main.client.clientUI=this;
			if(!(txtUserName.getText().isEmpty()==false&&txtPassword.getText().isEmpty()==false))
				showFailed("Some fields are empty");
			else {
				RegistrationController.login(txtUserName.getText(),txtPassword.getText());
				freshStart();
			}			
		}
    }
    /**
   	 * This method transfer the user to the ReaderMenu screen - if the user is a member.
   	 * It's has an event handler in case of exiting the screen which trigger the log-out process.
   	 * @throws IOException
   	 */
	public void openMemberMenuScreen() throws IOException {
		System.out.println("member array user size: " + Client.arrayUser.size());
		Main.primary.hide();
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		SplitPane root = loader.load(getClass().getResource("/GUI/ReaderMenu.fxml"));
		Scene scene = new Scene(root);			
		primaryStage.setScene(scene);	
		//Logout when pressed the "exit" button
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				RegistrationController.logout(Client.arrayUser.get(0),Client.arrayUser.get(1));
				System.out.println("Stage is closing");
			}
		});
		memberStage=primaryStage;
		primaryStage.setMinWidth(1540);
		primaryStage.setMaxWidth(1540);
		primaryStage.show();		
	}
	 /**
  	 * This method transfer the user to the LibrarianMenu screen - if the user is a librarian.
  	 * It's has an event handler in case of exiting the screen which trigger the log-out process.
  	 * @throws IOException
  	 */
	public void openLibrarianMenuScreen() throws IOException {
		System.out.println("librarian array user size: " + Client.arrayUser.size());
		Main.primary.hide();
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		SplitPane root = loader.load(getClass().getResource("/GUI/LibrarianMenu.fxml"));
		Scene scene = new Scene(root);			
		primaryStage.setScene(scene);
		//Logout when pressed the "exit" button
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				RegistrationController.logout(Client.arrayUser.get(0),Client.arrayUser.get(1));
				System.out.println("Stage is closing");
			}
		});
		primaryStage.setScene(scene);	
		librarianStage=primaryStage;
		primaryStage.setMinWidth(1540);
		primaryStage.setMaxWidth(1540);
		primaryStage.show();		
	}
	/**
	* This method transfer the user to the SearchBook screen.
	* @param event - event from press on "Search Book" button in the left pane.
	* @throws IOException
	*/
	public void searchBook(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		AnchorPane root = loader.load(getClass().getResource("/GUI/SearchBook.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		searchForReader=primaryStage;
		primaryStage.setMinWidth(1232);
		primaryStage.setMaxWidth(1232);
		primaryStage.setMaxHeight(928);
		primaryStage.setMinHeight(928);
		primaryStage.show();
	}


	/**
	 * This method set the variable of the GuiInterface in the client to this.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Main.client.clientUI=this;
		freshStart();
	}

	/**
	 * This method gets the values which have returned from the login process.
	 * The option are - the user doesn't exists in system, the user is already connected to the system, the member trying to login is graduated hence cannot login,
	 * the member trying to connect is locked (Status) hence can't login, the user is a member and the user is a librarian.
	 * @param obj - ArrayList with the relevant data for create this window and maneuver through the different methods (all the information that needed).
	 */
	@Override
	public void display(Object obj) {
		ArrayList<String> msg = (ArrayList<String>)obj;
		switch (msg.get(5)) {
		case "-1":
			showFailed("User doesnt exist in the system.");
			Client.arrayUser.clear();
			break;
		case "0":
			showFailed("The user is already logged into the system!");
			Client.arrayUser.clear();
			break;
		case "1":
			try {
				openLibrarianMenuScreen();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		case "2":
			try {
				openMemberMenuScreen();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		case "3":
			showFailed("The member already graduated hence he can't login!");
			Client.arrayUser.clear();
			break;
		case "4":
			showFailed("The member is locked, and can't connect to the library.");
			Client.arrayUser.clear();
			break;
		}
	}

	/**
	* Not used method(must implement because the implementation of GuiInterface)
	*/
	@Override
	public void showSuccess(String message) {
		// TODO Auto-generated method stub

	}

	/**
	 * This method show error pop-up on the screen with given message.
	 * @param String- the message that will be shown in the pop-up.
	 */
	@Override
	public void showFailed(String message) {
		Alert alert=new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(message);
		alert.showAndWait();
		freshStart();
		//		setFields(true);

	}

	/**
	 * This method clean up the fields on the screen.
	 */
	@Override
	public void freshStart() {
		txtUserName.clear();
		txtPassword.clear();
	}
}
