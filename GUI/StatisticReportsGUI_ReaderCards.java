package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Common.GuiInterface;
import Common.Member;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import logic.CommonController;
import logic.Main;

public class StatisticReportsGUI_ReaderCards implements GuiInterface,Initializable{

	    @FXML
	    private AnchorPane AnchorPane;
    
	    @FXML
	    private TableView<Member> TableViewMembers;

		@FXML
	    private TableColumn<Member,String> firstNameCol;

	    @FXML
	    private TableColumn<Member,String> lastNameCol;
	    
	    @FXML
	    private TableColumn<Member,String> memberIDCol;
	    
	    @FXML
	    private Label FirstNameLabel;

	    @FXML
	    private Label LastNameLabel;

	    @FXML
	    private Label MemberIDLabel;

	    @FXML
	    private Label EmailLabel;

	    @FXML
	    private Label StatusLabel;

	    @FXML
	    private Label PhoneNumberLabel;
	    
	    @FXML
	    private TextArea textareaNotes;
	    
	    @FXML
	    private Button btn_historyOfLoan;

	    @FXML
	    private Button btn_latesLostBook;
	    
	    @FXML
	    void historyOfLoan(ActionEvent event) throws IOException {
	    	MemberCardGUI.memberIDHistory = MemberIDLabel.getText(); //MemberIDLabel.setText(memberTemp.getId());;
	    	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/HistoryOfLoanTableView.fxml"));
	    	AnchorPane.getChildren().setAll(pane);
	    }

	    @FXML
	    void latesLostBook(ActionEvent event) {

	    }
	    
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			// TODO Auto-generated method stub
			Main.client.clientUI=this;
			
			
			TableViewMembers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //resize the columns to the table view
			TableColumn<Member,String> firstNameCol = new TableColumn<Member,String>("First Name");//attach the fxml column to the property of the column
			TableColumn<Member,String> lastNameCol = new TableColumn<Member,String>("Last Name");
			TableColumn<Member,String> memberIDCol = new TableColumn<Member,String>("Member ID");
			
			firstNameCol.setSortType(TableColumn.SortType.DESCENDING);//set sort type of the column to DESCENDING
			lastNameCol.setSortType(TableColumn.SortType.DESCENDING);
			memberIDCol.setSortType(TableColumn.SortType.DESCENDING);
			
			TableViewMembers.getColumns().setAll(firstNameCol, lastNameCol,memberIDCol);//attach the columns to the table view (personTable)
			
			
			getMembers();
			
			firstNameCol.setCellValueFactory(new PropertyValueFactory<Member,String>("firstName"));//set column property to the fxml column
			lastNameCol.setCellValueFactory(new PropertyValueFactory<Member,String>("lastName"));
			memberIDCol.setCellValueFactory(new PropertyValueFactory<Member,String>("id"));
			
			///////// row listener - when select row
			TableViewMembers.getSelectionModel().selectedIndexProperty().addListener(new RowSelectListener());
			
		}
		
			///////// row select listener
		 private class RowSelectListener implements ChangeListener {
					@Override
					public void changed(ObservableValue arg0, Object arg1, Object arg2) {
						// TODO Auto-generated method stub		
						Member memberTemp = TableViewMembers.getSelectionModel().getSelectedItem();//put in Person object the selected item in the table view
						FirstNameLabel.setText(memberTemp.getFirstName());
						LastNameLabel.setText(memberTemp.getLastName());
						MemberIDLabel.setText(memberTemp.getId());
						EmailLabel.setText(memberTemp.getEmail());
						StatusLabel.setText(memberTemp.getStatus());
						PhoneNumberLabel.setText(memberTemp.getPhoneNumber());
						textareaNotes.setText(memberTemp.getNotes());
					}

			     }

		@Override
		public void showSuccess(String string) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void display(Object obj) {
			// TODO Auto-generated method stub
			
			ArrayList<String> list = (ArrayList<String>)obj;
			int numOfMember = (list.size()-1)/7;
			int i=0,j=1;
			 ArrayList<Member> memberListTemp = new ArrayList<>();
			 Member memberTemp=null;
			 while(i<numOfMember) {
				 memberTemp = new Member(list.get(j), list.get(j+1), list.get(j+2), null, list.get(j+3), list.get(j+4), list.get(j+5), list.get(j+6), null, null, null, null);
				 i++;
				 j += 7;
				 memberListTemp.add(memberTemp);
			 }
			 ObservableList<Member> memberList = FXCollections.observableArrayList(memberListTemp);//create ObservableList and attach to the person's created in getTeamMembers function
			 TableViewMembers.setItems(memberList);//set ObservableList to the table view (personTable)
			 
		}

		@Override
		public void showFailed(String message) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void freshStart() {
			// TODO Auto-generated method stub
			
		}
		
		 private void getMembers() {
			 CommonController.ShowReaderCards();
		  }
	    
	    
    
    
}
