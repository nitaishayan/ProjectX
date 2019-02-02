package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Client.Client;
import Common.GuiInterface;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.Main;
import logic.RegistrationController;

/**
 * this is a controller of the 'MemberMenuGUI' fxml.
 * @author lior
 *
 */
public class MemberMenuGUI implements Initializable,GuiInterface{

	@FXML
	private SplitPane mainSplitPane;

	@FXML
	private AnchorPane leftPane;

	@FXML
	private Label lblUser_name;

	@FXML
	private ImageView asd;

	@FXML
	private Button btnPersonalData;

	@FXML
	private Button btnSearchBook;

	@FXML
	private Button btnHistoryActivities;

	@FXML
	private Button btnExtend;

	@FXML
	private AnchorPane rightPane;

	Button clickednow;

	/**
	 * this method clear the effect of all the buttons in the screen
	 *  and set an effect that return from the method 'InnerShadow' on a specific button
	 * @param btn - the specific button that remains with the effect 'InnerShadow'
	 */
	public void clerEffect(Button btn) {
		btnExtend.setEffect(null);
		btnHistoryActivities.setEffect(null);
		btnPersonalData.setEffect(null);
		btnSearchBook.setEffect(null);
		btn.setEffect(setEffect());
	}

	/**
	 * create an effect of type InnerShadow.
	 * @return innerShadow - the effect.
	 */
	public InnerShadow setEffect() {
		InnerShadow innerShadow = new InnerShadow();
		innerShadow.setChoke(0.32);
		innerShadow.setColor(Color.web("#2176ff"));
		return innerShadow;
	}

	/**
	 * open the INBOX window and present the message that received.
	 * the method also refresh and clear the previous screen that was last open. 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void InboxMouse(MouseEvent event) throws IOException {
		Stage primaryStage = new Stage();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				if (clickednow.equals(btnHistoryActivities)) {
					AnchorPane pane = null;
					try {
						pane = FXMLLoader.load(getClass().getResource("/GUI/ActivitiesHistory.fxml"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rightPane.getChildren().setAll(pane);
				}
				else if (clickednow.equals(btnPersonalData)) {
					AnchorPane pane = null;
					try {
						pane = FXMLLoader.load(getClass().getResource("/GUI/ReaderPersonalData.fxml"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rightPane.getChildren().setAll(pane);
				}
				else if (clickednow.equals(btnSearchBook)) {
					AnchorPane pane = null;
					try {
						pane = FXMLLoader.load(getClass().getResource("/GUI/SearchBook.fxml"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rightPane.getChildren().setAll(pane);
				}
				else if (clickednow.equals(btnExtend)) {
					AnchorPane pane = null;
					try {
						pane = FXMLLoader.load(getClass().getResource("/GUI/MemberExtendLoanPeriod.fxml"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rightPane.getChildren().setAll(pane);
				}
			}
		});
		primaryStage.initModality(Modality.APPLICATION_MODAL);
		FXMLLoader loader = new FXMLLoader();
		AnchorPane root = loader.load(getClass().getResource("/GUI/InBox.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("User Messages");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.showAndWait();
	}

	/**
	 * opens the fxml screen of the member menu.
	 * @throws IOException - throws exception if the load of the FXML does not successed.
	 */
	public void Display() throws IOException {
		Stage primaryStage=new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/ReaderMenu.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Member Menu");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	/**
	 * set the default screen that open with member menu screen.
	 * set the slider location and lock it.
	 * @throws IOException
	 */
	public void init() throws IOException {
		Main.client.clientUI= this;
		lblUser_name.setText(Client.arrayUser.get(2)+" "+Client.arrayUser.get(3));
		leftPane.maxWidthProperty().bind(mainSplitPane.widthProperty().multiply(0.1855));
		leftPane.minWidthProperty().bind(mainSplitPane.widthProperty().multiply(0.1855));
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/ReaderPersonalData.fxml"));
		clickednow=btnPersonalData;
		clerEffect(btnPersonalData);
		rightPane.getChildren().setAll(pane);
	}

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	/**
	 * Method that  log out from the system
	 * the method is send to DB and notify the server that the user is log out
	 * @param event		occurs when click on the button 
	 * @throws IOException
	 */
	public void Logout(ActionEvent event) throws IOException {
		OBLcontroller.memberStage.close();
		RegistrationController.logout(Client.arrayUser.get(0),Client.arrayUser.get(1));
		Client.arrayUser.clear();
		Main.primary.show();
	}


	/**
	 * Method that  show all the reports that the member can watch for himself
	 * the report are: status change history, loan history, and delay/lost books
	 * @param event		occurs when click on the button 
	 * @throws IOException
	 */
	@FXML
	void HistoryActivities(ActionEvent event) throws IOException {
		clickednow=btnHistoryActivities;
		clerEffect(btnHistoryActivities);
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/ActivitiesHistory.fxml"));
		rightPane.getChildren().setAll(pane);
	}

	/**
	 * show the reader personal data in the current window - able to search a card reader through the window
	 * @param event		occurs when click on the button 
	 * @throws IOException
	 */
	@FXML
	void PersonalDataScreen(ActionEvent event) throws IOException {
		clickednow=btnPersonalData;
		clerEffect(btnPersonalData);
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/ReaderPersonalData.fxml"));
		rightPane.getChildren().setAll(pane);
	}

	/**
	 * show the search book screen in the current window
	 * @param event		occurs when click on the button 
	 * @throws IOException
	 */
	@FXML
	void SearchScreen(ActionEvent event) throws IOException {
		clickednow=btnSearchBook;
		clerEffect(btnSearchBook);
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/SearchBook.fxml"));
		rightPane.getChildren().setAll(pane);
	}

	/**
	 * show the extend loan period screen in the current window
	 * @param event		occurs when click on the button 
	 * @throws IOException
	 */
	@FXML
	void extendLoanPeriodScreen(ActionEvent event) throws IOException{
		clickednow=btnExtend;
		clerEffect(btnExtend);
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/MemberExtendLoanPeriod.fxml"));
		rightPane.getChildren().setAll(pane);
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
