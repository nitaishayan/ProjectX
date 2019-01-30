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

	@FXML
	private Pane pane;

	@FXML
	private Button btnLogin;

	@FXML
	private Button btnSearchBook;

	@FXML
	private Label lblForgot;

	@FXML
	private TextField txtUserName;

	@FXML
	private TextField txtPassword;

	public static Stage librarianStage;
	public static Stage memberStage;
	public static Stage searchForReader;



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
    
	public void openMemberMenuScreen() throws IOException {
		Main.primary.hide();
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		SplitPane root = loader.load(getClass().getResource("/GUI/ReaderMenu.fxml").openStream());
		Scene scene = new Scene(root);			
		//		scene.getStylesheets().add(getClass().getResource("/gui/StudentForm.css").toExternalForm());
		primaryStage.setScene(scene);	
		//Logout when pressed the "exit" button
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				RegistrationController.logout(Client.arrayUser.get(0),Client.arrayUser.get(1));
				System.out.println("Stage is closing");
			}
		});
		memberStage=primaryStage;
		primaryStage.show();		
	}

	public void openLibrarianMenuScreen() throws IOException {
		Main.primary.hide();
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		SplitPane root = loader.load(getClass().getResource("/GUI/LibrarianMenu.fxml"));//.openStream());
		Scene scene = new Scene(root);			
		//		scene.getStylesheets().add(getClass().getResource("/gui/StudentForm.css").toExternalForm());
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
		primaryStage.show();		
	}

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



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Main.client.clientUI=this;
		freshStart();
	}


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


	@Override
	public void showSuccess(String message) {
		// TODO Auto-generated method stub

	}


	@Override
	public void showFailed(String message) {
		Alert alert=new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(message);
		alert.showAndWait();
		freshStart();
		//		setFields(true);

	}


	@Override
	public void freshStart() {
		txtUserName.clear();
		txtPassword.clear();
	}
}
