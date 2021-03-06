package GUI;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Client.Client;
import Common.GuiInterface;
import Common.LoanDetails;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import logic.BookHandlerController;
import logic.CommonController;
import logic.LoanController;
import logic.Main;

/**
	* This class represents the process extend book by a librarian.
 */
public class LibrarianExtendGUI implements GuiInterface,Initializable{

	@FXML
	private TableColumn<LoanDetails, String> CopyIDCol;

	@FXML
	private TableColumn<LoanDetails, String> bookNameCol;

	@FXML
	private TableColumn<LoanDetails, String> authorNameCol;

	@FXML
	private TableColumn<LoanDetails, String> startLoanDateCol;

	@FXML
	private TableColumn<LoanDetails, String> endLoanDateCol;

	@FXML
	private TextField txtMember_ID;

	@FXML
	private TableView<LoanDetails> table;

	
	/**
	 * This method get from GUI the search request of the user by the "ENTER" key and move it to the CommonController
	 * for continue in the search process. (by pressing on ENTER).
	 * @param event - event from press on ENTER.
	 */
	@FXML
	void memberID_Enter(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER){
			try {
				CommonController.checkMemberExistence(txtMember_ID.getText());
			} catch (Exception e) {
				showFailed(e.getMessage());
				freshStart();
			}
		}
	}
	
	/**
	 * This method get from GUI the search request of the user by the "Magnifier" icon and move it to the CommonController
	 * for continue in the search process. (by pressing on the "Magnifier" icon).
	 * @param event - event from press on the "Magnifier" icon.
	 */
	@FXML
	void memberID_mouse(MouseEvent event) {
		try {
			CommonController.checkMemberExistence(txtMember_ID.getText());
		} catch (Exception e) {
			showFailed(e.getMessage());
			freshStart();
		}
	}

	/**
	 * This method set the variable of the GuiInterface in the client to this.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		Main.client.clientUI=this;	
		CommonController.setColumnWidth(CopyIDCol, 70, 70, 70);
		CommonController.setColumnWidth(startLoanDateCol, 160, 160, 160);
		CommonController.setColumnWidth(endLoanDateCol, 160, 160, 160);
	}

	/**
	 * This method show information pop-up on the screen with the given success message.
	 * @param string - the message that will be shown in the pop-up.
	 */
	@Override
	public void showSuccess(String string) {
		Platform.runLater(()->{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setHeaderText("An successful operation");
			alert.setContentText(string);
			alert.showAndWait();		
		});
	}

	/**
	 * shows the table view that contain all the books that are current on loans by specific user.
	 * @param obj- the data of all the loaned books.
	 */
	@Override
	public void display(Object obj) {
		ArrayList<String> msg = (ArrayList<String>)obj;
		switch(msg.get(0)) {
		case "CurrentLoans":
			ArrayList<String> currentLoans = (ArrayList<String>)obj;

			if (currentLoans.size() <= 2)
			{
				showFailed("You dont have books in loan currently");
				return;
			}

			ObservableList<LoanDetails> loanarray		= FXCollections.observableArrayList();
			int 				 		numberOfBook    = (currentLoans.size()-1)/5;
			int							i				= 0;
			int							j				= 0;

			table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			CopyIDCol.setCellValueFactory(new PropertyValueFactory<LoanDetails,String>("copyID"));
			bookNameCol.setCellValueFactory(new PropertyValueFactory<LoanDetails,String>("bookName"));
			authorNameCol.setCellValueFactory(new PropertyValueFactory<LoanDetails,String>("authorName"));
			startLoanDateCol.setCellValueFactory(new PropertyValueFactory<LoanDetails,String>("LoanDate"));
			endLoanDateCol.setCellValueFactory(new PropertyValueFactory<LoanDetails,String>("ActualReturnDate"));

			while(i<numberOfBook)
			{
				LoanDetails newLoan = new LoanDetails(currentLoans.get(j+2), currentLoans.get(j+3),currentLoans.get(j+1),currentLoans.get(j+4),currentLoans.get(j+5));
				loanarray.add(newLoan);
				i++;
				j+=5;
			}

			Platform.runLater(() -> {	
				table.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (table.getSelectionModel().getSelectedItem()==null)
							return;
						if (Client.arrayUser.size() > 2)
						{
							BookHandlerController.extendLoanPeriodByLibrarian(txtMember_ID.getText(),table.getSelectionModel().getSelectedItem().getCopyID(),table.getSelectionModel().getSelectedItem().getBookName(), table.getSelectionModel().getSelectedItem().getAuthorName(),Client.arrayUser.get(0));
						}
					}

				});
			});

			table.setItems(loanarray);
			break;

		case "Check Member Existence":
			if(msg.size() == 1) {
				showFailed("Member doesn't exist!");
				freshStart();
			}
			else {
				LoanController.getCurrentLoans(txtMember_ID.getText());
			}
			break;

		default:
			break;
		}
	}		


	/**
	 * this method show error pop-up on the screen with given message
	 * @param string- the message that shown in the pop-up.
	 */
	@Override
	public void showFailed(String message) {
		Platform.runLater(()->{
			Alert alert=new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(message);
			alert.showAndWait();	
		});
	}

	/**
	 * this method clean up the fields on the screen.
	 */
	@Override
	public void freshStart() {
		txtMember_ID.clear();
		table.setItems(null);
	}

}
