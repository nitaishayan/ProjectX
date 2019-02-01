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

public class StatisticsReportsGUIActivity implements GuiInterface,Initializable{
	/**
	 * This class connect between the input in the GUI to the controller of Statistic Reports(Activity) process.
	 */
	
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.client.clientUI=this;
		setPickDateStart(StatisticReportsGUI_Manager.getStartDate());
		setPickDateEnd(StatisticReportsGUI_Manager.getEndDate());
		monthLabel.setText("Report for the requested time "+pickDateStart+" until - "+pickDateEnd);
		//StatisticReportsGUI_Manager.setEndDate(null);
		//StatisticReportsGUI_Manager.setStartDate(null);
		StatisticReportsController.getActivityReport(StatisticReportsGUI_Manager.getStartDate(),StatisticReportsGUI_Manager.getEndDate());
	}

	@Override
	public void showSuccess(String string) {
		Alert alert=new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(string);
		alert.showAndWait();		
	}

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
