package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Common.GuiInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import logic.Main;

public class StatisticReportsGUI_Manager implements Initializable, GuiInterface{
	private static String startDate;
	private static String endDate;

    @FXML
    private AnchorPane StatisticsPane;
	
    @FXML
    private Button btnReaderCards_statistics;

    @FXML
    private Button btnLoans_statistics;

    @FXML
    private Button btnEmployeeRecords_statistics;

    @FXML
    private Button btnActivity_statistics;

    @FXML
    private Button btnLateOnReturn_statistics;
    
    @FXML
    private DatePicker pickDateStart;

    @FXML
    private DatePicker pickDateEnd;

    @FXML
    void ActivityScreen(ActionEvent event) throws IOException {
    	startDate=pickDateStart.getValue().toString();
    	endDate=pickDateEnd.getValue().toString();
    	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticsReports-Activity.fxml"));
    	StatisticsPane.getChildren().setAll(pane);
    }

    @FXML
    void EmployeeRecordsScreen(ActionEvent event) throws IOException {
    	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticsReports-EmployeeRecords.fxml"));
    	StatisticsPane.getChildren().setAll(pane);
    }

    @FXML
    void LateOnReturnScreen(ActionEvent event) throws IOException {
    	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticsReports-LateInReturn.fxml"));
    	StatisticsPane.getChildren().setAll(pane);
    }

    @FXML
    void LoansScreen(ActionEvent event) throws IOException {
    	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticReports-Loans.fxml"));
    	StatisticsPane.getChildren().setAll(pane);
    }

    @FXML
    void ReaderCardsScreen(ActionEvent event) throws IOException {
    	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticReports-ReaderCards.fxml"));
    	StatisticsPane.getChildren().setAll(pane);
    }

	@Override
	public void showSuccess(String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void display(Object obj) {
		// TODO Auto-generated method stub
	}

	@Override
	public void showFailed(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void freshStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Main.client.clientUI=this;
	}

	public static String getEndDate() {
		return endDate;
	}

	public static void setEndDate(String endDate) {
		StatisticReportsGUI_Manager.endDate = endDate;
	}

	public static String getStartDate() {
		return startDate;
	}

	public static void setStartDate(String startDate) {
		StatisticReportsGUI_Manager.startDate = startDate;
	}
	
}
