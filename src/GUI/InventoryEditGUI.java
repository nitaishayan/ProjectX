package GUI;



import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Common.GuiInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.converter.LocalDateStringConverter;
import logic.BookHandlerController;
import logic.CommonController;
import logic.InventoryController;
import logic.Main;

/**
 * This class edit an existing book in the library.
 */
public class InventoryEditGUI implements Initializable,GuiInterface {
	public static File PDF;

	@FXML
	private AnchorPane MainPane;

	@FXML
	private Button btnSave;

	@FXML
	private TextField txtBook_Name;

	@FXML
	private TextField txtEdition;

	@FXML
	private TextField txtTheme;

	@FXML
	private TextField txtAuthors;

	@FXML
	private TextField txtCopies;

	@FXML
	private TextField txtLocation;

	@FXML
	private TextArea txtDescription;

	@FXML
	private DatePicker txtPurchase_Date;

	@FXML
	private DatePicker txtPrint_date;

	@FXML
	private RadioButton rdioBook_ID;

	@FXML
	private ToggleGroup choice;

	@FXML
	private RadioButton rdioBook_Name;

	@FXML
	private TextField txtBook_ID;

	@FXML
	private TextField txtPdf;
	
	@FXML
    private CheckBox CHBOX_YES;

    @FXML
    private CheckBox CHBOX_NO;
    
    @FXML
    private Label BookID_must;
    
    @FXML
    private Label BookName_must;

    @FXML
    private Label Author_must;
    
    @FXML
    private ImageView AuthorButton;

    @FXML
    private ImageView IDButton;
    
    @FXML
    private Button btn_browse;
	
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
	 * This method open the "File Chooser" to choose the specific pdf file we want to upload and set the name of the pdf in the table of contents field.
	 * @param event - event from press on "..."(Browse) button.
	 */
	@FXML
	public void pdf(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		PDF=fileChooser.showOpenDialog(OBLcontroller.librarianStage);
		if (PDF!=null)
			txtPdf.setText(PDF.getName());
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
	 * @param event - event from press on "BookID" RadioButton - when the search is by book id.
	 */
	@FXML
	void book_ID(ActionEvent event) {
		freshStart();
		txtBook_ID.setDisable(false);
		BookID_must.setVisible(true);
		BookName_must.setVisible(false);
		Author_must.setVisible(false);
		AuthorButton.setVisible(false);
		IDButton.setVisible(true);
	}

	/**
	 * This method enable/disable the fields.
	 * @param event - event from press on "BookName" RadioButton - when the search is by book name.
	 */
	@FXML
	void book_name(ActionEvent event) {
		freshStart();
		txtBook_Name.setDisable(false);
		txtAuthors.setDisable(false);
		BookID_must.setVisible(false);
		BookName_must.setVisible(true);
		Author_must.setVisible(true);
		IDButton.setVisible(false);
		AuthorButton.setVisible(true);

	}
	
	/**
	 * This method get the data of the chosen book and call the function checkExistence() from the InventoryController that send to the server.
	 * @param event -  event from press on "Magnifier" Icon - when the search is by book id.
	 */
	 @FXML
	    void bookIdMouse(MouseEvent event) {
		 if (txtBook_ID.getText().isEmpty()) {
				showFailed("fill book ID.");
			}
			else {
				ArrayList<String> msg=new ArrayList<>();
				msg.add(txtBook_ID.getText());
				InventoryController.checkExistence((ArrayList<String>) msg);
			}
	    }

	 /**
		 * This method get the data of the chosen book and call the function checkExistence() from the InventoryController that send to the server.
		 * @param event -  event from press on "Magnifier" Icon - when the search is by book name.
		 */
	    @FXML
	    void bookNameMouse(MouseEvent event) {
	    	if (txtBook_Name.getText().isEmpty()||txtAuthors.getText().isEmpty()) {
				showFailed("fill book.");
			}
			else {
				ArrayList<String> msg=new ArrayList<>();
				msg.add(txtBook_Name.getText());
				msg.add(txtAuthors.getText());
				InventoryController.checkExistence((ArrayList<String>) msg);
			}
	    }

	    /**
		 * This method checks if the book (by book name) requested exists in the library by pressing "ENTER" after filling the book name and author fields.
		 * @param event - event from click on ENTER on the keyboard.
		 */
	@FXML
	void EnterBook_Name(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER){
			if (txtBook_Name.getText().isEmpty()||txtAuthors.getText().isEmpty()) {
				showFailed("fill book.");
			}
			else {
				ArrayList<String> msg=new ArrayList<>();
				msg.add(txtBook_Name.getText());
				msg.add(txtAuthors.getText());
				InventoryController.checkExistence((ArrayList<String>) msg);
			}
		}
	}

	/**
	 * This method checks if the book (by book name) requested exists in the library by pressing "ENTER" after filling the book name and author fields.
	 * @param event - event from click on ENTER on the keyboard.
	 */
	@FXML
	void Enter_BookID(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER){
			if (txtBook_ID.getText().isEmpty()) {
				showFailed("fill book ID.");
			}
			else {
				ArrayList<String> msg=new ArrayList<>();
				msg.add(txtBook_ID.getText());
				InventoryController.checkExistence((ArrayList<String>) msg);
			}
		}
	}

	/**
	 * This method send to editCopy() function in InventoryController the data receive from the screen for further sending to the server.
	 * @param event - event from click on "Save" button.
	 */
	@FXML
	void Save(ActionEvent event) {
		if (checkfields())
			showFailed("Check for empty fields.");
		else {
			InventoryController.editCopy(txtBook_Name.getText(),
										txtEdition.getText(),
										txtTheme.getText(),
										txtPdf.getText(),
										txtAuthors.getText(),
										txtLocation.getText(),
										txtDescription.getText(),
										wanted,
										txtPurchase_Date.getValue().toString(),
										txtPrint_date.getValue().toString(),
										txtBook_ID.getText());
		}
	}

	/**
	 * This method checks if the one of the fields are empty.
	 * @return return a boolean value if one field is empty or not.
	 */
	public boolean checkfields() {
		if ((txtPdf.getText().isEmpty()))
			return true;
		if ((txtBook_Name.getText().isEmpty()))
			return true;
		if ((txtAuthors.getText().isEmpty()))
			return true;
		if ((txtEdition.getText().isEmpty()))
			return true;
		if ((txtTheme.getText().isEmpty()))
			return true;
		if (txtPrint_date.getValue()==null)
			return true;
		if (txtPurchase_Date.getValue()==null)
			return true;
		if ((txtLocation.getText().isEmpty()))
			return true;
		if ((txtDescription.getText().isEmpty()))
			return true;
		if ((CHBOX_NO.isSelected()==false&&CHBOX_YES.isSelected()==false))
			return true;
		return false;
	}

	/**
	 * This method show information pop-up on the screen with the given success message.
	 * @param string - the message that will be shown in the pop-up.
	 */
	@Override
	public void showSuccess(String string) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(string);
		alert.showAndWait();
		rdioBook_Name.setSelected(false);
		rdioBook_ID.setSelected(false);
		if (string.equals("details updated successfully in the system.")) {
			if (PDF==null)
				System.out.println("pdf problem");	
			else {BookHandlerController.sendPdf(PDF, txtBook_ID.getText());
			System.out.println("Sent");
			}
		}
	}

	/**
	 * This method enable/disable the fields.
	 * @param choice - a boolean variable that set enable/disable to the corresponding components.
	 */
	public void Disable(boolean choice) {
		txtEdition.setDisable(choice);
		txtTheme.setDisable(choice);
		txtCopies.setDisable(choice);
		txtLocation.setDisable(choice);
		CHBOX_NO.setDisable(choice);
		CHBOX_YES.setDisable(choice);
		txtPrint_date.setDisable(choice);
		txtPurchase_Date.setDisable(choice);
		txtDescription.setDisable(choice);
		txtBook_ID.setDisable(choice);
		txtAuthors.setDisable(choice);
		txtBook_Name.setDisable(choice);
	}

	/**
	 * This method update the inventory-edit screen with the data that return.
	 * The method give the user the functionality to edit a copy of the specific book in the inventory.
	 * @param obj - ArrayList with the relevant data for create this window (all the information that's needed).
	 */
	@Override
	public void display(Object obj) {
		Disable(false);
		txtBook_ID.setDisable(true);
		txtBook_Name.setDisable(true);
		txtAuthors.setDisable(true);
		ArrayList<String> details=(ArrayList<String>)obj;
		ArrayList<Integer> datearray=new ArrayList<>();
		datearray=CommonController.convertordate(details.get(7));
		LocalDate date=LocalDate.of(datearray.get(0), datearray.get(2), datearray.get(1));
		txtPrint_date.setValue(date);
		datearray=CommonController.convertordate(details.get(10));
		date=LocalDate.of(datearray.get(0), datearray.get(2), datearray.get(1));
		txtPurchase_Date.setValue(date);
		txtBook_ID.setText(details.get(1));
		txtEdition.setText(details.get(6));
		txtTheme.setText(details.get(8));
		txtCopies.setText(details.get(3));
		txtCopies.setDisable(true);
		txtLocation.setText(details.get(12));
		wanted=details.get(4);
		if (wanted.equals("true")) {
			CHBOX_YES.setSelected(true);
		}
		else CHBOX_NO.setSelected(true);
		txtDescription.setText(details.get(9));
		txtPdf.setText(details.get(11));
		btnSave.setDisable(false);
		txtAuthors.setText(details.get(5));
		txtBook_Name.setText(details.get(2));
		BookID_must.setVisible(false);
		BookName_must.setVisible(false);
		Author_must.setVisible(false);
		btn_browse.setDisable(false);
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
	}

	/**
	 * This method clean up the fields on the screen.
	 */
	@Override
	public void freshStart() {
		Disable(true);
		txtBook_ID.clear();
		txtEdition.clear();
		txtTheme.clear();
		txtCopies.clear();
		txtLocation.clear();
		txtPdf.clear();
		CHBOX_NO.setSelected(false);
		CHBOX_YES.setSelected(false);
		txtPrint_date.setValue(null);
		txtPurchase_Date.setValue(null);
		txtDescription.clear();
		txtAuthors.clear();
		txtBook_Name.clear();
		BookID_must.setVisible(false);
		BookName_must.setVisible(false);
		Author_must.setVisible(false);
		AuthorButton.setVisible(false);
		IDButton.setVisible(false);
		btn_browse.setDisable(true);

	}

	/**
	 * This method set the variable of the GuiInterface in the client to this.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.client.clientUI=this;		
		txtPdf.setEditable(false);
	}

}

