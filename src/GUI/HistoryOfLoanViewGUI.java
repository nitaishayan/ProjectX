package GUI;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;
import Client.Client;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import Common.GuiInterface;
import Common.LoanDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.BookHandlerController;
import logic.CommonController;
import logic.Main;
import logic.MemberCardController;
import logic.SearchBookController;
/**
 * Class that present the data of all the loan book that the specific member 
 * All books that the user held during the period in which he or she member in the library
 * during his time in the library
 * the presentation of the details is shown by a table view with a fxml page that call HistoryofLoanTableView.fxml
 * if the librarian press a row in the table view she can declare a copy as lost by the member
 * @author nitay shayan
 *
 */
public class HistoryOfLoanViewGUI implements Initializable,GuiInterface{
	Stage window;
	VBox vBox;
	String memberID;	
	String status;
	@FXML
	private TableView<LoanDetails> TableViewLoanHistory;

	@FXML
	private TableColumn<LoanDetails,String> BookName;

	@FXML
	private TableColumn<LoanDetails,String> CopyID;

	@FXML
	private TableColumn<LoanDetails,String> AuthorName;

	@FXML
	private TableColumn<LoanDetails,String> LoanDate;

	@FXML
	private TableColumn<LoanDetails,String> ActualReturnDate;
	@FXML
	private AnchorPane rootPane;
	@FXML
	private Label memberDetails;
/**
 * show success message based on the String
 */
	@Override
	public void showSuccess(String string) {
		Alert alert=new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(string);
		alert.showAndWait();			
	}
	/**
	 * Display method
	 * @param obj the object received by the server from the data base
	 * the object is an array list with the values of the requested query
	 * the order of the arraylist is below
	 * j+2 Book name ; //j CopyID ; //J+1 Loan Date; //J+3 Actual Return Date; //J+4 Author name
	 * j is the current counter for each iteration
	 * if the member did not loan any books, a proper message is appeard
	 * @param numberOfColumns number of columns in the table view
	 */
	@Override
	public void display(Object obj) {

		int numberOfColumns=5;
		int nonRelevantString=1;
		ArrayList<String> loanList = (ArrayList<String>)obj;
		System.out.println(loanList.toString()+"in historyGUI");
		if (loanList.get(0).equals("SearchMember")) {
			showSuccess("The member "+memberDetails.getText()+" details updated successfully");
		}
		else {
			if (loanList.get(1).equals("NotExist")) {
				showFailed("The member "+memberDetails.getText()+ " did not loan any book yet. we are waiting!");
			}
			else {

				TableViewLoanHistory.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //resize the columns to the table view
				//set up the columns
				BookName = new TableColumn<>("Book name");
				CopyID = new TableColumn<>("Copy ID");
				AuthorName = new TableColumn<>("Author name");
				LoanDate = new TableColumn<>("Loan Date");
				ActualReturnDate = new TableColumn<>("Actual return date");
				CommonController.setColumnWidth(CopyID,80, 80, 80);
				CommonController.setColumnWidth(BookName,290, 290, 290);

				//set up order descending
				BookName.setSortType(TableColumn.SortType.DESCENDING);
				CopyID.setSortType(TableColumn.SortType.DESCENDING);
				LoanDate.setSortType(TableColumn.SortType.DESCENDING);
				ActualReturnDate.setSortType(TableColumn.SortType.DESCENDING);
				AuthorName.setSortType(TableColumn.SortType.DESCENDING);

				//Set upSet property
				TableViewLoanHistory.getColumns().setAll(BookName,CopyID,AuthorName,LoanDate,ActualReturnDate);//attach the columns to the table view (personTable)
				BookName.setCellValueFactory(new PropertyValueFactory<LoanDetails,String>("bookName"));
				CopyID.setCellValueFactory(new PropertyValueFactory<LoanDetails,String>("copyID"));
				LoanDate.setCellValueFactory(new PropertyValueFactory<LoanDetails,String>("LoanDate"));
				ActualReturnDate.setCellValueFactory(new PropertyValueFactory<LoanDetails,String>("ActualReturnDate"));
				AuthorName.setCellValueFactory(new PropertyValueFactory<LoanDetails,String>("AuthorName"));

				/**
				 * if the librarian press a row in the able view - she is able to declare the requested copy as a lost copy
				 * first it checks if the copy is borrowed, and pop up screen to the option 
				*/
				Platform.runLater(() -> {	
					TableViewLoanHistory.setOnMouseClicked(new EventHandler<MouseEvent>() {
						
						@Override
						public void handle(MouseEvent event) {
							if (TableViewLoanHistory.getSelectionModel().getSelectedItem()==null)
								return;
							if (!TableViewLoanHistory.getSelectionModel().getSelectedItem().getActualReturnDate().equals("The book is borrowed")) {
								showFailed("The book is not loaned at the moment.");
								return;
							}
							if (Client.arrayUser.size() != 6)
							displayLoanDetails(memberID,TableViewLoanHistory.getSelectionModel().getSelectedItem().getCopyID(),TableViewLoanHistory.getSelectionModel().getSelectedItem().getBookName(),TableViewLoanHistory.getSelectionModel().getSelectedItem().getAuthorName());						}
					});
				});


				ObservableList<LoanDetails> loanDetails=FXCollections.observableArrayList();
				int loanRowSize = (loanList.size()-nonRelevantString)/numberOfColumns;
				int rowCounter=0, arrayJump=1;
				ArrayList<LoanDetails> list2 = null;
				LoanDetails loanTemp;
				while(rowCounter<loanRowSize) {
					if (loanList.get(arrayJump+3)==null) {
						loanTemp = new LoanDetails(loanList.get(arrayJump+2), loanList.get(arrayJump+4), loanList.get(arrayJump),loanList.get(arrayJump+1),"The book is borrowed");//create a new object by LoanDetails					
					}
					else
					{
						loanTemp = new LoanDetails(loanList.get(arrayJump+2), loanList.get(arrayJump+4), loanList.get(arrayJump),loanList.get(arrayJump+1),loanList.get(arrayJump+3));//create a new object by LoanDetails					
					}
					//j+2 Book name ; //j CopyID ; //J+1 Loan Date; //J+3 Actual Return Date; //J+4 Author name
					rowCounter++;
					arrayJump+=5;//jump each iteration
					loanDetails.add(loanTemp);

				}
				TableViewLoanHistory.setItems(loanDetails);
			}			
		}
	}
/**
 * 
 * @param memberID memberID by user
 * @param copyid	copyID of the lost book
 * @param bookname	book name of the lost book
 */
	public void displayLoanDetails(String memberID, String copyid, String bookname,String author) {
		Stage 	   	 primaryStage   = new Stage();
		VBox 	 	 mainVbox       = new VBox(20);
		Label 		 ans  			= new Label();
		Label		 detailes		= new Label();
		Scene 		 scene 			= new Scene(mainVbox);
		Button		 LostCopy = new Button("LOST COPY");
		HBox 		 hbox2			= new HBox(20);
/**
 * send to the data base the lost copy details after the press
 */
		LostCopy.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				BookHandlerController.returnBook(copyid, status,memberID);
				BookHandlerController.memberLostBook(memberID, copyid, bookname);
			}
		});

		primaryStage.initModality(Modality.APPLICATION_MODAL);
		mainVbox.setMinHeight(390);
		mainVbox.setMinWidth(580);
		mainVbox.setMaxHeight(390);
		mainVbox.setMaxWidth(580);
		detailes.setText("Press 'LOST COPY' to declare the copy as lost.\n"+"Book name: "+bookname+"\n Author: "+author+" \nCopy id: "+copyid);
		detailes.setFont(new Font("Ariel", 18));
		mainVbox.getChildren().add(detailes);
		mainVbox.setAlignment(Pos.CENTER);
		mainVbox.getChildren().add(LostCopy);
		primaryStage.setTitle("Lost book");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.showAndWait();
	}
	/**
	 * show failed message based on the String
	 */
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
	/**
	 * initialize method that launch page as he open from another screen
	 * save the details of the member from MemberCardGUI
	 * 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.client.clientUI=this;
		if (MemberCardGUI.memberIDHistory!=null) {
			memberID=MemberCardGUI.memberIDHistory;
			status=MemberCardGUI.status;
			memberDetails.setText(MemberCardGUI.memberFirstName+" "+MemberCardGUI.memberLastName);
			MemberCardGUI.memberIDHistory=null;
			MemberCardGUI.memberFirstName=null;
			MemberCardGUI.memberLastName=null;
			MemberCardGUI.status=null;
		}
		else {
			memberID=Main.client.arrayUser.get(0);//get ID by arrayUser
			memberDetails.setText(Main.client.arrayUser.get(2)+" "+Main.client.arrayUser.get(3));
		}
		/**
		 * launch the query to DB
		 */
		MemberCardController.viewPersonalHistory(memberID);
		//row listener - when we receive row from DB
		TableViewLoanHistory.getSelectionModel().selectedIndexProperty().addListener(new RowSelectListener());
	}
	private class RowSelectListener implements ChangeListener {
		@Override
		public void changed(ObservableValue arg0, Object arg1, Object arg2) {
			// TODO Auto-generated method stub		
			LoanDetails loanDetailsTemp = TableViewLoanHistory.getSelectionModel().getSelectedItem();
		}
		
	}
	
}