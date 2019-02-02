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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import logic.BookHandlerController;
import logic.CommonController;
import logic.LoanController;
import logic.Main;

public class MemberExtendLoanPeriodGUI implements Initializable, GuiInterface {

	@FXML
	private AnchorPane root;

	@FXML
	private Label titelLabel;

	@FXML
	private TableView<LoanDetails> table;

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

	@Override
	public void showSuccess(String message) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setHeaderText("An successful operation");
			alert.setContentText(message);
			alert.showAndWait();
	}

	@Override
	public void display(Object obj) {

		ArrayList<String> 			currentLoans 	= (ArrayList<String>)obj;

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
		
		CommonController.setColumnWidth(CopyIDCol, 70, 80, 90);

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
						BookHandlerController.extendLoanPeriodByMember(Client.arrayUser.get(0),table.getSelectionModel().getSelectedItem().getCopyID(),table.getSelectionModel().getSelectedItem().getBookName(), table.getSelectionModel().getSelectedItem().getAuthorName());
					}
				}

			});
		});

		table.setItems(loanarray);
	}

	@Override
	public void showFailed(String message) {
		Alert alert=new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(message);
		alert.showAndWait();
	}

	@Override
	public void freshStart() {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.client.clientUI=this;
		System.out.println("before get loans");
		LoanController.getCurrentLoans(Client.arrayUser.get(0));
		System.out.println("after get loans");
	}
}