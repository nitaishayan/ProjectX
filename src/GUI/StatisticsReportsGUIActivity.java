package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Common.GuiInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import logic.Main;
import logic.StatisticReportsController;
import sun.awt.datatransfer.DataTransferer;
/**
 * Class that present the history of activity report that issed by the librarian manager
 * all the report are saved in the DB and as requested - show them in table view
 * 
 * @author nitay shayan
 *
 */
public class StatisticsReportsGUIActivity implements GuiInterface,Initializable{
	
	private String pickDateStart;
	private String pickDateEnd;

    @FXML
    private AnchorPane anchorStatistics;
    @FXML
    private Label monthLabel;

    @FXML
    private Label freezeLabel;

    @FXML
    private Label nonActiveLabel;

    @FXML
    private Label numOfCopies;

    @FXML
    private Label delayMembers;

    @FXML
    private Button back;

    @FXML
    private Label activeLabel;
    /**
     * Method that return back to priginal screen 
     * @param event
     * @throws IOException
     */
    @FXML
    void backScreen(ActionEvent event) throws IOException {
     	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticReports.fxml"));
     	anchorStatistics.getChildren().setAll(pane);
    }
	public String getPickDateEnd() {
		return pickDateEnd;
	}

	public void setPickDateEnd(String pickDateEnd) {
		this.pickDateEnd = pickDateEnd;
	}

	public String getPickDateStart() {
		return pickDateStart;
	}

	public void setPickDateStart(String pickDateStart) {
		this.pickDateStart = pickDateStart;
	}
/**
 * Method that activate actions before the load of the page
 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.client.clientUI=this;
		setPickDateStart(StatisticReportsGUI_Manager.getStartDate());
		setPickDateEnd(StatisticReportsGUI_Manager.getEndDate());
		monthLabel.setText("Report for the requested time "+pickDateStart+" until - "+pickDateEnd);
		StatisticReportsController.getActivityReport(StatisticReportsGUI_Manager.getStartDate(),StatisticReportsGUI_Manager.getEndDate());
	}
/**
 * Method that print success based on input String
 * 
 */
	@Override
	public void showSuccess(String string) {
		Alert alert=new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(string);
		alert.showAndWait();		
	}
/**
 * Method that display the values as they receieved from DB in obj
 * @param obj that received from DB
 */
	@Override
	public void display(Object obj) {
		ArrayList<String>data=(ArrayList<String>) obj;
		activeLabel.setText(data.get(1));
		freezeLabel.setText(data.get(2));
		nonActiveLabel.setText(data.get(3));
		numOfCopies.setText(data.get(4));
		delayMembers.setText(data.get(5));
	}

	@Override
	public void showFailed(String message) {
		freshStart();
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
