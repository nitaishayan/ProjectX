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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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
	    
	    /**
	     * This method is triggered after choosing a reader card from the table view and pressing the button "history Of Loan".
	     * The method open a new window with the appropriate loan's history details.
	     * @param event - event cause from pressing on "history Of Loan" button.
	     * @throws IOException
	     */
	    @FXML
	    void historyOfLoan(ActionEvent event) throws IOException {
	    	//Load page of loan history
	    	MemberCardGUI.memberIDHistory=MemberIDLabel.getText();
	    	MemberCardGUI.memberFirstName=FirstNameLabel.getText();
	    	MemberCardGUI.memberLastName=LastNameLabel.getText();
	    	MemberCardGUI.status=StatusLabel.getText();
			Parent parent=FXMLLoader.load(getClass().getResource("/GUI/HistoryOfLoanTableView.fxml"));
			Scene scene=new Scene(parent);
			Stage stage=new Stage();
			stage.setScene(scene);
			stage.setMaxHeight(628);
			stage.setMinHeight(628);
			stage.setMinWidth(916);
			stage.setMaxWidth(916);
			stage.show();
	    }

	    /**
	     * This method is triggered after choosing a reader card from the table view and pressing the button "late/Lost Book".
	     * The method open a new window with the appropriate late/lost's history details.
	     * @param event - event cause from pressing on "late/Lost Book" button.
	     * @throws IOException
	     */
	    @FXML
	    void latesLostBook(ActionEvent event) throws IOException {
			//load table view that present the late and lost book by member
			MemberCardGUI.memberIDHistory=MemberIDLabel.getText();
			MemberCardGUI.memberFirstName=FirstNameLabel.getText();
			MemberCardGUI.memberLastName=LastNameLabel.getText();
			Parent parent=FXMLLoader.load(getClass().getResource("/GUI/DelayandLostTableView.fxml"));
			Scene scene=new Scene(parent);
			Stage stage=new Stage();
			stage.setScene(scene);
			stage.setMaxHeight(571);
			stage.setMinHeight(571);
			stage.setMinWidth(904);
			stage.setMaxWidth(904);
			stage.show();
	    }
	    
	    /**
		 * This method set the variable of the GuiInterface in the client to this.
		 * Set properties to the specific columns of the table view.
		 * Set the columns to the table view and add a click event when a row from the table view is pressed on.
		 * The method give the user the functionality to press on some reader card and get his details.
		 */
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
		
		/**
		 * This private class implements ChangeListener overrides the changed method to fulfill the functionality of pressing a row
		 * in the table view and show the card reader's details in the specific labels.
		 */
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

		/**
		* Not used method(must implement because the implementation of GuiInterface)
		*/
		@Override
		public void showSuccess(String string) {
			// TODO Auto-generated method stub	
		}

		/**
		 *  This method puts inside the table view all the card readers that return from the search after initialize method is called.
		 * @param obj - ArrayList with the relevant data for create this window and maneuver through the different methods (all the information that needed).
		 */
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

		/**
		* Not used method(must implement because the implementation of GuiInterface)
		*/
		@Override
		public void showFailed(String message) {
			// TODO Auto-generated method stub
		}

		/**
		* Not used method(must implement because the implementation of GuiInterface)
		*/
		@Override
		public void freshStart() {
			// TODO Auto-generated method stub
		}
		
		/**
		 * This method call to a function from CommonController that get the members for the table view.
		 */
		 private void getMembers() {
			 CommonController.ShowReaderCards();
		  }
	    
	    
    
    
}
