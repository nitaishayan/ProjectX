package GUI;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Common.DelayandLostDetails;
import Common.GuiInterface;
import Common.LoanDetails;
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
import javafx.scene.layout.AnchorPane;
import logic.CommonController;
import logic.Main;

public class DelayandLostDetailsTableViewGUI implements Initializable,GuiInterface{

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private Label memberDetails;

    @FXML
    private TableView<DelayandLostDetails> TableViewLostorDelayed;

    @FXML
    private TableColumn<DelayandLostDetails,String> CopyName;

    @FXML
    private TableColumn<DelayandLostDetails,String> CopyID;

    @FXML
    private TableColumn<DelayandLostDetails,String> c;

	private String memberID;

	@Override
	public void showSuccess(String string) {
		Alert alert=new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(string);
		alert.showAndWait();			
	}

	@Override
	public void display(Object obj) {
		int numberOfColumns=3;
		int nonRelevantString=1;
		ArrayList<String> dataList = (ArrayList<String>)obj;
		System.out.println(dataList.toString()+"in delayLostGUI");
		if (dataList.get(0).equals("SearchMember")) {
			showSuccess("The member "+memberDetails.getText()+" details updated successfully");
		}
		else {
			//Naming the columns
			CopyName = new TableColumn<>("Copy name");
			CopyID = new TableColumn<>("Copy ID");
			c = new TableColumn<>("Lost or delay");
			if (dataList.get(1).equals("NotExist")) {
				showFailed("The member "+memberDetails.getText()+ " did not lost or returned on delay any book. Yay!");
			}
			else {
				
				TableViewLostorDelayed.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //resize the columns to the table view


				//set up size
				CopyName.setMinWidth(100);
				CopyID.setMinWidth(100);
				c.setMinWidth(100);
				//set up order descending
				CopyName.setSortType(TableColumn.SortType.DESCENDING);
				CopyID.setSortType(TableColumn.SortType.DESCENDING);
				c.setSortType(TableColumn.SortType.DESCENDING);
				//Set up the fields that will be attached to the table view
				TableViewLostorDelayed.getColumns().setAll(CopyName,CopyID,c);//attach the columns to the table view (personTable)
				CopyName.setCellValueFactory(new PropertyValueFactory<DelayandLostDetails,String>("CopyName"));
				CopyID.setCellValueFactory(new PropertyValueFactory<DelayandLostDetails,String>("copyID"));
				c.setCellValueFactory(new PropertyValueFactory<DelayandLostDetails,String>("Delay"));

				
				ObservableList<DelayandLostDetails> DelayandLostDetails=FXCollections.observableArrayList();
				int loanRowSize = (dataList.size()-nonRelevantString)/numberOfColumns;
				int rowCounter=0, arrayJump=1;
				 DelayandLostDetails tableTemp;
				 while(rowCounter<loanRowSize) {				 
						 tableTemp = new DelayandLostDetails(dataList.get(arrayJump+2), dataList.get(arrayJump),dataList.get(arrayJump+1));//create a new object by DelayandLostDetails
						 System.out.println(tableTemp.getCopyName()+" "+tableTemp.getCopyID()+" "+tableTemp.getDelay());
						 
					 //j+2 Book name ; //j CopyID ; //J+1 Delay or lost
					 rowCounter++;
					 arrayJump+=3;
					 DelayandLostDetails.add(tableTemp);
				 }
				 TableViewLostorDelayed.setItems(DelayandLostDetails);
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
		CommonController.getDelayandLostBooks(getMemberID());
	}

	public String getMemberID() {
		return memberID;
	}

	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}

}

