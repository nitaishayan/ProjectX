package GUI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Client.Client;
import Common.GuiInterface;
import javafx.application.Platform;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;
import logic.BookHandlerController;
import logic.CommonController;
import logic.InventoryController;
import logic.Main;

public class InventoryAddGUI implements GuiInterface,Initializable{
	public static String Location;
	public static String bookname;
	public static String bookid;
	public static File PDF;

	@FXML
	private AnchorPane MainPane;

	@FXML
	private TextField txtBook_Name;

	@FXML
	private TextField txtEdition;

	@FXML
	private TextField txtTheme;

	@FXML
	private TextField txtAuthor;

	@FXML
	private DatePicker txtPrint_Date;

	@FXML
	private TextField txtCopies;

	@FXML
	private TextField txtShelf_Location;

	@FXML
	private TextArea txtDescription;

	@FXML
	private TextField txtTable_Of_Content;

	@FXML
	private TextField txtCatlog_Number;

	@FXML
	private Button btnAdd;

	@FXML
	private Button btnCopy;

	@FXML
	private DatePicker txtPurchase_Date;

	@FXML
	private TextField txtCopy_Location;

	@FXML
	private Button btnCopy_Location_Confirm;

	@FXML
	private CheckBox CHBOX_YES;

	@FXML
	private CheckBox CHBOX_NO;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	String wanted;
	public static String nextBookID;

	@FXML
	public void pdf(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		PDF=fileChooser.showOpenDialog(OBLcontroller.librarianStage);
		txtTable_Of_Content.setText(PDF.getName());
	}

	@FXML
	void WANTED_YES(ActionEvent event) {
		CHBOX_NO.setSelected(false);
		wanted="true";
	}

	@FXML
	void WANTED_NO(ActionEvent event) {
		CHBOX_YES.setSelected(false);
		wanted="false";
	}

	@FXML
	void AddCopy(ActionEvent event) {
		InventoryController.addCopy(Location,bookname,bookid);
	}

	@FXML
	void AddBook(ActionEvent event) {
		if (checkfields())
			showFailed("Fill all the dields");
		else {
			InventoryController.addBook(txtBook_Name.getText(), txtEdition.getText(), txtTheme.getText(), txtAuthor.getText(), txtPrint_Date.getValue().toString(),txtCopies.getText(),txtPurchase_Date.getValue().toString(),txtShelf_Location.getText(),wanted,txtDescription.getText());
			BookHandlerController.sendPdf(PDF, nextBookID);
		}
	}

	public void Enablefields(boolean status) {
		txtEdition.setDisable(status);
		txtTheme.setDisable(status);
		txtPrint_Date.setDisable(status);
		txtPurchase_Date.setDisable(status);
		txtShelf_Location.setDisable(status);
		txtDescription.setDisable(status);
		txtTable_Of_Content.setDisable(status);
		btnAdd.setDisable(status);
		CHBOX_NO.setDisable(status);
		CHBOX_YES.setDisable(status);
	}

	@FXML
	void BackToInventory(ActionEvent event) throws IOException {
		AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/Inventory.fxml"));
		MainPane.getChildren().setAll(pane);
	}

	@FXML
	void CheckExistense(ActionEvent event) {
		if (txtBook_Name.getText().isEmpty()||txtAuthor.getText().isEmpty()) {
			showFailed("fill the missing fields.");
			txtBook_Name.setEditable(true);
			txtAuthor.setEditable(true);
		}
		else{
			ArrayList<String> msg=new ArrayList<>();
			msg.add(txtBook_Name.getText());
			msg.add(txtAuthor.getText());
			InventoryController.checkExistence((ArrayList<String>) msg);
			Enablefields(false);
			btnCopy.setDisable(true);
		}
	}

	public boolean checkfields() {
		if ((txtEdition.getText().isEmpty()))
			return true;
		if ((txtTheme.getText().isEmpty()))
			return true;
		if (txtPrint_Date.getValue()==null)
			return true;
		if (txtPurchase_Date.getValue()==null)
			return true;
		if ((txtShelf_Location.getText().isEmpty()))
			return true;
		if ((txtDescription.getText().isEmpty()))
			return true;
		if ((CHBOX_NO.isSelected()==false&&CHBOX_YES.isSelected()==false))
			return true;
		//		if ((btnAdd.getText().isEmpty()))
		//			return true;
		return false;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.client.clientUI=this;		
	}

	@Override
	public void showSuccess(String string) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Confirm");
		alert.setHeaderText(string);
		alert.showAndWait();
		if (PDF==null)
			System.out.println("pdf problem");	
		else BookHandlerController.sendPdf(PDF, nextBookID);
	}

	@Override
	public void display(Object obj) {
		Platform.runLater(()->{
			ArrayList<String> msg = (ArrayList<String>)obj;
			ArrayList<Integer> datearray=new ArrayList<>();
			datearray=CommonController.convertordate(msg.get(7));
			LocalDate date=LocalDate.of(datearray.get(0), datearray.get(2), datearray.get(1));
			this.txtPrint_Date.setValue(date);
			datearray=CommonController.convertordate(msg.get(10));
			date=LocalDate.of(datearray.get(0), datearray.get(2), datearray.get(1));
			this.txtPurchase_Date.setValue(date);
			this.txtCatlog_Number.setText(msg.get(1));
			bookid=msg.get(1);
			bookname=msg.get(2);
			Location=msg.get(12);
			this.txtCopies.setText(msg.get(3));
			wanted=(msg.get(4));
			if (wanted.equals("true")) {
				CHBOX_YES.setSelected(true);
			}
			else CHBOX_NO.setSelected(true);
			this.txtEdition.setText(msg.get(6));
			this.txtTheme.setText(msg.get(8));
			this.txtDescription.setText(msg.get(9));
			this.txtShelf_Location.setText(msg.get(12));
			Enablefields(true);
			txtBook_Name.setDisable(true);
			txtAuthor.setDisable(true);
			btnCopy.setDisable(false);
			btnAdd.setDisable(true);
		});
	}

	@Override
	public void showFailed(String string) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Message");
		alert.setHeaderText(string);
		alert.showAndWait();
		txtBook_Name.setEditable(false);
		txtAuthor.setEditable(false);
	}

	@Override
	public void freshStart() {
		this.txtCopies.clear();
		CHBOX_NO.setSelected(false);
		CHBOX_YES.setSelected(false);
		this.txtEdition.clear();
		this.txtPrint_Date.setValue(null);
		this.txtTheme.clear();
		this.txtDescription.clear();
		this.txtPurchase_Date.setValue(null);//purchasedate.fromString(msg.get(10)));
		this.txtShelf_Location.clear();
		this.txtCatlog_Number.clear();
		txtAuthor.setEditable(true);
		txtBook_Name.setEditable(true);
		Enablefields(true);

	}
}
