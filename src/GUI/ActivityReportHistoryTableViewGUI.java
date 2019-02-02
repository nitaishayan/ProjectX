package GUI;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Common.ActivityReport;
import Common.DelayandLostDetails;
import Common.GuiInterface;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import logic.CommonController;
import logic.Main;
import logic.StatisticReportsController;
import javafx.scene.layout.AnchorPane;
/**
 * 
 * Class that show all the Activity reports that ever issued by the Librarian manager
 * 
 * the class show the details based on these parameters:
 * execution time,start time,end time,active members, freeze members,locked members,loan copies.
 * the results will appear on table view.
 * @author nitay shayan
 *
 */
public class ActivityReportHistoryTableViewGUI implements GuiInterface,Initializable{

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private TableView<ActivityReport> tableViewActivityReport;

    @FXML
    private TableColumn<ActivityReport,String> executionTime;

    @FXML
    private TableColumn<ActivityReport,String> startTime;

    @FXML
    private TableColumn<ActivityReport,String> endTime;

    @FXML
    private TableColumn<ActivityReport,String> activeMembers;

    @FXML
    private TableColumn<ActivityReport,String> freezeMembers;

    @FXML
    private TableColumn<ActivityReport,String> lockedMembers;

    @FXML
    private TableColumn<ActivityReport,String> loanCopies;
    
	/**
	 * 
	 *Initialize method that launch the request for the report to DB
	 * 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.client.clientUI=this;
    	StatisticReportsController.activityHistoryReport();
	}
	
	/**
	 * show success message based on the input string
	 * @param string 	the input string that contains the message
	 */
	@Override
	public void showSuccess(String string) {
		Alert alert=new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(string);
		alert.showAndWait();			
	}
	/**
	 * Display method that get from DB the object that contains all the parameters
	 * the parameters
	 * execution time,start time,end time,active members, freeze members,locked members,loan copies.
	 * This method also set the values and sizes of the table columns and eventually display on the screen
	 */
	@Override
	public void display(Object obj) {
		int numberOfColumns=8;
		int nonRelevantString=1;
		ArrayList<String> dataList = (ArrayList<String>)obj;
		System.out.println(dataList.toString()+"in Activity Report GUI");
			if (dataList.get(1).equals("NotExist")) {
				showFailed("No reports issued yet.");
			}
			else {
				
				tableViewActivityReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //resize the columns to the table view

				tableViewActivityReport.getColumns().setAll(executionTime,startTime,endTime,activeMembers,freezeMembers,lockedMembers,loanCopies);//attach the columns to the table view (personTable)

				executionTime.setCellValueFactory(new PropertyValueFactory<ActivityReport,String>("executionTime"));
				startTime.setCellValueFactory(new PropertyValueFactory<ActivityReport,String>("startTime"));
				endTime.setCellValueFactory(new PropertyValueFactory<ActivityReport,String>("endTime"));
				activeMembers.setCellValueFactory(new PropertyValueFactory<ActivityReport,String>("activeMembers"));
				freezeMembers.setCellValueFactory(new PropertyValueFactory<ActivityReport,String>("freezeMembers"));
				lockedMembers.setCellValueFactory(new PropertyValueFactory<ActivityReport,String>("lockedMembers"));
				loanCopies.setCellValueFactory(new PropertyValueFactory<ActivityReport,String>("loanCopies"));
				
				//set size
				CommonController.setColumnWidth(executionTime, 234, 234, 234);
				CommonController.setColumnWidth(startTime, 170, 170, 170);
				CommonController.setColumnWidth(endTime, 170, 170, 170);
				CommonController.setColumnWidth(activeMembers, 170, 170, 170);
				CommonController.setColumnWidth(freezeMembers, 170, 170, 170);
				CommonController.setColumnWidth(lockedMembers, 170, 170, 170);
				CommonController.setColumnWidth(loanCopies, 300, 300, 300);

				ObservableList<ActivityReport> ActivityReportDetails=FXCollections.observableArrayList();
				int loanRowSize = (dataList.size()-nonRelevantString)/numberOfColumns;
				int rowCounter=0, arrayJump=1;
				 ActivityReport tableTemp;
				 while(rowCounter<loanRowSize) {				 
						 tableTemp = new ActivityReport(dataList.get(arrayJump), dataList.get(arrayJump+1),dataList.get(arrayJump+2),dataList.get(arrayJump+3),dataList.get(arrayJump+4),dataList.get(arrayJump+5),dataList.get(arrayJump+6)+" / "+dataList.get(arrayJump+7),dataList.get(arrayJump+7));//create a new object ActivityReport
						 System.out.println(tableTemp.getMemberDelay());
					 rowCounter++;
					 arrayJump+=8;
					 ActivityReportDetails.add(tableTemp);
					 
					 Platform.runLater(() -> {	
						 tableViewActivityReport.setOnMouseClicked(new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent event) {
									if (tableViewActivityReport.getSelectionModel().getSelectedItem()==null)
										return;
								}
							});
						});
				 }
				 tableViewActivityReport.setItems(ActivityReportDetails);
			}			
		}
	/**
	 * show failed message based on the input string
	 * @param string 	the input string that contains the message
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
		// TODO Auto-generated method stub
		
	}

}
