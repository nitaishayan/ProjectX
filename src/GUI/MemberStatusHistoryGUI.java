package GUI;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Common.DelayandLostDetails;
import Common.GuiInterface;
import Common.MemberStatusDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import logic.CommonController;
import logic.Main;
import logic.MemberCardController;

/**
 * Class that present the data of all the status changes that the specific member had 
 * during his time in the library
 * the presentation of the details is shown by a table view with a fxml page that call MemberStatusHistory.fxml
 * @author nitay shayan
 *
 */
public class MemberStatusHistoryGUI implements Initializable,GuiInterface{
	private String memberID;
    @FXML
    private TableView<MemberStatusDetails> memberStatusHistory;

    @FXML
    private TableColumn<MemberStatusDetails, String> executionDate;

    @FXML
    private TableColumn<MemberStatusDetails, String> previousStatus;

    @FXML
    private TableColumn<MemberStatusDetails, String> currentStatus;

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
	 * 
	 * j is the current counter for each iteration
	 * j+2 current Status ; //j previous Status   ; //J+1 execution Date
	 * 
	 * the method insert the values to the table view 
	 */
	@Override
	public void display(Object obj) {
		int numberOfColumns=3;
		int nonRelevantString=1;
		ArrayList<String> dataList = (ArrayList<String>)obj;
		System.out.println(dataList.toString()+"in MemberStatusHistoryGUI");
		if (dataList.get(0).equals("SearchMember")) {
			showSuccess("The member "+memberDetails.getText()+" details updated successfully");
		}
		else {
			//Naming the columns
			currentStatus = new TableColumn<>("Current status");
			previousStatus = new TableColumn<>("Previous status");
			executionDate = new TableColumn<>("Execution date");
			if (dataList.get(1).equals("NotExist")) {
				showFailed("The member "+memberDetails.getText()+ " has no status changes yet");
			}
			else {
				
				memberStatusHistory.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //resize the columns to the table view


				//set up size
				currentStatus.setMinWidth(100);
				previousStatus.setMinWidth(100);
				executionDate.setMinWidth(100);
				//set up order descending
				currentStatus.setSortType(TableColumn.SortType.DESCENDING);
				previousStatus.setSortType(TableColumn.SortType.DESCENDING);
				executionDate.setSortType(TableColumn.SortType.DESCENDING);
				//Set up the fields that will be attached to the table view
				memberStatusHistory.getColumns().setAll(currentStatus,previousStatus,executionDate);//attach the columns to the table view (personTable)
				currentStatus.setCellValueFactory(new PropertyValueFactory<MemberStatusDetails,String>("currentStatus"));
				previousStatus.setCellValueFactory(new PropertyValueFactory<MemberStatusDetails,String>("previousStatus"));
				executionDate.setCellValueFactory(new PropertyValueFactory<MemberStatusDetails,String>("executionDate"));

				
				ObservableList<MemberStatusDetails> DelayandLostDetails=FXCollections.observableArrayList();
				int loanRowSize = (dataList.size()-nonRelevantString)/numberOfColumns;
				int rowCounter=0, arrayJump=1;
				MemberStatusDetails tableTemp;
				 while(rowCounter<loanRowSize) {				 
						 tableTemp = new MemberStatusDetails(dataList.get(arrayJump+2), dataList.get(arrayJump),dataList.get(arrayJump+1));//create a new object by MemberStatusDetails
						 System.out.println(tableTemp.getExecutionDate()+" "+tableTemp.getPreviousStatus()+" "+tableTemp.getCurrentStatus());
						 
					 //j+2 current Status ; //j previous Status   ; //J+1 execution Date
					 rowCounter++;
					 arrayJump+=3;
					 DelayandLostDetails.add(tableTemp);
				 }
				 memberStatusHistory.setItems(DelayandLostDetails);
			}			
		}
			
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
			setMemberID(MemberCardGUI.memberIDHistory);
			memberDetails.setText(MemberCardGUI.memberFirstName+" "+MemberCardGUI.memberLastName);
			MemberCardGUI.memberIDHistory=null;
			MemberCardGUI.memberFirstName=null;
			MemberCardGUI.memberLastName=null;
		}
		else {
			setMemberID(Main.client.arrayUser.get(0));//get ID by arrayUser
			memberDetails.setText(Main.client.arrayUser.get(2)+" "+Main.client.arrayUser.get(3));
		}
		/**
		 * launch the query to DB
		 */
		System.out.println("now in MemberStatusHistoryGUI");
    	MemberCardController.getStatusHistory(getMemberID());
	}

	public String getMemberID() {
		return memberID;
	}

	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}

}

