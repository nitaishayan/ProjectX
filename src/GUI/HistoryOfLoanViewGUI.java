package GUI;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import Common.GuiInterface;
import Common.LoanDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.CommonController;
import logic.Main;

public class HistoryOfLoanViewGUI implements Initializable,GuiInterface{

    @FXML
    private TableView<LoanDetails> TableViewLoanHistory;

    @FXML
    private TableColumn<LoanDetails, String> BookName;

    @FXML
    private TableColumn<LoanDetails, String> CopyID;
    @FXML
    private TableColumn<LoanDetails, String> ActualReturnDate;
    @FXML
    private TableColumn<LoanDetails, String> LoanDate;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label memberDetails;
	Stage window;
	VBox vBox;
	String memberID;	
	@Override
	public void showSuccess(String string) {
		Alert alert=new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(string);
		alert.showAndWait();			
	}
	@Override
	public void display(Object obj) {

		int numberOfColumns=4;
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
				LoanDate = new TableColumn<>("Loan Date");
				ActualReturnDate = new TableColumn<>("Actual return date");

				//set up size
				BookName.setMinWidth(200);
				CopyID.setMinWidth(200);
				LoanDate.setMinWidth(200);
				ActualReturnDate.setMinWidth(200);
				//set up order descending
				BookName.setSortType(TableColumn.SortType.DESCENDING);
				CopyID.setSortType(TableColumn.SortType.DESCENDING);
				LoanDate.setSortType(TableColumn.SortType.DESCENDING);
				ActualReturnDate.setSortType(TableColumn.SortType.DESCENDING);
				//Set upSet property
				TableViewLoanHistory.getColumns().setAll(BookName,CopyID,LoanDate,ActualReturnDate);//attach the columns to the table view (personTable)
				BookName.setCellValueFactory(new PropertyValueFactory<LoanDetails,String>("bookName"));
				CopyID.setCellValueFactory(new PropertyValueFactory<LoanDetails,String>("copyID"));
				LoanDate.setCellValueFactory(new PropertyValueFactory<LoanDetails,String>("LoanDate"));
				ActualReturnDate.setCellValueFactory(new PropertyValueFactory<LoanDetails,String>("ActualReturnDate"));

				
				ObservableList<LoanDetails> loanDetails=FXCollections.observableArrayList();
				int loanRowSize = (loanList.size()-nonRelevantString)/numberOfColumns;
				int rowCounter=0, arrayJump=1;
				ArrayList<LoanDetails> list2 = null;
				 LoanDetails loanTemp;
				 while(rowCounter<loanRowSize) {
					 if (loanList.get(arrayJump+3)==null) {
						 loanTemp = new LoanDetails(loanList.get(arrayJump+2), loanList.get(arrayJump), loanList.get(arrayJump+1),"The book is still borrowed");//create a new object by LoanDetails
					}
					 else
					 {
						 loanTemp = new LoanDetails(loanList.get(arrayJump+2), loanList.get(arrayJump), loanList.get(arrayJump+1),loanList.get(arrayJump+3));//create a new object by LoanDetails
						 TableViewLoanHistory.getRowFactory();
						 
					 }
					 //j+2 Book name ; //j CopyID ; //J+1 Loan Date
					 rowCounter++;
					 arrayJump+=4;
					 loanDetails.add(loanTemp);
					 
				 }
				 TableViewLoanHistory.setItems(loanDetails);
			}			
		}



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
		// TODO Auto-generated method stub
		
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.client.clientUI=this;
		if (MemberCardGUI.memberIDHistory!=null) {
			memberID=MemberCardGUI.memberIDHistory;
			memberDetails.setText(MemberCardGUI.memberFirstName+" "+MemberCardGUI.memberLastName);
			MemberCardGUI.memberIDHistory=null;
			MemberCardGUI.memberFirstName=null;
			MemberCardGUI.memberLastName=null;
		}
		else {
			memberID=Main.client.arrayUser.get(0);//get ID by arrayUser
			memberDetails.setText(Main.client.arrayUser.get(2)+" "+Main.client.arrayUser.get(3));
		}
		

		
		//load data into tableView
		CommonController.viewPersonalHistory(memberID);
		//row listener - when we receive row from DB
		TableViewLoanHistory.getSelectionModel().selectedIndexProperty().addListener(new RowSelectListener());
		}
//		 private void getHistoryOfLoans() {
//			 TableViewLoanHistory.setItems(getLoanDetails());
//	}
		private class RowSelectListener implements ChangeListener {
				@Override
				public void changed(ObservableValue arg0, Object arg1, Object arg2) {
					// TODO Auto-generated method stub		
					LoanDetails loanDetailsTemp = TableViewLoanHistory.getSelectionModel().getSelectedItem();
					}

		     	}
		
}