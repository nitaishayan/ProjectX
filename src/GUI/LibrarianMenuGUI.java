package GUI;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import Client.Client;
import Common.GuiInterface;
import Common.Librarian;
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
import javafx.scene.control.SplitPane.Divider;
import javafx.scene.control.TextField;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.Main;
import logic.RegistrationController;

/**
	* This class represents the Librarian Menu.
 */
public class LibrarianMenuGUI implements Initializable, GuiInterface{

	@FXML
	private SplitPane mainSplitPane;

	@FXML
	private AnchorPane leftPane;

	@FXML
	private Button btnRegistration;

	@FXML
	private Button btnSearchReader;

	@FXML
	private Button btnSearchBook;

	@FXML
	private Button btn_LoanBook;

	@FXML
	private Button btnReturnBook;

	@FXML
	private Button btnInventory;

	@FXML
	private Button btnShow_Report;

	@FXML
	private Button btnLog_out;

	@FXML
	private Label lblUser_name;

	@FXML
	private Button btnExtend;

	@FXML
	private AnchorPane rightPane;

	Button clickednow;


	/**
	* This method call the setEffect() method and applies it to the specific buttons.
	*/
	public void clerEffect(Button btn) {
		btnRegistration.setEffect(null);
		btnSearchReader.setEffect(null);
		btnSearchBook.setEffect(null);
		btn_LoanBook.setEffect(null);
		btnReturnBook.setEffect(null);
		btnInventory.setEffect(null);
		btnShow_Report.setEffect(null);
		btnExtend.setEffect(null);
		btn.setEffect(setEffect());
	}

	/**
	* This method set a special effect to a component.
	* @return return the InnerShadow instance with the effect.
	*/
	public InnerShadow setEffect() {
		InnerShadow innerShadow = new InnerShadow();
		innerShadow.setChoke(0.32);
		innerShadow.setColor(Color.web("#2176ff"));
		return innerShadow;
	}

	/**
	 * This method opens the screen of inbox messages.
	 * @param event - event from press on "Inbox" Icon.
	 * @throws IOException
	 */
	@FXML
	void InboxMouseClick(MouseEvent event) throws IOException {
		Stage primaryStage = new Stage();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				if (clickednow.equals(btnInventory)) {
					AnchorPane pane = null;
					try {
						pane = FXMLLoader.load(getClass().getResource("/GUI/Inventory.fxml"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rightPane.getChildren().setAll(pane);
				}
				else if(clickednow.equals(btn_LoanBook)) {
					AnchorPane pane = null;
					try {
						pane = FXMLLoader.load(getClass().getResource("/GUI/Loan.fxml"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rightPane.getChildren().setAll(pane);
				}
				else if(clickednow.equals(btnRegistration)) {
					AnchorPane pane = null;
					try {
						pane = FXMLLoader.load(getClass().getResource("/GUI/Registration.fxml"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rightPane.getChildren().setAll(pane);
				}
				else if(clickednow.equals(btnReturnBook)) {
					AnchorPane pane = null;
					try {
						pane = FXMLLoader.load(getClass().getResource("/GUI/ReturnBook.fxml"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rightPane.getChildren().setAll(pane);
				}
				else if(clickednow.equals(btnSearchBook)) {
					AnchorPane pane = null;
					try {
						pane = FXMLLoader.load(getClass().getResource("/GUI/LibrarianBookSearch.fxml"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rightPane.getChildren().setAll(pane);
				}
				else if(clickednow.equals(btnSearchReader)) {
					AnchorPane pane = null;
					try {
						pane = FXMLLoader.load(getClass().getResource("/GUI/ReaderCard.fxml"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rightPane.getChildren().setAll(pane);
				}
				else if(clickednow.equals(btnShow_Report)) {
					AnchorPane pane = null;
					try {
						pane = FXMLLoader.load(getClass().getResource("/GUI/StatisticReports.fxml"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					rightPane.getChildren().setAll(pane);
				}
				else if(clickednow.equals(btnExtend)) {
					AnchorPane pane = null;
					try {
						pane = FXMLLoader.load(getClass().getResource("/GUI/LibrarianExtend.fxml"));
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
		*  This method set the variable of the GuiInterface in the client to this.
	* It also set the user details in the specifi label.
		* @throws IOException
	 */
	public void init() throws IOException {
		Main.client.clientUI=this;
		if (Client.arrayUser.get(4).equals("false")) {
			btnShow_Report.setVisible(false);
		}
		leftPane.maxWidthProperty().bind(mainSplitPane.widthProperty().multiply(0.1855));
		leftPane.minWidthProperty().bind(mainSplitPane.widthProperty().multiply(0.1855));
		clerEffect(btnRegistration);
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/Registration.fxml"));
		clickednow=btnRegistration;
		rightPane.getChildren().setAll(pane);
		lblUser_name.setText(Client.arrayUser.get(2)+" "+Client.arrayUser.get(3));               	
	}

	/**
	 *  This method gets from GUI the log-out request of the user and move it to the RegistrationController.
		* @param event - event from press on the "log-out" button.
		* @throws IOException
		*/
	public void Logout(ActionEvent event) throws IOException {
		OBLcontroller.librarianStage.close();
		RegistrationController.logout(Client.arrayUser.get(0),Client.arrayUser.get(1));
		Client.arrayUser.clear();
		Main.primary.show();
	}

	/**
	 *  This method gets from GUI the opening inventory screen request of the user and move it to the RegistrationController.
	 * @param event - event from press on the "Inventory" button.
	 * @throws IOException
	 */
	@FXML
	void InventoryScreen(ActionEvent event) throws IOException {
		clickednow=btnInventory;
		clerEffect(btnInventory);
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/Inventory.fxml"));
		rightPane.getChildren().setAll(pane);
	}

	/**
	 *  This method gets from GUI the registration screen opening request of the user.
	 * @param event - event from press on the "Registration" button.
	 * @throws IOException
	 */
	@FXML
	void RegistrationScreen(ActionEvent event) throws IOException {
		clickednow=btnRegistration;
		clerEffect(btnRegistration);
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/Registration.fxml"));
		rightPane.getChildren().setAll(pane);
	}

	/**
	 *  This method gets from GUI the return book request of the user.
	 * @param event - event from press on the "Return Book" button.
	 * @throws IOException
	 */
	@FXML
	void ReturnBookScreen(ActionEvent event) throws IOException {
		clickednow=btnReturnBook;
		clerEffect(btnReturnBook);
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/ReturnBook.fxml"));
		rightPane.getChildren().setAll(pane);
	}

	/**
	 *  This method gets from GUI the search book request of the user.
	 * @param event - event from press on the "Search Book" button.
	 * @throws IOException
	 */
	@FXML
	void SearchBookScreen(ActionEvent event) throws IOException {
		clickednow=btnSearchBook;
		clerEffect(btnSearchBook);
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/LibrarianBookSearch.fxml"));
		rightPane.getChildren().setAll(pane);
	}

	/**
	 *  This method gets from GUI the search reader request of the user.
	 * @param event - event from press on the "Search Reader" button.
	 * @throws IOException
	 */
	@FXML
	void SearchReaderScreen(ActionEvent event) throws IOException {
		clickednow=btnSearchReader;
		clerEffect(btnSearchReader);
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/ReaderCard.fxml"));
		rightPane.getChildren().setAll(pane);
	}

	/**
	 *  This method gets from GUI the show report request of the user.
	 * @param event - event from press on the "Show Report" button.
	 * @throws IOException
	 */
	@FXML
	void ShowReportScreen(ActionEvent event) throws IOException {
		clickednow=btnShow_Report;
		clerEffect(btnShow_Report);
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticReports.fxml"));
		rightPane.getChildren().setAll(pane);
	}

	/**
	 *  This method gets from GUI the show Loan request of the user.
	 * @param event - event from press on the "Show Loan" button.
	 * @throws IOException
	 */
	@FXML
	void showLoanScreen(ActionEvent event) throws IOException {
		clickednow=btn_LoanBook;
		clerEffect(btn_LoanBook);
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/Loan.fxml"));
		rightPane.getChildren().setAll(pane);
	}

	/**
	 *  This method gets from GUI the extend loan request of the user.
	 * @param event - event from press on the "Extend Loan" button.
	 * @throws IOException
	 */
	@FXML
	void extendLoanScreen(ActionEvent event) throws IOException {
		clickednow=btnExtend;
		clerEffect(btnExtend);
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/LibrarianExtend.fxml"));
		rightPane.getChildren().setAll(pane);
	}

	/**
	 * This method call the init() method in the opening of this menu.
	 */
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
	* Not used method(must implement because the implementation of GuiInterface)
	*/
	@Override
	public void showSuccess(String string) {
		// TODO Auto-generated method stub

	}

	 /**
	 * Display method
	 * @param obj the object received by the server from the data base.
	 * The object is an array list with the values of the requested query.
	 */
	@Override
	public void display(Object obj) {
		OBLcontroller.librarianStage.close();
		Main.primary.show();
	}

	/**
	 * This method show an error pop-up on the screen with a given message.
	 * @param String - the message that will be shown in the pop-up.
	 */
	@Override
	public void showFailed(String message) {
		// TODO Auto-generated method stub

	}

	/**
	* This method clear the gui components.
	*/
	@Override
	public void freshStart() {
		// TODO Auto-generated method stub
	}
}
