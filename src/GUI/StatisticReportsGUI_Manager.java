package GUI;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import Common.GuiInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert.AlertType;
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
/**
 * 
 * @param event 
 * @throws IOException
 */
    @FXML
    void ActivityScreen(ActionEvent event) throws IOException {
    	if (pickDateStart.getValue()==null||pickDateEnd.getValue()==null) {
			showFailed("Please enter two dates");
		}
    	else if (pickDateStart.getValue().isAfter(pickDateEnd.getValue())) {
			showFailed("Wrong input, please enter valid period");

		} 
    	else{
        	startDate=pickDateStart.getValue().toString();
        	endDate=pickDateEnd.getValue().toString();
        	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticsReports-Activity.fxml"));
        	StatisticsPane.getChildren().setAll(pane);	
    	}
    }

    /**
	 * This method move the user to the StatisticsReports-EmployeeRecords screen.
	 * @param event - event from press on "Employee Records" button.
	 * @throws IOException
	 */
    @FXML
    void EmployeeRecordsScreen(ActionEvent event) throws IOException {
    	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticsReports-EmployeeRecords.fxml"));
    	StatisticsPane.getChildren().setAll(pane);
    }

    /**
	 * This method move the user to the StatisticsReports-LateInReturn screen.
	 * @param event - event from press on "Late On Return" button.
	 * @throws IOException
	 */
    @FXML
    void LateOnReturnScreen(ActionEvent event) throws IOException {
    	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticsReports-LateInReturn.fxml"));
    	StatisticsPane.getChildren().setAll(pane);
    }

    /**
   	 * This method move the user to the StatisticReports-Loans screen.
   	 * @param event - event from press on "Loans" button.
   	 * @throws IOException
   	 */
    @FXML
    void LoansScreen(ActionEvent event) throws IOException {
    	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticReports-Loans.fxml"));
    	StatisticsPane.getChildren().setAll(pane);
    }

    /**
   	 * This method move the user to the StatisticReports-ReaderCards screen.
   	 * @param event - event from press on "Reader Cards" button.
   	 * @throws IOException
   	 */
    @FXML
    void ReaderCardsScreen(ActionEvent event) throws IOException {
    	AnchorPane pane=FXMLLoader.load(getClass().getResource("/GUI/StatisticReports-ReaderCards.fxml"));
    	StatisticsPane.getChildren().setAll(pane);
    }

    /**
	* Not used method(must implement because the implementation of GuiInterface)
	*/
	@Override
	public void showSuccess(String string) {
		// TODO Auto-generated method stub
		
	}

	/**
	* Not used method(must implement because the implementation of GuiInterface)
	*/
	@Override
	public void display(Object obj) {
		// TODO Auto-generated method stub
	}

	/**
	* Not used method(must implement because the implementation of GuiInterface)
	*/
	@Override
	public void showFailed(String message) {
		freshStart();
		Alert alert=new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(message);
		alert.showAndWait();		
	}

	/**
	* Not used method(must implement because the implementation of GuiInterface)
	*/
	@Override
	public void freshStart() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This method set the variable of the GuiInterface in the client to this.
	 */
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
